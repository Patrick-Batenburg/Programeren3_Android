package com.patrick.maaltijdapp.model.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.patrick.maaltijdapp.model.domain.FellowEater;
import com.patrick.maaltijdapp.model.domain.Meal;
import com.patrick.maaltijdapp.model.domain.Student;
import com.patrick.maaltijdapp.model.utils.DateTimeUtility;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Provides specific data operations for meals.
 */
public class MealDAO implements DAO<Meal>
{
    private static final String TAG = MealDAO.class.getSimpleName();
    private SQLiteLocalDatabase db;
    private StudentDAO studentDAO;
    private DecimalFormat decimalFormat;

    /**
     * Initializes a new instance of the MealDAO class from the specified instances of the SQLiteLocalDatabase and StudentDAO classes.
     *
     * @param db The SQLite database to use.
     * @param studentDAO The student DAO to use.
     */
    public MealDAO(SQLiteLocalDatabase db, StudentDAO studentDAO)
    {
        this.db = db;
        this.studentDAO = studentDAO;
        decimalFormat = new DecimalFormat("#0.00");
    }

    /**
     * View the details of the meal.
     *
     * @param ID The meal ID to find.
     * @return Returns a meal.
     */
    @Override
    public Meal findDetails(int ID)
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID, Dish, DateTime, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat FROM Meals WHERE ID =" + ID + ";", null);
        Meal meal = null;

        if (cursor.moveToFirst())
        {
            meal = new Meal();
            meal.setID(cursor.getInt(0));
            meal.setDish(cursor.getString(1));
            meal.setDateTime(DateTimeUtility.iso8601ToDateTime(cursor.getString(2)));
            meal.setDescription(cursor.getString(3));
            meal.setChef(studentDAO.findDetails(cursor.getInt(4)));
            //meal.setImage(cursorMeal.getBlob(5));
            meal.setPrice(cursor.getDouble(6));
            meal.setMaxAmountOfFellowEaters(cursor.getInt(7));
            meal.setIsCookEating((cursor.getInt(8) != 0));
        }

        cursor.close();
        db.close();
        return meal;
    }

    /**
     * View the details of all the meals.
     *
     * @return Returns a list of meals.
     */
    @Override
    public ArrayList<Meal> findAllDetails()
    {
        ArrayList<Meal> meals = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursorMeal = db.rawQuery("SELECT ID, Dish, DateTime, Info, ChefID, FirstName, Insertion, LastName, Email, PhoneNumber, Picture, Price, MaxFellowEaters, DoesCookEat FROM Meals INNER JOIN Students ON Meals.ChefID = Students.StudentNumber WHERE DateTime >= Date('now') ORDER BY DateTime;", null);

        if (cursorMeal.moveToFirst())
        {
            while (!cursorMeal.isAfterLast())
            {
                Meal meal = new Meal();
                meal.setID(cursorMeal.getInt(0));
                meal.setDish(cursorMeal.getString(1));

                try
                {
                    meal.setDateTime(DateTimeUtility.iso8601ToDateTime(cursorMeal.getString(2)));
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    meal.setDateTime(DateTimeUtility.iso8601ToDateTime(cursorMeal.getString(2)));
                }

                meal.setDescription(cursorMeal.getString(3));
                meal.setChef(new Student(cursorMeal.getString(4), cursorMeal.getString(5), cursorMeal.getString(6), cursorMeal.getString(7), cursorMeal.getString(8), cursorMeal.getString(9)));
                //meal.setImage(cursorMeal.getBlob(10));
                meal.setPrice(cursorMeal.getDouble(11));
                meal.setMaxAmountOfFellowEaters(cursorMeal.getInt(12));
                meal.setIsCookEating(cursorMeal.getInt(13) != 0);

                Cursor cursorFellowEater = db.rawQuery("SELECT FellowEaters.ID, AmountOfGuests, Students.StudentNumber, FirstName, LastName, Insertion, Email, PhoneNumber FROM FellowEaters INNER JOIN Students ON Students.StudentNumber = FellowEaters.StudentNumber WHERE FellowEaters.MealID = " + meal.getID(), null);

                if (cursorFellowEater.moveToFirst())
                {
                    while (!cursorFellowEater.isAfterLast())
                    {
                        FellowEater fellowEater = new FellowEater();
                        fellowEater.setMeal(meal);
                        fellowEater.setID(cursorFellowEater.getInt(0));
                        fellowEater.setAmountOfGuests(cursorFellowEater.getInt(1));
                        fellowEater.setStudent(new Student(cursorFellowEater.getString(2), cursorFellowEater.getString(3), cursorFellowEater.getString(4), cursorFellowEater.getString(5), cursorFellowEater.getString(6), cursorFellowEater.getString(7)));
                        meal.addFellowEater(fellowEater);
                        cursorFellowEater.moveToNext();
                    }
                }

                cursorFellowEater.close();
                meals.add(meal);
                cursorMeal.moveToNext();
            }
        }

        cursorMeal.close();
        return meals;
    }

    /**
     * Creates a new meal entry in the database.
     *
     * @param meal The new meal to create.
     */
    @Override
    public void create(Meal meal)
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", meal.getID());
        values.put("Dish", meal.getDish());
        values.put("DateTime", meal.getDateTime().toString("yyyy-MM-dd HH:mm"));
        values.put("Info", meal.getDescription());
        values.put("ChefID", meal.getChef().getStudentNumber());
        values.put("Picture","");
        /*if (meal.getImage() != null)
        {
            values.put(SQLiteLocalDatabase.COLUMN_PICTURE, ImageUtility.getBytes(meal.getImage()));
        }
        else
        {
            values.put(SQLiteLocalDatabase.COLUMN_PICTURE, "");
        }*/

        values.put("Price", decimalFormat.format(meal.getPrice()));
        values.put("MaxFellowEaters", meal.getMaxAmountOfFellowEaters());
        values.put("DoesCookEat", meal.isCookEating() ? 1 : 0);

        if (db.insert("Meals", "ID, Dish, DateTime, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat", values) == 0)
        {
            Log.i(TAG, "Error inserting meal: " + meal.toString());
        }

        db.close();
    }

    /**
     * Creates new meals entrie in the database.
     *
     * @param meals The new meals to create.
     */
    @Override
    public void createBulk(ArrayList<Meal> meals)
    {
        for (Meal meal : meals)
        {
            create(meal);
        }
    }

    /**
     * Deletes all existing meals from the database.
     */
    @Override
    public void clear()
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM Meals;");
        db.close();
    }
}