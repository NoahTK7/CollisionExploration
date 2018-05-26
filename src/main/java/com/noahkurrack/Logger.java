/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

class Logger {

    static void logCollision(Collision collision) {

        if (Config.VERBOSE) {
            if (collision.isConfirmed()) {

                System.out.println("Collided hash:\t" + collision.getInput() + " --> " + collision.getHash() + "\n\t\t\t\t" +
                        collision.getInput2() + " --> " + collision.getHash());

                System.out.println("Number of attempts:\t" + (collision.getMatchAttempts() + 1) + ", (First occurrence: " +
                        (collision.getLoc2() + 1) + ")");

                System.out.println("Time elapsed: " + collision.getTime() + " milliseconds\n------------------------");

            } else {
                warn("Collision unconfirmed.");
            }
        }
    }

    private static void warn(String warning) {

        System.out.println("[Warning] " + warning);

    }

}