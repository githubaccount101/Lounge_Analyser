package Ui;

import mkw.*;
import mk8dx.*;
import mkw.Event;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.util.ShapeUtils;
import shared.Format;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;



public class RaceDao {

    private static final String dbPath = getDirectory();
    private static final String dbName = "loungedb.db";
    private static final String url = "jdbc:sqlite:"+dbPath+File.separator+dbName;

    private static final ArrayList<Event> events = new ArrayList<>();
    private static final ArrayList<Event> recentEvents = new ArrayList<>();
    private static final ArrayList<EventD> eventsD = new ArrayList<>();
    private static final ArrayList<EventD> recentEventsD= new ArrayList<>();

    public static void setUp(){
        createDb();
        createTables();
        if(defautTiersAreEmpty()){
            insertDefaultTiers();
        }
    }

    private static String getDirectory(){
        String path = System.getProperty("user.home") + File.separator + "Documents";
        path = path+File.separator+"LoungeData";
        return path;
    }

    private static Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void createDb(){
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

    private static void createNewDatabase(String directory, String fileName) {

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

    private static void createTables() {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS races (raceid int, eventid int, gpid REAL, tier int, format varchar(10)," +
                " track varchar(50), start int, finish int, players int, points int, satout boolean)";
        String sql3 = "CREATE TABLE IF NOT EXISTS racesD (raceid int, eventid int, tier varchar(10), format varchar(10), " +
                "track varchar(50), start int, finish int, points int, satout boolean)";
        String sql5 = "CREATE TABLE IF NOT EXISTS events (eventid int, tier int, format varchar(10),points int, nodc boolean)";
        String sql7 = "CREATE TABLE IF NOT EXISTS eventsD (eventid int,tier varchar(10), format varchar(10),points int, nodc boolean)";
        String sql9 = "CREATE TABLE IF NOT EXISTS gps (gpid REAL,tier int, format varchar(10),points int, nodc boolean, fullgp boolean)";
        String sql11 = "CREATE TABLE IF NOT EXISTS defaultTiers (mkw int, mk8dx varchar(10), randomized boolean, fc varchar(14))";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            stmt.execute(sql3);
            stmt.execute(sql5);
            stmt.execute(sql7);
            stmt.execute(sql9);
            stmt.execute(sql11);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void resetTables(){

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

    public static void removeLastestMkwEvent(){
        int eventid = getEventsStored();

        String sql = "Delete FROM events \n" +
                "Where eventid = "+eventid;
        String sql2 = "Delete FROM gps\n" +
                "Where gpid >= "+eventid;
        String sql3 = "Delete FROM races\n" +
                "Where eventid = "+eventid;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            stmt.execute(sql2);
            stmt.execute(sql3);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeLastestMk8dxEvent(){
        int eventid = getEventsDStored();

        String sql = "Delete FROM eventsD \n" +
                "Where eventid = "+eventid;
        String sql3 = "Delete FROM racesD\n" +
                "Where eventid = "+eventid;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            stmt.execute(sql3);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean defautTiersAreEmpty(){

        String sql = "Select Count(mkw) FROM defaultTiers";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            int rows = 0;

            while (rs.next()){
                rows = rs.getInt(1);
            }
            if(rows == 0){
               return true;
            }
            System.out.println("the one needed row is already here");
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private  static  void insertDefaultTiers(){
        String sql = "INSERT INTO defaultTiers (mkw,mk8dx,randomized, fc) VALUES (?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, 1);
            pstmt.setString(2,"f");
            pstmt.setBoolean(3,false);
            pstmt.setString(4,"nope");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateDefaultTierMkw(int tier){
        String sql = "UPDATE defaultTiers SET mkw = ? ";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tier);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updatefc(String fc){
        String sql = "UPDATE defaultTiers SET fc = ? ";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void updateDefaultTierMk8dx(String tier){
        String sql = "UPDATE defaultTiers SET mk8dx = ? ";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tier);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getFc(){
        String sql = "SELECT fc       \n" +
                "  FROM defaultTiers";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            String fc = rs.getString("fc");
            return fc;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return "";
        }
    }

    public static int getDefaultMkwTier(){
        String sql = "SELECT mkw       \n" +
                "  FROM defaultTiers";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            int tier = rs.getInt("mkw");
            return tier;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return 8;
        }
    }

    public static String getDefaultMk8dxTier(){
        String sql = "SELECT mk8dx       \n" +
                "  FROM defaultTiers";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            String tier = rs.getString("mk8dx");
            return tier;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return "";
        }
    }

    public static boolean isRandom(){
        String sql = "SELECT randomized       \n" +
                "  FROM defaultTiers";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            boolean random = rs.getBoolean("randomized");
            return random;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void updateRandom(Boolean onOff){
        String sql = "UPDATE defaultTiers SET randomized = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if(onOff){
                pstmt.setBoolean(1, true);
            }else{
                pstmt.setBoolean(1, false);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public static void insert(int raceid, int eventid, double gpid, int tier, String format, String track, int start, int finish, int players, int points, boolean satout) {
        String sql = "INSERT INTO races(raceid, eventid, gpid, tier, format, track, start, finish, players, points, satout) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect();
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

    public static void insertD(int raceid, int eventid, String tier, String format, String track, int start, int finish, int points, boolean satout) {
        String sql = "INSERT INTO racesD(raceid, eventid, tier, format, track, start, finish, points, satout) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect();
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


    public static void insertEvent(int eventId, int tier, String format,int points, boolean nodc){
        String sql = "INSERT INTO events (eventId, tier, format, points, nodc) VALUES(?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setInt(2, tier);
            pstmt.setString(3, format);
            pstmt.setInt(4, points);
            pstmt.setBoolean(5,nodc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertEventD(int eventId, String tier, String format,int points, boolean nodc){
        String sql = "INSERT INTO eventsD (eventId, tier, format,points, nodc) VALUES(?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setString(2, tier);
            pstmt.setString(3, format);
            pstmt.setInt(4, points);
            pstmt.setBoolean(5,nodc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertGp(double gpId, int tier, String format,int points, boolean nodc, boolean fullGp){
        String sql = "INSERT INTO gps (gpid, tier, format,points, nodc , fullgp) VALUES(?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, gpId);
            pstmt.setInt(2, tier);
            pstmt.setString(3, format);
            pstmt.setInt(4, points);
            pstmt.setBoolean(5,nodc);
            pstmt.setBoolean(6,fullGp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void storeEvent(){
        if(recentEvents.isEmpty() == false){
            for (Event e : recentEvents) {
                insertEvent(e.getEventId(), Integer.parseInt(e.getTier().getTier()),e.getFormat().getFormat(),e.getFinalPoints(), e.nodc());
                for (Gp gp : e.getCompletedGps()) {
                    insertGp(gp.getId(),Integer.parseInt(gp.getTier().getTier()), gp.getFormat().getFormat(), gp.getTotalPoints(), gp.nodc(), gp.fourRacesPlayed());
                    for (Race r : gp.getRaces()) {
                        storeRace(r);
                    }
                }
            }
        }else{
            System.out.println("no recent mkw events");
        }

    }

    private static void storeRace(Race r){
        if(r.isPlaceholder()){
            insert(r.getRaceId(),r.getEventId(),r.getGpId(),r.getTier(),r.getFormat(),"none",
                    r.getStart(),r.getFinish(),r.getPlayers(), r.getPoints(),r.isPlaceholder());
        }else{
            insert(r.getRaceId(),r.getEventId(),r.getGpId(),r.getTier(),r.getFormat(),r.getTrack().getFullName(),
                    r.getStart(),r.getFinish(),r.getPlayers(), r.getPoints(),r.isPlaceholder());
        }
        System.out.println("storing race: "+r);
    }

    public static void storeEventD(){

        if(recentEventsD.isEmpty() == false){
            for (EventD e : recentEventsD) {
                insertEventD(e.getEventId(), e.getTier().getTier(), e.getFormat().getFormat(),e.getFinalPoints(), e.nodc());
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

    private static void storeRaceD(RaceD r){
        if(r.isPlaceholder()){
            insertD(r.getRaceId(),r.getEventId(),r.getTier(),r.getFormat(),"none",
                    r.getStart(), r.getFinish(),r.getPoints(),r.isPlaceholder());
        }else{
            insertD(r.getRaceId(),r.getEventId(),r.getTier(),r.getFormat(),r.getTrack().getFullName(),
                    r.getStart(), r.getFinish(),r.getPoints(),r.isPlaceholder());
        }
        System.out.println("storing race: "+r);
    }

    public static void generateRandom(int times){
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

                insertGp(gpid, tier, format, gpoints, true,true);
            }

            insertEvent(eventid,tier, format, tpoints,true);


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

            insertEventD(eventid,tierD, format, tpointsD,true);
        }

    }

    public static int getEventsStored(){
        String sql = "Select Count(eventid) FROM events";
        String sql2 = "SELECT * FROM events WHERE eventid = (SELECT MAX(eventid) FROM events)";
        try (Connection conn = connect();
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

    public static int getEventsDStored(){
        String sql = "Select Count(eventid) FROM eventsD";
        String sql2 = "SELECT * FROM eventsD WHERE eventid = (SELECT MAX(eventid) FROM eventsD)";
        try (Connection conn = connect();
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

    public static int getRacesStored(){
        String sql = "Select Count(raceid) FROM races";
        String sql2 ="SELECT * FROM races WHERE raceid = (SELECT MAX(raceid) FROM races)";
        try (Connection conn = connect();
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

    public static int getRacesDStored(){
        String sql = "Select Count(raceid) FROM racesD";
        String sql2 ="SELECT * FROM racesD WHERE raceid = (SELECT MAX(raceid) FROM racesD)";
        try (Connection conn = connect();
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

    public static void add(Event event){
        recentEvents.add(event);
    }

    public static void refresh(){
        for(Event e: recentEvents){
            events.add(e);
        }
        recentEvents.clear();
        for(EventD e: recentEventsD){
            eventsD.add(e);
        }
        recentEvents.clear();
        recentEventsD.clear();
    }

    public static void addD(EventD event) {
        recentEventsD.add(event);
    }

    public static void refreshD() {
        for (EventD e : recentEventsD) {
            eventsD.add(e);
        }
        recentEvents.clear();
    }

    public static void printRecent(){

        System.out.println("mkw events");
        System.out.println("");
        for(Event e: recentEvents){
            System.out.println(e);
            e.printEvent();
        }
        System.out.println("8d events");
        System.out.println("");
        for(EventD e: recentEventsD){
            System.out.println(e);
            e.printEvent();
        }
    }

    public static boolean isPlayed(){
        if(recentEvents.isEmpty()==false||recentEventsD.isEmpty()==false){
            System.out.println("events have recently been played, proceeding to db storge");
            return true;
        }
        System.out.println("no events have been played recently");
        return false;
    }

    public static double getAvgPtsLastX(int lastXEvents){
        String sql = "SELECT avg(points)" +
                "FROM (select * from events order by eventid desc limit "+lastXEvents+")";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            DecimalFormat df = new DecimalFormat("#.#");
            return Double.valueOf(df.format(rs.getDouble("avg(points)")));
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static double getAvgPtsLastXDx(int lastXEvents){
        String sql = "SELECT avg(points)" +
                "FROM (select * from eventsD order by eventid desc limit "+lastXEvents+")";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            DecimalFormat df = new DecimalFormat("#.#");
            return Double.valueOf(df.format(rs.getDouble("avg(points)")));
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static int getRsRows(String sql){
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            int i = 0;
            while(rs.next()){
                i++;
            }
            return i;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static JTable getTrackTableBasic(){
        String sql ="SELECT track AS \"Track\", COUNT(track)AS \"Races Found\", ROUND(avg(finish),1)AS \"AVG Finish\" , Round(avg(points),1) AS \"AVG Points\"" +
                "FROM (select * from races order by raceid desc limit 1)\n" +
                "Where track like 'ppop'\n" +
                "group by track \n" +
                "ORDER BY avg(finish) desc";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            JTable table = new JTable(buildTableModel(rs));
            table.setAutoCreateRowSorter(true);
            return table;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static JTable getTierTableBasic(){
        String sql ="SELECT tier AS \"Tier\", COUNT(tier)AS \"Races Found\", ROUND(avg(finish),1)AS \"AVG Finish\" , Round(avg(points),1) AS \"AVG Points\"" +
                "FROM (select * from races order by raceid desc limit 1)\n" +
                "Where tier = 1\n" +
                "group by tier\n" +
                "ORDER BY avg(points) desc";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            JTable table = new JTable(buildTableModel(rs));
            table.setAutoCreateRowSorter(true);
            return table;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static JTable getFormatTableBasic(){
        String sql ="SELECT format AS \"Format\", COUNT(format)AS \"Races Found\", ROUND(avg(finish),1)AS \"AVG Finish\" , Round(avg(points),1) AS \"AVG Points\"" +
                "FROM (select * from races order by raceid desc limit 1)\n" +
                "Where track like 'ppop'\n" +
                "group by format \n" +
                "ORDER BY avg(points) desc";
        System.out.println(sql);
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            JTable table = new JTable(buildTableModel(rs));
            table.setAutoCreateRowSorter(true);
            return table;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static JTable getStartTableBasic(){
        String sql = "SELECT start AS \"Starting Position\", COUNT(track)AS \"Races Found\", ROUND(avg(finish),1)AS \"AVG Finish\" , Round(avg(points),1) AS \"AVG Points\""+
                "FROM (select * from races order by raceid desc limit 1)\n" +
                "Where track like 'ppop'\n" +
                "group by start \n" +
                "ORDER BY avg(points) desc";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            JTable table = new JTable(buildTableModel(rs));
            table.setAutoCreateRowSorter(true);
            return table;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static JTable getSetTable(String sql){
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            JTable table = new JTable(buildTableModel(rs));
            table.setAutoCreateRowSorter(true);
            return table;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }



    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        DefaultTableModel table = new DefaultTableModel(data, columnNames);
        return table;
    }

    public static XYSeries getSeries(String sql){
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            var series = new XYSeries("Total Event Score");

            ArrayList<Integer> holder = new ArrayList<>();

            while(rs.next()){
                int points = rs.getInt("points");
                holder.add(points);
            }

            ArrayList<Integer> holder2 = new ArrayList<>();
            for(int i=holder.size()-1;i>=0;i--){
                holder2.add(holder.get(i));
            }

            int start = 1;
            for(int a:holder2){
                series.add(start,a);
                start++;
            }
            return series;

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static XYSeries getSeries4Track(String sql){
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            var series = new XYSeries("Race Finish Position");

            ArrayList<Integer> holder = new ArrayList<>();

            while(rs.next()){
                int points = rs.getInt(1);
                holder.add(points);
            }

            ArrayList<Integer> holder2 = new ArrayList<>();
            for(int i=holder.size()-1;i>=0;i--){
                holder2.add(holder.get(i));
            }

            int start = 1;
            for(int a:holder2){
                series.add(start,a);
                start++;
            }
            return series;


        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static XYSeries getSeriesMovingAverage(String sql){
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            var series = new XYSeries("Moving Average");

            ArrayList<Integer> holder = new ArrayList<>();

            while(rs.next()){
                int points = rs.getInt(1);
                holder.add(points);
            }

            ArrayList<Integer> holder2 = new ArrayList<>();
            for(int i=holder.size()-1;i>=0;i--){
                holder2.add(holder.get(i));
            }

            ArrayList<Double> movingAvg = movingAvgTransformation(holder2);

            int start = 1;
            for(double a:movingAvg){
                series.add(start,a);
                start++;
            }
            return series;


        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }



    public static ArrayList<Double> movingAvgTransformation(ArrayList<Integer> original){
        int divisor = original.size()/4+1;
        System.out.println("divisor: "+divisor);

        ArrayList<Double> neo = new ArrayList<>();

        int i = 0;
        for(int a:original){
            double output;
            //if i is small enough
            if((i+divisor)<=(original.size()-1)){
                int sum = 0;
                int index=0;
                int summedDivisor = 0;

                for(int n = divisor; n>0;n--){
                    sum+=original.get(i+index)*n;
                    index++;
                    summedDivisor+=n;
                }
                output = 1.0*sum/(1.0*summedDivisor);
                System.out.println(a+" is now "+output);
                neo.add(output);
            }else if((original.size()-1)-i==0){
                output=a;
                System.out.println(a+" is now "+output);
                neo.add(output);
            } else{//if we are too close to the end of the list
                int newDivisor = (original.size()-1)-i;
                int sum = 0;
                int index=0;
                int summedDivisor = 0;

                for(int n = newDivisor; n>0;n--){
                    sum+=original.get(i+index)*n;
                    index++;
                    summedDivisor+=n;
                }
                output = 1.0*sum/(1.0*summedDivisor);
                System.out.println(a+" is now "+output);
                neo.add(output);
            }
            i++;
        }
        return neo;
    }

    public static XYSeriesCollection getDataset(XYSeries series){
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    public static XYSeriesCollection getDataset4Track(XYSeries series, XYSeries mA){
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(mA);
        return dataset;
    }

    public static JFreeChart getChart(XYSeriesCollection dataset){
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Applicable Events",
                "Event Recency",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setInverted(true);
        plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        var renderer = new XYSplineRenderer(8);

        renderer.setSeriesShapesVisible(1, false);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesStroke(0, new BasicStroke(0.3f));

        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));

        Rectangle rect = new Rectangle();
        Dimension dd = new Dimension();
        dd.setSize(6, 6); rect.setSize(dd);
        renderer.setSeriesShape(0, rect);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.lightGray);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[] {6.0f, 6.0f}, 0.0f
                )
        );

        chart.setTitle(new TextTitle("",
                        new Font("Comic Sans", java.awt.Font.BOLD, 16)
                )
        );
        return chart;
    }

    public static JFreeChart getChart4Tracks(XYSeriesCollection dataset){
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Applicable Events",
                "Race Recency",
                "Finish Position",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setInverted(true);
        plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getRangeAxis().setInverted(true);
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getRangeAxis().setRange(-2, 14);

        var renderer = new XYSplineRenderer(8);

        renderer.setSeriesShapesVisible(1, false);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesStroke(0, new BasicStroke(0.3f));

        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));

        Rectangle rect = new Rectangle();
        Dimension dd = new Dimension();
        dd.setSize(6, 6); rect.setSize(dd);
        renderer.setSeriesShape(0, rect);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.lightGray);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[] {6.0f, 6.0f}, 0.0f
                )
        );

        chart.setTitle(new TextTitle("",
                        new Font("Comic Sans", java.awt.Font.BOLD, 16)
                )
        );
        return chart;
    }

    public static ChartPanel getChartPanel( JFreeChart chart){
        ChartPanel chartPanel=  new ChartPanel(chart){
            @Override
            public Dimension getPreferredSize() {
                // given some values of w & h
                return new Dimension(450, 200);
            }
        };
        return chartPanel;
    }


    public static void main(String[] args) {

    }

    @Deprecated
    public static ChartPanel getChartPanel(String sql){
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            var series = new XYSeries("???");
            int x = 1;
            while(rs.next()){
                int points = rs.getInt("points");
                System.out.println(points);
                series.add(x,points);
                x++;
            }

            var dataset = new XYSeriesCollection();
            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Applicable Events",
                    "events",
                    "y",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    false,
                    false
            );

            chart.removeLegend();

            ChartPanel chartPanel=  new ChartPanel(chart);
            return chartPanel;

        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Deprecated
    public static ArrayList<Double> movingAvgTransformationOld(ArrayList<Integer> original){
        int divisor = original.size()/5+1;
        ArrayList<Double> neo = new ArrayList<>();
        int i = 0;

        System.out.println("--------------");
        System.out.println("divisor is: "+divisor);
        System.out.println("transforming");
        System.out.println("--------------");
        for(int a:original){
            boolean smallEnough = (i+divisor)<original.size();
            double newElement;

            if(smallEnough){
                int tempDivisor =divisor+1;
                int weightDivisor = 0;

                int sum = original.get(i)*tempDivisor;
                weightDivisor +=tempDivisor;

                for(int n = 0;n<divisor;n++){
                    tempDivisor--;
                    sum+=original.get(i+n+1)*(tempDivisor);
                    weightDivisor +=tempDivisor;

                }
                newElement = (1.0*(sum))/((1.0)*(weightDivisor));

            }else if(original.size()-i-1>0){
                int tempDivisor = original.size()-i-1;
                int weightDivisor = 0;

                int sum = original.get(i)*tempDivisor;
                weightDivisor +=tempDivisor;

                for(int n = 0;n<tempDivisor;n++){
                    tempDivisor--;
                    sum+=original.get(i+n+1)*(tempDivisor);
                    weightDivisor +=tempDivisor;
                }
                newElement = (1.0*(sum))/((1.0)*(weightDivisor));

            }else{
                newElement = 1.0*original.get(i);

            }
            neo.add(newElement);
            i++;
        }
        return neo;
    }

}
