package com.example.rollator.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rollator.MainActivity;
import com.example.rollator.R;
import com.example.rollator.databinding.FragmentHomeBinding;
import com.example.rollator.functions.FunPatient;
import com.example.rollator.test.PatientModelTest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private DatePickerDialog datePickerDialog;
    private Button dateButton, btnopslaan;
    private EditText txtNaam, txtKamer, txtTelefoon, txtAdres, txtPostcode;
    private Spinner drpBejaardentehuis;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        PatientModelTest.testPatientCreation();

        fillSpinnerBejaardentehuis(root);
        initDatePicker();

        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        dateButton = root.findViewById(R.id.datePickerButton);
        dateButton.setText("1900-11-11");

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        drpBejaardentehuis = (Spinner) root.findViewById(R.id.spinnerBejaardentehuis);
        txtNaam = (EditText) root.findViewById(R.id.Naam);
        txtKamer = (EditText) root.findViewById(R.id.Kamer);
        txtTelefoon = (EditText) root.findViewById(R.id.Telefoon);
        txtAdres = (EditText) root.findViewById(R.id.Adres);
        txtPostcode = (EditText) root.findViewById(R.id.Postcode);

        btnopslaan = root.findViewById(R.id.btnOpslaan);
        btnopslaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opslaan(root);
            }
        });

        return root;
    }

    private void fillSpinnerBejaardentehuis(View root) {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Zorghuis de villa");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) root.findViewById(R.id.spinnerBejaardentehuis);
        sItems.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    private int getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

//        int month = cal.get(Calendar.MONTH);
//        month = month + 1;
//        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year;
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        final Calendar calenderMaxLeeftijd = Calendar.getInstance();
        int maxLeeftijd = getTodaysDate() - 70;
        calenderMaxLeeftijd.set(maxLeeftijd, 11, 31);

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calenderMaxLeeftijd.getTimeInMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
//        return getMonthFormat(month) + " " + day + " " + year;
        return year + "-" + getMonthFormat(month) + "-" + day;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "01";
        if(month == 2)
            return "02";
        if(month == 3)
            return "03";
        if(month == 4)
            return "04";
        if(month == 5)
            return "05";
        if(month == 6)
            return "06";
        if(month == 7)
            return "07";
        if(month == 8)
            return "08";
        if(month == 9)
            return "09";
        if(month == 10)
            return "10";
        if(month == 11)
            return "11";
        if(month == 12)
            return "12";

        //default should never happen
        return "1";
    }

    private void Opslaan(View root){
//        getActivity().runOnUiThread(() -> {
//            root.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
//        });
        //Creating a new background Thread so the application doesn't freeze
        Thread th = new Thread(){
            @Override
            public void run(){
                //Check if the input is empty
                try {
                    //Getting the input data from the user
                    String bejaardentehuis = drpBejaardentehuis.getSelectedItem().toString();
                    String geboortedatum = String.valueOf(dateButton.getText());
                    String naam = String.valueOf(txtNaam.getText());
                    String kamer = String.valueOf(txtKamer.getText());
                    String telefoon = String.valueOf(txtTelefoon.getText());
                    String adres = String.valueOf(txtAdres.getText());
                    String postcode = String.valueOf(txtPostcode.getText());
                    if (naam.trim().length() < 1 || telefoon.trim().length() < 1 || adres.trim().length() < 1 || kamer.trim().length() < 1 || postcode.trim().length() <1){
                        //Letting the user know they need to fill in all fields
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), "Alle velden moeten ingevuld worden!", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    if (telefoon.trim().length() > 15){
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), "Een telefoonnummer mag niet langer zijn dan 15 cijfers!", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            root.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                        }
                    });

                    Date geboortedatumConverted = Date.valueOf(geboortedatum);
                    Date verblijftsinds = Date.valueOf(LocalDate.now().toString());

                    //Calling the real insert function with the user input as variables
                    FunPatient.insertPatient(bejaardentehuis, naam, telefoon, adres, postcode, geboortedatumConverted, verblijftsinds, kamer);

//                    root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                            txtKamer.getText().clear();
                            txtAdres.getText().clear();
                            txtNaam.getText().clear();
                            txtPostcode.getText().clear();
                            txtTelefoon.getText().clear();
                            dateButton.setText("1900-11-11");

                            Toast.makeText(getActivity(), "Patient " + naam +" ingevoerd!", Toast.LENGTH_SHORT).show();

                        }
                    });


                } catch (Exception e){
                    Log.e("Error", "" + e.getMessage());
                }

            }
        };
        //Starting the background Thread
        th.start();
    }
}