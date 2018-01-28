package com.patrick.maaltijdapp.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.patrick.maaltijdapp.R;
import com.patrick.maaltijdapp.controller.callbacks.LoginControllerCallback;
import com.patrick.maaltijdapp.model.domain.AppController;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base class for activities that use the support library action bar features.
 */
public class LoginActivity extends AppCompatActivity implements LoginControllerCallback
{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_REGISTER = 1;

    @BindView(R.id.input_student_number)
    EditText studentNumberEditText;

    @BindView(R.id.input_password)
    EditText passwordEditText;

    @BindView(R.id.input_button_login)
    Button loginButton;

    @BindView(R.id.input_link_register)
    TextView registerLinkTextView;

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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        appController = new AppController(this);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginHandler();
            }
        });

        registerLinkTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerLinkHandler();
            }
        });
    }

    /**
     * Represents the method that will handle a register request.
     */
    public void registerLinkHandler()
    {
        // Start the Register activity
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent, REQUEST_REGISTER);
        finish();
    }

    /**
     * Represents the method that will handle a login request.
     */
    public void loginHandler()
    {
        loginButton.setEnabled(false);
        registerLinkTextView.setEnabled(false);

        if (!validate())
        {
            onLoginFailed();
            return;
        }

        String studentNumber = studentNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        appController.login(studentNumber, password, this);
    }

    /**
     * Verifies the values of the controls.
     *
     * @return true if validation is successful; otherwise, false.
     */
    public boolean validate()
    {
        boolean valid = true;

        String studentNumber = studentNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        try
        {
            int result = Integer.parseInt(studentNumber);
            studentNumberEditText.setError(null);
        }
        catch (NumberFormatException e)
        {
            Log.i("", studentNumberEditText.getText().toString() + " is not a number");
            studentNumberEditText.setError(getText(R.string.error_student_number));
            valid = false;
        }

        if (password.isEmpty())
        {
            passwordEditText.setError(getText(R.string.error_password));
            valid = false;
        }
        else
        {
            passwordEditText.setError(null);
        }

        return valid;
    }

    /**
     * Occurs after a student has logged in successfully.
     */
    @Override
    public void onLoginSuccess()
    {
        Intent intent = new Intent(this, MealsActivity.class);
        startActivity(intent);
        loginButton.setEnabled(true);
        registerLinkTextView.setEnabled(true);
        finish();
    }

    /**
     * Occurs after a student has logged in unsuccessfully.
     */
    @Override
    public void onLoginFailed()
    {
        loginButton.setEnabled(true);
        registerLinkTextView.setEnabled(true);
    }

    /**
     * Occurs after a student has logged in successfully or unsuccessfully.
     *
     * @param response The result value of this callback.
     */
    @Override
    public void onLoginComplete(String response)
    {
        if (response.equals("Error: invalid token"))
        {
            Toast.makeText(getBaseContext(), getText(R.string.error_login), Toast.LENGTH_LONG).show();
            onLoginFailed();
        }
        else if (response.equals("Error: JSONException"))
        {
            Toast.makeText(getBaseContext(), getText(R.string.error_json_object), Toast.LENGTH_LONG).show();
            onLoginFailed();
        }
        else if (response.equals("Error: connection error"))
        {
            Toast.makeText(getBaseContext(), getText(R.string.error_connection), Toast.LENGTH_LONG).show();
            onLoginFailed();
        }
        else if (response.equals("success"))
        {
            Toast.makeText(getBaseContext(), getText(R.string.message_login_success), Toast.LENGTH_LONG).show();
            onLoginSuccess();
        }
    }
}
