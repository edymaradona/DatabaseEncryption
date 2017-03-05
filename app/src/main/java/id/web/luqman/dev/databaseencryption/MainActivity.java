package id.web.luqman.dev.databaseencryption;

import android.content.Context;
import android.database.SQLException;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.IOException;
import java.util.List;

import id.web.luqman.dev.databaseencryption.helper.DatabaseHelper;
import id.web.luqman.dev.databaseencryption.model.mProfile;

public class MainActivity extends AppCompatActivity {
    TextView txtFirstName, txtLastName;
    Button btnSubmit;
    DatabaseHelper DBHelper;
    ArrayAdapter<mProfile> dataAdapter = null;
    List<mProfile> dataProfile;
    ListView lvDisplay;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase.loadLibs(this);

        connectDatabase();

        txtFirstName = (TextView) findViewById(R.id.txtFirstName);
        txtLastName = (TextView) findViewById(R.id.txtLastName);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        lvDisplay = (ListView) findViewById(R.id.lvDisplay);
        layout = (LinearLayout) findViewById(R.id.layout);

        displayData();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(getApplicationContext(), getCurrentFocus().getWindowToken());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(getApplicationContext(), getCurrentFocus().getWindowToken());
                DBHelper.addProfile(txtFirstName.getText().toString(),
                        txtLastName.getText().toString());
                displayData();
            }
        });
    }

    public void connectDatabase() {
        DBHelper = new DatabaseHelper(this);
        try {
            DBHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DBHelper.closeDataBase();
        try {
            DBHelper.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }


    public void displayData() {
        dataProfile = DBHelper.getProfileList();

        if (dataProfile.size() > 0) {
            dataAdapter = new ArrayAdapter<mProfile>(MainActivity.this, R.layout.list_item_layout, dataProfile);
            lvDisplay.setAdapter(dataAdapter);
        }
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }
}
