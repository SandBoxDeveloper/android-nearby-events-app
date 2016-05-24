package com.hulldiscover.zeus.locationdistancecalulation.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    private static final String TAG = "MainActivity";

    //Members
    EditText mUserInputXCoordinate;
    EditText mUserInputYCoordinate;
    Button mReturnClosbyEventsButton;
    private final int mNumberOfResultsToDisplay = 5; // number rof results to display to user

    Float xCoordinateAsInteger = 0f;
    Float yCoordinateAsInteger = 0f;

    //HashMap
    Map<String, Float> calculatedDistancesMap = new HashMap<String, Float>(); // event's calculated distance away from user
    Map<Event, Integer> eventObjectWithCalculatedDistance = new HashMap<Event, Integer>(); // event and its distance away from user

    //Booleans to allow ordering of HashMaps
    public static boolean ASC = true; // Ascending order
    public static boolean DESC = false; // Descending order

    //List of Events
    ArrayList<Event> eventListings; // all events
    ArrayList<String> imageURLs = new ArrayList<String>();
    ArrayList<Event> mNearbyEvents = new ArrayList<Event>(); // events nearby to user's location


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

                // display message to user, that closeby events are being loaded
                Snackbar.make(parentLayout, getString(R.string.snackbar_message), Snackbar.LENGTH_SHORT).show();

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

                // list of calculated distances
                Map<Event, Integer> calculatedDistances = calculateDistanceBetweenLocations(eventListings, xCoordinateAsInteger, yCoordinateAsInteger);

                /*
                 * Sort results of calculated distance (CalculatedDistances)
                 * That contains how far-away the user is from
                 * All listed Events in ascending order (smallest to highest).
                 */
                Map<Event, Integer> sortedMapAsc = sortByComparator(calculatedDistances, ASC);
                /*
                 * Loop through results of calculated distances
                 * From listed events, to the user's location.
                 * Add n number of events closest to user
                 * Defined by mNumberOfResultsToDisplay global member variable
                 */
                List<Map.Entry<Event, Integer>> list = new LinkedList<Map.Entry<Event, Integer>>(sortedMapAsc.entrySet()); // sorted list of distances

                for (int counter = 0; counter < mNumberOfResultsToDisplay; counter++) {
                    //TODO: What if there are two events with the same distance away from the user?
                    Event key = list.get(counter).getKey();
                    System.out.println(key.getId() + " = " + list.get(counter).getValue());
                    mNearbyEvents.add(key); // add closet events
                    mNearbyEvents.get(counter).setDistance(list.get(counter).getValue()); // set event's distance away from user
                }

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
                String eventObject = gson.toJson(mNearbyEvents, listOfEventObjects);

                // 5. pass data as extras via intent
                intent.putExtra("obj", eventObject);

                // 6. start 'NearbyEventsList' activity, and pass data objects to it
                startActivity(intent);

            }
        });

    }

    /*
     * This method sorts a Hashmap,
     * In ascending, or descending order.
     *
     * Parameters must into a Hashmap with input Event Object, and Integer Type.
     * And boolean type, to represent what order the hashmap should be. i.e ASC or DESC
     *
     * Returns order/sorted HashMap.
     *
     * Referenced from http://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
     * User - Rais Alarm. Answered Dec 17 '12 at 11:24.
     */
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

    /*
     * This method prints HashMap
     * Key and Value.
     *
     * Parameters for method must include HashMap with types
     * Event, and Integer.
     *
     * Returns Void.
     */
    public static void printMap(Map<Event, Integer> map)
    {
        for (Map.Entry<Event, Integer> entry : map.entrySet())
        {
            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
        }
    }

    /*
     * Retrieve event items from XML file
     *
     * Return void.
     */
    public void retrieveEventListings() {
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            eventListings = parser.parse(getAssets().open("event_listings.xml"));
            // add url of each vending item
            for (int i = 0; i < eventListings.size(); i++) {
                imageURLs.add(eventListings.get(i).getEvent_image()); // get URL's of event images
                eventListings.get(i).setEvent_image(imageURLs.get(i)); // set event images
                eventListings.get(i).setLocation(eventListings.get(i).getLocation().x, eventListings.get(i).getLocation().y); // set event coordinates
                /*
                 * Map a few event details to a HashMap,
                 * For later reference.
                 * Only Event ID, Event Coordinate X and Coordinate Y,
                 * Are mapped.
                 */
                calculatedDistancesMap.put("Event " + i + " ID", (float) eventListings.get(i).getId()); // Event ID
                calculatedDistancesMap.put("Event " + i + " LocationX", eventListings.get(i).getLocation().x); // Event Coordinate X
                calculatedDistancesMap.put("Event " + i + " LocationY", eventListings.get(i).getLocation().y); // Event Coordinate Y
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Validate a user's inputs - x and y coordinates
     *
     * Returns boolean, either True or False.
     */
    public boolean validate() {
        boolean valid = true;

        // get text input from user as String
        String userXCoordinate = mUserInputXCoordinate.getText().toString();
        String userYCoordinate = mUserInputYCoordinate.getText().toString();

        // check X coordinate is not blank
        if (userXCoordinate.isEmpty()) {
            // show error message to user
            mUserInputXCoordinate.setError(getString(R.string.enter_x_coordinate));
            // set X coordinate as NOT valid
            valid = false;
        } else {
            // set X coordinate as valid
            mUserInputXCoordinate.setError(null);
        }

        // check X coordinate is not blank
        if (userYCoordinate.isEmpty()) {
            // show error message to user
            mUserInputYCoordinate.setError(getString(R.string.enter_y_coordinate));
            // set Y coordinate as NOT valid
            valid = false;
        } else {
            // set Y coordinate as valid
            mUserInputYCoordinate.setError(null);
        }

        // when a user's coordinates are entered
        if (userXCoordinate.isEmpty() == false && userYCoordinate.isEmpty() == false) {
            // check X Coordinate is valid between the range of -10 to 10
            if (Double.parseDouble(userXCoordinate) < (-10) || Double.parseDouble(userXCoordinate) > 10) {
                // show error message to user
                mUserInputXCoordinate.setError(getString(R.string.xCoordinate_error_message));
                // set X coordinate as NOT valid
                valid = false;
            } else {
                // set X coordinate as valid
                mUserInputXCoordinate.setError(null);
            }

            // check Y Coordinate is valid between the range of -10 to 10
            if (Double.parseDouble(userYCoordinate) < (-10) || Double.parseDouble(userYCoordinate) > 10) {
                // show error message to user
                mUserInputYCoordinate.setError(getString(R.string.yCoordinate_error_message));
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
    *
    * Returns Void.
    */
    public void validationFailed() {
        Toast.makeText(MainActivity.this, "Something is wrong, please check your coordinates", Toast.LENGTH_LONG).show(); // alert user
        mReturnClosbyEventsButton.setEnabled(true); // disable 'find nearby' events button
    }

    /*
    * Calculate the distance between a user's coordinates againsts the coordinates of listed events
    * This method returns a Hashtable - The event mapped with the distance the user is from this event
    *
    * Returns HashMap of calculated Distances.
    */
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

            // event location X coordinate
            Float x2 = calculatedDistancesMap.get(getResources().getString(R.string.in_string_event_tag)
                    + i + getResources().getString(R.string.in_string_locationX));
            // event location Y coordinate
            Float y2 = calculatedDistancesMap.get(getResources().getString(R.string.in_string_event_tag)
                    + i + getResources().getString(R.string.in_string_locationY));

            // calculate the distance, using Manhattan Distance
            float calculatedDistance = manhattanDistance(x1, x2, y1, y2); // calculated distance between user and event

            // add the event, and how far-away the user is from the event to the Hashmap
            eventObjectWithCalculatedDistance.put(eventListings.get(i), (int) calculatedDistance);

            // Log statements to display calculations
            // Involved with calculating the distance from
            // A user's location
            // And a Event
            Log.d(TAG, getResources().getString(R.string.DISTANCE_CALCULATIONS));

             /*
             * This log statement displays the Event Title,
             * And how far-away the event is
             * From the user's location.
             */
            Log.d(eventListings.get(i).getTitle(),
                    // get and display the Event Title
                    getResources().getString(R.string.in_string_distance_away_from)
                            + Float.toString(calculatedDistance)); // Title vs Calculated Distance

            /*
             * This log statement displays the Event ID,
             * And far-away the event is
             * From the user's location.
             */
            Log.d(getResources().getString(R.string.eventID_tag)
                    // get and display the eventID
                    + calculatedDistancesMap.get(getResources().getString(R.string.in_string_event_tag)
                    + i + getResources().getString(R.string.in_string_id_tag))

                    // get and display calculated distance away from the user
                    , getResources().getString(R.string.in_string_distance_away_from)
                    + Float.toString(calculatedDistance)); // Title vs Calculated Distance


            /*
             * This log statement displays the Event ID,
             * It's X and Y coordinates,
             * And the calculated distance away from the user's
             * Location.
             *
             * String resources are used instead of using string literals
             * Within this Log statement.
             */
            Log.d(getResources().getString(R.string.DISTANCE_CALCULATION_DETAILS_TAG),
                    getResources().getString(R.string.in_string_eventID_tag)

                    // get and display Event ID
                    + calculatedDistancesMap.get(getResources().getString(R.string.in_string_event_tag)
                    + i + getResources().getString(R.string.in_string_id_tag))
                    + getResources().getString(R.string.comma)

                    // get and display coordinate X of event
                    + getResources().getString(R.string.in_string_coordinateX)
                    + calculatedDistancesMap.get(getResources().getString(R.string.in_string_event_tag)
                    + i + getResources().getString(R.string.in_string_locationX))

                    // get and display coordinate Y of event
                    + getResources().getString(R.string.in_string_coordinateY)
                    + calculatedDistancesMap.get(getResources().getString(R.string.in_string_event_tag)
                    + i + getResources().getString(R.string.in_string_locationY))

                    // get and display calculated distance away from the user
                    + getResources().getString(R.string.in_string_distance) + String.valueOf(calculatedDistance));

        }
        //return distances; // return calculations for distances
        return eventObjectWithCalculatedDistance;
    }

    /*
    * Calculate the distance between a user's coordinates againsts the coordinates of events nearby
    * Using the Manhattan Distance
    *
    * Returns Float value.
    */
    public Float manhattanDistance(Float x1, Float x2, Float y1, Float y2) {
        Float distance = (Math.abs(x2 - x1)) + (Math.abs(y2 - y1));
        return distance;
    }
}
