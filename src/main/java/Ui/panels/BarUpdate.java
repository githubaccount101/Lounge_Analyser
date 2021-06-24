package Ui.panels;

import Ui.RaceDao;
import mk8dx.TierD;
import mk8dx.TrackD;
import mkw.Tier;
import mkw.Track;
import shared.Format;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class BarUpdate extends SwingWorker<Void, Integer> {

    JProgressBar randomBar;
    int max;
    JLabel label;
    ArrayList<JButton> allButtons;
    JLabel randomWarningLabel;
    JLabel dbLabel;
    JTextField randomTf;

    public BarUpdate(JProgressBar randomBar, int max, JLabel label,  ArrayList<JButton> allButtons,
                     JLabel randomWarningLabel, JLabel dbLabel, JTextField randomTf) {
        this.randomBar = randomBar;
        this.max = max;
        this.label = label;
        this.allButtons= allButtons;
        this.randomWarningLabel = randomWarningLabel;
        this.dbLabel = dbLabel;
        this.randomTf = randomTf;
    }

    @Override
    protected void process(List<Integer> chunks) {
        int i = chunks.get(chunks.size()-1);
        randomBar.setValue(i); // The last value in this array is all we care about.
        label.setText("Generated " + i + " of " + max);
    }

    @Override
    protected Void doInBackground() throws Exception {

        RaceDao.updateRandom(true);
        randomBar.setVisible(true);
        randomBar.setMaximum(max);
        allButtons.forEach(x->x.setEnabled(false));

        int[] p12 = {15,12, 10, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] p12d = {15,12, 10, 9,8, 7, 6, 5, 4, 3, 2, 1};
        Random rng= new Random();

        int raceid = 1;
        int raceidD= 1;

        for(int i = 1;i<=max;i++){
            int eventid = i;
            String format = Format.randomFormat().getFormat();
            int tier = Integer.parseInt(Tier.randomTier().getTier());
            String tierD = TierD.randomTierD().getTier();
            boolean satout = false;
            int tpoints = 0;

            for(int n = 1; n<=3; n++){
                int gpoints = 0;
                double gpid = 1.0*n/10+1.0*i;
                for(int x = 1; x<=4;x++){

                    String track = Track.randomTrack().getFullName();
                    int start = rng.nextInt(12)+1;
                    int finish = rng.nextInt(12)+1;
                    int players = 12;
                    int points = p12[finish-1];
                    tpoints+=points;
                    gpoints+=points;

                    RaceDao.insert(raceid, eventid, gpid, tier, format, track, start, finish, players, points, satout);

                    raceid++;
                }

                RaceDao.insertGp(gpid, tier, format, gpoints, true,true);
            }

            RaceDao.insertEvent(eventid,tier, format, tpoints,true);


            int tpointsD = 0;
            for(int k=1;k<=12;k++){

                String track = TrackD.randomTrackD().getFullName();
                int start = rng.nextInt(12)+1;
                int finish = rng.nextInt(12)+1;
                int points = p12d[finish-1];
                tpointsD+=points;

                RaceDao.insertD(raceidD, eventid, tierD, format, track, start, finish, points, satout);
                raceidD++;
            }

            RaceDao.insertEventD(eventid,tierD, format, tpointsD,true);
            publish(i);
        }
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
            randomBar.setVisible(false);
            randomTf.setText("");
            randomWarningLabel.setText("(limit 1000) - Random Mode is On");
            allButtons.forEach(x->x.setEnabled(true));
            dbLabel.setText(RaceDao.getEventsStored()+" MKW events stored, "+RaceDao.getEventsDStored()+" MK8DX events stored");
            label.setText("(limit 1000) - Random Mode is On");
            randomBar.setMaximum(1000);
            JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

//protected Void doInBackground() throws Exception {
//                System.out.println("randomizing");
//                RaceDao.updateRandom(true);
//                RaceDao.resetTables();
//                enableAllButtons(false);
//                randomBar.setVisible(true);
//                RaceDao.generateRandom(Integer.parseInt(randomTf.getText()));
//                randomTf.setText("");
//                inception();
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                System.out.println("now im done...");
//                enableAllButtons(true);
//                randomBar.setVisible(false);
//                initialize();
//                JOptionPane.showMessageDialog(null, "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
//                System.out.println("did it work?");
//

    //public void initialize(){
    //        currentMkwTierLabel.setText("(Current Default is "+String.valueOf(RaceDao.getDefaultMkwTier())+")");
    //        currentMk8dxTierLabel.setText("(Current Default is "+RaceDao.getDefaultMk8dxTier()+")");
    //        if(RaceDao.isRandom()){
    //            randomWarningLabel.setText("(limit 1000) - Random Mode is On");
    //        }else{
    //            randomWarningLabel.setText("(limit 1000) - Random Mode is Off");
    //        }
    //    }
}

