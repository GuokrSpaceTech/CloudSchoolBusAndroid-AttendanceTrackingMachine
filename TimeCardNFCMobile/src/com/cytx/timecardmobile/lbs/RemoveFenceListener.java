package com.cytx.timecardmobile.lbs;

import android.util.Log;

import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;

public class RemoveFenceListener implements OnRemoveBDGeofencesResultListener {
	@Override
	public void onRemoveBDGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
	    if (statusCode == BDLocationStatusCodes.SUCCESS) {
	        //围栏删除成功
	    	Log.i("","Remove Fence success");
	    }
    }
}