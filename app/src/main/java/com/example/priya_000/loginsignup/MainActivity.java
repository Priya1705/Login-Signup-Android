package com.example.priya_000.loginsignup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    final Context context = this;

    LoginDataBaseAdapter loginDataBaseAdapter;

    LoginButton loginButton;
    CallbackManager callbackManager;   //callbackManager manages the callbacks into the facebookSdk from the onActivityResult();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton=(LoginButton) findViewById(R.id.fb_login_bn);
        callbackManager=CallbackManager.Factory.create();    //instance of callbackManager

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getApplicationContext(), "Login Successful",
                                Toast.LENGTH_SHORT).show();

                        change();
                    }

            private void change() {

                Intent i=new Intent(MainActivity.this, UserAreaActivity.class);
                startActivity(i);
            }


            @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(),"Login Cancle",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(),"Error in Login in",Toast.LENGTH_SHORT).show();
                    }
                });


                // create a instance of SQLite Database
                loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();


        final EditText Username=(EditText)findViewById(R.id.etUsername);
        final EditText Password=(EditText)findViewById(R.id.etPassword);

        final Button Login=(Button)findViewById(R.id.bLogin);

        final TextView register=(TextView)findViewById(R.id.tvRegisterHere);
        final TextView forgotPassword=(TextView)findViewById(R.id.fg);

        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(i);
                    }
                }
        );

        forgotPassword.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.activity_change_password, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        alertDialogBuilder.setView(promptsView);

                        final EditText userInput = (EditText) promptsView.findViewById(R.id.input);






                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                if(userInput.getText().toString().length()<=0){

                                                    Toast.makeText(getApplicationContext(), "Please provide your UserName",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(MainActivity.this, "New Password is sent to your email", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });


                        AlertDialog alertDialog = alertDialogBuilder.create();

                        alertDialog.show();
                    }
                }
        );

        Login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validate();

                    }

                    private void validate() {
                        boolean invalid = false;

                        if(Username.getText().toString().length()<=0){
                            invalid=true;
                            Toast.makeText(getApplicationContext(), "Please fill Username",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(Password.getText().toString().length()<=0) {
                            invalid = true;
                            Toast.makeText(getApplicationContext(), "Please fill Password",
                                    Toast.LENGTH_SHORT).show();
                        }

                        else{

                            // get The User name and Password
                            String userName=Username.getText().toString();
                            String password=Password.getText().toString();

                            // fetch the Password form database for respective user name
                            String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);

                            // check if the Stored password matches with  Password entered by user
                            if(password.equals(storedPassword))
                            {
                                Toast.makeText(MainActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(MainActivity.this, UserAreaActivity.class);

                                String user=Username.getText().toString();

                                i.putExtra("Umessage", user);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "UserName or Password does not match", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}





