package seng272;

import java.util.List;
import java.util.ArrayList;

public class Child {
	private int level;
	private int points;
	private List<Task> taskList;
	private List<Wish> wishList;

	public Child() {
		this.level = 1;
		this.points = 0;
		this.taskList = new ArrayList<>();
		this.wishList = new ArrayList<>();
	}
	public void addPoints(int points) {
	    if (points > 0) {
	        this.points += points;
	        updateLevel();
	        System.out.println("\n=== POINTS&LEVEL ===");
	        System.out.println("- Points added: " + points);
        System.out.println("- Total points: " + this.points);
	    }
	}

	private void updateLevel() {
	    int newLevel;
	    if (points >= 80) {
	        newLevel = 4;
	    } else if (points >= 60) {
	        newLevel = 3;
	    } else if (points >= 40) {
	        newLevel = 2;
	    } else {
	        newLevel = 1;
	    }
	    
	    if (newLevel != level) {
	        System.out.println("Level up! New level: " + newLevel);
	        level = newLevel;
	    }
	}

	
	public void printBudget() {
		 System.out.println("\n=== BUDGET SUMMARY ===");
		System.out.println("Current Points: " + points);
	}

	public void printStatus() {
		System.out.println("\n=== CHILD STATUS ===");
		System.out.println("Current Status:");
		System.out.println("Level: " + level);
		System.out.println("Points: " + points);
	}


	public List<Task> getTaskList() {
		return taskList;
	}

	public List<Wish> getWishList() {
		return wishList;
	}
	public int getLevel() {
        return level;
    }

    public int getPoints() {
        return points;
    }
}