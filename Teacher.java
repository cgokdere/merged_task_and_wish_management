package kidsTask2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


public class Teacher {

	
	private String name;
	private List<Child> children = new LinkedList<>();

	public Teacher(String name) {
		this.name=name;
	}
	
	Child child = new Child(0,1,2);
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	 public void addChild(Child child) {
	        children.add(child);
	       
	    }

	    
	    public Child getChildById(int childId) {
	        for (Child c : children) {
	            if (c.getChildId() == childId) {
	                return c;
	            }
	        }
	        return null;
	    }

	    public List<Child> getChildren() {
	        return children;
	    }
	    
	
	public boolean taskChecked(int taskId, int rating) {
		for (Child c : children) { 
	        if (c.taskDone(taskId)) {
			c.setCoin(c.getCoin() + rating );
			System.out.println("Okundu");
			return true;
	        }
		}

		return false;
	}
	
	public void addTask1(String assigner, int taskId, String title, String description, String endDate, 
			String endTime, int taskPoints) {
		Tasks newTask = new Tasks(assigner,taskId, title, description, endDate, endTime, taskPoints);
	    
    	ReadFile.Tasks.put(taskId, newTask);
		
	}
	
	public void addTask2(String assigner, int taskId, String title, String description, String startDate, 
			String startTime, String endDate,String endTime, int taskPoints) {
		Tasks newTask = new Tasks(assigner,taskId, title, description, startDate, startTime, endDate, endTime, taskPoints);
    	ReadFile.Tasks.put(taskId,newTask);
		
	}
	
	public void addBudgetCoin(int childId, int coins) {
		 Child c = getChildById(childId);
		    if (c != null) {
			int coin = c.getCoin();
			coin = coin + coins;
			c.setCoin(coin);
			System.out.println(coins + " coins added");
		}
		
	}
	
	
	public void addTaskFromFile() {
	    String filePath = "C:\\Users\\user\\Desktop\\KidsTask\\Tasks.txt";
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.trim().isEmpty()) continue;
	            
	          
	            String[] parts = line.split("\t", -1);
	            for (int i = 0; i < parts.length; i++) {
	                parts[i] = parts[i].trim();
	            }
	            
	            try {
	                String assigner = parts[0];
	                int taskId = Integer.parseInt(parts[1]);
	                String title = parts[2];
	                String description = parts[3];
	                int taskPoints = Integer.parseInt(parts[parts.length-1]);

	                if (parts.length == 7) {
	             
	                    addTask1(assigner, taskId, title, description, 
	                            parts[4], parts[5], 
	                            taskPoints);
	                } else if (parts.length == 9) {
	                   
	                    addTask2(assigner, taskId, title, description,
	                            parts[4], parts[5], // startDate, startTime
	                            parts[6], parts[7], // endDate, endTime
	                            taskPoints);
	                } else {
	                    System.err.println("Hatalı format (eksik/fazla alan): " + line);
	                }
	            } catch (NumberFormatException e) {
	                System.err.println("Geçersiz sayı formatı: " + line);
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Tasks.txt okunamadı: " + e.getMessage());
	    }
	}

	
	  
}
