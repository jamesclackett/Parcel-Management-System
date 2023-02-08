package com.jimboidin.swing.panels;

import com.jimboidin.database.Database;
import com.jimboidin.Parcel;
import com.jimboidin.exceptions.ParcelException;
import com.jimboidin.swing.dialog.DetailsPanel;
import com.jimboidin.swing.models.ParcelTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;


/**
 * A Panel that represents a given list of Parcels on a JTable,
 * and allows the user to manipulate this parcel data through
 * the table itself and other UI features.
 */
public class ParcelPanel extends JPanel {
    private JScrollPane scrollPane;
    private JTable table;
    ParcelTableModel tableModel;
    private List<Parcel> parcelList;
    JPanel panelUI;
    JButton addButton, searchButton;

    public ParcelPanel(List<Parcel> parcelList){
        super(new BorderLayout());
        this.parcelList = parcelList;
        initializeTable();
        initializeUI();
    }

    /**
     * Creates and initializes the JTable along with the necessary JPopupMenus
     */
    private void initializeTable(){
        tableModel = new ParcelTableModel(parcelList);
        table = new JTable(tableModel);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete record");
        deleteItem.addActionListener(e -> deleteRow(table.getSelectedRow()));
        JMenuItem collectedItem = new JMenuItem("Mark Collected");
        collectedItem.addActionListener(e -> markCollected(table.getSelectedRow()));
        popupMenu.add(deleteItem);
        popupMenu.add(collectedItem);

        JPopupMenu popupMenuCollected = new JPopupMenu();
        JMenuItem deleteItem2 = new JMenuItem("Delete record");
        deleteItem2.addActionListener(e -> deleteRow(table.getSelectedRow()));
        popupMenuCollected.add(deleteItem2);

        table.addMouseListener(new MouseAdapter() {
                                   @Override
                                   public void mousePressed(MouseEvent e) {
                                       int row = table.rowAtPoint(e.getPoint());
                                       table.setRowSelectionInterval(row,row);


                                       if (parcelList.get(row).getCollectedDate() == null){
                                           table.setComponentPopupMenu(popupMenu);
                                       }
                                       else {
                                           table.setComponentPopupMenu(popupMenuCollected);
                                       }

                                   }
                               });

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * @return this Panel's JTable
     */
    public JTable getTable(){
        return table;
    }

    /**
     * Initializes the non-table UI.
     */
    private void initializeUI() {
        panelUI = new JPanel(new FlowLayout());
        panelUI.setPreferredSize(new Dimension(50,200));
        add(panelUI,BorderLayout.EAST);
        showAddButton();
        showSearchButton();
    }

    public void showSearchButton() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/com.jimboidin/icons/search.png"));
        searchButton = new JButton(icon);
        searchButton.setFocusable(false);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Search name:");
                if (name != null) search(name);
            }
        });
        panelUI.add(searchButton);
    }

    public void hideSearchButton(){
        panelUI.remove(searchButton);
    }

    public void showAddButton()  {
        ImageIcon icon = new ImageIcon(getClass().getResource("/com.jimboidin/icons/create.png"));
        addButton = new JButton(icon);
        addButton.setFocusable(false);
        addButton.addActionListener(e -> createParcel());
        panelUI.add(addButton);
    }

    public void hideAddButton(){
        panelUI.remove(addButton);
    }

    /**
     * Searches the database for the given name.
     * @param name the student's name
     */
    private void search(String name) {
        List<Parcel> resultList = Parcel.Connector.search(name);
        processSearch(resultList);
    }

    /**
     * Processes the result of a database search to match the current Panel's
     * parcelList.
     * Since the Connector::search method makes a 'SELECT *' query to the database,
     * this method filters out those results which should not exist in the current
     * Panel (i.e. Collected results in Expired).
     *
     * @param result the search result to process
     */
    private void processSearch(List<Parcel> result){
        List<Parcel> filteredList = new ArrayList<>();

        for (Parcel p : result){
            for (Parcel p2 : parcelList){
                if (p.getId() == p2.getId()) {
                    filteredList.add(p);
                }
            }
        }
        updateTableData(filteredList);
    }

    /**
     * Opens a dialog that allows a user to insert information about a new
     * Parcel.
     * The new Parcel is sent to the database and the current Panels JTable
     * is updated.
     */
    private void createParcel() {
        DetailsPanel panel = new DetailsPanel();
        int option = JOptionPane.showConfirmDialog(null, panel,
                "Enter details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION){
            try {
                Parcel parcel = new Parcel(panel.getFields());
                Parcel.Connector.insert(parcel);
                updateTableData(Parcel.Connector.buildList(Database.SELECT_UNEXPIRED));

            } catch (ParcelException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User did not select OK. JOption: " + option);
        }
    }

    /**
     * Deletes the selected Parcel from the database and updates the current Panels
     * JTable.
     *
     * @param row the row to be deleted
     */
    private void deleteRow(int row){
        Parcel parcel = parcelList.get(row);
        try {
            Parcel.Connector.delete(parcel);
            parcelList.remove(parcel);
            updateTableData(parcelList);
        } catch (ParcelException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Marks the selected Parcel as now Collected.
     * Sends this information to the database
     * @param row the row that has been marked as collected
     */
    private void markCollected(int row){
        Parcel parcel = parcelList.get(row);
        LocalDateTime dateTime = LocalDateTime.now();
        parcel.setCollectedDate(dateTime);
        try {
            Parcel.Connector.update(parcel);
            parcelList.remove(parcel);
            updateTableData(parcelList);
        } catch (ParcelException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Re-initializes the JTable with the given List of Parcels.
     * Makes sure that unwanted columns are removed from the new table.
     *
     * @param data the List of Parcels to update the table with
     */
    public void updateTableData(List<Parcel> data) {
        Set<String> columnNames = new HashSet<>();
        for (int i = 0; i < table.getColumnCount(); i++){
            columnNames.add(table.getColumnName(i));
        }

        parcelList = data;
        tableModel = new ParcelTableModel(parcelList);
        table.setModel(tableModel);

        Set<String> newColNames = new HashSet<>();
        for (int i = 0; i < table.getColumnCount(); i++){
            newColNames.add(table.getColumnName(i));
        }

        // removes unwanted columns after model update
        for (String str : newColNames){
            if (!columnNames.contains(str)){
                TableColumn column = table.getColumn(str);
                table.removeColumn(column);
            }
        }
    }
}
