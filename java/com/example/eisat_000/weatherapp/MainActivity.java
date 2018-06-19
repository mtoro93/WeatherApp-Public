package com.example.eisat_000.weatherapp;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LoaderManager.LoaderCallbacks<List<Weather>>, View.OnClickListener {

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    public static final String LOG_TAG = MainActivity.class.getName();
    private GoogleApiClient mClient;
    private Location mLastLocation;
    private String mLatitudeText;
    private String mLongitudeText;
    private static final String AMDOREN_REQUEST_URL = "https://www.amdoren.com/api/weather.php";
    private static final String AMDOREN_API_LINK = "https://www.amdoren.com";

    private static final String API_KEY = "####################";
    private WeatherAdapter mAdapter;
    private static final int FORECAST_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    /*

    API Key: #########################


    Backlink:
    Powered by <a href="https://www.amdoren.com">Amdoren</a>
    this is html, we can just put the link in as a textview and have an onclicklistener to start a webintent
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }


        //Create a GoogleApiClient instance
        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        ListView forecastListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        forecastListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new WeatherAdapter(this, new ArrayList<Weather>());

        forecastListView.setAdapter(mAdapter);

        TextView apiTextView = (TextView) findViewById(R.id.api_link);
        apiTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        Uri webpage = Uri.parse(AMDOREN_API_LINK);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    protected void onStart()
    {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            if(mClient != null)
                mClient.connect();

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


        super.onStart();
    }

    protected void onStop()
    {
        mClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        }
        catch (SecurityException e)
        {
            Log.e(LOG_TAG, "Error getting last location", e);
        }

        if(mLastLocation != null)
        {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
            Log.e(LOG_TAG, "Latitude: " + mLatitudeText);
            Log.e(LOG_TAG, "Longitude: " + mLongitudeText);

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(FORECAST_LOADER_ID, null, this);
        }
        else
        {
            Log.e(LOG_TAG, "Error getting latitude and longitude");
        }
    }

    @Override
    public void onConnectionSuspended(int cause)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {

    }

    /*protected void createLocationRequest()
    {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsRequest> result =
                LocationServices.SettingsApi.checkLocationSettings(mClient, builder.build());
    }*/



    @Override
    public Loader<List<Weather>> onCreateLoader(int i, Bundle bundle)
    {
        Uri baseUri = Uri.parse(AMDOREN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        Log.e(LOG_TAG, "Latitude for url" + mLatitudeText);
        Log.e(LOG_TAG, "Longitude for url" + mLongitudeText);

        uriBuilder.appendQueryParameter("api_key", API_KEY);
        uriBuilder.appendQueryParameter("lat", mLatitudeText);
        uriBuilder.appendQueryParameter("lon", mLongitudeText);


        //uriBuilder.appendQueryParameter("lat", String.valueOf(mLastLocation.getLatitude()));
       // uriBuilder.appendQueryParameter("lon", String.valueOf(mLastLocation.getLongitude()));


        return new WeatherLoader(this, uriBuilder.toString());
       // return new WeatherLoader(this, HARD_CODED_TEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Weather>> loader, List<Weather> forecasts)
    {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No forecasts found."
        mEmptyStateTextView.setText(R.string.no_forecasts);

        mAdapter.clear();

        if (forecasts != null && !forecasts.isEmpty())
            mAdapter.addAll(forecasts);
    }

    @Override
    public void onLoaderReset(Loader<List<Weather>> loader)
    {
        mAdapter.clear();
    }
}
