/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

import com.github.tomaslanger.chalk.Ansi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

class Output {

    private static ArrayList<String> collisionsStatus;
    private static ArrayList<Future> futures;
    private static HashMap<Integer, Integer> threadStatus;

    private static int running;
    private static int perThreadAmount;

    static void init(ArrayList<Future> futures, int perThread) {
        running = futures.size();
        perThreadAmount = perThread;
        collisionsStatus = new ArrayList<>();
        for (int i = 0; i < futures.size(); i++) {
            collisionsStatus.add("0--0");
        }
        threadStatus = new HashMap<>();
        for (int i = 0; i < futures.size(); i++) {
            threadStatus.put(i, 1);
        }
        Output.futures = futures;
        System.out.print("\n\n\n\n");
        run();
    }

    private static void run() {
        while (!Runner.getExecutor().isTerminated()) {
            StringBuilder output = new StringBuilder("\rCurrently running " + running + " threads...");
            running = futures.size();
            for (int i = 0; i < futures.size(); i++) {
                output.append("\n\r[Thread ").append(i+1).append("] ");
                if (futures.get(i).isDone()) {
                    running--;
                    output.append("Found hash ").append(perThreadAmount).append(" of ").append(perThreadAmount).append(". Done.");
                } else {
                    String[] stats = collisionsStatus.get(i).split("--");
                    output.append("Finding hash ").append(threadStatus.get(i)).append(" of ").append(perThreadAmount).append(". Current hash: ").append(stats[0]).append("       \t(hash #").append(stats[1]).append(")");
                }
            }
            String reset = Ansi.eraseLine()+Ansi.cursorUp()+Ansi.eraseLine()+Ansi.cursorUp()+Ansi.eraseLine()+Ansi.cursorUp()+Ansi.eraseLine()+Ansi.cursorUp();
            System.out.print(reset+output);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //TODO: log collisions after
    }

    static void submit(int id, String status) {
        collisionsStatus.set(id, status);
    }

    static void update(int id, int current) {
        threadStatus.replace(id, current);
    }
}