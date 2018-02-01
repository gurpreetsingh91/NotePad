package com.example.gpsingh.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by GPSINGH on 31-01-2018.
 */


// this class is a customerAdapter which is used to populate the data in the list view
public class CustomListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int id;
    private List<String> items;

    //contructor of the customAdaptor
    public CustomListAdapter(Context context, int textViewResourceId, List<String> list) {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
    }

    // the complete logic of populating the data (text and color) in the list view is writtten inside
    // the getView()
    @NonNull
    @Override
    public View getView(int position, @Nullable View v, @NonNull ViewGroup parent) {

        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.list_content);


        if (items.get(position) != null) {
            //text.setBackgroundColor();
            text.setText(MainActivity.displayArray.get(position));
            Log.d("adapter", position + "");

            //setting the color of the text views
            if (MainActivity.colorNotesArray != null && MainActivity.colorNotesArray.size() > position)
                text.setBackgroundColor(MainActivity.colorNotesArray.get(position));
            else
                text.setBackgroundColor(MainActivity.defaultColor);

        }

        return mView;
    }
}
