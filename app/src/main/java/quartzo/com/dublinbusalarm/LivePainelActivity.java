package quartzo.com.dublinbusalarm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapters.ListBusAdapter;
import decorator.SimpleDividerItemDecoration;
import entities.Alarm;
import entities.Bus;
import entities.Constants;

public class LivePainelActivity extends AppCompatActivity {

    //private  TextView txtBusStop;

    private RecyclerView listBusesLive;

    //ViewSwitcher viewSwitcher;

    Animation slide_in_left, slide_out_right;

    Context mContext;

    Alarm myData;

    boolean isFirsLoad;
    //ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_painel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myData = Alarm.create(getIntent().getStringExtra("myDataSerialized"));

        //txtBusStop = (TextView) findViewById(R.id.textViewBusStop);

        //txtBusStop.setText(myData.getBusStop());

        //viewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);

        getSupportActionBar().setTitle("Bus stop " + myData.getBus().getStop());
        getSupportActionBar().setSubtitle("Real time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        listBusesLive = (RecyclerView) findViewById(R.id.listViewBusLive);

        listBusesLive.setLayoutManager(layoutManager);

        listBusesLive.addItemDecoration(new SimpleDividerItemDecoration(this));

        slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);

        //viewSwitcher.setInAnimation(slide_in_left);
        //viewSwitcher.setOutAnimation(slide_out_right);

        isFirsLoad = true;

        new LastBusAsync().execute(myData.getBus().getStop());

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

            List<Bus> buses = new ArrayList<>();

            try {

                // Connect to the web site
                Document document = Jsoup.connect(Constants.URL_DUBLIN_BUS + params[0]).get();
                //Document document = Jsoup.connect(Constants.URL_DUBLIN_BUS).get();

                // Get the html document title
                Iterator<Element> table = document.select("table[id=rtpi-results]").select("tr").iterator();

                if(table != null) {

                    //Iterator<Element> ite = table.select("tr:contains(" + params[1] + " )").iterator();

                    while (table.hasNext()) {
                        Element ele = table.next();

                        if (!ele.className().contains("yellow")) {
                            Bus newBus = new Bus();

                            newBus.setRoute(ele.select("td").get(Constants.ROUTE).text());
                            newBus.setTime(ele.select("td").get(Constants.TIME).text());
                            newBus.setDestination(ele.select("td").get(Constants.DESTINATION).text());


                            buses.add(newBus);
                        }
                    }
                    }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return buses;
        }

        @Override
        protected void onPostExecute(List<Bus> result) {

            if(!result.isEmpty()) {
                listBusesLive.setAdapter(new ListBusAdapter(result, mContext));
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
