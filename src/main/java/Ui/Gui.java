package Ui;

import Ui.panels.*;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui {

    JFrame frame;
    JPanel cardPane;
    CardLayout card;

    public Gui() {
        frame = new JFrame("Lounge Data"){
            @Override
            public Dimension getPreferredSize() {
                // given some values of w & h
                return new Dimension(640, 480);
            }
        };
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardPane = new JPanel();
        card = new CardLayout();
        cardPane.setLayout(card);

        GridBagLayout layout = new GridBagLayout();

        JPanel mainMenu = new MainMenu(card, cardPane);
        JPanel settings = new SettingsPanel(card, cardPane);


        Mk8dxRaceInputPanel mk8dxRaceInput= new Mk8dxRaceInputPanel(card, cardPane );
        Mk8dxEnterTfPanel mk8dxTf= new Mk8dxEnterTfPanel(card, cardPane, mk8dxRaceInput);

        MkwRaceInputPanel mkwRaceInput= new MkwRaceInputPanel(card, cardPane);
        MkwEnterTfPanel mkwTf = new MkwEnterTfPanel(card, cardPane, mkwRaceInput);

        MkwSummaryPanel mkwStats = new MkwSummaryPanel(card, cardPane);
        Mk8dxSummaryPanel mk8dxStats = new Mk8dxSummaryPanel(card, cardPane);

        MkwAdvStatsPanel mkwAdvStats = new MkwAdvStatsPanel(card, cardPane);
        Mk8dxAdvStatsPanel mk8dxAdvStats = new Mk8dxAdvStatsPanel(card, cardPane);

        cardPane.add(mainMenu, "mainMenu");
        cardPane.add(settings, "settings");

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

    public static void main(String args[]) {

        EventQueue.invokeLater(() -> {
            Gui test = new Gui();
        });

    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
