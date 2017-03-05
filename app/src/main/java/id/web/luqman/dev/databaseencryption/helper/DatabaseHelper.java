package id.web.luqman.dev.databaseencryption.helper;

import android.content.ContentValues;
import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import id.web.luqman.dev.databaseencryption.model.mProfile;

/**
 * Created by Seven7 on 3/5/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private final String PASSWORD_DB = "password_db";
    private static final String DATABASE_NAME = "database.db";
    public final static String DATABASE_PATH = "/data/data/id.web.luqman.dev.databaseencryption/databases/";
    public static final int DATABASE_VERSION = 1;
    //public static final int DATABASE_VERSION_old = 1;

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }


    //Create a empty database on the system
    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            this.getWritableDatabase(PASSWORD_DB);
            try {

                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException {
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    //delete database
    public void db_delete() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openOrCreateDatabase(myPath, PASSWORD_DB, null);
    }

    public synchronized void closeDataBase() throws SQLException {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public void onCreate(SQLiteDatabase db) {
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }


    public ArrayList<mProfile> getProfileList() {
        ArrayList<mProfile> result = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase(PASSWORD_DB);
        Cursor res;
        res = db.rawQuery("SELECT * FROM tbl_profile", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            result.add(new mProfile(
                    res.getString(res.getColumnIndex("id")),
                    res.getString(res.getColumnIndex("first_name")),
                    res.getString(res.getColumnIndex("last_name"))
            ));
            res.moveToNext();
        }
        db.close();
        return result;
    }


    public boolean addProfile(String firstName, String lastName) {
        SQLiteDatabase db = this.getWritableDatabase(PASSWORD_DB);
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        long rowInserted = db.insert("tbl_profile", null, values);
        db.close();
        return true;
    }

    public boolean deleteProfile(String ID) {
        SQLiteDatabase db = this.getWritableDatabase(PASSWORD_DB);
        long rowDelete = db.delete("tbl_profile", "id='" + ID + "'", null);
        if (rowDelete == 0) {
            return false;
        }
        db.close();
        return true;
    }
}
