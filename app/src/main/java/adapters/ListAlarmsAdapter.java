package adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import entities.Alarm;
import quartzo.com.dublinbusalarm.R;
import utils.AlarmPersistence;

/**
 * Created by victor on 28/12/15.
 */
public class ListAlarmsAdapter extends ArrayAdapter<Alarm> {

    public ListAlarmsAdapter(Context context, List<Alarm> alarms) {
        super(context, 0, alarms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_alarms_adapter, parent, false);
        }


        Alarm alarm = getItem(position);

        TextView txtBusNumber = (TextView) convertView.findViewById(R.id.textViewBusNumber);
        TextView txtStopNumber = (TextView) convertView.findViewById(R.id.textViewStopNumber);
        TextView txtTimeNotif = (TextView) convertView.findViewById(R.id.textViewTimeNotification);

        txtStopNumber.setText("Bus stop " + alarm.getBusStop());
        txtBusNumber.setText("Bus number " + alarm.getBus());
        txtTimeNotif.setText("ID Alarm is.. " + alarm.getId());


        ImageView btnDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);

        btnDelete.setTag(position);

        btnDelete.setOnClickListener(evtDelete);

        return convertView;
    }


    private View.OnClickListener evtDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer taggedPosition = (Integer) v.getTag();

            AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

            Alarm alam =  getItem(taggedPosition);

            Intent alarmIntent = new Intent("EXECUTE_ALARM_BUS");

            manager.cancel(PendingIntent.getBroadcast(getContext(), alam.getId(), alarmIntent, PendingIntent.FLAG_ONE_SHOT));

            AlarmPersistence.deleteAlarm(alam, getContext());

            remove(alam);

            Toast toast = Toast.makeText(getContext(), "Deleted " + alam.toString(), Toast.LENGTH_SHORT);
            toast.show();

            ListView mainListView = (ListView) v.getRootView().findViewById(R.id.listViewAlarms);

            notifyDataSetChanged();

        }
    };
}
