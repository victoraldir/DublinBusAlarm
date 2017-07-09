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

public class BusStopFormDialogFragment extends DialogFragment {

    private BusStopFormListener mListener;

    public static BusStopFormDialogFragment newInstance() {
        return new BusStopFormDialogFragment();
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
                        mListener.onSetBusStop();
                        dialogInterface.dismiss();
                    }
                })

                .create();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if(context instanceof BusStopFormListener){
            mListener = (BusStopFormListener) context;
        }else {
            throw new NoInterfaceImplementation();
        }

    }

    public interface BusStopFormListener {
        void onSetBusStop();
    }

}
