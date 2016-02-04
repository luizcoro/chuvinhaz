package com.example.luiz.chuvinhaz.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import com.example.luiz.chuvinhaz.R;
import com.example.luiz.chuvinhaz.models.User;
import com.example.luiz.chuvinhaz.utils.Constants;
import com.example.luiz.chuvinhaz.views.adapters.GoogleMapsAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by luiz on 10/25/15.
 */
public class MyMapFragment extends Fragment {

    public interface MyMapFragmentListener
    {
        public void on_marker_click(User user_clicked);
    }

    private String me;

    private MyMapFragmentListener _listener;

    private GoogleMap _map;

    private HashMap<String, Marker> _user_to_marker = new HashMap<>();

    private SearchView _search_view;
    private ImageButton _go_my_location;

    public MyMapFragment() {
        // Required empty public constructor
    }

    public void setMe(User me) {
        this.me = me.getUUID();

        _user_to_marker.put(me.getUUID(),
                _map.addMarker(new MarkerOptions()
                        .title(me.getName())
                        .snippet(buildSnippet(me))
                        .position(me.getLatLng())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
    }

    public void attachActivity(Activity activity)
    {
        _listener = (MyMapFragmentListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        _search_view =  (SearchView) v.findViewById(R.id.searchView);

        _search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new FetchLocation().execute(query);
                _search_view.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        _search_view.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _go_my_location.setVisibility(View.GONE);
            }
        });

        _search_view.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    _search_view.setIconified(true);
                    _go_my_location.setVisibility(View.VISIBLE);
                }
            }
        });

        _go_my_location = (ImageButton) v.findViewById(R.id.go_my_location);

        _go_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerMapToLocation(_user_to_marker.get(me).getPosition());
            }
        });

        _map = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        setMaps();

        // Inflate the layout for this fragment
        return v;
    }

    public void setMaps() {
        _map.getUiSettings().setMapToolbarEnabled(false);
        _map.getUiSettings().setZoomControlsEnabled(true);
        _map.getUiSettings().setRotateGesturesEnabled(false);
        _map.getUiSettings().setCompassEnabled(false);

        _map.setInfoWindowAdapter(new GoogleMapsAdapter(getContext()));
//        _map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                if (_marker_id_to_user.containsKey(marker.getId())) {
//                    User u = _marker_id_to_user.get(marker.getId());
//
//                    if (u != null && !u.getId().equals(mId))
//                        _listener.on_marker_click(u);
//
//                }
//
//                return false;
//            }
//        });
    }

    public void setCustomIconsEnable(boolean enable)
    {
        if(enable)
        {
            _go_my_location.setVisibility(View.VISIBLE);
            _search_view.setVisibility(View.VISIBLE);
        }
        else {
            _go_my_location.setVisibility(View.GONE);
            _search_view.setVisibility(View.GONE);
        }
    }

    public void addMarker(String uuid)
    {
        _user_to_marker.put(uuid, null);
    }


    public void updateMarker(User user, boolean collecting) {
        String uuid = user.getUUID();
        if(_user_to_marker.containsKey(uuid)) {

            Marker marker = _user_to_marker.get(uuid);

            if(marker == null)
            {
                _user_to_marker.put(uuid,
                    _map.addMarker(new MarkerOptions()
                        .title(user.getName())
                        .snippet(buildSnippet(user))
                        .position(user.getLatLng())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))));
            }
            else
            {
                marker.setTitle(user.getName());
                marker.setSnippet(buildSnippet(user));

                animateMarker(marker, user.getLatLng(), user.isHideOnMap());
            }
        }
    }

    public void removeMarker(String uuid)
    {
        Marker tmp = _user_to_marker.remove(uuid);
        if(tmp != null) {
            tmp.remove();
        }
    }

    public String buildSnippet(User user)
    {
        String rain_level = Constants.getWeatherString(user.getUser_rain_level());
        String indoor_or_outdoor = Constants.getLocationString(user.getUser_indoor_or_outdoor());
        String day_or_night = Constants.getDayOrNightString(user.getUser_day_or_night());
        String user_temp = user.getUser_temp() + "°C";
        String user_humidity = Constants.getHumidityString(user.getUser_humidity_level());
        String user_wind = Constants.getWindString(user.getUser_wind_level());

        String user_air = Constants.getAirString(user.getUser_air());

        StringBuilder builder = new StringBuilder();

        builder.append(getString(R.string.maps_user_location)).append(indoor_or_outdoor).append("\n").append(getString(R.string.maps_user_period)).append(day_or_night).append("\n");

        if(!indoor_or_outdoor.equals(getString(R.string.options_location_outdoors)))
            builder.append(getString(R.string.maps_user_air)).append(user_air).append("\n");

        builder.append(getString(R.string.maps_user_temperature)).append(user_temp).append("\n").append(getString(R.string.maps_user_rain_level)).append(rain_level).append("\n").append(getString(R.string.maps_user_humidity_level)).append(user_humidity);

        if(!indoor_or_outdoor.equals(getString(R.string.options_location_indoors)))
            builder.append("\n").append(getString(R.string.maps_user_wind_level)).append(user_wind);

        String act = Constants.getActivityString(user.getAct());

        if(user.getAct() != -1)
            builder.append("\n").append(getString(R.string.maps_user_activity)).append(act).append(" (").append(user.getConfidence_act()).append("%)");

//            if(collecting) {
//                String a_rain_level = user.getAlgorithm_rain_level() == 0 ? "No rain" :
//                        user.getUser_rain_level() == 1 ? "Little rain" : "Much rain";
//
//                snippet += "\nAlgorithm rain level: " + a_rain_level + "\nAlgorithm rain volume: " + user.getAlgorithm_rain_volume();
//            }

        return builder.toString();
    }
    public void centerMapToLocation(LatLng location) {

        if (location != null) {
            _map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

        }
        else
        {
            Location loc = _map.getMyLocation();
            centerMapToLocation(new LatLng(loc.getLatitude(), loc.getLongitude()));
        }

    }
    private void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = _map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public class FetchLocation extends AsyncTask<String, Void, LatLng> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= ProgressDialog.show(getContext(),"" , "Searching...", true);
        }

        @Override
        protected LatLng doInBackground(String... queries) {

            Geocoder coder = new Geocoder(getContext());
            List<Address> address;
            LatLng p1 = null;

            String query = queries[0];

            try {
                address = coder.getFromLocationName(query, 5);
                if (address == null) {
                    return null;
                }

                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude() );

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            return p1;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            progressDialog.dismiss();
            centerMapToLocation(latLng);
        }

    }
}