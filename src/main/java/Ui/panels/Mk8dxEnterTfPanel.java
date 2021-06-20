package Ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import Ui.RaceDao;
import mk8dx.*;
import shared.Format;

public class Mk8dxEnterTfPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();
    JButton startButton = new JButton("Start");
    JButton backButton = new JButton("Back");
    JLabel titleLabel = new JLabel("mk8dx enter tier,format");
    JLabel tierLabel = new JLabel("Enter Tier:");
    JLabel formatLabel = new JLabel("Enter Format");
    JTextField tierTf = new JTextField();
    JTextField formatTf = new JTextField();

    EventD event;

    public Mk8dxEnterTfPanel(CardLayout card, JPanel cardPane, Mk8dxRaceInputPanel panel) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(40,5,40,5);

        Setter s = new Setter();
        s.addobjects(titleLabel,this, layout,gbc,0,0,1 , 1);

        s.addobjects(tierLabel,this, layout,gbc,0, 2,1,1,0.25,0);
        tierTf.setText("f");
        s.addobjects(tierTf,this, layout,gbc,1, 2,1,1, 0.75,0);
        formatTf.setText("1");
        s.addobjects(formatLabel,this, layout,gbc,0, 3,1,1,0.25,0);
        s.addobjects(formatTf,this, layout,gbc,1, 3,1,1,0.75,0);

        s.addobjects(startButton,this, layout,gbc,1,4,1, 1);
        s.addobjects(backButton,this, layout,gbc,0, 4,1,1);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String tierInput = tierTf.getText();
                String formatInput = formatTf.getText();

                if(InputVerifier.VerifyTierD(tierInput)&&InputVerifier.verifyFormat(formatInput)){
                    TierD tier = InputVerifier.getTierD(tierInput);
                    Format format = InputVerifier.getFormat(formatInput);
                    int eventsPlayed = RaceDao.getEventsDStored();
                    event = new EventD(tier,format,eventsPlayed);

                    int racesPlayed = RaceDao.getRacesDStored();
                    RaceD.setRaceCount(racesPlayed);

                    panel.setEvent(event);
                    panel.setTitle();
                    panel.setStatus();
                    card.show(cardPane,"mk8dxRace");
                }else{
                    if(InputVerifier.VerifyTierD(tierInput)==false&&InputVerifier.verifyFormat(formatInput)==false){
                        InputVerifier.InputErrorBox("Invalid Tier and Format");
                    }else if(InputVerifier.verifyFormat(formatInput)==false){
                        InputVerifier.InputErrorBox("Invalid Format");
                    }
                    else{
                        InputVerifier.InputErrorBox("Invalid tier");
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mk8dxMenu");
            }
        });
    }
}
