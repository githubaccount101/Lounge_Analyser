package Ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mk8dxMenuPanel extends JPanel  {

    GridBagConstraints gbc = new GridBagConstraints();
    JButton buttonBack = new JButton("Back");
    JButton buttonEnter = new JButton("Store event");
    JButton buttonSummary = new JButton("Performance Summary");
    JButton buttonAdvanced = new JButton("Advanced Performance Summary");
    JLabel titleLabel = new JLabel("mk8dxMenuPanel");

    public Mk8dxMenuPanel(CardLayout card, JPanel cardPane) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(40,10,40,10);

        Setter s = new Setter();
        s.addobjects(titleLabel,this, layout,gbc,0,0,1 , 1);
        s.addobjects(buttonBack,this, layout,gbc,0,4,1 , 1);
        s.addobjects(buttonEnter,this, layout,gbc,0, 1,1,1);
        s.addobjects(buttonSummary,this, layout,gbc,0, 3,1,1);
        s.addobjects(buttonAdvanced,this, layout,gbc,1, 3,1,1);


        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"selectGame");
            }
        });

        buttonEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mk8dxTf");
            }
        });

        buttonSummary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane, "mk8dxStats");
            }
        });

        buttonAdvanced.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane, "mk8dxAdvStats");
            }
        });
    }
}
