package Ui.panels;

import java.awt.*;

public class Setter {
    public void addobjects(Component componente, Container yourcontainer,
                           GridBagLayout layout, GridBagConstraints gbc,
                           int gridx, int gridy, int gridwidth, int gridheight){

        gbc.gridx = gridx;
        gbc.gridy = gridy;

        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;

        if(gridheight>1){
            gbc.fill = GridBagConstraints.VERTICAL;
        }
        if(gridwidth>1){
            gbc.fill = GridBagConstraints.HORIZONTAL;
        }

        layout.setConstraints(componente, gbc);
        yourcontainer.add(componente);
    }
}
