
package mk8dx;

import mkw.Gp;
import mkw.Race;
import shared.Format;
import java.util.ArrayList;
import java.util.Scanner;
import Ui.Ui;

public class EventD {
    private Scanner scanner;
    private final static int raceLimit = 12;

    private final TierD tier;
    private final Format format;

    private final int eventId;

    private GpD currentGp;
    private final ArrayList<GpD> completedGps = new ArrayList<>();
    private int racesPlayed = 0;
    private int gpPlayed = 0;

    private int racePoints = 0;
    private int dcPoints = 0;
    private int totalPoints = 0;
    private boolean inDcStandby = false;

    public boolean outOfStandby = false;
    public boolean backToStandby = false;

    public EventD(TierD tier, Format format, int eventsPlayed) {
        this.scanner = scanner;

        this.tier = tier;
        this.format = format;

        this.eventId = eventsPlayed+1;

        this.currentGp = new GpD(this,gpPlayed+1);
    }

    public void playRace(TrackD trackD, int started, int finished, int players) {
        currentGp.playRace(new RaceD(this, currentGp, racesPlayed + 1, trackD, started, finished));
        racesPlayed++;
        updateScoring();
        System.out.println("");
        System.out.println(currentGp);
        if(racesPlayed>=12){
            startNewGp();
        }
        outOfStandby = false;
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

        if(racesPlayed == 12){
            System.out.println("This event has 12 races played and is now conluded.");
        }else{
            System.out.println("the next GP is starting! "
                    + "Due to prior room reset(s), the GP only has " + (raceLimit - racesPlayed) + " races instead of 12.");
            currentGp = new GpD(this, gpPlayed + 1, raceLimit - racesPlayed);
        }
        updateInDcStandbyStatus();
    }

    public int getChancesToRejoin() {
        return currentGp.getRemainingRacesInGp();
    }

    public void updateScoring() {

        int raceSum = 0;
        int dcSum = 0;
        currentGp.updateTotalPoints();
        if (!currentGp.isUnplayed()) {
            raceSum += currentGp.getRacePoints();
            dcSum += currentGp.getDcPts();
        }
        for (GpD gp : completedGps) {
            raceSum += gp.getRacePoints();
            dcSum += gp.getDcPts();
        }
        racePoints = raceSum;
        dcPoints = dcSum;
        this.totalPoints = this.racePoints + this.dcPoints;
        updateInDcStandbyStatus();
    }

    public void updateInDcStandbyStatus() {
        this.inDcStandby = currentGp.isInDcStandby2(this.outOfStandby);
        this.outOfStandby = false;
    }


    public void dcStandby() {
        System.out.println("");
        System.out.println("you missed a race because you disconnected,"
                + " generating placeholder race");
        currentGp.playRace(new RaceD(this,currentGp, racesPlayed+1));
        racesPlayed++;
        currentGp.getDcCompensation();
        updateScoring();
        if(isEventDone()){
            startNewGp();
        }
    }

    public void dcProcessD(){
        int asks = getChancesToRejoin();
        boolean rejoined = false;

        if (currentGp.isUnplayed()) {
            System.out.println("The current GP is unplayed , ");
            this.dcStandby();
            return;
        }
        if (this.inDcStandby) {
            rejoined = Ui.yesNo("were you able to rejoin the room before the next race started?");
            if (rejoined) {
                outOfStandby = true;
                this.updateScoring();
                postRacestatus();
                System.out.println("you have rejoined, back to normal operations");
                return;
            }else{
                System.out.println("you are in dc standby because the prior race was missed and "
                        +"\n"+ "you were not able to rejoin before the next race started");
                dcStandby();
                if(racesPlayed>=12){
                    startNewGp();
                }
                postRacestatus();
                return;
            }
        }
        System.out.println("you have dc'd in this gp, the last race played was a normal race");
        dcStandby();
        if(racesPlayed>=12){
            startNewGp();
        }
    }

    public void undoDD(){
        if (this.racesPlayed == 0) {
            return;
        }

        System.out.println("");
        System.out.println("undo summary");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        if (currentGp.isUnplayed() || racesPlayed == 12) {
            System.out.println("The previous GP has ended and the first race of "
                    + "the next GP has not finished yet (that or 12 races been played)");
            currentGp = this.getMostRecentCompletedGp();
            removeMostRecentCompletedGp();
            this.gpPlayed--;
            System.out.println("editing the most recent completed GP");
            undoDDSubProcess();
        } else {
            System.out.println("you are currently in the middle of a gp");
            undoDDSubProcess();
        }
    }

    public void undoDDSubProcess(){
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

    public void undoProtocall() {
        undoDD();
        postRacestatus();
    }

    public void removeMostRecentCompletedRace(){
        System.out.println("removing race:" + currentGp.getMostRecentRace());
        currentGp.undoLastRace();
        this.racesPlayed--;
        RaceD.setRaceCount(RaceD.getRaceCount()-1);
        this.updateScoring();
    }

    public void resetProtocall() {
        System.out.println("room was reset, generating a new GP");
        startNewGp();
    }

    public void removeMostRecentCompletedGp(){
        completedGps.remove(completedGps.size()-1);
    }

    public GpD getCurrentGp(){
        return currentGp;
    }

    public GpD getMostRecentCompletedGp(){
        return this.completedGps.get(completedGps.size()-1);
    }

    public RaceD getLatestRace(){
        if(currentGp.isUnplayed()){
            return getMostRecentCompletedGp().getMostRecentRace();
        }else{
            return currentGp.getMostRecentRace();
        }
    }

    public int getEventId() {
        return eventId;
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

    public boolean nodc(){
        boolean nodc = true;
        for(GpD gp:completedGps){
            if(gp.nodc()==false){
                nodc = false;
            }
        }
        return nodc;
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

    public boolean currentGpIsUnplayed(){
        if(currentGp.isUnplayed()){
            return true;
        }
        return false;
    }

    public int getRacePoints() {
        return racePoints;
    }

    public int getDcPoints() {
        return dcPoints;
    }

    public TierD getTier() {
        return tier;
    }

    public Format getFormat() {
        return format;
    }

    public ArrayList<GpD> getCompletedGps() {
        return completedGps;
    }



    public String preRaceString(){
        StringBuilder bob = new StringBuilder();
        bob.append("Entering Data For Race " + (getRacesPlayed() + 1)  + ", (GP "+getCurrentlyPlayingGpId() + ", Race " + getRaceNumberforUpcomingRace()+")"
                +"\n" +
                "\n" + "Races played: " + racesPlayed
                + ", points: " + racePoints + ", dc points: " + dcPoints+", Total Points: "+totalPoints+"\n");

        if(racesPlayed>0){
            RaceD r = getMostRecentlyCompletedRace();
            if(r.isPlaceholder()){
                bob.append("\n"+"~~Last Race~~" +"\n"+
                        "Satout because of DC");
            }else{
                bob.append("\n"+"~~Last Race~~" +"\n"+ "Race "+r.getRace()+
                        ", " + r.getTrack().getAbbreviation() + ", finish: " +  r.getFinish()+ ", start: " +
                        r.getStart() +", players: " + r.getPlayers()+", points: " + r.getPoints());
            }

        }
        return bob.toString();
    }

    public RaceD getMostRecentlyCompletedRace(){

        if(this.isEventDone()){
            return getMostRecentCompletedGp().getMostRecentRace();
        }else if(this.gpPlayed==0){
            return currentGp.getMostRecentRace();
        }else{
            if(currentGpIsUnplayed()){
                return getMostRecentCompletedGp().getMostRecentRace();
            }else{
                return currentGp.getMostRecentRace();
            }
        }
    }

    public void preRaceStatus() {

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

    public String getEventSummary(){
        StringBuilder bob = new StringBuilder();
        if(this.gpPlayed==0){
            bob.append(currentGp.getGpString()+"\n");

        }
        for (GpD gp : completedGps) {
            bob.append(gp.getGpString()+"\n");
        }
        if (this.racesPlayed < 12&&this.gpPlayed!=0) {
            bob.append(currentGp.getGpString()+"\n");
        }
        return bob.toString();
    }
    public void printEvent() {
        if(this.gpPlayed==0){
            currentGp.printRaces();
        }
        for (GpD gp : completedGps) {
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
            rejoined = Ui.yesNo("were you able to rejoin the room before the GP ended because the room was reset?");
            if (rejoined) {
                manageDc();
                resetProtocall();
                break;
            }
            missed++;
            dcStandby();
        }

        if (rejoined == false) {
            this.manageDc();
        }
    }

    @Deprecated
    public void manageDc() {

        this.updateScoring();
        postRacestatus();
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
                removeMostRecentCompletedRace();
                System.out.println("one dc point removed");
                this.updateScoring();
                return;
            }else{
                System.out.println("the previous race was the final race of the most recently completed GP, "
                        + "and was a normal race");
                removeMostRecentCompletedRace();
                this.updateScoring();
                return;
            }
        }
        if(currentGp.isInDcStandby()){
            System.out.println("the previous race is not the final race of a GP, but is placeholder race");
            System.out.println(",meaning that the GP is DC standby until the GP is compeleted again"
                    + "or is further undone to a point before the DC was registered ");
            removeMostRecentCompletedRace();
            backToStandby = true;
            this.updateScoring();
            return;
        }
        if(currentGp.mostRecentRaceWasMissed()){
            System.out.println("\"the previous race is not the final race of a GP, but was a placeholder,"
                    +"\n"+ "you are not in dcstandby because you said you were able to rejoin");
            System.out.println("one dc point removed");
            backToStandby = true;
            removeMostRecentCompletedRace();
            this.updateScoring();
            return;
        }
        System.out.println("the previous race is not the final race of a GP, and is normal race");
        removeMostRecentCompletedRace();
        if(this.getLatestRace().isPlaceholder()){
            backToStandby = true;
        }
    }
    @Deprecated
    public void neoDcProcess(){
        int asks = getChancesToRejoin();
        boolean rejoined = false;

        if(currentGp.isUnplayed()){
            System.out.println("The current GP is unplayed , ");
            System.out.println("since you have already dc'd one placeholder race will be generated");
            this.dcStandby();
            return;
        }
        if(this.inDcStandby){

            rejoined = Ui.yesNo("were you able to rejoin the room before the next race started?");
            if (rejoined) {
                outOfStandby = true;
                this.updateScoring();
                postRacestatus();
                System.out.println("");
                System.out.println("you have rejoined, back to normal operations");
                return;
            } else {
                System.out.println("you are in dc standby because the prior race was missed and "
                        + "you were not able to rejoin before the enxt race started");
                dcStandby();
                postRacestatus();
                return;
            }
        }
        System.out.println("you have dc'd in this gp, the last race played was a normal race");
        dcStandby();
    }

    @Deprecated
    public void undoD(){
        if (this.racesPlayed == 0) {
            return;
        }
        System.out.println("");
        System.out.println("undo summary");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        if (currentGp.isUnplayed() || racesPlayed == 12) {
            System.out.println("The previous GP has ended and the first race of "
                    + "the next GP has not finished yet (that or 12 races been played)");
            currentGp = this.getMostRecentCompletedGp();
            removeMostRecentCompletedGp();
            this.gpPlayed--;
            System.out.println("editing the most recent completed GP");

            if (currentGp.isInDcStandby2(outOfStandby)) {
                System.out.println("you are in dc standby because the most recently played race was a placeholder");
                System.out.println("removing this placeholder");
                removeMostRecentCompletedRace();
                System.out.println("now the the new most recently completed race being a placeholder is "
                        + currentGp.mostRecentRaceWasMissed());
                if (currentGp.mostRecentRaceWasMissed()) {
                    System.out.println("since the new most recently completed race is still a placeholder,");
                    System.out.println("you are still in dc standby");
                    this.updateScoring();
                    return;
                } else {
                    System.out.println("since the new most recently completed race is still not placeholder,");
                    System.out.println("you are no longer in dc standby");
                    this.updateScoring();
                    return;
                }
            }else{
                System.out.println("the previous race was the final race of the most recently completed GP, "
                        + "and was a normal race");
                System.out.println("removing this normal race");
                removeMostRecentCompletedRace();
                System.out.println("now the the new most recently completed race being a placeholder is "
                        + currentGp.mostRecentRaceWasMissed());
                if (currentGp.mostRecentRaceWasMissed()) {
                    System.out.println("since the new most recently completed race is still a placeholder,");
                    System.out.println("you are now in dc standby");
                    this.updateScoring();
                    return;
                } else {
                    System.out.println("since the new most recently completed race is still not placeholder,");
                    System.out.println("you are not dc standby");
                    this.updateScoring();
                    return;
                }
            }
        }else{
            System.out.println("you are currently in the middle of a gp");
            if (currentGp.isInDcStandby2(outOfStandby)) {
                System.out.println(" the most recently played race was a placeholder");
                System.out.println("removing this placeholder");
                removeMostRecentCompletedRace();
                if(currentGp.isUnplayed()){
                    System.out.println("there are no races played for the gp, dc standby is now false");
                    this.updateScoring();
                    return;
                }
                System.out.println("now the the new most recently completed race being a placeholder is "
                        + currentGp.mostRecentRaceWasMissed());
                if (currentGp.mostRecentRaceWasMissed()) {
                    System.out.println("since the new most recently completed race is still a placeholder,");
                    System.out.println("you are in dc standby");
                    this.updateScoring();
                    return;
                } else {
                    System.out.println("since the new most recently completed race is not a placeholder,");
                    System.out.println("you are not in dc standby");
                    this.updateScoring();
                    return;
                }
            } else {
                System.out.println("the previous race was not the final race of the most recently completed GP, "
                        + "and was a normal race");
                System.out.println("removing this normal race");
                removeMostRecentCompletedRace();
                if(currentGp.isUnplayed()){
                    System.out.println("there are no races played for the gp, dc standby is now false");
                    this.updateScoring();
                    return;
                }
                System.out.println("now the the new most recently completed race being a placeholder is "
                        + currentGp.mostRecentRaceWasMissed());
                if (currentGp.mostRecentRaceWasMissed()) {
                    System.out.println("you are  not in standby desite the the most "
                            +"\n"+ "recent completed race being a placeholder because the race"
                            +"\n"+ "that was removed was a normal race so you were able to rejoin");
                    outOfStandby = true;
                    this.updateScoring();
                    return;
                } else {
                    System.out.println("since the new most recently completed race is still not placeholder,");
                    System.out.println("you are not dc standby");
                    this.updateScoring();
                    return;
                }
            }
        }
    }
}
