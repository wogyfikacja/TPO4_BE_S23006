package zad1;
import javax.swing.*;
import zad1.Client;

import org.w3c.dom.Text;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI {  
    public static void main(String args[]) throws Exception {
       // Client client = new Client("localhost",8900);


        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
 
        JMenuBar ob = new JMenuBar();
        JMenu ob1 = new JMenu("FILE");
        JMenu ob2 = new JMenu("Help");
        ob.add(ob1);
        ob.add(ob2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        ob1.add(m11);
        ob1.add(m22);
 
         
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text");
        JButton reset = new JButton("Reset");
        
        String[] topics = {"Topic1","Topic2","Topic3","Topic4","Topic5","Topic6","Topic7","Topic8","Topic9","Topic10"};

        ListModel<String> model = new DefaultListModel<String>();
        for(String topic:topics){
            model.addElement(topic);
        }

        JList<String> list = new JList<String>(model);
        list.setCellRenderer(new CheckboxListCellRenderer());
        list.updateUI();
        
        

        panel.add(label); // Components Added using Flow Layout
        panel.add(label); // Components Added using Flow Layout
        panel.add(reset);
        panel.add(list);
        JTextArea ta = new JTextArea();
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ta.append(list.getSelectedValuesList().toString());
            }
        });
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, list);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }

}
