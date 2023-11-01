package com.example.rollator.ui.loopMotivaties;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.rollator.R;
import com.example.rollator.functions.FunLoopMotivatie;

import java.util.List;
import java.util.Map;

public class WandelingenDone extends AppCompatActivity {

    ListView WandelingDoneListView;
    private List<Map<String,String>> mydatalist=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wandelingen_done);

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        WandelingDoneListView = (ListView) findViewById(R.id.WandelingProgressListView);

        getWandelingDone();
        Toast.makeText(WandelingenDone.this, "Wandelingen worden ingeladen", Toast.LENGTH_LONG).show();
    }

    SimpleAdapter a;

    public void getWandelingDone(){
        WandelingenDone.this.runOnUiThread(() -> {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        });
        //Creating a new background Thread so the application doesn't freeze
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    //Creating a variable for the getList() function
                    FunLoopMotivatie l = new FunLoopMotivatie();
                    mydatalist = l.getListDone();

                    //Creating a From variable to let the listview know where its coming from
//                    String[] from= {"kamer", "stappen", "wndlStart", "wndlStop"};
                    String[] from= {"kamer", "stappen",  "start", "stop"};
                    //Creating a Tow function to let the app know where to put the data in the list view item
                    int[] Tow = {R.id.listWndlKamer, R.id.listWndlStappen, R.id.listWndlStart, R.id.listWndlStop};

                    //Putting the data into the listview
                    //Layout.list calls a new XML file that represents a listview Item
                    a = new SimpleAdapter(WandelingenDone.this, mydatalist, R.layout.listwndldone,from, Tow);

                    //Running the ui thread to put the data into the listview
                    WandelingenDone.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WandelingDoneListView.setAdapter(a);
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        }
                    });

                } catch (Exception e){
                    //Catching a error and logging it
                    Log.e("Error", "" + e.getMessage());
                }
            }
        };
        //Starting the background Thread
        th.start();
    }
}