package Ui.panels;

import Ui.Gui;
import Ui.RaceDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("LOUNGE");

    JButton storeMkwButton = new JButton("New MKW Event");
    JButton storeMk8dxButton = new JButton("New MK8DX Event");
    JButton mkwTrackButton = new JButton ("MKW Track Analysis");
    JButton mk8dxTrackButton = new JButton("MK8DX Track Analysis");
    JButton mkwSummaryButton = new JButton("MKW Event Analysis");
    JButton mk8dxSummaryButton = new JButton("MK8DX Event Analysis");
    JButton mkwAdvButton = new JButton("MKW Advanced");
    JButton mk8dxAdvButton = new JButton("MK8DX Advanced");
    JButton settingsButton = new JButton("Settings");

    JTextArea randomStatus = new JTextArea("Program has randomly generated events,"+"\n"+
            "clear all events in settings to enable event storage");
    JTextArea fcStatus = new JTextArea("Set fc in settings to enable"+"\n"+
            "race autofill for mkw races");



    public MainMenu(CardLayout card, JPanel cardPane, MkwEnterTfPanel mkwEnterTfPanel, Mk8dxEnterTfPanel mk8dxEnterTfPanel,
                    MkwSummaryPanel mkwSummaryPanel, Mk8dxSummaryPanel mk8dxSummaryPanel,
                    MkwAdvStatsPanel mkwAdvStatsPanel, Mk8dxAdvStatsPanel mk8dxAdvStatsPanel, SettingsPanel settingsPanel,
                    MkwTrackPanel mkwTrackPanel, Mk8dxTrackPanel mk8dxTrackPanel) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        initialize();

        gbc.insets = new Insets(2,10,2,10);

        Setter s = new Setter();


        titleLabel.setFont(new Font("Comic Sans", Font.BOLD, 42));
        titleLabel.setForeground(Color.DARK_GRAY);

        s.addobjects(titleLabel,this, layout,gbc,0,0,1 , 1,1,0.5 ,false, true);
        s.addobjects(settingsButton,this, layout,gbc,1,0,1 , 1,1,1, false, false);

        gbc.insets = new Insets(10,20,10,20);

        randomStatus.setEditable(false);
        s.addobjects(randomStatus,this, layout,gbc,0,1,2 , 1,.000001,1, false, false);

        s.addobjects(storeMkwButton,this, layout,gbc,0,2,1 , 1,1,1);
        s.addobjects(storeMk8dxButton,this, layout,gbc,1,2,1 , 1,1,1);

        s.addobjects(mkwTrackButton,this, layout,gbc,0,3,1 , 1,1,1);
        s.addobjects(mk8dxTrackButton,this, layout,gbc,1,3,1 , 1,1,1);

        s.addobjects(mkwSummaryButton,this, layout,gbc,0,4,1 , 1,1,1);
        s.addobjects(mk8dxSummaryButton,this, layout,gbc,1,4,1 , 1,1,1);

        s.addobjects(mkwAdvButton,this, layout,gbc,0,5,1 , 1,1,1);
        s.addobjects(mk8dxAdvButton,this, layout,gbc,1,5,1, 1,1,1);

        randomStatus.setEditable(false);
        s.addobjects(fcStatus,this, layout,gbc,0,6,2 , 1,.000001,1, false, false);




        storeMkwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.defaultWidth,Gui.defaultHeight);
                card.show(cardPane,"mkwTf");
                mkwEnterTfPanel.initialize();
            }
        });

        storeMk8dxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.defaultWidth,Gui.defaultHeight);
                card.show(cardPane,"mk8dxTf");
                mk8dxEnterTfPanel.initialize();
            }
        });

        mkwTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.bigWidth,Gui.bigHeight);
                card.show(cardPane,"mkwTrack");
                mkwTrackPanel.initialize();
            }
        });

        mk8dxTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.bigWidth,Gui.bigHeight);
                card.show(cardPane,"mk8dxTrack");
                mk8dxTrackPanel.initialize();
            }
        });

        mkwSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.bigWidth,Gui.bigHeight);
                card.show(cardPane,"mkwStats");
                mkwSummaryPanel.initialize();
            }
        });

        mk8dxSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.bigWidth,Gui.bigHeight);
                card.show(cardPane,"mk8dxStats");
                mk8dxSummaryPanel.initialize();
            }
        });

        mkwAdvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.bigWidth,Gui.bigHeight);
                card.show(cardPane,"mkwAdvStats");
                mkwAdvStatsPanel.initialize();
            }
        });

        mk8dxAdvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.bigWidth,Gui.bigHeight);
                card.show(cardPane,"mk8dxAdvStats");
                mk8dxAdvStatsPanel.initialize();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.defaultWidth,Gui.defaultHeight);
                card.show(cardPane,"settings");
                settingsPanel.initialize();
            }

        });
    }

    public void initialize(){

        if(RaceDao.isRandom()){
            storeMkwButton.setEnabled(false);
            storeMk8dxButton.setEnabled(false);
            randomStatus.setVisible(true);
        }else{
            storeMkwButton.setEnabled(true);
            storeMk8dxButton.setEnabled(true);
            randomStatus.setVisible(false);
        }
        if(RaceDao.getFc().equals("nope")){
            fcStatus.setVisible(true);
        }else{
            fcStatus.setVisible(false);
        }
    }

}
