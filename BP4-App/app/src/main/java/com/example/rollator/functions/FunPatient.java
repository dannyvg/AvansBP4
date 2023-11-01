package com.example.rollator.functions;

import android.util.Log;

import java.sql.Connection;
import com.example.rollator.db.DatabasePostgreSqlConnection;
import com.example.rollator.model.Patient;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatConversionException;
import java.util.List;
import java.util.Map;

public class FunPatient {

    static Connection connect;

    public static void insertPatient (String bejaardentehuis, String naam, String telefoon, String adres, String postcode, Date geboortedatum, Date verblijftSinds, String kamer){
        //Creating a patient from the model
        Patient patient = new Patient(bejaardentehuis,naam, telefoon, adres, postcode, geboortedatum, verblijftSinds, kamer);

        try {
            DatabasePostgreSqlConnection db = new DatabasePostgreSqlConnection();
            //connecting to the database
            connect = db.connectionClass();
            //check if there is a connection
            if (connect != null) {
                Statement st = connect.createStatement();
                //query
                ResultSet rs = st.executeQuery("insert into patienten (bejaardentehuis, naam, geboortedatum, kamer, telefoon, adres, postcode) values ('" + patient.getBejaardentehuis() + "', '" + patient.getNaam() + "', '" + patient.getGeboortedatum() + "', '" + patient.getKamer() + "','" + patient.getTelefoon() + "', '" + patient.getAdres() + "', '" + patient.getPostcode() + "');");
                rs.close();
                st.close();
                connect.close();
            }
        } catch (IllegalFormatConversionException Ie){
            Log.e("Error", "" + Ie.getMessage());
        }
        catch (SQLException s){
            Log.e("Error", "" + s.getMessage());
        }
        catch (Exception e) {
            Log.e("Error", "" + e.getMessage());
        }
    }

    public static void updatePatient(String kamerOld,String kamer, String naam, String telefoon, String adres, String postcode, Date geboortedatum) {
        //creating a patient from the model to use in the function
        String bejaardentehuis = null;
        Date verblijftSinds = null;
        Patient patient = new Patient(bejaardentehuis,naam, telefoon, adres, postcode, geboortedatum, verblijftSinds, kamer);
        try {
            DatabasePostgreSqlConnection db = new DatabasePostgreSqlConnection();
            //connecting to the database
            connect = db.connectionClass();
            //check if there is a connection
            if (connect != null) {
                Statement st = connect.createStatement();
                //query
                ResultSet rs = st.executeQuery("UPDATE patienten SET naam = '" + patient.getNaam() +"', geboortedatum = '" + patient.getGeboortedatum() + "', kamer = '" + patient.getKamer() + "', telefoon = '" + patient.getTelefoon() + "', adres = '" + patient.getAdres() + "', postcode = '" + patient.getPostcode() + "' WHERE kamer = '" + kamerOld + "';");
                rs.close();
                st.close();
                connect.close();
            }
        } catch (IllegalFormatConversionException Ie){
            Log.e("Error", Ie.getMessage());
        }
        catch (SQLException s){
            Log.e("Error", s.getMessage());
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }


    public List<Map<String,String>> getList() {
        //making a new list to put the data in
        List<Map<String, String>> data = null;
        data = new ArrayList<Map<String, String>>();

        try {
            DatabasePostgreSqlConnection db = new DatabasePostgreSqlConnection();
            //connecting to the database
            connect = db.connectionClass();
            //check if there is a connection
            if (connect != null) {
                Statement st = connect.createStatement();
                //query
                ResultSet rs = st.executeQuery("Select * from patienten");
                while (rs.next()) {
                    //while there is data comng through the function adds the data to a package to use in the listview
                    Map<String, String> patienten = new HashMap<String, String>();
                    patienten.put("bejaardentehuis", rs.getString("bejaardentehuis"));
                    patienten.put("naam", rs.getString("naam"));
                    patienten.put("geboortedatum", rs.getString("geboortedatum"));
                    patienten.put("verblijftsinds", rs.getString("verblijftsinds"));
                    patienten.put("kamer", rs.getString("kamer"));
                    patienten.put("telefoon", rs.getString("telefoon"));
                    patienten.put("adres", rs.getString("adres"));
                    patienten.put("postcode", rs.getString("postcode"));
                    data.add(patienten);
                }
                rs.close();
                st.close();
                connect.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Log.e("SLOW","7");
        //returning the package
        return data;
    }

    public void delete(String kamer) {
        try {
            DatabasePostgreSqlConnection db = new DatabasePostgreSqlConnection();
            //connecting to the database
            connect = db.connectionClass();
            //check if there is a connection
            if (connect != null) {
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery("DELETE FROM patienten WHERE kamer = '" + kamer + "'");
                rs.close();
                st.close();
                connect.close();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }
}
