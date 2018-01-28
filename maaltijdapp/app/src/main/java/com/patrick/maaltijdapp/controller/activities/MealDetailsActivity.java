package com.patrick.maaltijdapp.controller.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.patrick.maaltijdapp.R;
import com.patrick.maaltijdapp.controller.callbacks.DeleteMealControllerCallback;
import com.patrick.maaltijdapp.controller.callbacks.InvalidTokenCallback;
import com.patrick.maaltijdapp.controller.callbacks.UnsubscribeControllerCallback;
import com.patrick.maaltijdapp.model.domain.AppController;
import com.patrick.maaltijdapp.model.domain.FellowEater;
import com.patrick.maaltijdapp.model.domain.Meal;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base class for activities that use the support library action bar features.
 */
public class MealDetailsActivity extends AppCompatActivity implements UnsubscribeControllerCallback, DeleteMealControllerCallback, InvalidTokenCallback
{
    private static final String TAG = MealDetailsActivity.class.getSimpleName();
    private static final int REQUEST_UPDATE_MEAL = 1;

    @BindView(R.id.meal_dish_name)
    TextView mealDishNameTextView;

    @BindView(R.id.meal_description)
    TextView mealDescriptionTextView;

    @BindView(R.id.meal_chef_name)
    TextView mealChefNameTextView;

    @BindView(R.id.meal_chef_eats)
    TextView mealChefEatsTextView;

    @BindView(R.id.meal_price)
    TextView mealPriceTextView;

    @BindView(R.id.meal_date_time)
    TextView mealDateTimeTextView;

    @BindView(R.id.meal_amount_of_fellow_eaters)
    TextView mealAmountOfFellowEatersTextView;

    @BindView(R.id.meal_image)
    ImageView mealImageView;

    @BindView(R.id.input_button_meal_subscribe)
    Button mealSubscribeButton;

    @BindView(R.id.input_button_meal_unsubscribe)
    Button mealUnsubscribeButton;

    @BindView(R.id.input_button_meal_full)
    Button mealFullButton;

    @BindView(R.id.input_button_meal_delete)
    Button mealDeleteButton;

    @BindView(R.id.input_button_meal_update)
    Button mealUpdateButton;

    private Meal meal;
    private AppController appController;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        ButterKnife.bind(this, this);
        appController = new AppController(this, this);
        Intent intent = getIntent();
        meal = new Meal ();
        meal = (Meal) intent.getSerializableExtra("Meal");

        if (meal != null)
        {
            mealDishNameTextView.setText(meal.getDish());
            mealDescriptionTextView.setText(meal.getDescription());
            mealChefNameTextView.setText(String.format("%s %s", getText(R.string.meal_chef_name), meal.getChef().getFullName()));
            mealChefEatsTextView.setText(String.format("%s %s", getText(R.string.meal_chef_eats), (meal.isCookEating() ? "Ja" : "Nee")));
            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            mealPriceTextView.setText(String.format("%s%s", getText(R.string.meal_price), decimalFormat.format(meal.getPrice())));
            mealAmountOfFellowEatersTextView.setText(String.format("%s %s/%s", getText(R.string.meal_amount_of_fellow_eaters), meal.getAmountOfFellowEaters(), meal.getMaxAmountOfFellowEaters()));
            mealDateTimeTextView.setText(String.format("%s %s", getText(R.string.meal_date_time), meal.getDateTimeToString()));
            //mealImageView.setImageBitmap(ImageUtility.getImage(meal.getImage()));
            File file = new File(this.getFilesDir(), "mealImages_" + meal.getID());
            Picasso.with(MealDetailsActivity.this).load(file).placeholder(R.drawable.no_image).error(R.drawable.no_image).fit().into(mealImageView);

            if (meal.getChef().equals(appController.getStudent()))
            {
                mealUpdateButton.setVisibility(View.VISIBLE);
                mealDeleteButton.setVisibility(View.VISIBLE);

                mealUpdateButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        updateMealHandler();
                    }
                });

                mealDeleteButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        launchDeleteMealDialog();
                    }
                });
            }
            else if (meal.getStudents().contains(appController.getStudent()))
            {
                mealUnsubscribeButton.setVisibility(View.VISIBLE);

                mealUnsubscribeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        launchUnsubscribeDialog();
                    }
                });
            }
            else if (meal.getAmountOfFellowEaters() < meal.getMaxAmountOfFellowEaters())
            {
                mealSubscribeButton.setVisibility(View.VISIBLE);

                mealSubscribeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        subscribeToMealHandler();
                    }
                });
            }
            else
            {
                mealFullButton.setVisibility(View.VISIBLE);
                mealFullButton.setEnabled(false);
            }
        }
    }

    /**
     * Represents the method that will handle a create fellow eater request.
     */
    private void subscribeToMealHandler()
    {
        Intent intent = new Intent(this, MealSubscribeActivity.class);
        intent.putExtra("Meal", meal);
        startActivity(intent);
    }

    /**
     * Launches the Unsubscribe dialog.
     */
    private void launchUnsubscribeDialog()
    {
        new AlertDialog.Builder(this).setTitle(getText(R.string.message_title_unsubscribe)).setMessage(getText(R.string.message_meal_unsubscribe)).setIcon(R.drawable.alert).setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
            for (FellowEater fellowEater : meal.getFellowEaters())
            {
                if (fellowEater.getStudent().equals(appController.getStudent()))
                {
                    mealUnsubscribeButton.setEnabled(false);
                    appController.deleteFellowEater(fellowEater, MealDetailsActivity.this);
                }
            }
            }
        }).setNegativeButton(getText(R.string.no), null).show();
    }

    /**
     * Launches the Delete Meal dialog.
     */
    private void launchDeleteMealDialog()
    {
        new AlertDialog.Builder(this).setTitle(getText(R.string.message_title_delete_meal)).setMessage(getText(R.string.message_delete_meal)).setIcon(R.drawable.alert).setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                mealDeleteButton.setEnabled(false);
                appController.deleteMeal(meal, MealDetailsActivity.this);
            }
        }).setNegativeButton(getText(R.string.no), null).show();
    }

    /**
     * Represents the method that will handle a update meal request.
     */
    private void updateMealHandler()
    {
        Intent intent = new Intent(this, MealCreateActivity.class);
        intent.putExtra("REQUEST_UPDATE_MEAL", REQUEST_UPDATE_MEAL);
        intent.putExtra("Meal", meal);
        startActivity(intent);
    }

    /**
     * Occurs after a fellow eater has been deleted successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    @Override
    public void onUnsubscribeComplete(boolean result)
    {
        if (result)
        {
            Toast.makeText(this, getText(R.string.message_meal_unsubscribed), Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("RELOAD", true);
            editor.apply();
            finish();
            mealUnsubscribeButton.setEnabled(true);
        }
        else
        {
            Toast.makeText(this, getText(R.string.error_connection), Toast.LENGTH_LONG).show();
            mealUnsubscribeButton.setEnabled(true);
        }
    }

    /**
     * Occurs after a meal has been deleted successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    @Override
    public void onDeleteMealComplete(boolean result)
    {
        if (result)
        {
            Toast.makeText(this, getText(R.string.message_meal_deleted), Toast.LENGTH_LONG).show();
            SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("RELOAD", true);
            editor.apply();
            finish();
            mealDeleteButton.setEnabled(true);
        }
        else
        {
            Toast.makeText(this, getText(R.string.error_connection), Toast.LENGTH_LONG).show();
            mealDeleteButton.setEnabled(true);
        }
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
            finish();
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
