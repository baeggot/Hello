package com.baeflower.hello.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitude;
    private String mLongtitude;
    private boolean mConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mLongitudeText = (TextView) findViewById(R.id.tv_current_location_longitude);
        mLatitudeText = (TextView) findViewById(R.id.tv_current_location_latitude);


        buildGoogleApiClient();
        mGoogleApiClient.connect();

        findViewById(R.id.btn_get_current_location).setOnClickListener(this);
    }

    /*
        Google play service에 연결하는 객체를 생성
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private TextView mLongitudeText;
    private TextView mLatitudeText;

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getApplicationContext(), "onConnected", Toast.LENGTH_SHORT).show();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = String.valueOf(mLastLocation.getLatitude());
            mLongtitude = String.valueOf(mLastLocation.getLongitude());
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "onConnectionSuspended, " + i, Toast.LENGTH_SHORT).show();

        mConnected = true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "onConnectionFailed, " + connectionResult, Toast.LENGTH_SHORT).show();

        mConnected = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_current_location:
                mLatitudeText.setText(mLatitude);
                mLongitudeText.setText(mLongtitude);
                break;

        }
    }

    /*
        플레이스토어 연결 됐는지 안됐는지 확인
     */
    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }


}
