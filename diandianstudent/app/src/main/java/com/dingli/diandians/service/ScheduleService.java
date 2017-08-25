package com.dingli.diandians.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScheduleService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent intent = new Intent(this, DaemonService.class);
        startService(intent);
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
