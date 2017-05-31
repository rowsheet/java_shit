package com.UserEvents.Meetups;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by alexanderkleinhans on 5/30/17.
 */
public class MeetupModel {
    public int createMeetup(
            String cookie,
            String start_time,
            String weekday,
            String name,
            String description,
            String[] categories
    ) throws Exception {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/skiphopp",
                            "alexanderkleinhans", "");
            System.out.println("Opened database successfully");

        } catch ( Exception e ) {
            System.out.println("ERROR:");
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
        return 0;
    }
}
