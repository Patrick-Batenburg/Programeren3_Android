package com.patrick.maaltijdapp.model.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.patrick.maaltijdapp.model.domain.FellowEater;
import com.patrick.maaltijdapp.model.domain.Meal;
import com.patrick.maaltijdapp.model.domain.Student;
import com.patrick.maaltijdapp.model.utils.DateTimeUtility;

import java.util.ArrayList;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Provides specific data operations for fellow eaters.
 */
public class FellowEaterDAO implements DAO<FellowEater>
{
    private static final String TAG = FellowEaterDAO.class.getSimpleName();
    private SQLiteLocalDatabase db;
    private StudentDAO studentDAO;
    private MealDAO mealDAO;

    /**
     * Initializes a new instance of the FellowEaterDAO class from the specified instances of the SQLiteLocalDatabase, StudentDAO and MealDAO classes.
     *
     * @param db The SQLite database to use.
     * @param studentDAO The student DAO to use.
     * @param mealDAO The meal DAO to use.
     */
    public FellowEaterDAO(SQLiteLocalDatabase db, StudentDAO studentDAO, MealDAO mealDAO)
    {
        this.db = db;
        this.studentDAO = studentDAO;
        this.mealDAO = mealDAO;
    }

    /**
     * View the details of the fellow eater.
     *
     * @param ID The fellow eater ID to find.
     * @return Returns a fellow eater.
     */
    @Override
    public FellowEater findDetails(int ID)
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID, AmountOfGuests, StudentNumber, MealID FROM FellowEaters WHERE ID = " + ID, null);
        FellowEater fellowEater = null;

        if (cursor.moveToFirst())
        {
            fellowEater = new FellowEater();
            fellowEater.setID(cursor.getInt(0));
            fellowEater.setAmountOfGuests(cursor.getInt(1));
            fellowEater.setStudent(studentDAO.findDetails(cursor.getInt(2)));
            fellowEater.setMeal(mealDAO.findDetails(cursor.getInt(3)));
        }

        cursor.close();
        db.close();
        return fellowEater;
    }

    /**
     * View the details of all the fellow eaters.
     *
     * @return Returns a list of fellow eaters.
     */
    @Override
    public ArrayList<FellowEater> findAllDetails()
    {
        ArrayList<FellowEater> fellowEaters = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT FellowEaters.ID, AmountOfGuests, FellowEaters.StudentNumber, FirstName, Insertion, LastName, Email, PhoneNumber, MealID, Dish, DateTime, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat FROM FellowEaters INNER JOIN Students ON FellowEaters.StudentNumber = Students.StudentNumber INNER JOIN Meals ON Meals.ID = FellowEaters.MealID ORDER BY FellowEaters.ID", null);

        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                FellowEater fellowEater = new FellowEater();
                fellowEater.setID(cursor.getInt(0));
                fellowEater.setAmountOfGuests(cursor.getInt(1));
                fellowEater.setStudent(new Student(cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
                fellowEater.setMeal(new Meal(cursor.getInt(8), cursor.getString(9), cursor.getString(10), DateTimeUtility.iso8601ToDateTime(cursor.getString(11)), new Student(), cursor.getDouble(14), cursor.getInt(15), Boolean.parseBoolean(cursor.getString(16))));
                fellowEaters.add(fellowEater);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return fellowEaters;
    }

    /**
     * Creates a new fellow eater entry in the database.
     *
     * @param fellowEater The new fellow eater to create.
     */
    @Override
    public void create(FellowEater fellowEater)
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", fellowEater.getID());
        values.put("AmountOfGuests", fellowEater.getAmountOfGuests());
        values.put("StudentNumber", fellowEater.getStudent().getStudentNumber());
        values.put("MealID", fellowEater.getMeal().getID());

        if (db.insert("FellowEaters", "ID, AmountOfGuests, StudentNumber, MealID", values) == 0)
        {
            Log.i(TAG, "Error inserting fellow eater: " + fellowEater.toString());
        }

        db.close();
    }

    /**
     * Creates new fellow eater entries in the database.
     *
     * @param fellowEaters The new fellow eaters to create.
     */
    @Override
    public void createBulk(ArrayList<FellowEater> fellowEaters)
    {
        for (FellowEater fellowEater : fellowEaters)
        {
            create(fellowEater);
        }
    }

    /**
     * Deletes all existing fellow eaters from the database.
     */
    @Override
    public void clear()
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM FellowEaters;");
        db.close();
    }
}