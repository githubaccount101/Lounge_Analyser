package Ui.panels;

import Ui.Gui;
import Ui.RaceDao;
import mk8dx.*;
import mkw.Race;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Mk8dxRaceInputPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("mk8dx race input");

    JLabel statusLabel = new JLabel("Status:");
    JTextArea statusTA = new JTextArea();

    JLabel trackLabel = new JLabel("Track:");
    JLabel startLabel = new JLabel("Start Position(1-12):");
    JLabel finishLabel = new JLabel("Finish Position(1-12):");
    JLabel rejoinLabel = new JLabel("Rejoined?");
    JLabel trackMatchLabel = new JLabel("No Track Found");
    JLabel dcLabel = new JLabel("If DC:");

    JTextField trackTf = new JTextField(TrackD.randomTrackD().getAbbreviation());
    JTextField startTf = new JTextField("12");
    JTextField finishTf = new JTextField("12");

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
        s.addobjects(titleLabel,this, layout,gbc,0,0,3 , 1, 1,0.1,true);

        s.addobjects(statusLabel,this, layout,gbc,0,1,1, 1,0.00001,1,true);
        s.addobjects(statusTA,this, layout,gbc,1,1,2 , 2,1,1,true);

        s.addobjects(trackLabel,this, layout,gbc,0, 5,1,1,0.000001,0.1,true);
        s.addobjects(trackTf,this, layout,gbc,1, 5,2,1, 1,0.1,true);

        matchLabelUpdate();
        s.addobjects(trackMatchLabel,this, layout,gbc,1, 6,3,1,1,0.1,true);

        s.addobjects(finishLabel,this, layout,gbc,1, 7,1,1,.00001,0.1,true);
        s.addobjects(finishTf,this, layout,gbc,2, 7,1,1,1,0.1,true);

        s.addobjects(startLabel,this, layout,gbc,1, 8,1,1,.00001,0.1,true);
        s.addobjects(startTf,this, layout,gbc,2, 8,1,1, 1,0.1,true);

        s.addobjects(resetBox,this, layout,gbc,1, 9,1,1,1,0.1,true);

        undoButton.setEnabled(false);
        s.addobjects(undoButton,this, layout,gbc,1, 10,1,1,1,0.1,true);
        s.addobjects(nextButton,this, layout,gbc,2, 10,1,1,1,0.1,true);

        submitButton.setEnabled(true);
        s.addobjects(backButton,this, layout,gbc,1, 11,1,1,1,0.1,true);
        s.addobjects(submitButton,this, layout,gbc,2, 11,1,1,1,0.1,true);

        s.addobjects(dcLabel,this, layout,gbc,0, 12,1,1,0.00001,0.1,true);
        s.addobjects(dcButtonOn,this, layout,gbc,1,12,2 , 1, 1 , 0.1,true);

        s.addobjects(rejoinLabel,this, layout,gbc,0, 13,1,1,0.0000001,0.1,true);
        rejoinButtonNo.setEnabled(true);
        s.addobjects(rejoinButtonNo,this, layout,gbc,1, 13,1,1,1,0.1,true);
        rejoinButtonYes.setEnabled(true);
        s.addobjects(rejoinButtonYes,this, layout,gbc,2, 13,1,1,1,0.1,true);



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                event = null;
                status = "n/a";
                Gui.frame.setSize(450,480);
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
                try{
                    Thread.sleep(150);
                }catch(InterruptedException j){
                    System.out.println(j.getMessage());
                }

                RaceD preRace = event.getMostRecentlyCompletedRace();
                String preTrack = preRace.getTrack().getAbbreviation();
                String preStart = String.valueOf(preRace.getStart());
                String preFinish = String.valueOf(preRace.getFinish());

                event.undoProtocall();
                event.preRaceStatus();

                startTf.setText(preStart);
                finishTf.setText(preFinish);
                trackTf.setText(preTrack);

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

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RaceDao.addD(event);
                RaceDao.storeEventD();
                RaceDao.refresh();
                event = null;
                status = "n/a";
                Gui.frame.setSize(450,480);
                card.show(cardPane,"mainMenu");
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
        trackTf.requestFocusInWindow();
    }

    private void setPostRaceTFRandom(){
        Random random = new Random();
        trackTf.setText(TrackD.randomTrackD().getAbbreviation());
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



    public void setTrackTf(){
        trackTf.setText(TrackD.randomTrackD().getAbbreviation());
    }

    public void nextRace(){

        String startS = startTf.getText();
        String finishS = finishTf.getText();
        String trackS = trackTf.getText();

        if(InputVerifier.verifySF(startS)&&InputVerifier.verifySF(finishS)&&InputVerifier.verifyTrackD(trackS)){
            try{
                Thread.sleep(150);
            }catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
            roomReset();
            TrackD track = InputVerifier.getTrackD(trackS);
            int start = Integer.parseInt(startS);
            int finish = Integer.parseInt(finishS);
            event.playRace(track, start, finish, 12);

            setStatus();
            eventDoneCheck();
            setPostRaceTF();
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
                finishfinish = "";
            }
            String statement = "invalid: "+tracktrack+" "+startstart+" "+finishfinish;
            InputVerifier.InputErrorBox(statement);
        }
    }

    public void initialize(){
        setTrackTf();
        setTitle();
        setStatus();
        setInitialButtons();
        startTf.setText("");
        finishTf.setText("");
        EventQueue.invokeLater( () -> trackTf.requestFocusInWindow() );
    }
}

