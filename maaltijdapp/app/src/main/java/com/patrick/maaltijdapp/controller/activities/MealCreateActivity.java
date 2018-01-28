package com.patrick.maaltijdapp.controller.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.patrick.maaltijdapp.R;
import com.patrick.maaltijdapp.controller.callbacks.CreateMealControllerCallback;
import com.patrick.maaltijdapp.controller.callbacks.InvalidTokenCallback;
import com.patrick.maaltijdapp.controller.callbacks.UpdateMealControllerCallback;
import com.patrick.maaltijdapp.model.domain.AppController;
import com.patrick.maaltijdapp.model.domain.Meal;
import com.patrick.maaltijdapp.model.utils.DateTimeUtility;
import com.patrick.maaltijdapp.model.utils.ImageUtility;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base class for activities that use the support library action bar features.
 */
public class MealCreateActivity extends AppCompatActivity implements CreateMealControllerCallback, UpdateMealControllerCallback, InvalidTokenCallback
{
    private static final String TAG = MealCreateActivity.class.getSimpleName();
    private static final int REQUEST_SELECT_IMAGE = 1;

    @BindView(R.id.meal_image)
    ImageView mealImageView;

    @BindView(R.id.meal_image_name)
    TextView mealImageNameTextView;

    @BindView(R.id.meal_create_or_update_title)
    TextView mealCreateOrUpdateTextView;

    @BindView(R.id.input_meal_name)
    EditText mealNameEditText;

    @BindView(R.id.input_meal_max_amount_of_fellow_eaters)
    EditText mealMaxFellowEatersEditText;

    @BindView(R.id.input_meal_price)
    EditText mealPriceEditText;

    @BindView(R.id.input_button_meal_delete_image)
    ImageButton mealDeleteImageButton;

    @BindView(R.id.input_button_meal_date)
    Button datePickerButton;

    @BindView(R.id.btn_meal_time)
    Button timePickerButton;

    @BindView(R.id.input_meal_date)
    EditText mealDateEditText;

    @BindView(R.id.input_button_save_meal)
    Button saveMealButton;

    @BindView(R.id.input_button_meal_upload_image)
    Button mealUploadImageButton;

    @BindView(R.id.input_meal_time)
    EditText mealTimeEditText;

    @BindView(R.id.input_meal_description)
    EditText mealDescriptionEditText;

    @BindView(R.id.input_switch_meal_is_cook_eating)
    Switch mealIsCookEatingSwitch;

    private AppController appController;
    private Bitmap image;
    private Meal meal;
    int createRequest = 0;
    int updateRequest = 0;
    DecimalFormat decimalFormat;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_create);
        ButterKnife.bind(this);
        appController = new AppController(this, this);
        image = null;
        meal = null;
        decimalFormat = new DecimalFormat("#0.00");
        Intent intent = getIntent();
        createRequest = intent.getIntExtra("REQUEST_CREATE_MEAL", 0);
        updateRequest = intent.getIntExtra("REQUEST_UPDATE_MEAL", 0);

        if (createRequest == 1)
        {
            mealCreateOrUpdateTextView.setText(getString(R.string.meal_create_title));
        }
        else if (updateRequest == 1)
        {
            mealCreateOrUpdateTextView.setText(getString(R.string.meal_update_title));
            mealImageNameTextView.setText(getText(R.string.meal_choose_new_image));
            meal = (Meal) intent.getSerializableExtra("Meal");
            mealNameEditText.setText(meal.getDish());
            mealDescriptionEditText.setText(meal.getDescription());
            mealDateEditText.setText(meal.getDateTime().toString("dd-MM-yyyy"));
            mealTimeEditText.setText(meal.getDateTime().toString("HH:mm"));
            mealMaxFellowEatersEditText.setText(String.valueOf(meal.getMaxAmountOfFellowEaters()));
            mealPriceEditText.setText(String.valueOf(decimalFormat.format(meal.getPrice())));
            mealIsCookEatingSwitch.setChecked(meal.isCookEating());

            File file = new File(this.getFilesDir(), "mealImages_" + meal.getID());
            Picasso.with(this).load(file).placeholder(R.drawable.no_image).error(R.drawable.no_image).centerCrop().fit().into(mealImageView);

            if (file.exists())
            {
                image = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        }

        datePickerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchDatePickerDialog();
            }
        });

        timePickerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchTimePickerDialog();
            }
        });

        saveMealButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveMealHandler();
            }
        });

        mealUploadImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                launchImageGallery();
            }
        });

        mealDeleteImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deleteMealImageHandler();
            }
        });
    }

    /**
     * Represents the method that will handle a delete meal image request.
     */
    private void deleteMealImageHandler()
    {
        Picasso.with(MealCreateActivity.this).load(R.drawable.no_image).centerCrop().fit().into(mealImageView);
        mealImageNameTextView.setText(getText(R.string.meal_image_name));
        mealDeleteImageButton.setVisibility(View.INVISIBLE);
        image = null;
    }

    /**
     * Represents the method that will handle a create meal request.
     */
    private void saveMealHandler()
    {
        if (!validate())
        {
            return;
        }

        String mealName = mealNameEditText.getText().toString();
        String mealDescription = mealDescriptionEditText.getText().toString();
        String mealDate = mealDateEditText.getText().toString();
        String mealTime = mealTimeEditText.getText().toString();
        int mealMaxFellowEaters = Integer.parseInt(mealMaxFellowEatersEditText.getText().toString());
        Double mealPrice = Double.parseDouble(mealPriceEditText.getText().toString());
        boolean isCookEating = mealIsCookEatingSwitch.isChecked();
        DateTime mealDateTime = DateTimeUtility.nenISO8601ToDateTime(String.format("%s %s", mealDate, mealTime));

        if (createRequest == 1)
        {
            meal = new Meal();
            meal.setMaxAmountOfFellowEaters(mealMaxFellowEaters);
            meal.setPrice(mealPrice);
            meal.setDish(mealName);
            meal.setDescription(mealDescription);
            meal.setIsCookEating(isCookEating);
            meal.setDateTime(mealDateTime);
            meal.setChef(appController.getStudent());

            if (image != null)
            {
                meal.setImage(image);
            }

            appController.createMeal(meal, this);
        }
        else if (updateRequest == 1)
        {
            meal.setMaxAmountOfFellowEaters(mealMaxFellowEaters);
            meal.setPrice(mealPrice);
            meal.setDish(mealName);
            meal.setDescription(mealDescription);
            meal.setIsCookEating(isCookEating);
            meal.setDateTime(mealDateTime);
            meal.setChef(appController.getStudent());

            if (image != null)
            {
                meal.setImage(image);
            }

            appController.updatedMeal(meal, this);
            saveMealButton.setEnabled(false);
        }
    }

    /**
     * Launches the Image Gallery.
     */
    private void launchImageGallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getText(R.string.meal_choose_image)), REQUEST_SELECT_IMAGE);
    }

    /**
     * Launches the Date Picker Dialog.
     */
    private void launchDatePickerDialog()
    {
        // Get Current Date
        final Calendar calendar = Calendar.getInstance();
        int calendarYear = calendar.get(Calendar.YEAR);
        int calendarMonth = calendar.get(Calendar.MONTH);
        int calendarDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                StringBuilder stringBuilder = new StringBuilder();

                if (day > 9)
                {
                    stringBuilder.append(day).append("-");
                }
                else
                {
                    stringBuilder.append("0").append(day).append("-");
                }

                if (month + 1 > 9)
                {
                    stringBuilder.append(month + 1).append("-");
                }
                else
                {
                    stringBuilder.append("0").append(month + 1).append("-");
                }

                stringBuilder.append(year);
                mealDateEditText.setText(stringBuilder.toString());
            }
        }, calendarYear, calendarMonth, calendarDay);

        datePickerDialog.show();
    }

    /**
     * Launches the Time Picker Dialog.
     */
    private void launchTimePickerDialog()
    {
        // Get Current Time
        final Calendar calendar = Calendar.getInstance();
        int calendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        int calendarMinute = calendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
        {
            public void onTimeSet(TimePicker view, int hour, int minute)
            {
                StringBuilder stringBuilder = new StringBuilder();

                if (hour > 9)
                {
                    stringBuilder.append(hour).append(":");
                }
                else
                {
                    stringBuilder.append("0").append(hour).append(":");
                }

                if (minute > 9)
                {
                    stringBuilder.append(minute);
                }
                else
                {
                    stringBuilder.append("0").append(minute);
                }

                mealTimeEditText.setText(stringBuilder.toString());
            }
        }, calendarHour, calendarMinute, false);

        timePickerDialog.show();
    }

    /**
     * Verifies the values of the controls.
     *
     * @return true if validation is successful; otherwise, false.
     */
    private boolean validate()
    {
        boolean valid = true;

        String mealName = mealNameEditText.getText().toString();
        String mealDescription = mealDescriptionEditText.getText().toString();
        String mealDate = mealDateEditText.getText().toString();
        String mealTime = mealTimeEditText.getText().toString();
        String mealMaxFellowEaters = mealMaxFellowEatersEditText.getText().toString();
        String mealPrice = mealPriceEditText.getText().toString();
        DateTime mealDateTime;

        try
        {
            DateTime now = new DateTime();
            mealDateTime = DateTimeUtility.nenISO8601ToDateTime(String.format("%s %s", mealDate, mealTime));

            if (mealDateTime == null)
            {
                mealDateEditText.setError(getText(R.string.error_meal_date));
                mealTimeEditText.setError(getText(R.string.error_meal_time));
                valid = false;
            }
            else
            {
                if (!mealDateTime.isAfter(now))
                {
                    mealDateEditText.setError(getText(R.string.error_meal_date));
                    mealTimeEditText.setError(getText(R.string.error_meal_time));
                    valid = false;
                }
                else
                {
                    mealDateEditText.setError(null);
                    mealTimeEditText.setError(null);
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            Log.e(TAG, e.getMessage());
            mealDateEditText.setError(getText(R.string.error_meal_date));
            mealTimeEditText.setError(getText(R.string.error_meal_time));
            valid = false;
        }

        if (mealName.isEmpty())
        {
            mealNameEditText.setError(getText(R.string.error_meal_dish));
            valid = false;
        }
        else
        {
            mealNameEditText.setError(null);
        }

        if (mealDescription.isEmpty())
        {
            mealDescriptionEditText.setError(getText(R.string.error_meal_description));
            valid = false;
        }
        else
        {
            mealNameEditText.setError(null);
        }

        if (mealMaxFellowEaters.isEmpty())
        {
            mealMaxFellowEatersEditText.setError(getText(R.string.error_meal_max_fellow_eaters));
            valid = false;
        }
        else
        {
            mealMaxFellowEatersEditText.setError(null);
        }

        try
        {
            int result = Integer.parseInt(mealMaxFellowEaters);
        }
        catch (NumberFormatException e)
        {
            Log.e(TAG, e.getMessage());
            mealMaxFellowEatersEditText.setError(getText(R.string.error_meal_max_fellow_eaters));
            valid = false;
        }

        if (mealPrice.isEmpty())
        {
            mealPriceEditText.setError(getText(R.string.error_meal_price));
            valid = false;
        }
        else
        {
            mealPriceEditText.setError(null);
        }

        try
        {
            Double result = Double.parseDouble(mealPrice);
            mealPriceEditText.setText(decimalFormat.format(result));
        }
        catch (NumberFormatException e)
        {
            Log.e(TAG, e.getMessage());
            mealPriceEditText.setError(getText(R.string.error_meal_price));
            valid = false;
        }

        return valid;
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            image = ImageUtility.getBitmapFromURI(this, uri);
            Picasso.with(MealCreateActivity.this).load(uri).placeholder(R.drawable.no_image).error(R.drawable.no_image).centerCrop().fit().into(mealImageView);
            mealImageNameTextView.setText(ImageUtility.getFileName(this, uri));
            mealDeleteImageButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Occurs after a meal has been created successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    @Override
    public void onCreateMealComplete(boolean result)
    {
        if (result)
        {
            Toast.makeText(this, getText(R.string.message_meal_created), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("RELOAD", true);
            editor.apply();
            saveMealButton.setEnabled(true);
            finish();
        }
        else
        {
            Toast.makeText(this, getText(R.string.error_connection), Toast.LENGTH_LONG).show();
            saveMealButton.setEnabled(true);
        }
    }

    /**
     * Occurs after a meal has been updated successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    @Override
    public void onUpdateMealComplete(boolean result)
    {
        if (result)
        {
            Toast.makeText(this, getText(R.string.message_meal_updated), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("RELOAD", true);
            editor.apply();
            finish();
            saveMealButton.setEnabled(true);
        }
        else
        {
            Toast.makeText(this, getText(R.string.error_connection), Toast.LENGTH_LONG).show();
            saveMealButton.setEnabled(true);
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
