/*
 * Copyright (c) 2019 Noah Kurrack. All rights reserved.
 */

package com.noahkurrack.collision.data;

import java.util.Objects;

public class StringPair {

    private final String string;
    private final int index;

    public StringPair(String string, int index) {
        this.string = string;
        this.index = index;
    }

    public String getString() {
        return string;
    }
    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringPair that = (StringPair) o;
        return index == that.index &&
                string.equals(that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string, index);
    }
}