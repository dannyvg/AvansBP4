package com.example.rollator.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rollator.R;
import com.example.rollator.databinding.FragmentGalleryBinding;
import com.example.rollator.functions.FunPatient;
import com.example.rollator.ui.patientShow.PatientShow;

import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    ListView PatientenListView;
    private List<Map<String,String>> mydatalist=null;
    private Button btnBekijk;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        PatientenListView = (ListView) root.findViewById(R.id.WandelingProgressListView);
        getPatients(root);
        Toast.makeText(getActivity(), "Patienten worden ingeladen", Toast.LENGTH_LONG).show();
//        btnBekijk = (Button) root.findViewById(R.id.btnBekijk);

        PatientenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> g = mydatalist.get(i);

                Intent myIntent = new Intent(getActivity(), PatientShow.class);
                myIntent.putExtra("naam", g.get("naam"));
                myIntent.putExtra("geboortedatum", g.get("geboortedatum"));
                myIntent.putExtra("kamer", g.get("kamer"));
                myIntent.putExtra("telefoon", g.get("telefoon"));
                myIntent.putExtra("adres", g.get("adres"));
                myIntent.putExtra("postcode", g.get("postcode"));

                startActivity(myIntent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    SimpleAdapter a;
    public void getPatients(View root){
        getActivity().runOnUiThread(() -> {
            root.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        });
        //Creating a new background Thread so the application doesn't freeze
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    //Creating a variable for the getList() function
                    FunPatient p = new FunPatient();
                    mydatalist = p.getList();

                    //Creating a From variable to let the listview know where its coming from
                    String[] from= {"naam", "kamer"};
                    //Creating a Tow function to let the app know where to put the data in the list view item
                    int[] Tow = {R.id.listWndlKamer, R.id.listWndlStappen};

                    //Putting the data into the listview
                    //Layout.list calls a new XML file that represents a listview Item
                    a = new SimpleAdapter(getActivity(), mydatalist, R.layout.list,from, Tow);

                    //Running the ui thread to put the data into the listview
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PatientenListView.setAdapter(a);
                            root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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