package com.mgx.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import com.mgx.Main;

public class LoginWindowController {
	 // Reference to the main application.
    private Main mainApp;
    
    
    @FXML
    private TextField userNameTF;
    

    @FXML
    private TextField passwordTF;
    
    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    // Login event
    @FXML
    private void handleLogin()
    {
    	// Currently we accept all logins - switch to root view
    	String username = userNameTF.getText();
    	String password = passwordTF.getText();
    	
    	System.out.println("Name: " + username + " pass: " + password);
    	Logger.getAnonymousLogger().log(Level.ALL, "Name: " + username + " pass: " + password);
    	
    	mainApp.login();
    	
    }
    
   
}
