package thesis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;

public class TablesGUI extends JFrame{
	public TablesGUI(ArrayList<Table> tablesList, Parser p){
        setTitle("List of Tables");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(10, 100, 600, 200);
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        JPopupMenu tablesMenu = new JPopupMenu();
        JMenu[] subMenus = new JMenu[tablesList.size()/25 + 1];
        add(mainPanel, BorderLayout.CENTER);
        int count = 0;
        int limit = tablesList.size()/25 + 1;
        int times = 0;
        while(times < limit){
        	subMenus[times] = new JMenu("More Tables");
	        for(int i = count ; i < count + 25 ; i++){
	        	if(i == tablesList.size())
	        		break;
	        	else{
		        	Table t = tablesList.get(i);
		        	JMenuItem item = new JMenuItem(tablesList.get(i).getName());
		        	subMenus[times].add(item);
		        	item.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
		        		p.computeCohesion(t, false);
		            }});;
	        	}
	        }
	        count+=25;
	        if(times != 0)
	        	subMenus[times - 1].add(subMenus[times]);
	        times++;
        }
        tablesMenu.add(subMenus[0]);
        JButton searchButton = new JButton("Show Tables");
        searchButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e){
				if ( e.getButton() == 1 ){ // 1-left, 2-middle, 3-right button
					tablesMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
        });
    	mainPanel.add(searchButton);
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel("<html>List of tables names</html>");
        label.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        label.setBackground(SystemColor.menu);
        upperPanel.add(label);
        add(upperPanel, BorderLayout.NORTH);
	}
}
