package org.team2d.uncle_bob.AsyncLoad;

import android.content.Context;
import android.os.AsyncTask;

import org.team2d.uncle_bob.Database.DatabaseService;

/**
 * Created by nikolaev on 13.11.16.
 */
@Deprecated
public class LoadDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    public  LoadDatabaseAsyncTask(Context cnt){
        mContext = cnt;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DatabaseService.loadDB(mContext);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    }
}
