package com.patrick.maaltijdapp.model.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.patrick.maaltijdapp.model.domain.Student;

import java.util.ArrayList;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Provides specific data operations for students.
 */
public class StudentDAO implements DAO<Student>
{
    private static final String TAG = StudentDAO.class.getSimpleName();
    private SQLiteLocalDatabase db;

    /**
     * Initializes a new instance of the StudentDAO class from the specified instance of the SQLiteLocalDatabase class.
     *
     * @param db The SQLite database to use.
     */
    public StudentDAO(SQLiteLocalDatabase db)
    {
        this.db = db;
    }

    /**
     * View the details of the student.
     *
     * @param ID The student number to find.
     * @return Returns a student.
     */
    @Override
    public Student findDetails(int ID)
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Students WHERE StudentNumber = " + ID + ";", null);
        Student student = null;

        if (cursor.moveToFirst())
        {
            student = new Student();
            student.setStudentNumber(cursor.getString(0));
            student.setFirstName(cursor.getString(1));
            student.setInsertion(cursor.getString(2));
            student.setLastName(cursor.getString(3));
            student.setEmailAddress(cursor.getString(4));
            student.setPhoneNumber(cursor.getString(5));
        }

        cursor.close();
        db.close();
        return student;
    }

    /**
     * View the details of all the students.
     *
     * @return Returns a list of students.
     */
    @Override
    public ArrayList<Student> findAllDetails()
    {
        ArrayList<Student> students = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT StudentNumber, FirstName, Insertion, LastName, Email, PhoneNumber FROM Students ORDER BY StudentNumber;", null);

        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                Student student = new Student();
                student.setStudentNumber(cursor.getString(0));
                student.setFirstName(cursor.getString(1));
                student.setInsertion(cursor.getString(2));
                student.setLastName(cursor.getString(3));
                student.setEmailAddress(cursor.getString(4));
                student.setPhoneNumber(cursor.getString(5));
                students.add(student);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return students;
    }

    /**
     * Creates a new student entries in the database.
     *
     * @param student The new student to create.
     */
    @Override
    public void create(Student student)
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StudentNumber", student.getStudentNumber());
        values.put("FirstName", student.getFirstName());
        values.put("Insertion", student.getInsertion());
        values.put("LastName", student.getLastName());
        values.put("Email", student.getEmailAddress());
        values.put("PhoneNumber", student.getPhoneNumber());

        if (db.insert("Students", "StudentNumber, Firstname, Insertion, Lastname, Email, PhoneNumber", values) == 0)
        {
            Log.i(TAG, "Error inserting student: " + student.toString());
        }

        db.close();
    }

    /**
     * Creates new students entries in the database.
     *
     * @param students The new students to create.
     */
    @Override
    public void createBulk(ArrayList<Student> students)
    {
        for (Student student : students)
        {
            create(student);
        }
    }

    /**
     * Deletes all existing students from the database.
     */
    @Override
    public void clear()
    {
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM Students;");
        db.close();
    }
}