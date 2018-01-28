package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Occurs after a refresh was successfully or unsuccessfully.
 */
public interface ReloadCallback
{
    /**
     * Occurs after a refresh was successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onReloadComplete(boolean result);

    /**
     * Refreshes the students in the application from persistent storage.
     **/
    void loadStudents();

    /**
     * Refreshes the meals in the application from persistent storage.
     **/
    void loadMeals();
}
