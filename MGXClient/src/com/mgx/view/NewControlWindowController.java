package com.mgx.view;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

import com.mgx.Main;
import com.mgx.control.DoubleTransition;

public class NewControlWindowController
{
	// Reference to the main application.
	private Main mainApp;

	@FXML
	private TabPane adjustmentsTabPane = new TabPane();

	@FXML
	private TabPane pulseTabPane = new TabPane();

	@FXML
	private LineChart<Double, Double> lineChart;
	
	@FXML
	private Button altTabButton;
	
	// Transition objects for ALT-TAB button
	private TranslateTransition fadeOutTransition;
	private TranslateTransition fadeInTransition;
	
	@FXML
	private ToolBar switchPanelsBtns;
	
	@FXML 
	private TitledPane toolsButtonsPane;

	// The tool bar state
	private boolean isToolbarFaded = false;
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	protected void initialize()
	{
		initChartWithDummyValues();
		
		initTransitions();
		
		// Start with hidden alt-tab buttons bar
		isToolbarFaded = true;
		switchPanelsBtns.setTranslateX(200);
		
		// Hide tools button bar
		toolsButtonsPane.setExpanded(false);
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;
	}
	
	
	//
	// Events
	//
	public void altTabClicked() {
		if (isToolbarFaded)
		{
			fadeInTransition.play();
		}
		else
		{
			fadeOutTransition.play();
		}
		isToolbarFaded = !isToolbarFaded;
	}
	 
    
	//
	// Helpers
	//
	
	private void initChartWithDummyValues()
	{
		ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections
				.observableArrayList();

		LineChart.Series<Double, Double> series1 = new LineChart.Series<Double, Double>();
		series1.setName("Series 1");
		series1.getData().add(new XYChart.Data<Double, Double>(0.0, 1.0));
		series1.getData().add(new XYChart.Data<Double, Double>(1.2, 1.4));
		series1.getData().add(new XYChart.Data<Double, Double>(2.2, 1.9));
		series1.getData().add(new XYChart.Data<Double, Double>(2.7, 2.3));
		series1.getData().add(new XYChart.Data<Double, Double>(2.9, 0.5));

		lineChartData.add(series1);

		LineChart.Series<Double, Double> series2 = new LineChart.Series<Double, Double>();
		series2.setName("Series 2");
		series2.getData().add(new XYChart.Data<Double, Double>(0.0, 1.6));
		series2.getData().add(new XYChart.Data<Double, Double>(0.8, 0.4));
		series2.getData().add(new XYChart.Data<Double, Double>(1.4, 2.9));
		series2.getData().add(new XYChart.Data<Double, Double>(2.1, 1.3));
		series2.getData().add(new XYChart.Data<Double, Double>(2.6, 0.9));

		lineChartData.add(series2);

		lineChart.setData(lineChartData);
		lineChart.createSymbolsProperty();

	}
	
	// Initialize the fade in, fade out transtitions
	private void initTransitions()
	{
		fadeOutTransition = new TranslateTransition(Duration.millis(300), switchPanelsBtns);
		fadeOutTransition.setFromX(0);
		fadeOutTransition.setToX(200);
		fadeOutTransition.setCycleCount(1);
		fadeOutTransition.setAutoReverse(false);
		
		fadeInTransition = new TranslateTransition(Duration.millis(500), switchPanelsBtns);
		fadeInTransition.setFromX(200);
		fadeInTransition.setToX(0);
		fadeInTransition.setCycleCount(1);
		fadeInTransition.setAutoReverse(false);
		

	}
}
