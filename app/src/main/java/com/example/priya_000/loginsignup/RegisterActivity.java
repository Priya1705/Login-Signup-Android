package com.example.priya_000.loginsignup;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    LoginDataBaseAdapter loginDataBaseAdapter;

    ImageView img;
    Button btn;
    private static final int REQUEST_CAMERA=1;
    private static final int RESULT_LOAD_IMAGE=0;

  /*  public static final Pattern emailPattern = Pattern.compile(

            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );         */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        img=(ImageView)findViewById(R.id.img);
        btn=(Button)findViewById(R.id.btn2);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getImageFromAlbum();
                    }
                }
        );

//        getActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText fname = (EditText) findViewById(R.id.etfname);
        final EditText lname = (EditText) findViewById(R.id.etlname);
        final EditText Username = (EditText) findViewById(R.id.etUsername);
        final EditText dob = (EditText) findViewById(R.id.etdob);
        final EditText Email = (EditText) findViewById(R.id.etEmail);
        final EditText pass = (EditText) findViewById(R.id.etpass);
        final EditText contact = (EditText) findViewById(R.id.etcontact);
        final EditText city = (EditText) findViewById(R.id.etCity);
        final Button bdob = (Button) findViewById(R.id.bdob);
        final Button register = (Button) findViewById(R.id.bRegister);

        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        bdob.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Calendar c = Calendar.getInstance();
                        int cyear = c.get(Calendar.YEAR);
                        int cmonth = c.get(Calendar.MONTH);
                        int cday = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                String date_selected = String.valueOf(dayOfMonth)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(year);
                                dob.setText(date_selected);
                                //                              CampusNewsISActivity.showLog("dateFieldTextBox selected");
                            }
                        },cyear,cmonth,cday);
                        //          dialog.setCancelable(false);
                        dialog.show();
                    }
                });



        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        validate();
                    }

                    private void validate() {
                        if(fname.getText().toString().length()<=0){

                            Toast.makeText(getApplicationContext(), "Please fill First name",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(lname.getText().toString().length()<=0){

                            Toast.makeText(getApplicationContext(), "Please fill Last name",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(Username.getText().toString().length()<=0){

                            Toast.makeText(getApplicationContext(), "Please fill Username",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(Email.getText().toString().length()<=0){

                            Toast.makeText(getApplicationContext(), "Please provide an Email address",
                                    Toast.LENGTH_SHORT).show();
                        }
                      /* else if(!(Email.equals(emailPattern))){
                            invalid = true;
                            Toast.makeText(getApplicationContext(),
                                    "Please fill a valid email address",Toast.LENGTH_SHORT).show();
                        }
                                 */
                        else if (pass.length() < 8) {

                            Toast.makeText(getApplicationContext(),
                                    "Please enter atleast 8 digits password", Toast.LENGTH_SHORT).show();
                        }
                        else if (contact.length() < 10 || contact.length()>10) {

                            Toast.makeText(getApplicationContext(),
                                    "Please enter a valid contact number", Toast.LENGTH_SHORT).show();
                        }

                        else if (city.getText().toString().length() <= 0){

                            Toast.makeText(getApplicationContext(), "Please fill City name",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String Fname=fname.getText().toString();
                            String Lname=lname.getText().toString();
                            String userName=Username.getText().toString();
                            String Dob = dob.getText().toString();
                            String email=Email.getText().toString();
                            String password=pass.getText().toString();
                            long Contact = Long.parseLong( contact.getText().toString() );
                            String City=city.getText().toString();

                            // Save the Data in Database
                            loginDataBaseAdapter.insertEntry(Fname, Lname,  userName, password,  Dob, Contact,email, City);
                            Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    }

                }
        );
    }


    private void getImageFromAlbum(){
        final CharSequence [] items={"CAMERA", "GALLERY", "CANCEL"};

        AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(items[i].equals("CAMERA")){
                    showAlert();
                }

                else if(items[i].equals("GALLERY")){
                    try{
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        intent.setType("image/*");

                        startActivityForResult(intent.createChooser(intent, "Select File"),RESULT_LOAD_IMAGE );

                    }catch(Exception exp){
                        Log.i("Error",exp.toString());
                    }
                }

                else if(items[i].equals("CANCEL")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(RegisterActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA);

                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMAGE ) {
            Uri selectedImageUri=data.getData();
            img.setImageURI(selectedImageUri);
        }

        if (resultCode == Activity.RESULT_OK && requestCode ==REQUEST_CAMERA ) {
            Bundle bundle=data.getExtras();
            final Bitmap bmp=(Bitmap)bundle.get("data");
            img.setImageBitmap(bmp);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginDataBaseAdapter.close();
    }
}

