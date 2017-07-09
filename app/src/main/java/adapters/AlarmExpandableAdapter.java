package adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import entities.AlarmChild;
import entities.AlarmParent;
import entities.Constants;
import entities.DaysOfWeek;
import quartzo.com.dublinbusalarm.LivePainelActivity;
import quartzo.com.dublinbusalarm.R;
import utils.AlarmPersistence;



/**
 * Created by victoraldir on 22/01/16.
 */
public class AlarmExpandableAdapter extends ExpandableRecyclerAdapter<AlarmExpandableAdapter.AlarmParentViewHolder, AlarmExpandableAdapter.AlarmChildViewHolder> {

    private LayoutInflater mInflator;
    private Context mContext;
    private View.OnClickListener evtSwitcherAlarm;
    public List<AlarmChild> parentAlarmList;
    private View.OnClickListener evtDelete;
    private CompoundButton.OnCheckedChangeListener evtCheckSound;
    private CompoundButton.OnCheckedChangeListener evtCheckVibrate;
    private DateFormat inFormat;
    private DateFormat outFormat;

    public AlarmExpandableAdapter(Context mContext, List<AlarmChild> parentAlarmList, View.OnClickListener evtSwitcherAlarm,
                                  View.OnClickListener evtDelete, CompoundButton.OnCheckedChangeListener evtCheckSound,
                                  CompoundButton.OnCheckedChangeListener evtCheckVibrate) {
        super(parentAlarmList);
        this.mInflator = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.evtSwitcherAlarm = evtSwitcherAlarm;
        this.parentAlarmList = parentAlarmList;
        this.evtDelete = evtDelete;
        this.evtCheckSound = evtCheckSound;
        this.evtCheckVibrate = evtCheckVibrate;
        //this.df = new SimpleDateFormat(DateFormat.is24HourFormat(mContext) ? Constants.TIME_MASK_24 : Constants.TIME_MASK_12);

        this.inFormat = new SimpleDateFormat( "HH:mm:ss");
        //this.outFormat = new SimpleDateFormat(Utils.is24Hours(mContext) ? Constants.TIME_MASK_24 : Constants.TIME_MASK_12);
    }

    public void swap(List<AlarmChild> parentAlarmList){
        this.parentAlarmList = parentAlarmList;
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
        AlarmParent alarm = (AlarmParent) parentListItem;

        parentViewHolder.txtStopNumber.setText(alarm.getBus().getStop());
        parentViewHolder.txtBusNumber.setText("Route " + alarm.getBus().getRoute());
        parentViewHolder.txtRouteDescription.setText(alarm.getBus().getDestination());
        parentViewHolder.txtAlarmLabel.setText(alarm.getTag());
        try {

            if(!alarm.getTime().isEmpty()){
                parentViewHolder.txtAlarmTime.setText(outFormat.format(inFormat.parse(alarm.getTime())));
                parentViewHolder.txtAlarmTime.setVisibility(View.VISIBLE);
                parentViewHolder.linearLayoutQuick.setVisibility(View.GONE);
            }else{
                //parentViewHolder.txtAlarmTime.setText(outFormat.format(inFormat.parse(alarm.getTime())));
                parentViewHolder.txtAlarmTime.setVisibility(View.GONE);
                parentViewHolder.linearLayoutQuick.setVisibility(View.VISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        parentViewHolder.itemView.setOnClickListener(evtClickAlarm);

        parentViewHolder.itemView.setTag(alarm);

        parentViewHolder.switchAlarm.setOnClickListener(evtSwitcherAlarm);

        parentViewHolder.switchAlarm.setTag(alarm);

        parentViewHolder.switchAlarm.setChecked(alarm.isActive());


    }

    @Override
    public void onBindChildViewHolder(final AlarmChildViewHolder childViewHolder, int position, Object childListItem) {
        final AlarmChild alarmChild = (AlarmChild) childListItem;

        childViewHolder.btnDelete.setOnClickListener(evtDelete);
        childViewHolder.btnDelete.setTag(alarmChild);

        childViewHolder.hasSoundTextBoxt.setChecked(alarmChild.isSound());
        childViewHolder.hasSoundTextBoxt.setTag(alarmChild);
        childViewHolder.hasSoundTextBoxt.setOnCheckedChangeListener(evtCheckSound);

        childViewHolder.hasVibrationCheckBox.setChecked(alarmChild.isVibrate());
        childViewHolder.hasVibrationCheckBox.setTag(alarmChild);
        childViewHolder.hasVibrationCheckBox.setOnCheckedChangeListener(evtCheckVibrate);

        childViewHolder.isRepeat.setVisibility(alarmChild.getTime().isEmpty() ? View.GONE : View.VISIBLE);

        childViewHolder.isRepeat.setChecked(alarmChild.isRepeat());

        boolean showDaysButtons = alarmChild.isRepeat() && !alarmChild.getTime().isEmpty();

        childViewHolder.layoutButtonstDays.setVisibility(showDaysButtons ? View.VISIBLE : View.GONE);

        if(showDaysButtons) {
            LinearLayout layoutButtons = childViewHolder.layoutButtonstDays;

            for (int x = 0; x < layoutButtons.getChildCount(); x++) {
                ToggleButton currButton = (ToggleButton) layoutButtons.getChildAt(x);

                final Set<DaysOfWeek> days = alarmChild.getDays();

                if (days.contains(DaysOfWeek.valueOf(currButton.getTag().toString()))) {
                    currButton.setChecked(true);
                } else {
                    currButton.setChecked(false);
                }

                currButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DaysOfWeek day = DaysOfWeek.valueOf(v.getTag().toString());

                        if (days.contains(day)) {
                            days.remove(day);
                        } else {
                            days.add(day);
                        }

                        alarmChild.setDays(days);

                        AlarmPersistence.saveAlarm(alarmChild, mContext);

                        parentAlarmList.set(parentAlarmList.indexOf(alarmChild), alarmChild);
                    }
                });

            }
        }

//        childViewHolder.isRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    childViewHolder.layoutButtonstDays.setVisibility(View.VISIBLE);
//                    alarmChild.setIsRepeat(true);
//                } else {
//                    childViewHolder.layoutButtonstDays.setVisibility(View.GONE);
//                    alarmChild.setIsRepeat(false);
//                }
//
//                if(parentAlarmList.indexOf(alarmChild) == -1){
//                    String test = "";
//                }
//
//                parentAlarmList.set(parentAlarmList.indexOf(alarmChild), alarmChild);
//
//                AlarmPersistence.saveAlarm(alarmChild, mContext);
//
//
//            }
//        });

        childViewHolder.txtLabel.setText(alarmChild.getTag());

        childViewHolder.txtLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);

                final EditText input = new EditText(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                lp.setMargins(24,24,24,24);
                input.setLayoutParams(lp);
                input.setText(childViewHolder.txtLabel.getText());

                builder.setTitle("Label");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String label = input.getText().toString();

                        childViewHolder.txtLabel.setText(label);

                        if (!label.equals(alarmChild.getTag())) {
                            alarmChild.setTag(label);
                            AlarmPersistence.saveAlarm(alarmChild,mContext);
                        }

                        parentAlarmList.set(parentAlarmList.indexOf(alarmChild),alarmChild);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setView(input);

                builder.show();
            }
        });
    }

    //----------------------------------- PARENT HOLDER ----------------------------------------

    public static class AlarmParentViewHolder extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180f;

        public TextView txtBusNumber;
        public TextView txtStopNumber;
        public TextView txtRouteDescription;
        public TextView txtAlarmLabel;
        public TextView txtAlarmTime;

        public Switch switchAlarm;
        public ImageButton mArrowExpandImageView;
        public LinearLayout linearLayoutQuick;


        public AlarmParentViewHolder(View v) {
            super(v);

            txtBusNumber = (TextView) v.findViewById(R.id.textViewBusNumber);

            txtStopNumber = (TextView) v.findViewById(R.id.textViewStopNumber);

            txtRouteDescription = (TextView) v.findViewById(R.id.textViewRouteDescription);

            txtAlarmLabel = (TextView) v.findViewById(R.id.textViewAlarmLabel);

            txtAlarmTime = (TextView) v.findViewById(R.id.textAlarmTime);

            switchAlarm = (Switch) v.findViewById(R.id.seekBarAlarm);

            linearLayoutQuick = (LinearLayout) v.findViewById(R.id.linearLayoutQuickAlarm);

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
        public CheckBox isRepeat;
        public LinearLayout layoutButtonstDays;
        public EditText txtLabel;

        public ImageButton btnDelete;


        public AlarmChildViewHolder(View itemView) {
            super(itemView);

            hasSoundTextBoxt = (CheckBox) itemView.findViewById(R.id.checkBoxHasSound);
            hasVibrationCheckBox = (CheckBox) itemView.findViewById(R.id.checkBoxHasVibration);
            btnDelete = (ImageButton) itemView.findViewById(R.id.imageViewDelete);
            isRepeat = (CheckBox) itemView.findViewById(R.id.checkBoxRepeat);
            layoutButtonstDays = (LinearLayout) itemView.findViewById(R.id.repeat_days);
            txtLabel = (EditText) itemView.findViewById(R.id.editTextLabel);
        }
    }

    //----------------------------------- CLICK EVENTS ------------------------------------------

    private View.OnClickListener evtClickAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Integer taggedPosition = (Integer) v.getTag();
            //Alarm alarm =  alarms.get(taggedPosition);
            AlarmChild alarm = (AlarmChild) v.getTag();

            Intent it = new Intent(mContext, LivePainelActivity.class);

            it.putExtra("myDataSerialized", alarm.serialize());

            mContext.startActivity(it);
        }
    };

}
