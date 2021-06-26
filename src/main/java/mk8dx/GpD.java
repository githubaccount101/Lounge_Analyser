
package mk8dx;

import mkw.Race;
import shared.Format;

import java.util.ArrayList;

public class GpD {
    private int maxRaces;
    private int racesPlayedInGp = 0;
    private ArrayList<RaceD> races = new ArrayList();
    private EventD event;
    private int gpId;
    private String id;

    private int racePoints;
    private int dcPts = 0;
    private int totalPoints = 0;

    private boolean standby = false;

    public GpD(EventD event, int gpId){
        this.event = event;
        this.gpId=gpId;
        this.id = this.event.getEventId()+"-"+this.gpId;
        this.maxRaces = 12;
    }

    public GpD(EventD event, int gpId, int max) {
        this(event,gpId);
        this.maxRaces = max;
    }

    public void playRace(RaceD r){
        System.out.println("");
        System.out.println("adding the following race to GP records");
        System.out.println(r);
        this.races.add(r);
        this.addScores();
        this.racesPlayedInGp++;
    }

    public int undoLastRace(){
        if(getMostRecentRace().isPlaceholder()){
            this.dcPts--;
        }
        int removed = getMostRecentRace().getPoints();
        this.racePoints-=removed;
        this.racesPlayedInGp--;
        races.remove(racesPlayedInGp);
        return removed;
    }

    public boolean isInDcStandby2(boolean rejoined){
        if(this.isUnplayed()){
            return false;
        }
        if(rejoined){
            return false;
        }
        boolean isDc = this.getMostRecentRace().isPlaceholder();
        if(isDc){
            return true;
        }else{
            return false;
        }
    }

    public boolean twoRaceCheck(){
        if(races.size()<2){
            return false;
        }
        if(races.get(races.size()-2).isPlaceholder()){
            return true;
        }
        return false;
    }

    public void clearDcPts(){
        this.dcPts = 0;
    }

    public void addScores() {
        int sum = 0;
        for(RaceD race:races){
            sum+=race.getPoints();
        }
        racePoints = sum;
    }

    public int getRacePoints(){
        return racePoints;
    }

    public int getDcPts(){
        return dcPts;
    }

    public void updateTotalPoints() {
        this.totalPoints = this.racePoints + this.dcPts;
    }

    public boolean newGpRequired(){
        return this.racesPlayedInGp ==this.maxRaces;
    }

    public boolean isUnplayed(){
        return this.racesPlayedInGp == 0;
    }

    public void setStandby(boolean standby) {
        this.standby = standby;
    }


    public int getDcCompensation() {
        System.out.println("you missed " + 1 + " race so you get "
                + 1 + " point in compensation");
        dcPts+=1;
        return 1;
    }

    public RaceD getMostRecentRace(){
        return races.get(racesPlayedInGp-1);
    }

    public int getRemainingRacesInGp(){
        return maxRaces-racesPlayedInGp;
    }

    public int getGpId() {
        return gpId;
    }

    public TierD getTier(){
        return event.getTier();
    }

    public Format getFormat(){
        return event.getFormat();
    }

    public int getIdofRaceAboutToBePlayed(){
        return racesPlayedInGp+1;
    }

    public ArrayList<RaceD> getRaces() {
        return races;
    }

    public int getMissedRacesFromdc() {
        int count = 0;
        for(RaceD r:races){
            if(r.isPlaceholder()){
                count++;
            }
        }
        return count;
    }

    public boolean mostRecentRaceWasMissed(){
        return this.getMostRecentRace().isPlaceholder();
    }

    public void setDcPts(int dcPts) {
        this.dcPts = dcPts;
    }

    public boolean nodc(){
        boolean nodc = true;
        for(RaceD r : getRaces()){
            if(r.isPlaceholder()){
                nodc = false;
            }
        }
        return nodc;
    }

    public String toString(){
        return "GP Status:: Event: "+this.event.getEventId()+", GP: "+gpId+", Races-Played: "+racesPlayedInGp+", Points: "+racePoints+", DC Points: "+dcPts;
    }

    public void printRaces(){
        System.out.println("~~[GP "+this.gpId+"]~~ Races-Played: "+racesPlayedInGp+", Points: "+racePoints+", DC Points: "+dcPts);
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        for(RaceD race:races){
            System.out.println(race);
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }

    public String getGpString(){
        StringBuilder bob = new StringBuilder();
        bob.append("~~[GP "+this.gpId+"]~~ Races-Played: "+racesPlayedInGp+", Total Points: "+totalPoints+", Points: "+racePoints+", DC Points: "+dcPts+"\n")
                .append("-----------------------------------------------------------------------------------------------------------"+"\n");
        for(RaceD r:races){
            bob.append(r.toString()+"\n");
        }
        bob.append("-----------------------------------------------------------------------------------------------------------"+"\n");
        return bob.toString();
    }

    @Deprecated
    public boolean isInDcStandby() {
        System.out.println("IS THIS EVER SEEEEEN??????????????????????????????????");
        //if the gp is unplayed return false
        if (this.isUnplayed()) {
            System.out.println("1");
            return false;
        }
        //if the most recently played race is a normal race, return false;
        if(this.getMostRecentRace().isPlaceholder()==false){
            System.out.println("2");
            return false;
        }
        //if the most recently played race was a placeholder, but we have rejoined,
        //return false;
        if(event.outOfStandby){
            System.out.println("3");
            return false;
        }
        //if the most recently played race was a placeholder and we just removed a race that was a placeholder
        if(this.getMostRecentRace().isPlaceholder()){
            System.out.println("4");
            return true;
        }
        if (!this.newGpRequired() && this.getMostRecentRace().isPlaceholder()) {

            System.out.println("5");return true;
        }
        System.out.println("6");
        return false;
    }

}
