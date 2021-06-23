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

        gbc.fill = GridBagConstraints.NONE;

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

    public void addobjects(Component component, Container container, GridBagLayout layout, GridBagConstraints gbc,
                           int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, boolean filled){
        gbc.gridx = gridx;
        gbc.gridy = gridy;

        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;

        gbc.weighty = weighty;
        gbc.weightx = weightx;

        if(filled){
            gbc.fill= GridBagConstraints.BOTH;
        }else{
            gbc.fill = GridBagConstraints.NONE;
        }
        layout.setConstraints(component, gbc);
        container.add(component);
    }

    public void addobjects(Component component, Container container, GridBagLayout layout, GridBagConstraints gbc,
                           int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, boolean fillX, boolean fillY){
        gbc.gridx = gridx;
        gbc.gridy = gridy;

        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;

        gbc.weighty = weighty;
        gbc.weightx = weightx;

        if(fillX&fillY){
            gbc.fill= GridBagConstraints.BOTH;
        }else if(fillX==false&&fillY==false){
            gbc.fill = GridBagConstraints.NONE;
        }

        if(fillX) {
            gbc.fill = GridBagConstraints.HORIZONTAL;
        }
        if(fillY){
            gbc.fill = GridBagConstraints.VERTICAL;
        }

        layout.setConstraints(component, gbc);
        container.add(component);
    }


}
