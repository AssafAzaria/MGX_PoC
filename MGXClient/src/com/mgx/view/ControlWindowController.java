package com.mgx.view;


import com.mgx.Main;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;

public class ControlWindowController {

    @FXML
    private ListView<String> seqList;
    
    @FXML
    private ComboBox<String> xedChoice;
    
    // Reference to the main application.
    private Main mainApp;

    /**
     * The constructor is called before the initialize() method.
     */
    public ControlWindowController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
      }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        seqList.setItems(mainApp.getPersonData());
        seqList.setCenterShape(true);
        seqList.getSelectionModel().select(1);
        
        
        xedChoice.setItems(mainApp.getXedData());
        xedChoice.setValue("XED#");
    }
}

