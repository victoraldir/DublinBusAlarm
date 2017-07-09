package dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import quartzo.com.dublinbusalarm.R;

/**
 * Created by victoraldir on 12/02/2017.
 */

public class CustomDialog extends DialogFragment {

    public static CustomDialog newInstance(int num) {
        CustomDialog f = new CustomDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.customdialogform, container, false);

        return v;
    }
}
