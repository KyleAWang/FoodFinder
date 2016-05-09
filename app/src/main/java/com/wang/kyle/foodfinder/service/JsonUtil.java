package com.wang.kyle.foodfinder.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {

	public static <T> T jsonObjToJava(String jsonStr, Class<T> clazz) {
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		T retObj;
		try {
			retObj = gson.fromJson(jsonStr, clazz);
			return retObj;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public static <T> List<T> jsonArrayToJava(String jsonStr, Type type) {
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			List<T> retList = gson.fromJson(jsonStr, type);
			return retList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String javaToJson(Object obj, Type type) {
		try {
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			String jsonStr = gson.toJson(obj, type);
			return jsonStr;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
