

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Task {
    protected String id;
    protected String assigner; 
    protected String title;
    protected String description;
    protected LocalDateTime deadline;
    protected int points;
    protected String status;
    protected int rating; 

  
    public Task(String id, String assigner, String title, String description, LocalDateTime deadline, int points) {
        this.id = id;
        if (!UserConstants.TEACHER.equals(assigner) && !UserConstants.PARENT.equals(assigner)) {
           
             System.err.println("Warning: Invalid assigner type '" + assigner + "' for task ID " + id + ". Defaulting to PARENT.");
             this.assigner = UserConstants.PARENT;
            
        } else {
             this.assigner = assigner;
        }
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.points = points;
        this.status = StatusConstants.TASK_PENDING; 
        this.rating = 0; 
    }

    
    public Task(String id, String assigner, String title, String description, LocalDateTime deadline, int points, String status, int rating) {
        this.id = id;
        this.assigner = assigner;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.points = points;
        this.status = status;
        this.rating = rating;
    }


    public String getId() { return id; }
    public String getAssigner() { return assigner; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDeadline() { return deadline; }
    public int getPoints() { return points; }
    public String getStatus() { return status; }
    public int getRating() { return rating; }

   
    public void setStatus(String status) {
        if (StatusConstants.TASK_PENDING.equals(status) ||
            StatusConstants.TASK_COMPLETED_BY_CHILD.equals(status) ||
            StatusConstants.TASK_APPROVED.equals(status)) {
            this.status = status;
        } else {
            System.err.println("Warning: Attempted to set invalid task status: '" + status + "' for task ID: " + this.id);
        }
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
             
             if (rating != 0) {
                 System.err.println("Warning: Invalid rating (" + rating + ") provided for task ID: " + this.id + ". Rating must be 1-5.");
             }
        }
    }


    public abstract boolean isActivityTask();
    public abstract LocalDateTime getStartTime();
    public abstract LocalDateTime getEndTime();
    public abstract String toFileString();

    @Override
    public String toString() {
        String assignerCode = UserConstants.TEACHER.equals(assigner) ? UserConstants.TEACHER_CODE : UserConstants.PARENT_CODE;
       
        return String.format("ID: %-5s | Title: %-25s | Assign: %s | Deadline: %-16s | Pts: %-3d | Status: %-18s | Rate: %s",
                id, "\"" + title + "\"", assignerCode,
                Main.formatDateTime(deadline), 
                points, status, rating == 0 ? "-" : String.valueOf(rating));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}