package kidsTask2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Child extends User {

	private int coin;
	private int childId;
	private int level;
	private int calculateLevel() {
        if (this.coin <= 0) return 0;
        if (this.coin <= 40) return 1;
        if (this.coin <= 60) return 2;
        if (this.coin <= 80) return 3;
        return 4;
    }


	public Child(int coin, int childId, int level) {

		this.coin = coin;
		this.childId = childId;
		this.level = level;

	}
	
	public Child() {
        this.coin = 0;  // Varsayılan değer
        this.childId = 0;  // Varsayılan değer
        this.level = 1;  // Varsayılan değer
    }
	
	Tasks task = new Tasks();
	


	public int getChildId() {
		return childId;
	}

	public void setChildId(int childId) {
		this.childId = childId;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
		this.level = calculateLevel();

	}
	
	public int getlevel() {
		return level;
	}

	public void setlevel(int level) {
		this.level = level;
	}
	
	
	public void ListAllTasks() {
		for (Tasks task : ReadFile.Tasks.values()) {
			System.out.println(task);
		}
		System.out.println();
		
	}
	
	public void ListAllWishes() {
		for (Wishes wish : ReadFile.Wishes.values()) {
			System.out.println(wish);
		}
	}
	
	
	
	public void ListAllWishesD() {
	    LocalDate today = LocalDate.now();
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Dosyadaki formata göre ayarla
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

	    System.out.println("----- Bugünkü Dilekler (" + today.format(dateFormatter) + ") -----");
	    boolean foundAny = false;

	    for (Wishes wish : ReadFile.Wishes.values()) {
	        try {
	            boolean isToday = false;

	            if (wish.getStartDate() != null && !wish.getStartDate().isEmpty()) {
	                LocalDate startDate = LocalDate.parse(wish.getStartDate(), dateFormatter);
	                if (startDate.equals(today)) isToday = true;
	            }

	            if (wish.getEndDate() != null && !wish.getEndDate().isEmpty()) {
	                LocalDate endDate = LocalDate.parse(wish.getEndDate(), dateFormatter);
	                if (endDate.equals(today)) isToday = true;
	            }

	            if (isToday) {
	                System.out.println(
	                    "ID: " + wish.getWishId() + 
	                    " | Başlık: " + wish.getWishTitle() + 
	                    " | Başlangıç: " + (wish.getStartDate() != null ? wish.getStartDate() + " " + wish.getStartTime() : "Belirtilmemiş") +
	                    " | Bitiş: " + (wish.getEndDate() != null ? wish.getEndDate() + " " + wish.getEndTime() : "Belirtilmemiş")
	                );
	               
	                foundAny = true;
	            }

	        } catch (DateTimeParseException e) {
	            System.err.println("Tarih/Saat formatı hatalı (ID: " + wish.getWishId() + "): " + e.getMessage());
	        }
	    }

	    if (!foundAny) {
	        System.out.println("Bugüne ait dilek bulunamadı.");
	    }
	    
	    System.out.println();
	}

    
	public void ListAllWishesW() {
	    LocalDate today = LocalDate.now();
	    LocalDate nextWeek = today.plusDays(7);

	    DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	    System.out.println("----- Bu Haftaki Dilekler (" +
	            today.format(displayFormatter) + " - " +
	            nextWeek.format(displayFormatter) + ") -----");

	    boolean foundAny = false;

	    for (Wishes wish : ReadFile.Wishes.values()) {
	        try {
	            boolean printed = false;

	          
	            if (wish.getStartDate() != null && !wish.getStartDate().isEmpty()) {
	                LocalDate startDate = LocalDate.parse(wish.getStartDate(), parseFormatter);
	                if (!startDate.isBefore(today) && !startDate.isAfter(nextWeek)) {
	                    System.out.println(
	                        "ID: " + wish.getWishId() +
	                        " | Başlık: " + wish.getWishTitle() +
	                        " | Tarih: " + startDate.format(displayFormatter) +
	                        " | Saat: " + (wish.getStartTime() != null ? wish.getStartTime() : "Belirtilmemiş") +
	                        " | Tip: Başlangıç"
	                    );
	                  
	                    foundAny = true;
	                    printed = true;
	                }
	            }

	           
	            if (!printed && wish.getEndDate() != null && !wish.getEndDate().isEmpty()) {
	                LocalDate endDate = LocalDate.parse(wish.getEndDate(), parseFormatter);
	                if (!endDate.isBefore(today) && !endDate.isAfter(nextWeek)) {
	                    System.out.println(
	                        "ID: " + wish.getWishId() +
	                        " | Başlık: " + wish.getWishTitle() +
	                        " | Tarih: " + endDate.format(displayFormatter) +
	                        " | Saat: " + (wish.getEndTime() != null ? wish.getEndTime() : "Belirtilmemiş") +
	                        " | Tip: Bitiş"
	                    );
	                    foundAny = true;
	                   
	                }
	            }

	        } catch (DateTimeParseException e) {
	            System.err.println("Hatalı tarih/saat formatı (ID: " + wish.getWishId() + "): " + e.getMessage());
	        }
	    }

	    if (!foundAny) {
	        System.out.println("Bu hafta için dilek bulunamadı.");
	    }
	    
	    System.out.println();
	}




	
	
    public boolean taskDone(int taskId) {
    	
    	for(Tasks task: ReadFile.Tasks.values()) {
    		if(taskId == task.getTaskId()) {
    		   task.setTaskStatus(true);
    		   System.out.println("Task " + taskId + " marked as done. Waiting for approval");
    		   return true;
    		}
    	}
    	System.out.println("Task ID " + taskId + " not found.");
    	return false;
    }
	
    public void addWish1(int wishId, String title, String description) {
        Wishes newWish = new Wishes(wishId, title, description);
        ReadFile.Wishes.put(wishId, newWish);
    }
    
    public void addWish2(int wishId, String title, String description, String startDate, String startTime, String endDate, String endTime) {
    	//LinkedList<LinkedList<Object>> wishes = new LinkedList<LinkedList<Object>>();
    	 Wishes newWish = new Wishes(wishId, title, description, startDate, startTime ,endDate,endTime);
         ReadFile.Wishes.put(wishId, newWish);
    }
    
    public void addWish1FromFile() {
        String filePath = "C:\\Users\\user\\Desktop\\KidsTask\\Wishes.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
               
                String[] parts = line.split("\t", -1);
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }
                
           
                if (parts.length != 3) continue;
                
                try {
                    int wishId = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String description = parts[2];
                    
                    addWish1(wishId, title, description);
                } catch (NumberFormatException e) {
                    System.err.println("Geçersiz ID formatı: " + line);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Eksik alan: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Wishes.txt okunamadı: " + e.getMessage());
        }
        System.out.println("Wish1 is read.");
    }
    
    public void addWish2FromFile() {
        String filePath = "C:\\Users\\user\\Desktop\\KidsTask\\Wishes.txt";
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
                    int wishId = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String description = parts[2];
                    String startDate = parts[3];
                    String startTime = parts[4];
                    String endDate = parts[5];
                    String endTime = parts[6];
                    
                    addWish2(wishId, title, description, startDate, startTime, endDate, endTime);
                } catch (NumberFormatException e) {
                    System.err.println("Geçersiz ID formatı: " + line);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Eksik alan: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Wishes.txt okunamadı: " + e.getMessage());
        }
        System.out.println("Wish2 is read.");
    }
    
   
   
    
}

