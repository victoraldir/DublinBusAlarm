package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import entities.Bus;
import quartzo.com.dublinbusalarm.R;

/**
 * Created by victor on 06/12/15.
 */
public class ListBusAdapter extends BaseAdapter {

    private List<Bus> busList;
    private int busesLayoutId;
    private Context ctx;

    public ListBusAdapter(List<Bus> catList, Context ctx) {

        this.busesLayoutId = R.layout.listviewbuses;
        this.busList = catList;
        this.ctx = ctx;

    }

    @Override
    public int getCount() {
        return busList.size();
    }

    @Override
    public Object getItem(int position) {
        return busList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return busList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(busesLayoutId, parent, false);
        }

        Bus bus = busList.get(position);

        TextView textView = (TextView) v.findViewById(R.id.textViewBus);

        textView.setText(bus.getTime());

        return v;
    }
}
