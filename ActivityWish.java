import java.time.LocalDateTime;

public class ActivityWish extends Wish {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ActivityWish(String id, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, title, description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override 
    public boolean isActivityWish() { return true; }
    
    @Override 
    public LocalDateTime getStartTime() { return startTime; }
    
    @Override 
    public LocalDateTime getEndTime() { return endTime; }

    @Override
    public String toFileString() {
        return String.format("WISH2;%s;\"%s\";\"%s\";%s;%d;%s;%s",
                id, title, description, status, requiredLevel, 
                startTime != null ? startTime.toString() : "null",
                endTime != null ? endTime.toString() : "null");
    }
    
    @Override
    public String toString() {
        return "[ActivityWish] " + super.toString() + 
                " | Start: " + (startTime != null ? startTime : "N/A") + 
                " | End: " + (endTime != null ? endTime : "N/A");
    }
}
