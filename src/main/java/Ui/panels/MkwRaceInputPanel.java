package Ui.panels;

import Ui.HtmlRace;
import Ui.RaceDao;

import Ui.Gui;
import mkw.*;
import mkw.Event;
import Ui.MogiUpdater;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class MkwRaceInputPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("mkw race input");

    JLabel statusLabel = new JLabel("Status:");
    JTextArea statusTA = new JTextArea();

    JLabel trackLabel = new JLabel("Track:");
    JLabel playersLabel = new JLabel("Players on Results(10-12):");
    JLabel startLabel = new JLabel("Start Position(1-12):");
    JLabel finishLabel = new JLabel("Finish Position(1-Players):");
    JLabel rejoinLabel = new JLabel("Rejoined?");
    JLabel trackMatchLabel = new JLabel("No Track Found");
    JLabel dcLabel = new JLabel("If DC:");

    JTextField trackTf = new JTextField("");
    JTextField playersTf = new JTextField("");
    JTextField startTf = new JTextField("");
    JTextField finishTf = new JTextField("");

    JButton autoButton = new JButton("AutoFill");
    JButton nextButton = new JButton("Next Race");
    JButton backButton = new JButton("Back");
    JButton undoButton = new JButton("Undo Race");
    JButton submitButton = new JButton("Submit");
    JButton dcButtonOn = new JButton("DC on Results");
    JButton dcButtonOff = new JButton("DC off Results");

    JButton rejoinButtonYes = new JButton("YES");
    JButton rejoinButtonNo = new JButton("NO");

    JCheckBox resetBox = new JCheckBox("Room was Reset");

    static Event event = null;
    static String status = "n/a";
    static int gpStart;
    static boolean startChange = false;
    static MogiUpdater mogiUpdater;

    boolean onOff = false;
    boolean reset = false;
    int resetShift = 0;

    JButton summaryButton = new JButton("All Races");

    public MkwRaceInputPanel(CardLayout card, JPanel cardPane, JFrame gui){

        GridBagLayout layout = new GridBagLayout();
        statusTA.setEditable(false);

        setLayout(layout);
        gbc.insets = new Insets(2,10,2,10);

        Setter s = new Setter();
        s.addobjects(titleLabel,this, layout,gbc,0,0,2 , 1, 1,0.1,true);
        s.addobjects(summaryButton,this, layout,gbc,2,0,1 , 1, 1,0.1,true);

        s.addobjects(statusLabel,this, layout,gbc,0,1,1, 1,0.0000001,1,true);
        s.addobjects(statusTA,this, layout,gbc,1,1,2 , 2,1,1,true);

        s.addobjects(trackLabel,this, layout,gbc,0, 5,1,1,0.000000001,0.1,true);
        s.addobjects(trackTf,this, layout,gbc,1, 5,2,1, 1,0.1,true);

        matchLabelUpdate();
        s.addobjects(trackMatchLabel,this, layout,gbc,1, 6,3,1,1,0.1,true);

        s.addobjects(finishLabel,this, layout,gbc,1, 7,1,1,.0000001,0.1,true);
        s.addobjects(finishTf,this, layout,gbc,2, 7,1,1,1,0.1,true);

        s.addobjects(startLabel,this, layout,gbc,1, 8,1,1,.000000001,0.1,true);
        s.addobjects(startTf,this, layout,gbc,2, 8,1,1, 1,0.1,true);

        s.addobjects(playersLabel,this, layout,gbc,1, 9,1,1,.00000001,0.1,true);
        s.addobjects(playersTf,this, layout,gbc,2, 9,1,1, 1,0.1,true);

        s.addobjects(resetBox,this, layout,gbc,1, 10,1,1,1,0.1,true);
        s.addobjects(autoButton,this, layout,gbc,2, 10,1,1,1,0.1,true);

        undoButton.setEnabled(false);
        s.addobjects(undoButton,this, layout,gbc,1, 11,1,1,1,0.1,true);
        s.addobjects(nextButton,this, layout,gbc,2, 11,1,1,1,0.1,true);

        submitButton.setEnabled(false);
        s.addobjects(backButton,this, layout,gbc,1, 12,1,1,1,0.1,true);
        s.addobjects(submitButton,this, layout,gbc,2, 12,1,1,1,0.1,true);

        s.addobjects(dcLabel,this, layout,gbc,0, 13,1,1,0.0000000001,0.1,true);
        s.addobjects(dcButtonOn,this, layout,gbc,1,13,1 , 1, 1 , 0.1,true);
        s.addobjects(dcButtonOff,this, layout,gbc,2,13,1 , 1, 1 , 0.1,true);

        s.addobjects(rejoinLabel,this, layout,gbc,0, 14,1,1,0.0000001,0.1,true);
        rejoinButtonNo.setEnabled(false);
        s.addobjects(rejoinButtonNo,this, layout,gbc,1, 14,1,1,1,0.1,true);
        rejoinButtonYes.setEnabled(false);
        s.addobjects(rejoinButtonYes,this, layout,gbc,2, 14,1,1,1,0.1,true);



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event = null;
                status = "n/a";
                Gui.frame.setSize(Gui.defaultWidth,Gui.defaultHeight);
                card.show(cardPane,"mkwTf");
            }
        });

        autoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomReset();
                mogiUpdater.initializeRaces(RaceDao.getFc());


                if (reset == false) {
                    int raceNumber = event.getRacesPlayed()+1;
                    if(raceNumber==1){
                        startTf.setText(String.valueOf(mogiUpdater.initialStart));
                    }

                    if(mogiUpdater.getRaces().containsKey(raceNumber)){
                        HtmlRace race  = mogiUpdater.getRaces().get(raceNumber);
                        trackTf.setText(race.getTrack());
                        playersTf.setText(String.valueOf(race.getPlayers()));
                        finishTf.setText(String.valueOf(race.getFinish()));

                    }else{
                        if(mogiUpdater.roomFound){
                            InputVerifier.relativePopup("room found but failed to find race "+(event.getRacesPlayed()+1)+" results","autofill error"
                                    ,resetBox);
                        }else{
                            InputVerifier.relativePopup("Failed to find room with "+RaceDao.getFc()+" on wiimfi","autofill error"
                                    ,resetBox);
                        }

                    }
                }else{
                    int raceNumber = event.getRacesPlayed()+1+resetShift;
                    if(raceNumber==1){
                        startTf.setText(String.valueOf(mogiUpdater.initialStart));
                    }

                    if(mogiUpdater.getRaces().containsKey(raceNumber)){
                        HtmlRace race  = mogiUpdater.getRaces().get(raceNumber);
                        trackTf.setText(race.getTrack());
                        playersTf.setText(String.valueOf(race.getPlayers()));
                        finishTf.setText(String.valueOf(race.getFinish()));
                        if(raceNumber==1){
                            startTf.setText(String.valueOf(mogiUpdater.initialStart));
                        }
                    }else{
                        InputVerifier.relativePopup("failed to find race/room","autofill error"
                                ,resetBox);
                    }
                }
            }
        });

        summaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String races = event.getEventSummary();
                JOptionPane pane = new JOptionPane(races);
                JDialog d = pane.createDialog((JFrame)null, "All Races");
                d.setLocationRelativeTo(resetBox);
                d.setVisible(true);
            }
        });

        dcButtonOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomReset();
                unplayedOrCompletedCheck(true);
                dcCheck();
                eventDoneCheck();
                if(event.isEventDone()){
                    setStatusEnd();
                    nextButton.setEnabled(false);
                    submitButton.setEnabled(true);
                }
                onOff = true;
                setPostDcTF();
            }
        });

        dcButtonOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomReset();
                unplayedOrCompletedCheck(true);
                dcCheck();
                eventDoneCheck();
                if(event.isEventDone()){
                    setStatusEnd();
                    nextButton.setEnabled(false);
                    submitButton.setEnabled(true);
                    dcButtonOn.setEnabled(false);
                    dcButtonOff.setEnabled(false);
                }
                onOff = false;
                setPostDcTF();
            }
        });

        rejoinButtonYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event.manageDc(onOff);
                event.resetProtocall();
                event.updateScoring();
                dcEnable();
                dcCheck();
                setStatus();
                resetBox.setSelected(false);
            }
        });

        rejoinButtonNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomReset();
                event.dcStandby();
                if(event.inDcStandby==false){
                    event.updateScoring();
                    event.manageDc(onOff);
                    eventDoneCheck();
                    dcCheck();
                    setStatus();
                }else{
                    event.updateScoring();
                    eventDoneCheck();
                    dcCheck();
                    setStatusDc();
                }
                if(event.isEventDone()){
                    setStatusEnd();
                    nextButton.setEnabled(false);
                    submitButton.setEnabled(true);
                    dcButtonOn.setEnabled(false);
                    dcButtonOff.setEnabled(false);
                }
                resetBox.setSelected(false);
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    Thread.sleep(150);
                }catch(InterruptedException j){
                    System.out.println(j.getMessage());
                }

                String preTrack;
                String preStart ;
                String preFinish ;
                String prePlayers ;

                Race preRace = event.getMostRecentlyCompletedRace();

                if(preRace.isPlaceholder()==false){
                    preTrack = preRace.getTrack().getAbbreviation();
                    preStart = String.valueOf(preRace.getStart());
                    preFinish = String.valueOf(preRace.getFinish());
                    prePlayers = String.valueOf(preRace.getPlayers());
                }else{
                    preTrack = "";
                    preStart = "";
                    preFinish = "";
                    prePlayers = "";
                }
                event.undoProtocall();
                event.preRaceStatus();

                startTf.setText(preStart);
                finishTf.setText(preFinish);
                trackTf.setText(preTrack);
                playersTf.setText(prePlayers);

                dcCheck();
                if(submitButton.isEnabled()){
                    submitButton.setEnabled(false);
                }
                if(nextButton.isEnabled()==false){
                    if(event.isInDcStandby()==false){
                        nextButton.setEnabled(true);
                    }
                }
                if(event.getRacesPlayed()==0){
                    backButton.setEnabled(true);
                    undoButton.setEnabled(false);
                }
                if(event.isInDcStandby()==false){
                    setStatus();
                }else{
                    setStatusDc();
                }
                resetBox.setSelected(false);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextRace();
            }
        });

        trackTf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    nextRace();
                }
            }

        });

        startTf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    nextRace();
                }
            }

        });

        finishTf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    nextRace();
                }
            }

        });

       playersTf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    nextRace();
                }
            }

        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RaceDao.add(event);
                RaceDao.storeEvent();
                RaceDao.refresh();
                event = null;
                status = "n/a";
                card.show(cardPane,"mainMenu");
                Gui.frame.setSize(Gui.defaultWidth,Gui.defaultHeight);
                setInitialButtons();
            }
        });

        trackTf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                matchLabelUpdate();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                matchLabelUpdate();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                matchLabelUpdate();
            }
        });
    }

    public void  setEvent(Event currentEvent){
        if(event==null) {
            event = currentEvent;
            status = "[Event"+event.getEventId()+"] Tier:"+event.getTier()+" Format:"+event.getFormat();
        }else{
            System.out.println("event initialization error, static eventD placeholder was not null after tier/format input");
        }
    }

    public void setStatus(){
        statusTA.setText(event.preRaceString());
    }

    public void setTitle(){
        titleLabel.setText("[Event"+event.getEventId()+"] T"+event.getTier().getTier()+" "+event.getFormat().getFormat());
    }

    public void setStatusEnd(){
        statusTA.setText("12 races have been played, press submit to store"+"\n"+
                "Points:"+event.getRacePoints()+" DC Points:"+event.getDcPoints()+" Total Points:"+event.getFinalPoints());
    }
    public void setStatusDc(){
        statusTA.setText("You've dc'd from the last race, select Yes if rejoined"+"\n"+
                event.preRaceString());
    }

    private void dcDisable(){
        nextButton.setEnabled(false);

        dcButtonOn.setEnabled(false);
        backButton.setEnabled(false);
        rejoinButtonNo.setEnabled(true);
        rejoinButtonYes.setEnabled(true);
    }

    private void dcEnable(){
        nextButton.setEnabled(true);

        dcButtonOn.setEnabled(true);
        rejoinButtonNo.setEnabled(false);
        rejoinButtonYes.setEnabled(false);
        if(event.getRacesPlayed()==0){
            backButton.setEnabled(true);
        }
    }


    private void setPostRaceTFRandom(){
        Random random = new Random();
        trackTf.setText(Track.randomTrack().getAbbreviation());
        if(event.currentGpIsUnplayed()){
            int pls = random.nextInt(12)+1;
            startTf.setText(String.valueOf(pls));
        }else{
            startTf.setText(finishTf.getText());
        }
        int pls = random.nextInt(12)+1;
        finishTf.setText(String.valueOf(pls));
    }

    public void roomReset(){
        boolean reset = resetBox.isSelected();

        if(reset){
            event.resetProtocall();
            event.postRacestatus();
            event.preRaceStatus();
            resetBox.setSelected(false);
            mogiUpdater=new MogiUpdater();
            reset = true;
            resetShift = event.getRacesPlayed();
        }
    }

    public void eventDoneCheck(){
        if(event.getRacesPlayed()>0){
            undoButton.setEnabled(true);
        }
        if(event.getRacesPlayed()>=12){
            setStatusEnd();
            dcButtonOn.setEnabled(false);
            dcButtonOff.setEnabled(false);
            nextButton.setEnabled(false);
            submitButton.setEnabled(true);
            rejoinButtonNo.setEnabled(false);
            rejoinButtonYes.setEnabled(false);
        }
        backButton.setEnabled(false);
    }

    public void matchLabelUpdate(){
        String input = trackTf.getText();
        if(InputVerifier.verifyTrack(input)){
            trackMatchLabel.setText(Track.fromString(input).get().getFullName());
        }else{
            trackMatchLabel.setText("No Track Found");
        }
    }

    public void unplayedOrCompletedCheck(boolean onResults){
        if(event.currentGp.isUnplayed()){
            System.out.println("The current GP is unplayed , ");
            System.out.println("since you have already dc'd one placeholder race will be generated");
            event.dcStandby();
            return;
        }
        if(event.currentGp.getRemainingRacesInGp()==1){
            System.out.println("you have disconnected from the last race of the Gp");
            event.dcStandby();
            event.manageDc(onResults);
            return;
        }
        event.dcStandby();
    }

    public void dcCheck(){
        if(event.isInDcStandby()){
            dcButtonOn.setEnabled(false);
            dcButtonOff.setEnabled(false);
            rejoinButtonNo.setEnabled(true);
            rejoinButtonYes.setEnabled(true);
            nextButton.setEnabled(false);
            setStatusDc();
        }else{
            dcButtonOn.setEnabled(true);
            dcButtonOff.setEnabled(true);
            nextButton.setEnabled(true);
            rejoinButtonNo.setEnabled(false);
            rejoinButtonYes.setEnabled(false);
            setStatus();
        }
        if(event.getRacesPlayed()==0){
            backButton.setEnabled(true);
        }
    }

    public void setInitialButtons() {
        nextButton.setEnabled(true);
        backButton.setEnabled(true);
        dcButtonOff.setEnabled(true);
        dcButtonOn.setEnabled(true);

        undoButton.setEnabled(false);
        submitButton.setEnabled(false);
        rejoinButtonNo.setEnabled(false);
        rejoinButtonYes.setEnabled(false);
    }

    public void setTrackTf(){
        trackTf.setText(Track.randomTrack().getAbbreviation());
    }

    private void setPostDcTF(){
        trackTf.setText("");

        if(event.getNumberOfCompletedGps() == 0){
            //no completed gps
            if(event.currentGpIsUnplayed()){
                //first gp, before first race
                startTf.setText("");
            } else if(event.currentGp.getRacesPlayedInGp()==1) {
                //after the first race of the first GP
                gpStart = 0;
                System.out.println("after the first race of the first GP, gpStart is now: "+gpStart);
                startTf.setText("");
            }else if(event.currentGp.getRacesPlayedInGp()>1&&event.currentGp.getRacesPlayedInGp()<event.currentGp.getMaxRaces()){
                //middle of first gp, after first before the max number of races in the gp are completed
                startTf.setText("");
            }
        }else{
            if(event.currentGpIsUnplayed()){
                //after at least 1 gp is played, but before the first race of a new gp is played
                System.out.println("at the start of a new gp, the default startTf text is the gpStart value of:" +gpStart);
                gpStart = 0;
                startTf.setText("");
                System.out.println("");
            }else if(event.currentGp.oneRaceIsPlayed()){
                //after at least 1 gp has been  played, and one race has been played in the new gp
                gpStart = 0;
                startTf.setText("");
            }else if(event.currentGp.getRacesPlayedInGp()>1&&event.currentGp.getRacesPlayedInGp()<event.currentGp.getMaxRaces()){
                //after at least 1 gp has been  played, and more than 1 race has been played in the gp
                startTf.setText("");
                gpStart = 0;
            }
        }
        finishTf.setText("");
        trackTf.requestFocusInWindow();
    }

    private void setPostRaceTF(){
        trackTf.setText("");

        if(event.getNumberOfCompletedGps() == 0){
            //no completed gps
            if(event.currentGpIsUnplayed()){
                //first gp, before first race
                startTf.setText("");
            } else if(event.currentGp.getRacesPlayedInGp()==1) {
                //after the first race of the first GP
                gpStart = Integer.parseInt(startTf.getText());
                System.out.println("after the first race of the first GP, gpStart is now: "+gpStart);
                startTf.setText(finishTf.getText());
            }else if(event.currentGp.getRacesPlayedInGp()>1&&event.currentGp.getRacesPlayedInGp()<event.currentGp.getMaxRaces()){
                //middle of first gp, after first before the max number of races in the gp are completed
                startTf.setText(finishTf.getText());
            }
        }else{
            if(event.currentGpIsUnplayed()){
                //after at least 1 gp is played, but before the first race of a new gp is played
                System.out.println("at the start of a new gp, the default startTf text is the gpStart value of:" +gpStart);
                startTf.setText(String.valueOf(gpStart));
                playersTf.setText("12");
                System.out.println("");
            }else if(event.currentGp.oneRaceIsPlayed()){
                //after at least 1 gp has been  played, and one race has been played in the new gp
                startChange = (Integer.parseInt(startTf.getText())!= gpStart);
                if(startChange) {
                    gpStart = Integer.parseInt(startTf.getText());
                    System.out.println("since the start position of a new gp has been changed the new gpStart default is: "+gpStart);
                    startTf.setText(finishTf.getText());
                }else{
                    System.out.println("the initial start position to begin a gp has not changed");
                    startTf.setText(finishTf.getText());
                }
            }else if(event.currentGp.getRacesPlayedInGp()>1&&event.currentGp.getRacesPlayedInGp()<event.currentGp.getMaxRaces()){
                //after at least 1 gp has been  played, and more than 1 race has been played in the gp
                startTf.setText(finishTf.getText());
            }
        }

        finishTf.setText("");
        trackTf.requestFocusInWindow();
    }

    public void nextRace(){
        String startS = startTf.getText();
        String playerS = playersTf.getText();
        String finishS = finishTf.getText();
        String trackS = trackTf.getText();

        if(InputVerifier.verifyTrack(trackS)&&InputVerifier.verifyPlayers(playerS)&&
                InputVerifier.verifySF(startS)&&InputVerifier.verifyFinish(finishS,Integer.parseInt(playerS))){
            try{
                Thread.sleep(150);
            }catch(InterruptedException e){
                System.out.println(e.getMessage());
            }

            roomReset();
            Track track = InputVerifier.getTrack(trackS);
            int players = Integer.parseInt(playerS);
            int start = Integer.parseInt(startS);
            int finish = Integer.parseInt(finishS);

            if(event.getRacesPlayed()==0){
                gpStart = start;
            }

            event.playRace(track, start, finish, players);

            setStatus();
            eventDoneCheck();
            setPostRaceTF();


        }else{
            String tracktrack = "track";
            if(InputVerifier.verifyTrack(trackS)){
                tracktrack = "";
            }
            String playerplayer = "players";
            String finishfinish = "finish";
            if(InputVerifier.verifyPlayers(playerS)){
                playerplayer = "";
                if(InputVerifier.verifyFinish(finishS, Integer.parseInt(playersTf.getText()))){
                    finishfinish = "";
                }
            }
            String startstart = "start";
            if(InputVerifier.verifySF(startS)){
                startstart = "";
            }

            String statement = "invalid: "+tracktrack+" "+playerplayer+" "+startstart+" "+finishfinish;
            InputVerifier.relativePopup(statement, "error", resetBox);
        }
    }

    public void initialize(){
        setTrackTf();
        setTitle();
        setStatus();
        setInitialButtons();
        playersTf.setText("12");
        startTf.setText("");
        finishTf.setText("");
        mogiUpdater = new MogiUpdater();
        mogiUpdater.setUp();
        if(RaceDao.getFc().equals("nope")){
            autoButton.setEnabled(false);
        }else{
            autoButton.setEnabled(true);
        }

        EventQueue.invokeLater( () -> trackTf.requestFocusInWindow() );
    }
}
