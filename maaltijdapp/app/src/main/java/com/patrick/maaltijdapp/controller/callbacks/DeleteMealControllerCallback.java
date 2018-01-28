package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Occurs after a meal has been deleted successfully or unsuccessfully.
 */
public interface DeleteMealControllerCallback
{
    /**
     * Occurs after a meal has been deleted successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onDeleteMealComplete(boolean result);
}
