package com.gae.scaffolder.plugin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.util.Map;
import java.util.HashMap;
import android.graphics.BitmapFactory;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Felipe Echanique on 08/06/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMPlugin";
	 Bitmap bigPicture;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

		//추가한것 - 메세지와 이미지를 인수로 지정
        sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("imgurllink"));
        //이미지 링크가 제대로 들어 있나 콘솔에 찍어 봄
        Log.d(TAG, "imgurl: " + remoteMessage.getData().get("imgurllink"));




        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
		/*
        Log.d(TAG, "==> MyFirebaseMessagingService onMessageReceived");
		
		if( remoteMessage.getNotification() != null){
			Log.d(TAG, "\tNotification Title: " + remoteMessage.getNotification().getTitle());
			Log.d(TAG, "\tNotification Message: " + remoteMessage.getNotification().getBody());
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("wasTapped", false);
		for (String key : remoteMessage.getData().keySet()) {
                Object value = remoteMessage.getData().get(key);
                Log.d(TAG, "\tKey: " + key + " Value: " + value);
				data.put(key, value);
        }
		
		Log.d(TAG, "\tNotification Data: " + data.toString());
        FCMPlugin.sendPushPayload( data );
		*/

        //sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
	 //private void sendNotification(String title, String messageBody, Map<String, Object> data) {
		private void sendNotification(String messageBody, String myimgurl) {
 
    Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //이미지 온라인 링크를 가져와 비트맵으로 바꾼다.
        try {
            URL url = new URL(myimgurl);
            bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setContentTitle("FCM Push Test")
                .setContentText("알림탭을 아래로 천천히 드래그 하세요.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)

//                //BigTextStyle
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .setBigContentTitle("FCM Push Big Text")
//                        .bigText(messageBody))

                //이미지를 보내는 스타일 사용하기
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bigPicture)
                        .setBigContentTitle("FCM Push Big Text Title")
                        .setSummaryText(messageBody))

                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
 


	 }


    private void _sendNotification(String title, String messageBody, Map<String, Object> data) {

        Intent intent = new Intent(this, FCMPluginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		for (String key : data.keySet()) {
			intent.putExtra(key, data.get(key).toString());
		}
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
