/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Runner {

    @Parameter(names = {"--amount", "-a"}, description = "Number of times to run CollisionFinder [default: 2]")
    private int amount = 2;

    private int perThreadAmount;

    @Parameter(names = {"--verbose", "-v"}, description = "Output stats to command line [default: false]")
    private boolean verbose = false;

    @Parameter(names = {"--file", "-f"}, description = "Output stats to file (collisions.json) [default: true]")
    private boolean fileOut = true;

    private int threads;
    private ExecutorService executor;

    public static void main(String[] args) {
        Runner runner = new Runner();
        JCommander.newBuilder()
                .addObject(runner)
                .build()
                .parse(args);
        try {
            runner.startRun();
        } catch (InterruptedException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private void startRun() throws InterruptedException, IOException, ParseException {
        //TODO: finalize logic
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
        processFiles();
        System.out.println("Done... exiting.");
    }

    public Runner() {
        threads = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(threads);
    }

    private void run(int threadId) {
        //run collision finder several times (data recorded to text file)
        CollisionFinder collisionFinder = new CollisionFinder(verbose, fileOut, threadId);
        for (int i = 1; i <= perThreadAmount; i++) {
            collisionFinder.reset();
            collisionFinder.findCollisions();
        }
    }

    private void processFiles() throws IOException, ParseException {
        //combine collision files
        System.out.println("Processing concurrent files...");

        ArrayList<File> files = new ArrayList<>();

        File folder = new File("./out");
        File[] listOfFiles = folder.listFiles();

        for (File file : (listOfFiles != null) ? listOfFiles : new File[0]) {
            if (file.isFile() && file.getName().contains(".json")) {
                files.add(file);
            }
        }

        File finalFile = new  File("out/collisions.json");
        JSONParser jsonParser = new JSONParser();

        JSONObject previousJson;
        JSONArray previousCollisions;

        JSONObject finalJson = new JSONObject();
        JSONArray finalArray = new JSONArray();

        if (finalFile.isFile() && finalFile.canRead()) {
            previousJson = (JSONObject) jsonParser.parse(new FileReader(finalFile));
            previousCollisions = (JSONArray) previousJson.get("collisions");
            finalArray.addAll(previousCollisions);
        }

        for (File currentFile:files) {
            JSONObject json = (JSONObject) jsonParser.parse(new FileReader(currentFile));
            JSONArray collisionsArray = (JSONArray) json.get("collisions");
            for (Object collision : collisionsArray) {
                collision = jsonParser.parse(collision.toString());
                ((JSONObject) collision).replace("collision-id", finalArray.size()+1);
                finalArray.add(collision);
            }
        }
        finalJson.put("collisions", finalArray);

        BufferedWriter writer = new BufferedWriter(new FileWriter(finalFile, false));
        writer.write(finalJson.toJSONString());
        writer.close();

        //delete partial files
        for (File currentFile:files) {
            currentFile.delete();
        }
    }
}