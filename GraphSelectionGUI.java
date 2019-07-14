package thesis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class GraphSelectionGUI extends JFrame{
	public GraphSelectionGUI(ArrayList<Table> tablesList, Parser p){
		setTitle("Select Graph");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 100, 500, 200);
        setLayout(new BorderLayout());
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel("<html>Select type of graph</html>");
        label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label.setBackground(SystemColor.menu);
        upperPanel.add(label);
        add(upperPanel, BorderLayout.NORTH);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        JButton couplingButton = new JButton("Coupling Graph");
        couplingButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
    		dispose();
    		p.computeCoupling(false, false);
        }});;
        mainPanel.add(couplingButton);
        JButton cohesionButton = new JButton("Cohesion Graph");
        cohesionButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
        	TablesGUI gui = new TablesGUI(tablesList, p);
    		gui.setVisible(true);
    		dispose();
        }});;
        mainPanel.add(cohesionButton);
        add(mainPanel, BorderLayout.SOUTH);
	}
}
