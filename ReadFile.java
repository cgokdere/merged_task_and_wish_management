package kidsTask2;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.io.*;

public class ReadFile {
	
	public static Map<Integer, Tasks> Tasks = new HashMap<>();
	public static LinkedList<String> Commands = new LinkedList<>();
	public static Map<Integer, Wishes> Wishes = new HashMap<>();
	
	
	
	 public static void main(String[] args) {
		 Parent parent = new Parent("");
		 //Child child = new Child();
		 User user = new User();
		 Child child = new Child(30,0,2);
		 Child child1 = new Child(1, 0, 5); 
		 Child child2 = new Child(2, 1, 3);
		 
		 parent.addChild(child);
		 //parent.addChild(child1);
		// parent.addChild(child2);
		 
		
		 
	        String commandsFilePath = "C:\\Users\\user\\Desktop\\KidsTask\\Commands.txt";
	    
	          try (BufferedReader reader = new BufferedReader(new FileReader(commandsFilePath))) {
	            String command;
	            while ((command = reader.readLine()) != null) {
	            	 System.out.println("Command: " + command); 
	            	
	            	switch(command) {
	            	
	            	case "ADD_TASK1": 
	            		parent.addTask1FromFile();
	            		break;
	            		
	            	case "ADD_TASK2":
	            		parent.addTask2FromFile();
	            		break;
	            		
	            	case "ADD_WISH1":
	            		for (Child c : parent.getChildren()) {
	            		    c.addWish1FromFile(); 
	            		    }
	            		break;
	            		
	            	case "ADD_WISH2":
	            		for (Child c : parent.getChildren()) {
	            		    c.addWish2FromFile(); 
	            		    }
	            		break;
	            		
	            	case "LIST_ALL_TASKS": 
	            	    for (Child c : parent.getChildren()) {
	            	        c.ListAllTasks();
	            	    }
	            	    break;
	            	    
	            	case "LIST_ALL_WISHES D":
	            	    for (Child c : parent.getChildren()) {
	            	        c.ListAllWishesD();
	            	    }
	            	    break;

	            	case "LIST_ALL_WISHES W":
	            	    for (Child c : parent.getChildren()) {
	            	        c.ListAllWishesW();
	            	    }
	            	    break;

	            	case "LIST_ALL_WISHES":
	            	    for (Child c : parent.getChildren()) {
	            	        c.ListAllWishes();
	            	    }
	            	    break;

	            	case "TASK_DONE": 
	            	    for (Child c : parent.getChildren()) {
	            	        c.taskDone(0); 
	            	    }
	            	    break;

	            		
	            	case "TASK_CHECKED":
	            		parent.taskChecked(0, 4);
	            		break;
	            		
	            	case "ADD_BUDGET_COIN":
	            		parent.addBudgetCoin(0, 30);
	            		break;
	            		
	            	case "WISH_CHECKED" : 
	            		parent.wishChecked(0, "APPROVED", 1);
	            		break;
	            		
	            	case "PRINT_BUDGET" :
	            		user.printBudget(0,parent.getChildren() );
	            		break;
	            		
	            	case "PRINT_STATUS" :
	            		user.printStatus(0, parent.getChildren());
	            		
	            	
	            	}
	                Commands.add(command);
	            }
	        } catch (IOException e) {
	            System.out.println("Commands.txt dosyası okunamadı: " + e.getMessage());
	        }
	        
	   
	        
	    
	}
	
   
}
