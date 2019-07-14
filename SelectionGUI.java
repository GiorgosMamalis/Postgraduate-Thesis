package thesis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class SelectionGUI extends JFrame{
	public SelectionGUI(ArrayList<Table> tablesList, Parser p){
		setTitle("Select Action");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 100, 500, 200);
        setLayout(new BorderLayout());
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel("<html>Select action</html>");
        label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label.setBackground(SystemColor.menu);
        upperPanel.add(label);
        add(upperPanel, BorderLayout.NORTH);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        
        JButton graphButton = new JButton("Graphs");
        graphButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
    		GraphSelectionGUI gui = new GraphSelectionGUI(tablesList, p);
    		gui.setVisible(true);
    		setState(ICONIFIED);
        }});;
        mainPanel.add(graphButton);
        JButton metricsButton = new JButton("Metrics");
        metricsButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
        	MetricsGUI gui = new MetricsGUI(tablesList, p);
        	gui.setVisible(true);
        }});;
        mainPanel.add(metricsButton);
        add(mainPanel, BorderLayout.SOUTH);
	}
}
