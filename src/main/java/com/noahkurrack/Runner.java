/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import org.json.simple.parser.ParseException;
import java.io.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Runner {

    @Parameter(names = {"--amount", "-a"}, description = "Number of times to run CollisionFinder [default: 2]")
    private int amount = 2;

    private int perThreadAmount;

    @Parameter(names = {"--verbose", "-v"}, description = "Output stats to command line [default: false]")
    private boolean verbose = false;

    @Parameter(names = {"--nofile", "-nf"}, description = "Output stats to file (collisions.json) [default: false]")
    private boolean noFile = false;

    private int threads;
    private ExecutorService executor;

    public static void main(String[] args) {
        Runner runner = new Runner();
        JCommander.newBuilder()
                .addObject(runner)
                .build()
                .parse(args);
        updateGlobalConfig(runner.amount, runner.verbose, runner.noFile);
        try {
            runner.startRun();
        } catch (InterruptedException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateGlobalConfig(int amount, boolean verbose, boolean noFile) {
        Config.AMOUNT = amount;
        Config.VERBOSE = verbose;
        Config.NO_FILE = noFile;
    }

    private void startRun() throws InterruptedException, IOException, ParseException {
        if (amount < threads) {
            this.perThreadAmount = amount;
            this.threads = 1;
            System.out.println("Running "+amount+" iterations in single thread.");
        } else {
            this.perThreadAmount = amount / threads;
            System.out.println("Running "+perThreadAmount+" iterations per "+threads+" threads for a total of "+threads*perThreadAmount+" iterations.");
        }
        for (int i = 0; i < this.threads; i++) {
            int finalI = i;
            executor.submit(() -> this.run(finalI));
        }
        executor.shutdown();
        while (true) {
            if(executor.awaitTermination(10, TimeUnit.SECONDS)) {
                break;
            }
        }
        FileManager.processFiles();
        System.out.println("Done... exiting.");
    }

    public Runner() {
        threads = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(threads);
    }

    private void run(int threadId) {
        //run collision finder several times (data recorded to json file)
        CollisionFinder collisionFinder = new CollisionFinder(threadId);
        for (int i = 1; i <= perThreadAmount; i++) {
            collisionFinder.reset();
            collisionFinder.findCollisions();
        }
    }
}