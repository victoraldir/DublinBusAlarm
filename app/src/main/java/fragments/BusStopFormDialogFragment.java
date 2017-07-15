package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import exceptions.NoInterfaceImplementation;
import quartzo.com.dublinbusalarm.MainActivity;
import quartzo.com.dublinbusalarm.MarkerActivity;
import quartzo.com.dublinbusalarm.R;

/**
 * Created by victoraldir on 08/07/2017.
 */

public class BusStopFormDialogFragment extends DialogFragment {

    public static final String ARG_BUS_STOP = "busStop";
    private EditText mEditTextBusStop;

    private BusStopFormListener mListener;

    public void seBusStop(String busStop){
        mEditTextBusStop.setText(busStop);
    }

    public static BusStopFormDialogFragment newInstance() {
        return new BusStopFormDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.customdialogform, null);

        Button btnMap= (Button) view.findViewById(R.id.btn_map);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), MarkerActivity.class);
                startActivityForResult(it,MarkerActivity.RC_PICK_PLACE);

            }
        });

        mEditTextBusStop = (EditText) view.findViewById(R.id.editTextBusStop);
        final EditText editTextBusNumber = (EditText) view.findViewById(R.id.editTextBusNumber);

        final TextInputLayout busStopdWrapper = (TextInputLayout) view.findViewById(R.id.busStopdWrapper);

        final AlertDialog dialogObject = new AlertDialog.Builder(getContext())
                .setTitle(R.string.pick_bus_stop)
                .setView(view)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_next, null)
                .create();


        dialogObject.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if(mEditTextBusStop.getText().toString().isEmpty()){

                            busStopdWrapper.setError(getString(R.string.bus_stop_not_empty));

                        }else{
                            mListener.onSetBusStop(mEditTextBusStop.getText().toString(),
                                    editTextBusNumber.getText().toString());

                            dialogInterface.dismiss();
                        }

                    }
                });

            }
        });

        return dialogObject;
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


    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity){

            if(((MainActivity) getActivity()).mBusStationFromActivityResult != null){
                seBusStop(((MainActivity) getActivity()).mBusStationFromActivityResult);
            }

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface BusStopFormListener {
        void onSetBusStop(String busStop, String bus);
        void onPickBusStopMap();
    }

}
