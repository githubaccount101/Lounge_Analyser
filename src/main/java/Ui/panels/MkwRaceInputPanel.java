package Ui.panels;

import Ui.RaceDao;

import Ui.Ui;
import mkw.*;
import mkw.Event;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MkwRaceInputPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("mkw race input");

    JLabel statusLabel = new JLabel("Status:");
    JTextArea statusTA = new JTextArea();

    JLabel trackLabel = new JLabel("Track:");
    JLabel playersLabel = new JLabel("Players on Results(10-12)");
    JLabel startLabel = new JLabel("Start Position(1-12)");
    JLabel finishLabel = new JLabel("Finish Position(1-Players)");
    JLabel rejoinLabel = new JLabel("Rejoined?");
    JLabel trackMatchLabel = new JLabel("No Track Found");
    JLabel dcLabel = new JLabel("If DC:");

    JTextField trackTf = new JTextField("lc");
    JTextField playersTf = new JTextField("12");
    JTextField startTf = new JTextField("5");
    JTextField finishTf = new JTextField("5");


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

    boolean onOff = false;

    public MkwRaceInputPanel(CardLayout card, JPanel cardPane){

        GridBagLayout layout = new GridBagLayout();
        statusTA.setEditable(false);

        setLayout(layout);
        gbc.insets = new Insets(2,10,2,10);

        Setter s = new Setter();
        s.addobjects(titleLabel,this, layout,gbc,0,0,3 , 1, 1,0.1,false);

        s.addobjects(statusLabel,this, layout,gbc,0,1,1, 1,0.1,1,true);
        s.addobjects(statusTA,this, layout,gbc,1,1,2 , 2,1,1,true);

        s.addobjects(trackLabel,this, layout,gbc,0, 5,1,1,0.1,0.1,false);
        s.addobjects(trackTf,this, layout,gbc,1, 5,2,1, 1,0.1,false);

        matchLabelUpdate();
        s.addobjects(trackMatchLabel,this, layout,gbc,1, 6,3,1,1,0.1,false);

        s.addobjects(playersLabel,this, layout,gbc,1, 7,1,1,1,0.1,false);
        s.addobjects(playersTf,this, layout,gbc,2, 7,1,1, 1,0.1,false);

        s.addobjects(startLabel,this, layout,gbc,1, 8,1,1,1,0.1,false);
        s.addobjects(startTf,this, layout,gbc,2, 8,1,1, 1,0.1,false);

        s.addobjects(finishLabel,this, layout,gbc,1, 9,1,1,1,0.1,false);
        s.addobjects(finishTf,this, layout,gbc,2, 9,1,1,1,0.1,false);

        s.addobjects(resetBox,this, layout,gbc,1, 10,1,1,1,0.1,false);

        undoButton.setEnabled(false);
        s.addobjects(undoButton,this, layout,gbc,1, 11,1,1,1,0.1,false);
        s.addobjects(nextButton,this, layout,gbc,2, 11,1,1,1,0.1,false);

        submitButton.setEnabled(false);
        s.addobjects(backButton,this, layout,gbc,1, 12,1,1,1,0.1,false);
        s.addobjects(submitButton,this, layout,gbc,2, 12,1,1,1,0.1,false);

        s.addobjects(dcLabel,this, layout,gbc,0, 13,1,1,0.1,0.1,false);
        s.addobjects(dcButtonOn,this, layout,gbc,1,13,1 , 1, 1 , 0.1,false);
        s.addobjects(dcButtonOff,this, layout,gbc,2,13,1 , 1, 1 , 0.1,false);

        s.addobjects(rejoinLabel,this, layout,gbc,0, 14,1,1,0.1,0.1,false);
        rejoinButtonNo.setEnabled(false);
        s.addobjects(rejoinButtonNo,this, layout,gbc,1, 14,1,1,1,0.1,false);
        rejoinButtonYes.setEnabled(false);
        s.addobjects(rejoinButtonYes,this, layout,gbc,2, 14,1,1,1,0.1,false);



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event = null;
                status = "n/a";
                card.show(cardPane,"mkwTf");
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
                event.undoProtocall();
                event.preRaceStatus();
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
                String startS = startTf.getText();
                String playerS = playersTf.getText();
                String finishS = finishTf.getText();
                String trackS = trackTf.getText();

                if(InputVerifier.verifyTrack(trackS)&&InputVerifier.verifyPlayers(playerS)&&
                            InputVerifier.verifySF(startS)&&InputVerifier.verifyFinish(finishS,Integer.parseInt(playerS))){
                    roomReset();
                    Track track = InputVerifier.getTrack(trackS);
                    int players = Integer.parseInt(playerS);
                    int start = Integer.parseInt(startS);
                    int finish = Integer.parseInt(finishS);
                    event.playRace(track, start, finish, players);

                    setStatus();
                    eventDoneCheck();
                }else{
                    String tracktrack = "track";
                    if(InputVerifier.verifyTrack(trackS)){
                        tracktrack = "";
                    }
                    String playerplayer = "players";
                    if(InputVerifier.verifyPlayers(playerS)){
                        playerplayer = "";
                    }
                    String startstart = "start";
                    if(InputVerifier.verifySF(startS)){
                        startstart = "";
                    }
                    String finishfinish = "finish";
                    if(InputVerifier.verifySF(finishS)){
                        finishfinish = "";
                    }
                    String statement = "invalid: "+tracktrack+" "+playerplayer+" "+startstart+" "+finishfinish;
                    InputVerifier.InputErrorBox(statement);
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
                card.show(cardPane,"mkwMenu");
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
        titleLabel.setText("[Event"+event.getEventId()+"] Tier:"+event.getTier().getTier()+" Format:"+event.getFormat().getFormat());
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

    private void setPostRaceTF(){
        trackTf.setText("");
        if(event.currentGpIsUnplayed()){
            startTf.setText("");
        }else{
            startTf.setText(finishTf.getText());
        }
        finishTf.setText("");
    }

    public void roomReset(){
        boolean reset = resetBox.isSelected();

        if(reset){
            event.resetProtocall();
            event.postRacestatus();
            event.preRaceStatus();
            resetBox.setSelected(false);
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
}
