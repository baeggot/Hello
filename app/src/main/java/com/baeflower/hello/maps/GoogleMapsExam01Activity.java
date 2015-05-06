
package com.baeflower.hello.maps;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.baeflower.hello.R;
import com.google.android.gms.maps.MapFragment;

public class GoogleMapsExam01Activity extends ActionBarActivity {

    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps_exam01);

//        mMapFragment = MapFragment.newInstance();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.fg_google_maps_practice01, mMapFragment);
//        fragmentTransaction.commit();
    }

}
