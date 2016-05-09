package com.wang.kyle.foodfinder.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void showToast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void back(View v) {
        this.getActivity().finish();
    }

//    public void handleError(ErrorResponse response) {
//
//    }


    protected String getUDID() {
        String PREFS_FILE = "gank_device_id.xml";
        String PREFS_DEVICE_ID = "gank_device_id";
        String uuid = null;

        synchronized (BaseFragment.class) {
            if (uuid == null) {
                final SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFS_FILE, 0);
                final String id = prefs.getString(PREFS_DEVICE_ID, null);

                if (id != null) {
                    // Use the ids previously computed and stored in the
                    // prefs file
                    uuid = id;
                } else {

                    final String androidId = Secure.getString(this.getActivity().getContentResolver(),
                            Secure.ANDROID_ID);

                    // Use the Android ID unless it's broken, in which case
                    // fallback on deviceId,
                    // unless it's not available, then fallback on a random
                    // number which we store
                    // to a prefs file
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                        } else {
                            final String deviceId = ((TelephonyManager) this
                                    .getActivity().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                            uuid = deviceId != null ? UUID.nameUUIDFromBytes(
                                    deviceId.getBytes("utf8")).toString() : UUID.randomUUID()
                                    .toString();
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    // Write the value out to the prefs file
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                }
            }
        }
        return uuid;
    }
    
    protected String getIMEI() {
//        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext()
//                .getSystemService(Context.TELEPHONY_SERVICE);
//
//        String imei = telephonyManager.getDeviceId();
        
        return "";
    }
    
}
