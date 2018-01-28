package com.patrick.maaltijdapp.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.patrick.maaltijdapp.R;
import com.patrick.maaltijdapp.controller.callbacks.InvalidTokenCallback;
import com.patrick.maaltijdapp.controller.callbacks.SubscribeControllerCallback;
import com.patrick.maaltijdapp.model.domain.AppController;
import com.patrick.maaltijdapp.model.domain.FellowEater;
import com.patrick.maaltijdapp.model.domain.Meal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base class for activities that use the support library action bar features.
 */
public class MealSubscribeActivity extends AppCompatActivity implements SubscribeControllerCallback, InvalidTokenCallback
{
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.input_student_number)
    EditText studentNumberEditText;

    @BindView(R.id.input_button_meal_subscribe)
    Button mealSubscribeButton;

    @BindView(R.id.input_amount_guests)
    EditText amountOfGuestsEditText;

    private AppController appController;
    private Meal meal;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_subscribe);
        ButterKnife.bind(this);
        appController = new AppController(this, this);
        Log.i(TAG, appController.getStudent().toString());

        studentNumberEditText.setText(appController.getStudent().getStudentNumber());
        Intent intent = getIntent();
        meal = (Meal) intent.getSerializableExtra("Meal");

        mealSubscribeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                subscribeHandler();
            }
        });
    }

    /**
     * Represents the method that will handle a create fellow eater request.
     */
    private void subscribeHandler()
    {
        if (!validate())
        {
            return;
        }

        String amountOfGuests = amountOfGuestsEditText.getText().toString();
        FellowEater fellowEater = new FellowEater();
        fellowEater.setStudent(appController.getStudent());
        fellowEater.setAmountOfGuests(Integer.parseInt(amountOfGuests));
        fellowEater.setMeal(meal);
        appController.createFellowEater(fellowEater, this);
        mealSubscribeButton.setEnabled(false);
    }

    /**
     * Verifies the values of the controls.
     *
     * @return true if validation is successful; otherwise, false.
     */
    private boolean validate()
    {
        boolean valid = true;
        String amountOfGuests = amountOfGuestsEditText.getText().toString();
        int parsedAmountOfGuests = -1;

        try
        {
            parsedAmountOfGuests = Integer.parseInt(amountOfGuests);
        }
        catch (NumberFormatException e)
        {
            Log.i(TAG, amountOfGuestsEditText.getText().toString() + " is not a number");
            amountOfGuestsEditText.setError(getText(R.string.error_amount_of_guests));
            valid = false;
        }

        if (parsedAmountOfGuests == -1)
        {
            valid = false;
        }
        else
        {
            amountOfGuestsEditText.setError(null);

            if (!(meal.getAmountOfFellowEaters() + parsedAmountOfGuests + 1 <= meal.getMaxAmountOfFellowEaters()))
            {
                amountOfGuestsEditText.setError(getText(R.string.error_amount_of_guests_limit));
                valid = false;
            }
            else
            {
                amountOfGuestsEditText.setError(null);
            }
        }

        return valid;
    }

    /**
     * Occurs after a fellow eater has been created successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    @Override
    public void onSubscribeComplete(boolean result)
    {
        if (result)
        {
            Toast.makeText(this, R.string.message_meal_subscribed, Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("RELOAD", true);
            editor.apply();
            finish();
            mealSubscribeButton.setEnabled(true);
        }
        else
        {
            Toast.makeText(this, getText(R.string.error_connection), Toast.LENGTH_LONG).show();
            mealSubscribeButton.setEnabled(true);
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
