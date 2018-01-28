package com.patrick.maaltijdapp.controller.callbacks;

/**
 * Created by Patrick on 21/01/2018.
 */

/**
 * Occurs after a token has expired or is invalid.
 **/
public interface InvalidTokenCallback
{
    /**
     * Occurs after a token has expired or is invalid.
     **/
    void onTokenInvalid();
}
