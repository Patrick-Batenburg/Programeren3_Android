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
import com.patrick.maaltijdapp.controller.callbacks.RegisterControllerCallback;
import com.patrick.maaltijdapp.model.domain.AppController;
import com.patrick.maaltijdapp.model.domain.Student;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base class for activities that use the support library action bar features.
 */
public class RegisterActivity extends AppCompatActivity implements RegisterControllerCallback
{
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.input_student_number)
    EditText studentNumberEditText;

    @BindView(R.id.input_first_name)
    EditText firstNameEditText;

    @BindView(R.id.input_insertion)
    EditText insertionEditText;

    @BindView(R.id.input_last_name)
    EditText lastNameEditText;

    @BindView(R.id.input_email_address)
    EditText emailEditText;

    @BindView(R.id.input_phone_number)
    EditText phoneNumberEditText;

    @BindView(R.id.input_password)
    EditText passwordEditText;

    @BindView(R.id.input_re_enter_password)
    EditText reEnterPasswordEditText;

    @BindView(R.id.input_button_register)
    Button registerButton;

    @BindView(R.id.input_link_login)
    TextView loginLinkTextView;

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
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        appController = new AppController(this);

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerHandler();
            }
        });

        loginLinkTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginLinkHandler();
            }
        });
    }

    /**
     * Represents the method that will handle a login request.
     */
    private void loginLinkHandler()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Represents the method that will handle a register request.
     */
    private void registerHandler()
    {
        Log.d(TAG, "Register");
        registerButton.setEnabled(false);
        loginLinkTextView.setEnabled(false);

        if (!validate())
        {
            onRegisterFailed();
            return;
        }

        String studentNumber = studentNumberEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String insertion = insertionEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String emailAddress = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Student student = new Student();
        student.setFirstName(firstName);
        student.setInsertion(insertion);
        student.setLastName(lastName);
        student.setStudentNumber(studentNumber);
        student.setEmailAddress(emailAddress);
        student.setPhoneNumber(phoneNumber);
        student.setPassword(password);

        appController.register(student, this);
    }

    /**
     * Verifies the values of the controls.
     *
     * @return true if validation is successful; otherwise, false.
     */
    private boolean validate()
    {
        boolean valid = true;

        String studentNumber = studentNumberEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String emailAddress = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String reEnterPassword = reEnterPasswordEditText.getText().toString();

        try
        {
            int result = Integer.parseInt(studentNumber);
            studentNumberEditText.setError(null);
        }
        catch (NumberFormatException e)
        {
            Log.e(TAG, e.getMessage());
            studentNumberEditText.setError(getText(R.string.error_student_number));
            valid = false;
        }

        if (firstName.isEmpty())
        {
            firstNameEditText.setError(getText(R.string.error_first_name));
            valid = false;
        }
        else
        {
            firstNameEditText.setError(null);
        }

        if (lastName.isEmpty())
        {
            lastNameEditText.setError(getText(R.string.error_last_name));
            valid = false;
        }
        else
        {
            lastNameEditText.setError(null);
        }

        if (emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
        {
            emailEditText.setError(getText(R.string.error_email));
            valid = false;
        }
        else
        {
            emailEditText.setError(null);
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() != 10)
        {
            phoneNumberEditText.setError(getText(R.string.error_email));
            valid = false;
        }
        else
        {
            phoneNumberEditText.setError(null);
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

        if (!(reEnterPassword.equals(password)))
        {
            reEnterPasswordEditText.setError(getText(R.string.error_re_enter_password));
            valid = false;
        }
        else
        {
            reEnterPasswordEditText.setError(null);
        }

        return valid;
    }

    /**
     * Occurs after a student has been registered successfully or unsuccessfully.
     *
     * @param result The result value of this callback.
     */
    @Override
    public void onRegisterComplete(boolean result)
    {
        if (result)
        {
            Toast.makeText(getBaseContext(), getText(R.string.message_account_registered), Toast.LENGTH_LONG).show();
            onRegisterSuccess();
        }
        else
        {
            Toast.makeText(getBaseContext(), getText(R.string.error_register_account), Toast.LENGTH_LONG).show();
            onRegisterFailed();
        }
    }

    /**
     * Occurs after a student has been registered successfully.
     */
    @Override
    public void onRegisterSuccess()
    {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
        registerButton.setEnabled(true);
        loginLinkTextView.setEnabled(true);
    }

    /**
     * Occurs after a student has been registered unsuccessfully.
     */
    @Override
    public void onRegisterFailed()
    {
        registerButton.setEnabled(true);
        loginLinkTextView.setEnabled(true);
    }
}
