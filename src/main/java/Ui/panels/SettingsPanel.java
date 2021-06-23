package Ui.panels;

import Ui.RaceDao;

import javax.management.StringValueExp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("Settings");

    JLabel mkwTierLabel = new JLabel("Enter MKW tier");
    JTextField mkwTierTf = new JTextField("");
    JButton mkwTierButton = new JButton("Set");

    JLabel currentMkwTierLabel = new JLabel();

    JLabel mk8dxTierLabel = new JLabel("Enter MK8DX tier");
    JTextField mk8dxTierTf = new JTextField("");
    JButton mkw8dxTierButton = new JButton("Set");

    JLabel currentMk8dxTierLabel = new JLabel();

    JButton resetButton = new JButton("Clear All Events");

    JButton buttonBack = new JButton("Back");

    public SettingsPanel(CardLayout card, JPanel cardPane,  JFrame frame) {

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        gbc.insets = new Insets(10,10,10,10);

        Setter s = new Setter();
        s.addobjects(titleLabel,this, layout,gbc,0,0,3 , 1,1,1,true);

        s.addobjects(mkwTierLabel,this, layout,gbc,0, 1,1,1);
        s.addobjects(mkwTierTf,this, layout,gbc,1, 1,1,1);
        s.addobjects(mkwTierButton,this, layout,gbc,2, 1,1,1);

        s.addobjects(currentMkwTierLabel,this, layout,gbc,0, 2,1,1);

        s.addobjects(mk8dxTierLabel,this, layout,gbc,0, 3,1,1);
        s.addobjects(mk8dxTierTf,this, layout,gbc,1, 3,1,1);
        s.addobjects(mkw8dxTierButton,this, layout,gbc,2, 3,1,1);

        s.addobjects(currentMk8dxTierLabel,this, layout,gbc,0, 4,1,1);

        s.addobjects(resetButton,this, layout,gbc,0, 5,3,1, 1, 1,true);

        s.addobjects(buttonBack,this, layout,gbc,0,6,3, 1, 1 ,1, true);

        mkwTierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(InputVerifier.VerifyTier(mkwTierTf.getText())){
                    RaceDao.updateDefaultTierMkw(Integer.parseInt(mkwTierTf.getText()));
                    initialize();
                    InputVerifier.InputErrorBox("Default Tier set to "+mkwTierTf.getText());
                }else{
                    InputVerifier.InputErrorBox("Invalid tier");
                }
            }
        });

        mkw8dxTierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(InputVerifier.VerifyTierD(mk8dxTierTf.getText())){
                    RaceDao.updateDefaultTierMk8dx(mk8dxTierTf.getText());
                    initialize();
                    InputVerifier.InputErrorBox("Default Tier set to "+mk8dxTierTf.getText());
                }else{
                    InputVerifier.InputErrorBox("Invalid tier");
                }
            }
        });

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

    public void initialize(){
        currentMkwTierLabel.setText("(Current Default is "+String.valueOf(RaceDao.getDefaultMkwTier())+")");
        currentMk8dxTierLabel.setText("(Current Default is "+RaceDao.getDefaultMk8dxTier()+")");
    }
}
