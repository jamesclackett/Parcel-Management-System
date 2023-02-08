package com.jimboidin;

import com.jimboidin.database.Database;
import com.jimboidin.swing.panels.ParcelPanel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.List;


public class Main {
    public static void main(String[] args){
        List<Parcel> newList = Parcel.Connector.buildList(Database.SELECT_UNEXPIRED);
        List<Parcel> expiredList = Parcel.Connector.buildList(Database.SELECT_EXPIRED);
        List<Parcel> collectedList = Parcel.Connector.buildList(Database.SELECT_COLLECTED);
        List<Parcel> allList = Parcel.Connector.buildList(Database.SELECT_ALL);

        JFrame frame = new JFrame("Parcel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,500);

        ParcelPanel newPanel = new ParcelPanel(newList);
        ParcelPanel expiredPanel = new ParcelPanel(expiredList);
        ParcelPanel collectedPanel = new ParcelPanel(collectedList);
        ParcelPanel allPanel = new ParcelPanel(allList);

        TableColumn newColumn = newPanel.getTable().getColumn("Collected");
        newPanel.getTable().removeColumn(newColumn);
        TableColumn expiredColumn = expiredPanel.getTable().getColumn("Collected");
        expiredPanel.getTable().removeColumn(expiredColumn);

        expiredPanel.hideAddButton();
        allPanel.hideAddButton();
        collectedPanel.hideAddButton();

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.add("READY", newPanel);
        tabbedPane.add("EXPIRED", expiredPanel);
        tabbedPane.add("COLLECTED", collectedPanel);
        tabbedPane.add("ALL", allPanel);

        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            switch (index){
                case 0 -> newPanel.updateTableData(Parcel.Connector.buildList(Database.SELECT_UNEXPIRED));
                case 1 -> expiredPanel.updateTableData(Parcel.Connector.buildList(Database.SELECT_EXPIRED));
                case 2 -> collectedPanel.updateTableData(Parcel.Connector.buildList(Database.SELECT_COLLECTED));
                case 3 -> allPanel.updateTableData(Parcel.Connector.buildList(Database.SELECT_ALL));
            }
        });

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}