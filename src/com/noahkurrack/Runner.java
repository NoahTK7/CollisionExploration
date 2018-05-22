/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Runner {

    @Parameter(names = {"--amount", "-a"}, description = "Number of times to run CollisionFinder [default: 2]")
    int amount = 2;

    @Parameter(names = {"--verbose", "-v"}, description = "Output stats to command line [default: false]")
    boolean verbose = false;

    @Parameter(names = {"--file", "-f"}, description = "Output stats to file (collisions.json) [default: true]")
    boolean fileOut = true;

    public static void main(String[] args) {
        Runner runner = new Runner();
        JCommander.newBuilder()
                .addObject(runner)
                .build()
                .parse(args);
    }

    void run() {
        //run collision finder several times (data recorded to text file)
        CollisionFinder collisionFinder = new CollisionFinder(verbose, fileOut);
        for (int i = 0; i < amount; i++) {
            collisionFinder.reset();
            collisionFinder.findCollisions();
        }
    }

}