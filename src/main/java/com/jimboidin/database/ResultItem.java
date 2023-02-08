package com.jimboidin.database;

import java.util.HashMap;

/**
 * A simple extension of HashMap that represents a single row
 * of table-data created by Result.
 */
public class ResultItem extends HashMap<String, Object> {

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ResultSet:\n");

        for (String key : keySet()){
            stringBuilder.append(key).append(": ").append(get(key)).append("\n");
        }

        return stringBuilder.toString();
    }
}
