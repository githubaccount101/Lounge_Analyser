
package mkw;

import java.util.HashMap;

public class Race {

    private int raceId;
    private Event event;
    private Gp gp;
    private int race;
    private Track track;
    private int start;
    private int finish;
    private int players;
    private int points;
    private boolean satOut;

    private final static int[] p12 = {15,12, 10, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    private final static int[] p11 = {15,12, 10, 8, 6, 5, 4, 3, 2, 1, 0};
    private final static int[] p10 = {15,12, 10, 8, 6, 4, 3, 2, 1, 0};
    private static int raceCount = 0;


    public Race(Event event, Gp gp, int race, Track track, int start, int finish, int players) {
        this.event = event;
        this.gp = gp;
        this.race = race;
        this.track = track;
        this.start = start;
        this.finish = finish;
        this.players = players;
        allocatePoints();
        this.satOut = false;
        raceCount++;
        this.raceId = raceCount;
    }

    public Race(Event event, Gp gp,int race) {
        this.event = event;
        this.gp = gp;
        this.race = race;
        this.track = null;
        this.start = 0;
        this.finish = 0;
        this.players = 0;
        this.points = 0;
        this.satOut = true;
        raceCount++;
        this.raceId = raceCount;
    }

    private void allocatePoints(){
        if(this.players == 12){
            this.points = p12[this.finish-1];
        }
        if(this.players == 11){
            this.points = p11[this.finish-1];
        }
        if(this.players == 10){
            this.points = p10[this.finish-1];
        }
    }

    public Event getEvent() {
        return event;
    }

    public int getRace() {
        return race;
    }

    public Track getTrack() {
        return track;
    }

    public int getStart() {
        return start;
    }

    public int getFinish() {
        return finish;
    }

    public int getPlayers() {
        return players;
    }

    public int getPoints() {
        return points;
    }

    public int getRaceId() {
        return raceId;
    }

    public double getGpId(){
        return this.gp.getId();
    }

    public int getEventId(){
        return this.event.getEventId();
    }

    public int getTier(){
        return Integer.parseInt(this.event.getTier().getTier());
    }

    public String getFormat(){
        return this.event.getFormat().getFormat();
    }

    public static void setRaceCount(int raceCount) {
        Race.raceCount = raceCount;
    }

    public boolean isPlaceholder(){
        if(this.satOut==true){
            return true;
        }
        return false;
    }



    @Override
    public String toString() {
        if(track == null){
            return "[Event"+event.getEventId()+"] Race:" + race + ", track=" + track+ ", "
                    + "start=" + start + ", finish=" + finish + ", players=" + players + ", "
                    + "points=" + points + ", satOut=" + satOut + '}';
        }
        return "[Event"+event.getEventId()+"] Race:" + race + ", track=" + track.getFullName() + ", "
                + "start=" + start + ", finish=" + finish + ", players=" + players + ", "
                + "points=" + points + ", satOut=" + satOut + '}';
    }

    public String getInsert(){
        StringBuilder bob = new StringBuilder();
        bob.append("insert into races values(");
        bob.append(this.raceId).append(",'")
                .append(gp.getId()).append("',")
                .append(event.getEventId()).append(",")
                .append(event.getTier().getTier()).append(",'")
                .append(event.getFormat().getFormat()).append("','")
                .append(track.getFullName()).append("',")
                .append(players).append(",")
                .append(this.start).append(",")
                .append(this.finish).append(",")
                .append(this.points).append(",")
                .append(this.satOut).append(")")
                .append(";");
        return bob.toString();
    }
}
