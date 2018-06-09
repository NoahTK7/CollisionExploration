/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack.collision.data;

public class Collision {

    private String input;
    private long hash;

    private long time;

    private int matchAttempts;
    private String input2;
    private long hash2;
    private int loc2;
    private boolean confirmed;

    public Collision(ResultPair resultPair, int matchAttempts) {
        this.input = resultPair.getInput();
        this.hash = resultPair.getHash();
        this.matchAttempts = matchAttempts;

        this.time = -1;
        this.input2 = null;
        this.hash2= -1;
        this.loc2 = -1;
        this.confirmed = false;
    }

    public void setTime(long start, long stop) {
        this.time = (stop - start) / 1000000;
    }

    public void setInput2(String input2) {
        this.input2 = input2;
    }

    public void setHash2(long hash2) {
        this.hash2 = hash2;
    }

    public void setLoc2(int loc2) {
        this.loc2 = loc2;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getInput() {
        return input;
    }

    public long getHash() {
        return hash;
    }

    public long getTime() {
        return time;
    }

    public int getMatchAttempts() {
        return matchAttempts;
    }

    public String getInput2() {
        return input2;
    }

    public long getHash2() {
        return hash2;
    }

    public int getLoc2() {
        return loc2;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
