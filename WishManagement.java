import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class WishManagement {
	
    private static Map<String, Wish> Wishes = new HashMap<>();

    public static void addWish1(ProductWish wish) {
        Wishes.put(wish.getId(), wish);
    }
    
    public void addWish(Wish wish) {
        Wishes.put(wish.getId(), wish);
    }

    public void ListAllWishes() {
        System.out.println("\n=== WISH LIST ===");
        if (Wishes.isEmpty()) {
            System.out.println("No wishes found.");
            return;
        }
        
        for (Wish wish : Wishes.values()) {
            System.out.println(wish);
        }
    }

    public void ListAllWishes(String filter) {
        System.out.println("\n=== WISH LIST (" + 
            (filter.isEmpty() ? "ALL" : filter.equals("D") ? "DAILY" : "WEEKLY") + 
            ") ===");
            
        if (Wishes.isEmpty()) {
            System.out.println("No wishes found.");
            return;
        }
        
        boolean foundAny = false;
        for (Wish wish : Wishes.values()) {
            if (filterWish(wish, filter)) {
                System.out.println(wish);
                foundAny = true;
            }
        }
        
        if (!foundAny) {
            System.out.println("No wishes found for " + 
                (filter.equals("D") ? "today" : filter.equals("W") ? "this week" : ""));
        }
    }
    
    public boolean filterWish(Wish wish, String filter) {
        switch (filter) {
            case "D":
                return isDueToday(wish);
            case "W":
                return isDueThisWeek(wish);
            default:
                return true; // Show all if no filter
        }
    }
    
    public boolean isDueToday(Wish wish) {
        if (!wish.isActivityWish()) {
            return false; // Product wishes are not date-specific
        }
        
        LocalDate today = LocalDate.now();
        
        LocalDateTime startTime = wish.getStartTime();
        LocalDateTime endTime = wish.getEndTime();
        
        if (startTime == null || endTime == null) {
            return false;
        }
        
        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
        
        // Check if today falls within the activity period
        return (today.isEqual(startDate) || today.isEqual(endDate)) ||
               (today.isAfter(startDate) && today.isBefore(endDate));
    }
    
    public boolean isDueThisWeek(Wish wish) {
        if (!wish.isActivityWish()) {
            return false; // Product wishes are not date-specific
        }
        
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(6); // 7 days including today
        
        LocalDateTime startTime = wish.getStartTime();
        LocalDateTime endTime = wish.getEndTime();
        
        if (startTime == null || endTime == null) {
            return false;
        }
        
        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
        
        // Activity starts this week
        boolean startsThisWeek = (startDate.isEqual(today) || startDate.isAfter(today)) && 
                                 startDate.isBefore(endOfWeek) || startDate.isEqual(endOfWeek);
        
        // Activity ends this week
        boolean endsThisWeek = (endDate.isEqual(today) || endDate.isAfter(today)) && 
                               endDate.isBefore(endOfWeek) || endDate.isEqual(endOfWeek);
        
        // Activity spans across this week
        boolean spansThisWeek = startDate.isBefore(today) && endDate.isAfter(endOfWeek);
        
        return startsThisWeek || endsThisWeek || spansThisWeek;
    }

    public void wishChecked(String wishId, String approvalStatus, int level, Child child) {
        Wish wishInMap = Wishes.get(wishId);
        if (wishInMap != null) {
            // Update status in the Wishes map
            if (approvalStatus.equals(Wish.COMMAND_APPROVED)) {
                wishInMap.setStatus(Wish.APPROVED);
                System.out.println("Wish " + wishId + " APPROVED");

                if (level != -1) {
                    wishInMap.setRequiredLevel(level);
                    if (child.getLevel() < level) {  
                        System.out.println("The child must be at least level " + level + " to fulfill this wish");
                    }
                }
            } else if (approvalStatus.equals(Wish.COMMAND_REJECTED)) {
                wishInMap.setStatus(Wish.REJECTED);
                System.out.println("Wish " + wishId + " REJECTED");
            }
            
            // Update the same wish in child's wishList
            for (Wish childWish : child.getWishList()) {
                if (childWish.getId().equals(wishId)) {
                    childWish.setStatus(wishInMap.getStatus());
                    childWish.setRequiredLevel(wishInMap.getRequiredLevel());
                    break;
                }
            }
        } else {
            System.out.println("Wish with ID " + wishId + " not found");
        }
    }

    public void addWish1(String wishId, String title, String description) {
        Wish newWish = new ProductWish(wishId, title, description);
        Wishes.put(wishId, newWish);
    }

    public void addWish2(int wishId, String title, String description, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        try {
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
            ActivityWish newWish = new ActivityWish(String.valueOf(wishId), title, description, startDateTime, endDateTime);
            Wishes.put(String.valueOf(wishId), newWish);
        } catch (DateTimeParseException e) {
            System.err.println("Error: Invalid date/time format for wish ID: " + wishId + ". Skipping wish.");
            e.printStackTrace();
        }
    }
}