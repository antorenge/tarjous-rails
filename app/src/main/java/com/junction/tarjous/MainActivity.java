package com.junction.tarjous;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import io.proximi.proximiiolibrary.Proximiio;
import io.proximi.proximiiolibrary.ProximiioFactory;
import io.proximi.proximiiolibrary.ProximiioFloor;
import io.proximi.proximiiolibrary.ProximiioGeofence;
import io.proximi.proximiiolibrary.ProximiioImageCallback;
import io.proximi.proximiiolibrary.ProximiioListener;
import io.proximi.proximiiolibrary.ProximiioPathfindingNode;
import io.proximi.proximiiolibrary.ProximiioPathfindingResult;
import io.proximi.proximiiolibrary.ProximiioPlace;

/**
 * Proximiio Demo
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private boolean zoomed;
    private LocationSource.OnLocationChangedListener locationListener;
    private boolean locationEnabled;
    private Proximiio proximiio;
    private ProximiioListener listener;
    private ProximiioFloor lastFloor;
    private ProximiioImageCallback imageCallback;
    private GroundOverlay floorPlan;
    private double currentLatitude, currentLongitude;
    ArrayList<ProximiioGeofence>geofences;

    private final static String TAG = "ProximiioDemo";
    String EMAIL="akaizat1@gmail.com";
    String PASSWORD="qwerty";
    private Polyline line;
    MaterialSearchView searchView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geofences= new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create a Proximiio instance
        proximiio = ProximiioFactory.getProximiio(this);

        // Create a ProximiioListener and add it to Proximiio
        listener = new ProximiioListener() {
            @Override
            public void geofenceEnter(ProximiioGeofence geofence) {
                Log.d(TAG, "Geofence enter: " + geofence.getName());
            }

            @Override
            public void addedGeofence(ProximiioGeofence geofence) {
                super.addedGeofence(geofence);
                geofences.add(geofence);
               // geofence.getn   get geo fence data
            }

            @Override
            public void position(double lat, double lon, double accuracy) {
                setPosition(lat, lon, accuracy);
            }

            @Override
            public void loginFailed(LoginError loginError) {
                Log.e(TAG, "LoginError! (" + loginError.toString() + ")");
            }

            @Override
            public void changedFloor(@Nullable ProximiioFloor floor) {
                lastFloor = floor;

                if (floor != null && floor.hasFloorPlan()) {
                    floor.requestFloorPlanImage(MainActivity.this, imageCallback);
                }
            }
        };

        imageCallback = new ProximiioImageCallback() {
            @Override
            public void loaded(Bitmap bitmap, float width, float height, double[] floorPlanPivot) {
                if (map != null) {
                    if (floorPlan != null) {
                        floorPlan.remove();
                    }

                    floorPlan = map.addGroundOverlay(new GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .position(new LatLng(floorPlanPivot[0], floorPlanPivot[1]), width, height)
                            .zIndex(0));
                }
            }

            @Override
            public void failed() {
                if (map != null && lastFloor != null) {
                    lastFloor.requestFloorPlanImage(getBaseContext(), this);
                }
            }
        };

        proximiio.setActivity(this);
        proximiio.addListener(listener);

        // Login to Proximi.io
        proximiio.setLogin(EMAIL, PASSWORD);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        proximiio.removeListener(listener);
        proximiio.removeActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        proximiio.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        proximiio.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        return true;
    }
    // Called when the map is ready to use
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        tryEnableLocation();

        // Set the location source of the map to Proximiio instead of the native positioning
        map.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                locationListener = onLocationChangedListener;
            }

            @Override
            public void deactivate() {
                locationListener = null;
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (lastFloor.getParent() != null) {
                    ProximiioPlace place = (ProximiioPlace)lastFloor.getParent();
                    ProximiioPathfindingResult result = place.getPath(currentLatitude,currentLongitude,latLng.latitude,
                            latLng.longitude,lastFloor.getID(),lastFloor.getID());
                    if(result!=null){
                        ArrayList<LatLng>points= new ArrayList<LatLng>(result.getPath().size());
                        double previousLat=0;
                        double previousLong=0;
                        float totalDistance=0;
                        for (ProximiioPathfindingNode node:result.getPath()){
                            points.add(new LatLng(node.getLat(),node.getLon()));
                            float[]results=new float[1];
                            if(previousLat!=0||previousLong!=0){
                                Location.distanceBetween(node.getLat(),node.getLon(),previousLat,previousLong,results);
                                totalDistance+=results[0];
                            }
                            previousLat=node.getLat();
                            previousLong=node.getLon();

                        }
                        if(line!=null){
                            line.remove();;
                        }
                        line=map.addPolyline(new PolylineOptions().addAll(points).color(Color.RED).width(5));

                        Toast.makeText(getApplicationContext(),totalDistance+"",Toast.LENGTH_SHORT).show();



                    }
                }
            }
        });
    }

    // Make sure we have sufficient permissions under Android 6.0 and later
    private void tryEnableLocation() {
        try {
            map.setMyLocationEnabled(true);
            locationEnabled = true;
        }
        catch (SecurityException e) {
            Log.e(TAG, "No permissions for positioning! (ProximiioService should be asking for them!)");
        }
    }

    // Set the position obtained from Proximiio to be our current position on the map
    private void setPosition(double lat, double lon, double accuracy) {
        currentLatitude=lat;
        currentLongitude=lon;
        if (map != null) {

            if (!locationEnabled) {
                tryEnableLocation();
            }

            // First time zoom to focus the map on the current location
            if (!zoomed) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 18));
                zoomed = true;
            }

            // Update our location on the map
            if (locationListener != null && locationEnabled) {
                Location location = new Location("Proximiio");
                location.setLatitude(lat);
                location.setLongitude(lon);
                location.setAccuracy((float)accuracy);
                locationListener.onLocationChanged(location);
            }
        }
    }
}
