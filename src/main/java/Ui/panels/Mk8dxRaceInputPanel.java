package Ui.panels;

import Ui.RaceDao;
import mk8dx.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mk8dxRaceInputPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("mk8dx race input");

    JLabel statusLabel = new JLabel("Status:");
    JTextArea statusTA = new JTextArea();

    JLabel trackLabel = new JLabel("Track:");
    JLabel startLabel = new JLabel("Start Position(1-12)");
    JLabel finishLabel = new JLabel("Finish Position(1-12)");
    JLabel rejoinLabel = new JLabel("Rejoined?");
    JLabel trackMatchLabel = new JLabel("No Track Found");
    JLabel dcLabel = new JLabel("If DC:");

    JTextField trackTf = new JTextField("mks");
    JTextField startTf = new JTextField("5");
    JTextField finishTf = new JTextField("5");

    JButton nextButton = new JButton("Next Race");
    JButton backButton = new JButton("Back");
    JButton undoButton = new JButton("Undo Race");
    JButton submitButton = new JButton("Submit");
    JButton dcButtonOn = new JButton("DC before or during race");

    JButton rejoinButtonYes = new JButton("YES");
    JButton rejoinButtonNo = new JButton("NO");

    JCheckBox resetBox = new JCheckBox("Room was Reset" );

    static EventD event = null;
    static String status = "n/a";


    public Mk8dxRaceInputPanel(CardLayout card, JPanel cardPane){

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

        s.addobjects(startLabel,this, layout,gbc,1, 7,1,1,1,0.1,false);
        s.addobjects(startTf,this, layout,gbc,2, 7,1,1, 1,0.1,false);

        s.addobjects(finishLabel,this, layout,gbc,1, 8,1,1,1,0.1,false);
        s.addobjects(finishTf,this, layout,gbc,2, 8,1,1,1,0.1,false);

        s.addobjects(resetBox,this, layout,gbc,1, 9,1,1,1,0.1,false);

        undoButton.setEnabled(false);
        s.addobjects(undoButton,this, layout,gbc,1, 10,1,1,1,0.1,false);
        s.addobjects(nextButton,this, layout,gbc,2, 10,1,1,1,0.1,false);

        submitButton.setEnabled(false);
        s.addobjects(backButton,this, layout,gbc,1, 11,1,1,1,0.1,false);
        s.addobjects(submitButton,this, layout,gbc,2, 11,1,1,1,0.1,false);

        s.addobjects(dcLabel,this, layout,gbc,0, 12,1,1,0.1,0.1,false);
        s.addobjects(dcButtonOn,this, layout,gbc,1,12,2 , 1, 1 , 0.1,false);

        s.addobjects(rejoinLabel,this, layout,gbc,0, 13,1,1,0.1,0.1,false);
        rejoinButtonNo.setEnabled(false);
        s.addobjects(rejoinButtonNo,this, layout,gbc,1, 13,1,1,1,0.1,false);
        rejoinButtonYes.setEnabled(false);
        s.addobjects(rejoinButtonYes,this, layout,gbc,2, 13,1,1,1,0.1,false);



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event = null;
                status = "n/a";
                card.show(cardPane,"mk8dxTf");
            }
        });

        dcButtonOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomReset();
                dcDisable();
                event.dcStandby();
                eventDoneCheck();
                setStatusDc();
                if(event.isEventDone()){
                    setStatusEnd();
                    nextButton.setEnabled(false);
                    submitButton.setEnabled(true);
                }
            }
        });

        rejoinButtonYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomReset();
                event.outOfStandby=true;
                event.updateScoring();
                dcEnable();
                roomReset();
                setStatus();
            }
        });

        rejoinButtonNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomReset();
                event.dcStandby();
                eventDoneCheck();
                if(event.getRacesPlayed()<12){
                    setStatusDc();
                }
                resetBox.setSelected(false);
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event.undoProtocall();

                event.preRaceStatus();
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
                    dcButtonOn.setEnabled(true);
                    setStatus();
                }else{
                    setStatusDc();
                    rejoinButtonNo.setEnabled(true);
                    rejoinButtonYes.setEnabled(true);
                }
                resetBox.setSelected(false);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startS = startTf.getText();
                String finishS = finishTf.getText();
                String trackS = trackTf.getText();

                if(InputVerifier.verifySF(startS)&&InputVerifier.verifySF(finishS)&&InputVerifier.verifyTrackD(trackS)){
                    roomReset();
                    TrackD track = InputVerifier.getTrackD(trackS);
                    int start = Integer.parseInt(startS);
                    int finish = Integer.parseInt(finishS);
                    event.playRace(track, start, finish, 12);

                    setStatus();
                    eventDoneCheck();
                }else{
                    String tracktrack = "track";
                    if(InputVerifier.verifyTrackD(trackS)){
                        tracktrack = "";
                    }
                    String startstart = "start";
                    if(InputVerifier.verifySF(startS)){
                        startstart = "";
                    }
                    String finishfinish = "finish";
                    if(InputVerifier.verifySF(finishS)){
                        startstart = "";
                    }
                    String statement = "invalid: "+tracktrack+" "+startstart+" "+finishfinish;
                    InputVerifier.InputErrorBox(statement);
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RaceDao.addD(event);
                RaceDao.storeEventD();
                RaceDao.refresh();
                event = null;
                status = "n/a";
                card.show(cardPane,"mk8dxMenu");
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

    public void  setEvent(EventD currentEvent){
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
            nextButton.setEnabled(false);
            submitButton.setEnabled(true);
            rejoinButtonNo.setEnabled(false);
            rejoinButtonYes.setEnabled(false);
        }
        backButton.setEnabled(false);
    }

    public void matchLabelUpdate(){
        String input = trackTf.getText();
        if(InputVerifier.verifyTrackD(input)){
            trackMatchLabel.setText(TrackD.fromString(input).get().getFullName());
        }else{
            trackMatchLabel.setText("No Track Found");
        }
    }

    public void setInitialButtons() {
        nextButton.setEnabled(true);
        backButton.setEnabled(true);
        dcButtonOn.setEnabled(true);

        undoButton.setEnabled(false);
        submitButton.setEnabled(false);
        rejoinButtonNo.setEnabled(false);
        rejoinButtonYes.setEnabled(false);
    }
}
