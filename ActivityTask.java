
import java.time.LocalDateTime;
public class ActivityTask extends Task {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    
    public ActivityTask(String id, String assigner, String title, String description, LocalDateTime startTime, LocalDateTime endTime, int points) {
        super(id, assigner, title, description, endTime, points); 
        this.startTime = startTime;
        this.endTime = endTime;
    }

    
    public ActivityTask(String id, String assigner, String title, String description, LocalDateTime deadline, int points, String status, int rating, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, assigner, title, description, deadline, points, status, rating); 
        this.startTime = startTime;
        this.endTime = endTime;
    }

    

    @Override
    public boolean isActivityTask() {
        return true; 
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.startTime; 
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime; 
    }

    @Override
    public String toFileString() {
       
        String assignerCode = UserConstants.TEACHER.equals(assigner) ? UserConstants.TEACHER_CODE : UserConstants.PARENT_CODE;

       
        
        
        String deadlineDateStr = Main.formatDate(deadline.toLocalDate());
        String deadlineTimeStr = Main.formatTime(deadline.toLocalTime());
        String startDateStr = Main.formatDate(startTime.toLocalDate());
        String startTimeStr = Main.formatTime(startTime.toLocalTime());
        String endDateStr = Main.formatDate(endTime.toLocalDate());
        String endTimeStr = Main.formatTime(endTime.toLocalTime());

       
        
        return String.format("TASK2;%s;%s;\"%s\";\"%s\";%s;%s;%d;%s;%d;%s;%s;%s;%s",
                assignerCode, id, title, description,
                deadlineDateStr, deadlineTimeStr, points, status, rating,
                startDateStr, startTimeStr, endDateStr, endTimeStr);
    }

    

    @Override
    public String toString() {
        
        return String.format("[ActivityTask] %s, Start: %s, End: %s",
                super.toString(),
                Main.formatDateTime(startTime),
                Main.formatDateTime(endTime));
    }
}