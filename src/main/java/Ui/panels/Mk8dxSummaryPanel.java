package Ui.panels;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Consumer;

import Ui.RaceDao;
import mk8dx.TierD;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeriesCollection;
import shared.Format;


public class Mk8dxSummaryPanel extends JPanel {

    GridBagConstraints gbc = new GridBagConstraints();

    JLabel titleLabel = new JLabel("MK8DX Summary");
    JButton runButton = new JButton("Run");
    JCheckBox dcBox = new JCheckBox("Exclude Events with DC's");
    JButton buttonBack = new JButton("Back");

    JLabel enterXLabel= new JLabel("Limit to Last");
    JTextField eventTF = new JTextField("50");
    JLabel enterXLabel2= new JLabel("Events ("+ RaceDao.getEventsStored()+" events stored)");

    JLabel lastXResultLabel  = new JLabel("");

    JLabel tierLabel = new JLabel("choose tier(s)");
    JButton allTierButton = new JButton("All Tiers");

    JLabel formatLabel = new JLabel("choose format(s)");
    JButton allFormatButton = new JButton("All Formats");

    XYSeriesCollection dataset = new XYSeriesCollection();
    JFreeChart chart = RaceDao.getChart(dataset);
    ChartPanel chartPanel = RaceDao.getChartPanel(chart);

    JButton testButton2 = new JButton("test!!!!");

    ArrayList<JCheckBox> tierBoxes = new ArrayList<>();
    ArrayList<JCheckBox> formatBoxes = new ArrayList<>();

    ArrayList< ArrayList<JCheckBox>> allBoxes = new ArrayList<>();

    public Mk8dxSummaryPanel(CardLayout card, JPanel cardPane) {

        GridBagLayout layout = new GridBagLayout();
        allBoxes.add(tierBoxes); allBoxes.add(formatBoxes);

        setLayout(layout);
        gbc.insets = new Insets(3,3,3,3);

        Setter s = new Setter();

        s.addobjects(titleLabel,this, layout,gbc,0,0,2 , 1,1,2,true);
        s.addobjects(runButton,this, layout,gbc,2,0,1 , 1,1,2,true);
        s.addobjects(dcBox,this, layout,gbc,3,0,3 , 1,1,2,true);
        dcBox.setSelected(true);
        s.addobjects(buttonBack,this, layout,gbc,11,0,2 , 1,1,2,true);

        s.addobjects(enterXLabel,this, layout,gbc,0,1,2 , 1,1,2,true);
        s.addobjects(eventTF,this, layout,gbc,2,1,1 , 1,1,2,true);
        s.addobjects(enterXLabel2,this, layout,gbc,3,1,2 , 1,1,2,true);

        s.addobjects(lastXResultLabel,this, layout,gbc,2,3,5 , 1,1,2,true);
        setLastXResultLabel();

        s.addobjects(tierLabel,this, layout,gbc,0,4,2 , 1, 1,2,true);
        int i = 2;
        for(TierD t:TierD.values()){
            JCheckBox temp = new JCheckBox(t.getTier());
            s.addobjects(temp,this, layout,gbc,i,4,1 , 1, 1,2,true);
            tierBoxes.add(temp);
            i++;
        }
        s.addobjects(allTierButton,this, layout,gbc,11,4,2 , 1, 1,2,true);

        s.addobjects(formatLabel,this, layout,gbc,0,5,2 , 1, 1,2,true);
        i = 2;
        for(Format f:Format.values()){
            JCheckBox temp = new JCheckBox(f.getFormat());
            s.addobjects(temp,this, layout,gbc,i,5,1 , 1, 1,2,true);
            formatBoxes.add(temp);
            i++;
        }
        s.addobjects(allFormatButton,this, layout,gbc,11,5,2 , 1, 1,2,true);

        toggleAllBoxes(true);
        chart.setTitle("Last "+eventTF.getText()+" Matching Events");
        updateDatasetSeries();
        s.addobjects(chartPanel,this, layout,gbc,0,6,14 , 14, 100,100000,true);

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        testButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(getSql());
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
        chart.setTitle(RaceDao.getRsRows(getSql())+" Matching Events");
    }

    public ArrayList<TierD> tierCheck(){
        ArrayList<TierD> selected = new ArrayList<>();

        TierD[] tiers = TierD.values();

        int i = 0;
        for(JCheckBox box:tierBoxes){
            if(box.isSelected()){
                selected.add(tiers[i]);
            }
            i++;
        }

        return selected;
    }

    public ArrayList<Format> formatCheck(){
        ArrayList<Format> selected = new ArrayList<>();

        Format[] formats = Format.values();

        int i = 0;
        for(JCheckBox box:formatBoxes){
            if(box.isSelected()){
                selected.add(formats[i]);
            }
            i++;
        }

        return selected;
    }


    public String tierBuilder(){
        ArrayList<TierD> selectedTiers = tierCheck();
        String sql= "AND (tier like '";
        StringBuilder bob = new StringBuilder();
        bob.append(sql);
        if(selectedTiers.size()==1){
            bob.append(selectedTiers.get(0).getTier()+"')");
        }else{
            bob.append(selectedTiers.get(0).getTier()+"'");
            for(int i = 1;i<selectedTiers.size();i++){
                bob.append(" or tier like '"+selectedTiers.get(i).getTier()+"'");
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
                .append("FROM (SELECT * FROM eventsD ORDER BY eventid DESC LIMIT "+(Integer.parseInt(eventTF.getText()))+")"+"\n");
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
            double avg = RaceDao.getAvgPtsLastXDx(eventsPlayed);
            lastXResultLabel.setText("Last "+eventsPlayed+" events, average Score: "+avg);
            return;
        }
        if(InputVerifier.verifyLastX(input)){
            int lastX= Integer.parseInt(input);
            double avg = RaceDao.getAvgPtsLastXDx(lastX);
            lastXResultLabel.setText("Last "+lastX+" events, average Score: "+avg);
            return;
        }
        lastXResultLabel.setText("Invalid number, or not an Integer");
    }


}