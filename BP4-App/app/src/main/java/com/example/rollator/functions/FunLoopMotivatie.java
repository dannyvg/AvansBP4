package com.example.rollator.functions;

import android.util.Log;

import com.example.rollator.db.DatabasePostgreSqlConnection;
import com.example.rollator.model.LoopMotivatie;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatConversionException;
import java.util.List;
import java.util.Map;

public class FunLoopMotivatie {

    static Connection connect;

    public static void insertLoopMotivatie (String kamer){
        Timestamp start = null;
        Timestamp stop = null;
        int stappen = 0;

        //Creating a patient from the model
        LoopMotivatie loop = new LoopMotivatie(kamer, stappen, start, stop);

        try {
            DatabasePostgreSqlConnection db = new DatabasePostgreSqlConnection();
            //connecting to the database
            connect = db.connectionClass();
            //check if there is a connection
            if (connect != null) {
                Statement st = connect.createStatement();
                //query
                ResultSet rs = st.executeQuery("insert into loopmotivaties (kamer) values ('" + loop.getKamer() + "');");
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

    public static void updateLoopmotivatie(String kamer, int stappen){
        Timestamp start = null;
        Date date = new Date();
        Timestamp stop = new Timestamp(date.getTime());

        //Creating a patient from the model
        LoopMotivatie loop = new LoopMotivatie(kamer, stappen, start, stop);

        try {
            DatabasePostgreSqlConnection db = new DatabasePostgreSqlConnection();
            //connecting to the database
            connect = db.connectionClass();
            //check if there is a connection
            if (connect != null) {
                Statement st = connect.createStatement();
                //query
                ResultSet rs = st.executeQuery("update loopmotivaties set wandelingstop = '"+ stop +"', stappen =  '"+ stappen +"' where kamer = '"+ kamer +"' ORDER BY wandelingstart desc limit 1 ;");
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

    public List<Map<String, String>> getListDone() {
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
                ResultSet rs = st.executeQuery("select kamer, stappen, DATE_TRUNC('second', wandelingstart) as wandelingstart, DATE_TRUNC('second', wandelingstop) as wandelingstop from loopmotivaties where wandelingstop is not null");
                while (rs.next()) {
                    //while there is data comng through the function adds the data to a package to use in the listview
                    Map<String, String> wndlDone = new HashMap<String, String>();
                    wndlDone.put("kamer", rs.getString("kamer"));
                    wndlDone.put("stappen", rs.getString("stappen"));
                    wndlDone.put("start", rs.getString("wandelingstart"));
                    wndlDone.put("stop", rs.getString("wandelingstop"));
                    data.add(wndlDone);
                }
                rs.close();
                st.close();
                connect.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //returning the package
        return data;
    }

    public List<Map<String, String>> getListProgress() {
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
                ResultSet rs = st.executeQuery("select kamer, DATE_TRUNC('second', wandelingstart) as wandelingstart from loopmotivaties where wandelingstop is null");
                while (rs.next()) {
                    //while there is data comng through the function adds the data to a package to use in the listview
                    Map<String, String> wndlDone = new HashMap<String, String>();
                    wndlDone.put("kamer", rs.getString("kamer"));
                    wndlDone.put("start", rs.getString("wandelingstart"));
                    data.add(wndlDone);
                }
                rs.close();
                st.close();
                connect.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //returning the package
        return data;
    }

}
