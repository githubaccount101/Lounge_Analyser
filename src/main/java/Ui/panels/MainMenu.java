package Ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("LOUNGE");

    JButton storeMkwButton = new JButton("Enter MKW Event");
    JButton storeMk8dxButton = new JButton("Enter MK8DX Event");
    JButton mkwSummaryButton = new JButton("Go to MKW Summary");
    JButton mk8dxSummaryButton = new JButton(" Go to MK8DX Summary");
    JButton mkwAdvButton = new JButton("Go to MKW Advanced");
    JButton mk8dxAdvButton = new JButton(" Go to MK8DX Advanced");
    JButton settingsButton = new JButton("Settings");


    public MainMenu(CardLayout card, JPanel cardPane, MkwEnterTfPanel mkwEnterTfPanel, Mk8dxEnterTfPanel mk8dxEnterTfPanel,
                    MkwSummaryPanel mkwSummaryPanel, Mk8dxSummaryPanel mk8dxSummaryPanel,
                    MkwAdvStatsPanel mkwAdvStatsPanel, Mk8dxAdvStatsPanel mk8dxAdvStatsPanel) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        gbc.insets = new Insets(10,10,10,10);

        Setter s = new Setter();

        titleLabel.setFont(new Font("Comic Sans", Font.BOLD, 40));
        s.addobjects(titleLabel,this, layout,gbc,0,0,1 , 1,1,1);
        s.addobjects(settingsButton,this, layout,gbc,1,0,1 , 1,1,1, false);



        s.addobjects(storeMkwButton,this, layout,gbc,0,2,1 , 1,1,1);
        s.addobjects(storeMk8dxButton,this, layout,gbc,1,2,1 , 1,1,1);


        s.addobjects(mkwSummaryButton,this, layout,gbc,0,3,1 , 1,1,1);
        s.addobjects(mk8dxSummaryButton,this, layout,gbc,1,3,1 , 1,1,1);

        s.addobjects(mkwAdvButton,this, layout,gbc,0,4,1 , 1,1,1);
        s.addobjects(mk8dxAdvButton,this, layout,gbc,1,4,1, 1,1,1);


        storeMkwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwTf");
                mkwEnterTfPanel.initialize();
            }
        });

        storeMk8dxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mk8dxTf");
                mk8dxEnterTfPanel.initialize();
            }
        });

        mkwSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwStats");
                mkwSummaryPanel.initialize();
            }
        });

        mk8dxSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mk8dxStats");
                mk8dxSummaryPanel.initialize();
            }
        });

        mkwAdvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwAdvStats");
                mkwAdvStatsPanel.initialize();
            }
        });

        mk8dxAdvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mk8dxAdvStats");
                mk8dxAdvStatsPanel.initialize();
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
