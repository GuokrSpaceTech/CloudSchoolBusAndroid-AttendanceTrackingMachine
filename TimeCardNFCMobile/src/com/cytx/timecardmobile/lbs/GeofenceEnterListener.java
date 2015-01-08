package com.cytx.timecardmobile.lbs;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;

public class GeofenceEnterListener implements OnGeofenceTriggerListener{

	@Override
	public void onGeofenceTrigger(String arg0) {
		// TODO Auto-generated method stub
		
//		String temp = logMsg.getText().toString();
//		temp+="\n进入围栏"+ arg0;
		//Message msg = MessageHandler.obtainMessage();
		//Bundle bundle = new Bundle();
		//bundle.putString("msg", temp);
		//msg.setData(bundle);
		//MessageHandler.sendMessage(msg);
		Log.i("","Entered the fence");
	}
	
	@Override
	public void onGeofenceExit(String arg0) {
		// TODO Auto-generated method stub
//		//String temp = logMsg.getText().toString();
//		temp+="\n退出围栏"+ arg0;
		//Message msg = MessageHandler.obtainMessage();
		//Bundle bundle = new Bundle();
		Log.i("","Exit the fence");
		//bundle.putString("msg", temp);
		//msg.setData(bundle);
		//MessageHandler.sendMessage(msg);
	}
}
