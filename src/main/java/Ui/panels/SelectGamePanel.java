package Ui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectGamePanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();
    JButton button2Mkw = new JButton("MKW");
    JButton button2Mk8dx = new JButton("MK8DX");

    public SelectGamePanel(CardLayout card, JPanel cardPane) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(40,10,40,10);

        Setter s = new Setter();
        s.addobjects(button2Mkw,this, layout,gbc,0,0,10 , 1);
        s.addobjects(button2Mk8dx,this, layout,gbc,15, 0,10,1);

        button2Mkw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwMenu");
            }
        });

        button2Mk8dx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mk8dxMenu");
            }
        });


    }
}
