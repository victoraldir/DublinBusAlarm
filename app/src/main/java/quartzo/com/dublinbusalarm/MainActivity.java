package quartzo.com.dublinbusalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.entities.Transport;

import org.joda.time.LocalTime;

import java.util.List;

import adapters.AlarmExpandableAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import decorator.SimpleDividerItemDecoration;
import entities.AlarmChild;
import entities.Bus;
import entities.Constants;
import fragments.BusStopFormDialogFragment;
import fragments.ListTransportDialogFragment;
import utils.AlarmPersistence;
import utils.StringUtils;
import utils.UtilCheckConnectivity;


public class MainActivity extends AppCompatActivity implements ExpandableRecyclerAdapter.ExpandCollapseListener,
        BusStopFormDialogFragment.BusStopFormListener,
        ListTransportDialogFragment.ListTransportListener{

    public static final String TAG_PICK_STATION = "dialogPickStation";
    public static final String TAG_PICK_BUS = "dialogPickBus";

    public static final String TAG_DIALOG = "dialog";

    ProgressDialog mProgressDialog;
    private Context mContext;

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    private RecyclerView listAlarms;
    private AlertDialog listDialog;
    private LocalTime timeBus;
    private AlarmChild alarmChosenSwitch;
    private Switch switchCurr;
    private AlarmExpandableAdapter alarmExpandableAdapter;
    private List<AlarmChild> alarms;
    private RecyclerView.LayoutManager lmAlarms;
    private TimePickerDialog mTimePickerDialog;
    private boolean isScheduled;
    private CheckBox checkBoxSound;
    private LocalTime timePicked;
    private CheckBox checkBoxVibrate;

    public static String mBusStationFromActivityResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isScheduled = false;

        mContext = this;

        //mAdapter = new ListBusAdapter(mContext, listDialog, evtClickBus);

        RecyclerView listBuses = new RecyclerView(mContext);
        listBuses.setLayoutManager(new LinearLayoutManager(mContext));
        listBuses.setHasFixedSize(true);
        listBuses.addItemDecoration(new SimpleDividerItemDecoration(this));



        listAlarms = (RecyclerView) findViewById(R.id.listViewAlarms);

        listAlarms.setLayoutManager(lmAlarms);
        listAlarms.setHasFixedSize(true);
        listAlarms.addItemDecoration(new SimpleDividerItemDecoration(this));
        alarms = AlarmPersistence.readStoredAlarms(mContext);
        alarmExpandableAdapter = new AlarmExpandableAdapter(mContext, alarms, evtSwitcherAlarm,
                evtDelete, evtCheckSound, evtCheckVibrate);
        alarmExpandableAdapter.setExpandCollapseListener(this);
        listAlarms.setAdapter(alarmExpandableAdapter);

        lmAlarms = new LinearLayoutManager(mContext);

        updateListViewAlarms();

        AlertDialog.Builder builderListDialog;

        builderListDialog =
                new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);

        builderListDialog.setTitle("Pick a bus");

        builderListDialog.setPositiveButton("New search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialogFormBusNumberBusStop().show();
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

    }

    @OnClick(R.id.fab)
    public void showDialogForAddingAlarm() {
        isScheduled =false;

        if (!UtilCheckConnectivity.isInternetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            warningNoConnection(Constants.NO_CONNECTION).show();
        }else{

            showDialog();
        }
    }

    void showDialog() {

        DialogFragment newFragment = BusStopFormDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), TAG_PICK_STATION);

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



    //----------------------------------------------AlertDialogs --------------------------------



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
                        //dialogFormBusNumberBusStop().show();
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

                //dialogFormBusNumberBusStop().show();
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

            //dialogPickIntervalAlarm(bus).show();

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

            //return Utils.requestListBus(params);
            return null;
        }

        @Override
        protected void onPostExecute(List<Bus> result) {

            mProgressDialog.dismiss();

            if(!result.isEmpty()) {

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

    public void showDialog(DialogFragment dialogFragment){


        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        dialogFragment.show(ft, TAG_DIALOG);
    }

    @Override
    public void onSetBusStop(String busSopNumber, String busNumber) {
        showDialog(ListTransportDialogFragment.newInstance(busSopNumber,busNumber));
    }

    @Override
    public void onPickBusStopMap() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != RESULT_CANCELED){

            if(resultCode == MarkerActivity.RC_PICK_PLACE){
                mBusStationFromActivityResult = StringUtils.sanitizeBusStation(
                        data.getStringExtra(MarkerActivity.EXTRA_BUS_STATION));
                Snackbar.make(mCoordinatorLayout,mBusStationFromActivityResult,Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public void onSetTransport(Transport transport) {
        Snackbar.make(mCoordinatorLayout,transport.getRoute() + " clicked",Snackbar.LENGTH_SHORT).show();
    }
}
