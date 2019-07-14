package thesis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MetricsGUI extends JFrame{
	public MetricsGUI(ArrayList<Table> tablesList, Parser p){
		setTitle("Select Metric");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 100, 500, 200);
        setLayout(new BorderLayout());
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel("<html>Select metric</html>");
        label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label.setBackground(SystemColor.menu);
        upperPanel.add(label);
        add(upperPanel, BorderLayout.NORTH);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        JButton cohesionButton = new JButton("Cohesion");
        cohesionButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
    		for(int i = 0 ; i < tablesList.size() ; i++){
    			p.computeCohesion(tablesList.get(i), true);
    		}
    		Metrics m = new Metrics();
    		m.printCohesionStats(p.getCohesionList(), tablesList);
        }});;
        mainPanel.add(cohesionButton);
        JButton degreeButton = new JButton("Coupling (Degree)");
        degreeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
        	p.computeCoupling(true, false);
        	HashMap<String, Integer> map = p.getCouplingDegreeMap();
        	Metrics m = new Metrics();
        	m.printCouplingStats(map);
        }});;
        mainPanel.add(degreeButton);
        JButton normalizedButton = new JButton("Coupling (Normalized Degree)");
        normalizedButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
        	p.computeCoupling(true, true);
        	HashMap<String, Double> map = p.getCouplingNormalizedDegreeMap();
        	Metrics m = new Metrics();
        	m.printCouplingNormalizedStats(map);
        }});;
        mainPanel.add(normalizedButton);
        add(mainPanel, BorderLayout.SOUTH);
	}
}
