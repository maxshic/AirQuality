package cn.teng520.airquality;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Lenovo on 2017/6/15.
 */

public class MyAdapter extends BaseAdapter {

    private AQI[] aqis;
    private Context context;

    public MyAdapter(Context contexts,AQI[] aqis) {
        this.context = contexts;
        this.aqis = aqis;
    }

    @Override
    public int getCount() {
        return aqis.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.airitem,null);
        TextView textView1 = (TextView)itemView.findViewById(R.id.textView1);
        TextView textView2 = (TextView)itemView.findViewById(R.id.textView2);
        TextView textView3 = (TextView)itemView.findViewById(R.id.textView3);
        textView1.setText(aqis[i].getPosition_name());
        textView2.setText(aqis[i].getAqi());
        textView3.setText(aqis[i].getPrimary_pollutant());
        return itemView;
    }
}
