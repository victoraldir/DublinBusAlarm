package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entities.Transport;

import java.util.ArrayList;
import java.util.List;

import quartzo.com.dublinbusalarm.R;

/**
 * Created by victoraldir on 12/07/2017.
 */

public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.ViewHolder> {

    private List<Transport> mTransportList;

    public TransportAdapter(List<Transport> transportList){
        mTransportList = transportList;
    }

    public void swap(List<Transport> transportList){
        if(transportList != null) {
            mTransportList = new ArrayList<>(transportList);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transport, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Transport transport = mTransportList.get(position);

        holder.textViewDestination.setText(transport.getDestination());
        holder.textViewMinutes.setText(transport.getExpectedTime());
        holder.textViewRoute.setText(transport.getRoute());

    }

    @Override
    public int getItemCount() {
        return mTransportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final TextView textViewMinutes;
        final TextView textViewRoute;
        final TextView textViewDestination;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            textViewMinutes = (TextView) itemView.findViewById(R.id.textViewMinutes);
            textViewRoute = (TextView) itemView.findViewById(R.id.textViewRoute);
            textViewDestination = (TextView) itemView.findViewById(R.id.textViewDestination);
        }
    }
}
