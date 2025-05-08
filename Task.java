import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    private String assignedBy; // T for teacher, F for parent
    private final int taskId;
    private String title;
    private String description;
    private LocalDateTime deadlineDate; // for task1
    private LocalDateTime endDate; // task2
    private LocalDateTime startDate; // for task2
    private int points;
    private boolean isCompleted;
    private boolean isApproved;
    private boolean taskStatus;
    private int rating;

    // constructor for task1(deadline)
    public Task(String assignedBy, int taskId, String title, String description, LocalDateTime deadlineDate,
            int points) {
        this.assignedBy = assignedBy;
        this.taskId = taskId;
        this.title = title;
        this.description = description != null ? description : "";
        this.deadlineDate = deadlineDate;
        this.points = points;
        this.isCompleted = false;
        this.isApproved = false;
        this.taskStatus = false;
        this.rating = 0;
    }

    // constructor for task2(activity time)
    public Task(String assignedBy, int taskId, String title, String description, LocalDateTime startDate,
            LocalDateTime endDate, int points) {
        this.assignedBy = assignedBy;
        this.taskId = taskId;
        this.title = title;
        this.description = description != null ? description : "";
        this.startDate = startDate;
        this.endDate = endDate;
        this.points = points;
        this.isCompleted = false;
        this.isApproved = false;
        this.taskStatus = false;
        this.rating = 0;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getFilterDate() {
        if (deadlineDate != null) {
            return deadlineDate.toLocalDate(); // For Task1 (deadline tasks)
        } else {
            return startDate.toLocalDate(); // For Task2 (activity tasks)
        }
    }

    public boolean isDueToday() {
        LocalDate today = LocalDate.now();

        if (deadlineDate != null) {
            return deadlineDate.toLocalDate().equals(today);
        } else {
            return !today.isBefore(startDate.toLocalDate()) && !today.isAfter(endDate.toLocalDate());
        }
    }

    public boolean isDueThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusWeeks(1);

        if (deadlineDate != null) {
            LocalDate deadline = deadlineDate.toLocalDate();
            return !deadline.isBefore(today) && deadline.isBefore(endOfWeek);
        } else {
            LocalDate activityStart = startDate.toLocalDate();
            LocalDate activityEnd = endDate.toLocalDate();
            return activityStart.isBefore(endOfWeek) && activityEnd.isAfter(today);
        }
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
    
    public boolean getTaskStatus() {
        return taskStatus;
    }

    public int getPoints() {
        return points;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            System.out.println("Rating must be between 1 and 5");
        }
    }

    public String getTaskDetails() {
        String idStr = String.format("ID:%-4d", taskId);
        String titleStr = String.format("Title:%-25s", title);
        String assignedStr = String.format("By:%-2s", assignedBy);
        String descStr = String.format("Desc:%-30s", description); 
        String pointsStr = String.format("Pts:%-3d", points);
        String completedStr = String.format("Done:%-5b", isCompleted);
        String approvedStr = String.format("Approved:%-5b", isApproved);
        String ratingStr = String.format("Rate:%-2d", rating);

        if (deadlineDate != null) {
            return String.format("%s | %s | %s | %s | Date:%-10s | Time:%-5s | %s | %s | %s | %s", idStr, titleStr,
                    assignedStr, descStr, deadlineDate.toLocalDate(), deadlineDate.toLocalTime(), pointsStr,
                    completedStr, approvedStr, ratingStr);
        } else {
            return String.format("%s | %s | %s | %s | Start:%-16s | End:%-16s | %s | %s | %s | %s", idStr, titleStr,
                    assignedStr, descStr, startDate.toLocalDate() + " " + startDate.toLocalTime(),
                    endDate.toLocalDate() + " " + endDate.toLocalTime(), pointsStr, completedStr, approvedStr,
                    ratingStr);
        }
    }
}