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

    public void addobjects(Component componente, Container yourcontainer, GridBagLayout layout, GridBagConstraints gbc,
                           int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty){
        gbc.gridx = gridx;
        gbc.gridy = gridy;

        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;

        gbc.weighty = weighty;
        gbc.weightx = weightx;

        gbc.fill= GridBagConstraints.BOTH;

        layout.setConstraints(componente, gbc);
        yourcontainer.add(componente);
    }

    public void addobjects(Component componente, Container yourcontainer, GridBagLayout layout, GridBagConstraints gbc,
                           int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, boolean filled){
        gbc.gridx = gridx;
        gbc.gridy = gridy;

        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;

        gbc.weighty = weighty;
        gbc.weightx = weightx;

        if(filled){
            gbc.fill= GridBagConstraints.BOTH;
        }

        layout.setConstraints(componente, gbc);
        yourcontainer.add(componente);
    }


}
