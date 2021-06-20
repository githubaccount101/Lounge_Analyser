package Ui.panels;

import Ui.RaceDao;
import mkw.*;
import shared.Format;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MkwAdvStatsPanel extends JPanel {

    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    JLabel enterXLabel= new JLabel("Limit to Last");
    JTextField enterXTF = new JTextField(String.valueOf(RaceDao.getEventsStored()));
    JLabel enterXLabel2= new JLabel("Events ("+ RaceDao.getEventsStored()+" events stored)");
    JLabel lastXResultLabel  = new JLabel("");
    JButton backButton = new JButton("Back");

    JLabel quickLabel = new JLabel("Quick Lookup");
    JButton bestTracksButton = new JButton("Best Tracks");
    JButton bestFormatsButton = new JButton("Best Formats");
    JButton bestTiersButton = new JButton("Best Tiers");
    JButton bestStartsButton = new JButton("Best Start Positions");

    JLabel titleLabel = new JLabel("Advanced Lookup");
    JButton resetButton = new JButton("Reset");
    JButton runButton = new JButton("Run");
    JButton testButton = new JButton("test");

    JLabel selectLabel = new JLabel("Select Aspect to rank:");
    JButton tierButton = new JButton("tier");
    JButton formatButton = new JButton("format");
    JButton trackButton = new JButton("track");
    JButton startButton = new JButton("Start Position");

    JLabel tierLabel = new JLabel("choose tier(s)");
    JButton allTierButton = new JButton("All Tiers");

    JLabel formatLabel = new JLabel("choose format(s)");
    JButton allFormatButton = new JButton("All Formats");

    JLabel startLabel = new JLabel("choose start(s)");
    JButton allStartButton = new JButton("All Start");

    JLabel finishLabel = new JLabel("choose finish(s)");
    JButton allFinishButton = new JButton("All Finish");

    JLabel trackLabel = new JLabel("choose track(s)");
    JButton allTrackButton = new JButton("All tracks");

    JButton selectAllBoxesButton = new JButton("Select All");

    JScrollPane scrollPane = new JScrollPane(RaceDao.getTrackTableBasic());

    ArrayList<JButton> quickButtons = new ArrayList<>();
    ArrayList<JButton> otherButtons = new ArrayList<>();
    ArrayList<JButton> allContraintButtons = new ArrayList<>();
    ArrayList<JCheckBox> tierBoxes = new ArrayList<>();
    ArrayList<JCheckBox> formatBoxes = new ArrayList<>();
    ArrayList<JCheckBox> startBoxes = new ArrayList<>();
    ArrayList<JCheckBox> finishBoxes = new ArrayList<>();
    ArrayList<JCheckBox> trackBoxes = new ArrayList<>();

    ArrayList<ArrayList<JCheckBox>> allboxes = new ArrayList<>();

    SearchState state = SearchState.NONE;

    public MkwAdvStatsPanel(CardLayout card, JPanel cardPane) {

        setLayout(layout);
        gbc.insets = new Insets(2,2,2,2);

        Setter s = new Setter();

        s.addobjects(enterXLabel,this, layout,gbc,0,0,2 , 1,1,2,true);
        s.addobjects(enterXTF,this, layout,gbc,3,0,1 , 1,1,2,true);
        s.addobjects(enterXLabel2,this, layout,gbc,4,0,4 , 1,1,2,true);
        setLastXResultLabel();
        s.addobjects(lastXResultLabel,this, layout,gbc,8,0 , 10,1,1,2,true);
        s.addobjects(backButton,this, layout,gbc,18,0,2 , 1,1,2,true);

        s.addobjects(quickLabel,this, layout,gbc,0,1,3 , 1,1,2,true);
        s.addobjects(bestTracksButton,this, layout,gbc,3,1,3, 1,1,2,true);
        s.addobjects(bestFormatsButton,this, layout,gbc,6,1 , 3,1,1,2,true);
        s.addobjects(bestTiersButton,this, layout,gbc,9,1 , 3,1,1,2,true);
        s.addobjects(bestStartsButton,this, layout,gbc,12,1 , 3,1,1,2,true);

        runButton.setEnabled(false);
        s.addobjects(titleLabel,this, layout,gbc,0,2,2 , 1,1,2,true);
        s.addobjects(runButton,this, layout,gbc,3,2,3 , 1,1,2,true);
        s.addobjects(resetButton,this, layout,gbc,6,2 , 3,1,1,2,true);
        s.addobjects(testButton,this, layout,gbc,9,2 , 3,1,1,2,true);

        s.addobjects(selectLabel,this, layout,gbc,0,3,2 , 1,1,2,false);
        s.addobjects(trackButton,this, layout,gbc,3,3,3 , 1,1,2,true);
        s.addobjects(formatButton,this, layout,gbc,6,3,3 , 1,1,2,true);
        s.addobjects(tierButton,this, layout,gbc,9,3,3 , 1,1,2,true);
        s.addobjects(startButton,this, layout,gbc,12,3,3 , 1,1,2,true);

        s.addobjects(tierLabel,this, layout,gbc,0,4,2 , 1, 1,2,false);
        int i = 3;
        for(Tier t:Tier.values()){
            JCheckBox temp = new JCheckBox(t.getTier());
            s.addobjects(temp,this, layout,gbc,i,4,1 , 1, 1,2,false);
            tierBoxes.add(temp);
            i++;
        }
        s.addobjects(allTierButton,this, layout,gbc,18,4,2 , 1, 1,2,false);

        s.addobjects(formatLabel,this, layout,gbc,0,5,2 , 1, 1,2,false);
        i = 3;
        for(Format f:Format.values()){
            JCheckBox temp = new JCheckBox(f.getFormat());
            temp.setFont(new Font("Arial", Font.PLAIN, 12));
            s.addobjects(temp,this, layout,gbc,i,5,1 , 1, 1,2,false);
            formatBoxes.add(temp);
            i++;
        }
        s.addobjects(allFormatButton,this, layout,gbc,18,5,2 , 1, 1,2,false);


        s.addobjects(startLabel,this, layout,gbc,0,6,2 , 1, 1,2,false);
        i = 3;
        for(int n = 1; n<=12; n++){
            String name = String.valueOf(n);
            JCheckBox temp = new JCheckBox(name);
            s.addobjects(temp,this, layout,gbc,i,6,1 , 1, .5,2,false);
            startBoxes.add(temp);
            i++;
        }
        s.addobjects(allStartButton,this, layout,gbc,18,6,2 , 1, 1,2,false);

        s.addobjects(finishLabel,this, layout,gbc,0,7,2 , 1, .5,2,false);
        i = 3;
        for(int n = 1; n<=12; n++){
            String name = String.valueOf(n);
            JCheckBox temp = new JCheckBox(name);
            s.addobjects(temp,this, layout,gbc,i,7,1 , 1, 1,12,false);
            finishBoxes.add(temp);
            i++;
        }
        s.addobjects(allFinishButton,this, layout,gbc,18,7,2 , 1, 1,2,false);

        s.addobjects(trackLabel,this, layout,gbc,0,8,2 , 1, 1,2,false);
        i = 3;
        Track[] tracks = Track.values();
        for(int n = 0;n<4;n++){
            for(int x =0; x<=7;x++){
                String name = tracks[n+x*4].getAbbreviation();
                JCheckBox temp = new JCheckBox(name);
                if(name.length()>3){
                    temp.setFont(new Font("Arial", Font.PLAIN, 10));
                }else{
                    temp.setFont(new Font("Arial", Font.PLAIN, 12));
                }
                s.addobjects(temp,this, layout,gbc,i,(n+8),1 , 1, 5,12,false);
                trackBoxes.add(temp);
                i++;
            }
            i=3;
        }
        s.addobjects(allTrackButton,this, layout,gbc,18,8,2 , 1, 1,2,false);

        s.addobjects(selectAllBoxesButton,this, layout,gbc,18,11,2 , 1, 1,2,false);

        s.addobjects(scrollPane,this, layout,gbc,0,12,24 , 10, 1,10000,true);

        fillAllBoxes();
        addQuickButtons();
        addConstraintButtons();
        addOtherButtons();
        enableAllBoxesAndConstraintButtons(false);
        limitCheck();

        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("------");
                System.out.println(tierBuilder());
                System.out.println(formatBuilder());
                System.out.println(startBuilder());
                System.out.println(finishBuilder());
                System.out.println(trackBuilder());
                System.out.println("");
                System.out.println(getTrackSql());
                System.out.println("");
                System.out.println(getFormatSql());
                System.out.println("");
                System.out.println(getTierSql());
                System.out.println("");
                System.out.println(getTrackSql());
                System.out.println("------");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPane,"mkwMenu");
                scrollPane.setViewportView(RaceDao.getTrackTableBasic());
                enableAllBoxesAndConstraintButtons(false);
                toggleAllBoxes(false);
                otherButtons.forEach(x->x.setEnabled(true));
                enterXTF.setText(String.valueOf(RaceDao.getEventsStored()));
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAllBoxesAndConstraintButtons(false);
                toggleAllBoxes(false);
                otherButtons.forEach(x->x.setEnabled(true));
                enterXTF.setText(String.valueOf(RaceDao.getEventsStored()));
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("the aspect is"+state);
                if(state.equals(SearchState.TRACK)){
                    scrollPane.setViewportView(RaceDao.getSetTable(getTrackSql()));
                }
                if(state.equals(SearchState.FORMAT)){
                    scrollPane.setViewportView(RaceDao.getSetTable(getFormatSql()));
                }
                if(state.equals(SearchState.TIER)){
                    scrollPane.setViewportView(RaceDao.getSetTable(getTierSql()));
                }
                if(state.equals(SearchState.START)){
                    scrollPane.setViewportView(RaceDao.getSetTable(getStartSql()));
                }

            }
        });

        bestTracksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAllBoxesAndConstraintButtons(false);
                otherButtons.forEach(x->x.setEnabled(false));
                resetButton.setEnabled(true);
                runButton.setEnabled(false);
                toggleAllBoxes(true);
                scrollPane.setViewportView(RaceDao.getSetTable(getTrackSql()));
                state=SearchState.NONE;
            }
        });

        bestFormatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAllBoxesAndConstraintButtons(false);
                otherButtons.forEach(x->x.setEnabled(false));
                resetButton.setEnabled(true);
                runButton.setEnabled(false);
                toggleAllBoxes(true);
                scrollPane.setViewportView(RaceDao.getSetTable(getFormatSql()));
                state=SearchState.NONE;
            }
        });

        bestTiersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAllBoxesAndConstraintButtons(false);
                otherButtons.forEach(x->x.setEnabled(false));
                resetButton.setEnabled(true);
                runButton.setEnabled(false);
                toggleAllBoxes(true);
                scrollPane.setViewportView(RaceDao.getSetTable(getTierSql()));
                state=SearchState.NONE;
            }
        });

        bestStartsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAllBoxesAndConstraintButtons(false);
                otherButtons.forEach(x->x.setEnabled(false));
                resetButton.setEnabled(true);
                runButton.setEnabled(false);
                toggleAllBoxes(true);
                scrollPane.setViewportView(RaceDao.getSetTable(getStartSql()));
                state=SearchState.NONE;
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

        allFinishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isAllSelected(finishBoxes)){
                    toggleBoxes(finishBoxes,false);
                }else{
                    toggleBoxes(finishBoxes,true);
                }
                if(atLeastOneBoxOfEachIsSelected()){
                    runButton.setEnabled(true);
                }else{
                    runButton.setEnabled(false);
                }
            }
        });

        allTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isAllSelected(trackBoxes)){
                    toggleBoxes(trackBoxes,false);
                }else{
                    toggleBoxes(trackBoxes,true);
                }
                if(atLeastOneBoxOfEachIsSelected()){
                    runButton.setEnabled(true);
                }else{
                    runButton.setEnabled(false);
                }
            }
        });

        trackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollPane.setViewportView(RaceDao.getTrackTableBasic());
                if(formatButton.isEnabled()==false&&tierButton.isEnabled()==false){
                    formatButton.setEnabled(true);
                    tierButton.setEnabled(true);
                    startButton.setEnabled(true);
                    enableAllBoxesAndConstraintButtons(false);
                    toggleAllBoxes(false);
                    state = SearchState.NONE;

                }else if(formatButton.isEnabled()==true&&tierButton.isEnabled()==true){
                    formatButton.setEnabled(false);
                    tierButton.setEnabled(false);
                    startButton.setEnabled(false);
                    enableAllBoxesAndConstraintButtons(true);
                    allTrackButton.setEnabled(false);
                    enableBoxes(trackBoxes,false);
                    toggleBoxes(trackBoxes,true);
                    state = SearchState.TRACK;
                }
            }
        });

        formatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollPane.setViewportView(RaceDao.getFormatTableBasic());
                if(trackButton.isEnabled()==false&&tierButton.isEnabled()==false){
                    trackButton.setEnabled(true);
                    tierButton.setEnabled(true);
                    startButton.setEnabled(true);
                    enableAllBoxesAndConstraintButtons(false);
                    toggleAllBoxes(false);
                    state = SearchState.NONE;

                }else if(trackButton.isEnabled()==true&&tierButton.isEnabled()==true){
                    trackButton.setEnabled(false);
                    tierButton.setEnabled(false);
                    startButton.setEnabled(false);
                    enableAllBoxesAndConstraintButtons(true);
                    allFormatButton.setEnabled(false);
                    enableBoxes(formatBoxes,false);
                    toggleBoxes(formatBoxes,true);
                    state = SearchState.FORMAT;
                }
            }
        });

        tierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollPane.setViewportView(RaceDao.getTierTableBasic());
                if(formatButton.isEnabled()==false&&trackButton.isEnabled()==false){
                    formatButton.setEnabled(true);
                    trackButton.setEnabled(true);
                    startButton.setEnabled(true);
                    enableAllBoxesAndConstraintButtons(false);
                    toggleAllBoxes(false);
                    state = SearchState.NONE;
                }else if(formatButton.isEnabled()==true&&trackButton.isEnabled()==true){
                    trackButton.setEnabled(false);
                    formatButton.setEnabled(false);
                    startButton.setEnabled(false);
                    enableAllBoxesAndConstraintButtons(true);
                    allTierButton.setEnabled(false);
                    enableBoxes(tierBoxes,false);
                    toggleBoxes(tierBoxes,true);
                    state = SearchState.TIER;
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollPane.setViewportView(RaceDao.getStartTableBasic());
                if(formatButton.isEnabled()==false&&trackButton.isEnabled()==false){
                    formatButton.setEnabled(true);
                    trackButton.setEnabled(true);
                    tierButton.setEnabled(true);
                    enableAllBoxesAndConstraintButtons(false);
                    toggleAllBoxes(false);
                    state = SearchState.NONE;
                }else if(formatButton.isEnabled()==true&&trackButton.isEnabled()==true){
                    trackButton.setEnabled(false);
                    formatButton.setEnabled(false);
                    tierButton.setEnabled(false);
                    enableAllBoxesAndConstraintButtons(true);
                    allStartButton.setEnabled(false);
                    enableBoxes(startBoxes,false);
                    toggleBoxes(startBoxes,true);
                    state = SearchState.START;
                }
            }
        });

        enterXTF.getDocument().addDocumentListener(new DocumentListener() {
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

        allboxes.forEach(x->x.forEach(new Consumer<JCheckBox>() {
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

        selectAllBoxesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(everyBoxIsSelected()){
                    allboxes.forEach(x->x.forEach(new Consumer<JCheckBox>() {
                        @Override
                        public void accept(JCheckBox jCheckBox) {
                            if(jCheckBox.isEnabled()){
                                jCheckBox.setSelected(false);
                            }
                        }
                    }));
                }else{
                    toggleAllBoxes(true);
                }
                if(atLeastOneBoxOfEachIsSelected()){
                    runButton.setEnabled(true);
                }else{
                    runButton.setEnabled(false);
                }
            }
        });
    }

    public void addQuickButtons(){
        quickButtons.add(bestTracksButton);
        quickButtons.add(bestStartsButton);
        quickButtons.add(bestTiersButton);
        quickButtons.add(bestFormatsButton);
    }

    public void addConstraintButtons(){
        allContraintButtons.add(allTierButton);
        allContraintButtons.add(allFormatButton);
        allContraintButtons.add(allStartButton);
        allContraintButtons.add(allFinishButton);
        allContraintButtons.add(allTrackButton);
        allContraintButtons.add(selectAllBoxesButton);
    }

    public void addOtherButtons(){
        otherButtons.add(resetButton);
        otherButtons.add(tierButton);
        otherButtons.add(trackButton);
        otherButtons.add(formatButton);
        otherButtons.add(startButton);
    }

    public void fillAllBoxes(){
        allboxes.add(tierBoxes);
        allboxes.add(formatBoxes);
        allboxes.add(startBoxes);
        allboxes.add(finishBoxes);
        allboxes.add(trackBoxes);
    }

    public void toggleBoxes(ArrayList<JCheckBox> boxes, boolean onOff){
        boxes.forEach(x->x.setSelected(onOff));
    }

    public void enableBoxes(ArrayList<JCheckBox> boxes, boolean onOff){
        boxes.forEach(x->x.setEnabled(onOff));
    }

    public void toggleAllBoxes( boolean onOff){
        allboxes.forEach(x->x.forEach(y->y.setSelected(onOff)));
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

    public void enableAllBoxesAndConstraintButtons(boolean onOff){
        allboxes.forEach(x->x.forEach(y->y.setEnabled(onOff)));
        allContraintButtons.forEach(x->x.setEnabled(onOff));
    }

    public void setLastXResultLabel(){
        String input = enterXTF.getText();
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

    public void limitCheck(){
        String input = enterXTF.getText();
        if(InputVerifier.verifyLastX(input)){
            otherButtons.forEach(x->x.setEnabled(true));
            quickButtons.forEach(x->x.setEnabled(true));
            enableAllBoxesAndConstraintButtons(false);

        }else{
            otherButtons.forEach(x->x.setEnabled(false));
            quickButtons.forEach(x->x.setEnabled(false));
            enableAllBoxesAndConstraintButtons(false);
            toggleAllBoxes(false);
            runButton.setEnabled(false);
        }
    }

    public boolean atLeastOneBoxOfEachIsSelected(){
        boolean tier = false;
        boolean format = false;
        boolean start = false;
        boolean finish = false;
        boolean track = false;
        for(JCheckBox box : tierBoxes){
            if(box.isSelected()){
                tier = true;
            }
        }
        for(JCheckBox box : formatBoxes){
            if(box.isSelected()){
                format = true;
            }
        }
        for(JCheckBox box : startBoxes){
            if(box.isSelected()){
                start = true;
            }
        }
        for(JCheckBox box : finishBoxes){
            if(box.isSelected()){
                finish = true;
            }
        }
        for(JCheckBox box : trackBoxes){
            if(box.isSelected()){
                track = true;
            }
        }
        if(tier&&format&&start&&finish&&track) {
            return true;
        }
        return false;
    }

    public boolean everyBoxIsSelected(){
        boolean tier = true;
        boolean format = true;
        boolean start = true;
        boolean finish = true;
        boolean track = true;
        for(JCheckBox box : tierBoxes){
            if(box.isSelected()==false){
                tier = false;
            }
        }
        for(JCheckBox box : formatBoxes){
            if(box.isSelected()==false){
                format = true;
            }
        }
        for(JCheckBox box : startBoxes){
            if(box.isSelected()==false){
                start =false;
            }
        }
        for(JCheckBox box : finishBoxes){
            if(box.isSelected()==false){
                finish = false;
            }
        }
        for(JCheckBox box : trackBoxes){
            if(box.isSelected()==false){
                track = false;
            }
        }
        if(tier&&format&&start&&finish&&track) {
            return true;
        }
        return false;
    }

    public ArrayList<Track> trackCheck(){
        ArrayList<Track> selected = new ArrayList<>();
        Track[] tracks = Track.values();
        Track[] tracksReordered = new Track[32];

        int i = 0;
        for(int n = 0;n<4;n++){
            for(int x =0; x<=7;x++){
                tracksReordered[i] = tracks[n+x*4];
                i++;
            }
        }
        i = 0;
        for(JCheckBox box:trackBoxes){
            if(box.isSelected()){
                selected.add(tracksReordered[i]);
            }
            i++;
        }
        return selected;
    }

    public ArrayList<Tier> tierCheck(){
        ArrayList<Tier> selected = new ArrayList<>();

        Tier[] tiers = Tier.values();

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

    public ArrayList<Integer> finishCheck(){

        ArrayList<Integer> selected = new ArrayList<>();

        int i = 1;
        for(JCheckBox box:finishBoxes){
            if(box.isSelected()==true){
                selected.add(i);
            }
            i++;
        }

        return selected;
    }

    public String trackBuilder(){
        ArrayList<Track> selectedTracks = trackCheck();
        String sql= "AND (track like '";
        StringBuilder bob = new StringBuilder();
        bob.append(sql);
        if(selectedTracks.size()==1){
            bob.append(selectedTracks.get(0).getSearchName()+"')");
        }else{
            bob.append(selectedTracks.get(0).getSearchName()+"'");
            for(int i = 1;i<selectedTracks.size();i++){
                bob.append(" or track like '"+selectedTracks.get(i).getSearchName()+"'");
            }
            bob.append(")");
        }
        return bob.toString();
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

    public String finishBuilder(){
        ArrayList<Integer> SelectedFinishes = finishCheck();
        String sql= "AND (finish=";
        StringBuilder bob = new StringBuilder();
        bob.append(sql);
        if(SelectedFinishes.size()==1){
            bob.append(SelectedFinishes.get(0)+")");
        }else{
            bob.append(SelectedFinishes.get(0));
            for(int i = 1;i<SelectedFinishes.size();i++){
                bob.append(" or finish="+SelectedFinishes.get(i));
            }
            bob.append(")");
        }
        return bob.toString();
    }

    public String getTrackSql(){
        String sql= "";
        StringBuilder bob = new StringBuilder();
        bob.append("SELECT track, COUNT(track)AS \"Races Found\", ROUND(avg(finish),1)AS \"AVG Finish\" , Round(avg(points),1) AS \"AVG Points\""+"\n")
                .append("FROM (select * from races order by raceid desc limit "+(Integer.parseInt(enterXTF.getText())*12)+")"+"\n")
                .append("Where track not like 'none'"+"\n")
                .append(tierBuilder()+"\n")
                .append(formatBuilder()+"\n")
                .append(startBuilder()+"\n")
                .append(finishBuilder()+"\n")
                .append("group by track \n" +
                        "HAVING COUNT(track) >0\n" +
                        "ORDER BY avg(points) desc");
        return bob.toString();
    }

    public String getFormatSql(){
        String sql= "";
        StringBuilder bob = new StringBuilder();
        bob.append("SELECT format, COUNT(format)AS \"Races Found\", ROUND(avg(finish),1)AS \"AVG Finish\" , Round(avg(points),1) AS \"AVG Points\""+"\n")
                .append("FROM (select * from races order by raceid desc limit "+(Integer.parseInt(enterXTF.getText())*12)+")"+"\n")
                .append("Where track not like 'none'"+"\n")
                .append(tierBuilder()+"\n")
                .append(startBuilder()+"\n")
                .append(finishBuilder()+"\n")
                .append(trackBuilder()+"\n")
                .append("group by format \n" +
                        "HAVING COUNT(format) >0\n" +
                        "ORDER BY avg(points) desc");
        return bob.toString();
    }

    public String getTierSql(){
        String sql= "";
        StringBuilder bob = new StringBuilder();
        bob.append("SELECT tier, COUNT(tier)AS \"Races Found\", ROUND(avg(finish),1)AS \"AVG Finish\" , Round(avg(points),1) AS \"AVG Points\""+"\n")
                .append("FROM (select * from races order by raceid desc limit "+(Integer.parseInt(enterXTF.getText())*12)+")"+"\n")
                .append("Where track not like 'none'"+"\n")
                .append(formatBuilder()+"\n")
                .append(startBuilder()+"\n")
                .append(finishBuilder()+"\n")
                .append(trackBuilder()+"\n")
                .append("group by tier \n" +
                        "HAVING COUNT(tier) >0\n" +
                        "ORDER BY avg(points) desc");
        return bob.toString();
    }

    public String getStartSql(){
        String sql= "";
        StringBuilder bob = new StringBuilder();
        bob.append("SELECT start, COUNT(start)AS \"Races Found\", ROUND(avg(finish),1)AS \"AVG Finish\" , Round(avg(points),1) AS \"AVG Points\""+"\n")
                .append("FROM (select * from races order by raceid desc limit "+(Integer.parseInt(enterXTF.getText())*12)+")"+"\n")
                .append("Where track not like 'none'"+"\n")
                .append(tierBuilder()+"\n")
                .append(formatBuilder()+"\n")
                .append(finishBuilder()+"\n")
                .append(trackBuilder()+"\n")
                .append("group by start \n" +
                        "HAVING COUNT(start) >0\n" +
                        "ORDER BY avg(points) desc");
        return bob.toString();
    }
}
