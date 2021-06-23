package Ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("Lounge Data");

    JButton storeMkwButton = new JButton("Enter MKW Event");
    JButton storeMk8dxButton = new JButton("Enter MK8DX Event");
    JButton mkwSummaryButton = new JButton("Go to MKW Summary");
    JButton mk8dxSummaryButton = new JButton(" Go to MK8DX Summary");
    JButton mkwAdvButton = new JButton("Go to MKW Advanced");
    JButton mk8dxAdvButton = new JButton(" Go to MK8DX Advanced");
    JButton settingsButton = new JButton("Settings");


    public MainMenu(CardLayout card, JPanel cardPane) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(5,5,5,5);

        Setter s = new Setter();

        titleLabel.setFont(new Font("Comic Sans", Font.BOLD, 50));
        s.addobjects(titleLabel,this, layout,gbc,1,0,23 , 1,1,1,true,true);

        s.addobjects(storeMkwButton,this, layout,gbc,0,2,2 , 1,1,1,true,true);
        s.addobjects(storeMk8dxButton,this, layout,gbc,3,2,2 , 1,1,1,true,true);


        s.addobjects(mkwSummaryButton,this, layout,gbc,0,3,2 , 1,1,1,true,true);
        s.addobjects(mk8dxSummaryButton,this, layout,gbc,3,3,2 , 1,1,1,true,true);
        s.addobjects(settingsButton,this, layout,gbc,2,3,1 , 1,1,1,false,false);


        s.addobjects(mkwAdvButton,this, layout,gbc,0,4,2 , 1,1,1,true,true);
        s.addobjects(mk8dxAdvButton,this, layout,gbc,3,4,2 , 1,1,1,true,true);


        storeMkwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwTf");
            }
        });

        storeMk8dxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mk8dxTf");
            }
        });

        mkwSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwStats");
            }
        });

        mk8dxSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mk8dxStats");
            }
        });

        mkwAdvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwAdvStats");
            }
        });

        mk8dxAdvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwAdvStats");
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"settings");
            }
        });
    }
}
