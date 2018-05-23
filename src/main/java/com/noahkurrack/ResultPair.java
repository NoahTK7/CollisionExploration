/*
 * Copyright (c) 2018 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack;

public class ResultPair {

    private final String key;
    private final Long value;

    public ResultPair(String aKey, Long aValue)
    {
        key   = aKey;
        value = aValue;
    }

    public String key()   { return key; }
    public Long value() { return value; }

}
