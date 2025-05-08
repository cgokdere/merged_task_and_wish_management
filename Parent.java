package kidsTask2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Parent extends User{
	
	private String name;
	private List<Child> children = new LinkedList<>();
	
	public Parent(String name) {
		this.name= name;
	}
	
	Child child = new Child(0,1,2);
	Wishes wish = new Wishes();
	
	
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
			System.out.println("Task " + taskId + " approved with "+ rating+ " stars.");
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
			System.out.println(coins + " coins added to child" + childId + "'s budget.");
		}
		
	}
	
    public void ListAllWishes() {
    	
    	for (Wishes wish : ReadFile.Wishes.values()) {
			System.out.println(wish);
		}
		
	}
    
   
    
    public void ListAllWishesD() {
        LocalDate today = LocalDate.now();
        
        for (Wishes wish : ReadFile.Wishes.values()) {
            String dateStr = wish.getStartDate();
            if (dateStr == null || dateStr.isEmpty()) {
                continue; // Skip wishes without dates
            }

            try {
              
                String[] dateParts = dateStr.split("-");
                if (dateParts.length != 3) continue;
                
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                
                LocalDate wishDate = LocalDate.of(year, month, day);
                
                if (wishDate.equals(today)) {
                    System.out.println(wish);
                }
            } catch (NumberFormatException | DateTimeException e) {
                System.err.println("Invalid date format in wish ID " + wish.getWishId() + ": " + dateStr);
            }
        }
    }
    
	
    
    public void ListAllWishesW() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        
        for (Wishes wish : ReadFile.Wishes.values()) {
            String dateStr = wish.getStartDate();
            if (dateStr == null || dateStr.isEmpty()) {
                continue; 
            }

            try {
                
                String[] dateParts = dateStr.split("-");
                if (dateParts.length != 3) continue;
                
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                
                LocalDate wishDate = LocalDate.of(year, month, day);
                
               
                if (!wishDate.isBefore(today) && !wishDate.isAfter(nextWeek)) {
                    System.out.println(wish + " (Date: " + wishDate + ")");
                }
            } catch (NumberFormatException | DateTimeException e) {
                System.err.println("Invalid date format in wish ID " + wish.getWishId() + ": " + dateStr);
            }
        }
    }
    
    
	
	public void wishChecked(int wishId, String approvalStatus, int level) {
	    for (Wishes wishes : ReadFile.Wishes.values()) {
	        if (wishId == wishes.getWishId()) {  
	            // Approve the wish
	            if (approvalStatus.equals("APPROVED")) {
	                wishes.setApprovalStatus("APPROVED");
	                System.out.println("Wish " + wishId+ " APPROVED");
	                if (level != -1) { 
	                	for (Child c : children) { 
	                    if (c.getlevel() < level) {  
	                    	System.out.println(" the child must be at least level" + level + "to fulfill this wish");
	                     
	                    }
	                }
	            }
	         }

	           
	            if (approvalStatus.equals("REJECTED")) {
	                wishes.setApprovalStatus("REJECTED");  
	                ReadFile.Wishes.remove(wishId);  
	                System.out.println("Wish " + wishId + " has been rejected and removed.");
	            }
	        }
	    }
	}

	
	
	public void addTask1FromFile() {
	    String filePath = "C:\\Users\\user\\Desktop\\KidsTask\\Tasks.txt";
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.trim().isEmpty()) continue;
	            
	            String[] parts = line.split("\t", -1);
	            for (int i = 0; i < parts.length; i++) {
	                parts[i] = parts[i].trim();
	            }
	            
	           
	            if (parts.length != 7) continue;
	            
	            try {
	                String assigner = parts[0];
	                int taskId = Integer.parseInt(parts[1]);
	                String title = parts[2];
	                String description = parts[3];
	                String endDate = parts[4];
	                String endTime = parts[5];
	                int taskPoints = Integer.parseInt(parts[6]);

	                addTask1(assigner, taskId, title, description, endDate, endTime, taskPoints);
	            } catch (NumberFormatException e) {
	                System.err.println("Geçersiz sayı formatı: " + line);
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Tasks.txt okunamadı: " + e.getMessage());
	    }
	    System.out.println("Task1 is read.");
	}
	
	
	public void addTask2FromFile() {
	    String filePath = "C:\\Users\\user\\Desktop\\KidsTask\\Tasks.txt";
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.trim().isEmpty()) continue;
	            
	            String[] parts = line.split("\t", -1);
	            for (int i = 0; i < parts.length; i++) {
	                parts[i] = parts[i].trim();
	            }
	            
	           
	            if (parts.length != 9) continue;
	            
	            try {
	                String assigner = parts[0];
	                int taskId = Integer.parseInt(parts[1]);
	                String title = parts[2];
	                String description = parts[3];
	                String startDate = parts[4];
	                String startTime = parts[5];
	                String endDate = parts[6];
	                String endTime = parts[7];
	                int taskPoints = Integer.parseInt(parts[8]);

	                addTask2(assigner, taskId, title, description, 
	                        startDate, startTime, endDate, endTime, taskPoints);
	            } catch (NumberFormatException e) {
	                System.err.println("Geçersiz sayı formatı: " + line);
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Tasks.txt okunamadı: " + e.getMessage());
	    }
	    System.out.println("Task2 is read.");
	}
	  
}
