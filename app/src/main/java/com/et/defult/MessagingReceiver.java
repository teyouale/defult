package com.et.defult;

import android.content.Context;
import android.content.Intent;
import android.provider.Telephony.Sms.Intents;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;



public class MessagingReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent == null ? null : intent.getAction();
        Log.d("TAG", "onReceive: ");

        // If on KitKat+ and default messaging app then look for new deliver actions actions.
        if (Utils.hasKitKat() && Utils.isDefaultSmsApp(context)) {
            if (Intents.SMS_DELIVER_ACTION.equals(action)) {
                handleIncomingSms(context, intent);
            } else if (Intents.WAP_PUSH_DELIVER_ACTION.equals(action)) {
                handleIncomingMms(context, intent);
            }
        } else { // Otherwise look for old pre-KitKat actions
            if (Intents.SMS_RECEIVED_ACTION.equals(action)) {
                handleIncomingSms(context, intent);
            } else if (Intents.WAP_PUSH_RECEIVED_ACTION.equals(action)) {
                handleIncomingMms(context, intent);
            }
        }
    }

    private void handleIncomingSms(Context context, Intent intent) {
        // TODO: Handle SMS here
        // As an example, we'll start a wakeful service to handle the
        //abortBroadcast();
        Log.d("TAG", "handleIncomingSms: ");
        intent.setAction(MessagingService.ACTION_MY_RECEIVE_SMS);
        intent.setClass(context, MessagingService.class);
        startWakefulService(context, intent);
    }

    private void handleIncomingMms(Context context, Intent intent) {
        // TODO: Handle MMS here
        // As an example, we'll start a wakeful service to handle the MMS
        intent.setAction(MessagingService.ACTION_MY_RECEIVE_MMS);
        intent.setClass(context, MessagingService.class);
        startWakefulService(context, intent);
    }
}
