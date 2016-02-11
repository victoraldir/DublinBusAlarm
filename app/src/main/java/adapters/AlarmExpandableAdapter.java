package adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

import entities.Alarm;
import quartzo.com.dublinbusalarm.LivePainelActivity;
import quartzo.com.dublinbusalarm.R;


/**
 * Created by victoraldir on 22/01/16.
 */
public class AlarmExpandableAdapter extends ExpandableRecyclerAdapter<AlarmExpandableAdapter.AlarmParentViewHolder, AlarmExpandableAdapter.AlarmChildViewHolder> {

    private LayoutInflater mInflator;
    private Context mContext;
    private View.OnClickListener evtSwitcherAlarm;
    //private List<Alarm> parentAlarmList;
    private View.OnClickListener evtDelete;
    private CompoundButton.OnCheckedChangeListener evtCheckSound;
    private CompoundButton.OnCheckedChangeListener evtCheckVibrate;

    public AlarmExpandableAdapter(Context mContext, List<Alarm> parentAlarmList, View.OnClickListener evtSwitcherAlarm,
                                  View.OnClickListener evtDelete, CompoundButton.OnCheckedChangeListener evtCheckSound,
                                  CompoundButton.OnCheckedChangeListener evtCheckVibrate) {
        super(parentAlarmList);
        this.mInflator = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.evtSwitcherAlarm = evtSwitcherAlarm;
        //this.parentAlarmList = parentAlarmList;
        this.evtDelete = evtDelete;
        this.evtCheckSound = evtCheckSound;
        this.evtCheckVibrate = evtCheckVibrate;
    }

    @Override
    public AlarmParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflator.inflate(R.layout.layout_list_alarms_adapter_parent, parentViewGroup, false);
        return new AlarmParentViewHolder(view);
    }

    @Override
    public AlarmChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mInflator.inflate(R.layout.layout_list_alarms_adapter_child, childViewGroup, false);
        return new AlarmChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(AlarmParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
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
    public void onBindChildViewHolder(AlarmChildViewHolder childViewHolder, int position, Object childListItem) {
        Alarm alarm = (Alarm) childListItem;

        childViewHolder.btnDelete.setOnClickListener(evtDelete);
        childViewHolder.btnDelete.setTag(alarm);

        childViewHolder.hasSoundTextBoxt.setChecked(alarm.isSound());
        childViewHolder.hasSoundTextBoxt.setTag(alarm);
        childViewHolder.hasSoundTextBoxt.setOnCheckedChangeListener(evtCheckSound);

        childViewHolder.hasVibrationCheckBox.setChecked(alarm.isVibrate());
        childViewHolder.hasVibrationCheckBox.setTag(alarm);
        childViewHolder.hasVibrationCheckBox.setOnCheckedChangeListener(evtCheckVibrate);
    }

    //----------------------------------- PARENT HOLDER ----------------------------------------

    public static class AlarmParentViewHolder extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180f;

        public TextView txtBusNumber;
        public TextView txtStopNumber;
        public TextView txtTimeNotif;

        public Switch switchAlarm;
        public ImageButton mArrowExpandImageView;


        public AlarmParentViewHolder(View v) {
            super(v);

            txtBusNumber = (TextView) v.findViewById(R.id.textViewBusNumber);

            txtStopNumber = (TextView) v.findViewById(R.id.textViewStopNumber);

            txtTimeNotif = (TextView) v.findViewById(R.id.textViewTimeNotification);

            switchAlarm = (Switch) v.findViewById(R.id.seekBarAlarm);

            mArrowExpandImageView = (ImageButton) v.findViewById(R.id.arrow_expand_imageview);

            mArrowExpandImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()) {
                        collapseView();
                    } else {
                        expandView();
                    }

                }
            });

        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (expanded) {
                    mArrowExpandImageView.setRotation(ROTATED_POSITION);
                } else {
                    mArrowExpandImageView.setRotation(INITIAL_POSITION);
                }
            }
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                RotateAnimation rotateAnimation;
                if (expanded) { // rotate clockwise
                    rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                } else { // rotate counterclockwise
                    rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                            INITIAL_POSITION,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                }

                rotateAnimation.setDuration(200);
                rotateAnimation.setFillAfter(true);
                mArrowExpandImageView.startAnimation(rotateAnimation);
            }
        }
    }

    //----------------------------------- CHIELD HOLDER -----------------------------------------

    public static class AlarmChildViewHolder extends ChildViewHolder {

        public CheckBox hasSoundTextBoxt;
        public CheckBox hasVibrationCheckBox;
        public ImageButton btnDelete;


        public AlarmChildViewHolder(View itemView) {
            super(itemView);

            hasSoundTextBoxt = (CheckBox) itemView.findViewById(R.id.checkBoxHasSound);
            hasVibrationCheckBox = (CheckBox) itemView.findViewById(R.id.checkBoxHasVibration);
            btnDelete = (ImageButton) itemView.findViewById(R.id.imageViewDelete);
        }
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
