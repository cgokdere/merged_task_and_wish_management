

import java.time.LocalDateTime;
import java.util.Objects;


public abstract class Wish {
    protected String id;
    protected String title;
    protected String description;
    protected String status;
    protected int requiredLevel; 

    
    public Wish(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = StatusConstants.WISH_PENDING_APPROVAL;
        this.requiredLevel = -1; 
    }

   
    public Wish(String id, String title, String description, String status, int requiredLevel) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.requiredLevel = requiredLevel;
    }

    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public int getRequiredLevel() { return requiredLevel; }

    
    public void setStatus(String status) {
        if (StatusConstants.WISH_PENDING_APPROVAL.equals(status) ||
            StatusConstants.WISH_APPROVED.equals(status) ||
            StatusConstants.WISH_REJECTED.equals(status)) {
            this.status = status;
        } else {
             System.err.println("Warn: Invalid wish status: '" + status + "' ID: " + this.id);
        }
    }
    public void setRequiredLevel(int requiredLevel) {
        if (requiredLevel >= 1) { 
             this.requiredLevel = requiredLevel;
        } else {
            System.err.println("Warn: Invalid required level ("+ requiredLevel +") for wish ID: " + this.id +". Level should be >= 1.");
           
        }
    }


   
    public abstract boolean isActivityWish();
    public abstract LocalDateTime getStartTime();
    public abstract LocalDateTime getEndTime();
    public abstract String toFileString();

    @Override
    public String toString() {
        String levelReqStr = (requiredLevel < 1) ? "N/A" : String.valueOf(requiredLevel);
        if (StatusConstants.WISH_PENDING_APPROVAL.equals(status)) {
            levelReqStr = "N/A (Pending)";
        } else if (StatusConstants.WISH_REJECTED.equals(status)) {
             levelReqStr = "N/A (Rejected)";
        }

        return String.format("ID: %-5s | Title: %-25s | Desc: %-30s | Status: %-18s | Lvl Req: %s",
                id, "\"" + title + "\"", "\"" + description + "\"", status, levelReqStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wish wish = (Wish) o;
        return Objects.equals(id, wish.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
