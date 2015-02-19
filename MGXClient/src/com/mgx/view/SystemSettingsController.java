package com.mgx.view;

import org.controlsfx.dialog.Dialogs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.*;
import javafx.util.converter.FloatStringConverter;

import com.mgx.Main;
import com.mgx.control.RemoteMGXController;
import com.mgx.model.ParameterRow;
import com.thales.shared.XEDInfo;
import com.thales.shared.XEDPropertyInfo;

public class SystemSettingsController
{
	// Text for header label
	private static final String HEADER_LABEL = "Settings";

	// Reference to the main application.
	private Main mainApp;

	// Header label
	@FXML
	private Label headerLabel;
	
	// The name of the XED associated with this window
	private String XEDName;

	//
	// Tables - Not the best of ideas to have 4 tables in the same window...
	//
	// Table 1
	@FXML
	private TableView<ParameterRow> table1;

	@FXML
	private TableColumn<ParameterRow, String> paramNameColumn1;

	@FXML
	private TableColumn<ParameterRow, Float> valueColumn1;

	@FXML
	private TableColumn<ParameterRow, Float> minColumn1;

	@FXML
	private TableColumn<ParameterRow, Float> maxColumn1;

	@FXML
	private TableColumn<ParameterRow, String> unitColumn1;

	@FXML
	private TableColumn<ParameterRow, String> lorColumn1;

	// Table 2
	@FXML
	private TableView<ParameterRow> table2;

	@FXML
	private TableColumn<ParameterRow, String> paramNameColumn2;

	@FXML
	private TableColumn<ParameterRow, Float> valueColumn2;

	@FXML
	private TableColumn<ParameterRow, Float> minColumn2;

	@FXML
	private TableColumn<ParameterRow, Float> maxColumn2;

	@FXML
	private TableColumn<ParameterRow, String> unitColumn2;

	@FXML
	private TableColumn<ParameterRow, String> lorColumn2;

	// Table 3
	@FXML
	private TableView<ParameterRow> table3;

	@FXML
	private TableColumn<ParameterRow, String> paramNameColumn3;

	@FXML
	private TableColumn<ParameterRow, Float> valueColumn3;

	@FXML
	private TableColumn<ParameterRow, Float> minColumn3;

	@FXML
	private TableColumn<ParameterRow, Float> maxColumn3;

	@FXML
	private TableColumn<ParameterRow, String> unitColumn3;

	@FXML
	private TableColumn<ParameterRow, String> lorColumn3;

	// Table 4
	@FXML
	private TableView<ParameterRow> table4;

	@FXML
	private TableColumn<ParameterRow, String> paramNameColumn4;

	@FXML
	private TableColumn<ParameterRow, Float> valueColumn4;

	@FXML
	private TableColumn<ParameterRow, Float> minColumn4;

	@FXML
	private TableColumn<ParameterRow, Float> maxColumn4;

	@FXML
	private TableColumn<ParameterRow, String> unitColumn4;

	@FXML
	private TableColumn<ParameterRow, String> lorColumn4;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize()
	{
		connectCellFactories();
	}

	public SystemSettingsController()
	{
	}

	//
	// Events
	//
	@FXML
	private void handleUpdate()
	{
		String msg = null;

		// Get the selected line (currently from second table and on.)
		ParameterRow row = table2.getSelectionModel().getSelectedItem();
		if (row == null)
		{
			row = table3.getSelectionModel().getSelectedItem();
		}
		if (row == null)
		{
			row = table4.getSelectionModel().getSelectedItem();
		}
		if (row == null)
		{
			msg = "PLEASE SELECT A LINE FROM A TABLE";
		}

		if (row != null)
		{
			// Create display message
			StringBuilder msgB = new StringBuilder();
			msgB.append("Parameter Name: ").append(row.getParamName())
					.append(" , ");
			msgB.append("Value: ").append(row.getValue()).append(" , ");
			msgB.append("Min: ").append(row.getMin()).append(" , ");
			msgB.append("Max: ").append(row.getMax()).append(" , ");
			msgB.append("Unit: ").append(row.getUnit()).append(" , ");
			msgB.append("Lor: ").append(row.getLor()).append(" , ");
			msg = msgB.toString();
		}

		// Show the message.
		Dialogs d = Dialogs.create();
		d.title("Save Pressed");
		d.masthead("Selected Row: ");
		d.message(msg);
		d.showInformation();

		// Send the info to the server
		RemoteMGXController.getInstance().sendXEDPropertyUpdate(
				lastEditedRow.getParamName(), 
				lastEditedValue, XEDName);
		

	}

	/**
	 * Exit button event
	 */
	@FXML
	private void handleExit()
	{
		System.exit(0);
	}

	//
	// Helpers
	//
	/**
	 * Is called by the main application to give a reference back to itself.
	 */
	public void setMainApp(Main mainApp)
	{
		this.mainApp = mainApp;

		// Add observable list data to the table
		table1.getItems().add(new ParameterRow("param"));
	}

	/**
	 * Connects cell factories to the data, and register cell edit event
	 */
	private void connectCellFactories()
	{
		connectCellFactoryToASingleTable(paramNameColumn1, valueColumn1,
				minColumn1, maxColumn1, unitColumn1, lorColumn1);
		connectCellFactoryToASingleTable(paramNameColumn2, valueColumn2,
				minColumn2, maxColumn2, unitColumn2, lorColumn2);
		connectCellFactoryToASingleTable(paramNameColumn3, valueColumn3,
				minColumn3, maxColumn3, unitColumn3, lorColumn3);
		connectCellFactoryToASingleTable(paramNameColumn4, valueColumn4,
				minColumn4, maxColumn4, unitColumn4, lorColumn4);

		// Enable editing of certain cells
		registerCellEditFactory(valueColumn2);
		registerCellEditFactory(valueColumn3);
		registerCellEditFactory(valueColumn4);
		
		
	    
	    
	}

	/**
	 *  Registers cell factories to a single table
	 */
	private void connectCellFactoryToASingleTable(
			TableColumn<ParameterRow, String> nameCol,
			TableColumn<ParameterRow, Float> valueCol,
			TableColumn<ParameterRow, Float> minCol,
			TableColumn<ParameterRow, Float> maxCol,
			TableColumn<ParameterRow, String> unitCol,
			TableColumn<ParameterRow, String> lorCol)
	{
		nameCol.setCellValueFactory(cellData -> cellData.getValue()
				.paramNameProperty());
		unitCol.setCellValueFactory(cellData -> cellData.getValue()
				.unitProperty());
		lorCol.setCellValueFactory(cellData -> cellData.getValue()
				.lorProperty());

		minCol.setCellValueFactory(cellData -> cellData.getValue()
				.minProperty().asObject());
		maxCol.setCellValueFactory(cellData -> cellData.getValue()
				.maxProperty().asObject());
		valueCol.setCellValueFactory(cellData -> cellData.getValue()
				.valueProperty().asObject());

	}
	
	// PATCH For transmition
	private float lastEditedValue;
	private TableView<ParameterRow> lastEditedTable = table1;
	private ParameterRow lastEditedRow = new ParameterRow("test");
	
	/**
	 *  Makes the given cell editable by text field. Also registers a listener
	 *  and handle the event
	 */
	private void registerCellEditFactory(TableColumn<ParameterRow, Float> col) 
	{
		// Default text field editor and float converter
		col.setCellFactory(TextFieldTableCell.<ParameterRow, Float>
			forTableColumn(new MyFloatStringConverter())); 
		
		// Register event handler
		col.setOnEditCommit(new EventHandler<CellEditEvent<ParameterRow, Float>>()
		{
			@Override
			public void handle(CellEditEvent<ParameterRow, Float> e)
			{
				// Keep the latest change for transmition (Atrocious, but will 
				// be changed later. 
				lastEditedValue = e.getNewValue();
				lastEditedTable = e.getTableView();
				lastEditedRow = e.getRowValue();
				
				// Set the new value to the table
				((ParameterRow) e.getTableView().getItems()
						.get(e.getTablePosition().getRow())).setValue(e
						.getNewValue());
			}
		});
	}

	// Extending only to catch the exception. 
	// The parent class (amazingly) crashes on wrong input...
	class MyFloatStringConverter extends FloatStringConverter
	{
		public Float fromString(String value) {
	        try {
	        	return super.fromString(value);
	        }catch(NumberFormatException e)
	        {
	        	return 0F;
	        }
	    }
	}
	
	//
	// Incoming data handling
	//
	/**
	 * Update tables data from the given XEDInfo
	 */
	public void updateData(XEDInfo info)
	{
		XEDName = info.Name;
		headerLabel.setText(info.Name + " " + HEADER_LABEL);

		// Assumption: the structure of xed properties. TODO: Check if this is
		// always so.
		for (XEDPropertyInfo prop : info.properties)
		{
			if (prop.name.contains("Voltage"))
			{
				addTableRow(table2, prop);
			} else if (prop.name.contains("Current")
					|| prop.name.contains("Curent")) // BWwwww
			{
				addTableRow(table3, prop);
			} else if (prop.name.contains("Temperture")
					|| prop.name.contains("Temprature")) // BWwwww
			{
				addTableRow(table4, prop);
			}
		}
	}
	
	/**
	 * A single property update
	 */
	public void updateSingleProperty(String propName, float newValue)
	{
		//TODO: Improve this
		if (propName.contains("Voltage"))
		{
			updateTableRow(table2, propName, newValue);
		} else if (propName.contains("Current")
				|| propName.contains("Curent")) // BWwwww
		{
			updateTableRow(table3, propName, newValue);
		} else if (propName.contains("Temperture")
				|| propName.contains("Temprature")) // BWwwww
		{
			updateTableRow(table4, propName, newValue);
		}
	}

	/**
	 * Adds a new line to the given table
	 * @param table the table
	 * @param prop the property to add
	 */
	private void addTableRow(TableView<ParameterRow> table, XEDPropertyInfo prop)
	{
		ParameterRow paramRow = new ParameterRow(prop.name, prop.value,
				prop.min, prop.max, prop.units);
		table.getItems().add(paramRow);
	}
	
	/**
	 * Update an existing table row
	 * @param table
	 * @param propName
	 * @param newValue
	 */
	private void updateTableRow(TableView<ParameterRow> table, String propName, float newValue)
	{
		for (ParameterRow row : table.getItems())
		{
			if (row.getParamName().equals(propName))
			{
				row.setValue(newValue);
				break;
			}
		}
	}
	
	public String getXEDName()
	{
		return XEDName;
	}


}
