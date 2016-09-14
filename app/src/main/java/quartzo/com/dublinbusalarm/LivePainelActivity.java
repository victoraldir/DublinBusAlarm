package quartzo.com.dublinbusalarm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapters.ListBusAdapter;
import decorator.SimpleDividerItemDecoration;
import entities.AlarmChild;
import entities.AlarmParent;
import entities.Bus;
import utils.Utils;

public class LivePainelActivity extends AppCompatActivity {

    //private  TextView txtBusStop;

    private RecyclerView listBusesLive;

    //ViewSwitcher viewSwitcher;

    Animation slide_in_left, slide_out_right;

    Context mContext;

    AlarmParent myData;

    boolean isFirsLoad;
    //ProgressDialog mProgressDialog;

    LinearLayout layout;

    private ProgressBar progressBar;

    private TextView txtNoData;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_painel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myData = AlarmChild.create(getIntent().getStringExtra("myDataSerialized"));

        int mNotificationId = getIntent().getIntExtra("mNotificationId",0);

        if(mNotificationId != 0){
            NotificationManager mNM = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            mNM.cancel(mNotificationId);
        }
        //txtBusStop = (TextView) findViewById(R.id.textViewBusStop);

        //txtBusStop.setText(myData.getBusStop());

        //viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);

        getSupportActionBar().setTitle("Bus stop " + myData.getBus().getStop());
        getSupportActionBar().setSubtitle("Real time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBarListBus);

        txtNoData = (TextView) findViewById(R.id.txtNoData);

        listBusesLive = (RecyclerView) findViewById(R.id.listViewBusLive);

        listBusesLive.setLayoutManager(new LinearLayoutManager(mContext));

        listBusesLive.addItemDecoration(new SimpleDividerItemDecoration(this));

        listBusesLive.setVisibility(View.GONE);

        slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);

        layout = (LinearLayout) findViewById(R.id.layoutNoBus);



        //viewSwitcher.setInAnimation(slide_in_left);
        //viewSwitcher.setOutAnimation(slide_out_right);

        isFirsLoad = true;

        new LastBusAsync().execute(myData.getBus().getStop());

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = new Intent(this, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + LivePainelActivity.class);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public class LastBusAsync extends AsyncTask<String, List<Bus>, List<Bus>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if(!isFirsLoad){
//                viewSwitcher.getNextView();
//            }else{
//                isFirsLoad = false;
//            }


        }

        @Override
        protected List<Bus> doInBackground(String... params) {

            return Utils.requestListBus(params);
        }

        @Override
        protected void onPostExecute(List<Bus> result) {

            if(!result.isEmpty()) {
                layout.setVisibility(View.GONE);
                listBusesLive.setAdapter(new ListBusAdapter(result, mContext));
                listBusesLive.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
            }
            //viewSwitcher.showNext();

            Timer t = new Timer();
            t.schedule(new TimerTask() {

                @Override
                public void run() {                   //timer

                    new LastBusAsync().execute(myData.getBus().getStop());

                }
            }, 5000L);
        }
    }
}
