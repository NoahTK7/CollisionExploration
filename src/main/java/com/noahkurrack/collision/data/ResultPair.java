/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack.collision.data;

public class ResultPair {

    private final String input;
    private final long hash;

    public ResultPair(String input, long hash) {
        this.input = input;
        this.hash = hash;
    }

    public String getInput() {
        return input;
    }
    public long getHash() {
        return hash;
    }

}
