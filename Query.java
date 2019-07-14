package thesis;

import java.util.ArrayList;

public class Query {
	private String fullQuery;
	private ArrayList<String> attributes = new ArrayList<String>();
	private ArrayList<String> tables = new ArrayList<String>();
	
	public Query(String fullQuery, ArrayList<String> attributes, ArrayList<String> tables){
		this.fullQuery = fullQuery;
		this.attributes = attributes;
		this.tables = tables;
	}
	
	public ArrayList<String> getTablesHit(){
		return tables;
	}
	
	public ArrayList<String> getAttributesWanted(){
		return attributes;
	}
	
	public String getFullQuery(){
		return fullQuery;
	}
	
	public String toString(){
		String res = "Full Query: " + fullQuery + "\nAttributes wanted: ";
		for(int i = 0 ; i < attributes.size() ; i++){
			res += attributes.get(i) + ", ";
		}
		res += "\nTables hit: ";
		for(int i = 0 ; i < tables.size() ; i++){
			res += tables.get(i) + ", ";
		}
		return res;
	}
}
