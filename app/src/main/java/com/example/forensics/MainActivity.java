package com.example.forensics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    static File directory = null;
    static Uri uri;
    int rqCode = 1;
    static File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        Context context = getApplicationContext();
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == rqCode && resultCode == Activity.RESULT_OK) {
            uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                TextView jdir = findViewById(R.id.xdir);
                String[] split = uri.getPath().split(":");
                String path = split[1];
                Log.e("DIRE NUEVAAAA", split[1]);

                directory = new File(Environment.getExternalStorageDirectory(), path);
                files = directory.listFiles();
                jdir.setText(directory.getName());
            }
        }
    }
}