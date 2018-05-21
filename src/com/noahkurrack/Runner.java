/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

public class Runner {

    public static void main(String[] args) {

        int amount = 2;

        switch (args.length) {
            case 1:
                amount = Integer.valueOf(args[0]);
                break;
            default:
                System.out.println("Unknown number of arguments... continuing with default configuration.");
                break;
        }

        //run collision finder several times (data recorded to text file)
        CollisionFinder collisionFinder = new CollisionFinder();
        for (int i = 0; i < amount; i++) {
            collisionFinder.reset();
            collisionFinder.findCollisions();
        }

    }

}