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

    @Override
    public void onReceive(Context context, Intent intent)
    {
        DebugClass.displayCurrentStack("Boot Complete, service will be started");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Intent myintent = new Intent(context, TimeCardService.class);
            context.startService(myintent);
        }
    }
}