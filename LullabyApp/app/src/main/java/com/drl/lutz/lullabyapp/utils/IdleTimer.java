package com.drl.lutz.lullabyapp.utils;

import android.util.Log;

/*
 * Taken from http://stackoverflow.com/questions/4075180/application-idle-time and modified
 */
public class IdleTimer extends Thread {


    public interface IdleTimerEventListener {
        public void onIdleTooLong();
    }

    private IdleTimerEventListener listener = null;

    private static final String TAG= IdleTimer.class.getName();
    private long lastUsed;
    private long period;
    private boolean stop;



    public IdleTimer(long period,IdleTimerEventListener listener)
    {
        this.period = period;
        this.listener = listener;
        stop = false;
    }

    public void run()
    {
        long idle=0;
        this.touch();
        do
        {
            idle=System.currentTimeMillis()-lastUsed;
            Log.d(TAG, "Application is idle for " + idle + " ms");
            try
            {
                Thread.sleep(2000); //check every 2 seconds
            }
            catch (InterruptedException e)
            {
                Log.d(TAG, "Waiter interrupted!");
                stop = true;
            }
            if(idle > period)
            {
                if (listener != null)
                    listener.onIdleTooLong();
                idle=0;
                stop = true;
            }
        }
        while(!stop);
        Log.d(TAG, "Finishing Waiter thread");
    }

    public synchronized void touch()
    {
        lastUsed=System.currentTimeMillis();
    }

    public synchronized void forceInterrupt()
    {
        this.interrupt();
    }

}