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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hulldiscover.zeus.locationdistancecalulation.Adapter.ListAdapter;
import com.hulldiscover.zeus.locationdistancecalulation.Helper.XMLPullParserHandler;
import com.hulldiscover.zeus.locationdistancecalulation.Model.Event;
import com.hulldiscover.zeus.locationdistancecalulation.R;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.this.TAG;

    EditText mUserInputXCoordinate;
    EditText mUserInputYCoordinate;
    Button mReturnClosbyEventsButton;
    ListView mListview; //ListView
    ListAdapter mListAdapter; //ListAdapter
    int mNumberOfResultsToDisplay = 5; // number rof results to display to user

    Float xCoordinateAsInteger = 0f;
    Float yCoordinateAsInteger = 0f;

    //map
    Map<String, Float> map = new HashMap<String, Float>();
    Map<String, Integer> eventWithDistance = new HashMap<String, Integer>();
    Map<String, Event> eventWithCalculatedDistance = new HashMap<String, Event>();
    Map<String, Event> events = new HashMap<String, Event>();

    // list of vending stock
    ArrayList<Event> eventListings;
    ArrayList<String> imageURLs = new ArrayList<String>();
    ArrayList<Integer> mEventID = new ArrayList<Integer>();
    ArrayList<Event> mEvents = new ArrayList<Event>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // used for later reference for call Android snackbar
        final View parentLayout = findViewById(R.id.textView);

        // user input values
        mUserInputXCoordinate = (EditText) findViewById(R.id.inputXCoordinate);
        mUserInputYCoordinate = (EditText) findViewById(R.id.inputYCoordinate);

        // reference button
        mReturnClosbyEventsButton = (Button) findViewById(R.id.findButton);

        // validate user input
        /*if (!validate()) { // if validation failed
            validationFailed(); // alert user
            return;
        }*/

            /*// if validation was successful
            mReturnClosbyEventsButton.setEnabled(false); // enable 'find nearby' events button
            try {
                // convert string input to Integer value
                xCoordinateAsInteger = Integer.parseInt(mUserInputXCoordinate.getText().toString());
                yCoordinateAsInteger = Integer.parseInt(mUserInputYCoordinate.getText().toString());
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }*/

        /*if(mUserInputXCoordinate.getText().toString() !="" && mUserInputYCoordinate.getText().toString() !="") {
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
        }*/

            // gather event items from XML file
            retrieveEventListings();


            // find nearby events button
            // on button click
            mReturnClosbyEventsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // validate inputs
                    // validate user input
                    if (!validate()) { // if validation failed
                        validationFailed(); // alert user
                        return;
                    }

                    // validation was successful
                    mReturnClosbyEventsButton.setEnabled(false); // enable 'find nearby' events button
                    try {
                        // convert string input to Integer value
                        xCoordinateAsInteger = Float.parseFloat(mUserInputXCoordinate.getText().toString());
                        yCoordinateAsInteger = Float.parseFloat(mUserInputYCoordinate.getText().toString());
                    } catch (NumberFormatException exception) {
                        exception.printStackTrace();
                    }

                    //list of calculated distances
                    ArrayList<Integer> calculatedDistances = calculateDistanceBetweenLocations(eventListings, xCoordinateAsInteger, yCoordinateAsInteger);
                    Collections.sort(calculatedDistances);

                    //List<Event> mList = new ArrayList<Event>(); //remove
                    ArrayList<Event> mQuestionList = new ArrayList<Event>();

                    //display events that are closest to user i.e smaller distance
                    for (int i = 0; i < mNumberOfResultsToDisplay; i++) {
                        Log.d("Sorted List", calculatedDistances.get(i).toString());
                        eventWithCalculatedDistance.get("Event " + map.get("Event " + i + " ID")); //TODO

                        eventWithDistance.get("Event " + map.get("Event " + i + " ID")); //event ID
                        eventWithDistance.get("Event " + map.get("Event " + i + " ID") + " Distance"); //distance event is from user
                        mEventID.add(eventWithDistance.get("Event " + map.get("Event " + i + " ID"))); // add event ids to list
                        mEvents.add(eventWithCalculatedDistance.get("Event " + map.get("Event " + i + " ID")));
                        events.get("Event " + i);
                        mQuestionList.add(mEvents.get(i)); // remove
                    }

                    // display message to user
                    Snackbar.make(parentLayout, getString(R.string.snackbar_message), Snackbar.LENGTH_SHORT).show();


                    //pass data by intents
                    Intent intent = new Intent(MainActivity.this, NearbyEventsList.class);
                    intent.putExtra("EventID", mEventID);

                    Gson gson = new Gson();
                    Type listOfTestObject = new TypeToken<List<Event>>(){}.getType();
                    String s = gson.toJson(mEvents, listOfTestObject);
                    intent.putExtra("obj", s);

                    //intent.putExtra("event", mQuestionList);
                    //intent.putExtra("data", new DataWrapper(mEvents));
                    startActivity(intent);


                }
            });

    }

    /*
     * gather event items from XML file
      */
    private void retrieveEventListings() {
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            eventListings = parser.parse(getAssets().open("event_listings.xml"));
            // add url of each vending item
            for (int i = 0; i < eventListings.size(); i++) {
                imageURLs.add(eventListings.get(i).getEvent_image());
                eventListings.get(i).setEvent_image(imageURLs.get(i));
                eventListings.get(i).setLocation(eventListings.get(i).getLocation().x, eventListings.get(i).getLocation().y);
                map.put("Event " + i + " ID", (float)eventListings.get(i).getId());
                map.put("Event " + i + " LocationX", eventListings.get(i).getLocation().x);
                map.put("Event " + i + " LocationY", eventListings.get(i).getLocation().y);
                events.put("Event " + i, eventListings.get(i)); // map events
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Validate a user's coordinate input
    */
    public boolean validate() {
        boolean valid = true;

        // get text input from user as String
        String userXCoordinate = mUserInputXCoordinate.getText().toString();
        String userYCoordinate = mUserInputYCoordinate.getText().toString();

        // check X coordinate is not blank
        if (userXCoordinate.isEmpty()) {
            // show error message to user
            mUserInputXCoordinate.setError("enter a X coordinate");
            // set X coordinate as NOT valid
            valid = false;
        } else {
            // set X coordinate as valid
            mUserInputXCoordinate.setError(null);
        }

        // check X coordinate is not blank
        if (userYCoordinate.isEmpty()) {
            // show error message to user
            mUserInputYCoordinate.setError("enter a Y coordinate");
            // set Y coordinate as NOT valid
            valid = false;
        } else {
            // set Y coordinate as valid
            mUserInputYCoordinate.setError(null);
        }

        // when a user's coordinates are entered
        if (userXCoordinate.isEmpty() == false && userYCoordinate.isEmpty() == false) {
            // check X Coordinate is valid between the range of 0 - 10
            if (Double.parseDouble(userXCoordinate) < (-10) || Double.parseDouble(userXCoordinate) > 10) {
                // show error message to user
                mUserInputXCoordinate.setError("enter a valid X coordinate between 0 and 10");
                // set X coordinate as NOT valid
                valid = false;
            } else {
                // set X coordinate as valid
                mUserInputXCoordinate.setError(null);
            }

            // check Y Coordinate is valid between the range of 0 - 10
            if (Double.parseDouble(userYCoordinate) < (-10) || Double.parseDouble(userYCoordinate) > 10) {
                // show error message to user
                mUserInputYCoordinate.setError("enter a valid Y coordinate between 0 and 10");
                // set Y coordinate as NOT valid
                valid = false;
            } else {
                // set Y coordinate as valid
                mUserInputYCoordinate.setError(null);
            }
        }

        // return whether user input is valid
        return valid;
    }

    /*
    * Validate of a user's coordinate inputs have failed
    *
    * This method alerts the user that there input is inncorrect
    */
    public void validationFailed() {
        Toast.makeText(MainActivity.this, "Something is wrong, please check your coordinates", Toast.LENGTH_LONG).show();
        mReturnClosbyEventsButton.setEnabled(true); // disable 'find nearby' events button
    }

    public class DataWrapper implements Serializable {

        private ArrayList<Event> parliaments;

        public DataWrapper(ArrayList<Event> data) {
            this.parliaments = data;
        }

        public ArrayList<Event> getParliaments() {
            return this.parliaments;
        }

    }

    /*
    * Calculate the distance between a user's coordinates againsts the coordinates of events nearby
    */
    private ArrayList<Integer> calculateDistanceBetweenLocations(ArrayList<Event> eventListings, Float userXInput, Float userYInput) {
        //init x, y coordinates
        Float xCoordinateAsInteger = userXInput;
        Float yCoordinateAsInteger = userYInput;

        //list of calculated distance
        ArrayList<Integer> distances = new ArrayList<>();

        //loop through all events
        for (int i = 0; i < eventListings.size(); i++) {

            //determine the distance between the event and the user
            Float x1 = xCoordinateAsInteger; // user input
            Float y1 = yCoordinateAsInteger; // user input
            Float x2 = map.get("Event " + i + " LocationX"); //event location x, coordinate
            Float y2 = map.get("Event " + i + " LocationY"); //event location y, coordinate

            float calculatedDistance = manhattanDistance(x1, x2, y1, y2); // calculated distance between user and event
            distances.add((int)calculatedDistance); // add distance to list
            eventWithCalculatedDistance.put("Event " + map.get("Event " + i + " ID"), eventListings.get(i)); //TODO

            eventWithDistance.put("Event " + map.get("Event " + i + " ID"), eventListings.get(i).getId()); //event ID
            eventWithDistance.put("Event " + map.get("Event " + i + " ID") + " Distance", (int)calculatedDistance); //distance event is from user

            Log.d(TAG, "Event ID " + map.get("Event " + i + " ID") + "," + " Location at: X-" + map.get("Event " + i + " LocationX") + " And Y-" + map.get("Event " + i + " LocationY") + " Distance= " + String.valueOf(calculatedDistance));

        }
        return distances; // return calculations for distances
    }

    /*
    * Calculate the distance between a user's coordinates againsts the coordinates of events nearby
    * Using the Manhattan Distance
    */
    public Float manhattanDistance(Float x1, Float x2, Float y1, Float y2) {
        Float distance = (Math.abs(x2 - x1)) + (Math.abs(y2 - y1));
        return distance;
    }
}
