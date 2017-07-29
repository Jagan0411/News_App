package com.example.harish.news_app;


import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import com.example.harish.news_app.utilities.NetworkUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by harish on 28-07-2017.
 */

public class RefreshService extends JobService {

    AsyncTaskLoader asyncTaskLoader;


    @Override
    public boolean onStartJob(JobParameters params) {

        asyncTaskLoader = new AsyncTaskLoader(this) {

            @Override
            protected void onStartLoading() {
                Toast.makeText(RefreshService.this, "Refreshed", Toast.LENGTH_SHORT).show();
                forceLoad();
            }

            @Override
            public Object loadInBackground() {
                NetworkUtils.reloadDB(RefreshService.this);
                return null;
            }

        };

        asyncTaskLoader.startLoading();
        jobFinished(params, false); //End of job and setting boolean to false for completed jobs.

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        if (asyncTaskLoader != null)
        {
            asyncTaskLoader.cancelLoad();//Cancelling the AsyncTask on job completion
        }

        return true;
    }
}
