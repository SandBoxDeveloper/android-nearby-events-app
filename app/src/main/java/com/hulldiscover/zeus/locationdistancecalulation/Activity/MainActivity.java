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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.this.TAG;

    EditText mUserInputXCoordinate;
    EditText mUserInputYCoordinate;
    Button mReturnClosbyEventsButton;
    ListView mListview; //ListView
    ListAdapter mListAdapter; //ListAdapter
    int mNumberOfResultsToDisplay = 3; // number rof results to display to user

    Float xCoordinateAsInteger = 0f;
    Float yCoordinateAsInteger = 0f;

    //map
    Map<String, Float> map = new HashMap<String, Float>();
    Map<String, Integer> eventWithDistance = new HashMap<String, Integer>();
    Map<String, Event> eventWithCalculatedDistance = new HashMap<String, Event>();
    Map<Event, Integer> eventObjectWithCalculatedDistance = new HashMap<Event, Integer>();
    Map<String, Event> events = new HashMap<String, Event>();

    public static boolean ASC = true;
    public static boolean DESC = false;

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

        // user input values, x and y coordinates
        mUserInputXCoordinate = (EditText) findViewById(R.id.inputXCoordinate);
        mUserInputYCoordinate = (EditText) findViewById(R.id.inputYCoordinate);

        // reference to button
        mReturnClosbyEventsButton = (Button) findViewById(R.id.findButton);

        // gather event items from XML file
        retrieveEventListings();

        // on button click
        // find events nearby to user coordinates
        mReturnClosbyEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // validate user input
                if (!validate()) { // if validation failed
                    validationFailed(); // alert user and disable 'find nearby' events button
                    return; // allow user to input values again
                }

                // validation was successful
                mReturnClosbyEventsButton.setEnabled(false); // enable 'find nearby' events button
                try {
                    // grab user inputs, x and y coordinates

                    // setup local variables to convert string input from
                    // the user's device
                    // to an Integer values
                    xCoordinateAsInteger = Float.parseFloat(mUserInputXCoordinate.getText().toString());
                    yCoordinateAsInteger = Float.parseFloat(mUserInputYCoordinate.getText().toString());
                } catch (NumberFormatException exception) {
                    exception.printStackTrace();
                }

                //list of calculated distances
                //ArrayList<Integer> calculatedDistances = calculateDistanceBetweenLocations(eventListings, xCoordinateAsInteger, yCoordinateAsInteger);
                //Collections.sort(calculatedDistances);

                /*ArrayList<Integer> calculatedDistances = calculateDistanceBetweenLocations(eventListings, xCoordinateAsInteger, yCoordinateAsInteger);
                for (int j = 0; j < eventObjectWithCalculatedDistance.size(); j++) {
                    Collections.sort(eventObjectWithCalculatedDistance.values());
                }*/

               // Collections.sort(calculatedDistances);

                ArrayList<Event> mQuestionList = new ArrayList<Event>();

                Map<Event, Integer> calculatedDistances = calculateDistanceBetweenLocations(eventListings, xCoordinateAsInteger, yCoordinateAsInteger);
                Map<Event, Integer> sortedMapAsc = sortByComparator(calculatedDistances, ASC);
                /*for (Map.Entry<Event,Integer> entry : calculatedDistances.entrySet()) {
                    Event key = entry.getKey();
                    Integer value = entry.getValue();
                    Log.d("Key: ", key.getTitle().toString());
                    Log.d("Value: ", value.toString());
                    mEvents.add(entry.getKey());
                    // do stuff
                    //Map<Event, Integer> treeMap = new TreeMap<Event, Integer>(calculatedDistances);
                    //Log.d("Sorted Map", treeMap.toString());
                }*/


                //Map<Event, Integer> treeMap = new TreeMap<Event, Integer>(eventObjectWithCalculatedDistance);
                //Log.d("Sorted Map", treeMap.toString());
                /*for(int q = 0 ; q < eventObjectWithCalculatedDistance.size(); q++) {
                    int eventItem = eventObjectWithCalculatedDistance.get(q);

                }*/

                List<Map.Entry<Event, Integer>> list = new LinkedList<Map.Entry<Event, Integer>>(sortedMapAsc.entrySet());

                    /*
                    * Loop through results of calculated distances
                    * From listed events, to the user's location.
                    * Add n number of events closest to user
                    * Defined by mNumberOfResultsToDisplay global member variable
                    */
                for(int counter  = 0; counter < mNumberOfResultsToDisplay; counter++) {
                    Event key = list.get(counter).getKey();
                    System.out.println(key.getId() + " = " + list.get(counter).getValue());
                    mEvents.add(key); // add closet events
                }

                //display events that are closest to user i.e smaller distance
                //for (int i = 0; i < mNumberOfResultsToDisplay; i++) {
                    //Log.d("Sorted List", calculatedDistances.get(i).toString());
                   /* Log.d("Cal Distance", eventWithCalculatedDistance.get("Event " + map.get("Event " + i + " ID")).getTitle());
                    eventWithCalculatedDistance.get("Event " + map.get("Event " + i + " ID")); //TODO

                    eventWithDistance.get("Event " + map.get("Event " + i + " ID")); //event ID
                    eventWithDistance.get("Event " + map.get("Event " + i + " ID") + " Distance"); //distance event is from user
                    mEventID.add(eventWithDistance.get("Event " + map.get("Event " + i + " ID"))); // add event ids to list
                    mEvents.add(eventWithCalculatedDistance.get("Event " + map.get("Event " + i + " ID")));
                    events.get("Event " + i);
                    mQuestionList.add(mEvents.get(i)); // remove*/

                    /*for (Map.Entry<Event,Integer> entry = 0; b < mNumberOfResultsToDisplay; b++) {
                        mEvents.add(sortedMapAsc);
                    }*/

                   /* Iterator it = sortedMapAsc.entrySet().iterator();
                    while (sortedMapAsc.size() < mNumberOfResultsToDisplay)  {
                        Map.Entry pair = (Map.Entry)it.next();
                        Event key = (Event)pair.getKey();
                        System.out.println(key.getId() + " = " + pair.getValue());
                        mEvents.add((Event)pair.getKey());
                        it.remove(); // avoids a ConcurrentModificationException
                    }*/



                    /*for (Map.Entry<Event,Integer> entry : calculatedDistances.entrySet()) {
                        Event key = entry.getKey();
                        Integer value = entry.getValue();
                        Log.d("Key: ", key.getTitle().toString());
                        Log.d("Value: ", value.toString());
                        //Collections.sort(entry);
                        //mEvents.add(entry.getKey());
                        // do stuff
                        //Map<Event, Integer> treeMap = new TreeMap<Event, Integer>(calculatedDistances);
                        //Log.d("Sorted Map", treeMap.toString());
                    }*/
                //}

                // display message to user
                Snackbar.make(parentLayout, getString(R.string.snackbar_message), Snackbar.LENGTH_SHORT).show();


                /*
                * Pass data by intents to NearbyEventList Activity
                * This data will be sent so that the next activity
                * Can display a list of nearby events to the user
                */

                // 1. create Intent
                Intent intent = new Intent(MainActivity.this, NearbyEventsList.class);

                // 2. using Gson Library to parse event objects to 'NearbyEventsList' activity
                Gson gson = new Gson();

                // 3. let Gson know the type of data we're intending to send
                Type listOfEventObjects = new TypeToken<List<Event>>() {  // List of "Event" objects
                }.getType();

                // 4. java 'Event' object to JSON, and assign to a String
                String eventObject = gson.toJson(mEvents, listOfEventObjects);

                // 5. pass data as extras via intent
                intent.putExtra("obj", eventObject);

                // 6. start 'NearbyEventsList' activity, and pass data objects to it
                startActivity(intent);

            }
        });

    }

    private static Map<Event, Integer> sortByComparator(Map<Event, Integer> unsortMap, final boolean order)
    {

        List<Map.Entry<Event, Integer>> list = new LinkedList<Map.Entry<Event, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Event, Integer>>()
        {
            public int compare(Map.Entry<Event, Integer> o1,
                               Map.Entry<Event, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Event, Integer> sortedMap = new LinkedHashMap<Event, Integer>();
        for (Map.Entry<Event, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static void printMap(Map<Event, Integer> map)
    {
        for (Map.Entry<Event, Integer> entry : map.entrySet())
        {
            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
        }
    }

    /*
     * Retrieve event items from XML file
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
                map.put("Event " + i + " ID", (float) eventListings.get(i).getId());
                map.put("Event " + i + " LocationX", eventListings.get(i).getLocation().x);
                map.put("Event " + i + " LocationY", eventListings.get(i).getLocation().y);
                events.put("Event " + i, eventListings.get(i)); // map events
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Validate a user's inputs - x and y coordinates
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
    * A user's coordinates are in-valid
    * This method alerts the user that there input is incorrect
    */
    public void validationFailed() {
        Toast.makeText(MainActivity.this, "Something is wrong, please check your coordinates", Toast.LENGTH_LONG).show(); // alert user
        mReturnClosbyEventsButton.setEnabled(true); // disable 'find nearby' events button
    }

    /*
    * Calculate the distance between a user's coordinates againsts the coordinates of listed events
    * This method returns a Hashtable - The event mapped with the distance the user is from this event
    */
    //private ArrayList<Integer> calculateDistanceBetweenLocations(ArrayList<Event> eventListings, Float userXInput, Float userYInput) {
    private Map<Event, Integer> calculateDistanceBetweenLocations(ArrayList<Event> eventListings, Float userXInput, Float userYInput) {
        //init x, y coordinates
        Float xCoordinateAsInteger = userXInput;
        Float yCoordinateAsInteger = userYInput;

        //list of calculated distance
        ArrayList<Integer> distances = new ArrayList<>();

        //loop through all events
        for (int i = 0; i < eventListings.size(); i++) {

            /*
             * Determine the distance between events and the user
             */
            Float x1 = xCoordinateAsInteger; // user input
            Float y1 = yCoordinateAsInteger; // user input
            Float x2 = map.get("Event " + i + " LocationX"); //event location x coordinate
            Float y2 = map.get("Event " + i + " LocationY"); //event location y coordinate

            // calculate the distance, using Manhattan Distance
            float calculatedDistance = manhattanDistance(x1, x2, y1, y2); // calculated distance between user and event
            distances.add((int) calculatedDistance); // add distance to list


            eventObjectWithCalculatedDistance.put(eventListings.get(i), (int) calculatedDistance);
            Log.d("MainActivity", "Calculation");
            Log.d(eventListings.get(i).getTitle(), "Distance Away from user " + Float.toString(calculatedDistance)); // Title vs Calculated Distance
            Log.d("Event ID: " + map.get("Event " + i + " ID"), "Distance Away from user " + Float.toString(calculatedDistance)); // Title vs Calculated Distance


            eventWithCalculatedDistance.put("Event " + map.get("Event " + i + " ID"), eventListings.get(i)); //TODO

            eventWithDistance.put("Event " + map.get("Event " + i + " ID"), eventListings.get(i).getId()); //event ID
            eventWithDistance.put("Event " + map.get("Event " + i + " ID") + " Distance", (int) calculatedDistance); //distance event is from user

            Log.d(TAG, "Event ID " + map.get("Event " + i + " ID") + "," + " Location at: X-" + map.get("Event " + i + " LocationX") + " And Y-" + map.get("Event " + i + " LocationY") + " Distance= " + String.valueOf(calculatedDistance));

        }
        //return distances; // return calculations for distances
        return eventObjectWithCalculatedDistance;
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
