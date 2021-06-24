package Ui.panels;

import Ui.Gui;
import Ui.RaceDao;

import javax.management.StringValueExp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SettingsPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("Settings");

    JLabel dbLabel = new JLabel("");

    JLabel mkwTierLabel = new JLabel("Set new default MKW tier:");
    JTextField mkwTierTf = new JTextField("");
    JButton mkwTierButton = new JButton("Set");

    JLabel currentMkwTierLabel = new JLabel();

    JLabel mk8dxTierLabel = new JLabel("Set new default MK8DX tier:");
    JTextField mk8dxTierTf = new JTextField("");
    JButton mkw8dxTierButton = new JButton("Set");

    JLabel currentMk8dxTierLabel = new JLabel();

    JLabel randomLabel = new JLabel("Generate random events:");
    JTextField randomTf = new JTextField("");
    JButton randomButton = new JButton("Generate");

    JLabel randomWarningLabel = new JLabel("(limit 1000)");
    JProgressBar randomBar = new JProgressBar();

    JButton resetButton = new JButton("Clear All Events");

    JButton buttonBack = new JButton("Back");

    ArrayList<JButton> allButtons = new ArrayList<>();

    MainMenu mainMenu;

    public SettingsPanel(CardLayout card, JPanel cardPane) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(10,10,10,10);

        Setter s = new Setter();

        gbc.anchor = GridBagConstraints.LINE_START;

        titleLabel.setFont(new Font("Comic Sans", Font.BOLD, 25));
        s.addobjects(titleLabel,this, layout,gbc,0,0,3 , 1,1,1,true);

        s.addobjects(dbLabel,this, layout,gbc,0,1,3 , 1,1,1,true);

        s.addobjects(mkwTierLabel,this, layout,gbc,0, 2,1,1,1,1, true);
        s.addobjects(mkwTierTf,this, layout,gbc,1, 2,1,1,100,1, true);
        s.addobjects(mkwTierButton,this, layout,gbc,2, 2,1,1,1,1, true);

        s.addobjects(currentMkwTierLabel,this, layout,gbc,0, 3,1,1,1,1, true);

        s.addobjects(mk8dxTierLabel,this, layout,gbc,0, 4,1,1,1,1, true);
        s.addobjects(mk8dxTierTf,this, layout,gbc,1, 4,1,1,100,1, true);
        s.addobjects(mkw8dxTierButton,this, layout,gbc,2, 4,1,1,1,1, true);

        s.addobjects(currentMk8dxTierLabel,this, layout,gbc,0, 5,1,1,1,1, true);

        s.addobjects(randomLabel,this, layout,gbc,0, 6,1,1,1,1, true);
        s.addobjects(randomTf,this, layout,gbc,1, 6,1,1,100,1, true);
        s.addobjects(randomButton,this, layout,gbc,2, 6,1,1,1,1, true);

        s.addobjects(randomWarningLabel,this, layout,gbc,0, 7,1,1,1,1, true);

        s.addobjects(randomBar,this, layout,gbc,0, 8,3,1,1, 1,true);
        randomBar.setVisible(false);

        s.addobjects(resetButton,this, layout,gbc,0, 9,3,1, 2, 1,true);

        s.addobjects(buttonBack,this, layout,gbc,0,10,3, 1, 2 ,1, true);

        fillAllButtons();

        resetButton.setOpaque(false);
        resetButton.setContentAreaFilled(false);


        mkwTierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(InputVerifier.VerifyTier(mkwTierTf.getText())){
                    RaceDao.updateDefaultTierMkw(Integer.parseInt(mkwTierTf.getText()));
                    initialize();
                    InputVerifier.InputErrorBox("Default Tier set to "+mkwTierTf.getText());
                    mkwTierTf.setText("");
                }else{
                    InputVerifier.InputErrorBox("Invalid tier");
                }
            }
        });

        mkw8dxTierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(InputVerifier.VerifyTierD(mk8dxTierTf.getText())){
                    RaceDao.updateDefaultTierMk8dx(mk8dxTierTf.getText());
                    initialize();
                    InputVerifier.InputErrorBox("Default Tier set to "+mk8dxTierTf.getText());
                    mk8dxTierTf.setText("");
                }else{
                    InputVerifier.InputErrorBox("Invalid tier");
                }
            }
        });

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(420,480);
                card.show(cardPane,"mainMenu");
                mainMenu.initialize();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(null, "Are you sure? This will also re-enable event storage",
                        "Confirm", JOptionPane.YES_NO_OPTION);
                if(a==0){
                    System.out.println("Clearing everything");
                    RaceDao.resetTables();
                    RaceDao.updateRandom(false);
                }
                if(a>0){
                    System.out.println("no");
                }
                initialize();
            }
        });

        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(InputVerifier.verifyRandom(randomTf.getText())){
                    int a = JOptionPane.showConfirmDialog(null, "This will clear all existing events and disable" + "\n"+
                                    "the input of new events until all events are cleared again."  + "\n"+
                                    "An equal of random event will be generated for both games."+ "\n"+
                                    "Generating more random events will take more time, up to several"+ "\n"+
                                    "minutes for 1000. The program will be disabled until"+ "\n"+
                                    "the all the events have been generated. You can do this before"+ "\n"+
                                    "you start storing your own events to get a feel for the program's"+ "\n"+
                                    "analysis functions for either game...",
                            "Confirm", JOptionPane.YES_NO_OPTION);
                    if(a==0){
                        try{
                            inception();
                        }catch(Exception s){
                            System.out.println(s.getMessage());
                        }

                    }
                    if(a>0){
                        System.out.println("no");
                    }
                }else{
                    InputVerifier.InputErrorBox("Invalid Number");
                }
                initialize();
            }
        });
    }

    public void initialize(){
        currentMkwTierLabel.setText("(Current Default is "+String.valueOf(RaceDao.getDefaultMkwTier())+")");
        currentMk8dxTierLabel.setText("(Current Default is "+RaceDao.getDefaultMk8dxTier()+")");
        if(RaceDao.isRandom()){
            randomWarningLabel.setText("(limit 1000) - Random Mode is On");
        }else{
            randomWarningLabel.setText("(limit 1000) - Random Mode is Off");
        }
        dbLabel.setText(RaceDao.getEventsStored()+" MKW events stored, "+RaceDao.getEventsDStored()+" MK8DX events stored");
    }

    public void fillAllButtons(){
        allButtons.add(mkwTierButton);
        allButtons.add(mkw8dxTierButton);
        allButtons.add(randomButton);
        allButtons.add(resetButton);
        allButtons.add(buttonBack);
    }

    public void enableAllButtons(boolean onOff){
        allButtons.forEach(x->x.setEnabled(onOff));
    }

    public void setMainMenu(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }

    private void inception(){
        new BarUpdate(randomBar,Integer.valueOf(randomTf.getText()),randomWarningLabel, allButtons,
                randomWarningLabel, dbLabel,randomTf).execute();
    }

    @Deprecated
    public void processInBackground(){
        SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                System.out.println("randomizing");
                RaceDao.updateRandom(true);
                RaceDao.resetTables();
                enableAllButtons(false);
                randomBar.setVisible(true);
                RaceDao.generateRandom(Integer.parseInt(randomTf.getText()));
                randomTf.setText("");
                return null;
            }

            @Override
            protected void done() {
                System.out.println("now im done...");
                enableAllButtons(true);
                randomBar.setVisible(false);
                initialize();
                JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("did it work?");
            }


        };
        worker.execute();
    }


}
