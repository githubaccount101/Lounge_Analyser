package Ui.panels;

import Ui.RaceDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();
    JButton buttonBack = new JButton("Back");
    JButton resetButton = new JButton("Clear All Entries");
    JLabel titleLabel = new JLabel("Settings");

    public SettingsPanel(CardLayout card, JPanel cardPane,  JFrame frame) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(40,10,40,10);

        Setter s = new Setter();
        s.addobjects(titleLabel,this, layout,gbc,0,0,1 , 1);
        s.addobjects(buttonBack,this, layout,gbc,0,4,1 , 1);
        s.addobjects(resetButton,this, layout,gbc,0, 1,1,1);


        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mainMenu");
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(null, "Are you sure?",
                        "Confirm", JOptionPane.YES_NO_OPTION);

                if(a==0){
                    System.out.println("Clearing everything");
                    RaceDao.resetTables();





                }
                if(a>0){
                    System.out.println("no");
                }

            }
        });


    }
}
