package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import exceptions.NoInterfaceImplementation;
import quartzo.com.dublinbusalarm.R;

/**
 * Created by victoraldir on 08/07/2017.
 */

public class ListTransportDialogFragment extends DialogFragment {

    private static final String ARG_BUS_STOP = "_bus_number";
    private static final String ARG_BUS_NUMBER = "_bus_stop";

    private ListTransportListener mListener;

    public static ListTransportDialogFragment newInstance(String busStop, String bus) {

        Bundle arguments = new Bundle();
        arguments.putString(ARG_BUS_STOP,busStop);
        arguments.putString(ARG_BUS_STOP,bus);

        ListTransportDialogFragment listTransportDialogFragment = new ListTransportDialogFragment();
        listTransportDialogFragment.setArguments(arguments);

        return new ListTransportDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.customdialogform, null);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.pick_bus)
                .setView(view)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_next, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onSetBus();
                        dialogInterface.dismiss();
                    }
                })

                .create();
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
        void onSetBus();
    }

}
