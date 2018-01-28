package com.patrick.maaltijdapp.model.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.patrick.maaltijdapp.model.callbacks.APICallback;
import com.patrick.maaltijdapp.model.domain.FellowEater;
import com.patrick.maaltijdapp.model.domain.Meal;
import com.patrick.maaltijdapp.model.domain.Student;
import com.patrick.maaltijdapp.model.utils.DateTimeUtility;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patrick on 17/01/2018.
 */

/**
 * Represents an API service and allows you to connect to a server, manipulate it, or get information from it.
 */
public class APIService
{
    private static final String TAG = APIService.class.getSimpleName();
    private static final String URL_LOGIN = "http://prog4node.herokuapp.com/api/login";
    public static final String URL_REGISTER = "http://prog4node.herokuapp.com/api/register";
    private static final String URL_STUDENT = "http://prog4node.herokuapp.com/api/student";
    public static final String URL_STUDENT_SLASH = "http://prog4node.herokuapp.com/api/student/";
    private static final String URL_MEAL = "http://prog4node.herokuapp.com/api/meal";
    private static final String URL_MEAL_SLASH = "http://prog4node.herokuapp.com/api/meal/";
    private static final String URL_FELLOW_EATER = "http://prog4node.herokuapp.com/api/felloweater";
    private static final String URL_FELLOW_EATER_SLASH = "http://prog4node.herokuapp.com/api/felloweater/";
    private RequestQueue requestQueue;
    private APICallback apiCallback;
    private Context context;

    /**
     * Initializes a new instance of the APIService class from the specified instances of the Context and APICallback classes.
     *
     * @param context The Context object associated with the current state of the application for the current request.
     * @param apiCallback The event that can point to one or more functions.
     */
    public APIService(Context context, APICallback apiCallback)
    {
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        this.apiCallback = apiCallback;
        this.context = context;
    }

    /**
     * Provides elements for logging in to a server.
     *
     * @param studentNumber The student number that allows you to login.
     * @param password The password that allows you to login.
     */
    public void login(String studentNumber, String password)
    {
        Log.i(TAG, "Login Student Number: " + studentNumber);
        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put("studentNumber", studentNumber);
            jsonObject.put("password", password);
        }
        catch (JSONException e)
        {
            apiCallback.onLoggedIn("Error: JSONException");
            Log.e(TAG, "JSON Error Login: " + e.getMessage());
        }

        final JsonObjectRequest request = new JsonObjectRequest(Method.POST, URL_LOGIN, jsonObject, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if (response.getString("token") == null)
                    {
                        apiCallback.onLoggedIn("Error: invalid token");
                        Log.e(TAG, "Token Error Login: " + response);
                    }
                    else
                    {
                        apiCallback.onLoggedIn(response.getString("token"));
                        Log.i(TAG, "Token: " + response);
                    }
                }
                catch (JSONException e)
                {
                    apiCallback.onLoggedIn("Error: JSONException");
                    Log.e(TAG, "JSON Error Login: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                if (e.toString().equals("com.android.volley.AuthFailureError"))
                {
                    apiCallback.onLoggedIn("Error: invalid token");
                }
                else
                {
                    apiCallback.onLoggedIn("Error: connection error");
                }

                Log.e(TAG, "Volley Error Login: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Gets all the students from the server.
     */
    public void getStudents()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);
        final ArrayList<Student> students = new ArrayList<>();
        final int[] counter = {0};

        JsonArrayRequest request = new JsonArrayRequest(Method.GET, URL_STUDENT, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Student student = new Student(jsonObject.getString("StudentNumber"), jsonObject.getString("Firstname"), jsonObject.getString("Insertion"), jsonObject.getString("Lastname"), jsonObject.getString("Email"), jsonObject.getString("PhoneNumber"));
                        students.add(student);

                        if (++counter[0] == students.size())
                        {
                            apiCallback.onLoadedStudents(students);
                        }
                    }

                    Log.i(TAG, "Response GET Students: " + response);
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "JSON Error GET Students: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                if (e.networkResponse != null)
                {
                    if(e.networkResponse.statusCode == 401)
                    {
                        apiCallback.onTokenInvalid();
                    }
                    else
                    {
                        if (++counter[0] == students.size())
                        {
                            apiCallback.onLoadedStudents(students);
                        }
                    }
                }
                else
                {
                    if (++counter[0] == students.size())
                    {
                        apiCallback.onLoadedStudents(students);
                    }
                }

                Log.e(TAG, "Volley Error GET Students: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Creates the specified student.
     *
     * @param student The student to create.
     */
    public void createStudent(Student student)
    {
        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put("studentNumber", student.getStudentNumber());
            jsonObject.put("firstname", student.getFirstName());
            jsonObject.put("insertion", student.getInsertion());
            jsonObject.put("lastname", student.getLastName());
            jsonObject.put("password", student.getPassword());
            jsonObject.put("email", student.getEmailAddress());
            jsonObject.put("phonenumber", student.getPhoneNumber());
        }
        catch (JSONException e)
        {
            Log.e(TAG, "JSON Error POST Student: " + e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(Method.POST, URL_STUDENT, jsonObject, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                apiCallback.onRegisteredStudent(true);
                Log.i(TAG, "Response POST Student: " + response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                apiCallback.onRegisteredStudent(false);
                Log.e(TAG, "Volley Error POST Student: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Gets all the meals from the server.
     */
    public void getMeals()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);
        final ArrayList<Meal> meals = new ArrayList<>();

        JsonArrayRequest request = new JsonArrayRequest(Method.GET, URL_MEAL, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Meal meal = new Meal(jsonObject.getInt("ID"), jsonObject.getString("Dish"), jsonObject.getString("Info"), DateTimeUtility.iso8601ToDateTime(jsonObject.getString("DateTime")), new Student(jsonObject.getString("ChefID")), jsonObject.getDouble("Price"), jsonObject.getInt("MaxFellowEaters"), jsonObject.getInt("DoesCookEat") != 0);
                        DateTime now = new DateTime();

                        if (!meal.getDateTime().isAfter(now))
                        {
                            Log.i(TAG, "GET Meals Expired meal: " + meal.toString());
                        }
                        else
                        {
                            meals.add(meal);
                        }
                    }
                    catch (JSONException e)
                    {
                        Log.e(TAG, "JSON Error GET Meals: " + e.getMessage());
                    }
                }

                getMealImages(meals);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                if (e.networkResponse != null)
                {
                    if(e.networkResponse.statusCode == 401)
                    {
                        apiCallback.onTokenInvalid();
                    }
                    else
                    {
                        getMealImages(meals);
                    }
                }
                else
                {
                    getMealImages(meals);
                }

                Log.e(TAG, "Volley Error GET Meals: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Gets all the meal images for the specified meals from the server.
     *
     * @param meals The images of the meal to get.
     */
    private void getMealImages(final ArrayList<Meal> meals)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);
        final int[] counter = {0};
        File fileDir = context.getFilesDir();

        for (final Meal meal : meals)
        {
            final File file = new File(fileDir, "mealImages_" + meal.getID());

            if (!file.exists() && !file.isDirectory())
            {
                ImageRequest request = new ImageRequest(URL_MEAL_SLASH + meal.getID() + "/picture", new Response.Listener<Bitmap>()
                {
                    @Override
                    public void onResponse(Bitmap response)
                    {
                        try
                        {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            response.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        }
                        catch (IOException e)
                        {
                            Log.e(TAG, "IO Exception GET Meal Bitmaps: " + e.getMessage());
                        }

                        if (++counter[0] == meals.size())
                        {
                            apiCallback.onLoadedMeals(meals);
                        }

                        Log.i(TAG, "Response GET Meal Bitmaps: " + response);
                    }
                },  1024, 1024, null, Config.ARGB_8888,  new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError e)
                    {
                        if (e.networkResponse != null)
                        {
                            if(e.networkResponse.statusCode == 401)
                            {
                                apiCallback.onTokenInvalid();
                            }
                        }

                        if (++counter[0] == meals.size())
                        {
                            apiCallback.onLoadedMeals(meals);
                        }
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError
                    {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }
                };

                requestQueue.add(request);
            }
        }

        if (counter[0] != meals.size())
        {
            apiCallback.onLoadedMeals(meals);
        }
    }

    /**
     * Creates the specified meal.
     *
     * @param meal The meal to create.
     */
    public void createMeal(Meal meal)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);
        JSONObject jsonObject = new JSONObject();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        try
        {
            jsonObject.put("ChefID", meal.getChef().getStudentNumber());
            jsonObject.put("Dish", meal.getDish());
            jsonObject.put("Info",  meal.getDescription());
            jsonObject.put("DateTime", DateTimeUtility.dateTimeToISO8601(meal.getDateTime()));
            jsonObject.put("DoesCookEat", meal.isCookEating() ? 1 : 0);
            jsonObject.put("MaxFellowEaters", meal.getMaxAmountOfFellowEaters());
            jsonObject.put("Price", decimalFormat.format(meal.getPrice()));

            if (meal.getImage() != null)
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                meal.getImage().compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                byte[] byteArray = outputStream.toByteArray();

                jsonObject.put("Picture", Base64.encodeToString(byteArray, Base64.DEFAULT));
            }
        }
        catch (JSONException e)
        {
            apiCallback.onLoggedIn("error");
            Log.e(TAG, "JSON Error POST Meal: " + e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(Method.POST, URL_MEAL, jsonObject, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                apiCallback.onCreatedMeal(true);
                Log.i(TAG, "Response POST Meal: " + response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                if (e.networkResponse != null)
                {
                    if(e.networkResponse.statusCode == 401)
                    {
                        apiCallback.onTokenInvalid();
                    }
                    else
                    {
                        apiCallback.onCreatedMeal(false);
                    }
                }
                else
                {
                    apiCallback.onCreatedMeal(false);
                }

                Log.e(TAG, "Volley Error POST Meal: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Updates the specified meal.
     *
     * @param meal The meal to update.
     */
    public void updateMeal(Meal meal)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);
        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put("ID", meal.getID());
            jsonObject.put("ChefID", meal.getChef().getStudentNumber());
            jsonObject.put("Dish", meal.getDish());
            jsonObject.put("Info",  meal.getDescription());
            jsonObject.put("DateTime", DateTimeUtility.dateTimeToISO8601(meal.getDateTime()));
            jsonObject.put("DoesCookEat", meal.isCookEating() ? 1 : 0);
            jsonObject.put("MaxFellowEaters", meal.getMaxAmountOfFellowEaters());
            jsonObject.put("Price", meal.getPrice());

            if (meal.getImage() != null)
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                meal.getImage().compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                byte[] byteArray = outputStream.toByteArray();

                jsonObject.put("Picture", Base64.encodeToString(byteArray, Base64.DEFAULT));
            }
        }
        catch (JSONException e)
        {
            apiCallback.onLoggedIn("error");
            Log.e(TAG, "JSON Error PUT Meal: " + e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(Method.PUT, URL_MEAL, jsonObject, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                apiCallback.onUpdatedMeal(true);
                Log.i(TAG, "Response PUT Meal: " + response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                if (e.networkResponse != null)
                {
                    if (e.networkResponse.statusCode == 401)
                    {
                        apiCallback.onTokenInvalid();
                    }
                    else
                    {
                        apiCallback.onUpdatedMeal(false);
                    }
                }
                else
                {
                    apiCallback.onUpdatedMeal(false);
                }

                Log.e(TAG, "Volley Error PUT Meal: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Deletes the specified meal.
     *
     * @param meal The meal to delete.
     */
    public void deleteMeal(Meal meal)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);

        JsonObjectRequest request = new JsonObjectRequest(Method.DELETE, URL_MEAL_SLASH + meal.getID(), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                apiCallback.onDeletedMeal(true);
                Log.i(TAG, "Response DELETE Meal: " + response);
            }
        }, new Response.ErrorListener()
        {
            @Override

            public void onErrorResponse(VolleyError e)
            {
                if (e.networkResponse != null)
                {
                    if (e.networkResponse.statusCode == 401)
                    {
                        apiCallback.onTokenInvalid();
                    }
                    else
                    {
                        apiCallback.onDeletedMeal(false);
                    }
                }
                else
                {
                    apiCallback.onDeletedMeal(false);
                }

                Log.e(TAG, "Volley Error DELETE Meal: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);

                return headers;
            }
        };
        requestQueue.add(request);
    }

    /**
     * Gets all the fellow eaters from the server.
     **/
    public void getFellowEaters()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);
        final ArrayList<FellowEater> fellowEaters = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Method.GET, URL_FELLOW_EATER, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        FellowEater fellowEater = new FellowEater(jsonObject.getInt("ID"), new Student(jsonObject.getString("StudentNumber")), jsonObject.getInt("AmountOfGuests"), new Meal(jsonObject.getInt("MealID")));
                        fellowEaters.add(fellowEater);
                        apiCallback.onLoadedFellowEaters(fellowEaters);
                    }

                    Log.i(TAG, "Response GET Fellow Eaters: " + response);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "JSON Error GET Fellow Eaters: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                if (e.networkResponse != null)
                {
                    if (e.networkResponse.statusCode == 401)
                    {
                        apiCallback.onTokenInvalid();
                    }
                }

                Log.e(TAG, "Volley Error GET Fellow Eaters: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Creates the specified fellow eater.
     *
     * @param fellowEater The fellow eater to create.
     */
    public void createFellowEater(FellowEater fellowEater)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);
        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put("StudentNumber", fellowEater.getStudent().getStudentNumber());
            jsonObject.put("MealID", fellowEater.getMeal().getID());
            jsonObject.put("AmountOfGuests", fellowEater.getAmountOfGuests());
        }
        catch (JSONException e)
        {
            apiCallback.onLoggedIn("error");
            Log.e(TAG, "JSON Error POST Fellow Eater: " + e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(Method.POST, URL_FELLOW_EATER, jsonObject, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                apiCallback.onCreatedFellowEater(true);
                Log.i(TAG, "Response POST Fellow Eater: " + response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                if (e.networkResponse != null)
                {
                    if (e.networkResponse.statusCode == 401)
                    {
                        apiCallback.onTokenInvalid();
                    }
                    else
                    {
                        apiCallback.onCreatedFellowEater(false);
                    }
                }
                else
                {
                    apiCallback.onCreatedFellowEater(false);
                }

                Log.e(TAG, "Volley Error POST Fellow Eater: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Deletes the specified fellow eater.
     *
     * @param fellowEater The fellow eater to delete.
     */
    public void deleteFellowEater(FellowEater fellowEater)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("AUTHENTICATION_TOKEN", null);

        JsonObjectRequest request = new JsonObjectRequest(Method.DELETE, URL_FELLOW_EATER_SLASH + fellowEater.getID(), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                apiCallback.onDeletedFellowEater(true);
                Log.i(TAG, "Response DELETE Fellow Eater: " + response);
            }
        }, new Response.ErrorListener()
        {
            @Override

            public void onErrorResponse(VolleyError e)
            {
                if (e.networkResponse != null)
                {
                    if (e.networkResponse.statusCode == 401)
                    {
                        apiCallback.onTokenInvalid();
                    }
                    else
                    {
                        apiCallback.onDeletedFellowEater(false);
                    }
                }
                else
                {
                    apiCallback.onDeletedFellowEater(false);
                }

                Log.e(TAG, "Volley Error DELETE Fellow Eater: " + e.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }
}