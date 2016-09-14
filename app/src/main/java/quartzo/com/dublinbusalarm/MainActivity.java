package quartzo.com.dublinbusalarm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.List;

import adapters.AlarmExpandableAdapter;
import adapters.ListBusAdapter;
import decorator.SimpleDividerItemDecoration;
import entities.AlarmChild;
import entities.AlarmParent;
import entities.Bus;
import entities.Constants;
import utils.AlarmPersistence;
import utils.UtilCheckConnectivity;
import utils.Utils;

public class MainActivity extends AppCompatActivity implements ExpandableRecyclerAdapter.ExpandCollapseListener {

    ProgressDialog mProgressDialog;
    private Context mContext;

    private RecyclerView listBuses;

    private RecyclerView listAlarms;

    private TextView txtBusStop;

    private TextView txtBusNumber;

    private TextInputLayout inputBusStopLayout;

    private TextInputLayout inputBusNumberLayout;

    private AlertDialog listDialog;

    private LocalTime timeBus;

    private AlarmChild alarmChosenSwitch;

    private Switch switchCurr;

    private AlarmExpandableAdapter alarmExpandableAdapter;

    private List<AlarmChild> alarms;

    private Tracker mTracker;

    private RecyclerView.LayoutManager lmAlarms;

    private TimePickerDialog mTimePickerDialog;

    private boolean isScheduled;

    private CheckBox checkBoxSound;

    private LocalTime timePicked;

    private CheckBox checkBoxVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isScheduled = false;
        //txttitle.setText(readDataShared());

        mContext = this;

        //pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);

        com.github.clans.fab.FloatingActionButton fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isScheduled =false;

                if (!UtilCheckConnectivity.isInternetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    warningNoConnection(Constants.NO_CONNECTION).show();
                }else{

                    dialogFormBusNumberBusStop().show();
                }
            }
        });

        com.github.clans.fab.FloatingActionButton fabSchedule = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab3);

        fabSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScheduled =true;
                mTimePickerDialog = new TimePickerDialog(mContext,evtPickTime,LocalTime.now().getHourOfDay(),LocalTime.now().getMinuteOfHour(),Utils.is24Hours(mContext));
                mTimePickerDialog.show();
            }
        });

        listBuses = new RecyclerView(mContext);

        listAlarms = (RecyclerView) findViewById(R.id.listViewAlarms);

        listBuses.setLayoutManager(new LinearLayoutManager(mContext));

        lmAlarms = new LinearLayoutManager(mContext);

        listAlarms.setLayoutManager(lmAlarms);

        listAlarms.setHasFixedSize(true);

        listAlarms.addItemDecoration(new SimpleDividerItemDecoration(this));

        listBuses.setHasFixedSize(true);

        listBuses.addItemDecoration(new SimpleDividerItemDecoration(this));

        alarms = AlarmPersistence.readStoredAlarms(mContext);
        alarmExpandableAdapter = new AlarmExpandableAdapter(mContext, alarms, evtSwitcherAlarm,
                evtDelete, evtCheckSound, evtCheckVibrate);
        alarmExpandableAdapter.setExpandCollapseListener(this);
        listAlarms.setAdapter(alarmExpandableAdapter);

        updateListViewAlarms();

        AlertDialog.Builder builderListDialog;

        builderListDialog =
                new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);

        builderListDialog.setTitle("Pick a bus");

        builderListDialog.setPositiveButton("New search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogFormBusNumberBusStop().show();
            }
        });

        builderListDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchCurr != null)
                    switchCurr.setChecked(false);
            }
        });

        listDialog = builderListDialog.create();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + MainActivity.class);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemExpanded(int position) {

        alarmExpandableAdapter.collapseAllParents();
        alarmExpandableAdapter.expandParent(position);

    }

    @Override
    public void onListItemCollapsed(int position) {

        alarmExpandableAdapter.collapseParent(position);

//        if(!alarmExpandableAdapter.parentAlarmList.equals(alarms))
//        alarms.addAll(alarmExpandableAdapter.parentAlarmList);

        alarmExpandableAdapter.notifyChildItemChanged(position,0);
        alarmExpandableAdapter.notifyParentItemChanged(position);


    }

    //------------------------------------------------Private Methods ---------------------------

    private void updateListViewAlarms() {


        if (!alarms.isEmpty()) {
            listAlarms.setVisibility(View.VISIBLE);

        } else {
            listAlarms.setVisibility(View.GONE);
        }

    }

    private void saveAlarm(int minutes, Bus bus) {
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        DateTime date = new DateTime();

        int alarmId = LocalTime.now().getMillisOfDay();

        if (minutes != 0) {

            LocalTime busTime = timeBus.minusHours(LocalTime.now().getHourOfDay()).minusMinutes(LocalTime.now().getMinuteOfHour()).minusSeconds(LocalTime.now().getSecondOfMinute());

            int diff = busTime.getMinuteOfHour() - minutes;

            date = date.plusMinutes(diff);
        } else {

            LocalTime timeChosen = LocalTime.now().withHourOfDay(timePicked.getHourOfDay()).
                    withMinuteOfHour(timePicked.getMinuteOfHour()).withSecondOfMinute(0);

            if (timeChosen.isBefore(LocalTime.now())) {
                date = date.plusDays(1).withHourOfDay(timeChosen.getHourOfDay()).
                        withMinuteOfHour(timeChosen.getMinuteOfHour()).withSecondOfMinute(0);
            } else {
                date = date.withHourOfDay(timeChosen.getHourOfDay()).withMinuteOfHour(timeChosen.getMinuteOfHour()).withSecondOfMinute(0);
            }

        }

        AlarmChild myData;

        if (txtBusNumber == null) {
            myData = alarmChosenSwitch;
            myData.setIsActive(true);

        } else {
            if (bus != null) {
                myData = new AlarmChild(alarmId,0, bus, "", "",
                        true, checkBoxVibrate.isChecked(), checkBoxSound.isChecked(), false, AlarmChild.generateDaysWeek());

            } else {

                Bus newBus = new Bus();
                newBus.setRoute(txtBusNumber.getText().toString());
                newBus.setStop(txtBusStop.getText().toString());
                newBus.setDestination("N/A");
                newBus.setTime("00:00:00");

                myData = new AlarmChild(alarmId, 0, newBus, LocalTime.now().toString(Constants.TIME_MASK) , "",
                        true, checkBoxVibrate.isChecked(), checkBoxSound.isChecked(), true, AlarmChild.generateDaysWeek());

            }

        }

        Intent alarmIntent;

        if (!isScheduled) {

            alarmIntent = new Intent("EXECUTE_ALARM_BUS");

            alarmIntent.putExtra("myAlarmId", myData.getId());

            PendingIntent appIntent = PendingIntent.getBroadcast(mContext, alarmId, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

            manager.set(AlarmManager.RTC_WAKEUP, date.getMillis(), appIntent);

        } else {

            alarmIntent = new Intent("EXECUTE_ALARM_BUS_SCHEDULE");

            alarmIntent.putExtra("myAlarmId", myData.getId());

            PendingIntent appIntent = PendingIntent.getBroadcast(mContext, alarmId, alarmIntent, PendingIntent.FLAG_ONE_SHOT);

            //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, date.getMillis(), AlarmManager.INTERVAL_DAY, appIntent);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, DateTime.now().plusSeconds(30).getMillis(), AlarmManager.INTERVAL_DAY, appIntent);
        }

        AlarmPersistence.saveAlarm(myData, mContext);

        if (!alarms.contains(myData)) {
            alarms.add(0, myData);


            if (alarms.size() == 1) {
                alarmExpandableAdapter.notifyDataSetChanged();
            }

            alarmExpandableAdapter.notifyParentItemInserted(0);
            alarmExpandableAdapter.collapseAllParents();
            alarmExpandableAdapter.expandParent(0);

            lmAlarms.scrollToPosition(0);

        } else {
            int position = alarms.indexOf(myData);
            alarmExpandableAdapter.notifyParentItemChanged(position);
            //lmAlarms.scrollToPosition(position);

        }

        updateListViewAlarms();
    }

    private boolean validateData() {
        boolean result = true;

        String busStop = txtBusStop.getText().toString();
        if (busStop == null || busStop.equals("0") || busStop.equals("")) {
            // We set the error message
            inputBusStopLayout.setError("You must to inform a valid bus stop");

            if(isScheduled) {
                String busNumber = txtBusStop.getText().toString();
                if (busNumber == null || busNumber.equals("0") || busNumber.equals("")) {
                    // We set the error message
                    inputBusNumberLayout.setError("You must to inform a valid route");
                    //result = false;
                }
            }

            result = false;
        }

        if(isScheduled) {
            String busNumber = txtBusStop.getText().toString();
            if (busNumber == null || busNumber.equals("0") || busNumber.equals("")) {
                // We set the error message
                inputBusNumberLayout.setError("You must to inform a valid route");
                result = false;
            }
        }


        return result;
    }

    //----------------------------------------------AlertDialogs --------------------------------

    private AlertDialog dialogFormBusNumberBusStop(){

        AlertDialog.Builder builder =
                new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.customdialogform, (ViewGroup) findViewById(android.R.id.content), false);

        inputBusStopLayout = (TextInputLayout) v.findViewById(R.id.busStopdWrapper);
        inputBusNumberLayout = (TextInputLayout) v.findViewById(R.id.busNumberWrapper);

        inputBusStopLayout.setErrorEnabled(true);

        inputBusStopLayout.setHint("Bus stop");


        if(!isScheduled){
            inputBusNumberLayout.setHint("Bus number (optional)");
            builder.setTitle("Search a bus");
        }else{
            inputBusNumberLayout.setHint("Bus number");
            inputBusNumberLayout.setErrorEnabled(true);
            builder.setTitle("Inform a route and bus stop");
        }


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setView(v);

        final AlertDialog f = builder.create();

        f.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = f.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        txtBusStop = (TextView) f.findViewById(R.id.editTextBusStop);

                        txtBusNumber = (TextView) f.findViewById(R.id.editTextBusNumber);

                        inputBusStopLayout = (TextInputLayout) f.findViewById(R.id.busStopdWrapper);
                        inputBusNumberLayout = (TextInputLayout) f.findViewById(R.id.busNumberWrapper);

                        inputBusNumberLayout.setErrorEnabled(true);

//                        if (validateData()) {
//                            new LastBusAsync().execute(txtBusStop.getText().toString(), txtBusNumber.getText().toString());
//                            f.dismiss();
//                        }

                        if (validateData()) {
                            if (!isScheduled) {
                                new LastBusAsync().execute(txtBusStop.getText().toString(), txtBusNumber.getText().toString());
                            } else {
                                dialogPickIntervalAlarm(null).show();
                                //saveAlarm(0, null);
                            }
                            f.dismiss();
                        }

                        //Dismiss once everything is OK.

                    }
                });
            }
        });

        //builder.show();

        return f;
    }

    private AlertDialog dialogPickIntervalAlarm(final Bus bus){

        AlertDialog.Builder builder =
                new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.customdialogtimepicker, (ViewGroup) findViewById(android.R.id.content), false);

        builder.setTitle("Set alert options");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Dialog f = (Dialog) dialog;

                NumberPicker number = (NumberPicker) f.findViewById(R.id.numberPicker2);

                checkBoxVibrate = (CheckBox) f.findViewById(R.id.checkBoxVibrate);

                checkBoxSound = (CheckBox) f.findViewById(R.id.checkBoxSound);
                if(!isScheduled) {
                    saveAlarm(number.getValue(), bus);
                }else{
                    saveAlarm(0, null);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchCurr != null)
                    switchCurr.setChecked(false);
            }
        });


        NumberPicker time = (NumberPicker) v.findViewById(R.id.numberPicker2);
        if(!isScheduled) {
            timeBus = LocalTime.parse(bus.getTime());

            LocalTime difference = timeBus.minusHours(LocalTime.now().getHourOfDay()).minusMinutes(LocalTime.now().getMinuteOfHour()).minusSeconds(LocalTime.now().getSecondOfMinute());

            int val = difference.getMinuteOfHour();

            time.setMinValue(1);
            time.setMaxValue(val - 1);

            time.setValue(val - 1);


        }else{
            time.setMinValue(1);
            time.setMaxValue(15);

            time.setValue(10);
        }

        builder.setView(v);

        return builder.create();
    }

    private AlertDialog warningNoConnection(int cons) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);

        switch (cons){
            case Constants.NO_CONNECTION:
                builder.setTitle("Oh dear! =(");
                builder.setMessage("You are not connected on internet. Please, connect to internet before execute this operation.");
                builder.setPositiveButton("OK", null);
                break;
            case Constants.NO_DATA_UNAVAILABLE:
                builder.setTitle("Ops! ");
                builder.setMessage("Something went wrong. No data has been found");
                builder.setPositiveButton("New search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogFormBusNumberBusStop().show();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                break;
            case Constants.NO_DATA_UNAVAILABLE_SWITCHER:
                builder.setTitle("Ops! ");
                builder.setMessage("Something went wrong. No data has been found");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(switchCurr != null)
                            switchCurr.setChecked(false);
                    }
                });
                break;
        }

        return builder.create();

    }

    //------------------------------------------------Events--------------------------------------

    private TimePickerDialog.OnTimeSetListener evtPickTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timePicked = LocalTime.now().withHourOfDay(hourOfDay).withMinuteOfHour(minute);

            if (!UtilCheckConnectivity.isInternetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                warningNoConnection(Constants.NO_CONNECTION).show();

            }else{

                dialogFormBusNumberBusStop().show();
            }

        }
    };

    private View.OnClickListener evtSwitcherAlarm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Switch swt = (Switch) v;
            isScheduled = false;
            if (swt.isChecked()) {
                if (UtilCheckConnectivity.isInternetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    AlarmChild alarm = (AlarmChild) v.getTag();
                    alarmChosenSwitch = alarm;
                    switchCurr = swt;

                    new LastBusAsync().execute(alarm.getBus().getStop(), alarm.getBus().getRoute(), "isSwitcher");
                } else {
                    warningNoConnection(Constants.NO_CONNECTION).show();
                    swt.setChecked(false);
                }
            } else {
                AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

                AlarmChild alarm = (AlarmChild) v.getTag();

                Intent alarmIntent = new Intent("EXECUTE_ALARM_BUS");

                manager.cancel(PendingIntent.getBroadcast(mContext, alarm.getId(), alarmIntent, PendingIntent.FLAG_ONE_SHOT));

                alarm.setIsActive(false);

                AlarmPersistence.saveAlarm(alarm, mContext);

            }
        }
    };

    private View.OnClickListener evtDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

            final AlarmChild alarm = (AlarmChild) v.getTag();

            Intent alarmIntent = new Intent("EXECUTE_ALARM_BUS");

            manager.cancel(PendingIntent.getBroadcast(mContext, alarm.getId(), alarmIntent, PendingIntent.FLAG_ONE_SHOT));

            if(alarm.getIdNextAlarm() != 0){
                manager.cancel(PendingIntent.getBroadcast(mContext, alarm.getIdNextAlarm(), alarmIntent, PendingIntent.FLAG_ONE_SHOT));
            }

            AlarmPersistence.deleteAlarm(alarm, mContext);

            final int position = alarms.indexOf(alarm);

            alarms.remove(alarm);

            alarmExpandableAdapter.notifyParentItemRemoved(position);


            if (alarms.isEmpty()) {
                updateListViewAlarms();
            }

            Snackbar.make(v, "Alarm deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlarmPersistence.saveAlarm(alarm, mContext);

                            if (!alarms.contains(alarm)) {
                                alarms.add(position, alarm);
                                if (alarms.size() == 1) {
                                    alarmExpandableAdapter.notifyDataSetChanged();
                                    alarmExpandableAdapter.notifyParentItemInserted(position);
                                    lmAlarms.scrollToPosition(position);
                                } else {
                                    alarmExpandableAdapter.notifyParentItemInserted(position);
                                }
                            } else {
                                //int position = alarms.indexOf(alarm);
                                alarmExpandableAdapter.notifyParentItemChanged(position);

                            }

                            lmAlarms.scrollToPosition(position);

                            updateListViewAlarms();
                        }
                    })
                    .setDuration(Snackbar.LENGTH_LONG)
                    .show();

        }
    };

    CompoundButton.OnCheckedChangeListener evtCheckVibrate = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AlarmChild alarmTest = (AlarmChild) buttonView.getTag();
            alarmTest.setIsVibrate(isChecked);
            AlarmPersistence.saveAlarm(alarmTest, mContext);
            alarms.set(alarms.indexOf(alarmTest),alarmTest);
            //alarmExpandableAdapter.notifyChildItemChanged(alarms.indexOf(alarmTest),0);
        }
    };

    CompoundButton.OnCheckedChangeListener evtCheckSound = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AlarmChild alarmTest = (AlarmChild) buttonView.getTag();
            alarmTest.setIsSound(isChecked);
            AlarmPersistence.saveAlarm(alarmTest, mContext);
            alarms.set(alarms.indexOf(alarmTest),alarmTest);
            //alarmExpandableAdapter.notifyChildItemChanged(alarms.indexOf(alarmTest),0);
        }
    };

    private AdapterView.OnClickListener evtClickBus = new AdapterView.OnClickListener() {

        @Override
        public void onClick(final View view) {

            final Bus bus = (Bus) view.getTag();

            listDialog.cancel();

            dialogPickIntervalAlarm(bus).show();

        }
    };

    //------------------------------------------------AsyncTasks ----------------------------------

    public class LastBusAsync extends AsyncTask<String, List<Bus>, List<Bus>> {

        boolean isSwitcher = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Dublin Bus");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected List<Bus> doInBackground(String... params) {

            if(params.length > 2){
                if(params[2] != null && params[2].equals("isSwitcher"))
                    isSwitcher = true;
            }

            return Utils.requestListBus(params);
        }

        @Override
        protected void onPostExecute(List<Bus> result) {

            mProgressDialog.dismiss();

            if(!result.isEmpty()) {

                listBuses.setAdapter(new ListBusAdapter(result, mContext, listDialog, evtClickBus));

                listDialog.setView(listBuses);

                listDialog.show();

            }else{
                if(isSwitcher){
                    warningNoConnection(Constants.NO_DATA_UNAVAILABLE_SWITCHER).show();
                }else {
                    warningNoConnection(Constants.NO_DATA_UNAVAILABLE).show();
                }
            }
        }
    }
}
