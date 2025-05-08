import java.util.ArrayList;
import java.util.List;

public class TaskManagement {
    private List<Task> taskList = new ArrayList<>();
    
    public void addTask(Task task) {
        if (task == null) {
            System.out.println("Task can't be null");
            return;
        }
        taskList.add(task);
    }
    
    public void listAllTasks(String filter) {
        String header = String.format("%-7s | %-30s | %-5s | %-30s | %-18s | %-18s | %-5s | %-8s | %-9s | %-5s", "ID",
                "Title", "By", "Description", "Date/Start", "Time/End", "Pts", "Done", "Approved", "Rate");
        System.out.println(
                "\n=== TASK LIST (" + (filter.isEmpty() ? "ALL" : filter.equals("D") ? "DAILY" : "WEEKLY") + ") ===");
        System.out.println(header);
        System.out.println("-".repeat(140));
        boolean hasTasks = false;
        for (Task task : taskList) {
            if (filterTask(task, filter)) {
                System.out.println(task.getTaskDetails());
                hasTasks = true;
            }
        }
        if (!hasTasks) {
            System.out.println(
                    "No tasks found for " + (filter.equals("D") ? "today" : filter.equals("W") ? "this week" : ""));
        }
    }
    
    public boolean filterTask(Task task, String filter) {
        switch (filter) {
        case "D":
            return task.isDueToday();
        case "W":
            return task.isDueThisWeek();
        default:
            return true; // Show all if no filter
        }
    }
    
    public void taskDone(int taskId, Child child) {
        Task foundTask = null;
        
        // Find task in taskList
        for (Task task : taskList) {
            if (task.getTaskId() == taskId) {
                task.setCompleted(true);
                foundTask = task;
                break;
            }
        }
        
        // If found, also update in child's taskList
        if (foundTask != null) {
            // Update in child's list
            for (Task childTask : child.getTaskList()) {
                if (childTask.getTaskId() == taskId) {
                    childTask.setCompleted(true);
                    break;
                }
            }
            System.out.println("Task " + taskId + " (" + foundTask.getTitle() + ") marked as completed");
        } else {
            System.out.println("Task with ID " + taskId + " not found");
        }
    }
    
    public void taskChecked(int taskId, int rating, Child child) {
        Task foundTask = null;
        
        // Find task in taskList
        for (Task task : taskList) {
            if (task.getTaskId() == taskId) {
                if (!task.isCompleted()) {
                    System.out.println("Task " + taskId + " has not been marked as completed yet");
                    return;
                }
                
                task.setApproved(true);
                task.setRating(rating);
                foundTask = task;
                break;
            }
        }
        
        // If found, also update in child's taskList and award points
        if (foundTask != null) {
            // Update in child's list
            for (Task childTask : child.getTaskList()) {
                if (childTask.getTaskId() == taskId) {
                    childTask.setApproved(true);
                    childTask.setRating(rating);
                    break;
                }
            }
            
            int pointsEarned = foundTask.getPoints();
            child.addPoints(pointsEarned);
            
            System.out.println("\n=== TASK UPDATES ===");
            System.out.println("Task " + taskId + " " + foundTask.getTitle() + " approved with rating " + rating + "/5");
            System.out.println("Points earned: " + pointsEarned + " " + "Total Points: " + child.getPoints());
        } else {
            System.out.println("Task with ID " + taskId + " not found");
        }
    }
}



