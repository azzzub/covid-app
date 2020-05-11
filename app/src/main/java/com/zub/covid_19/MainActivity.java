package com.zub.covid_19;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String linkPerProvinsi = "https://services5.arcgis.com/VS6HdKS0VfIhv8Ct/arcgis/rest/services/COVID19_Indonesia_per_Provinsi/FeatureServer/0/query?f=json&where=(Kasus_Posi%20%3C%3E%200)%20AND%20(Provinsi%20%3C%3E%20%27Indonesia%27)&returnGeometry=false&spatialRel=esriSpatialRelIntersects&outFields=*&orderByFields=Kasus_Posi%20desc&resultOffset=0&resultRecordCount=34&resultType=standard&cacheHint=true";
    private String linkTest = "http://echo.jsontest.com/insert-key-here/insert-value-here/key/value";
    private JSONObject responses;
    private boolean isFinishFetch = false;

    private void dataResponse(String url){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        isFinishFetch = true;
                        responses = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error Volley: ", error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataResponse(linkPerProvinsi);
        if(isFinishFetch){
            Log.d("Resp", String.valueOf(responses));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
