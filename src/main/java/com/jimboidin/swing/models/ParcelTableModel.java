package com.jimboidin.swing.models;

import com.jimboidin.Parcel;
import com.jimboidin.exceptions.ParcelException;

import javax.swing.table.AbstractTableModel;
import java.util.List;


/**
 * A Custom TableModel that allows JTable to handle a List of
 * Parcel Objects.
 */
public class ParcelTableModel extends AbstractTableModel {
    private List<Parcel> data;
    private final String[] columnNames = { "Name", "Address", "Phone",
            "Company", "Color", "Arrived", "Collected"};
    private final String[] realNames = { "STUDENT_NAME", "ADDRESS", "PHONE",
            "PARCEL_COMPANY", "PARCEL_COLOR", "ARRIVAL_DATE", "COLLECTED_DATE"};

    public ParcelTableModel(List<Parcel> data){
        this.data = data;
    }
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).get(realNames[columnIndex]);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        String colName = getColumnName(columnIndex);
        if (colName.equals("Arrived") || colName.equals("Collected")){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        System.out.println("setValueAt called");
        Parcel parcel = data.get(rowIndex);
        parcel.set(realNames[columnIndex], aValue);
        try {
            Parcel.Connector.update(parcel);
        } catch (ParcelException e) {
            e.printStackTrace();
        }
        fireTableCellUpdated(rowIndex,columnIndex);
    }
}
