/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack.collision;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import com.noahkurrack.collision.data.Config;
import com.noahkurrack.collision.out.FileManager;
import com.noahkurrack.collision.out.Output;
import org.json.simple.parser.ParseException;
import java.io.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Runner {

    @Parameter(names = {"--amount", "-a"}, description = "Number of times to run CollisionFinder [default: 2]")
    private int amount = Config.AMOUNT;

    @Parameter(names = {"--verbose", "-v"}, description = "Output progress to command line")
    private boolean verbose = Config.VERBOSE;

    @Parameter(names = {"--simplefile", "-sf"}, description = "Output plain text to file instead of json")
    private boolean noFile = Config.SIMPLE_FILE;

    private int threads;
    private static ExecutorService executor;

    public static void main(String[] args) {
        Runner runner = new Runner();
        JCommander.newBuilder()
                .addObject(runner)
                .build()
                .parse(args);
        runner.updateGlobalConfig();
        try {
            runner.startRun();
        } catch (InterruptedException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private void updateGlobalConfig() {
        Config.AMOUNT = this.amount;
        Config.VERBOSE = this.verbose;
        Config.SIMPLE_FILE = this.noFile;
    }

    private void startRun() throws InterruptedException, IOException, ParseException {
        int perThreadAmount;
        if (amount < threads) {
            perThreadAmount = amount;
            this.threads = 1;
            System.out.println("Running "+amount+" iterations in single thread...");
        } else {
            perThreadAmount = amount / threads;
            System.out.println("Running "+ perThreadAmount +" iterations per "+threads+" threads for a total of "+threads* perThreadAmount +" iterations...");
        }

        ArrayList<Future> futures = new ArrayList<>();
        for (int i = 0; i < this.threads; i++) {
            futures.add(executor.submit(new CollisionThread(i, perThreadAmount)));
        }

        executor.shutdown();

        if (verbose) {
            Output.init(futures, perThreadAmount);
        }

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

    public static ExecutorService getExecutor() {
        return executor;
    }
}