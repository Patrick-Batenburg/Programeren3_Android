package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Occurs after a student has been registered successfully or unsuccessfully.
 */
public interface RegisterControllerCallback
{
    /**
     * Occurs after a student has been registered successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    void onRegisterComplete(boolean result);

    /**
     * Occurs after a student has been registered successfully.
     **/
    void onRegisterSuccess();

    /**
     * Occurs after a student has been registered unsuccessfully.
     */
    void onRegisterFailed();
}
