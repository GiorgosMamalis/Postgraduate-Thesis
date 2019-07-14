package thesis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
	private boolean join = false;
	private ArrayList<Table> tablesList = new ArrayList<Table>();
	private ArrayList<Query> queriesList = new ArrayList<Query>();
	private PrintWriter outputStream = null;
	private PrintWriter outputStream2 = null;
	private Scanner inputStream = null;
	private ArrayList<Double> cohesionList = new ArrayList<Double>();
	private HashMap<String, Integer> couplingDegreeMap = new HashMap<String, Integer>();
	private HashMap<String, Double> couplingNormalizedDegreeMap = new HashMap<String, Double>();

	public void init(String filename){
		openFiles(filename);
		editTables();
		editQueries();
		assignQueriesToTables();
		SelectionGUI gui = new SelectionGUI(tablesList, this);
		gui.setVisible(true);
	}
	
	private void openFiles(String filename){
		try{
			outputStream = new PrintWriter(new FileOutputStream("create.txt"));
			outputStream2 = new PrintWriter(new FileOutputStream("select.txt"));
			inputStream = new Scanner(new FileInputStream(filename));
		}
		catch(FileNotFoundException e){
			System.out.println("Problem opening files!!!");
			System.exit(0);
		}
		while(inputStream.hasNextLine()){
			String line = inputStream.nextLine();
			line = getLine(line);
			String[] words = line.split("\\s");
			if(words[0].equals("CREATE")){
				if(words[1].equals("TABLE")){
					outputStream.println(line.trim());
				}
			}
			else if(words[0].equals("SELECT")){
				outputStream2.println(line.trim());
			}
		}
		outputStream.close();
		outputStream2.close();
		inputStream.close();
	}
	
	private String getLine(String line){
		line = line.trim();
		line = line.replaceAll("=", " = ");
		line = line.replaceAll("\\s{2,}", " ");
		line = line.replaceAll("`", "");
		line = line.replaceAll("\\bfrom\\b", "FROM");
		line = line.replaceAll("\\bselect\\b", "SELECT");
		line = line.replaceAll("\\bwhere\\b", "WHERE");
		line = line.replaceAll("\\bcreate\\b", "CREATE");
		line = line.replaceAll("\\btable\\b", "TABLE");
		line = line.replaceAll("\\border\\b", "ORDER");
		line = line.replaceAll("\\bindex\\b", "INDEX");
		line = line.replaceAll("\\band\\b", "AND");
		line = line.replaceAll("\\bjoin\\b", "JOIN");
		return line;
	}
	
	private void editTables(){
		try{
			inputStream = new Scanner(new FileInputStream("create.txt"));
		}
		catch(FileNotFoundException e){
			System.out.println("Problem opening file!!!");
			System.exit(0);
		}
		while(inputStream.hasNextLine()){
			String line = inputStream.nextLine();
			line = line.trim();
			String[] nameAndFirstArgument = line.split("\\(");
			String tempArray[] = nameAndFirstArgument[0].split("\\s+");
			String tableName = tempArray[tempArray.length - 1];
			tempArray = nameAndFirstArgument[1].trim().split("\\s+");
			String firstArgument = tempArray[0];
			ArrayList<String> argumentsList = new ArrayList<String>();
			argumentsList.add(firstArgument);
			String[] restArguments = line.split(",");
			for(int i = 1 ; i < restArguments.length ; i++){
				restArguments[i] = restArguments[i].trim();
				tempArray = restArguments[i].split("\\s+");
				if(!isKeyWord(tempArray[0]))
					if(!containsElement(argumentsList, tempArray[0])){
						argumentsList.add(tempArray[0].replaceAll("[()]",""));
					}
			}
			Table newTable = new Table(tableName, argumentsList);
			if(!isAlreadyIn(tableName)){
				tablesList.add(newTable);
			}
		}
		inputStream.close();
	}
	
	private boolean isKeyWord(String word){
		if(word.equals("PRIMARY") || word.equals("KEY") || word.equals("UNIQUE") || word.equals("FOREIGN") || word.equals("CONSTRAINT") || word.equals("INDEX"))
			return true;
		return false;
	}
	
	private boolean containsElement(ArrayList<String> list, String element){
		for(int i = 0 ; i < list.size() ; i++){
			if(list.get(i).equals(element.replaceAll("[()]",""))){
				return true;
			}
		}
		return false;
	}
	
	private void editQueries(){
		try{
			inputStream = new Scanner(new FileInputStream("select.txt"));
		}
		catch(FileNotFoundException e){
			System.out.println("Problem opening file!!!");
			System.exit(0);
		}
		while(inputStream.hasNextLine()){
			String line = inputStream.nextLine();
			line = line.replaceAll("[()]", "");
			line = line.trim();
			String[] from = line.split("FROM");
			String[] select = from[0].split("SELECT");
			String attributes = select[1].trim();
			String[] where = from[1].split("WHERE");
			if(where.length == 1)
				where = from[1].split("ORDER");
			String tables = where[0].trim();
			String[] listOfAttributes = attributes.split(",");
			ArrayList<String> attList = new ArrayList<String>();
			for(int i = 0 ; i < listOfAttributes.length ; i++){
				attList.add(listOfAttributes[i].trim());
			}
			if(where.length > 1){
				String rest = where[1].trim();
				String[] and = rest.split("AND");
				for(int i = 0 ; i < and.length ; i++){
					String[] words = and[i].trim().split(" ");
					if(!isAlreadyIn(attList, words[0].trim())){
						attList.add(words[0].trim());
					}
				}
			}
			ArrayList<String> tabList = new ArrayList<String>();
			checkForJoin(tables, tabList);
			if(!join){
				String[] listOfTables = tables.split(",");
				for(int i = 0 ; i < listOfTables.length ; i++){
					String[] temp = listOfTables[i].trim().split(";");
					tabList.add(temp[0].trim());
				}
			}
			join = false;
			attList = checkStar(attList, tabList);
			Query query = new Query(line, attList, tabList);
			queriesList.add(query);
		}
	}
	
	private void checkForJoin(String line,ArrayList<String> tabList){
		String[] words = line.split("JOIN");
		if(words.length > 1){
			tabList.add(line.split("\\s+")[0]);
			join = true;
			String[] wanted = words[1].trim().split("\\s+");
			tabList.add(wanted[0].trim());
		}
	}
	
	private boolean isAlreadyIn(ArrayList<String> attList, String word){
		for(int i = 0 ; i < attList.size() ; i++){
			if(attList.get(i).equals(word))
				return true;
		}
		return false;
	}
	
	private ArrayList<String> checkStar(ArrayList<String> attList, ArrayList<String> tabList){
		if(attList.get(0).equals("*")){
			String name = tabList.get(0);
			for(int i = 0 ; i < tablesList.size() ; i++){
				if(name.equals(tablesList.get(i).getName()))
					attList = tablesList.get(i).getAttributes();
			}
		}
		return attList;
	}
	
	private void assignQueriesToTables(){
		for(int i = 0 ; i < queriesList.size() ; i++){
			Query temp = queriesList.get(i);
			ArrayList<String> tablesHit = temp.getTablesHit();
			for(int j = 0 ; j < tablesHit.size() ; j++){
				String tableName = tablesHit.get(j);
				for(int k = 0 ; k < tablesList.size() ; k++){
					if(tableName.equals(tablesList.get(k).getName())){
						tablesList.get(k).addQuery(temp);
					}
				}
			}
		}
	}
	
	public void computeCoupling(boolean print, boolean normalized){
		ArrayList<Query> queriesWanted = new ArrayList<Query>();
		ArrayList<String> allTables = new ArrayList<String>();
		ArrayList<ArrayList<String>> tablesWanted = new ArrayList<ArrayList<String>>();
		for(int i = 0 ; i < tablesList.size() ; i++){
			allTables.add(tablesList.get(i).getName());
			couplingDegreeMap.put(tablesList.get(i).getName(), 0);
		}
		boolean showGUI = false;
		for(int i = 0 ; i < queriesList.size() ; i++){
			Query tempQuery = queriesList.get(i);
			if(tempQuery.getTablesHit().size() > 1){
				if(isQueryLegit(tempQuery.getTablesHit())){
					queriesWanted.add(tempQuery);
					showGUI = true;
					tablesWanted.add(tempQuery.getTablesHit());
				}
			}
		}
		if(print){
			Metrics m = new Metrics();
			if(normalized){
				couplingNormalizedDegreeMap = m.getCouplingNormalizedDegreeMap(allTables, tablesWanted, queriesWanted);
				//couplingNormalizedDegreeMap = tablesGraph.getCouplingNormalizedDegreeMap();
			}
			else{
				couplingDegreeMap = m.getCouplingDegreeMap(allTables, tablesWanted, queriesWanted);
			}
		}
		else{
			if(showGUI){
				GraphGUI tablesGraph = new GraphGUI(allTables, tablesWanted, queriesWanted);
				tablesGraph.setVisible(true);
				GraphGUI completeGraph = new GraphGUI(allTables);
				completeGraph.setVisible(true);
			}
			else{
				ErrorGUI gui = new ErrorGUI();
				gui.setVisible(true);
			}
		}
	}

	public void computeCohesion(Table t, boolean print){
		ArrayList<Query> queriesWanted = new ArrayList<Query>();
		ArrayList<String> allAttributes = t.getAttributes();
		ArrayList<ArrayList<String>> attributesWanted = new ArrayList<ArrayList<String>>();
		boolean showGUI = false;
		for(int i = 0 ; i < t.getQueries().size() ; i++){
			Query tempQuery = t.getQueries().get(i);
			if(tempQuery.getTablesHit().size() == 1 && tempQuery.getAttributesWanted().size() > 1){
				queriesWanted.add(tempQuery);
				showGUI = true;
				attributesWanted.add(tempQuery.getAttributesWanted());
			}
		}
		if(print){
			Metrics m = new Metrics();
			cohesionList.add(m.getCohesion(allAttributes, attributesWanted));
		}
		else{
			if(showGUI){
				GraphGUI attributesGraph = new GraphGUI(t.getName(), allAttributes, attributesWanted, queriesWanted);
				attributesGraph.setVisible(true);
				GraphGUI completeGraph = new GraphGUI(t.getName(), allAttributes);
				completeGraph.setVisible(true);
			}
			else{
				ErrorGUI gui = new ErrorGUI();
				gui.setVisible(true);
			}
		}
	}
	
	public ArrayList<Double> getCohesionList(){
		return cohesionList;
	}
	
	public boolean isAlreadyIn(String name){
		for(int i = 0 ; i < tablesList.size() ; i++){
			if(name.equals(tablesList.get(i).getName())){
				return true;
			}
		}
		return false;
	}
	
	public boolean isQueryLegit(ArrayList<String> namesList){
		for(int i = 0 ; i < namesList.size() ; i++){
			String name = namesList.get(i);
			if(!isAlreadyIn(name)){
				return false;
			}
		}
		return true;
	}
	
	public HashMap<String, Integer> getCouplingDegreeMap(){
		return couplingDegreeMap;
	}
	
	public HashMap<String, Double> getCouplingNormalizedDegreeMap(){
		return couplingNormalizedDegreeMap;
	}
	
	public ArrayList<String> getTablesNamesList(){
		ArrayList<String> names = new ArrayList<String>();
		for(Table t : tablesList){
			names.add(t.getName());
		}
		return names;
	}
}