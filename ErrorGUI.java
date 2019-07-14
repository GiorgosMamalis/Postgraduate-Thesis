package thesis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ErrorGUI extends JFrame{
	public ErrorGUI(){
		setTitle("Error");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 200, 300, 200);
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));
        JLabel firstLabel = new JLabel("<html>Can't create graph!!!</html>", SwingConstants.CENTER);
        firstLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        firstLabel.setBackground(SystemColor.menu);
        mainPanel.add(firstLabel);
        JLabel secondLabel = new JLabel("<html>No connections between nodes!!!</html>", SwingConstants.CENTER);
        secondLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        secondLabel.setBackground(SystemColor.menu);
        mainPanel.add(secondLabel);
        add(mainPanel, BorderLayout.NORTH);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.setLayout(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
        	dispose();
        }});;
        southPanel.add(closeButton);
        add(southPanel, BorderLayout.SOUTH);
	}
}
