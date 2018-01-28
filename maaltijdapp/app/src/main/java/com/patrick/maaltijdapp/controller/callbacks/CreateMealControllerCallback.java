package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Occurs after a meal has been created successfully or unsuccessfully.
 */
public interface CreateMealControllerCallback
{
    /**
     * Occurs after a meal has been created successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onCreateMealComplete(boolean result);
}
