package Ui.panels;

import mk8dx.EventD;

import javax.swing.*;
import java.awt.*;

public class MkwDcPanel extends JPanel {

    static EventD event = null;

    public MkwDcPanel(CardLayout card, JPanel cardPane) {

    }

    public void  setEvent(EventD currentEvent){
        if(event==null) {
            event = currentEvent;
        }else{
            System.out.println("event initialization error, static eventD placeholder was not null after tier/format input");
        }
    }
}
