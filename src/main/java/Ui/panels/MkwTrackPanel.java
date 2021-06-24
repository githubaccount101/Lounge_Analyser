package Ui.panels;

import Ui.Gui;
import Ui.RaceDao;
import mkw.Tier;
import mkw.Track;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.json.JSONUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import shared.Format;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MkwTrackPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("MKW Track Analysis");
    JButton buttonBack = new JButton("Back");

    JLabel enterXLabel= new JLabel("Search in last");
    JTextField eventTF = new JTextField(String.valueOf(getInitialInteger()));
    JLabel enterXLabel2= new JLabel("("+ RaceDao.getEventsStored()+" events stored)");
    JCheckBox dcBox = new JCheckBox("Exclude Events with DC's");

    JLabel lastXResultLabel  = new JLabel("");

    JLabel trackLabel = new JLabel("Track:");
    JTextField trackTf = new JTextField(Track.randomTrack().getAbbreviation());
    JButton runButton = new JButton("Run");

    JLabel trackMatchLabel = new JLabel("No Track Found");

    JLabel tierLabel = new JLabel("Include tier(s)");
    JButton allTierButton = new JButton("All Tiers");

    JLabel formatLabel = new JLabel("Include format(s)");
    JButton allFormatButton = new JButton("All Formats");

    JLabel startLabel = new JLabel("Include Start(s)");
    JButton allStartButton = new JButton("All Starts");

    XYSeriesCollection dataset = new XYSeriesCollection();
    JFreeChart chart = RaceDao.getChart4Tracks(dataset);
    ChartPanel chartPanel = RaceDao.getChartPanel(chart);

    JCheckBox box0 = new JCheckBox("Show Finish Position Line");
    JCheckBox box1 = new JCheckBox("Show Moving Average Line");

    ArrayList<JCheckBox> tierBoxes = new ArrayList<>();
    ArrayList<JCheckBox> formatBoxes = new ArrayList<>();
    ArrayList<JCheckBox> startBoxes = new ArrayList<>();

    ArrayList< ArrayList<JCheckBox>> allBoxes = new ArrayList<>();

    public MkwTrackPanel(CardLayout card, JPanel cardPane) {

        GridBagLayout layout = new GridBagLayout();
        allBoxes.add(tierBoxes); allBoxes.add(formatBoxes);allBoxes.add(startBoxes);

        setLayout(layout);
        gbc.insets = new Insets(3,3,3,3);

        Setter s = new Setter();

        s.addobjects(titleLabel,this, layout,gbc,0,0,2 , 1,1,2,true);
        s.addobjects(runButton,this, layout,gbc,2, 0,1,1, 1,2,true);
        s.addobjects(enterXLabel2,this, layout,gbc,3,0,3 , 1,1,2,true);
        s.addobjects(buttonBack,this, layout,gbc,16,0,2 , 1,1,2,true);

        s.addobjects(enterXLabel,this, layout,gbc,0,1,2 , 1,1,2,true);
        s.addobjects(eventTF,this, layout,gbc,2,1,1 , 1,1,2,true);
        s.addobjects(lastXResultLabel,this, layout,gbc,3,1,3 , 1,1,2,true);
        setLastXResultLabel();

        s.addobjects(trackLabel,this, layout,gbc,6, 1,1,1,1,2,true);
        s.addobjects(trackTf,this, layout,gbc,7, 1,3,1, 0.000001,2,true);
        matchLabelUpdate();
        s.addobjects(trackMatchLabel,this, layout,gbc,10, 1,5,1,1,0.1,true);

        s.addobjects(tierLabel,this, layout,gbc,0,5,4 , 1, 1,2,true);
        int i = 2;
        for(Tier t:Tier.ALL){
            JCheckBox temp = new JCheckBox(t.getTier());
            s.addobjects(temp,this, layout,gbc,i,5,1 , 1, 1,2,true);
            tierBoxes.add(temp);
            i++;
        }
        s.addobjects(allTierButton,this, layout,gbc,16,5,2 , 1, 1,2,true);

        s.addobjects(formatLabel,this, layout,gbc,0,6,2 , 1, 1,2,true);
        i = 2;
        for(Format f:Format.ALL){
            JCheckBox temp = new JCheckBox(f.getFormat());
            s.addobjects(temp,this, layout,gbc,i,6 , 1, 1,2,2,true);
            formatBoxes.add(temp);
            i++;
        }
        s.addobjects(allFormatButton,this, layout,gbc,16,6,2 , 1, 1,2,true);

        s.addobjects(startLabel,this, layout,gbc,0,7,2 , 1, 1,2,true);
        i = 2;
        for(int n = 1; n<=12; n++){
            String name = String.valueOf(n);
            JCheckBox temp = new JCheckBox(name);
            s.addobjects(temp,this, layout,gbc,i,7,1 , 1, 1,2,true);
            startBoxes.add(temp);
            i++;
        }
        s.addobjects(allStartButton,this, layout,gbc,16,7,2 , 1, 1,2,true);

        toggleAllBoxes(true);
        chart.setTitle("Last "+eventTF.getText()+" Matching Events");
        updateDatasetSeries();
        s.addobjects(chartPanel,this, layout,gbc,0,8,18 , 7, 1,100000,true);

        s.addobjects(box0,this, layout,gbc,4,16,4 , 1, 1,2,true);
        s.addobjects(box1,this, layout,gbc,8,16,4 , 1, 1,2,true);
        box0.setSelected(true);
        box1.setSelected(true);

        dcBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(getSql());
                dataset.removeAllSeries();
                dataset.addSeries(new XYSeries(""));
                dataset.addSeries(RaceDao.getSeriesMovingAverage(getSql()));
            }
        });

        box1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("finishbox is "+box0.isSelected()+" MAbox is "+ box1.isSelected());
                if(box0.isSelected()){
                    if(box1.isSelected()){
                        //blue on black on
                        updateDatasetSeries();
                    }else{
                       //blue off black on
                        updateDatasetSeries();
                        dataset.removeSeries(1);
                    }
                }else{
                    if(box1.isSelected()){
                        //blue on black off
                        dataset.removeAllSeries();
                        dataset.addSeries(new XYSeries(""));
                        dataset.addSeries(RaceDao.getSeriesMovingAverage(getSql()));
                    }else{
                        //both off
                        dataset.removeAllSeries();
                    }

                }
            }
        });

        box0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("finishbox is "+box0.isSelected()+" MAbox is "+ box1.isSelected());
                if(box1.isSelected()){
                    //blue selected black selected
                    if(box0.isSelected()){
                        updateDatasetSeries();
                    }else{
                        //blue selected black unselected
                        dataset.removeAllSeries();
                        dataset.addSeries(new XYSeries(""));
                        dataset.addSeries(RaceDao.getSeriesMovingAverage(getSql()));
                    }
                }else{
                    if(box0.isSelected()){
                        //blue off black on
                        updateDatasetSeries();
                        dataset.removeSeries(1);
                    }else{
                        //both off
                        dataset.removeAllSeries();
                    }

                }
            }
        });

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(420,480);
                card.show(cardPane,"mainMenu");
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = eventTF.getText();
                if(InputVerifier.verifyLastX(input)){
                    updateDatasetSeries();
                }else{
                    InputVerifier.InputErrorBox("invalid number of events");
                }
            }
        });


        allTierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isAllSelected(tierBoxes)){
                    toggleBoxes(tierBoxes,false);
                }else{
                    toggleBoxes(tierBoxes,true);
                }
                if(atLeastOneBoxOfEachIsSelected()){
                    runButton.setEnabled(true);
                }else{
                    runButton.setEnabled(false);
                }
            }
        });

        allFormatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isAllSelected(formatBoxes)){
                    toggleBoxes(formatBoxes,false);
                }else{
                    toggleBoxes(formatBoxes,true);
                }
                if(atLeastOneBoxOfEachIsSelected()){
                    runButton.setEnabled(true);
                }else{
                    runButton.setEnabled(false);
                }
            }
        });

        allStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isAllSelected(startBoxes)){
                    toggleBoxes(startBoxes,false);
                }else{
                    toggleBoxes(startBoxes,true);
                }
                if(atLeastOneBoxOfEachIsSelected()){
                    runButton.setEnabled(true);
                }else{
                    runButton.setEnabled(false);
                }
            }
        });

        eventTF.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                limitCheck();
                setLastXResultLabel();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                limitCheck();
                setLastXResultLabel();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                limitCheck();
                setLastXResultLabel();
            }
        });

        trackTf.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                matchLabelUpdate();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                matchLabelUpdate();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                matchLabelUpdate();
            }
        });

        allBoxes.forEach(x->x.forEach(new Consumer<JCheckBox>() {
            @Override
            public void accept(JCheckBox jCheckBox) {
                jCheckBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(atLeastOneBoxOfEachIsSelected()){
                            runButton.setEnabled(true);
                        }else{
                            runButton.setEnabled(false);
                        }
                    }
                });
            }
        }));

        trackTf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String input = eventTF.getText();
                    if(InputVerifier.verifyLastX(input)){
                        updateDatasetSeries();
                    }else{
                        InputVerifier.InputErrorBox("invalid number of events");
                    }
                }
            }

        });
    }

    public void limitCheck(){
        String input = eventTF.getText();
        if(InputVerifier.verifyLastX(input)){
            formatBoxes.forEach(x->x.setEnabled(true));
            tierBoxes.forEach(x->x.setEnabled(true));
            startBoxes.forEach(x->x.setEnabled(true));
            allTierButton.setEnabled(true);
            allFormatButton.setEnabled(true);
            allStartButton.setEnabled(true);

            runButton.setEnabled(true);
        }else{
            formatBoxes.forEach(x->x.setEnabled(false));
            tierBoxes.forEach(x->x.setEnabled(false));
            startBoxes.forEach(x->x.setEnabled(false));
            allTierButton.setEnabled(false);
            allFormatButton.setEnabled(false);
            allStartButton.setEnabled(false);

            runButton.setEnabled(false);
        }
    }

    public XYSeriesCollection getDataset(){
        return RaceDao.getDataset(RaceDao.getSeries(getSql()));
    }

    public void updateDatasetSeries(){
        dataset.removeAllSeries();
        dataset.addSeries(RaceDao.getSeries4Track(getSql()));
        dataset.addSeries(RaceDao.getSeriesMovingAverage(getSql()));
        chart.setTitle(trackMatchLabel.getText()+": "+RaceDao.getRsRows(getSql())+" Matching Race(s) Found");
    }

    public ArrayList<Tier> tierCheck(){
        ArrayList<Tier> selected = new ArrayList<>();

        int i = 0;
        for(JCheckBox box:tierBoxes){
            if(box.isSelected()){
                selected.add(Tier.ALL.get(i));
            }
            i++;
        }
        return selected;
    }

    public ArrayList<Format> formatCheck(){
        ArrayList<Format> selected = new ArrayList<>();
        int i = 0;
        for(JCheckBox box:formatBoxes){
            if(box.isSelected()){
                selected.add(Format.ALL.get(i));
            }
            i++;
        }
        return selected;
    }

    public ArrayList<Integer> startCheck(){

        ArrayList<Integer> selected = new ArrayList<>();

        int i = 1;
        for(JCheckBox box:startBoxes){
            if(box.isSelected()==true){
                selected.add(i);
            }
            i++;
        }

        return selected;
    }

    public String tierBuilder(){
        ArrayList<Tier> selectedTiers = tierCheck();
        String sql= "AND (tier=";
        StringBuilder bob = new StringBuilder();
        bob.append(sql);
        if(selectedTiers.size()==1){
            bob.append(selectedTiers.get(0).getTier()+")");
        }else{
            bob.append(selectedTiers.get(0).getTier());
            for(int i = 1;i<selectedTiers.size();i++){
                bob.append(" or tier="+selectedTiers.get(i).getTier());
            }
            bob.append(")");
        }
        return bob.toString();
    }

    public String formatBuilder(){
        ArrayList<Format> selectedFormats = formatCheck();
        String sql= "AND (format like '";
        StringBuilder bob = new StringBuilder();
        bob.append(sql);
        if(selectedFormats.size()==1){
            bob.append(selectedFormats.get(0).getFormat()+"')");
        }else{
            bob.append(selectedFormats.get(0).getFormat()+"'");
            for(int i = 1;i<selectedFormats.size();i++){
                bob.append(" or format like '"+selectedFormats.get(i).getFormat()+"'");
            }
            bob.append(")");
        }
        return bob.toString();
    }

    public String startBuilder(){
        ArrayList<Integer> SelectedStarts = startCheck();
        String sql= "AND (start=";
        StringBuilder bob = new StringBuilder();
        bob.append(sql);
        if(SelectedStarts.size()==1){
            bob.append(SelectedStarts.get(0)+")");
        }else{
            bob.append(SelectedStarts.get(0));
            for(int i = 1;i<SelectedStarts.size();i++){
                bob.append(" or start="+SelectedStarts.get(i));
            }
            bob.append(")");
        }
        return bob.toString();
    }

    public String getSql(){
        StringBuilder bob = new StringBuilder();
        String track = Track.fromString(trackTf.getText()).get().getSearchName();
        bob.append("SELECT finish"+"\n")
                .append("FROM (SELECT * FROM races ORDER BY raceid DESC LIMIT "+(Integer.parseInt(eventTF.getText())*12)+")"+"\n");
        bob.append("Where track = '"+track+"'"+"\n");
        bob.append(tierBuilder()+"\n")
                .append(formatBuilder()+"\n")
                .append(startBuilder()+"\n")
                .append("order by raceid");
        return bob.toString();
    }

    public boolean atLeastOneBoxOfEachIsSelected(){
        boolean tier = false;
        boolean format = false;
        for(JCheckBox box : tierBoxes){
            if(box.isSelected()){
                tier = true;
            }
        }
        System.out.println("");
        for(JCheckBox box : formatBoxes){
            if(box.isSelected()){
                format = true;
            }
        }
        if(tier&&format) {
            return true;
        }
        return false;
    }

    public boolean isAllSelected(ArrayList<JCheckBox> boxes){
        boolean isAllSelected = true;
        for(JCheckBox box:boxes){
            if(box.isSelected()==false){
                isAllSelected = false;
            }
        }
        return isAllSelected;
    }

    public void toggleBoxes(ArrayList<JCheckBox> boxes, boolean onOff){
        boxes.forEach(x->x.setSelected(onOff));
    }

    public void toggleAllBoxes(Boolean onOff){
        toggleBoxes(formatBoxes,onOff);
        toggleBoxes(tierBoxes,onOff);
        toggleBoxes(startBoxes,onOff);
    }

    public void setLastXResultLabel(){
        String input = eventTF.getText();
        if(input.equals("All")||input.equals("all")||input.equals("ALL")){
            int eventsPlayed = RaceDao.getEventsStored();
            double avg = RaceDao.getAvgPtsLastX(eventsPlayed);
            lastXResultLabel.setText("events, average Score: "+avg);
            return;
        }
        if(InputVerifier.verifyLastX(input)){
            int lastX= Integer.parseInt(input);
            double avg = RaceDao.getAvgPtsLastX(lastX);
            lastXResultLabel.setText( "events, average Score: "+avg);
            return;
        }
        lastXResultLabel.setText("events, Invalid number");
    }

    public void initialize(){
        int eventsStored = RaceDao.getEventsStored();
        if(eventsStored == 0){
            runButton.setEnabled(false);
        }
        eventTF.setText(String.valueOf(getInitialInteger()));
        enterXLabel2.setText("("+ eventsStored+" events stored)");
        setLastXResultLabel();
        String input = eventTF.getText();
        if(InputVerifier.verifyLastX(input)){
            updateDatasetSeries();
        }
        if(Integer.parseInt(input)==0){
            dataset.removeAllSeries();
            dataset.addSeries(new XYSeries(""));
            chart.setTitle(0+" Matching Event(s) Found");
        }
    }

    public int getInitialInteger(){
        int number = 100;
        int eventsStored = RaceDao.getEventsStored();
        if(number>eventsStored){
            number = eventsStored;
        }
        return number;
    }

    public void matchLabelUpdate(){
        String input = trackTf.getText();
        if(InputVerifier.verifyTrack(input)){
            trackMatchLabel.setText(Track.fromString(input).get().getFullName());
            String input2 = eventTF.getText();
            if(InputVerifier.verifyLastX(input2)){
                formatBoxes.forEach(x->x.setEnabled(true));
                tierBoxes.forEach(x->x.setEnabled(true));
                startBoxes.forEach(x->x.setEnabled(true));
                allTierButton.setEnabled(true);
                allFormatButton.setEnabled(true);
                allStartButton.setEnabled(true);

                runButton.setEnabled(true);
            }else{
                formatBoxes.forEach(x->x.setEnabled(false));
                tierBoxes.forEach(x->x.setEnabled(false));
                startBoxes.forEach(x->x.setEnabled(false));
                allTierButton.setEnabled(false);
                allFormatButton.setEnabled(false);
                allStartButton.setEnabled(false);

                runButton.setEnabled(false);
            }
        }else{
            trackMatchLabel.setText("No Track Found");
            formatBoxes.forEach(x->x.setEnabled(false));
            tierBoxes.forEach(x->x.setEnabled(false));
            startBoxes.forEach(x->x.setEnabled(false));
            allTierButton.setEnabled(false);
            allFormatButton.setEnabled(false);
            allStartButton.setEnabled(false);

            runButton.setEnabled(false);
        }
    }


}
