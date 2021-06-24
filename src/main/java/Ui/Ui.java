package Ui;


import mkw.Race;
import shared.Format;
import java.util.Scanner;
import mk8dx.*;
import mkw.Event;
import mkw.Tier;
import mkw.Track;

@Deprecated
public class Ui {

    private Scanner scanner;

    public Ui(Scanner scanner) {

        this.scanner = scanner;
    }

    public void start(){


        while(true){
            System.out.println("e for entry, x to exit");
            String input = scanner.nextLine();

            if(input.equals("x")){
                System.out.println("exiting");
                break;
            }

            if (input.equals("e")) {
                enter();
            }
        }
    }

    private void enter(){

        while (true) {
            System.out.println("new event: x to exit, w for mkw, d for mk8dx");
            String input = scanner.nextLine();

            if (input.equals("x")) {
                break;
            }
            if (input.equals("w")) {
                Tier tier = getTier();
                Format format = getFormat();
                System.out.println("\n" + "initializing event");
                mogiTime(tier, format);
            }
            if (input.equals("d")) {
                TierD tier = getTierD();
                Format format = getFormat();
                System.out.println("\n" + "initializing event");
                mogiTimeD(tier, format);
            }
        }
    }

    private void mogiTime(Tier t, Format f){

        int eventsplayed = RaceDao.getEventsStored();
        System.out.println("");
        System.out.println("generating id's");
        System.out.println("starting the "+(eventsplayed+1)+"th event and "+(RaceDao.getRacesStored()+1)+"th race");
        System.out.println("");
        Event event = new Event(t,f, eventsplayed);
        Race.setRaceCount(RaceDao.getRacesStored());
        boolean onResults = false;


        while(event.getRacesPlayed()<12){

            if(event.getRacesPlayed()==0){
                boolean quitMogi = yesNo("cancel event?");
                if(quitMogi){
                    return;
                }
            }

            event.preRaceStatus();

            if(event.getRacesPlayed()!=0){
                System.out.println("");
                boolean undo = yesNo("Undo the last race entry?");
                if(undo){
                    event.undoProtocall();
                    continue;
                }
            }

            if(event.isInDcStandby()){
                event.neoDcProcess(onResults);
                if(event.getRacesPlayed()==12){
                    System.out.println("");
                    boolean undo = yesNo("12 races have been played,"
                            + " this is the last chance to change anything."
                            + " undo last race entry?");
                    if (undo) {
                        event.undoProtocall();
                        continue;
                    }
                }
                continue;
            }

            boolean roomReset = yesNo("was the room reset prior to this race?");
            if(roomReset){
                event.resetProtocall();
                event.postRacestatus();
                event.preRaceStatus();
            }

            boolean dc = yesNo("Did you dc this race?");

            if(dc){
                onResults = yesNo("were you on the Results Page");
                event.neoDcProcess(onResults);
            }else{
                Track trackInput = getTrack();
                int playersInput = getPlayers();
                int startInput = getStart(playersInput);
                int finishInput = getFinish(playersInput);
                event.playRace(trackInput, startInput, finishInput, playersInput);
            }

            event.postRacestatus();

            if(event.getRacesPlayed()==12){
                System.out.println("");
                boolean undo = yesNo("12 races have been played,"
                        + " this is the last chance to change anything."
                        + " undo last race entry?");
                if (undo) {
                    event.undoProtocall();
                    continue;
                }
            }
        }

        System.out.println("storing event, final points: "+event.getFinalPoints());
        RaceDao.add(event);
        System.out.println("This session's events");
        RaceDao.printRecent();

        System.out.println("storing");
        RaceDao.storeEvent();

        RaceDao.refresh();
        System.out.println("back to main menu");
    }

    private void mogiTimeD(TierD t, Format f){

        int eventsplayed = RaceDao.getEventsDStored();
        System.out.println("");
        System.out.println("generating id's");
        System.out.println("starting the "+(eventsplayed+1)+"th event and "+(RaceDao.getRacesDStored()+1)+"th race");
        System.out.println("");
        EventD event = new EventD(t,f,eventsplayed);
        RaceD.setRaceCount(RaceDao.getRacesDStored());

        while(event.getRacesPlayed()<12){

            if(event.getRacesPlayed()==0){
                boolean quitMogi = yesNo("cancel event?");
                if(quitMogi){
                    return;
                }
            }

            event.preRaceStatus();

            if(event.getRacesPlayed()!=0){
                System.out.println("");
                boolean undo = yesNo("Undo the last race entry?");
                if(undo){
                    event.undoProtocall();
                    continue;
                }
            }

            if(event.isInDcStandby()){
                event.dcProcessD();
                if(event.getRacesPlayed()==12){
                    System.out.println("");
                    boolean undo = yesNo("12 races have been played,"
                            + " this is the last chance to change anything."
                            + " undo last race entry?");
                    if (undo) {
                        event.undoProtocall();
                        continue;
                    }
                }
                continue;
            }

            boolean roomReset = yesNo("was the room reset prior to this race?");
            if(roomReset){
                event.resetProtocall();
                event.postRacestatus();
                event.preRaceStatus();
            }

            boolean dc = yesNo("Did you dc this race?");

            if(dc){
                event.dcProcessD();
            }else{
                TrackD trackInput = getTrackD();
                int startInput = getStart(12);
                int finishInput = getFinish(12);
                event.playRace(trackInput, startInput, finishInput, 12);
            }

            event.postRacestatus();

            if(event.getRacesPlayed()==12){
                System.out.println("");
                boolean undo = yesNo("12 races have been played,"
                        + " this is the last chance to change anything."
                        + " undo last race entry?");
                if (undo) {
                    event.undoProtocall();
                    continue;
                }
            }
        }

        System.out.println("storing event, final points: "+event.getFinalPoints());
        RaceDao.addD(event);

        System.out.println("This session's events");
        RaceDao.printRecent();

        System.out.println("storing");
        RaceDao.storeEventD();

        RaceDao.refresh();
        System.out.println("back to main menu");
    }


    private Tier getTier() {
        while (true) {
            System.out.println("enter tier (1-8)");
            String Input = scanner.nextLine();

            if(Tier.fromString(Input).isPresent()){
                Tier tier = Tier.fromString(Input).get();
                System.out.println("you've entered "+tier.getTier());
                return tier;
            }
            System.out.println("not a valid tier");
        }
    }

    private TierD getTierD() {
        while (true) {
            System.out.println("enter tier (a-f,s, or x)");
            String Input = scanner.nextLine();

            if(TierD.fromString(Input).isPresent()){
                TierD tier = TierD.fromString(Input).get();
                System.out.println("you've entered "+tier.getTier());
                return tier;
            }
            System.out.println("not a valid tier");
        }
    }

    private Format getFormat(){
        while(true){
            System.out.println("enter format");
            String formatInput = scanner.nextLine();

            if(Format.fromString(formatInput).isPresent()){
                Format format = Format.fromString(formatInput).get();
                System.out.println("you've entered "+format.getFormat());
                return format;
            }
            System.out.println("not a valid format");
        }
    }

    private Track getTrack(){
        while(true){
            System.out.println("enter track");
            String trackInput = scanner.nextLine();

            if(Track.fromString(trackInput).isPresent()){
                Track track = Track.fromString(trackInput).get();
                System.out.println("You have entered: "+track.getFullName());
                return track;
            }
            System.out.println("not a valid track");
        }
    }

    private TrackD getTrackD() {
        while (true) {
            System.out.println("enter track");
            String trackInput = scanner.nextLine();

            if (TrackD.fromString(trackInput).isPresent()) {
                TrackD track = TrackD.fromString(trackInput).get();
                System.out.println("You have entered: " + track.getFullName());
                return track;
            }
            System.out.println("not a valid track");
        }
    }

    private int getStart(int players){
        while(true){
            System.out.println("enter Start(1-12)");
            String input = scanner.nextLine();

            boolean properInt = input.matches("[1-9]|10|11|12");

            if(properInt){
                return Integer.parseInt(input);
            }
            System.out.println("not a valid start position");
        }
    }

    private int getFinish(int players) {
        while (true) {
            System.out.println("enter Finish(1-"+players+")");
            String input = scanner.nextLine();

            boolean properInt = false;
            if(players == 10){
                if(input.matches("[1-9]|10")){
                    properInt = true;
                }
            }
            if(players == 11){
                if(input.matches("[1-9]|10|11")){
                    properInt = true;
                }
            }
            if(players == 12){
                if(input.matches("[1-9]|10|11|12")){
                    properInt = true;
                }
            }
            if(properInt){
                return Integer.parseInt(input);
            }
            System.out.println("not a valid finish position");
        }
    }

    private int getPlayers(){
        while (true) {
            System.out.println("enter players on results (10-12)");
            String input = scanner.nextLine();

            boolean properInt = input.matches("1[0-2]");
            if (properInt) {
                return Integer.parseInt(input);
            }
            System.out.println("not a valid number of players for the race");
        }
    }

    public static boolean yesNo(String question){
        while (true) {

            Scanner scanner = new Scanner(System.in);

            System.out.println(question +"(y/n)");
            String input = scanner.nextLine();

            if(input.equals("y")){
                return true;
            }
            if(input.equals("n")){
                return false;
            }
            System.out.println("enter y or n");
        }
    }

}