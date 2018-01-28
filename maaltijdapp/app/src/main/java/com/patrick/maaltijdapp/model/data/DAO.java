package com.patrick.maaltijdapp.model.data;

import java.util.ArrayList;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Provides specific data operations
 */
public interface DAO<T>
{
    /**
     * View the details of the specified type.
     *
     * @param ID The ID of the specified type find.
     * @return Returns a object of the specified type.
     */
    T findDetails(int ID);

    /**
     * View the details of the specified type.
     *
     * @return Returns a list of objects of the specified type.
     */
    ArrayList<T> findAllDetails();

    /**
     * Creates a new objects entry of the specified type in the database.
     *
     * @param object The new object to create.
     */
    void create(T object);

    /**
     * Creates new objects entries of the specified type in the database.
     *
     * @param objects The new objects to create.
     */
    void createBulk(ArrayList<T> objects);

    /**
     * Deletes all existing objects of the specified type from the database.
     */
    void clear();
}
