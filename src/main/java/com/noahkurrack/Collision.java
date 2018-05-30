/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

class Collision {

    private String input;
    private long hash;

    private long time;

    private int matchAttempts;
    private String input2;
    private long hash2;
    private int loc2;
    private boolean confirmed;

    Collision(ResultPair resultPair, int matchAttempts) {
        this.input = resultPair.getInput();
        this.hash = resultPair.getHash();
        this.matchAttempts = matchAttempts;

        this.time = -1;
        this.input2 = null;
        this.hash2= -1;
        this.loc2 = -1;
        this.confirmed = false;
    }

    void setTime(long start, long stop) {
        this.time = (stop - start) / 1000000;
    }

    void setInput2(String input2) {
        this.input2 = input2;
    }

    void setHash2(long hash2) {
        this.hash2 = hash2;
    }

    void setLoc2(int loc2) {
        this.loc2 = loc2;
    }

    void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    String getInput() {
        return input;
    }

    long getHash() {
        return hash;
    }

    long getTime() {
        return time;
    }

    int getMatchAttempts() {
        return matchAttempts;
    }

    String getInput2() {
        return input2;
    }

    long getHash2() {
        return hash2;
    }

    int getLoc2() {
        return loc2;
    }

    boolean isConfirmed() {
        return confirmed;
    }
}
