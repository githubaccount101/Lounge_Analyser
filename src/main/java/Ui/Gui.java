package Ui;

import Ui.panels.*;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui implements ActionListener {

    JFrame frame;
    JPanel cardPane;
    JPanel mainMenu, mkwMenu, mk8dxMenu, mkwTf, mk8dxTf;
    CardLayout card;


    public Gui() {
        frame = new JFrame("CardLayout Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 480);

        cardPane = new JPanel();
        card = new CardLayout();
        cardPane.setLayout(card);

        JPanel mainMenu = new SelectGamePanel(card, cardPane);
        JPanel mkwMenu = new MkwMenuPanel(card, cardPane);
        JPanel mk8dxMenu = new Mk8dxMenuPanel(card, cardPane);
        JPanel mkwTf = new MkwEnterTfPanel(card, cardPane);
        JPanel mk8dxTf= new Mk8dxEnterTfPanel(card, cardPane);

        cardPane.add(mainMenu, "selectGame");
        cardPane.add(mkwMenu, "mkwMenu");
        cardPane.add(mk8dxMenu, "mk8dxMenu");
        cardPane.add(mkwTf, "mkwTf");
        cardPane.add(mk8dxTf, "mk8dxTf");

        frame.add(cardPane);
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        card.next(cardPane);
    }

    public static void main(String args[]) {
        Gui test = new Gui();
    }
}
