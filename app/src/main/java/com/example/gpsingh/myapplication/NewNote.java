/**
 * Created by GPSINGH on 25-01-2018.
 */


package com.example.gpsingh.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.FileObserver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

//Activity class for for creating new note
public class NewNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // for menu bar to save, discard new note
    //no functionality has been added to the settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_discardNote) {

            finish();
        }

        if (id == R.id.action_saveNote) {

            EditText editText = (EditText) findViewById(R.id.text_editor_newNote);

            String theText = (String) editText.getText().toString();
            MainActivity.notesArray.add(theText);

            String[] beforeNewline = theText.split("\n", 20);
            MainActivity.displayArray.add(beforeNewline[0]);

            MainActivity.theListView.setAdapter(MainActivity.theCustomAdapter);

            try {
                FileOutputStream fileOutputStream = openFileOutput("noteSaves", Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(MainActivity.notesArray);
                objectOutputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
