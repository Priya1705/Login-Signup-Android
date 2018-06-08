package com.example.priya_000.loginsignup;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import static android.os.Build.ID;

public class LoginDataBaseAdapter {

    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;


    public static final int NAME_COLUMN = 1;


    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"LOGIN"+"" +
            "( " +"ID"+" integer primary key autoincrement,"+ "FNAME text, LNAME text, USERNAME  text," +
            "PASSWORD text, DOB number, EMAIL text, CONTACT number, CITY text ); ";


    // Variable to hold the database instance
    public SQLiteDatabase db;


    // Context of the application using the database.
    private final Context context;


    // Database open/upgrade helper
    private DataBaseHelper dbHelper;
    public  LoginDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  LoginDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String Fname, String Lname, String userName,String password, String Dob,
                            long Contact, String email, String City)
    {
        ContentValues newValues = new ContentValues();

        // Assign values for each row.
        newValues.put("FNAME",Fname);
        newValues.put("LNAME",Lname);
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD",password);
        newValues.put("DOB",Dob);
        newValues.put("CONTACT",Contact);
        newValues.put("EMAIL",email);
        newValues.put("CITY",City);

        // Insert the row into your table
        db.insert("LOGIN", null, newValues);



    }
    public int deleteEntry(String userName)
    {
        String id=String.valueOf(ID);
        String where="USERNAME=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{userName}) ;
        Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    public String getSinlgeEntry(String userName)
    {
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }
    public void  updateEntry(String userName,String password)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD",password);

        String where="USERNAME = ?";
        db.update("LOGIN",updatedValues, where, new String[]{userName});
    }
}

