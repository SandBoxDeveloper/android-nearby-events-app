package com.hulldiscover.zeus.locationdistancecalulation.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hulldiscover.zeus.locationdistancecalulation.Adapter.ListAdapter;
import com.hulldiscover.zeus.locationdistancecalulation.Helper.XMLPullParserHandler;
import com.hulldiscover.zeus.locationdistancecalulation.Model.Event;
import com.hulldiscover.zeus.locationdistancecalulation.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText mUserInputXCoordinate;
    EditText mUserInputYCoordinate;
    Button mReturnClosbyEventsButton;
    ListView mListview; //ListView
    ListAdapter mListAdapter; //ListAdapter

    //map
    Map<String, Integer> map = new HashMap<String, Integer>();
    Map<String, Integer> eventWithDistance = new HashMap<String, Integer>();

    // list of vending stock
    ArrayList<Event> eventListings;
    ArrayList<String> imageURLs = new ArrayList<String>();
    ArrayList<Integer> mEventID = new ArrayList<Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // used for later reference for call Android snackbar
        final View parentLayout = findViewById(R.id.textView);

        // find listView layout
        //mListview = (ListView)findViewById(R.id.listView);

        // user input values
        mUserInputXCoordinate = (EditText) findViewById(R.id.inputXCoordinate);
        mUserInputYCoordinate = (EditText) findViewById(R.id.inputYCoordinate);

        // reference button
        mReturnClosbyEventsButton = (Button) findViewById(R.id.findButton);

        // convert inputs to int values
        int xCoordinateAsInteger = 0;
        int yCoordinateAsInteger = 0;
        if(mUserInputXCoordinate.getText().toString() !="" && mUserInputYCoordinate.getText().toString() !="") {
            try {
                xCoordinateAsInteger = Integer.parseInt(mUserInputXCoordinate.getText().toString());
                yCoordinateAsInteger = Integer.parseInt(mUserInputYCoordinate.getText().toString());
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
        }
        else {
            Toast.makeText(MainActivity.this, "Please insert you coordinates, (x, y)",
            Toast.LENGTH_SHORT).show();
        }

        // gather vending items stock from XML file
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            eventListings = parser.parse(getAssets().open("event_listings.xml"));
            // add url of each vending item
            for(int i = 0; i < eventListings.size(); i++) {
                imageURLs.add(eventListings.get(i).getEvent_image());
                eventListings.get(i).setEvent_image(imageURLs.get(i));
                eventListings.get(i).setLocation(eventListings.get(i).getLocation().x, eventListings.get(i).getLocation().y);
                map.put("Event " + i + " ID", eventListings.get(i).getId());
                map.put("Event " + i + " LocationX", eventListings.get(i).getLocation().x);
                map.put("Event " + i + " LocationY", eventListings.get(i).getLocation().y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        mReturnClosbyEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //list of calculated distances
                ArrayList<Integer> calculatedDistances = calculateDistanceBetweenLocations(eventListings);
                Collections.sort(calculatedDistances);

                //display events that are closest to user i.e smaller distance
                for (int i = 0; i < calculatedDistances.size(); i++) {
                    Log.d("Sorted List", calculatedDistances.get(i).toString());
                    eventWithDistance.get("Event " + map.get("Event " + i + " ID")); //event ID
                    eventWithDistance.get("Event " + map.get("Event " + i + " ID") + " Distance"); //distance event is from user
                    mEventID.add(eventWithDistance.get("Event " + map.get("Event " + i + " ID"))); // add event ids to list
                }

                // display message to user
                Snackbar.make(parentLayout, getString(R.string.snackbar_message), Snackbar.LENGTH_SHORT).show();

                //pass data by intents
                Intent intent = new Intent(getBaseContext(), NearbyEventsList.class);
                intent.putExtra("EventID", mEventID);
                startActivity(intent);


            }
        });


            // x location of events
            /*Map<String, Integer> eventLocationX = new HashMap<String, Integer>();
            eventLocationX.put("Event "+i, map.get(i).getLocation().x);

            // y location of events
            Map<String, Integer> eventLocationY = new HashMap<String, Integer>();
            eventLocationX.put("Event "+i, map.get(i).getLocation().y);

            for(int countX = 0; countX < eventLocationX.size(); countX++) { // loop through x values
                for(int countY = 0; countY < eventLocationY.size(); countY++) { // loop through y values

                }
            }
            distances.add(map.get(i).getLocation().x);*/

            //int calculatedDistance = manhattenDistance(map.get(i).getLocation().x, map.get(i).getLocation().x);
            //distances.add(calculatedDistance);


    }

    private ArrayList<Integer> calculateDistanceBetweenLocations(ArrayList<Event> eventListings) {
        //init x, y coordinates
        int xCoordinateAsInteger = 0;
        int yCoordinateAsInteger = 0;

        //list of calculated distance
        ArrayList<Integer> distances = new ArrayList<>();

        //loop through all events
        for (int i = 0; i < eventListings.size(); i++) {

            //determine the distance between the event and the user
            int x1 = xCoordinateAsInteger; // user input
            int y1 = yCoordinateAsInteger; // user input
            int x2 = map.get("Event " + i + " LocationX"); //event location x, coordinate
            int y2 = map.get("Event " + i + " LocationY"); //event location y, coordinate

            int calculatedDistance = manhattenDistance(x1, x2, y1, y2); // calculated distance between user and event
            distances.add(calculatedDistance); // add distance to list
            eventWithDistance.put("Event " + map.get("Event " + i + " ID"), eventListings.get(i).getId()); //event ID
            eventWithDistance.put("Event " + map.get("Event " + i + " ID") + " Distance", calculatedDistance); //distance event is from user

            Log.d("MainActivity", "Event ID " + map.get("Event " + i + " ID") + "," + " Location at: X-" + map.get("Event " + i + " LocationX") + " And Y-" + map.get("Event " + i + " LocationY") + " Distance= " + String.valueOf(calculatedDistance));

        }
        return distances; // return calculations for distances
    }

    public int manhattenDistance(int x1, int x2, int y1, int y2) {
        int distance = (Math.abs(x2 - x1)) + (Math.abs(y2 - y1));
        return distance;
    }
}
