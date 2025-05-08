

import java.util.ArrayList;
import java.util.List;

public class Child {
    private int totalPoints;
    private List<Integer> ratings; 

    public Child() {
        this.totalPoints = 0;
        this.ratings = new ArrayList<>();
    }

    public void addPoints(int amount) {
        if (amount != 0) { 
             this.totalPoints += amount;
             System.out.println("Debug: Added " + amount + " points. New total: " + this.totalPoints);
        }
        
    }

    public void addRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.ratings.add(rating);
        } else {
            System.err.println("Warn: Invalid rating " + rating + " ignored.");
        }
    }

   
    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalPoints() { return totalPoints; }

    public double getAverageRating() {
        if (ratings.isEmpty()) return 0.0;
       
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
       
    }

   
    public int getLevel() {
        if (totalPoints >= 80) return 4;
        if (totalPoints >= 60) return 3;
        if (totalPoints >= 40) return 2;
        return 1; 
    }

    
    public void clearRatings() {
        this.ratings.clear();
    }


    public String getStatusString() {
        return String.format("Level: %d (Based on %d Points) | Avg Rating: %.2f (from %d ratings)",
                getLevel(), getTotalPoints(), getAverageRating(), ratings.size());
    }
}