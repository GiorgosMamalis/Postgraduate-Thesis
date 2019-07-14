package thesis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Metrics {
	public double getCohesion(ArrayList<String> allAttributes, ArrayList<ArrayList<String>> attributesWanted){
		int lines = 0;
		for(int i = 0 ; i < attributesWanted.size() ; i++){
			ArrayList<String> tempList = attributesWanted.get(i);
			lines += tempList.size()*(tempList.size() - 1) / 2;
		}
		int completeLines = allAttributes.size() * (allAttributes.size() - 1) / 2;
		double cohesion = (double)lines/completeLines;
		if(cohesion <= 1.0)
			return cohesion;
		else
			return 1.0;
	}
	
    public HashMap<String, Integer> getCouplingDegreeMap(ArrayList<String> allTables, ArrayList<ArrayList<String>> tablesWanted, ArrayList<Query> queriesWanted){
    	HashMap<String, Integer> couplingDegreeMap = new HashMap<String, Integer>();
		for(int i = 0 ; i < allTables.size() ; i++){
			couplingDegreeMap.put(allTables.get(i), 0);
		}
		for(int k = 0 ; k < tablesWanted.size() ; k++){
			ArrayList<String> tempList = tablesWanted.get(k);
			for(int i = 0 ; i < tempList.size() ; i++){
				if(couplingDegreeMap.containsKey(tempList.get(i))){
					int value = couplingDegreeMap.get(tempList.get(i));
					value++;
					couplingDegreeMap.put(tempList.get(i), value);
				}
			}	
		}
    	return couplingDegreeMap;
    }
	
    public HashMap<String, Double> getCouplingNormalizedDegreeMap(ArrayList<String> allTables, ArrayList<ArrayList<String>> tablesWanted, ArrayList<Query> queriesWanted){
    	HashMap<String, Integer> couplingDegreeMap = new HashMap<String, Integer>();
    	int completeLines = allTables.size() - 1;
		for(int i = 0 ; i < allTables.size() ; i++){
			couplingDegreeMap.put(allTables.get(i), 0);
		}
		for(int k = 0 ; k < tablesWanted.size() ; k++){
			ArrayList<String> tempList = tablesWanted.get(k);
			for(int i = 0 ; i < tempList.size() ; i++){
				if(couplingDegreeMap.containsKey(tempList.get(i))){
					int value = couplingDegreeMap.get(tempList.get(i));
					value++;
					couplingDegreeMap.put(tempList.get(i), value);
				}
			}	
		}
    	HashMap<String, Double> couplingNormalizedDegreeMap = new HashMap<String, Double>();
    	for(String key : couplingDegreeMap.keySet()){
    		if(couplingDegreeMap.get(key) == 0){
    			couplingNormalizedDegreeMap.put(key, 0.0);
    		}
    		else{
    			int value = couplingDegreeMap.get(key);
    			if((double)value / completeLines > 1.0)
    				couplingNormalizedDegreeMap.put(key, 1.0);
    			else
    				couplingNormalizedDegreeMap.put(key, (double)value / completeLines);
    		}
    	}
    	return couplingNormalizedDegreeMap;
    }
    
	public void printCohesionStats(ArrayList<Double> statsList, ArrayList<Table> tablesList){
		try (PrintWriter writer = new PrintWriter(new File("Cohesion Metric by Table.csv"))) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0 ; i < statsList.size() ; i++){
				sb.append(tablesList.get(i).getName() + ';' + statsList.get(i) + '\n');
			}
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		HashMap<Double, Integer> frequencyMap = new HashMap<Double, Integer>();
		double max = 0.0;
		double min = 1.0;
		double sum = 0.0;
		Collections.sort(statsList);
		for(int i = 0 ; i < statsList.size() ; i++){
			if(frequencyMap.containsKey(statsList.get(i))){
				int temp = frequencyMap.get(statsList.get(i));
				temp++;
				frequencyMap.put(statsList.get(i), temp);
			}
			else{
				frequencyMap.put(statsList.get(i), 1);
			}
			sum += statsList.get(i);
			if(statsList.get(i) > max)
				max = statsList.get(i);
			if(statsList.get(i) < min)
				min = statsList.get(i);
		}
		double mean = sum / statsList.size();
		sum = 0.0;
		for(int i = 0; i < statsList.size(); i++){
			   sum += Math.pow((statsList.get(i) - mean), 2);
		}
		int maxValue = 0;
		double mode = -1.0;
		for(Double key: frequencyMap.keySet()){
			if(frequencyMap.get(key) > maxValue){
				maxValue = frequencyMap.get(key);
				mode = key;
			}
		}
		try (PrintWriter writer = new PrintWriter(new File("Cohesion Metric Stats.csv"))) {
			StringBuilder sb = new StringBuilder();
			sb.append("Max" + ';' + max + '\n');
			sb.append("Min" + ';' + min + '\n');
			sb.append("Mean" + ';' + mean + '\n');
			sb.append("Median" + ';' + statsList.get(statsList.size() / 2) + '\n');
			sb.append("Standard Deviation" + ';' + sum + '\n');
			sb.append("Mode" + ';' + mode + '\n');
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void printCouplingStats(HashMap<String, Integer> couplingMap){
    	ArrayList<Integer> statsList = new ArrayList<Integer>();
    	for(String key: couplingMap.keySet()){
    		statsList.add(couplingMap.get(key));
		}
		HashMap<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
		int max = 0;
		int min = statsList.size();
		double sum = 0.0;
		try (PrintWriter writer = new PrintWriter(new File("Coupling Metric by Table.csv"))) {
			StringBuilder sb = new StringBuilder();
			for(String key: couplingMap.keySet()){
				sb.append(key + ';' + couplingMap.get(key) + '\n');
			}
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		Collections.sort(statsList);
		for(int i = 0 ; i < statsList.size() ; i++){
			if(frequencyMap.containsKey(statsList.get(i))){
				int temp = frequencyMap.get(statsList.get(i));
				temp++;
				frequencyMap.put(statsList.get(i), temp);
			}
			else{
				frequencyMap.put(statsList.get(i), 1);
			}
			sum += (double)statsList.get(i);
			if(statsList.get(i) > max)
				max = statsList.get(i);
			if(statsList.get(i) < min)
				min = statsList.get(i);
		}
		double mean = sum / statsList.size();
		sum = 0.0;
		for(int i = 0; i < statsList.size(); i++){
			   sum += Math.pow((statsList.get(i) - mean), 2);
		}
		int maxValue = 0;
		int mode = -1;
		for(Integer key: frequencyMap.keySet()){
			if(frequencyMap.get(key) > maxValue){
				maxValue = frequencyMap.get(key);
				mode = key;
			}
		}
		try (PrintWriter writer = new PrintWriter(new File("Coupling Metric Stats.csv"))) {
			StringBuilder sb = new StringBuilder();
			sb.append("Max" + ';' + max + '\n');
			sb.append("Min" + ';' + min + '\n');
			sb.append("Mean" + ';' + mean + '\n');
			sb.append("Median" + ';' + statsList.get(statsList.size() / 2) + '\n');
			sb.append("Standard Deviation" + ';' + sum + '\n');
			sb.append("Mode" + ';' + mode + '\n');
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void printCouplingNormalizedStats(HashMap<String, Double> normalizedCouplingMap){
    	ArrayList<Double> statsList = new ArrayList<Double>();
    	for(String key: normalizedCouplingMap.keySet()){
    		statsList.add(normalizedCouplingMap.get(key));
		}
    	try (PrintWriter writer = new PrintWriter(new File("Normalized Coupling Metric by Table.csv"))) {
			StringBuilder sb = new StringBuilder();
			for(String key: normalizedCouplingMap.keySet()){
				sb.append(key + ';' + normalizedCouplingMap.get(key) + '\n');
			}
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		HashMap<Double, Integer> frequencyMap = new HashMap<Double, Integer>();
		double max = 0.0;
		double min = (double)statsList.size();
		double sum = 0.0;
		Collections.sort(statsList);
		for(int i = 0 ; i < statsList.size() ; i++){
			if(frequencyMap.containsKey(statsList.get(i))){
				int temp = frequencyMap.get(statsList.get(i));
				temp++;
				frequencyMap.put(statsList.get(i), temp);
			}
			else{
				frequencyMap.put(statsList.get(i), 1);
			}
			sum += (double)statsList.get(i);
			if(statsList.get(i) > max)
				max = statsList.get(i);
			if(statsList.get(i) < min)
				min = statsList.get(i);
		}
		
		double mean = sum / statsList.size();
		sum = 0.0;
		for(int i = 0; i < statsList.size(); i++){
			   sum += Math.pow((statsList.get(i) - mean), 2);
		}
		int maxValue = 0;
		double mode = -1.0;
		for(Double key: frequencyMap.keySet()){
			if(frequencyMap.get(key) > maxValue){
				maxValue = frequencyMap.get(key);
				mode = key;
			}
		}
		try (PrintWriter writer = new PrintWriter(new File("Normalized Coupling Metric Stats.csv"))) {
			StringBuilder sb = new StringBuilder();
			sb.append("Max" + ';' + max + '\n');
			sb.append("Min" + ';' + min + '\n');
			sb.append("Mean" + ';' + mean + '\n');
			sb.append("Median" + ';' + statsList.get(statsList.size() / 2) + '\n');
			sb.append("Standard Deviation" + ';' + sum + '\n');
			sb.append("Mode" + ';' + mode + '\n');
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
