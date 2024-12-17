package controller;

import model.Row;
import service.RowService;

public class RowController {
    private final RowService rowService;

    public RowController(RowService rowService) {
        this.rowService = rowService;
    }

    public Row findRowByID(int rowID) {
        return rowService.findRowByID(rowID);
    }
}
