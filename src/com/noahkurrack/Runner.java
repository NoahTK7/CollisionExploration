/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Runner {

    public static void main(String[] args) {

        //set output of program to text file
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("output.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(out);

        //run collision finder several times (data recorded to text file)
        for (int i = 0; i < 2; i++) {
            CollisionFinder.findCollisions(false);
        }

    }

}
