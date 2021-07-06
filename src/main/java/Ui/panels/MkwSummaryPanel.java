package Ui.panels;

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

import Ui.Gui;
import Ui.RaceDao;
import mkw.Race;
import mkw.Tier;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import shared.Format;

public class MkwSummaryPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("MKW Event Analysis");
    JButton runButton = new JButton("Run");
    JCheckBox dcBox = new JCheckBox("Exclude Events with DC's");
    JButton buttonBack = new JButton("Back");

    JLabel enterXLabel= new JLabel("Limit to Last");
    JTextField eventTF = new JTextField(String.valueOf(getInitialInteger()));
    JLabel enterXLabel2= new JLabel("Events ("+ RaceDao.getEventsStored()+" events stored)");

    JLabel lastXResultLabel  = new JLabel("");

    JLabel tierLabel = new JLabel("Include tier(s)");
    JButton allTierButton = new JButton("All Tiers");

    JLabel formatLabel = new JLabel("Include format(s)");
    JButton allFormatButton = new JButton("All Formats");

    XYSeriesCollection dataset = new XYSeriesCollection();
    JFreeChart chart = RaceDao.getChart(dataset);
    ChartPanel chartPanel = RaceDao.getChartPanel(chart);

    JCheckBox box0 = new JCheckBox("Show Event Score Line");
    JCheckBox box1 = new JCheckBox("Show Moving Average Line");

    ArrayList<JCheckBox> tierBoxes = new ArrayList<>();
    ArrayList<JCheckBox> formatBoxes = new ArrayList<>();

    ArrayList< ArrayList<JCheckBox>> allBoxes = new ArrayList<>();

    public MkwSummaryPanel(CardLayout card, JPanel cardPane) {

        GridBagLayout layout = new GridBagLayout();
        allBoxes.add(tierBoxes); allBoxes.add(formatBoxes);

        setLayout(layout);
        gbc.insets = new Insets(3,3,3,3);

        Setter s = new Setter();

        s.addobjects(titleLabel,this, layout,gbc,0,0,2 , 1,1,2,true);
        s.addobjects(runButton,this, layout,gbc,2,0,1 , 1,1,2,true);

        s.addobjects(dcBox,this, layout,gbc,3,0,3 , 1,1,2,true);
        dcBox.setSelected(false);
        s.addobjects(buttonBack,this, layout,gbc,11,0,2 , 1,1,2,true);

        s.addobjects(enterXLabel,this, layout,gbc,0,1,2 , 1,1,2,true);
        s.addobjects(eventTF,this, layout,gbc,2,1,1 , 1,1,2,true);
        s.addobjects(enterXLabel2,this, layout,gbc,3,1,2 , 1,1,2,true);

        s.addobjects(lastXResultLabel,this, layout,gbc,2,3,5 , 1,1,2,true);
        setLastXResultLabel();

        s.addobjects(tierLabel,this, layout,gbc,0,4,2 , 1, 1,2,true);
        int i = 2;
        for(Tier t:Tier.ALL){
            JCheckBox temp = new JCheckBox(t.getTier());
            s.addobjects(temp,this, layout,gbc,i,4,1 , 1, 1,2,true);
            tierBoxes.add(temp);
            i++;
        }
        s.addobjects(allTierButton,this, layout,gbc,11,4,2 , 1, 1,2,true);

        s.addobjects(formatLabel,this, layout,gbc,0,5,2 , 1, 1,2,true);
        i = 2;
        for(Format f:Format.ALL){
            JCheckBox temp = new JCheckBox(f.getFormat());
            s.addobjects(temp,this, layout,gbc,i,5,1 , 1, 1,2,true);
            formatBoxes.add(temp);
            i++;
        }
        s.addobjects(allFormatButton,this, layout,gbc,11,5,2 , 1, 1,2,true);

        toggleAllBoxes(true);
        chart.setTitle("Last "+eventTF.getText()+" Matching Events");
        updateDatasetSeries();
        s.addobjects(chartPanel,this, layout,gbc,0,6,14 , 7, 1,100000,true);

        s.addobjects(box0,this, layout,gbc,4,14,2 , 1, 1,2,true);
        s.addobjects(box1,this, layout,gbc,6,14,2 , 1, 1,2,true);
        box0.setSelected(true);
        box1.setSelected(true);

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.frame.setSize(Gui.defaultWidth,Gui.defaultHeight);
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

        eventTF.addKeyListener(new KeyAdapter() {
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
            allTierButton.setEnabled(true);
            allFormatButton.setEnabled(true);
            toggleAllBoxes(true);
            runButton.setEnabled(true);
        }else{
            formatBoxes.forEach(x->x.setEnabled(false));
            tierBoxes.forEach(x->x.setEnabled(false));
            allTierButton.setEnabled(false);
            allFormatButton.setEnabled(false);
            toggleAllBoxes(false);
            runButton.setEnabled(false);
        }
    }

    public XYSeriesCollection getDataset(){
        return RaceDao.getDataset(RaceDao.getSeries(getSql()));
    }

    public void updateDatasetSeries(){
        dataset.removeAllSeries();
        dataset.addSeries(RaceDao.getSeries(getSql()));
        dataset.addSeries(RaceDao.getSeriesMovingAverage(getSql()));
        chart.setTitle(RaceDao.getRsRows(getSql())+" Matching Event(s) Found");
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

    public String getSql(){
        StringBuilder bob = new StringBuilder();
        bob.append("SELECT points"+"\n")
                .append("FROM (SELECT * FROM events ORDER BY eventid DESC LIMIT "+(Integer.parseInt(eventTF.getText()))+")"+"\n");
        if(dcBox.isSelected()){
            bob.append("Where nodc = true"+"\n");
        }else{
            bob.append("Where (nodc = true or nodc = false)"+"\n");
        }
        bob.append(tierBuilder()+"\n")
                .append(formatBuilder()+"\n")
                .append(tierBuilder()+"\n")
                .append("order by eventid");
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
    }

    public void setLastXResultLabel(){
        String input = eventTF.getText();
        if(input.equals("All")||input.equals("all")||input.equals("ALL")){
            int eventsPlayed = RaceDao.getEventsStored();
            double avg = RaceDao.getAvgPtsLastX(eventsPlayed);
            lastXResultLabel.setText("Last "+eventsPlayed+" events, average Score: "+avg);
            return;
        }
        if(InputVerifier.verifyLastX(input)){
            int lastX= Integer.parseInt(input);
            double avg = RaceDao.getAvgPtsLastX(lastX);
            lastXResultLabel.setText("Last "+lastX+" events, average Score: "+avg);
            return;
        }
        lastXResultLabel.setText("Invalid number, or not an Integer");
    }

    public void initialize(){
        int eventsStored = RaceDao.getEventsStored();
        if(eventsStored == 0){
            runButton.setEnabled(false);
        }
        eventTF.setText(String.valueOf(getInitialInteger()));
        enterXLabel2.setText("Events ("+ eventsStored+" events stored)");
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
        int number = 50;
        int eventsStored = RaceDao.getEventsStored();
        if(50>eventsStored){
            number = eventsStored;
        }
        return number;
    }


}
