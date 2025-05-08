


import java.time.LocalDateTime;

public class SimpleTask extends Task {

   
    public SimpleTask(String id, String assigner, String title, String description, LocalDateTime deadline, int points) {
        super(id, assigner, title, description, deadline, points); 
    }

    
    public SimpleTask(String id, String assigner, String title, String description, LocalDateTime deadline, int points, String status, int rating) {
        super(id, assigner, title, description, deadline, points, status, rating); 
    }

    

    @Override
    public boolean isActivityTask() {
        return false; 
    }

    @Override
    public LocalDateTime getStartTime() {
        return null; 
    }

    @Override
    public LocalDateTime getEndTime() {
        return null; 
    }

    @Override
    public String toFileString() {
       
        String assignerCode = UserConstants.TEACHER.equals(assigner) ? UserConstants.TEACHER_CODE : UserConstants.PARENT_CODE;

        
        String deadlineDateStr = Main.formatDate(deadline.toLocalDate());
        String deadlineTimeStr = Main.formatTime(deadline.toLocalTime());

       
        return String.format("TASK1;%s;%s;\"%s\";\"%s\";%s;%s;%d;%s;%d",
                assignerCode, id, title, description,
                deadlineDateStr, deadlineTimeStr, points, status, rating);
    }

  

    @Override
    public String toString() {
       
        return "[SimpleTask] " + super.toString();
    }
}
