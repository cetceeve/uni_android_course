package de.ur.mi.android.tasks.eggtimer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import de.mi.eggtimer.R;
import de.ur.mi.android.tasks.eggtimer.debug.DebugHelper;
import de.ur.mi.android.tasks.eggtimer.listener.OnEggTimerStatusChangedListener;

public class EggTimerService extends Service implements OnEggTimerStatusChangedListener {

    private static final int myNotificationID = 12345;
    private OnEggTimerStatusChangedListener onEggTimerStatusChangedListener;
    private IBinder iBinder;
    private EggTimer myEggTimer;

    @Override
    public void onCreate() {
        DebugHelper.logDebugMessage("onCreate Service");
        iBinder = new LocalBinder();
    }

    public void setOnEggTimerStatusChangedListener(OnEggTimerStatusChangedListener onEggTimerStatusChangedListener) {
        this.onEggTimerStatusChangedListener = onEggTimerStatusChangedListener;
    }

    public void stopTimer() {
        // anhalten des Timers mit einer geeigneten Methode des EggTimer-Objekts,
        // achten sie dabei auf möglicherweise auftretende NullPointer!
        myEggTimer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DebugHelper.logDebugMessage("onStartCommand");

        // Erstellen eines neuen EggTimers in der angelegten Klassenvariable mit den Extras des Intents
        @NonNull
        Bundle bundle = intent.getExtras();

        int eggSize = bundle.getInt(getString(R.string.eggSize));
        int doneness = bundle.getInt(getString(R.string.doneness));

        myEggTimer = new EggTimer(onEggTimerStatusChangedListener, eggSize, doneness);
        // Starten des Timers
        myEggTimer.start();

        return START_NOT_STICKY;
    }


    private void sendNotification() {
        DebugHelper.logDebugMessage("notification will be created");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.emo_im_foot_in_mouth)
                        .setContentTitle(getString(R.string.dialogTitle))
                        .setContentText(getString(R.string.notificationText));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, EggTimerActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(EggTimerActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(myNotificationID, mBuilder.build());

        // Vibration starten
    }


    class LocalBinder extends Binder {

        EggTimerService getBinder() {
            return EggTimerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return iBinder;
    }

    @Override
    public void onUpdateCountdownView(String toUpdate) {
        if (onEggTimerStatusChangedListener != null) {
            onEggTimerStatusChangedListener.onUpdateCountdownView(toUpdate);
        }
    }

    @Override
    public void onResetCountdownView(int stringID) {
        System.out.println("onUpdateCountdownView Service");
        if (onEggTimerStatusChangedListener != null) {
            onEggTimerStatusChangedListener.onResetCountdownView(stringID);
        }
    }

    @Override
    public void onEggTimerFinished() {
        System.out.println("OnEggTimerFinished Service");
        if (onEggTimerStatusChangedListener != null && ActivityVisibilityState.getIsVisible()) {
            onEggTimerStatusChangedListener.onEggTimerFinished();
        }
        if (!ActivityVisibilityState.getIsVisible()) {
            sendNotification();
        }
    }
}
