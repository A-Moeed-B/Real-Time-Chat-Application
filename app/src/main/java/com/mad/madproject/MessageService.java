package com.mad.madproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MessageService extends Service {
    private Looper serviceLooper;
    private static final String TAG="Message Service";
    private Notification  notification;
    private PendingIntent pendingIntent;
    private final IBinder binder=new LocalBinder();
    private String message="";
    public class  LocalBinder extends Binder{
        MessageService getService()
        {
            return MessageService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public void onCreate()
    {
        super.onCreate();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent,int flags,int startID)
    {
        Intent notificationIntent=new Intent(this,MainActivity.class);
        pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        message=intent.getStringExtra("MESSAGE");
        updateForegroundNotification("Message: "+intent.getStringExtra("MESSAGE"));
        return START_STICKY;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateForegroundNotification(String string)
    {
        String NOTIFICATION_CHANNEL_ID="foregroundService";
        String channelName="Message Background Service";
        NotificationChannel channel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,channelName, NotificationManager.IMPORTANCE_LOW);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{0});
        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager!=null;

        manager.createNotificationChannel(channel);
        notification=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Chat Message!: ")
                .setContentText(string)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        Vibrator vibrate=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrate.vibrate(500);
        startForeground(1234,notification);
    }
    public String getMessage()
    {
        return message;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
