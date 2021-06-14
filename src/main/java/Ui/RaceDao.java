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
        String sql = "CREATE TABLE IF NOT EXISTS races (raceid int, eventid int, gpid varchar(10), tier int, format varchar(10), track varchar(50), start int, finish int, players int, points int, satout boolean)";
        String sql2 = "DELETE FROM races";
        String sql3 = "CREATE TABLE IF NOT EXISTS racesD (raceid int, eventid int, tier varchar(10), format varchar(10), track varchar(50), start int, finish int, points int, satout boolean)";
        String sql4 = "DELETE FROM racesD";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(int raceid, int eventid, String gpid, int tier, String format, String track, int start, int finish, int players, int points, boolean satout) {
        String sql = "INSERT INTO races(raceid, eventid, gpid, tier, format, track, start, finish, players, points, satout) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, raceid);
            pstmt.setInt(2, eventid);
            pstmt.setString(3, gpid);
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

    public void insertD(int raceid, int eventid, String tier, String format, String track, int start, int finish, int points, boolean satout) {
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

    public void storeEvent(){
        if(recentEvents.isEmpty() == false){
            for (Event e : this.recentEvents) {
                for (Gp gp : e.getCompletedGps()) {
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

    public void generate100(){
        int[] p12 = {15,12, 10, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] p12d = {15,12, 10, 9,8, 7, 6, 5, 4, 3, 2, 1};
        Random rng= new Random();

        int raceid = 1;
        int raceidD= 1;

        for(int i = 1;i<=100;i++){
            int eventid = i;
            String format = Format.randomFormat().getFormat();
            int tier = Integer.parseInt(Tier.randomTier().getTier());
            String tierD = TierD.randomTierD().getTier();
            boolean satout = false;

            for(int n = 1; n<=3; n++){
                for(int x = 1; x<=4;x++){
                    String gpid = String.valueOf(i)+"-"+String.valueOf(n);
                    String track = Track.randomTrack().getFullName();
                    int start = rng.nextInt(12)+1;
                    int finish = rng.nextInt(12)+1;
                    int players = 12;
                    int points = p12[finish-1];

                    insert(raceid, eventid, gpid, tier, format, track, start, finish, players, points, satout);
                    raceid++;
                }
            }
            for(int k=1;k<=12;k++){

                String track = TrackD.randomTrackD().getFullName();
                int start = rng.nextInt(12)+1;
                int finish = rng.nextInt(12)+1;
                int points = p12d[finish-1];

                insertD(raceidD, eventid, tierD, format, track, start, finish, points, satout);
                raceidD++;
            }

        }

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
