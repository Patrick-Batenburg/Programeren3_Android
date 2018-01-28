package com.patrick.maaltijdapp.model.domain;

import android.graphics.Bitmap;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Represents an meal.
 */
public class Meal implements Serializable
{
    private static final String TAG = Meal.class.getSimpleName();
    private int ID;
    private String dish;
    private String description;
    private DateTime dateTime;
    private Student chef;
    private double price;
    private int maxAmountOfFellowEaters;
    private Bitmap image;
    private boolean isCookEating;
    private ArrayList<FellowEater> fellowEaters;

    /**
     * Initializes an empty instance of the Meal class.
     */
    public Meal()
    {
        this.fellowEaters = new ArrayList<>();
    }

    /**
     * Initializes a new instance of the Meal class specified by ID.
     *
     * @param ID The ID of the Meal.
     */
    public Meal(int ID)
    {
        this.ID = ID;
    }

    /**
     * Initializes a new instance of the Meal class specified by ID, DateTime, chef, price, max amount of fellow eaters and whenever the cook is eating or not.
     *
     * @param ID The ID of the meal.
     * @param dish The name of the dish.
     * @param description The description of the meal.
     * @param dateTime The DateTime of the meal.
     * @param chef The chef of the meal.
     * @param price The price of the meal.
     * @param maxAmountOfFellowEaters The max amount of fellow eaters of the meal.
     * @param isCookEating Whenever the cook is eating or not.
     */
    public Meal(int ID, String dish, String description, DateTime dateTime, Student chef, double price, int maxAmountOfFellowEaters, boolean isCookEating)
    {
        this.ID = ID;
        this.dish = dish;
        this.description = description;
        this.dateTime = dateTime;
        this.chef = chef;
        this.price = price;
        this.maxAmountOfFellowEaters = maxAmountOfFellowEaters;
        this.isCookEating = isCookEating;
        this.fellowEaters = new ArrayList<>();
        this.image = null;
    }

    /**
     * Gets the ID represented by this instance.
     *
     * @return Returns the ID represented by this instance.
     */
    public int getID()
    {
        return ID;
    }

    /**
     * Sets the ID represented by this instance.
     *
     * @param ID The value to set.
     **/
    public void setID(int ID)
    {
        this.ID = ID;
    }

    /**
     * Gets the dish represented by this instance.
     *
     * @return Returns the dish represented by this instance.
     */
    public String getDish()
    {
        return dish;
    }

    /**
     * Sets the dish represented by this instance.
     *
     * @param dish The value to set.
     **/
    public void setDish(String dish)
    {
        this.dish = dish;
    }

    /**
     * Gets the description represented by this instance.
     *
     * @return Returns the description represented by this instance.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description represented by this instance.
     *
     * @param description The value to set.
     **/
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Gets the DateTime represented by this instance.
     *
     * @return Returns the DateTime represented by this instance.
     */
    public DateTime getDateTime()
    {
        return dateTime;
    }

    /**
     * Sets the DateTime represented by this instance.
     *
     * @param dateTime The value to set.
     **/
    public void setDateTime(DateTime dateTime)
    {
        this.dateTime = dateTime;
    }

    /**
     * Gets the DateTime represented by this instance in dd-MM-yyyy HH:mm format.
     *
     * @return Returns the DateTime represented by this instance in dd-MM-yyyy HH:mm format.
     */
    public String getDateTimeToString()
    {
        return dateTime.toString("dd-MM-yyyy HH:mm");
    }

    /**
     * Gets the chef represented by this instance.
     *
     * @return Returns the chef represented by this instance.
     */
    public Student getChef()
    {
        return chef;
    }

    /**
     * Sets the chef represented by this instance.
     *
     * @param chef The value to set.
     **/
    public void setChef(Student chef)
    {
        this.chef = chef;
    }

    /**
     * Gets the image represented by this instance.
     *
     * @return Returns the image represented by this instance.
     */
    public Bitmap getImage()
    {
        return this.image;
    }

    /**
     * Sets the image represented by this instance.
     *
     * @param image The value to set.
     **/
    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    /**
     * Gets the price represented by this instance.
     *
     * @return Returns the price represented by this instance.
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * Sets the price represented by this instance.
     *
     * @param price The value to set.
     **/
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     * Gets the maxAmountOfFellowEaters amount of fellow eaters represented by this instance.
     *
     * @return Returns the maxAmountOfFellowEaters amount of fellow eaters represented by this instance.
     */
    public int getMaxAmountOfFellowEaters()
    {
        return maxAmountOfFellowEaters;
    }

    /**
     * Sets the maxAmountOfFellowEaters amount of fellow eaters represented by this instance.
     *
     * @param maxAmountOfFellowEaters The value to set.
     **/
    public void setMaxAmountOfFellowEaters(int maxAmountOfFellowEaters)
    {
        this.maxAmountOfFellowEaters = maxAmountOfFellowEaters;
    }

    /**
     * Returns a value that indicates whether the cook is eating.
     *
     * @return true if cook is eating; otherwise, false.
     */
    public boolean isCookEating()
    {
        return isCookEating;
    }

    /**
     * Sets the value that indicates whether the cook is eating.
     *
     * @param cookEating The value to set.
     **/
    public void setIsCookEating(boolean cookEating)
    {
        this.isCookEating = cookEating;
    }

    /**
     * Gets the fellow eaters represented by this instance.
     *
     * @return Returns the fellow eaters represented by this instance.
     */
    public ArrayList<FellowEater> getFellowEaters()
    {
        return fellowEaters;
    }

    /**
     * Adds a fellow eater to the list represented by this instance.
     *
     * @param fellowEater The fellow eater to add.
     **/
    public void addFellowEater(FellowEater fellowEater)
    {
        this.fellowEaters.add(fellowEater);
    }

    /**
     * Gets the current amount of fellow eaters represented by this instance.
     *
     * @return Returns the current amount of fellow eaters represented by this instance.
     */
    public int getAmountOfFellowEaters()
    {
        int result = 0;

        for (FellowEater fellowEater : fellowEaters)
        {
            result += fellowEater.getAmount();
        }

        return result;
    }

    /**
     * Gets the students represented by this instance.
     *
     * @return Returns the students represented by this instance.
     */
    public ArrayList<Student> getStudents()
    {
        ArrayList<Student> students = new ArrayList<>();

        for (FellowEater fellowEater : fellowEaters)
        {
            students.add(fellowEater.getStudent());
        }

        return students;
    }

    /**
     * Prints the fellow eaters represented by this instance to a String.
     *
     * @return Returns the fellow eaters represented by this instance to a String.
     */
    public String printFellowEaters(List<FellowEater> fellowEaters)
    {
        StringBuilder result = new StringBuilder();

        if (!fellowEaters.isEmpty())
        {
            for (FellowEater fellowEater : fellowEaters)
            {
                result.append(fellowEater.toString());
                result.append("\t");
            }
        }

        return result.toString();
    }

    /**
     * Converts the value of the current Meal object to its equivalent string representation.
     *
     * @return The string representation of the value of this instance.
     */
    @Override
    public String toString()
    {
        return "Meal{" +
                "ID=" + ID +
                ", dish='" + dish + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime.toString("yyyy-MM-dd HH:mm:ss") +
                ", chef=" + chef +
                ", price=" + price +
                ", maxAmountOfFellowEaters=" + maxAmountOfFellowEaters +
                ", image=" + image +
                ", isCookEating=" + isCookEating +
                ", fellowEaters=" + printFellowEaters(fellowEaters) +
                '}';
    }
}
