package adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.joda.time.LocalTime;

import java.util.List;

import entities.Bus;
import quartzo.com.dublinbusalarm.R;

/**
 * Created by victor on 06/12/15.
 */
public class ListBusAdapter extends RecyclerView.Adapter<ListBusAdapter.BusViewHolder> {

    private List<Bus> busList;
    //private int busesLayoutId;
    private Context ctx;
    //private LocalTime timeCurr;
    AlertDialog listDialog;
    AdapterView.OnClickListener evtClickBus;

    public ListBusAdapter(List<Bus> catList, Context ctx, AlertDialog listDialog, AdapterView.OnClickListener evtClickBus) {

        //this.busesLayoutId = R.layout.layout_list_bus_adapter;
        this.busList = catList;
        this.ctx = ctx;
        //this.timeCurr = timeCurr;
        this.listDialog = listDialog;
        this.evtClickBus = evtClickBus;

    }

    public ListBusAdapter(List<Bus> catList, Context ctx) {

        //this.busesLayoutId = R.layout.layout_list_bus_adapter;
        this.busList = catList;
        this.ctx = ctx;
        //this.timeCurr = timeCurr;
        this.listDialog = listDialog;

    }

    @Override
    public BusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_list_bus_adapter, parent, false);

        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusViewHolder holder, int position) {

        Bus bus = busList.get(position);

        holder.textViewDestination.setText(bus.getDestination());

        holder.textViewRoute.setText("Route " + bus.getRoute());

        holder.itemView.setOnClickListener(evtClickBus);

        holder.itemView.setTag(bus);

        LocalTime timeCurr = LocalTime.now();

        if(!bus.getTime().equalsIgnoreCase("due")) {
            LocalTime diff = LocalTime.parse(bus.getTime()).minusHours(timeCurr.getHourOfDay()).minusMinutes(timeCurr.getMinuteOfHour()).minusSeconds(timeCurr.getSecondOfMinute());
            holder.textViewMinutes.setText(String.valueOf(diff.getMinuteOfHour()));
        }else{
            holder.textViewMinutes.setText(bus.getTime());
        }
    }


    @Override
    public long getItemId(int position) {
        return busList.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewRoute;

        public TextView textViewMinutes;

        public TextView textViewDestination;

        public BusViewHolder(View v) {
            super(v);

            textViewRoute = (TextView) v.findViewById(R.id.textViewRoute);

            textViewMinutes = (TextView) v.findViewById(R.id.textViewMinutes);

            textViewDestination = (TextView) v.findViewById(R.id.textViewDestination);

        }
    }
}
