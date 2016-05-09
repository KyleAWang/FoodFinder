package com.wang.kyle.foodfinder.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.wang.kyle.foodfinder.module.AutoComRequest;
import com.wang.kyle.foodfinder.module.AutoComResponse;
import com.wang.kyle.foodfinder.module.PlaceDetailRequest;
import com.wang.kyle.foodfinder.module.PlaceDetailResponse;
import com.wang.kyle.foodfinder.module.PlaceSearchRequest;
import com.wang.kyle.foodfinder.module.PlaceSearchResponse;
import com.wang.kyle.foodfinder.webservice.ServiceConstants;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class HttpIntentService extends IntentService {
    public static final String ACTION_SEARCH_PLACES = "com.ibm.kyle.workshop.service.action.SEARCH_PLACES";
    public static final String ACTION_SEARCH_DETAIL = "com.ibm.kyle.workshop.service.action.SEARCH_DETAIL";
    public final static int RESULTCODE_SUCCESS = 1;
    public final static int RESULTCODE_FAIL = 0;
    private final static String RESULT_STATUS_SUCCESS = "OK";


    public HttpIntentService() {

        super("HttpIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("receiver");

            if (ACTION_SEARCH_PLACES.equals(action)) {
                PlaceSearchRequest request = (PlaceSearchRequest) intent.getSerializableExtra("request");
                PlaceSearchResponse response = doSearchPlaces(request);

                Bundle bundle = new Bundle();
                bundle.putSerializable("response", response);
                if (response != null && response.getStatus().equals(RESULT_STATUS_SUCCESS)) {
                    resultReceiver.send(RESULTCODE_SUCCESS, bundle);
                } else {
                    resultReceiver.send(RESULTCODE_FAIL, bundle);
                }
            } else if (ACTION_SEARCH_DETAIL.equals(action)) {
                PlaceDetailRequest request = (PlaceDetailRequest) intent.getSerializableExtra("request");
                PlaceDetailResponse response = doSearchDetail(request);

                Bundle bundle = new Bundle();
                bundle.putSerializable("response", response);
                if (response != null && response.getStatus().equals(RESULT_STATUS_SUCCESS)) {
                    resultReceiver.send(RESULTCODE_SUCCESS, bundle);
                } else {
                    resultReceiver.send(RESULTCODE_FAIL, bundle);
                }
            }


        }
    }


    private AutoComResponse doAutoCom(AutoComRequest request) {
        AutoComResponse response = null;
        String url = ServiceConstants.SERVICE_AUTOCOM;
        String requestJson = JsonUtil.javaToJson(request, AutoComRequest.class);
        RestClient restClient = new RestClient();
        try {
            String jsonStr = restClient.doPost(url, requestJson);
            response = JsonUtil.jsonObjToJava(jsonStr, AutoComResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

    private PlaceSearchResponse doSearchPlaces(PlaceSearchRequest request) {
        PlaceSearchResponse response = null;
        String url = ServiceConstants.SERVICE_SEARCH_PLACES;
        StringBuffer sb = new StringBuffer(url);
        sb.append("&location=");
        sb.append(request.getLatitude());
        sb.append(",");
        sb.append(request.getLongitude());
        sb.append("&radius=");
        sb.append(request.getRadius());
        if (request.getKeyword() != null && !request.getKeyword().equalsIgnoreCase("restaurant")) {
            sb.append("&keyword=");
            sb.append(request.getKeyword());
        }
        sb.append("&language=");
        sb.append(request.getLanguage());
        sb.append("&type=");
        sb.append(request.getType());

        RestClient restClient = new RestClient();
        try {
            String jsonStr = restClient.doPost(sb.toString(), null);
            response = JsonUtil.jsonObjToJava(jsonStr, PlaceSearchResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private PlaceDetailResponse doSearchDetail(PlaceDetailRequest request) {
        PlaceDetailResponse response = null;
        String url = ServiceConstants.SERVICE_SEARCH_DETAIL;
        StringBuffer sb = new StringBuffer(url);
        sb.append("&placeid=");
        sb.append(request.getPlaceid());

        RestClient restClient = new RestClient();
        try {
            String jsonStr = restClient.doPost(sb.toString(), null);
            response = JsonUtil.jsonObjToJava(jsonStr, PlaceDetailResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
