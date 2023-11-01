package com.example.rollator.db;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabasePostgreSqlConnection {
    Connection con;
    //Creating strings to use in the connection
    String user, pass, host, port, db;

    public Connection connectionClass() {
        //Defining strings to use in the connection with the postgreSQL database
        host = "";
        db = "";
        port = "";
        user = "";
        pass = "";
        //Capturing accidental network access on the main ui thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionUrl = null;

        try {
            //Calling the imported postgreSQL driver jar
            Class.forName("org.postgresql.Driver");
            //Connection link
            ConnectionUrl = "jdbc:postgresql://" + host + ":" + port + "/" + db;
            //Connecting to database using the driver manager
            connection = DriverManager.getConnection(ConnectionUrl, user, pass);
            //Logging connection
            Log.e("Database:", "Connectie");

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        //Returning the connection so i can reuse it with every function
        return connection;
    }
}
