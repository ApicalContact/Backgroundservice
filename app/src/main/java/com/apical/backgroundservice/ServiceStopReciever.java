package com.apical.backgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceStopReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, FieldViewService.class);
        context.startService(background);
    }

}