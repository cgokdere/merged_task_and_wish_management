import java.time.LocalDateTime;

public class ProductWish extends Wish {
   
    public ProductWish(String id, String title, String description) {
        super(id, title, description);
    }

    @Override 
    public boolean isActivityWish() { return false; }
    
    @Override 
    public LocalDateTime getStartTime() { return null; }
    
    @Override 
    public LocalDateTime getEndTime() { return null; }

    @Override
    public String toFileString() {
        return String.format("WISH1;%s;\"%s\";\"%s\";%s;%d",
                id, title, description, status, requiredLevel);
    }

    @Override 
    public String toString() { 
        return "[ProductWish] " + super.toString(); 
    }
}