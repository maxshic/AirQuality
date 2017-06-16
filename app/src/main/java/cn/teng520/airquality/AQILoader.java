package cn.teng520.airquality;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Lenovo on 2017/6/16.
 */

public class AQILoader {
    public static final String BASE_URL =
            "http://www.pm25.in/api/querys/only_aqi.json?city=CITY_NAME&token=5j1znBVAsnSf5xQyNQyq";

    public AQI[] loader(String cityId) throws IOException, JSONException {
        String realURL = BASE_URL.replace("CITY_NAME",cityId);

        URLConnection conn = new URL(realURL).openConnection();
        InputStream is = conn.getInputStream();
        String jsonText = new TextReader().readText(is, "UTF-8");
        Log.e("hello",jsonText);
        AQI[] aqis = parse(jsonText);
        return aqis;
    }

    private AQI[] parse(String jsonText) throws JSONException {
        JSONArray arr = new JSONArray(jsonText);
        int n = arr.length();
        AQI[] aqis = new AQI[n];

        for(int i=0;i<n;i++){
            AQI aqi = new AQI();
            JSONObject o = arr.getJSONObject(i);
            aqi.setAqi(o.getString("aqi"));
            aqi.setArea(o.getString("area"));
            aqi.setPosition_name(o.getString("position_name"));
            aqi.setPrimary_pollutant(o.getString("primary_pollutant"));
            aqi.setQuality(o.getString("quality"));
            aqi.setStation_code(o.getString("station_code"));
            aqi.setTime_point(o.getString("time_point"));
            aqis[i] = aqi;
        }
        Log.e("hello",aqis[n-1].toString());
        return aqis;

    }
}
