/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack.collision.out;

import com.noahkurrack.collision.data.Config;
import com.noahkurrack.collision.data.Collision;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private static boolean simple;
    private File output;

    public FileManager(int threadId) {
        simple = Config.SIMPLE_FILE;
        if (simple) {
            output = new File("out/collisions-" + threadId + ".txt");
        } else {
            output = new File("out/collisions-" + threadId + ".json");
        }
    }

    public void writeToFile(Collision collision) {
        //serialize data to json, output to file
        JSONParser jsonParser = new JSONParser();
        JSONObject json;
        JSONArray collisionsArray;
        try {
            if (output.isFile() && output.canRead()) {
                json = (JSONObject) jsonParser.parse(new FileReader(output));
                collisionsArray = (JSONArray) json.get("collisions");
            } else {
                json = new JSONObject();
                collisionsArray = new JSONArray();
            }
            JSONObject currentCollisionJ = new JSONObject();
            currentCollisionJ.put("collision-id", collisionsArray.size() + 1);
            currentCollisionJ.put("match-attempts", (collision.getMatchAttempts() + 1));
            JSONArray locs = new JSONArray();
            locs.add((collision.getMatchAttempts() + 1));
            locs.add((collision.getLoc2() + 1));
            currentCollisionJ.put("locations", locs);
            JSONArray strings = new JSONArray();
            strings.add(collision.getInput());
            strings.add(collision.getInput2());
            currentCollisionJ.put("strings", strings);
            currentCollisionJ.put("hash", collision.getHash());
            currentCollisionJ.put("time", collision.getTime());
            collisionsArray.add(currentCollisionJ);

            json.put("collisions", collisionsArray);

            BufferedWriter writer = new BufferedWriter(new FileWriter(output, false));
            writer.write(json.toJSONString());
            writer.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void processFiles() throws IOException, ParseException {
        //combine collision files
        System.out.println("\nProcessing concurrent files...");

        ArrayList<File> files = new ArrayList<>();

        File folder = new File("./out");
        File[] listOfFiles = folder.listFiles();

        for (File file : (listOfFiles != null) ? listOfFiles : new File[0]) {
            if (file.isFile() && file.getName().contains("collisions-")) {
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

        for (File currentFile : files) {
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
        for (File currentFile : files) {
            currentFile.delete();
        }
    }

    public void writeToFileSimple(Collision collision) {
        /* TODO: output plain text per thread
        System.out.println("Collided hash:\t" + collision.getInput() + " --> " + collision.getHash() + "\n\t\t\t\t" +
                collision.getInput2() + " --> " + collision.getHash2());

        System.out.println("Number of attempts:\t" + (collision.getMatchAttempts() + 1) + ", (First occurrence: " +
                (collision.getLoc2() + 1) + ")");

        System.out.println("Time elapsed: " + collision.getTime() + " milliseconds\n------------------------\n");
        */
    }

    public static void processFilesSimple() throws IOException, ParseException {
        //TODO: append text files
    }
}