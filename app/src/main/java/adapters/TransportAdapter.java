package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entities.Transport;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import fragments.ListTransportDialogFragment;
import quartzo.com.dublinbusalarm.R;
import utils.DateUtils;

/**
 * Created by victoraldir on 12/07/2017.
 */

public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.ViewHolder> {

    private List<Transport> mTransportList;
    private ListTransportDialogFragment.ListTransportListener mListener;

    public TransportAdapter(List<Transport> transportList,
                            ListTransportDialogFragment.ListTransportListener listener){
        mTransportList = transportList;
        mListener = listener;
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

        final Transport transport = mTransportList.get(position);

        holder.textViewDestination.setText(transport.getDestination());
        holder.textViewMinutes.setText(DateUtils.getIntervalMinutes(transport.getExpectedTime()));
        holder.textViewRoute.setText(transport.getRoute());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSetTransport(transport);
            }
        });

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
