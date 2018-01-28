package com.patrick.maaltijdapp.model.callbacks;

import com.patrick.maaltijdapp.model.domain.FellowEater;
import com.patrick.maaltijdapp.model.domain.Meal;
import com.patrick.maaltijdapp.model.domain.Student;

import java.util.ArrayList;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Provides methods that respond to when create, update, delete events occur.
 */
public interface APICallback
{
    /**
     * Occurs after students are needed to be loaded in the database.
     *
     * @param students The new students to create.
     */
    void onLoadedStudents(ArrayList<Student> students);

    /**
     * Occurs after a student has been registered successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onRegisteredStudent(boolean result);

    /**
     * Occurs after a student has logged in successfully or unsuccessfully.
     *
     * @param response The response value of this callback.
     */
    void onLoggedIn(String response);

    /**
     * Occurs after a token has expired or is invalid.
     **/
    void onTokenInvalid();

    /**
     * Occurs after meals are needed to be loaded in the database.
     *
     * @param meals The new meals to create.
     */
    void onLoadedMeals(ArrayList<Meal> meals);

    /**
     * Occurs after a meal has been created successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onCreatedMeal(boolean result);

    /**
     * Occurs after a meal has been updated successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onUpdatedMeal(boolean result);

    /**
     * Occurs after a meal has been deleted successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onDeletedMeal(boolean result);

    /**
     * Occurs after fellow eaters are needed to be loaded in the database.
     *
     * @param fellowEaters The new fellow eaters to create.
     */
    void onLoadedFellowEaters(ArrayList<FellowEater> fellowEaters);

    /**
     * Occurs after a fellow eater has been created successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onCreatedFellowEater(boolean result);

    /**
     * Occurs after a fellow eater has been deleted successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onDeletedFellowEater(boolean result);
}
