package com.iit.t1.u_board;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NEEL DESAI on 11/3/2015.
 */
public class SignUpValidation extends Activity {
    private EditText username;
    private EditText email;
    private EditText phone;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.editText);
        email = (EditText) findViewById(R.id.editText3);
        phone = (EditText) findViewById(R.id.editText4);
        password = (EditText) findViewById(R.id.editText2);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final String user = username.getText().toString();
                if (!isValidUser(user)) {
                    username.setError("Invalid Username");
                }
                final String emailid = email.getText().toString();
                if (!isValidEmail(emailid)) {
                    email.setError("Invalid Email");
                }

                final String phoneno = phone.getText().toString();
                if (!isValidPhone(phoneno)) {
                    phone.setError("Invalid Phone Number");
                }

                final String pass = password.getText().toString();
                if (!isValidPassword(pass)) {
                    password.setError("Invalid Password");
                }

            }
        });
    }

    //validating username
    private boolean isValidUser(String user) {

        String USER_PATTERN = "^[a-zA-Z0-9]*$";

        Pattern pattern = Pattern.compile(USER_PATTERN);
        Matcher matcher = pattern.matcher(user);
        return matcher.matches();
    }

    // validating email id
    private boolean isValidEmail(String emailid) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailid);
        return matcher.matches();
    }


    // validating phone number
    private boolean isValidPhone(String phoneno) {
        if(phoneno.length()==10) {
            String Number_PATTERN = "^[0-9]*$";

            Pattern pattern = Pattern.compile(Number_PATTERN);
            Matcher matcher = pattern.matcher(phoneno);
            return matcher.matches();
        }
        else{
            return false;
        }
    }


    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6 && pass.length()<128) {
            return true;
        }
        return false;
    }
}