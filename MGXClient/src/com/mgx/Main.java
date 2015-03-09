package com.mgx;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.mgx.control.RemoteMGXController;
import com.mgx.model.*;
import com.mgx.view.ControlWindowController;
import com.mgx.view.LoginWindowController;
import com.mgx.view.NewControlWindowController;
import com.mgx.view.RootWindowController;
import com.mgx.view.SystemSettingsController;
import com.mgx.view.UserTypesWindowController;
import com.thales.shared.XEDInfo;
import com.thales.shared.XEDPropertyValueUpdate;
import com.thales.shared.events.Event;
import com.thales.shared.networking.EventsHandler;
import com.thales.shared.networking.client.ConnectionBase;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application
{
	//
	// Stage and windows
	//
	private Stage primaryStage;
	private AnchorPane controlWindow;
	//private AnchorPane systemSettingsWindow;
	private AnchorPane userTypesWindow;
	private AnchorPane loginWindow;
	private BorderPane rootWindow;
	private TabPane settingsWindowTabPane = new TabPane();
	private BorderPane newControlWindow;
	
	/**
	 * The list of system settings controllers. We have one for each tab/XED
	 */
	private List<SystemSettingsController> settingsControllers = new LinkedList<>();

	/**
	 * Data for list view in control window
	 */
	private ObservableList<String> personData = FXCollections
			.observableArrayList();

	/**
	 * Data for combo box control window
	 */
	private ObservableList<String> xedData = FXCollections
			.observableArrayList();

	/**
	 * Data for list view in user types window
	 */
	private ObservableList<UserType> userTypeData = FXCollections
			.observableArrayList();
	
	

	/**
	 * Setting window Tab pane
	 */
	
	/**
	 * Constructor - initialize tables with dummy data
	 */
	public Main()
	{
		// Add some sample data
		addSampleData();
		
	}

	@Override
	public void start(Stage primaryStage)
	{
		this.primaryStage = primaryStage;

		initRootWindow();

		// Load all windows without showing them.
		// TODO: maybe we should lazy initialize windows for performance
		loadControlWindow();
		//loadSystemSettingsWindow();
		loadUserTypesWindow();
		loadLoginWindow();
		loadNewControlWindow();

		// Start by showing login window
//		Scene scene = new Scene(newControlWindow);
//		primaryStage.setScene(scene);

		showRootWindowWithControl();
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	//
	// Window showing -- called by various controllers
	//
	public final void showRootWindowWithSettings()
	{
		// Show the scene containing the root layout.
		Scene scene = new Scene(rootWindow);
		primaryStage.setScene(scene);

		showSystemSettingsWindow();
	}
	
	public final void showRootWindowWithControl()
	{
		// Show the scene containing the root layout.
		Scene scene = new Scene(rootWindow);
		primaryStage.setScene(scene);

		showNewControlWindow();
	}

	public final void showSystemSettingsWindow()
	{
		rootWindow.setCenter(settingsWindowTabPane);
	}

	public final void showUserTypesWindow()
	{
		rootWindow.setCenter(userTypesWindow);
	}

	public final void showControlWindow()
	{
		rootWindow.setCenter(controlWindow);
	}
	
	public final void showNewControlWindow()
	{
		rootWindow.setCenter(newControlWindow);
	}


	//
	// Windows loading
	//

	/**
	 * Initializes root layout - contains the tool bar and wraps all other
	 * windows.
	 */
	public final void initRootWindow()
	{
		try
		{
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootWindow.fxml"));
			rootWindow = (BorderPane) loader.load();

			// Give the controller access to the main app.
			RootWindowController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Loads the login window
	 */
	private void loadLoginWindow()
	{

		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LoginWindow.fxml"));
			loginWindow = (AnchorPane) loader.load();

			// Give the controller access to the main app.
			LoginWindowController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e)
		{
			e.printStackTrace();

		}
	}

	/**
	 * Loads the control window (without showing)
	 */
	private void loadControlWindow()
	{

		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("view/ControlWindow.fxml"));
			controlWindow = (AnchorPane) loader.load();

			// Give the controller access to the main app.
			ControlWindowController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Loads the user types window (without showing)
	 */
	private void loadUserTypesWindow()
	{

		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("view/UserTypesWindow.fxml"));
			userTypesWindow = (AnchorPane) loader.load();

			// Give the controller access to the main app.
			UserTypesWindowController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * loads the system settings window (without showing)
	 */
//	private void loadSystemSettingsWindow()
//	{
//
//		try
//		{
//			// Load person overview.
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(Main.class
//					.getResource("view/SystemSettingsWindow.fxml"));
//			systemSettingsWindow = (AnchorPane) loader.load();
//
//			// Give the controller access to the main app.
//			SystemSettingsController controller = loader.getController();
//
//			controller.setMainApp(this);
//
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//
//		}
//	}

	/**
	 * loads the system settings window (without showing)
	 */
	private void loadNewSystemSettingsTab(XEDInfo xed)
	{

		try
		{
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("view/SystemSettingsWindow.fxml"));
			AnchorPane systemSettingsWindow = (AnchorPane) loader.load();

			// Give the controller access to the main app.
			SystemSettingsController controller = loader.getController();
			settingsControllers.add(controller);
			
			controller.setMainApp(this);

			Tab tab = new Tab(xed.Name);
			tab.setContent(systemSettingsWindow);
			settingsWindowTabPane.getTabs().add(tab);

			// Update controller with XED
			controller.updateData(xed);
		} catch (IOException e)
		{
			e.printStackTrace();

		}
	}

	/**
	 * Loads the user types window (without showing)
	 */
	private void loadNewControlWindow()
	{

		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class
					.getResource("view/NewControlWindow.fxml"));
			newControlWindow = (BorderPane) loader.load();

			// Give the controller access to the main app.
			NewControlWindowController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//
	// Data return functions to be used by controllers
	//
	public ObservableList<String> getPersonData()
	{
		return personData;
	}

	public ObservableList<String> getXedData()
	{
		return xedData;
	}

	public ObservableList<UserType> getUserTypeData()
	{
		return userTypeData;
	}

	
	//
	// MGX Server handling
	//

	
	/**
	 * Login to the system. Ask server for XEDS, and launch settings window
	 */
	public final void login()
	{
		// Send requests to MGX for XEDs
		RemoteMGXController mgx = RemoteMGXController.getInstance();
		mgx.requestXEDInfo(new RemoteMGXEventsHandler());
		
		// Switch to settings view
		showRootWindowWithSettings();
	}


	/**
	 * MGX Events handler - responses from the server
	 */
	class RemoteMGXEventsHandler extends EventsHandler
	{
		
		@Override
		public void handleEvent(Event event, ConnectionBase connection)
		{
			System.out.println(getName() + " got event > " + event);
			switch (event.getName()) 
			{
				// Contains list of XEDS to display
				case "GetXEDsResponse": 
					handleXEDResponse(event);
					break;
				// Contains an update on a single property
				case "com.thales.shared.events.XEDUpdateNotification": 
					handleXEDUpdateNotification(event);
					break;
			
			}
		}
		
		private void handleXEDResponse(Event event)
		{
			// Passes callback to the FX thread,
			// and releases the background server thread.
			Platform.runLater(new Runnable()
			{
				public void run()
				{
					updateXEDData((XEDInfo[]) event.data);
				}
			});
		}
		
		private void handleXEDUpdateNotification(Event event)
		{
			Platform.runLater(new Runnable()
			{
				public void run()
				{
					XEDPropertyValueUpdate update = (XEDPropertyValueUpdate)(event.data);
					
					// Find the correct controller
					for(SystemSettingsController cont : settingsControllers)
					{
						if (cont.getXEDName().equals(update.XEDName))
						{
							cont.updateSingleProperty(update.name, update.newValue);
						}
					}
					
				
				}
			});
		}
		
		private void updateXEDData(XEDInfo[] xeds)
		{
			// For each xed - add a tab to the view
			for (XEDInfo xed : xeds)
			{
				loadNewSystemSettingsTab(xed);
			}

		}

		@Override
		public String getName()
		{
			return "Main gui events handler";
		}

	}
	
	
	public static void main(String[] args)
	{
		// Launches the application - constructor() and start() will be called,
		launch(args);
	}
	
	private void addSampleData()
	{
		personData.add("TOMO 1");
		personData.add("CBCT");
		personData.add("CAT Scan");
		personData.add("Test 1");
		personData.add("Test 2");
		personData.add("Test 3");
		personData.add("Test 4");
		personData.add("Test 5");

		xedData.add("XED#");
		xedData.add("XED#");
		xedData.add("XED#");
		xedData.add("XED#");

		for (UserType t : UserType.values())
		{
			userTypeData.add(t);
		}

	}
}
