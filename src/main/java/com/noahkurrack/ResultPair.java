/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

class ResultPair {

    private final String input;
    private final long hash;

    ResultPair(String input, long hash) {
        this.input = input;
        this.hash = hash;
    }

    String getInput() {
        return input;
    }
    long getHash() {
        return hash;
    }

}
