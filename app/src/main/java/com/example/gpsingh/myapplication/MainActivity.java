/**
 * Created by GPSINGH on 25-01-2018.
 */

package com.example.gpsingh.myapplication;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    //initializing the variables
    static ArrayList<String> notesArray = new ArrayList<String>();
    static ArrayList<String> displayArray = new ArrayList<String>();
    //static ArrayList<Integer> colorArray = new ArrayList<Integer>();
    static ArrayList<Integer> colorNotesArray = new ArrayList<Integer>();
    static HashMap<Integer, Uri> imageMap = new HashMap<Integer, Uri>();
    static ListAdapter theAdapter;
    static CustomListAdapter theCustomAdapter;
    static ListView theListView;
    static int itemNumber;
    public static int defaultColor = 0xFFFFFFFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            // for writing the text, image and color information to the file system
            // to persist the data
            FileInputStream fileInputStream = openFileInput("noteSaves");
            FileInputStream fileInputStreamColor = openFileInput("noteSavesColor");
            FileInputStream fileInputStreamImage = openFileInput("noteSavesImage");

            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ObjectInputStream objectInputStreamColor = new ObjectInputStream(fileInputStreamColor);
            ObjectInputStream objectInputStreamImage = new ObjectInputStream(fileInputStreamImage);


            notesArray = (ArrayList<String>) objectInputStream.readObject();
            colorNotesArray = (ArrayList<Integer>) objectInputStreamColor.readObject();
            imageMap = (HashMap<Integer, Uri>) objectInputStreamImage.readObject();


            objectInputStream.close();
            objectInputStreamColor.close();
            objectInputStreamImage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String x : notesArray) {
            String[] beforeNewline = x.split("\n", 20);

            displayArray.add(beforeNewline[0]);

        }

        // making use of list view and custom adapter
        theCustomAdapter = new CustomListAdapter(MainActivity.this, R.layout.list_green_text, displayArray);

        theListView = (ListView) findViewById(R.id.listView);
        //theListView.setAdapter(theAdapter);
        theListView.setAdapter(theCustomAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                itemNumber = position;
                Intent intent = new Intent(MainActivity.this, ENotee.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_new) {

            Intent intent = new Intent(this, NewNote.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
