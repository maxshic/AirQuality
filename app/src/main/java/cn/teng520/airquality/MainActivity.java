package cn.teng520.airquality;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;

public class MainActivity extends Activity {

    private ListView airList;
    private String city2;
    private String[] city_name;
    private String[] city_code;
    private ArrayAdapter<String> adapter;
    private TextView area;
    private TextView aqi;
    private TextView quality;
    private TextView primary_pollutant;
    private TextView time_point;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city_name = getResources().getStringArray(R.array.city1);
        city_code = getResources().getStringArray(R.array.city2);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,city_name);
        airList = (ListView)findViewById(R.id.airList);
        area = (TextView)findViewById(R.id.area);
        aqi = (TextView)findViewById(R.id.aqi);
        quality = (TextView)findViewById(R.id.quality);
        primary_pollutant = (TextView)findViewById(R.id.primary_pollutant);
        time_point = (TextView)findViewById(R.id.time_point);
        imageView = (ImageView)findViewById(R.id.button_refresh);

        SharedPreferences sp = getSharedPreferences("aqi", Context.MODE_PRIVATE);
        city2 = sp.getString("code","changshu");
        loadAQI(city2);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCity();
            }
        });
    }

    private void loadAQI(final String city2){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AQI[] aqis = new AQILoader().loader(city2);
                    showAQIOnUiThread(aqis);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showAQIOnUiThread(final AQI[] aqis) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int n = aqis.length;
                AQI[] temps = new AQI[n-1];
                for(int i=0;i<temps.length;i++){
                    temps[i] = aqis[i];
                }
                AQI temp = aqis[n-1];
                area.setText(temp.getArea());
                aqi.setText(temp.getAqi());
                quality.setText(String.format(getResources().getString(R.string.quality),temp.getQuality()));
                primary_pollutant.setText(String.format(getResources().getString(R.string.primary_pollutant),temp.getPrimary_pollutant()));
                time_point.setText(String.format(getResources().getString(R.string.time_point),temp.getTime_point()));
                MyAdapter adapter = new MyAdapter(MainActivity.this,temps);
                airList.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.city_choose:
                chooseCity();
                return true;
            case R.id.about:
                showAbout();
                return true;
        }
        return false;
    }

    private void chooseCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择城市:");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sp = getSharedPreferences("aqi", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("code",city_code[i]);
                editor.commit();
                Toast.makeText(MainActivity.this,city_name[i],Toast.LENGTH_SHORT).show();
                loadAQI(city_code[i]);
            }
        });
        builder.show();
    }

    private void showAbout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.about)
                .setMessage(R.string.about_message)
                .setPositiveButton(android.R.string.ok,null)
                .create()
                .show();
    }
}
