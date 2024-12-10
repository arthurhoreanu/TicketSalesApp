package model;

import controller.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a row within a section, containing details such as its ID, capacity, and associated section.
 */
public class Row implements Identifiable {
    private int rowID;
    private int rowCapacity;
    private int sectionID; // Links to the parent Section

    static Controller controller = ControllerProvider.getController();

    public Row(int rowID, int rowCapacity, int sectionID) {
        this.rowID = rowID;
        this.rowCapacity = rowCapacity;
        this.sectionID = sectionID;
    }

    public Row(int rowCapacity, int sectionID) {
        this.rowID = 0; // Default ID, to be set externally
        this.rowCapacity = rowCapacity;
        this.sectionID = sectionID;
    }

    @Override
    public Integer getID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public int getRowCapacity() {
        return rowCapacity;
    }

    public void setRowCapacity(int rowCapacity) {
        this.rowCapacity = rowCapacity;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public Section getSection() {
        return controller.findSectionByID(sectionID);
    }
//todo findSeatByRowID in controller
    public List<Seat> getSeats() {
        return controller.findSeatsByRowID(rowID);
    }

    @Override
    public String toCsv() {
        return String.join(",",
                String.valueOf(rowID),
                String.valueOf(rowCapacity),
                String.valueOf(sectionID)
        );
    }

    public static Row fromCsv(String csvLine) {
        String[] fields = csvLine.split(",");
        int rowID = Integer.parseInt(fields[0].trim());
        int rowCapacity = Integer.parseInt(fields[1].trim());
        int sectionID = Integer.parseInt(fields[2].trim());
        return new Row(rowID, rowCapacity, sectionID);
    }

    @Override
    public void toDatabase(PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, rowID);
        stmt.setInt(2, rowCapacity);
        stmt.setInt(3, sectionID);
    }

    public static Row fromDatabase(ResultSet rs) throws SQLException {
        int rowID = rs.getInt("rowID");
        int rowCapacity = rs.getInt("rowCapacity");
        int sectionID = rs.getInt("sectionID");
        return new Row(rowID, rowCapacity, sectionID);
    }

    @Override
    public String toString() {
        return "Row{" +
                "rowID=" + rowID +
                ", rowCapacity=" + rowCapacity +
                ", sectionID=" + sectionID +
                '}';
    }
}
