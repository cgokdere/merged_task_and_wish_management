
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

   
    private static final String COMMANDS_FILE_PATH = "/TaskManagement/src/Task.txt";
    private static final String TASKS_FILE_PATH = "Tasks.txt";
    private static final String WISHES_FILE_PATH = "Wishes.txt";
   
    private static List<Task> tasks = new ArrayList<>();
    private static List<Wish> wishes = new ArrayList<>();
    private static Child child = new Child(); 
    
    private static final Pattern COMMAND_PARSE_PATTERN = Pattern.compile("\"([^\"]*)\"|\\S+");
    
    private static final Pattern SEMICOLON_SPLIT_REGEX = Pattern.compile("\"((?:[^\"]|\\\\\")*)\"|([^;]+)"); 


  
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

   
    public static LocalDateTime parseDateTime(String dateStr, String timeStr) throws DateTimeParseException {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
        LocalTime time = LocalTime.parse(timeStr, TIME_FORMATTER);
        return LocalDateTime.of(date, time);
    }
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
    public static LocalTime parseTime(String timeStr) throws DateTimeParseException {
        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }
    public static String formatDate(LocalDate date) {
        return (date == null) ? "N/A" : date.format(DATE_FORMATTER);
    }
    public static String formatTime(LocalTime time) {
        return (time == null) ? "N/A" : time.format(TIME_FORMATTER);
    }
    public static String formatDateTime(LocalDateTime dateTime) {
        return (dateTime == null) ? "N/A" : dateTime.format(DATE_TIME_FORMATTER);
    }

   
    public static void main(String[] args) {
        System.out.println("Starting Task and Wish Application...");

        Path commandPath = Paths.get(COMMANDS_FILE_PATH);
        Path taskPath = Paths.get(TASKS_FILE_PATH);
        Path wishPath = Paths.get(WISHES_FILE_PATH);

        
        loadWishesFromFile(wishPath); 
        loadTasksFromFile(taskPath);  

       
        processCommands(commandPath);

       

        System.out.println("\n----------------------------------------");
        System.out.println("Command processing finished.");
        System.out.println("Final Child Status: " + child.getStatusString());
        System.out.println("----------------------------------------");
    }

   
    private static void loadTasksFromFile(Path filePath) {
        tasks = new ArrayList<>();
        child = new Child(); 
        int initialPoints = 0;
        List<Integer> initialRatings = new ArrayList<>();

        if (!Files.exists(filePath)) {
            System.out.println("INFO: Tasks file not found (will be created on save): " + filePath.toAbsolutePath());
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line; int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue; 

                String[] parts = splitDataLine(line);
                try {
                    if (parts.length < 10) { throw new IllegalArgumentException("Expected at least 10 parts, got " + parts.length); }

                    String taskType = parts[0];
                    String assignerCode = parts[1];
                    String assigner = UserConstants.TEACHER_CODE.equalsIgnoreCase(assignerCode) ? UserConstants.TEACHER : UserConstants.PARENT;
                    String id = parts[2];
                    String title = unquote(parts[3]);
                    String description = unquote(parts[4]);
                    LocalDateTime deadline = parseDateTime(parts[5], parts[6]);
                    int points = Integer.parseInt(parts[7]);
                    String status = parts[8];
                    int rating = Integer.parseInt(parts[9]);

                    Task loadedTask = null;
                    if ("TASK1".equalsIgnoreCase(taskType) && parts.length == 10) {
                        loadedTask = new SimpleTask(id, assigner, title, description, deadline, points, status, rating);
                    } else if ("TASK2".equalsIgnoreCase(taskType) && parts.length == 14) {
                       
                        LocalDateTime startTime = parseDateTime(parts[10], parts[11]);
                        LocalDateTime endTime = parseDateTime(parts[12], parts[13]);
                        loadedTask = new ActivityTask(id, assigner, title, description, deadline, points, status, rating, startTime, endTime);
                    } else {
                        throw new IllegalArgumentException("Unknown task type '" + taskType + "' or incorrect number of parts (" + parts.length + ")");
                    }

                    if (loadedTask != null) {
                        tasks.add(loadedTask);
                        
                        if (StatusConstants.TASK_APPROVED.equals(loadedTask.getStatus())) {
                            initialPoints += loadedTask.getPoints();
                            if (loadedTask.getRating() >= 1 && loadedTask.getRating() <= 5) {
                                initialRatings.add(loadedTask.getRating());
                            }
                        }
                    }
                } catch (DateTimeParseException e) { System.err.printf("Error L%d Task: Invalid date/time format - %s\n", lineNumber, e.getMessage());
                } catch (NumberFormatException e) { System.err.printf("Error L%d Task: Invalid number format (points/rating) - %s\n", lineNumber, e.getMessage());
                } catch (IllegalArgumentException e) { System.err.printf("Error L%d Task: %s - Line: %s\n", lineNumber, e.getMessage(), line);
                } catch (Exception e) { System.err.printf("Unexpected Error L%d Task: %s\n", lineNumber, e.getMessage()); e.printStackTrace(); } // Catch broader exceptions
            }
        } catch (IOException e) {
            System.err.println("ERROR loading tasks: " + e.getMessage());
        }
        
        child.setTotalPoints(initialPoints);
        child.clearRatings(); 
        initialRatings.forEach(child::addRating); 

        System.out.println("INFO: Loaded " + tasks.size() + " tasks from " + filePath.toAbsolutePath());
        System.out.println("INFO: Initial Child State - Points: " + child.getTotalPoints() + ", Avg Rating: " + String.format("%.2f", child.getAverageRating()));
    }


    private static void loadWishesFromFile(Path filePath) {
        wishes = new ArrayList<>();
         if (!Files.exists(filePath)) {
             System.out.println("INFO: Wishes file not found (will be created on save): " + filePath.toAbsolutePath());
             return;
         }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line; int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                 lineNumber++;
                 line = line.trim();
                 if (line.isEmpty() || line.startsWith("#")) continue;

                 String[] parts = splitDataLine(line);
                 try {
                     if (parts.length < 6) { throw new IllegalArgumentException("Expected at least 6 parts, got " + parts.length); }

                    String wishType = parts[0];
                    String id = parts[1];
                    String title = unquote(parts[2]);
                    String description = unquote(parts[3]);
                    String status = parts[4];
                    int requiredLevel = Integer.parseInt(parts[5]); 

                    Wish loadedWish = null;
                    if ("WISH1".equalsIgnoreCase(wishType) && parts.length == 6) {
                        loadedWish = new ProductWish(id, title, description, status, requiredLevel);
                    }
                    else if ("WISH2".equalsIgnoreCase(wishType) && parts.length == 10) {
                       
                         LocalDateTime startTime = parseDateTime(parts[6], parts[7]);
                         LocalDateTime endTime = parseDateTime(parts[8], parts[9]);
                        loadedWish = new ActivityWish(id, title, description, status, requiredLevel, startTime, endTime);
                    }
                    else {
                         throw new IllegalArgumentException("Unknown wish type '" + wishType + "' or incorrect number of parts (" + parts.length + ")");
                    }

                    if (loadedWish != null) {
                        
                         wishes.add(loadedWish);
                    }
                } catch (DateTimeParseException e) { System.err.printf("Error L%d Wish: Invalid date/time format - %s\n", lineNumber, e.getMessage());
                } catch (NumberFormatException e) { System.err.printf("Error L%d Wish: Invalid number format (level) - %s\n", lineNumber, e.getMessage());
                } catch (IllegalArgumentException e) { System.err.printf("Error L%d Wish: %s - Line: %s\n", lineNumber, e.getMessage(), line);
                } catch (Exception e) { System.err.printf("Unexpected Error L%d Wish: %s\n", lineNumber, e.getMessage()); e.printStackTrace(); }
            }
        } catch (IOException e) {
            System.err.println("ERROR loading wishes: " + e.getMessage());
        }
         System.out.println("INFO: Loaded " + wishes.size() + " wishes from " + filePath.toAbsolutePath());
    }

   
    private static void saveTasksToFile() {
        Path filePath = Paths.get(TASKS_FILE_PATH);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("# Task File - Format: TASK_TYPE;ASSIGNER_CODE;ID;\"TITLE\";\"DESC\";DEADLINE_DATE;DEADLINE_TIME;POINTS;STATUS;RATING[;START_DATE;START_TIME;END_DATE;END_TIME]\n");
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
            System.out.println("INFO: Saved " + tasks.size() + " tasks to " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("ERROR saving tasks: " + e.getMessage());
        }
    }

    private static void saveWishesToFile() {
        Path filePath = Paths.get(WISHES_FILE_PATH);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
             
             List<Wish> wishesToSave = wishes.stream()
                                            .filter(w -> !StatusConstants.WISH_REJECTED.equals(w.getStatus()))
                                            .collect(Collectors.toList());
             writer.write("# Wish File - Format WISH1: WISH1;ID;\"TITLE\";\"DESC\";STATUS;REQ_LEVEL\n");
             writer.write("# Wish File - Format WISH2: WISH2;ID;\"TITLE\";\"DESC\";STATUS;REQ_LEVEL;START_DATE;START_TIME;END_DATE;END_TIME\n");
            for (Wish wish : wishesToSave) {
                writer.write(wish.toFileString());
                writer.newLine();
            }
            System.out.println("INFO: Saved " + wishesToSave.size() + " wishes (excluding rejected) to " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("ERROR saving wishes: " + e.getMessage());
        }
    }

     
     private static void processCommands(Path commandPath) {
        if (!Files.exists(commandPath)) {
            System.err.println("FATAL: Commands file not found: " + commandPath.toAbsolutePath());
            return;
        }
        System.out.println("\nProcessing commands from: " + commandPath.toAbsolutePath());
        int lineNumber = 0;
        try (BufferedReader reader = Files.newBufferedReader(commandPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue; 

                System.out.println("\n[Cmd Line " + lineNumber + "] Executing: " + line);
                try {
                    String[] commandParts = parseCommand(line);
                    if (commandParts.length > 0) {
                        executeCommand(commandParts);
                    }
                   
                } catch (Exception e) {
                    
                    System.err.println("Error processing command on line " + lineNumber + ": " + e.getMessage());
                    e.printStackTrace(); 
                }
            }
        } catch (IOException e) {
            System.err.println("FATAL ERROR reading commands file: " + e.getMessage());
        }
     }

   
    private static String[] parseCommand(String commandLine) {
        if (commandLine == null || commandLine.trim().isEmpty()) return new String[0];
        List<String> parts = new ArrayList<>();
        Matcher m = COMMAND_PARSE_PATTERN.matcher(commandLine.trim());
        while (m.find()) {
            if (m.group(1) != null) {
                
                parts.add(m.group(1));
            } else {
              
                parts.add(m.group());
            }
        }
        return parts.toArray(new String[0]);
    }


   
    private static String[] splitDataLine(String line) {
        List<String> parts = new ArrayList<>();
        Matcher m = SEMICOLON_SPLIT_REGEX.matcher(line);
        int lastIndex = 0;
        while(m.find()) {
            
            if (m.start() > lastIndex) {
                 String skipped = line.substring(lastIndex, m.start());
                 for (int i = 0; i < skipped.length(); i++) {
                     if (skipped.charAt(i) == ';') parts.add("");
                 }
            }
             if (m.group(1) != null) { 
                 parts.add(m.group(1).replace("\\\"", "\"").replace("\\\\", "\\")); // Unescape quotes/backslashes
             } else if (m.group(2) != null) { 
                 parts.add(m.group(2));
             }
            lastIndex = m.end();
        }
       
         while (lastIndex < line.length() && line.charAt(lastIndex) == ';') {
             parts.add("");
             lastIndex++;
         }
          

        return parts.toArray(new String[0]);
    }

   
    private static String unquote(String s) {
        if (s != null && s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\"); // Also handle escaped quotes inside
        }
        return s;
    }

    
    private static void executeCommand(String[] parts) {
        if (parts == null || parts.length == 0) {
            System.err.println("Warn: Received empty command.");
            return;
        }
        String command = parts[0].toUpperCase();
        
        try {
            switch (command) {
                
                case "ADD_TASK1":       handleAddTask1Cmd(parts); break;
                case "ADD_TASK2":       handleAddTask2Cmd(parts); break;
                case "LIST_ALL_TASKS":  handleListAllTasksCmd(parts); break;
                case "TASK_DONE":       handleTaskDoneCmd(parts); break;
                case "TASK_CHECKED":    handleTaskCheckedCmd(parts); break;

               
                case "ADD_WISH1":       handleAddWish1Cmd(parts); break;
                case "ADD_WISH2":       handleAddWish2Cmd(parts); break;
                case "LIST_ALL_WISHES": handleListAllWishesCmd(parts); break;
                case "WISH_CHECKED":    handleWishCheckedCmd(parts); break;

                
                case "ADD_BUDGET_COIN": handleAddBudgetCoinCmd(parts); break;
                case "PRINT_BUDGET":    handlePrintBudgetCmd(parts); break;
                case "PRINT_STATUS":    handlePrintStatusCmd(parts); break;

                default:
                    System.err.println("Unknown command: " + command);
                    break;
            }
        } catch (IndexOutOfBoundsException e) {
             System.err.println("Exec Error ("+command+"): Missing required arguments. " + e.getMessage());
        } catch (DateTimeParseException e) {
             System.err.println("Exec Error ("+command+"): Invalid date or time format. " + e.getMessage());
        } catch (NumberFormatException e) {
             System.err.println("Exec Error ("+command+"): Invalid number format (points, rating, level, amount). " + e.getMessage());
        } catch (IllegalArgumentException e) {
             System.err.println("Exec Error ("+command+"): Invalid argument value. " + e.getMessage());
        } catch (Exception e) { // Catch unexpected errors during command handling
            System.err.println("Unexpected Exec Error ("+command+"): " + e.getMessage());
            e.printStackTrace();
        }
    }

   
    private static void handleAddTask1Cmd(String[] parts) {
        
        if (parts.length != 8) { printArgError(parts[0], 8, parts.length); return; }

        String assignerCode = parts[1];
        String taskId = parts[2];
        String title = parts[3]; 
        String description = parts[4];
        String dateStr = parts[5];
        String timeStr = parts[6];
        int points = Integer.parseInt(parts[7]); 
        if (findTaskById(taskId).isPresent()) {
            System.err.println("Error: Task ID '" + taskId + "' already exists.");
            return;
        }

        String assigner = UserConstants.TEACHER_CODE.equalsIgnoreCase(assignerCode) ? UserConstants.TEACHER : UserConstants.PARENT;
        LocalDateTime deadline = parseDateTime(dateStr, timeStr);

        Task newTask = new SimpleTask(taskId, assigner, title, description, deadline, points);
        tasks.add(newTask);
        saveTasksToFile(); 
        System.out.println("Simple Task added: " + newTask.getId());
    }

   
    private static void handleAddTask2Cmd(String[] parts) {
        if (parts.length != 10) { printArgError(parts[0], 10, parts.length); return; }

        String assignerCode = parts[1];
        String taskId = parts[2];
        String title = parts[3];
        String description = parts[4];
        String startDateStr = parts[5];
        String startTimeStr = parts[6];
        String endDateStr = parts[7];
        String endTimeStr = parts[8];
        int points = Integer.parseInt(parts[9]);

        if (findTaskById(taskId).isPresent()) {
            System.err.println("Error: Task ID '" + taskId + "' already exists.");
            return;
        }

        String assigner = UserConstants.TEACHER_CODE.equalsIgnoreCase(assignerCode) ? UserConstants.TEACHER : UserConstants.PARENT;
        LocalDateTime startTime = parseDateTime(startDateStr, startTimeStr);
        LocalDateTime endTime = parseDateTime(endDateStr, endTimeStr);

       
        Task newTask = new ActivityTask(taskId, assigner, title, description, startTime, endTime, points);
        tasks.add(newTask);
        saveTasksToFile();
        System.out.println("Activity Task added: " + newTask.getId());
    }

   
    private static void handleListAllTasksCmd(String[] parts) {
        List<Task> tasksToList;
        String title = "\n--- All Tasks ---";

        if (parts.length == 1) {
            tasksToList = new ArrayList<>(tasks); // List all
        } else if (parts.length == 2) {
            String filter = parts[1].toUpperCase();
            LocalDate today = LocalDate.now();
            if ("D".equals(filter)) {
                title = "\n--- Daily Tasks (" + formatDate(today) + ") ---";
                tasksToList = getDailyTasks(today);
            } else if ("W".equals(filter)) {
                LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                 title = "\n--- Weekly Tasks (Week of " + formatDate(startOfWeek) + ") ---";
                tasksToList = getWeeklyTasks(today);
            } else {
                System.err.println("Invalid LIST_ALL_TASKS filter: " + parts[1] + ". Use 'D' for daily or 'W' for weekly.");
                return;
            }
        } else {
            printArgError(parts[0], "1 or 2", parts.length);
            return;
        }
        
        System.out.println(title);
        printTaskList(tasksToList);
    }

  
    private static void handleTaskDoneCmd(String[] parts) {
        if (parts.length != 2) { printArgError(parts[0], 2, parts.length); return; }
        String taskId = parts[1];
        Optional<Task> taskOpt = findTaskById(taskId);

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            if (StatusConstants.TASK_PENDING.equals(task.getStatus())) {
                task.setStatus(StatusConstants.TASK_COMPLETED_BY_CHILD);
                saveTasksToFile(); // Persist status change
                System.out.println("Task '" + taskId + "' marked as completed by child.");
            } else {
                System.err.println("Error: Task '" + taskId + "' cannot be marked done. Current status: " + task.getStatus());
            }
        } else {
            System.err.println("Error: Task not found with ID: " + taskId);
        }
    }

   
    private static void handleTaskCheckedCmd(String[] parts) {
        if (parts.length != 3) { printArgError(parts[0], 3, parts.length); return; }
        String taskId = parts[1];
        int rating = Integer.parseInt(parts[2]); // Throws NumberFormatException

        if (rating < 1 || rating > 5) {
            System.err.println("Error: Invalid rating (" + rating + "). Rating must be between 1 and 5.");
            return;
        }

        Optional<Task> taskOpt = findTaskById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            
            if (StatusConstants.TASK_COMPLETED_BY_CHILD.equals(task.getStatus())) {
                task.setStatus(StatusConstants.TASK_APPROVED);
                task.setRating(rating);

                
                child.addPoints(task.getPoints());
                child.addRating(rating);

                saveTasksToFile(); 
                System.out.println("Task '" + taskId + "' approved with rating " + rating + ". "
                                 + task.getPoints() + " points added. New total: " + child.getTotalPoints());
               
            } else {
                System.err.println("Error: Task '" + taskId + "' cannot be checked. Current status: " + task.getStatus() + ". Must be '"+StatusConstants.TASK_COMPLETED_BY_CHILD+"'.");
            }
        } else {
            System.err.println("Error: Task not found for approval with ID: " + taskId);
        }
    }

     
     private static void handleAddWish1Cmd(String[] parts) {
        
         if (parts.length != 4) { printArgError(parts[0], 4, parts.length); return; }
         String wishId = parts[1];
         String title = parts[2];
         String description = parts[3];

        if (findWishById(wishId).isPresent()) {
            System.err.println("Error: Wish ID '" + wishId + "' already exists.");
            return;
        }

         Wish newWish = new ProductWish(wishId, title, description); 
         wishes.add(newWish);
         saveWishesToFile(); 
         System.out.println("Product Wish added: " + newWish.getId() + " (Status: " + newWish.getStatus() + ")");
     }

   
    private static void handleAddWish2Cmd(String[] parts) {
        if (parts.length != 8) { printArgError(parts[0], 8, parts.length); return; }
        String wishId = parts[1];
        String title = parts[2];
        String description = parts[3];
        String startDateStr = parts[4];
        String startTimeStr = parts[5];
        String endDateStr = parts[6];
        String endTimeStr = parts[7];

        if (findWishById(wishId).isPresent()) {
            System.err.println("Error: Wish ID '" + wishId + "' already exists.");
            return;
        }

        LocalDateTime startTime = parseDateTime(startDateStr, startTimeStr);
        LocalDateTime endTime = parseDateTime(endDateStr, endTimeStr);

        Wish newWish = new ActivityWish(wishId, title, description, startTime, endTime);
        wishes.add(newWish);
        saveWishesToFile();
        System.out.println("Activity Wish added: " + newWish.getId() + " (Status: " + newWish.getStatus() + ")");
    }

    // LIST_ALL_WISHES
    private static void handleListAllWishesCmd(String[] parts) {
        if (parts.length != 1) { printArgError(parts[0], 1, parts.length); return; }
        System.out.println("\n--- All Wishes (Excluding Rejected on Load/Save) ---");
        // Requirement 2.3.2 implies rejected wishes are removed. We filter them during save.
        // Let's display all currently in memory for clarity, or filter here too?
        // Filtering here aligns with the "removed from list" idea more immediately.
        List<Wish> visibleWishes = wishes.stream()
                                        .filter(w -> !StatusConstants.WISH_REJECTED.equals(w.getStatus()))
                                        .collect(Collectors.toList());
        printWishList(visibleWishes);
        // Or print all including rejected:
        // System.out.println("\n--- All Wishes (Including Rejected in memory) ---");
        // printWishList(wishes);
    }

    // WISH_CHECKED W102 APPROVED 3
    // WISH_CHECKED W103 REJECTED
    private static void handleWishCheckedCmd(String[] parts) {
        if (parts.length < 3 || parts.length > 4) { printArgError(parts[0], "3 or 4", parts.length); return; }

        String wishId = parts[1];
        String statusCmd = parts[2].toUpperCase(); 

        Optional<Wish> wishOpt = findWishById(wishId);
        if (wishOpt.isEmpty()) {
            System.err.println("Error: Wish not found with ID: " + wishId);
            return;
        }
        Wish wish = wishOpt.get();

        
        if (!StatusConstants.WISH_PENDING_APPROVAL.equals(wish.getStatus())) {
            System.err.println("Error: Cannot check wish '" + wishId + "'. Current status: " + wish.getStatus());
            return;
        }

        if (StatusConstants.COMMAND_APPROVED.equals(statusCmd)) {
            if (parts.length != 4) { printArgError(parts[0] + " " + statusCmd, 4, parts.length); return; }
            int level = Integer.parseInt(parts[3]); 

            if (level < 1) { 
                 System.err.println("Error: Required level must be 1 or greater.");
                 return;
            }

            wish.setStatus(StatusConstants.WISH_APPROVED);
            wish.setRequiredLevel(level);
            saveWishesToFile(); // Persist change
            System.out.println("Wish '" + wishId + "' approved. Required Level: " + level);

        } else if (StatusConstants.COMMAND_REJECTED.equals(statusCmd)) {
            if (parts.length != 3) { printArgError(parts[0] + " " + statusCmd, 3, parts.length); return; }

            wish.setStatus(StatusConstants.WISH_REJECTED);
            
            System.out.println("Wish '" + wishId + "' rejected.");

        } else {
            System.err.println("Error: Invalid status for WISH_CHECKED. Use '" + StatusConstants.COMMAND_APPROVED + "' or '" + StatusConstants.COMMAND_REJECTED + "'.");
        }
    }

   
     private static void handleAddBudgetCoinCmd(String[] parts) {
         if (parts.length != 2) { printArgError(parts[0], 2, parts.length); return; }
         int amount = Integer.parseInt(parts[1]); 

         if (amount == 0) {
             System.out.println("Warn: Adding 0 points has no effect.");
             return;
         }

         child.addPoints(amount);
        
         System.out.println(amount + " points added manually. New total (in memory): " + child.getTotalPoints());
     }

    
     private static void handlePrintBudgetCmd(String[] parts) {
         if (parts.length != 1) { printArgError(parts[0], 1, parts.length); return; }
         System.out.println("\n--- Budget ---");
         System.out.println("Current Total Points: " + child.getTotalPoints());
     }

    
    private static void handlePrintStatusCmd(String[] parts) {
        if (parts.length != 1) { printArgError(parts[0], 1, parts.length); return; }
        System.out.println("\n--- Child Status ---");
        System.out.println(child.getStatusString());
    }


   
    private static Optional<Task> findTaskById(String taskId) {
        if (taskId == null || taskId.trim().isEmpty()) return Optional.empty();
        return tasks.stream()
                    .filter(t -> taskId.equalsIgnoreCase(t.getId()))
                    .findFirst();
    }

    private static Optional<Wish> findWishById(String wishId) {
         if (wishId == null || wishId.trim().isEmpty()) return Optional.empty();
        return wishes.stream()
                     .filter(w -> wishId.equalsIgnoreCase(w.getId()))
                     .findFirst();
    }

  
    private static List<Task> getDailyTasks(LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay(); 
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay(); 

        return tasks.stream().filter(task -> {
            if (task.isActivityTask()) {
               
                return task.getStartTime().isBefore(dayEnd) && task.getEndTime().isAfter(dayStart);
            } else {
               
                return task.getDeadline() != null && task.getDeadline().toLocalDate().equals(date);
            }
        }).collect(Collectors.toList());
    }

    
    private static List<Task> getWeeklyTasks(LocalDate date) {
        LocalDate weekStartDay = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEndDay = weekStartDay.plusDays(6); // Sunday

        LocalDateTime weekStart = weekStartDay.atStartOfDay(); 
        LocalDateTime weekEnd = weekEndDay.plusDays(1).atStartOfDay();

        return tasks.stream().filter(task -> {
             if (task.isActivityTask()) {
               
                 return task.getStartTime().isBefore(weekEnd) && task.getEndTime().isAfter(weekStart);
             } else {
                 
                 LocalDate deadlineDate = (task.getDeadline() != null) ? task.getDeadline().toLocalDate() : null;
                 return deadlineDate != null && !deadlineDate.isBefore(weekStartDay) && !deadlineDate.isAfter(weekEndDay);
             }
        }).collect(Collectors.toList());
    }

   
    private static void printArgError(String cmd, int exp, int act) {
        System.err.println("Args Error ("+cmd+"): Expected "+exp+" arguments, but got "+act+".");
    }
    private static void printArgError(String cmd, String exp, int act) {
        System.err.println("Args Error ("+cmd+"): Expected "+exp+" arguments, but got "+act+".");
    }

    private static void printTaskList(List<Task> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("(No tasks match criteria)");
        } else {
            list.forEach(System.out::println); 
        }
    }

    private static void printWishList(List<Wish> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("(No wishes match criteria)");
        } else {
            list.forEach(System.out::println); 
        }
    }
}