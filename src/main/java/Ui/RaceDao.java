package Ui;

import mkw.*;
import mk8dx.*;
import shared.Format;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class RaceDao {

    private final String dbPath;
    private final String dbName = "loungedb.db";
    private final String url;

    private ArrayList<Event> events;
    private ArrayList<Event> recentEvents;
    private ArrayList<EventD> eventsD;
    private ArrayList<EventD> recentEventsD;



    public RaceDao(){
        this.dbPath = getDirectory();
        this.url = "jdbc:sqlite:"+dbPath+File.separator+dbName;

        this.events = new ArrayList<>();
        this.recentEvents = new ArrayList<>();
        this.eventsD = new ArrayList<>();
        this.recentEventsD = new ArrayList<>();
    }

    public void setUp(){
        createDb();
        createTables();
    }

    private String getDirectory(){
        String path = System.getProperty("user.home") + File.separator + "Documents";
        path = path+File.separator+"LoungeData";
        return path;
    }

    private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void createDb(){
        try{
            if (Files.isDirectory(Paths.get(dbPath))) {
                System.out.println("directory exists for db creation");
                createNewDatabase(dbPath,dbName);
            }else{
                System.out.println("directory in documents does not exist, creating directory for db");
                Files.createDirectories(Paths.get(dbPath));
                createNewDatabase(dbPath, dbName);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());;
        }
    }

    private void createNewDatabase(String directory, String fileName) {

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("creating db: "+fileName);
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTables() {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS races (raceid int, eventid int, gpid REAL, tier int, format varchar(10)," +
                " track varchar(50), start int, finish int, players int, points int, satout boolean)";
        String sql2 = "DELETE FROM races";
        String sql3 = "CREATE TABLE IF NOT EXISTS racesD (raceid int, eventid int, tier varchar(10), format varchar(10), " +
                "track varchar(50), start int, finish int, points int, satout boolean)";
        String sql4 = "DELETE FROM racesD";
        String sql5 = "CREATE TABLE IF NOT EXISTS events (eventid int,points int, nodc boolean)";
        String sql6 = "DELETE FROM events";
        String sql7 = "CREATE TABLE IF NOT EXISTS eventsD (eventid int,points int, nodc boolean)";
        String sql8 = "DELETE FROM eventsD";
        String sql9 = "CREATE TABLE IF NOT EXISTS gps (gpid REAL,points int, nodc boolean, fullgp boolean)";
        String sql10 = "DELETE FROM gps";


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            stmt.execute(sql3);
            stmt.execute(sql5);
            stmt.execute(sql7);
            stmt.execute(sql9);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void resetTables(){

        String sql2 = "DELETE FROM races";
        String sql4 = "DELETE FROM racesD";
        String sql6 = "DELETE FROM events";
        String sql8 = "DELETE FROM eventsD";
        String sql10 = "DELETE FROM gps";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql2);
            stmt.execute(sql4);
            stmt.execute(sql6);
            stmt.execute(sql8);
            stmt.execute(sql10);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insert(int raceid, int eventid, double gpid, int tier, String format, String track, int start, int finish, int players, int points, boolean satout) {
        String sql = "INSERT INTO races(raceid, eventid, gpid, tier, format, track, start, finish, players, points, satout) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, raceid);
            pstmt.setInt(2, eventid);
            pstmt.setDouble(3, gpid);
            pstmt.setInt(4, tier);
            pstmt.setString(5, format);
            pstmt.setString(6, track);
            pstmt.setInt(7, start);
            pstmt.setInt(8, finish);
            pstmt.setInt(9, players);
            pstmt.setInt(10, points);
            pstmt.setBoolean(11, satout);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertD(int raceid, int eventid, String tier, String format, String track, int start, int finish, int points, boolean satout) {
        String sql = "INSERT INTO racesD(raceid, eventid, tier, format, track, start, finish, points, satout) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, raceid);
            pstmt.setInt(2, eventid);
            pstmt.setString(3, tier);
            pstmt.setString(4, format);
            pstmt.setString(5, track);
            pstmt.setInt(6, start);
            pstmt.setInt(7, finish);
            pstmt.setInt(8, points);
            pstmt.setBoolean(9, satout);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertEvent(int eventId, int points, boolean nodc){
        String sql = "INSERT INTO events (eventId, points, nodc) VALUES(?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setInt(2, points);
            pstmt.setBoolean(3,nodc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertEventD(int eventId, int points, boolean nodc){
        String sql = "INSERT INTO eventsD (eventId, points, nodc) VALUES(?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setInt(2, points);
            pstmt.setBoolean(3,nodc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertGp(double gpId, int points, boolean nodc, boolean fullGp){
        String sql = "INSERT INTO gps (gpid, points, nodc , fullgp) VALUES(?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, gpId);
            pstmt.setInt(2, points);
            pstmt.setBoolean(3,nodc);
            pstmt.setBoolean(4,fullGp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void storeEvent(){
        if(recentEvents.isEmpty() == false){
            for (Event e : this.recentEvents) {
                insertEvent(e.getEventId(),e.getFinalPoints(), e.nodc());
                for (Gp gp : e.getCompletedGps()) {
                    insertGp(gp.getId(), gp.getTotalPoints(), gp.nodc(), gp.fourRacesPlayed());
                    for (Race r : gp.getRaces()) {
                        storeRace(r);
                    }
                }
            }
        }else{
            System.out.println("no recent mkw events");
        }

    }

    private void storeRace(Race r){
        if(r.isPlaceholder()){
            insert(r.getRaceId(),r.getEventId(),r.getGpId(),r.getTier(),r.getFormat(),"none",
                    r.getStart(),r.getFinish(),r.getPlayers(), r.getPoints(),r.isPlaceholder());
        }else{
            insert(r.getRaceId(),r.getEventId(),r.getGpId(),r.getTier(),r.getFormat(),r.getTrack().getFullName(),
                    r.getStart(),r.getFinish(),r.getPlayers(), r.getPoints(),r.isPlaceholder());
        }
        System.out.println("storing race: "+r);
    }

    public void storeEventD(){

        if(recentEventsD.isEmpty() == false){
            for (EventD e : recentEventsD) {
                insertEventD(e.getEventId(),e.getFinalPoints(), e.nodc());
                for (GpD gp : e.getCompletedGps()) {
                    for (RaceD r : gp.getRaces()) {
                        storeRaceD(r);
                    }
                }
            }
        }else{
            System.out.println("no recent 8d events");
        }
    }

    private void storeRaceD(RaceD r){
        if(r.isPlaceholder()){
            insertD(r.getRaceId(),r.getEventId(),r.getTier(),r.getFormat(),"none",
                    r.getStart(), r.getFinish(),r.getPoints(),r.isPlaceholder());
        }else{
            insertD(r.getRaceId(),r.getEventId(),r.getTier(),r.getFormat(),r.getTrack().getFullName(),
                    r.getStart(), r.getFinish(),r.getPoints(),r.isPlaceholder());
        }
        System.out.println("storing race: "+r);
    }

    public void generateRandom(int times){
        int[] p12 = {15,12, 10, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] p12d = {15,12, 10, 9,8, 7, 6, 5, 4, 3, 2, 1};
        Random rng= new Random();

        int raceid = 1;
        int raceidD= 1;

        for(int i = 1;i<=times;i++){
            int eventid = i;
            String format = Format.randomFormat().getFormat();
            int tier = Integer.parseInt(Tier.randomTier().getTier());
            String tierD = TierD.randomTierD().getTier();
            boolean satout = false;
            int tpoints = 0;

            for(int n = 1; n<=3; n++){
                int gpoints = 0;
                double gpid = 1.0*n/10+1.0*i;
                for(int x = 1; x<=4;x++){

                    String track = Track.randomTrack().getFullName();
                    int start = rng.nextInt(12)+1;
                    int finish = rng.nextInt(12)+1;
                    int players = 12;
                    int points = p12[finish-1];
                    tpoints+=points;
                    gpoints+=points;

                    insert(raceid, eventid, gpid, tier, format, track, start, finish, players, points, satout);
                    raceid++;
                }
                insertGp(gpid,gpoints,true,true);
            }
            insertEvent(eventid,tpoints,true);

            int tpointsD = 0;
            for(int k=1;k<=12;k++){

                String track = TrackD.randomTrackD().getFullName();
                int start = rng.nextInt(12)+1;
                int finish = rng.nextInt(12)+1;
                int points = p12d[finish-1];
                tpointsD+=points;

                insertD(raceidD, eventid, tierD, format, track, start, finish, points, satout);
                raceidD++;
            }
            insertEventD(eventid,tpointsD,true);
        }

    }

    public int getEventsStored(){
        String sql = "Select Count(eventid) FROM events";
        String sql2 = "SELECT * FROM events WHERE eventid = (SELECT MAX(eventid) FROM events)";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            int events = 0;

            while (rs.next()){
                events = rs.getInt(1);
            }
            if(events == 0){
                return events;
            }

            int idOfLatestEventPlayed = 0;
            ResultSet rs2 = stmt.executeQuery(sql2);
            idOfLatestEventPlayed = rs2.getInt(1);
            return idOfLatestEventPlayed;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public int getEventsDStored(){
        String sql = "Select Count(eventid) FROM eventsD";
        String sql2 = "SELECT * FROM eventsD WHERE eventid = (SELECT MAX(eventid) FROM eventsD)";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            int events = 0;

            while (rs.next()){
                events = rs.getInt(1);
            }
            if(events == 0){
                return events;
            }

            int idOfLatestEventPlayed = 0;
            ResultSet rs2 = stmt.executeQuery(sql2);
            idOfLatestEventPlayed = rs2.getInt(1);
            return idOfLatestEventPlayed;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public int getRacesStored(){
        String sql = "Select Count(raceid) FROM races";
        String sql2 ="SELECT * FROM races WHERE raceid = (SELECT MAX(raceid) FROM races)";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            int count = 0;

            while (rs.next()){
                count = rs.getInt(1);
            }

            if(count == 0){
                return count;
            }
            int idOfLatestRacePlayed = 0;
            ResultSet rs2 = stmt.executeQuery(sql2);
            idOfLatestRacePlayed = rs2.getInt(1);
            return idOfLatestRacePlayed;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public int getRacesDStored(){
        String sql = "Select Count(raceid) FROM racesD";
        String sql2 ="SELECT * FROM races WHERE raceid = (SELECT MAX(raceid) FROM racesD)";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            int count = 0;

            while (rs.next()){
                count = rs.getInt(1);
            }

            if(count == 0){
                return count;
            }
            int idOfLatestRacePlayed = 0;
            ResultSet rs2 = stmt.executeQuery(sql2);
            idOfLatestRacePlayed = rs2.getInt(1);
            return idOfLatestRacePlayed;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public void add(Event event){
        recentEvents.add(event);
    }

    public void refresh(){
        for(Event e: recentEvents){
            events.add(e);
        }
        recentEvents.clear();
    }

    public void addD(EventD event) {
        recentEventsD.add(event);
    }

    public void refreshD() {
        for (EventD e : recentEventsD) {
            eventsD.add(e);
        }
        recentEvents.clear();
    }

    public void printRecent(){
        System.out.println("");
        System.out.println("mkw events");
        System.out.println("");
        for(Event e: recentEvents){
            System.out.println(e);
            e.printEvent();
        }
        System.out.println("");
        System.out.println("8d events");
        System.out.println("");
        for(EventD e: recentEventsD){
            System.out.println(e);
            e.printEvent();
        }
    }

    public boolean isPlayed(){
        if(this.recentEvents.isEmpty()==false||this.recentEventsD.isEmpty()==false){
            System.out.println("events have recently been played, proceeding to db storge");
            return true;
        }
        System.out.println("no events have been played recently");
        return false;
    }

}
