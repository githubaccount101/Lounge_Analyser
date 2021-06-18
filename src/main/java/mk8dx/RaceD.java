
package mk8dx;

import mkw.Race;
import shared.Format;

public class RaceD {

    private int raceId;
    private EventD event;
    private GpD gp;
    private int race;
    private TrackD track;
    private int start;
    private int finish;
    private int points;

    private int players= 12;
    private boolean satOut= false;
    private TierD tier;
    private Format format;


    private final static int[] p12 = {15,12, 10, 9,8, 7, 6, 5, 4, 3, 2, 1};
    private static int dcCount;
    private static int raceCount = 0;

    public RaceD(EventD event, GpD gp, int race, TrackD track, int start, int finish) {
        this.event = event;
        this.gp = gp;
        this.race = race;
        this.track = track;
        this.start = start;
        this.finish = finish;
        this.players = 12;
        allocatePoints();

        this.tier = event.getTier();
        this.format= event.getFormat();

        raceCount++;
        this.raceId = raceCount;
    }

    public RaceD(EventD event, GpD gp,int race) {
        this.event = event;
        this.gp = gp;
        this.race = race;
        this.track = null;
        this.start = 0;
        this.finish = 0;
        this.players = 0;
        this.points = 0;
        this.satOut = true;

        this.tier = event.getTier();
        this.format = event.getFormat();

        raceCount++;
        this.raceId = raceCount;
    }

    public static void setRaceCount(int raceCount) {
        RaceD.raceCount = raceCount;
    }

    public static int getRaceCount() {
        return raceCount;
    }

    private void allocatePoints(){
        this.points = p12[this.finish-1];
    }

    public EventD getEvent() {
        return event;
    }

    public int getRace() {
        return race;
    }

    public TrackD getTrack() {
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

    public int getRaceId(){
        return this.raceId;
    }

    public int getEventId(){
        return this.event.getEventId();
    }

    public String getTier(){
        return this.event.getTier().getTier();
    }

    public String getFormat(){
        return this.event.getFormat().getFormat();
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
                    + "points=" + points + ", satOut=" + satOut + '}'+" raceid: "+raceId;
        }
        return "[Event"+event.getEventId()+"] Race:" + race + ", track=" + track.getFullName() + ", "
                + "start=" + start + ", finish=" + finish + ", players=" + players + ", "
                + "points=" + points + ", satOut=" + satOut + '}'+" raceid: "+raceId;
    }



}
