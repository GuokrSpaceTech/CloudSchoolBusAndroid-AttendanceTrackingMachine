package com.cytx.timecardmobile.lbs;

import android.util.Log;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;

public class AddGeofenceListener implements OnAddBDGeofencesResultListener {
	public GeofenceClient mGeofenceClient;
	
	public AddGeofenceListener(GeofenceClient geofenceClient)
	{
		mGeofenceClient = geofenceClient;
	}
	
	@Override
	public void onAddBDGeofencesResult(int statusCode, String geofenceRequestId) {
	    if (statusCode == BDLocationStatusCodes.SUCCESS) {
		    Log.i("","Geofence add successfully");
		    if(mGeofenceClient!=null)
		    {
		    	mGeofenceClient.start();
		    }
	    }
    }
}
