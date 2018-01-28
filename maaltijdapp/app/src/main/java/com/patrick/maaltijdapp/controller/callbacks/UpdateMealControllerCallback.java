package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 21/01/2018.
 */

/**
 * Occurs after a meal has been updated successfully or unsuccessfully.
 */
public interface UpdateMealControllerCallback
{
    /**
     * Occurs after a meal has been updated successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onUpdateMealComplete(boolean result);
}
