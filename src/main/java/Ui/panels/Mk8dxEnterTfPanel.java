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
    JLabel titleLabel = new JLabel("Entering MK8DX Event ["+ (RaceDao.getEventsDStored()+1)+"]");
    JLabel tierLabel = new JLabel("Enter Tier:");
    JLabel formatLabel = new JLabel("Enter Format");
    JTextField tierTf = new JTextField("f");
    JTextField formatTf = new JTextField("2");

    EventD event;

    public Mk8dxEnterTfPanel(CardLayout card, JPanel cardPane, Mk8dxRaceInputPanel panel) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(40,5,40,5);

        Setter s = new Setter();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        s.addobjects(titleLabel,this, layout,gbc,0,0,2 , 1,1,1);

        tierLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        s.addobjects(tierLabel,this, layout,gbc,0,2,1 , 1,1,1);
        tierTf.setFont(new Font("Arial", Font.PLAIN, 18));
        s.addobjects(tierTf,this, layout,gbc,1,2,1 , 1,1,1);

        formatLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        s.addobjects(formatLabel,this, layout,gbc,0,3,1 , 1,1,1);
        formatTf.setFont(new Font("Arial", Font.PLAIN, 18));
        s.addobjects(formatTf,this, layout,gbc,1,3,1 , 1,1,1);

        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        s.addobjects(startButton,this, layout,gbc,1,4,1 , 1,1,1);
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        s.addobjects(backButton,this, layout,gbc,0,4,1, 1,1,1);

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

                    panel.setTrackTf();
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
                card.show(cardPane,"mainMenu");
            }
        });
    }

    public void initialize(){
        titleLabel.setText("Entering MK8DX Event ["+ (RaceDao.getEventsDStored()+1)+"]");
        tierTf.setText(RaceDao.getDefaultMk8dxTier());
    }
}
