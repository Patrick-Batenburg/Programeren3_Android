package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Occurs after a fellow eater has been deleted successfully or unsuccessfully.
 */
public interface UnsubscribeControllerCallback
{
    /**
     * Occurs after a fellow eater has been deleted successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onUnsubscribeComplete(boolean result);
}
