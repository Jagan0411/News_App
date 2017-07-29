package com.example.harish.news_app;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by harish on 28-07-2017.
 */

public class JobDispatcher  {

    private static boolean bool;

    //Syncing for making the data concurrent
    synchronized public static void schedule( final Context context){

        if(bool) {
            return;
        }

        //Creating driver object
        Driver driver = new GooglePlayDriver(context);

        //Creating firebase's job dispatcher
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        //Job constraints set
        Job constraintRefreshJob = dispatcher.newJobBuilder()
                .setService(RefreshService.class)
                .setTag("news_refresh")
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0,45))
                .setReplaceCurrent(true)
                .build();


        dispatcher.schedule(constraintRefreshJob);
        bool = true; //setting it to true for scheduled jobs
    }

}
