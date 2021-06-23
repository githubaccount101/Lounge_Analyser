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
    JLabel titleLabel = new JLabel("Entering MKW Event ["+ (RaceDao.getEventsStored()+1)+"]");
    JLabel tierLabel = new JLabel("Enter Tier:");
    JLabel formatLabel = new JLabel("Enter Format");
    JTextField tierTf = new JTextField("1");
    JTextField formatTf = new JTextField("2");

    mkw.Event event;

    public MkwEnterTfPanel(CardLayout card, JPanel cardPane, MkwRaceInputPanel panel) {

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

        buttonStart.setFont(new Font("Arial", Font.PLAIN, 18));
        s.addobjects(buttonStart,this, layout,gbc,1,4,1 , 1,1,1);
        buttonBack.setFont(new Font("Arial", Font.PLAIN, 18));
        s.addobjects(buttonBack,this, layout,gbc,0,4,1, 1,1,1);

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

                    panel.setTrackTf();
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
                card.show(cardPane,"mainMenu");
            }
        });
    }

    public void initialize(){
        titleLabel.setText("Entering MKW Event ["+ (RaceDao.getEventsStored()+1)+"]");
        tierTf.setText(String.valueOf(RaceDao.getDefaultMkwTier()));
    }
}
