package ir.iraddress.www.services;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.google.android.gms.wearable.DataMap.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification( remoteMessage);

            Intent broadcast = new Intent();
            broadcast.setAction("OPEN_NEW_ACTIVITY");
            broadcast.putExtra("notification_body", remoteMessage.getNotification().getBody());
            broadcast.putExtra("notification_title", remoteMessage.getNotification().getTitle());
            broadcast.putExtra("fcm_type", remoteMessage.getData().get("type"));

            switch(remoteMessage.getData().get("type")){
                case "request_follow":
                    broadcast.putExtra("accept", remoteMessage.getData().get("accept"));
                    broadcast.putExtra("reject", remoteMessage.getData().get("reject"));
                    break;
            }

            sendBroadcast(broadcast);

        }

    }
}
