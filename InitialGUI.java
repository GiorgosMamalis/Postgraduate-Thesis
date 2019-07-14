package thesis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InitialGUI extends JFrame{
	
	public InitialGUI() {
        setTitle("Browse");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 200, 300, 100);
        setLayout(new GridBagLayout());
        JButton browse = new JButton("Browse");
        browse.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
        	JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new java.io.File("."));
            fileChooser.setDialogTitle("Choose a File:");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".sql files", "sql");
            fileChooser.setFileFilter(filter);
            if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                new Parser().init(fileChooser.getSelectedFile().getAbsolutePath());
                dispose();
        }});;
        add(browse, new GridBagConstraints());
	}
	
	public static void main(String[] args) {
       EventQueue.invokeLater(new Runnable() {
            	public void run(){
            		try {
            			new InitialGUI().setVisible(true);
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
       }});
	}
}
