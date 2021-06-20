
package mkw;

import shared.Format;

import java.util.ArrayList;

public class Gp {
    private int maxRaces;
    private int racesPlayedInGp = 0;
    private ArrayList<Race> races = new ArrayList();
    private Event event;
    private int gpId;
    private double id;

    private int racePoints;
    private int dcPts = 0;
    private int totalPoints = 0;

    public Gp(Event event, int gpId){
        this.event = event;
        this.gpId = gpId;
        this.id = 1.0*(this.gpId)/10+1.0*this.event.getEventId();
        this.maxRaces = 4;
    }

    public Gp(Event event, int gpId, int max) {
        this(event,gpId);
        this.maxRaces = max;
    }

    public void playRace(Race r){
        System.out.println("");
        System.out.println("adding the following race to GP records");
        System.out.println(r);
        this.races.add(r);
        this.addScores();
        this.racesPlayedInGp++;
    }

    public int undoLastRace(){
        int removed = getMostRecentRace().getPoints();
        this.racePoints-=removed;
        this.racesPlayedInGp--;
        races.remove(racesPlayedInGp);
        return removed;
    }

    public boolean isInDcStandby() {
        //if the GP does has not reached it's maximum race capacity and the
        //most recent completed race is a placeholder, the gp is in DC standby
        if(this.isUnplayed()){
            return false;
        }
        if(!this.newGpRequired()&&this.getMostRecentRace().isPlaceholder()){
            return true;
        }
        return false;
    }

    public boolean twoRaceCheck() {
        if (races.size() < 2) {
            return false;
        }
        if (races.get(races.size() - 2).isPlaceholder()) {
            return true;
        }
        return false;
    }

    public void clearDcPts(){
        this.dcPts = 0;
    }

    public void addScores() {
        int sum = 0;
        for(Race race:races){
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

    public int getDcCompensation(boolean onResults) {
        int missed = getMissedRacesFromdc();
        if (onResults) {
            if (missed == 4) {
                System.out.println("-----------");
                System.out.println("you missed " + missed + " races in the last gp before the reset");
                System.out.println("you were on the results screen when you dc'd so you get " + 15 + "pts in compensation");
                System.out.println("-----------");
                dcPts=15;
                return 15;
            } else {
                System.out.println("-----------");
                System.out.println("you missed " + missed + " races in the last gp before the reset");
                System.out.println("you were on the results screen when you dc'd so you get " + ((missed - 1) * 3) + "pts in compensation");
                System.out.println("-----------");
                dcPts=(missed - 1) * 3;
                return (missed - 1) * 3;
            }
        } else {
            if (missed == 4) {
                System.out.println("-----------");
                System.out.println("you missed " + missed + " races in the last gp before the reset");
                System.out.println("you were not on the results screen when you dc'd so you get " + 18 + "pts in compensation");
                System.out.println("-----------");
                dcPts=18;
                return 18;
            } else {
                System.out.println("-----------");
                System.out.println("you missed " + missed + " races in the last gp before the reset");
                System.out.println("you were on the results screen when you dc'd so you get " + ((missed) * 3) + "pts in compensation");
                System.out.println("-----------");
                dcPts=(missed) * 3;
                return (missed) * 3;
            }
        }
    }

    public Race getMostRecentRace(){
        return races.get(racesPlayedInGp-1);
    }

    public int getRemainingRacesInGp(){
        return maxRaces-racesPlayedInGp;
    }

    public int getGpId() {
        return gpId;
    }

    public double getId() {
        return id;
    }

    public int getIdofRaceAboutToBePlayed(){
        return racesPlayedInGp+1;
    }

    public ArrayList<Race> getRaces() {
        return races;
    }

    public int getTotalPoints(){
        return  totalPoints;
    }

    public Tier getTier(){
        return event.getTier();
    }

    public Format getFormat(){
        return event.getFormat();
    }


    public int getMissedRacesFromdc() {
        int count = 0;
        for(Race r:races){
            if(r.isPlaceholder()){
                count++;
            }
        }
        return count;
    }

    public boolean nodc(){
        boolean nodc = true;
        for(Race r : getRaces()){
            if(r.isPlaceholder()){
                nodc = false;
            }
        }
        return nodc;
    }

    public boolean fourRacesPlayed(){
        if(racesPlayedInGp==4){
            return true;
        }
        return false;
    }

    public boolean mostRecentRaceWasMissed(){
        return this.getMostRecentRace().isPlaceholder();
    }

    public void setDcPts(int dcPts) {
        this.dcPts = dcPts;
    }

    public String toString(){
        return "GP Status:: Event: "+event.getEventId()+", GP: "+gpId+", Races-Played: "+racesPlayedInGp+", Points: "+racePoints+", DC Points: "+dcPts;
    }

    public void printRaces(){
        System.out.println("~~[GP "+this.gpId+"]~~ Races-Played: "+racesPlayedInGp+", Points: "+racePoints+", DC Points: "+dcPts);
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        for(Race race:races){
            System.out.println(race);
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }

}
