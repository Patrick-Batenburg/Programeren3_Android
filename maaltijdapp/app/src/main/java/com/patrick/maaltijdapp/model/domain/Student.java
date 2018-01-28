package com.patrick.maaltijdapp.model.domain;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Represents an student.
 */
public class Student implements Serializable
{
    private static final String TAG = Student.class.getSimpleName();

    private String studentNumber;
    private String firstName;
    private String insertion;
    private String lastName;
    private String password;
    private String emailAddress;
    private String phoneNumber;

    /**
     * Initializes an empty instance of the Student class.
     */
    public Student()
    {
    }

    /**
     * Initializes a new instance of the Student class specified by student number.
     *
     * @param studentNumber The student number of the student.
     */
    public Student(String studentNumber)
    {
        this.studentNumber = studentNumber;
    }

    /**
     * Initializes a new instance of the Student class specified by student number, first name, insertion, last name, email address and phone number.
     *
     * @param studentNumber The student number of the student.
     * @param firstName The first name of the student.
     * @param insertion The insertion of the student.
     * @param lastName The last name of the student.
     * @param emailAddress The email address of the student.
     * @param phoneNumber The phone number of the student.
     */
    public Student(String studentNumber, String firstName, String insertion, String lastName, String emailAddress, String phoneNumber)
    {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.insertion = insertion;

        if (this.insertion != null && !this.insertion.isEmpty() && !this.insertion.equals("null"))
        {
            this.insertion = insertion;
        }
        else
        {
            this.insertion = "";
        }

        this.lastName = lastName;
        this.password = "";
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the full name of the student in first name, insertion (optional) and last name format.
     *
     * @return Returns the full name of the student.
     */
    public String getFullName()
    {
        StringBuilder result = new StringBuilder();

        if (this.firstName != null && !this.firstName.isEmpty() && !this.lastName.equals("null"))
        {
            result.append(this.firstName);

        }

        if (this.insertion != null && !this.insertion.isEmpty() && !this.insertion.equals("null"))
        {
            result.append(" ");
            result.append(this.insertion);

        }

        if (this.lastName != null && !this.lastName.isEmpty() && !this.lastName.equals("null"))
        {
            result.append(" ");
            result.append(this.lastName);

        }

        return result.toString();
    }

    /**
     * Gets the student number represented by this instance.
     *
     * @return Returns the student number represented by this instance.
     */
    public String getStudentNumber()
    {
        return this.studentNumber;
    }

    /**
     * Sets the student number represented by this instance.
     *
     * @param studentNumber The value to set.
     **/
    public void setStudentNumber(String studentNumber)
    {
        this.studentNumber = studentNumber;
    }

    /**
     * Gets the first name represented by this instance.
     *
     * @return Returns the student number represented by this instance.
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * Sets the first name represented by this instance.
     *
     * @param firstName The value to set.
     **/
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Gets the insertion represented by this instance.
     *
     * @return Returns the insertion represented by this instance.
     */
    public String getInsertion()
    {
        return this.insertion;
    }

    /**
     * Sets the insertion represented by this instance.
     *
     * @param insertion The value to set.
     **/
    public void setInsertion(String insertion)
    {
        this.insertion = insertion;
    }

    /**
     * Gets the last name represented by this instance.
     *
     * @return Returns the last name represented by this instance.
     */
    public String getLastName()
    {
        return this.lastName;
    }

    /**
     * Sets the last name represented by this instance.
     *
     * @param lastName The value to set.
     **/
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Gets the password represented by this instance.
     *
     * @return Returns the password represented by this instance.
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * Sets the password represented by this instance.
     *
     * @param password The value to set.
     **/
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Gets the email address represented by this instance.
     *
     * @return Returns the email address represented by this instance.
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * Sets the email address represented by this instance.
     *
     * @param emailAddress The value to set.
     **/
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the phone number represented by this instance.
     *
     * @return Returns the phone number represented by this instance.
     */
    public String getPhoneNumber()
    {
        return this.phoneNumber;
    }

    /**
     * Sets the phone number represented by this instance.
     *
     * @param phoneNumber The value to set.
     **/
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns a value indicating whether this instance is equal to a specified object.
     *
     * @param o An object to compare with this instance.
     * @return true if o is an instance of Student and equals the value of this instance; otherwise, false.
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return this.studentNumber.equals(student.studentNumber);
    }

    /**
     * Returns the hash code for this instance.
     *
     * @return A 32-bit signed integer hash code.
     */
    @Override
    public int hashCode()
    {
        return studentNumber.hashCode();
    }

    /**
     * Converts the value of the current Student object to its equivalent string representation.
     *
     * @return The string representation of the value of this instance.
     */
    @Override
    public String toString()
    {
        return "Student{" +
                "studentNumber='" + studentNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", insertion='" + insertion + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
