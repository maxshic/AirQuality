package cn.teng520.airquality;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import java.io.IOException;

public class MainActivity extends Activity {

    private ListView airList;
    private String city2 = "changshu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        airList = (ListView)findViewById(R.id.airList);
        loadAQI(city2);
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
                //cityTextView.setText(weather.getCity());
                MyAdapter adapter = new MyAdapter(MainActivity.this,aqis);
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
                return true;
            case R.id.about:
                showAbout();
                return true;
        }
        return false;
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
