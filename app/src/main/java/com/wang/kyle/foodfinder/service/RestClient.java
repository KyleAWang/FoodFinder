package com.wang.kyle.foodfinder.service;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * most of codes from there:http://lukencode.com/ , thanks luke! e.g: RestClient
 * client = new RestClient(LOGIN_URL); client.AddParam("accountType", "GOOGLE");
 * client.AddParam("source", "tboda-widgalytics-0.1"); client.AddParam("Email",
 * _username); client.AddParam("Passwd", _password); client.AddParam("service",
 * "analytics"); client.AddHeader("GData-Version", "2");
 * 
 * try { client.Execute(RequestMethod.POST); } catch (Exception e) {
 * e.printStackTrace(); }
 * 
 * String response = client.getResponse();
 * 
 */
public class RestClient {
    //constant
	public static final int TIME_OUT_REQUEST = 5000;
	public static final int TIME_OUT_CONNECTION = 10000;
	private static final String ENCODE_UTF8 = "UTF-8";
	
	private final String TAG = getClass().getSimpleName();

	private List<NameValuePair> params;
	private List<NameValuePair> headers;

	private int responseCode;
	private String message;
	private String response;
	//property for tuning purpose in the future
	private int parTimeOutRequest = TIME_OUT_REQUEST;
	private int parTimeOutConnection = TIME_OUT_CONNECTION;
	private String parEncoding = ENCODE_UTF8;
	
	public void setParTimeOutRequest(int parTimeOutRequest) {
		this.parTimeOutRequest = parTimeOutRequest;
	}

	public void setParTimeOutConnection(int parTimeOutConnection) {
		this.parTimeOutConnection = parTimeOutConnection;
	}

	public void setParEncoding(String parEncoding) {
		this.parEncoding = parEncoding;
	}

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public RestClient() {
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	public void addParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void addHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}
	
	public void setHeaders(List<NameValuePair> hs) {
        headers = hs;
    }

	public String doGet(String url) throws Exception {
		// add parameters
		String combinedParams = "";
		url = url.replace(" ", "");
		if (!params.isEmpty()) {
			combinedParams += "?";
			for (NameValuePair p : params) {
				String paramString;
				try {
					if (p.getValue() == null) {
						Log.d(TAG, " request parameter " + p.getName()
								+ " is null ");
						continue;
					}
					paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), parEncoding);
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, e.getMessage());
				}

			}
		}
		String realUrl = url + combinedParams;
		Log.d(TAG, "Rest GET url: " + realUrl);
		HttpGet request = new HttpGet(realUrl);
		// request.setHeader("content-type", ENCODE_UTF8);

		// add headers
		for (NameValuePair h : headers) {
			request.addHeader(h.getName(), h.getValue());
		}
		String result = executeRequest(request, url);
		return result;
	}

	public String doPost(String url, String jsonStr) throws Exception {
		url = url.replace(" ", "");
		return postOrPut(url, jsonStr, "POST");
	}

	public String doPut(String url, String jsonStr) throws Exception {
		url = url.replace(" ", "");
		return postOrPut(url, jsonStr, "PUT");
	}

	private String postOrPut(String url, String jsonStr, String postPutFlag)
			throws Exception {
		url = url.replace(" ", "");
		String result = null;
		HttpEntityEnclosingRequestBase request = null;
		if ("PUT".equalsIgnoreCase(postPutFlag)) {
			request = new HttpPut(url.trim());
			request.setHeader("content-type", "application/json;charset=UTF-8");
		} else { // should be POST
			request = new HttpPost(url.trim());
			request.setHeader("content-type", "application/json;charset=UTF-8");
		}
		// add headers
		for (NameValuePair h : headers) {
			request.addHeader(h.getName(), h.getValue());
		}
		if (jsonStr != null) {
		    
			Log.d(TAG, "request json:" + jsonStr);
			StringEntity sEntity;
			try {
				sEntity = new StringEntity(jsonStr, parEncoding);
				request.setEntity(sEntity);
				Log.d(TAG, "Rest " + postPutFlag + " url: " + url);
				Log.d(TAG, "Rest " + postPutFlag + " data: " + jsonStr);
				result = executeRequest(request, url.trim());
				
				Log.d(TAG, "Result  " + result);

			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, e.getMessage());
			}
		} else {
			Log.d(TAG, "Rest " + postPutFlag + " url: " + url);
			result = executeRequest(request, url.trim());
		}
		return result;
	}

	private String executeRequest(HttpUriRequest request, String url)
			throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();

		client.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 10000);//10000
		client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				12000);//5000

		HttpResponse httpResponse;
		httpResponse = client.execute(request);
		responseCode = httpResponse.getStatusLine().getStatusCode();
		message = httpResponse.getStatusLine().getReasonPhrase();

		HttpEntity entity = httpResponse.getEntity();
		if (entity != null) {
			response = EntityUtils.toString(entity, parEncoding);
			response = URLDecoder.decode(response, parEncoding);
		}
		Log.d(TAG, "result data : " + response);
		return response;
	}
}
