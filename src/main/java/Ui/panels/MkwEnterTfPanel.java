package Ui.panels;

import Ui.RaceDao;
import mk8dx.RaceD;
import mkw.*;
import shared.Format;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MkwEnterTfPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();
    JButton buttonStart = new JButton("Start");
    JButton buttonBack = new JButton("Back");
    JLabel titleLabel = new JLabel("mkw enter tier,format");
    JLabel tierLabel = new JLabel("Enter Tier:");
    JLabel formatLabel = new JLabel("Enter Format");
    JTextField tierTf = new JTextField("1");
    JTextField formatTf = new JTextField("1");

    mkw.Event event;

    public MkwEnterTfPanel(CardLayout card, JPanel cardPane, MkwRaceInputPanel panel) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(40,5,40,5);

        Setter s = new Setter();
        s.addobjects(titleLabel,this, layout,gbc,0,0,1 , 1);

        s.addobjects(tierLabel,this, layout,gbc,0, 2,1,1,0.25,0);
        s.addobjects(tierTf,this, layout,gbc,1, 2,1,1, 0.75,0);
        s.addobjects(formatLabel,this, layout,gbc,0, 3,1,1,0.25,0);
        s.addobjects(formatTf,this, layout,gbc,1, 3,1,1,0.75,0);

        s.addobjects(buttonStart,this, layout,gbc,1,4,1, 1);
        s.addobjects(buttonBack,this, layout,gbc,0, 4,1,1);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String tierInput = tierTf.getText();
                String formatInput = formatTf.getText();

                if(InputVerifier.VerifyTier(tierInput)&&InputVerifier.verifyFormat(formatInput)){
                    Tier tier = InputVerifier.getTier(tierInput);
                    Format format = InputVerifier.getFormat(formatInput);
                    int eventsPlayed = RaceDao.getEventsStored();
                    event = new mkw.Event(tier,format,eventsPlayed);

                    int racesPlayed = RaceDao.getRacesStored();
                    Race.setRaceCount(racesPlayed);

                    panel.setEvent(event);
                    panel.setTitle();
                    panel.setStatus();
                    card.show(cardPane,"mkwRace");
                }else{
                    if(InputVerifier.VerifyTier(tierInput)==false&&InputVerifier.verifyFormat(formatInput)==false){
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

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwMenu");
            }
        });
    }
}
