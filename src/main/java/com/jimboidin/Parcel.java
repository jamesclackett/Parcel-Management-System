package com.jimboidin;

import com.jimboidin.database.Database;
import com.jimboidin.database.Result;
import com.jimboidin.database.ResultItem;
import com.jimboidin.exceptions.ParcelException;
import com.jimboidin.exceptions.ResultException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The Parcel class represents student parcels.
 * It allows the reading and manipulation of relevant fields and its
 * field-structure is closely related to the database's parcels table.
 */
public class Parcel {
    private int id;
    private String studentName, address, phone, parcelCompany, parcelColor;
    private LocalDateTime arrivalDate, collectedDate;


    public Parcel(Map<String, Object> fields) throws ParcelException {
        if (fields.get(Fields.NAME) == null)
            throw new ParcelException("parcels must have a student name");
        setStudentName((String) fields.get(Fields.NAME));
        setAddress((String)fields.get(Fields.ADDRESS));
        setPhone((String) fields.get(Fields.PHONE));
        setParcelCompany((String) fields.get(Fields.COMPANY));
        setParcelColor((String) fields.get(Fields.COLOR));
        setArrivalDate((LocalDateTime) fields.get(Fields.ARRIVED));
        setCollectedDate((LocalDateTime) fields.get(Fields.COLLECTED));
    }

    public int getId() {
        return id;
    }

    /**
     * Sets this Parcel's ID. Should be used with caution.
     *
     * @param id the ID to set (must not already exist)
     */
    private void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDateTime getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(LocalDateTime collectedDate) {
        this.collectedDate = collectedDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParcelCompany() {
        return parcelCompany;
    }

    public void setParcelCompany(String parcelCompany) {
        this.parcelCompany = parcelCompany;
    }

    public String getParcelColor() {
        return parcelColor;
    }

    public void setParcelColor(String parcelColor) {
        this.parcelColor = parcelColor;
    }

    /**
     * Method to get this Parcel's value for the given key.
     *
     * @param key the field name
     * @return the field's value
     */
    public Object get(String key){
        Object value;

        switch (key){
            case Fields.ID -> value = getId();
            case Fields.NAME -> value = getStudentName();
            case Fields.ADDRESS -> value = getAddress();
            case Fields.PHONE -> value = getPhone();
            case Fields.COMPANY -> value = getParcelCompany();
            case Fields.COLOR -> value = getParcelColor();
            case Fields.ARRIVED -> value = getArrivalDate();
            case Fields.COLLECTED -> value = getCollectedDate();
            default -> value = null;
        }

        return value;
    }

    /**
     * Method to set this Parcel's value for the given key.
     *
     * @param key the field name
     * @param value the new value
     */
    public void set(String key, Object value){
        switch (key){
            case Fields.NAME -> setStudentName((String) value);
            case Fields.ADDRESS -> setAddress((String) value);
            case Fields.PHONE -> setPhone((String) value);
            case Fields.COMPANY -> setParcelCompany((String) value);
            case Fields.COLOR -> setParcelColor((String) value);
            case Fields.ARRIVED -> setArrivalDate((LocalDateTime) value);
            case Fields.COLLECTED -> setCollectedDate((LocalDateTime) value);
            default -> System.out.println("Unknown key");
        }
    }

    /**
     * Checks if the Parcel already exists in the database.
     *
     * @return true if the parcel exists, false if it does not
     */
    private boolean existsInDb() {
        return id != 0;
    }

    /**
     * A class for getting the column names of the parcels table
     */
    static class Fields{
        public static final String ID = "IDPARCELS";
        public static final String NAME = "STUDENT_NAME";
        public static final String ADDRESS = "ADDRESS";
        public static final String PHONE = "PHONE";
        public static final String COMPANY = "PARCEL_COMPANY";
        public static final String COLOR = "PARCEL_COLOR";
        public static final String ARRIVED = "ARRIVAL_DATE";
        public static final String COLLECTED = "COLLECTED_DATE";
    }


    /**
     * Communicates with the database using the Database class, in order
     * to create, read, update and delete Parcels.
     */
    public static class Connector {

        /**
         * Searches the database for any rows that contain the given student name.
         *
         * @param name the student name to search for
         * @return a list of Parcel objects that have matched the search criteria
         */
        public static List<Parcel> search(String name){
            String sql = "SELECT * FROM parcel_management_sys.parcels " +
                    "WHERE "+Fields.NAME+" LIKE '%"+name+"%' " +
                    "ORDER BY arrival_date DESC;";
            return buildList(sql);

        }

        /**
         * Inserts a Parcel to the database
         *
         * @param parcel the new Parcel object to be inserted
         * @return the integer result of Database::executeUpdate
         * @throws ParcelException - if the Parcel already exists in the database
         */
        public static int insert(Parcel parcel) throws ParcelException {
            if (parcel.getId() != 0){
                throw new ParcelException("Cannot insert: Parcel already exists in database");
            }

            String sql = "INSERT INTO `parcel_management_sys`.`parcels` " +
                    "(`"+Fields.NAME+"`, `"+Fields.ADDRESS+"`, `"+Fields.PHONE+"`, `"+Fields.COMPANY+"`, " +
                    "`"+Fields.COLOR+"`) VALUES ("+
                    wrapInQuotes(parcel.getStudentName())+", "+
                    wrapInQuotes(parcel.getAddress())+", "+
                    wrapInQuotes(parcel.getPhone())+", "+
                    wrapInQuotes(parcel.getParcelCompany())+", "+
                    wrapInQuotes(parcel.getParcelColor())+");";

            System.out.println(sql);

            return Database.executeUpdate(Database.DEFAULT, sql);
        }


        /**
         * Updates a pre-existing Parcel in the database with modified information.
         *
         * @param parcel the updated Parcel to be sent
         * @throws ParcelException - if the Parcel does not already exist.
         */
        public static void update(Parcel parcel) throws ParcelException {
            if (!parcel.existsInDb()){
                throw new ParcelException("Cannot update: Parcel does not exist in database");
                }
            String sql = "UPDATE " +
                    "`parcel_management_sys`.`parcels` " +
                    "SET `"+Fields.NAME+"` = "+wrapInQuotes(parcel.getStudentName())+", " +
                    "`"+Fields.ADDRESS+"` = "+wrapInQuotes(parcel.getAddress())+", " +
                    "`"+Fields.PHONE+"` = "+wrapInQuotes(parcel.getPhone())+", " +
                    "`"+Fields.COMPANY+"` = "+wrapInQuotes(parcel.getParcelCompany())+", " +
                    "`"+Fields.COLOR+"` = "+wrapInQuotes(parcel.getParcelColor())+", " +
                    "`"+Fields.COLLECTED+"` = "+wrapInQuotes(parcel.getCollectedDate())+
                    " WHERE (`"+Fields.ID+"` = "+parcel.getId()+");";

            System.out.println(sql);

            Database.executeUpdate(Database.DEFAULT, sql);
        }

        /**
         * Deletes the given Parcel from the database.
         *
         * @param parcel the Parcel to be deleted
         * @throws ParcelException - if the Parcel does not already exist
         */
        public static void delete(Parcel parcel) throws ParcelException {
            if (parcel.existsInDb()){
                String sql = "DELETE FROM `parcel_management_sys`.`parcels` " +
                        "WHERE (`"+Fields.ID+"` = '"+parcel.getId()+"');";
                Database.executeUpdate(Database.DEFAULT, sql);
            }
            else throw new ParcelException("Parcel does not exist in database");

        }

        /**
         * Creates a Parcel from a ResultItem.
         *
         * @param resultItem the ResultItem to check and then build a Parcel from.
         * @return The Parcel created
         * @throws ResultException - if the ResultItem does not contain an ID
         * @throws ParcelException - if the ResultItem does not contain a student name
         */
        private static Parcel buildParcel(ResultItem resultItem) throws ResultException, ParcelException {
            if (resultItem.containsKey(Fields.ID)){
                Parcel parcel = new Parcel(resultItem);
                parcel.setId((Integer) resultItem.get(Fields.ID));
                return parcel;
            }
            else {
                throw new ResultException("ID necessary to build Parcel from Database");
            }
        }

        /**
         * Builds a List of Parcels by querying the database and calling buildParcel()
         * on the received ResultItem(s).
         *
         * @param sql the SQL query to be sent
         * @return a List of Parcels that have been built.
         */
        public static List<Parcel> buildList(String sql) {
            //TODO: make Optional
            List<Parcel> parcelList = new ArrayList<>();

            Result result = Database.executeQuery(
                    Database.DEFAULT, sql);

            for (ResultItem item : result.getItems()) {
                try {
                    parcelList.add(buildParcel(item));
                } catch (ResultException | ParcelException e) {
                    e.printStackTrace();
                    System.out.println("List not built");
                    return null;
                }
            }

            System.out.println("List of size " + parcelList.size() + " created");
            return parcelList;
        }
    }

    //Wraps a string in single quotes to better suit SQL
    //Prevents strings of 'null' being sent instead of actual null

    /**
     * A class to add single-quotes to a string.
     * This is useful when text is added programmatically to an SQL String,
     * since it creates the option of leaving out single-quotes on certain values.
     * Example: for null values, it is important to add NULL to the database, and not
     * the string 'null'.
     *
     * @param str the String to be wrapped
     * @return The wrapped String
     */
    private static String wrapInQuotes(String str){
        if (str == null) return null;
        else return "'"+str+"'";
    }

    /**
     * See wrapInQuotes(String str) above.
     */
    private static String wrapInQuotes(LocalDateTime dateTime){
        if (dateTime == null) return null;
        else return "'"+dateTime+"'";
    }


}
