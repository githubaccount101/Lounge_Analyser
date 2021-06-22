package Ui;

import Ui.panels.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui implements ActionListener {

    JFrame frame;
    JPanel cardPane;
    CardLayout card;

    public Gui() {
        frame = new JFrame("Lounge Data");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 720);

        cardPane = new JPanel();
        card = new CardLayout();
        cardPane.setLayout(card);

        JPanel mainMenu = new SelectGamePanel(card, cardPane);
        JPanel mkwMenu = new MkwMenuPanel(card, cardPane);
        JPanel mk8dxMenu = new Mk8dxMenuPanel(card, cardPane);

        Mk8dxRaceInputPanel mk8dxRaceInput= new Mk8dxRaceInputPanel(card, cardPane );
        Mk8dxEnterTfPanel mk8dxTf= new Mk8dxEnterTfPanel(card, cardPane, mk8dxRaceInput);

        MkwRaceInputPanel mkwRaceInput= new MkwRaceInputPanel(card, cardPane);
        MkwEnterTfPanel mkwTf = new MkwEnterTfPanel(card, cardPane, mkwRaceInput);

        MkwSummaryPanel mkwStats = new MkwSummaryPanel(card, cardPane);
        Mk8dxSummaryPanel mk8dxStats = new Mk8dxSummaryPanel(card, cardPane);

        MkwAdvStatsPanel mkwAdvStats = new MkwAdvStatsPanel(card, cardPane);
        Mk8dxAdvStatsPanel mk8dxAdvStats = new Mk8dxAdvStatsPanel(card, cardPane);

        cardPane.add(mainMenu, "selectGame");
        cardPane.add(mkwMenu, "mkwMenu");
        cardPane.add(mk8dxMenu, "mk8dxMenu");
        cardPane.add(mkwTf, "mkwTf");
        cardPane.add(mk8dxTf, "mk8dxTf");
        cardPane.add(mkwRaceInput,"mkwRace");
        cardPane.add(mk8dxRaceInput, "mk8dxRace");
        cardPane.add(mkwStats, "mkwStats");
        cardPane.add(mk8dxStats, "mk8dxStats");
        cardPane.add(mkwAdvStats, "mkwAdvStats");
        cardPane.add(mk8dxAdvStats, "mk8dxAdvStats");

        frame.add(cardPane);
        frame.pack();
        centreWindow(frame);
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        card.next(cardPane);
    }

    public static void main(String args[]) {
        Gui test = new Gui();
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
