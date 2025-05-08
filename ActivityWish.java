


import java.time.LocalDateTime;

public class ActivityWish extends Wish {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

   
    public ActivityWish(String id, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, title, description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

   
    public ActivityWish(String id, String title, String description, String status, int requiredLevel, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, title, description, status, requiredLevel);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override public boolean isActivityWish() { return true; }
    @Override public LocalDateTime getStartTime() { return startTime; }
    @Override public LocalDateTime getEndTime() { return endTime; }

    @Override
    public String toFileString() {
       
        String startDateStr = Main.formatDate(startTime.toLocalDate());
        String startTimeStr = Main.formatTime(startTime.toLocalTime());
        String endDateStr = Main.formatDate(endTime.toLocalDate());
        String endTimeStr = Main.formatTime(endTime.toLocalTime());

        return String.format("WISH2;%s;\"%s\";\"%s\";%s;%d;%s;%s;%s;%s",
                id, title, description, status, requiredLevel,
                startDateStr, startTimeStr, endDateStr, endTimeStr);
    }

     @Override
    public String toString() {
        return String.format("[ActivityWish] %s, Start: %s, End: %s",
                super.toString(),
                Main.formatDateTime(startTime),
                Main.formatDateTime(endTime));
    }
}
