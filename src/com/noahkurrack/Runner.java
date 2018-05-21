/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

public class Runner {

    public static void main(String[] args) {

        //run collision finder several times (data recorded to text file)
        CollisionFinder collisionFinder = new CollisionFinder();
        for (int i = 0; i < 4; i++) {
            collisionFinder.reset();
            collisionFinder.findCollisions();
        }

    }

}