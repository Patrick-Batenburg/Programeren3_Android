package com.patrick.maaltijdapp.model.data;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Provides specific data operations
 */
public class DAOFactory
{
    private static final String TAG = DAOFactory.class.getSimpleName();

    private SQLiteLocalDatabase db;

    /**
     * Initializes a new instance of the DAOFactory class from the specified instance of the SQLiteLocalDatabase class.
     *
     * @param db The SQLite database to use.
     */
    public DAOFactory(SQLiteLocalDatabase db)
    {
        this.db = db;
    }

    /**
     * @return Returns a new StudentDAO
     */
    public StudentDAO getStudentDAO()
    {
        return new StudentDAO(db);
    }

    /**
     * @return Returns a new MealDAO
     */
    public MealDAO getMealDAO()
    {
        return new MealDAO(db, getStudentDAO());
    }

    /**
     * @return Returns a new FellowEaterDAO
     */
    public FellowEaterDAO getFellowEaterDAO()
    {
        return new FellowEaterDAO(db, getStudentDAO(), getMealDAO());
    }
}
