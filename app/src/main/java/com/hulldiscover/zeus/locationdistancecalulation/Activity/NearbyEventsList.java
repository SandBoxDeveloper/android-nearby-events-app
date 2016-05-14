package com.hulldiscover.zeus.locationdistancecalulation.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.hulldiscover.zeus.locationdistancecalulation.Adapter.ListAdapter;
import com.hulldiscover.zeus.locationdistancecalulation.Helper.XMLPullParserHandler;
import com.hulldiscover.zeus.locationdistancecalulation.Model.Event;
import com.hulldiscover.zeus.locationdistancecalulation.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Zeus on 14/05/16.
 */
public class NearbyEventsList extends AppCompatActivity{

    //members
    ListView mListview;
    ListAdapter mListAdapter;
    ArrayList<Event> mAllEventsList;
    ArrayList<Event> mNearbyEventsList;
    ArrayList<String> imageURLs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_events_listview);

        //SETUP
        mListview = (ListView)findViewById(R.id.listView); // find listView layout
        imageURLs = new ArrayList<String>(); // url of events images
        mNearbyEventsList = new ArrayList<Event>(); // list of nearby events
        ArrayList<Integer> eventID = new ArrayList<Integer>(); // event ids
        //get data

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventID = extras.getIntegerArrayList("EventID");
        }

        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            mAllEventsList = parser.parse(getAssets().open("event_listings.xml"));
            // add url of each vending item
            for(int i = 0; i < mAllEventsList.size(); i++) {
                imageURLs.add(mAllEventsList.get(i).getEvent_image());
                mAllEventsList.get(i).setEvent_image(imageURLs.get(i)); // set event image
                mAllEventsList.get(i).setLocation(mAllEventsList.get(i).getLocation().x, mAllEventsList.get(i).getLocation().y); // set event location

                // from previous activity,
                // events that have been identified as nearby to the user
                // grab from the data-source of events, based on their ID
                // add them to list as nearby events
                mNearbyEventsList.add(mAllEventsList.get(eventID.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //display nearby events to screen
        mListAdapter = new ListAdapter(this, R.layout.listview_event_item, mNearbyEventsList);
        mListview.setAdapter(mListAdapter);
    }
}
