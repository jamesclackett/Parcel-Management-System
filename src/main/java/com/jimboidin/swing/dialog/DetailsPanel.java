package com.jimboidin.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Sets out the Dialog UI for creating a new Parcel.
 * Contains a panel of JLabels corresponding to Parcel fields and
 * a panel of JTextFields which allow the user to enter information.
 */
public class DetailsPanel extends JPanel{
    private JPanel labels, controls;
    private JTextField nameField, addressField,
            phoneField, companyField, colorField;

    public DetailsPanel(){
        super(new BorderLayout(10,10));
        labels = new JPanel(new GridLayout(5,1, 5,5));
        controls = new JPanel(new GridLayout(5,1, 5, 5));

        labels.add(new JLabel("Name", SwingConstants.TRAILING));
        labels.add(new JLabel("Address", SwingConstants.TRAILING));
        labels.add(new JLabel("Phone", SwingConstants.TRAILING));
        labels.add(new JLabel("Company", SwingConstants.TRAILING));
        labels.add(new JLabel("Color", SwingConstants.TRAILING));

        nameField = new JTextField();
        addressField = new JTextField();
        phoneField = new JTextField();
        companyField = new JTextField();
        colorField = new JTextField();

        controls.add(nameField);
        controls.add(addressField);
        controls.add(phoneField);
        controls.add(companyField);
        controls.add(colorField);

        add(labels, BorderLayout.WEST);
        add(controls, BorderLayout.CENTER);
    }

    /**
     * Creates a Map from the user-input information. Makes
     * sure that there is a key for every Parcel field, and
     * that if the user leaves a field blank, that null is
     * given to the Map, as opposed to "".
     *
     * @return a Map of fields that the user has input.
     */
    public Map<String, Object> getFields(){
        Map<String, Object> fields = new HashMap<>();
        if (checkIfEmpty(nameField.getText())){
            fields.put("STUDENT_NAME", null);
        } else {
            fields.put("STUDENT_NAME", nameField.getText());
        }
        if (checkIfEmpty(addressField.getText())){
            fields.put("ADDRESS", null);
        } else {
            fields.put("ADDRESS", addressField.getText());
        }
        if (checkIfEmpty(phoneField.getText())){
            fields.put("PHONE", null);
        } else {
            fields.put("PHONE", phoneField.getText());
        }
        if (checkIfEmpty(companyField.getText())){
            fields.put("PARCEL_COMPANY", null);
        } else {
            fields.put("PARCEL_COMPANY", companyField.getText());
        }
        if (checkIfEmpty(colorField.getText())){
            fields.put("PARCEL_COLOR", null);
        } else {
            fields.put("PARCEL_COLOR", colorField.getText());
        }

        return fields;
    }

    /**
     * Checks if the user has left a particular field blank.
     *
     * @param text the String received from a JTextField
     * @return true if the field was left blank, false if not
     */
    private boolean checkIfEmpty(String text) {
        return text.equals("");
    }


}
