package com.medical.view;

import com.medical.Main;
import com.medical.model.UserType;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class UserTypesWindowController {
	 @FXML
	 private ListView<UserType> userTypesList;
	 
	// Reference to the main application.
	private Main mainApp;
	
	
	
	/**
	 * Cancel button event
	 */
	@FXML
	private void handleCancel()
	{
		System.exit(0);
	}
	
	
	 /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        userTypesList.setItems(mainApp.getUserTypeData());
        userTypesList.getSelectionModel().select(1);
        
    }
}
