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
import java.util.Collections;
import java.util.zip.CRC32;

public class CollisionFinder {

    //utility
    public static void main(String[] args) {
        findCollisions(true);
    }

    //lists of attempted inputs (strings) and their outputs (hashes)
    private static ArrayList<String> strings = new ArrayList<>();
    private static ArrayList<Long> hashes = new ArrayList<>();

    private static int ID = 0;

    private static BufferedWriter writer;

    //begin meaningful code execution
    static void findCollisions(boolean debug) {
        //starts timer
        long startTime = System.nanoTime();
        ID++;

        //creates object that contains algorithm, takes input to produce hashes
        CRC32 hasher = new CRC32();

        //add one default value (0) to array of outputs
        hashes.add(hasher.getValue());

        //loop until collision found, max 2^32-1 (same amount of possible hashes)
        for (int i = 0; i < Integer.MAX_VALUE; i++) {

            //generates random string (random sequence of characters) to input to hashing algorithm (length of 8 characters)
            String currentString = randomStringGenerator();

            //inputs random string into CRC32 algorithm
            hasher.update(currentString.getBytes());

            //gets output of CRC32 algorithm (the hash)
            long hash = hasher.getValue();

            //displays current progress to user
            if (debug) {
                System.out.print("\rCurrent hash: " + hash + "\t(hash #" + (i + 1) + ")");
            }

            //resets algorithm for next hash
            hasher.reset();

            //checks if collision found
            //if no collision, code attempts again starting at line 27
            if (!insertInOrder(hash, currentString)) {

                //executes when collision found

                //stops timer
                long stopTime = System.nanoTime();

                //finds first string that produces same hash as current string
                //only executes after collision found (that means there is always two inputs that produce the given output, this code finds the first one)
                int otherIndex = Collections.binarySearch(hashes, hash);
                String firstString = strings.get(otherIndex);

                //confirms outputs match for both inputs
                hasher.reset();
                hasher.update(firstString.getBytes());
                long confirmHash = hasher.getValue();

                //displays information about the collision to record in a spreadsheet
                System.out.println("\n------------------------\nCollision!");
                System.out.println("Collided hash:\t" + currentString + " --> " + hash + "\n\t\t\t\t" + firstString + " --> " + confirmHash);
                System.out.println("Number of attempts:\t" + (i + 1) + ", (First occurrence: " + (otherIndex + 1) + ")");
                System.out.println("Time elapsed: " + ((stopTime - startTime) / 1000000) + " milliseconds");

                JSONParser jsonParser = new JSONParser();
                JSONObject json;
                JSONArray collisionsArray;
                try {
                    File output = new File("collisions.json");
                    if (output.isFile() && output.canRead()) {
                        json = (JSONObject) jsonParser.parse(new FileReader("collisions.json"));
                        collisionsArray = (JSONArray) json.get("collisions");
                    } else {
                        json = new JSONObject();
                        collisionsArray = new JSONArray();
                    }
                    JSONObject currentCollision = new JSONObject();
                    currentCollision.put("collision-id",ID);
                    currentCollision.put("match-attempts", (i+1));
                    JSONArray locs = new JSONArray();
                    locs.add((i+1));
                    locs.add((otherIndex+1));
                    currentCollision.put("locations", locs);
                    JSONArray strings = new JSONArray();
                    strings.add(currentString);
                    strings.add(firstString);
                    currentCollision.put("strings", strings);
                    currentCollision.put("hash", hash);
                    currentCollision.put("time", ((stopTime - startTime) / 1000000));
                    collisionsArray.add(currentCollision);

                    json.put("collisions", collisionsArray);

                    writer = new BufferedWriter(new FileWriter(output, false));
                    writer.write(json.toJSONString());
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                /*
                String out = "";
                out += "\n------------------------\nCollision!";
                out += "\nCollided hash:\t" + currentString + " --> " + hash + "\n\t\t\t\t" + firstString + " --> " + confirmHash;
                out += "\nNumber of attempts:\t" + (i + 1) + ", (First occurrence: " + (otherIndex + 1) + ")";
                out += "Time elapsed: " + ((stopTime - startTime) / 1000000) + " milliseconds";

                try {
                    writer = new BufferedWriter(new FileWriter("output.txt", true));
                    writer.append(out);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */

                //exit program
                break;
            }
        }
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

    //checks if collision has occurred (i.e. if output has been produced before)
    //otherwise, inserts input and output into their respective lists
    private static boolean insertInOrder(long element, String string) {
        for (int i = 0; i < hashes.size(); i++) {
            if (element == hashes.get(i)) {
                //collision found
                return false;
            }
            if (element < hashes.get(i)) {
                hashes.add(i, element);
                strings.add(i, string);
                return true;
            }
        }
        hashes.add(element);
        strings.add(string);
        return true;
    }
}