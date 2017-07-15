package quartzo.com.dublinbusalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by victoraldir on 15/07/2017.
 */

public class MarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int RC_PICK_PLACE = 1;
    public static final String EXTRA_BUS_STATION = "extraBusStation";

    private GoogleMap mMap;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        mContext = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    //TODO fetch data from stations.json
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng parnellStopBus = new LatLng(53.352241,-6.263695);

        googleMap.addMarker(new MarkerOptions().position(parnellStopBus)
                .title("Parnell Square, Parnell Street").snippet(String.format(getString(R.string.station),"235")));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(parnellStopBus));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {

                Toast.makeText(mContext,arg0.getSnippet(),Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();

                bundle.putString(EXTRA_BUS_STATION,arg0.getSnippet());

                Intent data = new Intent();
                data.putExtras(bundle);

                setResult(RC_PICK_PLACE,data);
                finish();
            }
        });

    }


}
