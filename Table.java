package thesis;

import java.util.ArrayList;

public class Table {
	private String name;
	private ArrayList<String> attributes = new ArrayList<String>();
	private ArrayList<Query> queries = new ArrayList<Query>();
	
	public Table(String name, ArrayList<String> attributes){
		this.name = name;
		this.attributes = attributes;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<String> getAttributes(){
		return attributes;
	}
	
	public ArrayList<Query> getQueries(){
		return queries;
	}
	
	public boolean hasThatAttribute(String att){
		for(int i = 0 ; i < attributes.size() ; i++){
			if(attributes.get(i).equals(att))
				return true;
		}
		return false;
	}
	
	public void addQuery(Query q){
		queries.add(q);
	}
	
	public String toString(){
		String res = "Table name: " + name + "\nAttributes:";
		for(int i = 0 ; i < attributes.size() ; i++){
			res += attributes.get(i) + ", ";
		}
		res += "\nQueries that hit this table:";
		for(int i = 0 ; i < queries.size() ; i++){
			res += "\n" + queries.get(i).getFullQuery();
		}
		return res;
	}
}
