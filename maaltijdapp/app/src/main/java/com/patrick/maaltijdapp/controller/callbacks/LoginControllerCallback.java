package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Occurs after a student has logged in successfully or unsuccessfully.
 */
public interface LoginControllerCallback
{
    /**
     * Occurs after a student has logged in successfully or unsuccessfully.
     *
     * @param response The result value of this callback.
     */
    void onLoginComplete(String response);

    /**
     * Occurs after a student has logged in successfully.
     */
    void onLoginSuccess();

    /**
     * Occurs after a student has logged in unsuccessfully.
     */
    void onLoginFailed();
}
