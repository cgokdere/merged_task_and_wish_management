package kidsTask2;

public class Wishes {

	private int wishId;
	private String wishTitle;
	private String description;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String type;
	private String approvalStatus;

	public Wishes(int wishId, String wishTitle, String description, String startDate, String startTime, String endDate, String endTime,
			String type, String approvalStatus) {
		this.wishId = wishId;
		this.wishTitle = wishTitle;
		this.description = description;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
		this.type = type;
		this.approvalStatus = approvalStatus;
	}
	
	public Wishes(int wishId, String wishTitle, String description, String startDate, String startTime, String endDate, String endTime) {
  this.wishId = wishId;
  this.wishTitle = wishTitle;
  this.description = description;
  this.startDate = startDate;
  this.startTime = startTime;
  this.endDate = endDate;
  this.endTime=endTime;
  
}
	public Wishes(int wishId, String wishTitle, String description) {
		  this.wishId = wishId;
		  this.wishTitle = wishTitle;
		  this.description = description;
		  
		  
		}
	
	public Wishes() {
		this.wishId = 1;
		this.wishTitle = " ";
		this.description = " ";
		this.startDate = " ";
		this.startTime= "";
		this.endTime = " ";
		this.endDate = "";
		this.type = " ";
		this.approvalStatus = " ";
	}
	
	public int getWishId() {
        return wishId;
    }

    public void setWishId(int wishId) {
        this.wishId = wishId;
    }

    public String getWishTitle() {
        return wishTitle;
    }

    public void setWishTitle(String wishTitle) {
        this.wishTitle = wishTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void setApprovalStatus(String approvalStatus) {
    	this.approvalStatus= approvalStatus;
    }
    
    public String getApprovalStatus() {
    	return approvalStatus;
    }
    
    
    @Override
    public String toString() {
 
        String basicInfo = String.format(
            "%-8s | %-25s | %-40s",
            wishId,
            wishTitle,
            description
        );

    
        String dateInfo;
        if (startDate != null && !startDate.trim().isEmpty()) {
            dateInfo = String.format(
                " | %-10s %-5s | %-10s %-5s",
                startDate,
                startTime != null ? startTime : "",
                endDate,
                endTime != null ? endTime : ""
            );
        } else {
            dateInfo = String.format(
                " | %-16s | %-10s %-5s",
                "",
                endDate != null ? endDate : "",
                endTime != null ? endTime : ""
            );
        }

     
        String extraInfo = String.format(
            " | %-10s  %-8s",
            type != null ? type : "",
            approvalStatus != null ? approvalStatus : ""
        );

        return basicInfo + dateInfo + extraInfo;
    }
}

