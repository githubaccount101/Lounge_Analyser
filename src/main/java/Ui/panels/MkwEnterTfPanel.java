package Ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MkwEnterTfPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();
    JButton buttonStart = new JButton("Start");
    JButton buttonBack = new JButton("Back");
    JLabel titleLabel = new JLabel("mkw enter tier,format");

    public MkwEnterTfPanel(CardLayout card, JPanel cardPane) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(40,10,40,10);

        Setter s = new Setter();
        s.addobjects(titleLabel,this, layout,gbc,0,0,1 , 1);
        s.addobjects(buttonStart,this, layout,gbc,0,1,1, 1);
        s.addobjects(buttonBack,this, layout,gbc,1, 1,1,1);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
