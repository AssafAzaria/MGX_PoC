package com.medical.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single row in the Settings window tables
 */
public class ParameterRow {
	//
	// Members
	//
	private final StringProperty paramName;
	private final FloatProperty value;
	private final FloatProperty min;
	private final FloatProperty max;
	private final StringProperty unit;
	private final StringProperty lor;
	
	//
	// Constructors
	//
	public ParameterRow(String paramName) {
		// initial values
		this(paramName, 0, 0, 0, "---", "---");
	}
	
	
	public ParameterRow(String paramName, float value, float min, float max, 
			String unit) 
	{
		// default lor
		this(paramName, value, min, max, unit, "---");
	}
	
	public ParameterRow(String paramName, float value, float min, float max, 
			String unit, String lor) {
		this.paramName = new SimpleStringProperty(paramName);
		this.value = new SimpleFloatProperty(value);
		this.min = new SimpleFloatProperty(min);
		this.max = new SimpleFloatProperty(max);
		this.unit = new SimpleStringProperty(unit);
		this.lor = new SimpleStringProperty(lor);
	}
	
	//
	// Getters @ Setters
	//
	public StringProperty paramNameProperty() {
		return paramName;
	}
	
	public String getParamName() {
		return paramName.get();
	}
	
	public void setParamName(String name)
	{
		this.paramName.set(name);
	}
	
	public FloatProperty valueProperty() {
		return value;
	}
	
	public float getValue() {
		return value.get();
	}
	
	public void setValue(float value)
	{
		this.value.set(value);
	}
	
	
	public FloatProperty minProperty() {
		return min;
	}
	
	public float getMin() {
		return min.get();
	}
	
	public void setMin(float min)
	{
		this.min.set(min);
	}
	
	
	public FloatProperty maxProperty() {
		return max;
	}
	
	public float getMax() {
		return max.get();
	}
	
	public void setMax(float max)
	{
		this.max.set(max);
	}
	
	public StringProperty unitProperty() {
		return unit;
	}
	
	public String getUnit() {
		return unit.get();
	}
	
	public void setUnit(String unit)
	{
		this.unit.set(unit);
	}
	
	public StringProperty lorProperty() {
		return lor;
	}
	
	public String getLor() {
		return lor.get();
	}
	
	public void setLor(String lor)
	{
		this.lor.set(lor);
	}
	
}
