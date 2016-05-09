package com.wang.kyle.foodfinder.receiver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by Kyle on 4/17/2016.
 */
@SuppressLint("ParcelCreator")
public class HttpServiceReceiver extends ResultReceiver {
    private Listener listener;
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public HttpServiceReceiver(Handler handler) {
        super(handler);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (listener != null)
            listener.onReceiveResult(resultCode, resultData);
    }

    public  interface Listener {
        void onReceiveResult(int resultCode, Bundle resultData);
    }



}
