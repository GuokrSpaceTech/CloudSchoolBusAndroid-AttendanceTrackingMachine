package com.cytx.timecard.receiver;

/**
 * Created by Saigon on 2015/1/20.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cytx.timecard.service.TimeCardService;
import com.cytx.timecard.utility.DebugClass;

public class BootBroadcastReceiver extends BroadcastReceiver
{
    private String other;

    @Override
    public void onReceive(Context arg0, Intent arg1)
    {
        DebugClass.displayCurrentStack("Boot Complete, service will be started");
        if (arg1.getAction().equals(other))
        {
            Intent myintent = new Intent(arg0, TimeCardService.class);
            // TODO modified if any parameters needed to be passed to the service
            //myintent.setAction("org.allin.android.musicService");
            arg0.startService(myintent);
        }
    }
}