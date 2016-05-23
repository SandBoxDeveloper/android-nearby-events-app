package com.hulldiscover.zeus.locationdistancecalulation.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hulldiscover.zeus.locationdistancecalulation.Adapter.ListAdapter;
import com.hulldiscover.zeus.locationdistancecalulation.Model.Event;
import com.hulldiscover.zeus.locationdistancecalulation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zeus on 14/05/16.
 */
public class NearbyEventsList extends AppCompatActivity{

    //Members
    ListView mListview;
    ListAdapter mListAdapter;
    ArrayList<Event> mNearbyEventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_events_listview);

        // used for later reference for call Android snackbar
        View parentLayout = findViewById(R.id.txt_eventID);

        //SETUP
        mListview = (ListView)findViewById(R.id.listView); // find listView layout

        mNearbyEventsList = new ArrayList<Event>(); // list of nearby events
        ArrayList<Event> nearbyEvents = new ArrayList<Event>(); // event ids

        // Get Data
        // Deserializing JSON, it needs that information to be able
        // To determine what type of object it should deserialize each array element to
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("obj");
        nearbyEvents = gson.fromJson(strObj, new TypeToken<List<Event>>(){}.getType());


        /* from previous activity,
         * events that have been identified as nearby to the user
         * grab from the data-source of events
         * add them to a list to be displayed to the user
         */
        mNearbyEventsList.clear(); // clear old list
        for(int j = 0; j < nearbyEvents.size(); j++) {
            mNearbyEventsList.add(nearbyEvents.get(j));
        }

        //display nearby events to screen
        mListAdapter = new ListAdapter(this, R.layout.listview_event_item, mNearbyEventsList);
        mListview.setAdapter(mListAdapter);
    }
}
