/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 
package localizationsim;

import java.text.DecimalFormat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class Coordinates implements Cloneable
{
	/** The x component of the position */
	public double x;
	/** The y component of the position */
	public double y;

	/**
	 * Creates a new set of coordinates
	 * @param x The x component of the position
	 * @param y The y component of the position
	 */
	public Coordinates(double x, double y)
	{
		this.x =  (x);
		this.y =  (y);
	}

    Coordinates() {

    }

	@Override
	public Coordinates clone()
	{
		Coordinates clone = new Coordinates(0,0);
		clone.mimic(this);
		return clone;
	}

	/**
	 * Copies coordinates from another set of coordinates
	 * @param other The other coordinates to copy from
	 */
	public void mimic(Coordinates other)
	{
		this.x = other.x;
		this.y = other.y;
	}

	/**
	 * Adds x and y components to the coordinates
	 * @param x The x component to add
	 * @param y The y component to add
	 */
	public void add(double x, double y)
	{
		this.x += x;
		this.y += y;
	}

	/**
	 * Adds to the internal coordinates the components of another set of
	 * coordinates
	 * @param other The other coordinates to add from
	 */
	public void addFrom(Coordinates other)
	{
		this.x += other.x;
		this.y += other.y;
	}

	/**
	 * Multiplies the components of the coordinates/position by the given
	 * factor
	 * @param factor The number to multiply each component by
	 */
	public Coordinates multiplyBy(double factor)
	{

                return new Coordinates(this.x * factor,this.y * factor);
	}

        public Coordinates divideBy(double factor)
	{

                return new Coordinates(this.x / factor,this.y / factor);
	}
	/**
	 * Sets all the coordinates to 0.
	 */
	public void clear()
	{
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Gets the distance between this and another coordinate
	 * @param other The other coordinate of interest
	 * @return The distance between the coordinates
	 */
	public double distanceFrom(Coordinates other)
	{
		double deltaX = Math.abs(this.x - other.x);
		double deltaY = Math.abs(this.y - other.y);
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	}


}
