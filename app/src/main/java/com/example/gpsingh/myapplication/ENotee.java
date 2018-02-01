/**
 * Created by GPSINGH on 25-01-2018.
 */

package com.example.gpsingh.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import static java.security.AccessController.getContext;

//Activity for individual note when created
public class ENotee extends AppCompatActivity {

    final Context context = this;
    EditText editText;
    ImageView imageView;
    Uri orgUri, uriFromPath;
    String convertedPath;
    private int colorAssigned;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    //initializing the data in the onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ee_note);

        if (MainActivity.itemNumber >= MainActivity.colorNotesArray.size())
            colorAssigned = MainActivity.defaultColor;
        else
            colorAssigned = MainActivity.colorNotesArray.get(MainActivity.itemNumber);

        editText = (EditText) findViewById(R.id.text_editor_1);
        imageView = (ImageView) findViewById(R.id.imageView1);

        editText.setText(MainActivity.notesArray.get(MainActivity.itemNumber));

        //setting background color
        if (MainActivity.colorNotesArray != null && MainActivity.colorNotesArray.size() > MainActivity.itemNumber)
            editText.setBackgroundColor(MainActivity.colorNotesArray.get(MainActivity.itemNumber));
        else
            editText.setBackgroundColor(MainActivity.defaultColor);


        //setting the image if there is any set before
        //checking for access permission
        //if not having then request for the same
        if (MainActivity.imageMap.containsKey(MainActivity.itemNumber)) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                loadImage();
            }

        }

        //Toolbar code
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
    public boolean onCreateOptionsMenu(Menu menu2) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_e_note, menu2);
        return true;
    }

   //Menu bar for save, discard, share , add image, take picture etc
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings2) {
            return true;
        }

        if (id == R.id.action_discardNote2) {
            MainActivity.notesArray.remove(MainActivity.itemNumber);
            MainActivity.displayArray.remove(MainActivity.itemNumber);
            MainActivity.colorNotesArray.remove(MainActivity.itemNumber);
            MainActivity.imageMap.remove(MainActivity.itemNumber);

            MainActivity.theListView.setAdapter(MainActivity.theCustomAdapter);

            try {
                FileOutputStream fileOutputStream = openFileOutput("noteSaves", Context.MODE_PRIVATE);
                FileOutputStream fileOutputStreamColor = openFileOutput("noteSavesColor", Context.MODE_PRIVATE);
                FileOutputStream fileOutputStreamImage = openFileOutput("noteSavesImage", Context.MODE_PRIVATE);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                ObjectOutputStream objectOutputStreamColor = new ObjectOutputStream(fileOutputStreamColor);
                ObjectOutputStream objectOutputStreamImage = new ObjectOutputStream(fileOutputStreamImage);

                objectOutputStream.writeObject(MainActivity.notesArray);
                objectOutputStreamColor.writeObject(MainActivity.colorNotesArray);
                objectOutputStreamImage.writeObject(MainActivity.imageMap);

                objectOutputStream.close();
                objectOutputStreamColor.close();
                objectOutputStreamImage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }

        if (id == R.id.action_saveNote2) {

            EditText editText = (EditText) findViewById(R.id.text_editor_1);
            String theText = (String) editText.getText().toString();

            MainActivity.notesArray.set(MainActivity.itemNumber, theText);

            String[] beforeNewline = theText.split("\n", 20);

            MainActivity.displayArray.set(MainActivity.itemNumber, beforeNewline[0]);

            if (MainActivity.itemNumber + 1 > MainActivity.colorNotesArray.size())
                MainActivity.colorNotesArray.add(colorAssigned);
            else
                MainActivity.colorNotesArray.set(MainActivity.itemNumber, colorAssigned);


            MainActivity.theListView.setAdapter(MainActivity.theCustomAdapter);

            try {
                FileOutputStream fileOutputStream = openFileOutput("noteSaves", Context.MODE_PRIVATE);
                FileOutputStream fileOutputStreamColor = openFileOutput("noteSavesColor", Context.MODE_PRIVATE);
                FileOutputStream fileOutputStreamImage = openFileOutput("noteSavesImage", Context.MODE_PRIVATE);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                ObjectOutputStream objectOutputStreamColor = new ObjectOutputStream(fileOutputStreamColor);
                ObjectOutputStream objectOutputStreamImage = new ObjectOutputStream(fileOutputStreamImage);

                objectOutputStream.writeObject(MainActivity.notesArray);
                objectOutputStreamColor.writeObject(MainActivity.colorNotesArray);
                objectOutputStreamImage.writeObject(MainActivity.imageMap);


                objectOutputStream.close();
                objectOutputStreamColor.close();
                objectOutputStreamImage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();

        }

        if (id == R.id.action_shareNote2) {
            String message = MainActivity.notesArray.get(MainActivity.itemNumber);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
        }

        //action_addImage
        if (id == R.id.action_addImage2) {

            EditText text = (EditText) findViewById(R.id.text_editor_1);
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        }

        //action select color
        if (id == R.id.action_colorPicker) {

            // custom dialogue
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle("Select Color");

            Button red = (Button) dialog.findViewById(R.id.red);
            Button blue = (Button) dialog.findViewById(R.id.blue);
            Button yellow = (Button) dialog.findViewById(R.id.yellow);
            Button orange = (Button) dialog.findViewById(R.id.orange);

            // if button is clicked, close the custom dialog
            red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int red = 0xFFF75858;
                    colorAssigned = red;
                    editText.getBackground().setColorFilter(red, PorterDuff.Mode.SRC_ATOP);
                    dialog.dismiss();
                }
            });

            blue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int blue = 0xFF8F6BF2;
                    colorAssigned = blue;
                    editText.getBackground().setColorFilter(blue, PorterDuff.Mode.SRC_ATOP);
                    dialog.dismiss();
                }
            });

            yellow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int yellow = 0xFFEEF76F;
                    colorAssigned = yellow;
                    editText.getBackground().setColorFilter(yellow, PorterDuff.Mode.SRC_ATOP);
                    dialog.dismiss();
                }
            });

            orange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int orange = 0xFFFFB407;
                    colorAssigned = orange;
                    editText.getBackground().setColorFilter(orange, PorterDuff.Mode.SRC_ATOP);
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        if (id == R.id.action_takePicture) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1337);
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d("Result  code : ", resultCode + "");
        Log.d("Request code : ", requestCode + "");

        if (resultCode == RESULT_OK && requestCode != 1337) {
            imageView.setImageBitmap(null);

            orgUri = data.getData();
            Log.d("Returned Uri", orgUri.toString());

            //path converted from URI
            convertedPath = getRealPathFromURI(orgUri);

            Log.d("Real Path", convertedPath);

            uriFromPath = Uri.fromFile(new File(convertedPath));

            Log.d("Back Uri", uriFromPath.toString());

            try {
                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(orgUri));
                imageView.setImageBitmap(bm);
                MainActivity.imageMap.put(MainActivity.itemNumber, orgUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == 1337) {


            imageView.setImageBitmap(null);
            orgUri = data.getData();
            Log.d("Returned Uri", orgUri.toString());
            try {
                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(orgUri));

                bm = Bitmap.createScaledBitmap(bm, 1000, 1000, true);
                imageView.setImageBitmap(bm);
                MainActivity.imageMap.put(MainActivity.itemNumber, orgUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
    // URI to path convertor
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    // for getting the Camera and storage access permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                loadImage();

            } else {
                // Permission Denied
                Toast.makeText(ENotee.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                launchCamera();

                Toast.makeText(ENotee.this, "Camera Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                // Permission Denied
                Toast.makeText(ENotee.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    // to load the image
    public void loadImage() {

        //code to access the gallery
        Uri tempUri = MainActivity.imageMap.get(MainActivity.itemNumber);
        if (tempUri != null)
            try {
                Bitmap bm = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(tempUri));
                bm = Bitmap.createScaledBitmap(bm, 1000, 1000, true);
                imageView.setImageBitmap(bm);
                MainActivity.imageMap.put(MainActivity.itemNumber, tempUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    // for launching the camera
    public void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1337);
    }

}
