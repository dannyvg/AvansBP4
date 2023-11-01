package com.example.rollator.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rollator.R;
import com.example.rollator.databinding.FragmentSlideshowBinding;
import com.example.rollator.ui.loopMotivaties.WandelingenDone;
import com.example.rollator.ui.loopMotivaties.WandelingenProgress;
import com.example.rollator.ui.patientShow.PatientShow;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    private Button done, progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        done = (Button) root.findViewById(R.id.btnDone);
        progress = (Button) root.findViewById(R.id.btnProgress);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), WandelingenDone.class);
                startActivity(myIntent);
            }
        });

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), WandelingenProgress.class);
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
}