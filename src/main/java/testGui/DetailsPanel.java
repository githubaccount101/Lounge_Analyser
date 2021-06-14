package testGui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetailsPanel extends JPanel {
    public DetailsPanel(){
        Dimension size = getPreferredSize();
        size.width = 360;
        setPreferredSize(size);

        setBorder(BorderFactory.createTitledBorder("git gud"));

        JLabel nameLabel = new JLabel("Name: ");
        JLabel oLabel = new JLabel("job");

        JTextField nameField = new JTextField(10);
        JTextField oField = new JTextField(10);

        JButton addBtn = new JButton("add");

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String occ = oField.getText();

                String text = name  +": "+occ+"\n";
                System.out.println(text);
            }
        });

        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        //1st column//////////////////

        gc.anchor = GridBagConstraints.LINE_END;
        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx=0;
        gc.gridy=0;

        add(nameLabel, gc);

        gc.gridx=0;
        gc.gridy=1;
        add(oLabel,gc);

        //2nd colimn//////////

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx=1;
        gc.gridy=0;
        add(nameField,gc);

        gc.gridx=1;
        gc.gridy=1;
        add(oField,gc);

        //Final Row
        gc.weighty = 10;

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx=1;
        gc.gridy=2;
        add(addBtn, gc);
    }

}
