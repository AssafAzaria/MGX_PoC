package com.mgx.control;

import javafx.animation.Transition;
import javafx.beans.property.*;
import javafx.util.Duration;

/**
 * This {@code Transition} animates/changes any DoubleProperty over a
 * {@code duration}. This is done by updating the the property at a regular
 * interval.
 * <p>
 * It starts from the {@code fromValue} if provided else uses the properties's
 * original value.
 * <p>
 * It stops at the {@code toValue} value if provided else it will use start
 * value plus {@code byValue}.
 * <p>
 * The {@code toValue} takes precedence if both {@code toValue} and
 * {@code byValue} are specified.
 * 
 * <p>
 * Code Segment Example:
 * </p>
 * 
 * <pre>
 * <code>
 * import snap.javafx.*;
 * 
 * ...
 * 
 *     DoubleProperty positionProp = aSplitPane.getDividers().get(0).positionProperty();
 *     
 *     DoubleTransition dt = new DoubleTransition(Duration.millis(500), positionProp);
 *     dt.setFromValue(1);
 *     dt.setToValue(0.5);
 *     dt.play();
 * 
 * ...
 * 
 * </code>
 * </pre>
 * 
 * @see Transition
 * @see Animation
 */
public class DoubleTransition extends Transition
{

	private DoubleProperty _dp;

	/**
	 * The constructor of {@code FadeTransition}
	 * 
	 * @param duration
	 *            The duration of the {@code FadeTransition}
	 * @param node
	 *            The {@code node} which opacity will be animated
	 */
	public DoubleTransition(Duration duration, DoubleProperty aDP)
	{
		setDuration(duration);
		setCycleDuration(duration);
		setDoubleProperty(aDP);
	}

	/**
	 * The duration of this {@code FadeTransition}.
	 * <p>
	 * It is not possible to change the {@code duration} of a running
	 * {@code FadeTransition}. If the value of {@code duration} is changed for a
	 * running {@code FadeTransition}, the animation has to be stopped and
	 * started again to pick up the new value.
	 * <p>
	 * Note: While the unit of {@code duration} is a millisecond, the
	 * granularity depends on the underlying operating system and will in
	 * general be larger. For example animations on desktop systems usually run
	 * with a maximum of 60fps which gives a granularity of ~17 ms.
	 * 
	 * @defaultValue 400ms
	 */
	private ObjectProperty<Duration> duration;
	private static final Duration DEFAULT_DURATION = Duration.millis(400);

	public final void setDuration(Duration value)
	{
		if (duration != null || (!DEFAULT_DURATION.equals(value)))
			durationProperty().set(value);
	}

	public Duration getDuration()
	{
		return (duration == null) ? DEFAULT_DURATION : duration.get();
	}

	public final ObjectProperty<Duration> durationProperty()
	{
		if (duration == null)
		{
			duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION)
			{
				public void invalidated()
				{
					setCycleDuration(getDuration());
				}

				public Object getBean()
				{
					return DoubleTransition.this;
				}

				public String getName()
				{
					return "duration";
				}
			};
		}
		return duration;
	}

	/**
	 * Returns the DoubleProperty.
	 */
	public DoubleProperty getDoubleProperty()
	{
		return _dp;
	}

	/**
	 * Sets the DoubleProperty.
	 */
	protected void setDoubleProperty(DoubleProperty aDP)
	{
		_dp = aDP;
	}

	/**
	 * Specifies the start opacity value for this {@code FadeTransition}.
	 * <p>
	 * It is not possible to change {@code fromValue} of a running
	 * {@code FadeTransition}. If the value of {@code fromValue} is changed for
	 * a running {@code FadeTransition}, the animation has to be stopped and
	 * started again to pick up the new value.
	 */
	private DoubleProperty fromValue;

	public final void setFromValue(double aValue)
	{
		fromValueProperty().set(aValue);
	}

	public final Object getFromValue()
	{
		return fromValueProperty().get();
	}

	public final DoubleProperty fromValueProperty()
	{
		if (fromValue == null)
		{
			double value = getDoubleProperty().get();
			fromValue = new SimpleDoubleProperty(this, "fromValue", value);
		}
		return fromValue;
	}

	/**
	 * Specifies the stop opacity value for this {@code JFXKeyValueTransition}.
	 * <p>
	 * It is not possible to change {@code toValue} of a running
	 * {@code JFXKeyValueTransition}. If the value of {@code toValue} is changed
	 * for a running {@code FadeTransition}, the animation has to be stopped and
	 * started again to pick up the new value.
	 */
	private DoubleProperty toValue;

	public final Object getToValue()
	{
		return toValueProperty().get();
	}

	public final void setToValue(double value)
	{
		toValueProperty().set(value);
	}

	public DoubleProperty toValueProperty()
	{
		if (toValue == null)
			toValue = new SimpleDoubleProperty(this, "toValue");
		return toValue;
	}

	/**
	 * Specifies the incremented stop opacity value, from the start, of this
	 * {@code JFXKeyValueTransition}.
	 * <p>
	 * It is not possible to change {@code byValue} of a running
	 * {@code JFXKeyValueTransition}. If the value of {@code byValue} is changed
	 * for a running {@code JFXKeyValueTransition}, the animation has to be
	 * stopped and started again to pick up the new value.
	 */
	private DoubleProperty byValue;

	public final Object getByValue()
	{
		return byValueProperty().get();
	}

	public final void setByValue(double value)
	{
		byValueProperty().set(value);
	}

	public final DoubleProperty byValueProperty()
	{
		if (byValue == null)
			byValue = new SimpleDoubleProperty(this, "byValue");
		return byValue;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void interpolate(double aFraction)
	{
		double fromValue = (Double) getFromValue();
		double toValue = (Double) getToValue();
		double value = getInterpolation(aFraction, fromValue, toValue);
		getDoubleProperty().set(value);
	}

	/** Returns an interpolation for a given fraction (from 0-1) and two values. */
	private static double getInterpolation(double aFraction, double aStart,
			double anEnd)
	{
		return aStart + (anEnd - aStart) * aFraction;
	}

}