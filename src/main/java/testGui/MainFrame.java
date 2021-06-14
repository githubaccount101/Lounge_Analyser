package testGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private DetailsPanel details;

    public MainFrame(String title){
        super(title);

        //Set Layout manager
        setLayout(new BorderLayout());

        //Create Swing components
        JTextArea textArea = new JTextArea();
        JButton mkwButton = new JButton("mkw");

        details = new DetailsPanel();

        // Add Swing components to content pane
        Container c = getContentPane();
        c.add(textArea,BorderLayout.CENTER);
        c.add(mkwButton,BorderLayout.SOUTH);
        c.add(details, BorderLayout.WEST);

        //add behaviour
        mkwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("Hello\n");
            }
        });
    }
}
