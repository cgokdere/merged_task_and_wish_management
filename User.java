package kidsTask2;

import java.util.List;


public class User {
    private Child child;

    public User() {
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public void printBudget(int childId, List<Child> children) {
        if (child != null && child.getChildId() == childId) {
            System.out.println("Coin: " + child.getCoin());
        } else {
            for (Child c : children) {
                if (c.getChildId() == childId) {
                    System.out.println("Child " + childId+ "'s coin: " + c.getCoin());
                    return;
                }
            }
            System.out.println("Invalid ID");
        }
    }

    public void printStatus(int childId, List<Child> children) {
        if (child != null && child.getChildId() == childId) {
            System.out.println("Level: " + child.getlevel());
        } else {
            for (Child c : children) {
                if (c.getChildId() == childId) {
                    System.out.println("Child "+ childId+ "'s level: " + c.getlevel());
                    return;
                }
            }
            System.out.println("Invalid Id");
        }
    }
}
