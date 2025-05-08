package kidsTask2;

import java.util.LinkedList;
import java.util.List;

public class Tasks {

	private int taskId;
	private String assigner;
	private String title;
	private String endDate;
	private String endTime;
	private int taskPoints;
	private String startTime;
	private String startDate;
	private String description;
	private boolean taskStatus = false;
	//private LinkedList<String> Tasks;
	
	public Tasks(int taskId, String assigner, String title, String endDate, String endTime, int taskPoints, String startTime, 
			String startDate, String description, boolean taskStatus, LinkedList<String> Tasks) {
		this.taskId = taskId;
		this.title = title;
		this.endTime=endTime;
		this.endDate= endDate;
		this.assigner = assigner;
		this.taskPoints= taskPoints;
		this.startTime=startTime;
		this.startDate=startDate;
		this.description=description;
		this.taskStatus = taskStatus;
		
	}
	
	public Tasks(String assigner, int taskId, String title, String description, String endDate, String endTime, int taskPoints) {
		this.taskId = taskId;
		this.title = title;
		this.endTime=endTime;
		this.endDate= endDate;
		this.assigner = assigner;
		this.taskPoints= taskPoints;
		this.description=description;
		
		
	}
	
	public Tasks(String assigner, int taskId, String title, String description, String startDate, String startTime, 
			String endDate, String endTime, int taskPoints) {
		this.taskId = taskId;
		this.title = title;
		this.endTime=endTime;
		this.endDate= endDate;
		this.startDate = startDate;
		this.startTime =startTime;
		this.assigner = assigner;
		this.taskPoints= taskPoints;
		this.description=description;
		
		
	}
	
	public Tasks() {
		this.taskId = 100;
		this.title = " ";
		this.endTime="";
		this.endDate= " ";
		this.assigner = "-";
		this.taskPoints= 0;
		this.startTime= "";
		this.startDate= " ";
		this.description= " ";
		this.taskStatus = false;
	}
	
	public boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

	
	public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAssigner() {
        return assigner;
    }

    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public int getTaskPoints() {
        return taskPoints;
    }

    public void setTaskPoints(int taskPoints) {
        this.taskPoints = taskPoints;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    @Override
    public String toString() {
     
        String basicInfo = String.format(
            "%-8d | %-25s | %-40s | %-8s | %-5d | %-8s",
            taskId,
            title,
            description,
            assigner,
            taskPoints,
            taskStatus ? "Done" : "Pending"
        );

      
        String dateInfo;
        if (startDate != null) {
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
                endDate,
                endTime != null ? endTime : ""
            );
        }

        return basicInfo + dateInfo;
    }
	
}
