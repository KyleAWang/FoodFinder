package com.wang.kyle.foodfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.wang.kyle.foodfinder.module.AutoComRequest;
import com.wang.kyle.foodfinder.module.AutoComResponse;
import com.wang.kyle.foodfinder.module.Prediction;
import com.wang.kyle.foodfinder.service.JsonUtil;
import com.wang.kyle.foodfinder.service.RestClient;
import com.wang.kyle.foodfinder.webservice.ServiceConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 5/4/2016.
 */
public class SuggestionAdapter extends ArrayAdapter<Prediction> {
    protected static final String TAG = "SuggestionAdapter";
    private List<Prediction> suggestions;

    public SuggestionAdapter(Context context, int resource) {
        super(context, resource);
        suggestions = new ArrayList<Prediction>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Prediction prediction = suggestions.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }
        TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
        textView.setText(prediction.getDescription());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                AutoComResponse response = null;
                if (constraint != null) {
                    response = autoComplete(constraint.toString());
                }
                suggestions.clear();
                if (response != null){
                    for(Prediction prediction : response.getPredictions()){
                        suggestions.add(prediction);
                    }
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }


            private AutoComResponse autoComplete(String input) {
                AutoComRequest request = new AutoComRequest();
                request.setInput(input);

                AutoComResponse response = doAutoCom(request);
                return response;


            }

            private AutoComResponse doAutoCom(AutoComRequest request) {
                AutoComResponse response = null;
                String url = ServiceConstants.SERVICE_AUTOCOM;
//                String requestJson = JsonUtil.javaToJson(request, AutoComRequest.class);
                StringBuffer urlS = new StringBuffer(url);
                urlS.append("&input=");
                urlS.append(request.getInput());
                urlS.append("&offset=");
                urlS.append(request.getOffset());
                RestClient restClient = new RestClient();
                try {
                    String jsonStr = restClient.doPost(urlS.toString(), null);
                    response = JsonUtil.jsonObjToJava(jsonStr, AutoComResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return response;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        };
        return filter;
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Prediction getItem(int index) {
        return suggestions.get(index);
    }


    private class MyFilter extends Filter  {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            AutoComResponse response = null;
            if (constraint != null) {
                response = autoComplete(constraint.toString());
            }
            suggestions.clear();
            for(Prediction prediction : response.getPredictions()){
                suggestions.add(prediction);
            }
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
        }


        private AutoComResponse autoComplete(String input) {
            AutoComRequest request = new AutoComRequest();
            request.setInput(input);

            AutoComResponse response = doAutoCom(request);
            return response;


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

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
