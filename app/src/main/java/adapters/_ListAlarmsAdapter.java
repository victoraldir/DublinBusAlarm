package adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

import entities.Alarm;
import quartzo.com.dublinbusalarm.LivePainelActivity;
import quartzo.com.dublinbusalarm.R;
import utils.AlarmPersistence;

/**
 * Created by victor on 28/12/15.
 */
public class _ListAlarmsAdapter  {

//    Context mContext;
//    List<Alarm> alarms;
//    View.OnClickListener evtSwitcherAlarm;
//
//    public _ListAlarmsAdapter(Context mContext, List<Alarm> alarms, View.OnClickListener evtSwitcherAlarm) {
//        this.mContext = mContext;
//        this.alarms = alarms;
//        this.evtSwitcherAlarm = evtSwitcherAlarm;
//    }
//
//    private View.OnClickListener evtClickAlarm = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Integer taggedPosition = (Integer) v.getTag();
//                //Alarm alarm =  alarms.get(taggedPosition);
//                Alarm alarm = (Alarm) v.getTag();
//
//                Intent it = new Intent(mContext, LivePainelActivity.class);
//
//                it.putExtra("myDataSerialized", alarm.serialize());
//
//                mContext.startActivity(it);
//            }
//        };
//
//    private View.OnClickListener evtDelete = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            //Integer taggedPosition = (Integer) v.getTag();
//
//            AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//
//            //Alarm alarm =  alarms.get(taggedPosition);
//            Alarm alarm = (Alarm) v.getTag();
//
//            Intent alarmIntent = new Intent("EXECUTE_ALARM_BUS");
//
//            manager.cancel(PendingIntent.getBroadcast(mContext, alarm.getId(), alarmIntent, PendingIntent.FLAG_ONE_SHOT));
//
//            AlarmPersistence.deleteAlarm(alarm, mContext);
//
//            alarms.remove(alarm);
//
//            Toast toast = Toast.makeText(mContext, "Deleted " + alarm.toString(), Toast.LENGTH_SHORT);
//            toast.show();
//
//            notifyDataSetChanged();
//
//        }
//    };
//
//    @Override
//    public AlarmParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_alarms_adapter_parent, parent, false);
//
//        return new AlarmParentViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(AlarmParentViewHolder holder, int position) {
//
//        Alarm alarm = alarms.get(position);
//
//        holder.txtStopNumber.setText(alarm.getBus().getStop());
//        holder.txtBusNumber.setText("Route " + alarm.getBus().getRoute());
//        holder.txtTimeNotif.setText(alarm.getBus().getDestination());
//
////        holder.btnDelete.setOnClickListener(evtDelete);
////        holder.btnDelete.setTag(alarm);
//
//        holder.itemView.setOnClickListener(evtClickAlarm);
//
//        holder.itemView.setTag(alarm);
//
//        holder.switchAlarm.setOnClickListener(evtSwitcherAlarm);
//
//        holder.switchAlarm.setTag(alarm);
//
//        holder.switchAlarm.setChecked(alarm.isActive());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        if(alarms == null){
//            return 0;
//        }
//        return alarms.size();
//    }

//    public static class AlarmParentViewHolder extends ParentViewHolder {
//
//        private static final float INITIAL_POSITION = 0.0f;
//        private static final float ROTATED_POSITION = 180f;
//
//        public TextView txtBusNumber;
//        public TextView txtStopNumber;
//        public TextView txtTimeNotif;
//
//        public Switch switchAlarm;
//        public ImageButton mArrowExpandImageView;
//
//
//        public AlarmParentViewHolder(View v) {
//            super(v);
//
//            txtBusNumber = (TextView) v.findViewById(R.id.textViewBusNumber);
//
//            txtStopNumber = (TextView) v.findViewById(R.id.textViewStopNumber);
//
//            txtTimeNotif = (TextView) v.findViewById(R.id.textViewTimeNotification);
//
//            switchAlarm = (Switch) v.findViewById(R.id.seekBarAlarm);
//
//            mArrowExpandImageView = (ImageButton) v.findViewById(R.id.arrow_expand_imageview);
//
//            mArrowExpandImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isExpanded()) {
//                        collapseView();
//                    } else {
//                        expandView();
//                    }
//
//                }
//            });
//
//        }
//
//        @Override
//        public boolean shouldItemViewClickToggleExpansion() {
//            return false;
//        }
//
//        @Override
//        public void setExpanded(boolean expanded) {
//            super.setExpanded(expanded);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                if (expanded) {
//                    mArrowExpandImageView.setRotation(ROTATED_POSITION);
//                } else {
//                    mArrowExpandImageView.setRotation(INITIAL_POSITION);
//                }
//            }
//        }
//
//        @Override
//        public void onExpansionToggled(boolean expanded) {
//            super.onExpansionToggled(expanded);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                RotateAnimation rotateAnimation;
//                if (expanded) { // rotate clockwise
//                    rotateAnimation = new RotateAnimation(ROTATED_POSITION,
//                            INITIAL_POSITION,
//                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//                } else { // rotate counterclockwise
//                    rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
//                            INITIAL_POSITION,
//                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//                }
//
//                rotateAnimation.setDuration(200);
//                rotateAnimation.setFillAfter(true);
//                mArrowExpandImageView.startAnimation(rotateAnimation);
//            }
//        }
//    }
//
//    public static class AlarmChildViewHolder extends ChildViewHolder {
//
//        public CheckBox hasSoundTextBoxt;
//        public CheckBox hasVibrationCheckBox;
//        public ImageButton btnDelete;
//
//
//        public AlarmChildViewHolder(View itemView) {
//            super(itemView);
//
//            hasSoundTextBoxt = (CheckBox) itemView.findViewById(R.id.checkBoxHasSound);
//            hasVibrationCheckBox = (CheckBox) itemView.findViewById(R.id.checkBoxHasVibration);
//            btnDelete = (ImageButton) itemView.findViewById(R.id.imageViewDelete);
//        }
//    }
}
