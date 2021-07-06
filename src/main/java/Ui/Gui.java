package Ui;

import Ui.panels.*;

import java.awt.*;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui {

    public static JFrame frame;
    JPanel cardPane;
    CardLayout card;
    public static final int defaultWidth = 450;
    public static final int defaultHeight = 480;
    public static final int bigWidth = 900;
    public static final int bigHeight = 660;


    public Gui() {
        RaceDao.setUp();

        frame = new JFrame("Lounge Data"){
            @Override
            public Dimension getPreferredSize() {
                // given some values of w & h
                return new Dimension(450, 480);
            }
        };
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardPane = new JPanel();
        card = new CardLayout();
        cardPane.setLayout(card);

        GridBagLayout layout = new GridBagLayout();

        Mk8dxRaceInputPanel mk8dxRaceInput= new Mk8dxRaceInputPanel(card, cardPane );
        Mk8dxEnterTfPanel mk8dxTf= new Mk8dxEnterTfPanel(card, cardPane, mk8dxRaceInput);

        MkwRaceInputPanel mkwRaceInput= new MkwRaceInputPanel(card, cardPane, frame);
        MkwEnterTfPanel mkwTf = new MkwEnterTfPanel(card, cardPane, mkwRaceInput);

        MkwTrackPanel mkwTrackPanel = new MkwTrackPanel(card, cardPane);
        Mk8dxTrackPanel mk8dxTrackPanel = new Mk8dxTrackPanel(card, cardPane);

        MkwSummaryPanel mkwStats = new MkwSummaryPanel(card, cardPane);
        Mk8dxSummaryPanel mk8dxStats = new Mk8dxSummaryPanel(card, cardPane);

        MkwAdvStatsPanel mkwAdvStats = new MkwAdvStatsPanel(card, cardPane);
        Mk8dxAdvStatsPanel mk8dxAdvStats = new Mk8dxAdvStatsPanel(card, cardPane);

        SettingsPanel settings = new SettingsPanel(card, cardPane);

        MainMenu mainMenu = new MainMenu(card, cardPane, mkwTf, mk8dxTf, mkwStats, mk8dxStats, mkwAdvStats, mk8dxAdvStats,settings , mkwTrackPanel, mk8dxTrackPanel);

        settings.setMainMenu(mainMenu);


        cardPane.add(mainMenu, "mainMenu");
        cardPane.add(settings, "settings");

        cardPane.add(mkwTf, "mkwTf");
        cardPane.add(mk8dxTf, "mk8dxTf");

        cardPane.add(mkwTrackPanel,"mkwTrack");
        cardPane.add(mk8dxTrackPanel, "mk8dxTrack");
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
