package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.APIService;
import com.api.interfaces.IService;
import com.api.utils.Provider;
import com.entities.Station;
import com.entities.Transport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.TransportAdapter;
import exceptions.NoInterfaceImplementation;
import quartzo.com.dublinbusalarm.R;

/**
 * Created by victoraldir on 08/07/2017.
 */

public class ListTransportDialogFragment extends DialogFragment {

    private static final String ARG_STATION = "_station";
    private static final String ARG_TRANSPORT = "_transport";

    private ListTransportListener mListener;
    private TransportAdapter mAdapter;
    private String mTransportNumber;
    private String mStatioNumber;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    public static ListTransportDialogFragment newInstance(String station, String transport) {

        Bundle arguments = new Bundle();
        arguments.putString(ARG_STATION,station);
        arguments.putString(ARG_TRANSPORT,transport);

        ListTransportDialogFragment listTransportDialogFragment = new ListTransportDialogFragment();
        listTransportDialogFragment.setArguments(arguments);

        return listTransportDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){

            mTransportNumber = getArguments().getString(ARG_TRANSPORT);
            mStatioNumber = getArguments().getString(ARG_STATION);

        }

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppCompatAlertDialogStyle);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transport_list, container, false);

        mProgressBar = (ProgressBar) v.findViewById(R.id.transport_list_progress);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycle_transport_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new TransportAdapter(new ArrayList<Transport>(),mListener);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        new LoadTransportsAsyncTask().execute(mStatioNumber);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if(context instanceof ListTransportListener){
            mListener = (ListTransportListener) context;
        }else {
            throw new NoInterfaceImplementation();
        }

    }

    public interface ListTransportListener {
        void onSetTransport(Transport transport);
    }

    public class LoadTransportsAsyncTask extends AsyncTask<String, Object, List<Transport>>{

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected List<Transport> doInBackground(String... params) {

            IService service = APIService.getInstance().getProvider(Provider.DUBLIN_BUS);

            Station station = service.getStationById(mStatioNumber);

            if(station != null && station.getTransportList() != null){
                return station.getTransportList();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Transport> transportList) {

            mAdapter.swap(transportList);
            mAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }
    }
}
