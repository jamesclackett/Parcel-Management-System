package com.jimboidin.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Maps a persistent Data Structure from an SQL ResultSet
 * Iterates through a given ResultSet and creates a list
 * of 'ResultItems' that can be handled as a single row of
 * table-data
 */
public class Result {
    private final List<ResultItem> itemList;

    Result(ResultSet resultSet) {
        itemList = new ArrayList<>();

        try {

            while (resultSet.next()){
                ResultItem resultItem = new ResultItem();
                ResultSetMetaData metaData = resultSet.getMetaData();

                int colCount = metaData.getColumnCount();
                for (int i = 1; i <= colCount; i++){
                    Object o = resultSet.getObject(i);
                    if (o != null && o.getClass() == Timestamp.class){
                        o = ((Timestamp) o).toLocalDateTime();
                    }
                    resultItem.put(metaData.getColumnName(i), o);
                }
                itemList.add(resultItem);

            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * A method to get the List of ResultItem(s) which
     * are contained in the current instance.
     *
     * @return this instance's list of ResultItem(s)
     */
    public List<ResultItem> getItems() {
        return itemList;
    }

    @Override
    public String toString() {
        int rowCount = itemList.size();
        Set<String> keySet = itemList.get(0).keySet();
        return super.toString() + "\n Columns: " + keySet + "\n Number of Rows: " + rowCount;
    }

    /**
     * A method to get the number of rows that were contained within
     * the ResultSet object.
     *
     * @return the size of this instance's list of ResultItem(s)
     */
    public int size(){
        return itemList.size();
    }
}

