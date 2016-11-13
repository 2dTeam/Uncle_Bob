package org.team2d.uncle_bob;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.team2d.uncle_bob.Database.DatabaseService;


public class UBIntentService extends IntentService {
    private static final String ACTION_LOAD_DB = "org.team2d.uncle_bob.action.LOAD_DB";
    public static final String DB_LOADED = "org.team2d.uncle_bob.action.DB_LOADED";


    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);

    public UBIntentService() {
        super("UBIntentService");
    }


    public static void startActionLoadDB(Context context) {
        Intent intent = new Intent(context, UBIntentService.class);
        intent.setAction(ACTION_LOAD_DB);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_DB.equals(action)) {
                handleActionLoadDB();
            }
        }
    }

    private void handleActionLoadDB() {
        DatabaseService.loadDB(this);
        Intent dbIntent = new Intent(DB_LOADED);
        broadcastManager.sendBroadcast(dbIntent);
    }
}
