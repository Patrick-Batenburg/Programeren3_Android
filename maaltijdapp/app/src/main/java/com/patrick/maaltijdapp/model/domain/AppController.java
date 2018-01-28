package com.patrick.maaltijdapp.model.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.patrick.maaltijdapp.controller.callbacks.CreateMealControllerCallback;
import com.patrick.maaltijdapp.controller.callbacks.DeleteMealControllerCallback;
import com.patrick.maaltijdapp.controller.callbacks.InvalidTokenCallback;
import com.patrick.maaltijdapp.controller.callbacks.LoginControllerCallback;
import com.patrick.maaltijdapp.controller.callbacks.RegisterControllerCallback;
import com.patrick.maaltijdapp.controller.callbacks.ReloadCallback;
import com.patrick.maaltijdapp.controller.callbacks.SubscribeControllerCallback;
import com.patrick.maaltijdapp.controller.callbacks.UnsubscribeControllerCallback;
import com.patrick.maaltijdapp.controller.callbacks.UpdateMealControllerCallback;
import com.patrick.maaltijdapp.model.callbacks.APICallback;
import com.patrick.maaltijdapp.model.data.DAO;
import com.patrick.maaltijdapp.model.data.DAOFactory;
import com.patrick.maaltijdapp.model.data.SQLiteLocalDatabase;
import com.patrick.maaltijdapp.model.services.APIService;

import java.util.ArrayList;

/**
 * Created by Patrick on 16/01/2018.
 */

/**
 * Provides methods that respond to GET, create, update, delete and refresh requests that are made to an NodeJS server.
 */
public class AppController implements APICallback
{
    private static final String TAG = AppController.class.getSimpleName();
    private Context context;
    private APIService apiService;
    private DAOFactory daoFactory;
    private LoginControllerCallback loginCallback;
    private ReloadCallback reloadCallback;
    private RegisterControllerCallback registerCallback;
    private SubscribeControllerCallback subscribeControllerCallback;
    private UnsubscribeControllerCallback unsubscribeControllerCallback;
    private CreateMealControllerCallback createMealControllerCallback;
    private DeleteMealControllerCallback deleteMealControllerCallback;
    private UpdateMealControllerCallback updateMealControllerCallback;
    private InvalidTokenCallback invalidTokenCallback;
    private String studentNumber;

    /**
     * Initializes a new instance of the AppController class from the specified instance of the Context class.
     *
     * @param context The Context object associated with the current state of the application for the current request.
     */
    public AppController(Context context)
    {
        this.context = context;
        daoFactory = new DAOFactory(new SQLiteLocalDatabase(context));
        apiService = new APIService(context, this);
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        studentNumber = sharedPreferences.getString("STUDENT_NUMBER", "");
    }

    /**
     * Initializes a new instance of the AppController class from the specified instances of the Context and InvalidTokenCallback classes.
     *
     * @param context The Context object associated with the current state of the application for the current request.
     * @param invalidTokenCallback The event that occurs after a token has expired or is invalid.
     */
    public AppController(Context context, InvalidTokenCallback invalidTokenCallback)
    {
        this.context = context;
        this.invalidTokenCallback = invalidTokenCallback;
        daoFactory = new DAOFactory(new SQLiteLocalDatabase(context));
        apiService = new APIService(context, this);
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        studentNumber = sharedPreferences.getString("STUDENT_NUMBER", "");
    }

    /**
     * Gets the current logged in student from the database.
     */
    public Student getStudent()
    {
        return daoFactory.getStudentDAO().findDetails(Integer.parseInt(studentNumber));
    }

    /**
     * Gets all the students from the database.
     */
    public ArrayList<Student> getStudents()
    {
        return daoFactory.getStudentDAO().findAllDetails();
    }

    /**
     * Provides elements for logging in to a server.
     *
     * @param studentNumber The student number that allows you to login.
     * @param password The password that allows you to login.
     * @param loginControllerCallback The event that occurs after a student has logged in successfully or unsuccessfully.
     */
    public void login(String studentNumber, String password, LoginControllerCallback loginControllerCallback)
    {
        this.studentNumber = studentNumber;
        loginCallback = loginControllerCallback;
        apiService.login(studentNumber, password);
    }

    /**
     * Provides elements to register a student to a server.
     *
     * @param student The student to register.
     * @param registerControllerCallback The event that occurs after a student has registered successfully or unsuccessfully.
     */
    public void register(Student student, RegisterControllerCallback registerControllerCallback)
    {
        this.registerCallback = registerControllerCallback;
        apiService.createStudent(student);
    }

    /**
     * Refreshes the students in the application from persistent storage.
     *
     * @param reloadCallback The event that occurs after a reload has initiated.
     */
    public void loadStudents(ReloadCallback reloadCallback)
    {
        this.reloadCallback = reloadCallback;
        apiService.getStudents();
    }

    /**
     * Gets all the meals from the database.
     */
    public ArrayList<Meal> getMeals()
    {
        return daoFactory.getMealDAO().findAllDetails();
    }

    /**
     * Gets the meal from the database.
     *
     * @param ID The meal ID to find.
     */
    public Meal getMeal(int ID)
    {
        return daoFactory.getMealDAO().findDetails(ID);
    }

    /**
     * Creates the specified meal.
     *
     * @param meal The meal to create
     * @param createMealControllerCallback The event that occurs after a meal is created.
     */
    public void createMeal(Meal meal, CreateMealControllerCallback createMealControllerCallback)
    {
        this.createMealControllerCallback = createMealControllerCallback;
        apiService.createMeal(meal);
    }

    /**
     * Updates the specified meal.
     *
     * @param meal The meal to update
     * @param updateMealControllerCallback The event that occurs after a meal is updated.
     */
    public void updatedMeal(Meal meal, UpdateMealControllerCallback updateMealControllerCallback)
    {
        this.updateMealControllerCallback = updateMealControllerCallback;
        apiService.updateMeal(meal);
    }

    /**
     * Deletes the specified meal.
     *
     * @param meal The meal to delete
     * @param deleteMealControllerCallback The event that occurs after a meal is deleted.
     */
    public void deleteMeal(Meal meal, DeleteMealControllerCallback deleteMealControllerCallback)
    {
        this.deleteMealControllerCallback = deleteMealControllerCallback;
        apiService.deleteMeal(meal);
    }

    /**
     * Refreshes the meals in the application from persistent storage.
     *
     * @param reloadCallback The event that occurs after a reload has initiated.
     */
    public void loadMeals(ReloadCallback reloadCallback)
    {
        this.reloadCallback = reloadCallback;
        apiService.getMeals();
        apiService.getFellowEaters();
    }

    /**
     * Gets all the fellow eaters from the database.
     */
    public ArrayList<FellowEater> getFellowEaters()
    {
        return daoFactory.getFellowEaterDAO().findAllDetails();
    }

    /**
     * Gets the fellow eater from the database.
     *
     * @param ID The fellow eater ID to find.
     */
    public FellowEater getFellowEater(int ID)
    {
        return daoFactory.getFellowEaterDAO().findDetails(ID);
    }

    /**
     * Creates the specified fellow eater.
     *
     * @param fellowEater The fellow eater to create
     * @param subscribeControllerCallback The event that occurs after a fellow eater is created.
     */
    public void createFellowEater(FellowEater fellowEater, SubscribeControllerCallback subscribeControllerCallback)
    {
        this.subscribeControllerCallback = subscribeControllerCallback;
        apiService.createFellowEater(fellowEater);
    }

    /**
     * Deletes the specified fellow eater.
     *
     * @param fellowEater The fellow eater to delete
     * @param unsubscribeControllerCallback The event that occurs after a fellow eater is deleted.
     */
    public void deleteFellowEater(FellowEater fellowEater, UnsubscribeControllerCallback unsubscribeControllerCallback)
    {
        this.unsubscribeControllerCallback = unsubscribeControllerCallback;
        apiService.deleteFellowEater(fellowEater);
    }

    /**
     * Occurs after a student has logged in successfully or unsuccessfully.
     *
     * @param response Gets the response value of this callback.
     */
    @Override
    public void onLoggedIn(String response)
    {
        if (response.equals("Error: invalid token") || response.equals("Error: JSONException") || response.equals("Error: connection error"))
        {
            loginCallback.onLoginComplete(response);
        }
        else
        {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("AUTHENTICATION_TOKEN", response);
            editor.putString("STUDENT_NUMBER", studentNumber);
            loginCallback.onLoginComplete("success");
            editor.apply();
        }
    }

    /**
     * Occurs after a token has expired or is invalid.
     **/
    @Override
    public void onTokenInvalid()
    {
        invalidTokenCallback.onTokenInvalid();
    }

    /**
     * Occurs after students are needed to be loaded in the database.
     *
     * @param students The new students to create.
     */
    @Override
    public void onLoadedStudents(ArrayList<Student> students)
    {
        DAO<Student> studentDAO = daoFactory.getStudentDAO();
        studentDAO.clear();
        studentDAO.createBulk(students);

        reloadCallback.onReloadComplete(true);
    }

    /**
     * Occurs after a student has been registered successfully or unsuccessfully.
     *
     * @param result Gets the result value of this callback.
     */
    @Override
    public void onRegisteredStudent(boolean result)
    {
        registerCallback.onRegisterComplete(result);
    }

    /**
     * Occurs after meals are needed to be loaded in the database.
     *
     * @param meals The new meals to create.
     */
    @Override
    public void onLoadedMeals(ArrayList<Meal> meals)
    {
        DAO<Meal> mealDAO = daoFactory.getMealDAO();
        mealDAO.clear();
        mealDAO.createBulk(meals);
        reloadCallback.onReloadComplete(true);
    }

    /**
     * Occurs after a meal has been created successfully or unsuccessfully.
     *
     * @param result Gets the result value of this callback.
     */
    @Override
    public void onCreatedMeal(boolean result)
    {
        createMealControllerCallback.onCreateMealComplete(result);
    }

    /**
     * Occurs after a meal has been updated successfully or unsuccessfully.
     *
     * @param result Gets the result value of this callback.
     */
    @Override
    public void onUpdatedMeal(boolean result)
    {
        updateMealControllerCallback.onUpdateMealComplete(result);
    }

    /**
     * Occurs after a meal has been deleted successfully or unsuccessfully.
     *
     * @param result Gets the result value of this callback.
     */
    @Override
    public void onDeletedMeal(boolean result)
    {
        deleteMealControllerCallback.onDeleteMealComplete(result);
    }

    /**
     * Occurs after fellow eaters are needed to be loaded in the database.
     *
     * @param fellowEaters The new fellow eaters to create.
     */
    @Override
    public void onLoadedFellowEaters(ArrayList<FellowEater> fellowEaters)
    {
        DAO<FellowEater> fellowEaterDAO = daoFactory.getFellowEaterDAO();
        fellowEaterDAO.clear();
        fellowEaterDAO.createBulk(fellowEaters);
        reloadCallback.onReloadComplete(true);
    }

    /**
     * Occurs after a fellow eater has been created successfully or unsuccessfully.
     *
     * @param result Gets the result value of this callback.
     */
    @Override
    public void onCreatedFellowEater(boolean result)
    {
        subscribeControllerCallback.onSubscribeComplete(result);
    }

    /**
     * Occurs after a fellow eater has been deleted successfully or unsuccessfully.
     *
     * @param result Gets the result value of this callback.
     */
    @Override
    public void onDeletedFellowEater(boolean result)
    {
        unsubscribeControllerCallback.onUnsubscribeComplete(result);
    }
}
