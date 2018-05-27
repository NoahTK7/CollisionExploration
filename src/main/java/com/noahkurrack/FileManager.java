/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

class FileManager {

    private File output;

    FileManager(int threadId) {
        output = new File("out/collisions-"+threadId+".json");
    }

    void writeToFile(Collision collision) {
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

    static void processFiles() throws IOException, ParseException {
        //combine collision files
        System.out.println("Processing concurrent files...");

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
}