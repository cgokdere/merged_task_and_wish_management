import java.util.ArrayList;
import java.util.List;

public class Child {
    private int childId;
    private int points;
    private int level;
    private List<Task> taskList;
    private List<Wish> wishList;
    private List<Integer> ratings;

    public Child() {
        this.childId = 0;
        this.points = 0;
        this.level = 1;
        this.taskList = new ArrayList<>();
        this.wishList = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public Child(int childId, int points, int level) {
        this.childId = childId;
        this.points = points;
        this.level = calculateLevel();
        this.taskList = new ArrayList<>();
        this.wishList = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    // Points and level management
    public void addPoints(int points) {
        if (points > 0) {
            this.points += points;
            updateLevel();
            System.out.println("\n=== POINTS & LEVEL ===");
            System.out.println("- Points added: " + points);
            System.out.println("- Total points: " + this.points);
        }
    }

    private void updateLevel() {
        int newLevel = calculateLevel();
        if (newLevel != level) {
            System.out.println("Level up! New level: " + newLevel);
            level = newLevel;
        }
    }

    private int calculateLevel() {
        if (points >= 80) return 4;
        if (points >= 60) return 3;
        if (points >= 40) return 2;
        return 1;
    }

    // Rating management
    public void addRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            ratings.add(rating);
            System.out.println("Rating " + rating + " added successfully.");
        } else {
            System.err.println("Invalid rating. Must be between 1 and 5.");
        }
    }
    
    // Status reporting
    public void printBudget() {
        System.out.println("\n=== BUDGET SUMMARY ===");
        System.out.println("Current Points: " + points);
    }

    public void printStatus() {
        System.out.println("\n=== CHILD STATUS ===");
        System.out.println("Child ID: " + childId);
        System.out.println("Level: " + level);
        System.out.println("Points: " + points);
    }

    // Getters and setters
    public int getChildId() { return childId; }
    public void setChildId(int childId) { this.childId = childId; }
    public int getPoints() { return points; }
    public void setPoints(int points) { 
        this.points = points; 
        updateLevel();
    }
    public int getLevel() { return level; }
    public List<Task> getTaskList() { return taskList; }
    public List<Wish> getWishList() { return wishList; }
}


