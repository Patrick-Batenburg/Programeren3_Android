package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Occurs after a fellow eater has been created successfully or unsuccessfully.
 */
public interface SubscribeControllerCallback
{
    /**
     * Occurs after a fellow eater has been created successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onSubscribeComplete(boolean result);
}
