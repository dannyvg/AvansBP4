package com.example.rollator.test;

import android.util.Log;

import com.example.rollator.model.Patient;

import java.sql.Date;

public class PatientModelTest {
    public static void testPatientCreation() {
        // SETUP
        String Bejaardentehuis = "Zorghuis de villa";
        String Naam = "Danny van Gasteren";
        String Telefoon = "06-34466126";
        String Adres = "Statenlaan 7";
        String Postcode = "5223LH";
        Date Geboortedatum = Date.valueOf("1952-04-11");
        Date VerblijftSinds = Date.valueOf("2023-06-02");
        String Kamer = "B155";
        Patient patient;

        // MAKING THE PATIENT
        try {
            patient = new Patient(Bejaardentehuis, Naam, Telefoon, Adres, Postcode, Geboortedatum, VerblijftSinds, Kamer);
        } catch (Exception e) {
            // Handle any exception
            Log.e("Error Object","Failed to create a patient object: " + e.getMessage());
            return;
        }

        //CHECKING IF THE DATA IN PATIENT IS THE SAME AS WE SPECIFIED ABOVE
        if (Naam == patient.getNaam() && Bejaardentehuis == patient.getBejaardentehuis() && Telefoon == patient.getTelefoon() && Adres == patient.getAdres() &&
                Postcode == patient.getPostcode() && Geboortedatum == patient.getGeboortedatum() && VerblijftSinds == patient.getVerblijftSinds() && Kamer == patient.getKamer()){
            Log.i("Unit Test ", "Passed");
        } else {
            Log.e("Unit Test ", "Failed");
        }

    }

}
