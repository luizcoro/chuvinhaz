package com.example.luiz.chuvinhaz.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.luiz.chuvinhaz.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by luiz on 6/26/15.
 */
public class GoogleMapsAdapter implements GoogleMap.InfoWindowAdapter{
    private Context _context;

    public GoogleMapsAdapter(Context _context) {
        this._context = _context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file info_window_layout
        View v = LayoutInflater.from(_context).inflate(R.layout.mark_view, null);

        ((TextView) v.findViewById(R.id.marker_title)).setText(marker.getTitle());
        ((TextView) v.findViewById(R.id.marker_snippet)).setText(marker.getSnippet());

        return v;
    }
}
