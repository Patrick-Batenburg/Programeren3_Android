package com.patrick.maaltijdapp.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.patrick.maaltijdapp.R;
import com.patrick.maaltijdapp.controller.callbacks.InvalidTokenCallback;
import com.patrick.maaltijdapp.controller.callbacks.ReloadCallback;
import com.patrick.maaltijdapp.model.domain.AppController;
import com.patrick.maaltijdapp.model.domain.Meal;
import com.patrick.maaltijdapp.view.MealAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base class for activities that use the support library action bar features.
 */
public class MealsActivity extends AppCompatActivity implements ReloadCallback, InvalidTokenCallback
{
    private static final String TAG = MealsActivity.class.getSimpleName();
    private static final int REQUEST_CREATE_MEAL = 1;

    @BindView(R.id.meals_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.meal_list_view)
    ListView mealListView;

    @BindView(R.id.input_button_create_meal)
    Button createMealButton;

    private AppController appController;
    private ArrayAdapter arrayAdapter;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        ButterKnife.bind(this);
        appController = new AppController(this, this);
        loadStudents();
        loadMeals();
        arrayAdapter = new MealAdapter(this, R.layout.list_view_meal_row, appController.getMeals());
        mealListView.setAdapter(arrayAdapter);

        mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                getMealDetailsHandler(adapterView, i);
            }
        });

        createMealButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createMealHandler();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadMeals();
            }
        });
    }

    /**
     * Represents the method that will handle a get meal details request.
     *
     * @param adapterView The AdapterView where the click happened.
     * @param i The row id of the item that was clicked.
     */
    private void getMealDetailsHandler(AdapterView<?> adapterView, int i)
    {
        Meal meal = (Meal) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(this, MealDetailsActivity.class);
        intent.putExtra("Meal", meal);
        startActivity(intent);
    }

    /**
     * Represents the method that will handle a create meal request.
     */
    private void createMealHandler()
    {
        Intent intent = new Intent(this, MealCreateActivity.class);
        intent.putExtra("REQUEST_CREATE_MEAL", REQUEST_CREATE_MEAL);
        startActivity(intent);
    }

    /**
     * Refreshes the students in the application from persistent storage.
     **/
    @Override
    public void loadStudents()
    {
        appController.loadStudents(this);
    }

    /**
     * Refreshes the meals in the application from persistent storage.
     **/
    @Override
    public void loadMeals()
    {
        appController.loadMeals(this);
    }

    /**
     * Called after Activity.OnRestoreInstanceState(Bundle), Activity.OnRestart, or Activity.OnPause, for your activity to start interacting with the user.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("RELOAD", true))
        {
            loadStudents();
            loadMeals();
            arrayAdapter.clear();
            arrayAdapter.addAll(appController.getMeals());
            arrayAdapter.notifyDataSetChanged();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("RELOAD", false);
            editor.apply();
        }
    }

    /**
     * Occurs after a refresh was successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    @Override
    public void onReloadComplete(boolean result)
    {
        if (result)
        {
            arrayAdapter.clear();
            //mealListView.invalidateViews();
            arrayAdapter.addAll(appController.getMeals());
            arrayAdapter.notifyDataSetChanged();

            if (swipeRefreshLayout.isRefreshing())
            {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, R.string.message_reload_success, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.error_reload, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Occurs after a token has expired or is invalid.
     **/
    @Override
    public void onTokenInvalid()
    {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        Toast.makeText(this, getText(R.string.message_token_invalid), Toast.LENGTH_SHORT).show();
    }
}