package com.example.rollator.ui.patientShow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.FragmentNavigator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rollator.MainActivity;
import com.example.rollator.R;
import com.example.rollator.functions.FunPatient;
import com.example.rollator.ui.connectRollator.ConnectScreen;
import com.example.rollator.ui.gallery.GalleryFragment;
import com.example.rollator.ui.gallery.GalleryViewModel;
import com.example.rollator.ui.home.HomeFragment;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;

public class PatientShow extends AppCompatActivity {
    private EditText txtKamer, txtNaam, txtTelefoon, txtAdres, txtPostcode;
    private Button dateButton, btnVerwijder, btnOpslaan, btnKoppel;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_show);

        Intent intent = getIntent();
        String kamerOld = intent.getStringExtra("kamer");
        String naam = intent.getStringExtra("naam");
        String telefoon = intent.getStringExtra("telefoon");
        String adres = intent.getStringExtra("adres");
        String postcode = intent.getStringExtra("postcode");
        String geboortedatum = intent.getStringExtra("geboortedatum");

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        txtKamer = (EditText) findViewById(R.id.Kamer);
        txtNaam = (EditText) findViewById(R.id.Naam);
        txtTelefoon = (EditText) findViewById(R.id.Telefoon);
        txtAdres = (EditText) findViewById(R.id.Adres);
        txtPostcode = (EditText) findViewById(R.id.Postcode);
        dateButton = (Button) findViewById(R.id.datePickerButton);
        btnVerwijder = (Button) findViewById(R.id.btnVerwijder);
        btnOpslaan = (Button) findViewById(R.id.btnOpslaan);
        btnKoppel = (Button) findViewById(R.id.btnKoppel);

        txtKamer.setText(kamerOld);
        txtNaam.setText(naam);
        txtTelefoon.setText(telefoon);
        txtAdres.setText(adres);
        txtPostcode.setText(postcode);
        dateButton.setText(geboortedatum);

        String[] arrayGeboortedatumString = geboortedatum.split("-", 5);
        initDatePicker(arrayGeboortedatumString);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        btnVerwijder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(kamerOld);
            }
        });

        btnKoppel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientShow.this, ConnectScreen.class);
                i.putExtra("naam", naam);
                i.putExtra("kamer", kamerOld);
                startActivity(i);
            }
        });

        btnOpslaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the users input
                String kamer = String.valueOf(txtKamer.getText());
                String naam = String.valueOf(txtNaam.getText());
                String telefoon = String.valueOf(txtTelefoon.getText());
                String adres = String.valueOf(txtAdres.getText());
                String postcode = String.valueOf(txtPostcode.getText());
                String geboortedatum = String.valueOf(dateButton.getText());

                //Calling the update function with the user input variables and the old phone number to use in the query
                updateGebruiker(kamerOld, kamer, naam, telefoon, adres, postcode, geboortedatum);

            }
        });
    }

    private int getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    private void initDatePicker(String[] arrayGeboortedatumString)
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

        int size = arrayGeboortedatumString.length;
        int [] arrayGeboortedatumInt = new int [size];
        for(int i=0; i<size; i++) {
            arrayGeboortedatumInt[i] = Integer.parseInt(arrayGeboortedatumString[i]);
        }

        datePickerDialog = new DatePickerDialog(PatientShow.this, style, dateSetListener, arrayGeboortedatumInt[0], arrayGeboortedatumInt[1], arrayGeboortedatumInt[2]);
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

    private void delete(String kamer){
        PatientShow.this.runOnUiThread(() -> {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            Toast.makeText(PatientShow.this, "Bezig met verwijderen van  " + kamer + " ...", Toast.LENGTH_LONG).show();
        });

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    FunPatient p = new FunPatient();
                    //Calling the real delete function with the before and after variable
                    p.delete(kamer);

                    //Running the ui thread to let the user know it deleted the patient
                    PatientShow.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(PatientShow.this, "Verwijderd!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Back to the list
                    Intent i = new Intent(PatientShow.this, MainActivity.class);
                    startActivity(i);

                } catch (Exception e){
                    //Catching a error and logging it
                    Log.e("Error", e.getMessage());
                }
            }

        };
        //Starting the background Thread
        th.start();
    }

    public void updateGebruiker(String kamerOld,String kamer, String naam, String telefoon, String adres, String postcode, String geboortedatum){
        PatientShow.this.runOnUiThread(() -> {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        });
        //Creating a new background Thread so the application doesn't freeze
        Thread th = new Thread() {
            @Override
            public void run() {
                //Check if the input is empty
                if (naam.trim().length() < 1 || telefoon.trim().length() < 1 || adres.trim().length() < 1 || kamer.trim().length() < 1 || postcode.trim().length() <1){
                    //Letting the user know they need to fill in all fields
                    PatientShow.this.runOnUiThread(() -> {
                        Toast.makeText(PatientShow.this, "Alle velden moeten ingevuld worden!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                if (telefoon.trim().length() > 15){
                    PatientShow.this.runOnUiThread(() -> {
                        Toast.makeText(PatientShow.this, "Een telefoonnummer mag niet langer zijn dan 15 cijfers!", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                Date geboortedatumConverted = Date.valueOf(geboortedatum);

                //Calling the real update function and giving it the variables
                FunPatient.updatePatient(kamerOld, kamer, naam, telefoon, adres, postcode, geboortedatumConverted);

                PatientShow.this.runOnUiThread(() -> {
                    Toast.makeText(PatientShow.this, "Patient is geupdate!", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                });

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        };
        //Starting the background Thread
        th.start();
    }


}