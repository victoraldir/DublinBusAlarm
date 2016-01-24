package adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import entities.Alarm;
import quartzo.com.dublinbusalarm.LivePainelActivity;
import quartzo.com.dublinbusalarm.R;
import utils.AlarmPersistence;


/**
 * Created by victoraldir on 22/01/16.
 */
public class AlarmExpandableAdapter extends ExpandableRecyclerAdapter<ListAlarmsAdapter.AlarmParentViewHolder, ListAlarmsAdapter.AlarmChildViewHolder> {

    private LayoutInflater mInflator;
    private Context mContext;
    private View.OnClickListener evtSwitcherAlarm;
    private List<Alarm> parentAlarmList;
    private View.OnClickListener evtDelete;

    public AlarmExpandableAdapter(Context mContext, List<Alarm> parentAlarmList, View.OnClickListener evtSwitcherAlarm, View.OnClickListener evtDelete) {
        super(parentAlarmList);
        this.mInflator = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.evtSwitcherAlarm = evtSwitcherAlarm;
        this.parentAlarmList = parentAlarmList;
        this.evtDelete = evtDelete;
    }

    @Override
    public ListAlarmsAdapter.AlarmParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflator.inflate(R.layout.layout_list_alarms_adapter_parent, parentViewGroup, false);
        return new ListAlarmsAdapter.AlarmParentViewHolder(view);
    }

    @Override
    public ListAlarmsAdapter.AlarmChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mInflator.inflate(R.layout.layout_list_alarms_adapter_child, childViewGroup, false);
        return new ListAlarmsAdapter.AlarmChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(ListAlarmsAdapter.AlarmParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        Alarm alarm = (Alarm) parentListItem;

        parentViewHolder.txtStopNumber.setText(alarm.getBus().getStop());
        parentViewHolder.txtBusNumber.setText("Route " + alarm.getBus().getRoute());
        parentViewHolder.txtTimeNotif.setText(alarm.getBus().getDestination());



        parentViewHolder.itemView.setOnClickListener(evtClickAlarm);

        parentViewHolder.itemView.setTag(alarm);

        parentViewHolder.switchAlarm.setOnClickListener(evtSwitcherAlarm);

        parentViewHolder.switchAlarm.setTag(alarm);

        parentViewHolder.switchAlarm.setChecked(alarm.isActive());


    }

    @Override
    public void onBindChildViewHolder(ListAlarmsAdapter.AlarmChildViewHolder childViewHolder, int position, Object childListItem) {
        Alarm alarm = (Alarm) childListItem;

        childViewHolder.btnDelete.setOnClickListener(evtDelete);
        childViewHolder.btnDelete.setTag(alarm);

        //TODO
//        childViewHolder.hasSoundTextBoxt.setChecked(alarm.isSound());
//        childViewHolder.hasVibrationCheckBox.setChecked(alarm.isVibrate());

    }



    //----------------------------------- CLICK EVENTS ------------------------------------------

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

}
