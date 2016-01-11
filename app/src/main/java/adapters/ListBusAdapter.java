package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.LocalTime;

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
    private LocalTime timeCurr;

    public ListBusAdapter(List<Bus> catList, Context ctx, LocalTime timeCurr) {

        this.busesLayoutId = R.layout.layout_list_bus_adapter;
        this.busList = catList;
        this.ctx = ctx;
        this.timeCurr = timeCurr;

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

        TextView textViewRoute = (TextView) v.findViewById(R.id.textViewRoute);

        TextView textViewMinutes = (TextView) v.findViewById(R.id.textViewMinutes);

        TextView textViewDestination = (TextView) v.findViewById(R.id.textViewDestination);

        textViewRoute.setText("Route " + bus.getRoute());

        textViewDestination.setText(bus.getDestination());

        if(!bus.getTime().equalsIgnoreCase("due")) {
            LocalTime diff = LocalTime.parse(bus.getTime()).minusHours(timeCurr.getHourOfDay()).minusMinutes(timeCurr.getMinuteOfHour()).minusSeconds(timeCurr.getSecondOfMinute());
            textViewMinutes.setText(String.valueOf(diff.getMinuteOfHour()));
        }else{
            textViewMinutes.setText(bus.getTime());
        }



        return v;
    }


}
