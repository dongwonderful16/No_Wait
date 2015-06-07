package com.cameron.no_wait;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    Location lastLocation;
    private double lastAccuracy = (double) 1e10;
    private long lastAccuracyTime = 0;

    protected double lat;
    protected double lng;
    String msg_id;
    // public AppInfo appInfo;

    private static final String LOG_TAG = "Cameron";

    private static final float GOOD_ACCURACY_METERS = 100;

    // This is an id for my app, to keep the key space separate from other apps.
    private static final String MY_APP_ID = "NoWait";

    //private static final String SERVER_URL_PREFIX = "https://luca-teaching.appspot.com/store/default/";
    private static final String SERVER_URL_PREFIX ="https://hw3n-dot-luca-teaching.appspot.com/store/default/";

    // To remember the favorite account.
    public static final String PREF_ACCOUNT = "pref_account";

    // To remember the post we received.
    public static final String PREF_POSTS = "pref_posts";

    // Uploader.
    //private ServerCall uploader;

    // Remember whether we have already successfully checked in.
    private boolean checkinSuccessful = false;

    private ArrayList<String> accountList;

    private class ListElement {
        ListElement() {};

        public String textLabel;
        public String listText;
        public String buttonLabel;

    }

    private ArrayList<ListElement> aList;

    private class MyAdapter extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
            this.context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            final ListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(resource,  newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            //TextView tv = (TextView) newView.findViewById(R.id.itemText);
            Button b = (Button) newView.findViewById(R.id.itemButton);
           // tv.setText(w.textLabel);
            b.setText(w.buttonLabel);

            // Sets a listener for the button, and a tag for the button as well.
            b.setTag(new Integer(position));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Reacts to a button press.
                    // Gets the integer tag of the button.
                    String s = v.getTag().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, s, duration);
                    toast.show();
                }
            });

            // Set a listener for the whole list item.
            newView.setTag(w.textLabel);
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    Log.i(LOG_TAG, "DESTID: " + w.listText);
                    intent.putExtra("userid", w.listText);
                    startActivity(intent);
                    String s = v.getTag().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, s, duration);
                    toast.show();
                }
            });

            return newView;
        }
    }

    private MyAdapter aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //appInfo = AppInfo.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aList = new ArrayList<ListElement>();
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        // First super, then do stuff.
        // Let us display the previous posts, if any.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String result = settings.getString(PREF_POSTS, null);
        if (result != null) {
            try {
                displayResult(result);
            } catch (Exception e) {
                // Removes settings that can't be correctly decoded.
                Log.w(LOG_TAG, "Failed to display old messages: " + result + " " + e);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove(PREF_POSTS);
                editor.commit();
            }
        }
        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        // String result = settings.getString(PREF_POSTS, null);
        if (result != null) {
            displayResult(result);
        }
    }
*/

/*
    @Override
    protected void onPause() {
        // Stops the upload if any.
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
        if (uploader != null) {
            uploader.cancel(true);
            uploader = null;
        }
        super.onPause();
    }

*/
    public void clickMap(View v) {
        Uri MapsActivity = Uri.parse("geo:0,0?q=restaurants");
        //Uri geoLocation = Uri.parse("geo:0,0?q=restaurants");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, MapsActivity);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        //Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setPackage("com.google.android.apps.maps");
        //intent.setData(geoLocation);
        //if (intent.resolveActivity(getPackageManager()) != null) {
        //    startActivity(intent);
        //}


        //Intent intent = new Intent(this, MapsActivity.class);
        //intent.putExtra("lat", lat);
        //intent.putExtra("lng", lng);
        //startActivity(intent);
        /*if (lastLocation != null) {
            // Get the text we want to send.
            ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
            spinner.setVisibility(View.VISIBLE);                                   //make the spinner visible
            //EditText et = (EditText) findViewById(R.id.editText);
            //String msg = et.getText().toString();

            // Then, we start the call.
            PostMessageSpec myCallSpec = new PostMessageSpec();


            myCallSpec.url = SERVER_URL_PREFIX + "put_local";
            myCallSpec.context = MainActivity.this;
            // Let's add the parameters.
            HashMap<String, String> m = new HashMap<String, String>();  //creates a hashmap of the given values
            m.put("lat", String.valueOf(lat));
            m.put("lng", String.valueOf(lng));
            //m.put("userid", appInfo.userid);
            myCallSpec.setParams(m);                        //calls to the server

            // Actual server call.
            if (uploader != null) {
                // There was already an upload in progress.
                uploader.cancel(true);
            }
            uploader = new ServerCall();
            uploader.execute(myCallSpec);
            //et.setText("", EditText.BufferType.EDITABLE); //clears the text view
        }else{
            Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_LONG ).show();
        } */
    }

    private String reallyComputeHash(String s) {
        // Computes the crypto hash of string s, in a web-safe format.
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());
            digest.update("My secret key".getBytes());
            byte[] md = digest.digest();
            // Now we need to make it web safe.
            String safeDigest = Base64.encodeToString(md, Base64.URL_SAFE);
            return safeDigest;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * This class is used to do the HTTP call, and it specifies how to use the result.
     */
    /*
    class PostMessageSpec extends ServerCallSpec {
        @Override
        public void useResult(Context context, String result) {
            if (result == null) {
                // Do something here, e.g. tell the user that the server cannot be contacted.
                Log.i(LOG_TAG, "The server call failed.");
            } else {
                // Translates the string result, decoding the Json.
                Log.i(LOG_TAG, "Received string: " + result);
                displayResult(result);
                // Stores in the settings the last messages received.
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_POSTS, result);
                editor.commit();
            }
        }
    }  */


   /* private void displayResult(String result) {
        Gson gson = new Gson();
        Data ml = gson.fromJson(result, Data.class);
        Log.i(LOG_TAG, result);          //print the log good for debugging
        // Fills aList, so we can fill the listView.
        aList.clear();
        for (int i = 0; i < ml.RestaurantList.length; i++) {       //print out the list of restaurants
            ListElement ael = new ListElement();
            ael.textLabel = ml.RestaurantList[i].Restaurant + "\n" + ml.RestaurantList[i].loc +"\n" + ml.RestaurantList[i].wait;
            // ael.listText = ml.messages[i].userid;
            ael.buttonLabel = "Click";
            aList.add(ael);
        }
        aa.notifyDataSetChanged();
        ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.INVISIBLE);             //make the progress bar invisible
    }
*/

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //TextView tv = (TextView) findViewById(R.id.textView);
            lastLocation = location;
            lat = lastLocation.getLatitude();
            lng = lastLocation.getLongitude();
            float acc = location.getAccuracy();

            String Text = "Latitude: " + lat + "\nLongitude: " +lng + "\nAccuracy: " + acc;
            //tv.setText(Text);                  //write the location
            //Toast.makeText(getApplicationContext(), "Location Updated,"+lat+" "+lng, Toast.LENGTH_LONG ).show();
            // Do something with the location you receive.
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    public void clickRefresh(View v) {
        Intent intent = new Intent(this, MapList.class);
        //intent.putExtra("lat", lat);
        //intent.putExtra("lng", lng);
        startActivity(intent);
        /*if (lastLocation != null) {
            ProgressBar spinner = (ProgressBar)findViewById(R.id.progressBar);     //make spinner visible
            spinner.setVisibility(View.VISIBLE);
            PostMessageSpec myCallSpec = new PostMessageSpec();
            myCallSpec.url = SERVER_URL_PREFIX + "get_local";
            myCallSpec.context = MainActivity.this;
            HashMap<String, String> m = new HashMap<String, String>();   //create hashmap with just long and lat
            m.put("lat", String.valueOf(lat));
            m.put("lng", String.valueOf(lng));
            //m.put("userid", appInfo.userid);
            //m.put("public", pub);
            myCallSpec.setParams(m);                                //run server call get_local with m as parameters

            if (uploader != null) {
                // There was already an upload in progress.
                uploader.cancel(true);
            }
            uploader = new ServerCall();
            uploader.execute(myCallSpec);
        }else{
            Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_LONG ).show();          //if no location send message
        }
     */
    }
}