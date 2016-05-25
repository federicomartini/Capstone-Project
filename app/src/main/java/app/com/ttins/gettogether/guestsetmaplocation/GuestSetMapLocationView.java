package app.com.ttins.gettogether.guestsetmaplocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.GuestActivity;

public class GuestSetMapLocationView extends SupportMapFragment implements OnMapReadyCallback {

    public static final String LOG_TAG = GuestSetMapLocationView.class.getSimpleName();

    public static final String FRAG_MAP_ADDRESS_ARG = "FRAG_MAP_ADDRESS_ARG";

    MapView gMapView;
    GoogleMap gMap = null;
    String address = null;
    Callback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GuestActivity) {
            callback = (Callback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    void setUpMapIfNeeded() {
        if (gMap == null) {
            getMapAsync(this);
        }
    }

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");

        Bundle args = getArguments();

        if (args.containsKey(FRAG_MAP_ADDRESS_ARG)) {
            address = args.getString(FRAG_MAP_ADDRESS_ARG);
        } else {
            address = null;
        }

        View view = inflater.inflate(R.layout.map_layout, container, false);
        gMapView = (MapView) view.findViewById(R.id.map);
        gMapView.getMapAsync(this);

        return view;
    }
*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(LOG_TAG, "onMapReady");
        LatLng latlng;
        gMap = googleMap;
        String guestAddress;

        if (address == null || address.isEmpty()) {
            guestAddress = getResources().getString(R.string.default_map_address);
        } else {
            guestAddress = address;
        }

        gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        latlng = getLocationFromAddress(getActivity(), guestAddress);
        gMap.addMarker(new MarkerOptions().position(latlng).title(guestAddress));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {
        Log.d(LOG_TAG, "getLocationFromAddress");
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
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
    public void onPause() {
        Log.d(LOG_TAG, "onPause");
        super.onPause();
        callback.onMapViewGone();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setAddress(String address) {
        Log.d(LOG_TAG, "Address set from activity: " + address);
        this.address = address;
    }

    public interface Callback {
        void onMapViewGone();
    }


}
