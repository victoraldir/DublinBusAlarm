package adapters;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import entities.Alarm;
import quartzo.com.dublinbusalarm.LivePainelActivity;
import quartzo.com.dublinbusalarm.R;
import utils.AlarmPersistence;

/**
 * Created by victor on 28/12/15.
 */
public class ListAlarmsAdapter extends RecyclerView.Adapter<ListAlarmsAdapter.AlarmViewHolder> {

    Context mContext;
    List<Alarm> alarms;
    View.OnClickListener evtSwitcherAlarm;

    public ListAlarmsAdapter(Context mContext, List<Alarm> alarms, View.OnClickListener evtSwitcherAlarm) {
        this.mContext = mContext;
        this.alarms = alarms;
        this.evtSwitcherAlarm = evtSwitcherAlarm;
    }

    private View.OnClickListener evtClickAlarm = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Integer taggedPosition = (Integer) v.getTag();
                //Alarm alarm =  alarms.get(taggedPosition);
                Alarm alarm = (Alarm) v.getTag();

                Intent it = new Intent(mContext, LivePainelActivity.class);

                it.putExtra("myDataSerialized", alarm.serialize());

                mContext.startActivity(it);
            }
        };

    private View.OnClickListener evtDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Integer taggedPosition = (Integer) v.getTag();

            AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

            //Alarm alarm =  alarms.get(taggedPosition);
            Alarm alarm = (Alarm) v.getTag();

            Intent alarmIntent = new Intent("EXECUTE_ALARM_BUS");

            manager.cancel(PendingIntent.getBroadcast(mContext, alarm.getId(), alarmIntent, PendingIntent.FLAG_ONE_SHOT));

            AlarmPersistence.deleteAlarm(alarm, mContext);

            alarms.remove(alarm);

            Toast toast = Toast.makeText(mContext, "Deleted " + alarm.toString(), Toast.LENGTH_SHORT);
            toast.show();

            notifyDataSetChanged();

        }
    };

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_alarms_adapter, parent, false);

        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {

        Alarm alarm = alarms.get(position);

        holder.txtStopNumber.setText("Bus stop " + alarm.getBusStop());
        holder.txtBusNumber.setText("Bus number " + alarm.getBus());
        holder.txtTimeNotif.setText("ID Alarm is.. " + alarm.getId());

        holder.btnDelete.setOnClickListener(evtDelete);
        holder.btnDelete.setTag(alarm);

        holder.itemView.setOnClickListener(evtClickAlarm);

        holder.itemView.setTag(alarm);

        holder.switchAlarm.setOnClickListener(evtSwitcherAlarm);

        holder.switchAlarm.setTag(alarm);

        holder.switchAlarm.setChecked(alarm.isActive());

    }

    @Override
    public int getItemCount() {
        if(alarms == null){
            return 0;
        }
        return alarms.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {

        public TextView txtBusNumber;
        public TextView txtStopNumber;
        public TextView txtTimeNotif;
        public ImageView btnDelete;
        public Switch switchAlarm;

        public AlarmViewHolder(View v) {
            super(v);

            txtBusNumber = (TextView) v.findViewById(R.id.textViewBusNumber);

            txtStopNumber = (TextView) v.findViewById(R.id.textViewStopNumber);

            txtTimeNotif = (TextView) v.findViewById(R.id.textViewTimeNotification);

            btnDelete = (ImageView) v.findViewById(R.id.imageViewDelete);

            switchAlarm = (Switch) v.findViewById(R.id.seekBarAlarm);

        }
    }
}
