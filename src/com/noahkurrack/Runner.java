/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

public class Runner {

    public static void main(String[] args) {

        //run collision finder several times (data recorded to text file)
        for (int i = 0; i < 5; i++) {
            CollisionFinder.findCollisions();
        }

    }

}