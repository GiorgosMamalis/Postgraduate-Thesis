package thesis;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class GraphGUI extends JFrame {
    int width;
    int height;
    ArrayList<Node> nodes;
    ArrayList<edge> edges;
    
    
    public GraphGUI(ArrayList<String> allTables){
    	setTitle("Coupling graph (COMPLETE)");
    	setNodes(allTables);
    	for(int i = 0 ; i < allTables.size() ; i++){
    		for(int j = i + 1 ; j < allTables.size() ; j++){
    			addEdge(i, j);
    		}
    	}
    }
    
    public GraphGUI(ArrayList<String> allTables, ArrayList<ArrayList<String>> tablesWanted, ArrayList<Query> queriesWanted){
    	setTitle("Coupling graph");
    	setNodes(allTables);
		for(int k = 0 ; k < tablesWanted.size() ; k++){
			ArrayList<Integer> edgesList = new ArrayList<Integer>();
			ArrayList<String> tempList = tablesWanted.get(k);
			for(int i = 0 ; i < tempList.size() ; i++){
				for(int j = 0; j < allTables.size() ; j++){
					if(tempList.get(i).equals(allTables.get(j))){
						edgesList.add(j);
					}
				}
			}
			setEdges(edgesList);	
		}
		addQueriesButton(queriesWanted);
    }
    
    public GraphGUI(String tableName, ArrayList<String> allAttributes){
    	setTitle("Table " + tableName + " cohesion graph (COMPLETE)");
    	setNodes(allAttributes);
    	for(int i = 0 ; i < allAttributes.size() ; i++){
    		for(int j = i + 1 ; j < allAttributes.size() ; j++){
    			addEdge(i, j);
    		}
    	}
    }
    
    public GraphGUI(String tableName, ArrayList<String> allAttributes, ArrayList<ArrayList<String>> attributesWanted, ArrayList<Query> queriesWanted) {
    	setTitle("Table " + tableName + " cohesion graph");
    	setNodes(allAttributes);
		for(int k = 0 ; k < attributesWanted.size() ; k++){
			ArrayList<Integer> edgesList = new ArrayList<Integer>();
			ArrayList<String> tempList = attributesWanted.get(k);
			for(int i = 0 ; i < tempList.size() ; i++){
				for(int j = 0; j < allAttributes.size() ; j++){
					if(tempList.get(i).equals(allAttributes.get(j))){
						edgesList.add(j);
					}
				}
			}
			setEdges(edgesList);
		}
		addQueriesButton(queriesWanted);
    }
    
    private void setNodes(ArrayList<String> allAttributes){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		nodes = new ArrayList<Node>();
		edges = new ArrayList<edge>();
		width = 30;
		height = 30;
		int centerX = 600;
		int centerY = 300;
		int radius = 250;
		double angle = Math.toRadians(360 / allAttributes.size());
	    for (int i=0; i < allAttributes.size(); i++) {
	        double theta = i*angle;
	        int dx = (int)(radius * Math.sin(theta));
	        int dy = (int)(-radius * Math.cos(theta));
	        addNode(allAttributes.get(i), centerX + dx, centerY + dy);
	    }
    }
    
    private void addQueriesButton(ArrayList<Query> queriesWanted){
		setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton queriesButton = new JButton("Queries");
		JPopupMenu queriesMenu = new JPopupMenu();
		for(int i = 0 ; i < queriesWanted.size() ; i++){
			JMenuItem item = new JMenuItem(queriesWanted.get(i).getFullQuery());
			queriesMenu.add(item);
		}
		queriesButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e){
				if ( e.getButton() == 1 ){ // 1-left, 2-middle, 3-right button
					queriesMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
        buttonPanel.add(queriesButton);
		add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setEdges(ArrayList<Integer> edgesList){
    	for(int i = 0 ; i < edgesList.size() ; i++){
			for(int j = i + 1; j < edgesList.size() ; j++){
				addEdge(edgesList.get(i), edgesList.get(j));
			}
		}
    }

	class Node {
		int x, y;
		String name;
	
		public Node(String myName, int myX, int myY) {
		    x = myX;
		    y = myY;
		    name = myName;
		}
    }
    
    class edge {
		int i,j;
		
		public edge(int ii, int jj) {
		    i = ii;
		    j = jj;	    
		}
    }
    
    public void addNode(String name, int x, int y) { 
	//add a node at pixel (x,y)
	nodes.add(new Node(name,x,y));
	this.repaint();
    }
    public void addEdge(int i, int j) {
		//add an edge between nodes i and j
		edges.add(new edge(i,j));
		this.repaint();
    }
    
    public void paint(Graphics g) { // draw the nodes and edges
		FontMetrics f = g.getFontMetrics();
		int nodeHeight = Math.max(height, f.getHeight());
	
		g.setColor(Color.black);
		for (edge e : edges) {
		    g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
		    		nodes.get(e.j).x, nodes.get(e.j).y);
		}
	
		for (Node n : nodes) {
		    int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
		    g.setColor(Color.white);
		    g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
			       nodeWidth, nodeHeight);
		    g.setColor(Color.black);
		    g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
			       nodeWidth, nodeHeight);
		    
		    g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
				 n.y+f.getHeight()/2);
		}
    }
}