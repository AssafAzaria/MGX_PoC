package com.medical.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import com.medical.Main;

public class RootWindowController {
	// Reference to the main application.
	private Main mainApp;

	@FXML
	private ComboBox<String> userCombo;
	
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}
	
	// Set the main app	
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
		
		// dummy values for combo
		userCombo.getItems().addAll("David Smith", "Switch User", "Log Off");
	    userCombo.setValue("David Smith");  
	}
	
	//
	// Events
	//
	@FXML
	private void handleCogWheelButton()
	{
		// switch to control window
		mainApp.showControlWindow();
	}
	
	@FXML
	private void handleTempertureButton()
	{
		// switch to user types window
		mainApp.showUserTypesWindow();
	}
	
	@FXML
	private void handleLogButton()
	{
		// switch to settings window
		mainApp.showSystemSettingsWindow();
	}
}
