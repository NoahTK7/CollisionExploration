/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack.collision;

import com.noahkurrack.collision.data.Collision;
import com.noahkurrack.collision.data.Config;
import com.noahkurrack.collision.data.StringPair;
import com.noahkurrack.collision.out.FileManager;
import com.noahkurrack.collision.out.Output;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.CRC32;

public class CollisionFinder {

    //utility
    public static void main(String[] args) {
        CollisionFinder collisionFinder = new CollisionFinder(1);
        collisionFinder.findCollisions();
    }

    //list of attempted inputs (strings) and their respective outputs (hashes)
    //private ArrayList<ResultPair> results;

    private final Map<Long, StringPair> resultsMap;

    private FileManager fileManager;
    private CRC32 hasher;

    private int threadId;

    public CollisionFinder(int threadId) {
        //creates object that contains algorithm, takes input to produce hashes
        hasher = new CRC32();

        //this.results = new ArrayList<>();
        this.resultsMap = Collections.synchronizedMap(new LinkedHashMap<>());
        this.threadId = threadId;
        this.fileManager = new FileManager(threadId);
    }

    //begin meaningful code execution
    void findCollisions() {
        //starts timer
        long startTime = System.nanoTime();

        //add one default hash (0) to array of outputs
        //results.add(new ResultPair("", hasher.getValue()));

        resultsMap.clear();

        //loop until collision found, max 2^32-1 (same amount of possible hashes)
        for (int i = 0; i < Integer.MAX_VALUE; i++) {

            //generates random string (random sequence of characters) to input to hashing algorithm (length of 8 characters)
            String currentString = randomStringGenerator();

            //reset hashing algorithm
            hasher.reset();

            //inputs random string into CRC32 algorithm
            hasher.update(currentString.getBytes());

            //gets output of CRC32 algorithm (the hash)
            long hash = hasher.getValue();

            //displays current progress to user
            if (Config.VERBOSE) {
                Output.submit(threadId, String.valueOf(hash)+"--"+String.valueOf(i+1));
            }

            StringPair prevVal = null;

            synchronized (resultsMap) {
                if (!resultsMap.isEmpty()) {
                    prevVal = resultsMap.get(hash);
                }
            }

            if (prevVal == null) {
                synchronized (resultsMap) {
                    resultsMap.put(hash, new StringPair(currentString, i));
                }
            } else {
                //collision
                //System.out.println("Collision...!");

                //executes when collision found
                Collision currentCollision = new Collision(new StringPair(currentString, i), hash);

                //stops timer
                long stopTime = System.nanoTime();
                currentCollision.setTime(startTime, stopTime);

                currentCollision.setLoc2(prevVal.getIndex());
                currentCollision.setInput2(prevVal.getString());

                //confirms outputs match for both inputs
                hasher.reset();
                hasher.update(prevVal.getString().getBytes());
                long confirmHash = hasher.getValue();
                currentCollision.setHash2(confirmHash);

                if (hash == confirmHash) {
                    currentCollision.setConfirmed(true);
                }

                //file manager
                if (Config.SIMPLE_FILE) {
                    fileManager.writeToFileSimple(currentCollision);
                } else {
                    //serialize data to json, output to file
                    fileManager.writeToFile(currentCollision);
                }

                resultsMap.clear();

                //exit program
                break;
            }
        }

        //System.out.println("10");
    }

    //possible characters used when generating random strings
    //these characters used to avoid the occurrence of the same string being generated twice
    //  62 characters means 62^8 = 218,340,105,584,896 possible combinations
    //  nearly 0 chance (hundreds of decimals places of 0s) of generating the same sting twice using the same birthday paradox formula
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    //returns a string of random characters of a specified length
    //from https://dzone.com/articles/generate-random-alpha-numeric
    private static String randomStringGenerator() {
        int size = 8;
        StringBuilder builder = new StringBuilder();
        while (size-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();

    }

    //***
    /*
    //checks if collision has occurred (i.e. if output has been produced before)
    //otherwise, inserts input and output into their respective lists
    private boolean insertInOrder(ResultPair resultPair) {
        for (int i = 0; i < results.size(); i++) {
            if (resultPair.getHash() == (results.get(i).getHash())) {
                //collision found
                return false;
            }
            if (resultPair.getHash() < results.get(i).getHash()) {
                results.add(i, resultPair);
                return true;
            }
        }
        results.add(resultPair);
        return true;
    }
    */
    //***

    void reset() {
        resultsMap.clear();
    }
}