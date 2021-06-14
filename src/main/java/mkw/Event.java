
package mkw;

import mk8dx.GpD;
import mk8dx.RaceD;
import shared.Format;
import java.util.ArrayList;
import java.util.Scanner;

public class Event {
    private Scanner scanner;
    private final static int raceLimit = 12;
    private static int totalEvents = 0;

    private final Tier tier;
    private final Format format;

    private final int eventId;

    private Gp currentGp;
    private final ArrayList<Gp> completedGps = new ArrayList<>();
    private int racesPlayed = 0;
    private int gpPlayed = 0;

    private int racePoints = 0;
    private int dcPoints = 0;
    private int totalPoints = 0;
    private boolean inDcStandby = false;

    public Event(Tier tier, Format format, Scanner scanner) {
        this.scanner = scanner;

        this.tier = tier;
        this.format = format;

        Event.totalEvents++;
        this.eventId = totalEvents;

        this.currentGp = new Gp(this,gpPlayed+1);
    }

    public void playRace(Track track, int started, int finished, int players) {
        currentGp.playRace(new Race(this, currentGp, racesPlayed + 1, track, started, finished, players));
        racesPlayed++;
        updateScoring();
        System.out.println("");
        System.out.println(currentGp);

        if (currentGp.newGpRequired()) {
            startNewGp();
        }
    }

    public void updateScoring(){

        int raceSum = 0;
        int dcSum = 0;
        currentGp.updateTotalPoints();
        if(!currentGp.isUnplayed()){
            raceSum+=currentGp.getRacePoints();
            dcSum+=currentGp.getDcPts();
        }
        for(Gp gp:completedGps){
            raceSum+=gp.getRacePoints();
            dcSum+=gp.getDcPts();
        }
        racePoints =raceSum;
        dcPoints = dcSum;
        this.totalPoints = this.racePoints+this.dcPoints;
        updateInDcStandbyStatus();
    }

    public void startNewGp(){
        if(currentGp.isUnplayed()){
            System.out.println("room was reset before or during the first race of a GP, "
                    + "so we are still in gp "+(this.gpPlayed+1));
            return;
        }
        System.out.println("");
        System.out.println("Storing the GP that just finished to the event");
        completedGps.add(currentGp);
        this.gpPlayed++;

        if(racesPlayed<=8){
            System.out.println("the next GP is starting!");
            currentGp = new Gp(this, gpPlayed+1);
        }else if(racesPlayed == 12){
            System.out.println("This event has 12 races played and is now conluded.");
        }else{
            System.out.println("the next GP is starting! "
                    + "Due to prior room resets, the GP only has "+(raceLimit-racesPlayed)+" races instead of 4.");
            currentGp = new Gp(this,gpPlayed+1,raceLimit-racesPlayed);
        }
        updateInDcStandbyStatus();
    }

    public void manageDc(boolean onResults) {
        currentGp.getDcCompensation(onResults);
        this.updateScoring();
        postRacestatus();

        if (currentGp.newGpRequired()) {
            startNewGp();
        }
    }

    public int getChancesToRejoin() {
        return currentGp.getRemainingRacesInGp();
    }

    public void dcStandby() {
        System.out.println("");
        System.out.println("you missed a race because you disconnected,"
                + " generating placeholder race");
        currentGp.playRace(new Race(this,currentGp, racesPlayed+1));
        racesPlayed++;
        updateScoring();
        System.out.println("");
        currentGp.printRaces();
    }

    public void neoDcProcess(boolean onResults){
        boolean rejoined = false;

        if(currentGp.isUnplayed()){
            System.out.println("The current GP is unplayed , ");
            System.out.println("since you have already dc'd one placeholder race will be generated");
            dcStandby();
            return;
        }
        if(this.inDcStandby){
            rejoined = yesNo("were you able to rejoin the room before the GP ended because the room was reset?");
            if (rejoined) {
                manageDc(onResults);
                resetProtocall();
            } else {
                dcStandby();
                if(inDcStandby==false){
                    manageDc(onResults);
                }
            }
            return;
        }
        System.out.println("you have dc'd in this gp, the last race played was a normal race");
        dcStandby();
    }

    public void undoV3(){
        if(this.racesPlayed == 0) {
            return;
        }
        System.out.println("");
        System.out.println("undo summary");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if (currentGp.isUnplayed() || racesPlayed == 12) {
            System.out.println("The previous GP has ended and the first race of "
                    + "the next GP has not started yet (that or 12 races been played)");
            System.out.println("resting dc point until a gp has been completed(if applicable)");
            currentGp = this.getMostRecentCompletedGp();
            currentGp.clearDcPts();
            removeMostRecentCompletedGp();
            this.gpPlayed--;
            System.out.println("editing the most recent completed GP");
            undoDDSubProcess();
        } else {
            System.out.println("you are currently in the middle of a gp");
            undoDDSubProcess();
        }
    }

    public void undoDDSubProcess() {
        boolean endUpInStandby = currentGp.twoRaceCheck();

        if (endUpInStandby) {
            System.out.println("Once the most recently played race is removed,");
            System.out.println("the subsequent most recently played race is a placeholder");
            System.out.println("therefore, you are back in dc standby");
            removeMostRecentCompletedRace();
        } else {
            System.out.println("Once the most recently played race is removed,");
            System.out.println("the subsequent most recently played race is not a placeholder");
            System.out.println("or there are no completed races in the GP");
            System.out.println("therefore, you are not in dc standby");
            removeMostRecentCompletedRace();

        }
    }

    public void resetProtocall() {
        System.out.println("room was reset, generating a new GP");
        startNewGp();
    }

    public void undoProtocall() {
        undoV3();
        postRacestatus();
    }

    public void removeMostRecentCompletedRace(){
        System.out.println("removing race:" + currentGp.getMostRecentRace());
        currentGp.undoLastRace();
        this.racesPlayed--;
        this.updateScoring();
    }


    public void updateInDcStandbyStatus(){
        this.inDcStandby = currentGp.isInDcStandby();
    }

    public void removeMostRecentCompletedGp(){
        completedGps.remove(completedGps.size()-1);
    }

    public Gp getCurrentGp(){
        return currentGp;
    }

    public Gp getMostRecentCompletedGp(){
        return this.completedGps.get(completedGps.size()-1);
    }

    public Race getLatestRace(){
        if(currentGp.isUnplayed()){
            return getMostRecentCompletedGp().getMostRecentRace();
        }else{
            return currentGp.getMostRecentRace();
        }
    }

    public boolean nodc(){
        boolean nodc = true;
        for(Gp gp:completedGps){
            if(gp.nodc()==false){
                nodc = false;
            }
        }
        return nodc;
    }


    public int getEventId() {
        return eventId;
    }

    public Tier getTier() {
        return tier;
    }

    public Format getFormat() {
        return format;
    }

    public ArrayList<Gp> getCompletedGps() {
        return completedGps;
    }

    public int getRacesPlayed() {
        return racesPlayed;
    }

    public int getFinalPoints() {
        return totalPoints;
    }

    public int getCurrentlyPlayingGpId(){
        return currentGp.getGpId();
    }

    public int getRaceNumberforUpcomingRace(){
        return currentGp.getIdofRaceAboutToBePlayed();
    }

    public boolean isEventDone(){
        if(this.racesPlayed==this.raceLimit){
            return true;
        }
        return false;
    }

    public boolean isInDcStandby() {
        return inDcStandby;
    }

    public void endEventEarly(){
        for(int i=this.racesPlayed+1; i<=raceLimit; i++){
            this.dcStandby();
        }
    }

    public void cancelEvent(){
        while(racesPlayed>0){
            this.undoLastRaceOrDc();
        }
    }

    private boolean yesNo(String question) {
        while (true) {

            System.out.println(question + "(y/n)");
            String input = scanner.nextLine();

            if (input.equals("y")) {
                return true;
            }
            if (input.equals("n")) {
                return false;
            }
            System.out.println("enter y or n");
        }
    }

    public void preRaceStatus() {
        System.out.println("");
        System.out.println("");
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("Entering Data For: GP " + getCurrentlyPlayingGpId() + ", Race " + getRaceNumberforUpcomingRace()
                + "(Race " + (getRacesPlayed() + 1) + " overall)"+", dc standby is currently "+this.inDcStandby);
        System.out.println("[Event so far] " + this);
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("");
    }

    public void postRacestatus() {
        System.out.println("");
        System.out.println("all races so far:");
        printEvent();
    }

    public String toString() {
        return "GP's played: " + this.gpPlayed + ", Races played: " + racesPlayed
                + ", points: " + racePoints + ", dc points: " + dcPoints+", Total Points: "+totalPoints;
    }

    public void printEvent() {
        if(this.gpPlayed==0){
            currentGp.printRaces();
        }
        for (Gp gp : completedGps) {
            gp.printRaces();
        }
        if (this.racesPlayed < 12&&this.gpPlayed!=0) {
            currentGp.printRaces();
        }
    }

    @Deprecated
    public void undoLastRaceOrDc(){
        if(this.racesPlayed == 0){
            return;
        }
        if(currentGp.isUnplayed()||racesPlayed==12){
            currentGp = this.getMostRecentCompletedGp();
            removeMostRecentCompletedGp();
            this.gpPlayed--;
            if(currentGp.getDcPts()!=0){
                System.out.println("prior race(s) was/were placeholders beceause you dc'd, removing all placeholders");
                int missed = currentGp.getMissedRacesFromdc();
                for (int i = 1; i <= missed; i++) {
                    System.out.println("removing placeholder:" + currentGp.getMostRecentRace());
                    currentGp.undoLastRace();
                    this.racesPlayed--;
                }
                currentGp.clearDcPts();
                this.updateScoring();
                return;
            }
        }
        currentGp.undoLastRace();
        this.racesPlayed--;
        this.updateScoring();
        System.out.println("");
        System.out.println("Last Race or Dc sequence has been undone");
    }

    @Deprecated
    public void startDcProcess(boolean onResults){
        int asks = getChancesToRejoin();
        boolean rejoined = false;

        int missed = 1;
        for (int i = 1; i <= asks; i++) {

            this.preRaceStatus();
            rejoined = yesNo("were you able to rejoin the room before the GP ended because the room was reset?");
            if (rejoined) {
                manageDc(onResults);
                resetProtocall();
                break;
            }
            missed++;
            dcStandby();
        }

        if (rejoined == false) {
            this.manageDc(onResults);
        }
    }

    @Deprecated
    public void neoUndo(){
        if (this.racesPlayed == 0) {
            return;
        }
        System.out.println("");
        System.out.println("undo summary");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if (currentGp.isUnplayed() || racesPlayed == 12) {
            System.out.println("The previous GP has ended and the first race of "
                    + "the next GP has not started yet (that or 12 races been played)");
            currentGp = this.getMostRecentCompletedGp();
            removeMostRecentCompletedGp();
            this.gpPlayed--;
            System.out.println("editing the most recent completed GP");

            if(currentGp.mostRecentRaceWasMissed()){

                System.out.println("");
                System.out.println("The previous race was the final race of the most recently completed GP, "
                        + "and was a placeholder because you missed the race due to a disconnection");
                currentGp.clearDcPts();
                removeMostRecentCompletedRace();
                System.out.println("DC points cleared until GP is completed again");
                return;
                //if the last race of a GP was not a placeholder, it is a normal race
            }else{
                System.out.println("the previous race was the final race of the most recently completed GP, "
                        + "and was a normal race");
                removeMostRecentCompletedRace();
                return;
            }
        }
        if(currentGp.isInDcStandby()){
            System.out.println("the previous race is not the final race of a GP, but is placeholder race");
            System.out.println(",meaning that the GP is DC standby until the GP is compeleted again"
                    + "or is further undone to a point before the DC was registered ");
            removeMostRecentCompletedRace();
            return;
        }
        System.out.println("the previous race is not the final race of a GP, but is normal race");
        removeMostRecentCompletedRace();
    }
}
