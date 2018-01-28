package com.patrick.maaltijdapp.model.domain;

import java.io.Serializable;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Represents an fellow eater.
 */
public class FellowEater implements Serializable
{
    private static final String TAG = FellowEater.class.getSimpleName();
    private int ID;
    private Student student;
    private int amountOfGuests;
    private Meal meal;

    /**
     * Initializes an empty instance of the FellowEater class.
     */
    public FellowEater()
    {

    }

    /**
     * Initializes a new instance of the FellowEater class specified by ID, student, amount of guests and meal.
     *
     * @param ID The ID of the fellow eater.
     * @param student The student of the fellow eater.
     * @param amountOfGuests The amount of guests of the fellow eater.
     * @param meal The meal of the fellow eater.
     */
    public FellowEater(int ID, Student student, int amountOfGuests, Meal meal)
    {
        this.ID = ID;
        this.student = student;
        this.amountOfGuests = amountOfGuests;
        this.meal = meal;
    }

    /**
     * Gets the amount of guests with the student represented by this instance.
     *
     * @return Returns the amount of guests with the student represented by this instance.
     */
    public int getAmount()
    {
        return amountOfGuests + 1;
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
     * Gets the student represented by this instance.
     *
     * @return Returns the student represented by this instance.
     */
    public Student getStudent()
    {
        return student;
    }

    /**
     * Sets the student represented by this instance.
     *
     * @param student The value to set.
     **/
    public void setStudent(Student student)
    {
        this.student = student;
    }

    /**
     * Gets the amount of guests represented by this instance.
     *
     * @return Returns the amount of guests represented by this instance.
     */
    public int getAmountOfGuests()
    {
        return amountOfGuests;
    }

    /**
     * Sets the amount of guests represented by this instance.
     *
     * @param amountOfGuests The value to set.
     **/
    public void setAmountOfGuests(int amountOfGuests)
    {
        this.amountOfGuests = amountOfGuests;
    }

    /**
     * Gets the meal represented by this instance.
     *
     * @return Returns the meal represented by this instance.
     */
    public Meal getMeal()
    {
        return meal;
    }

    /**
     * Sets the meal represented by this instance.
     *
     * @param meal The value to set.
     **/
    public void setMeal(Meal meal)
    {
        this.meal = meal;
    }

    /**
     * Converts the value of the current Meal object to its equivalent string representation.
     *
     * @return The string representation of the value of this instance.
     */
    @Override
    public String toString()
    {
        return "FellowEater{" +
                "ID=" + ID +
                ", student=" + student +
                ", amountOfGuests=" + amountOfGuests +
                ", mealID=" + meal.getID() +
                '}';
    }
}
