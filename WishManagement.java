package seng272;

	import java.util.List;
	import java.util.ArrayList;

	public class WishManagement {
		private List<Wish> wishList;

		public WishManagement() {
			this.wishList = new ArrayList<>();
		}

		public void addWish(Wish wish) {
			wishList.add(wish);
		}

		public void listAllWishes() {
			System.out.println("\n=== WISH LIST ===");

			// Print pending wishes
			System.out.println("Pending Wishes:");
			boolean hasPending = false;
			for (Wish wish : wishList) {
				if (wish.getStatus().equals("Pending")) {
					System.out.println(
							"- " + wish.getTitle() + (wish.getDescription().isEmpty() ? "" : ": " + wish.getDescription()));
					hasPending = true;
				}
			}
			if (!hasPending) {
				System.out.println("No pending wishes");
			}

			// Print approved wishes
			System.out.println("\nApproved Wishes:");
			boolean hasApproved = false;
			for (Wish wish : wishList) {
				if (wish.getStatus().equals("Approved")) {
					System.out.println("- " + wish.getTitle() + " (Level " + wish.getRequiredLevel() + ")");
					hasApproved = true;
				}
			}
			if (!hasApproved) {
				System.out.println("No approved wishes");
			}
		}

		public void wishChecked(String wishId, String status, int requiredLevel) {
			System.out.println("\n=== WISH PROCESSING ===");

			// Find the wish
			Wish targetWish = null;
			for (Wish wish : wishList) {
				if (wish.getWishId().equals(wishId)) {
					targetWish = wish;
					break;
				}
			}

			if (targetWish == null) {
				System.out.println("Error: Wish with ID " + wishId + " not found");
				return;
			}

			System.out.println("Processing wish:");
			System.out.println("- ID: " + wishId);
			System.out.println("- Title: " + targetWish.getTitle());
			System.out.println("- Current Status: " + targetWish.getStatus());

			if (status.equalsIgnoreCase("APPROVED")) {
				// Check if already approved
				if (targetWish.getStatus().equals("Approved")) {
					System.out.println(" Wish is already approved");
					return;
				}

				// Approve with required level
				targetWish.approveWish(requiredLevel);
				System.out.println("Approved (Required Level: " + requiredLevel + ")");

			} else if (status.equalsIgnoreCase("REJECTED")) {
				// Rejection logic
				if (targetWish.getStatus().equals("Rejected")) {
					System.out.println("Wish is already rejected");
					return;
				}
				targetWish.rejectWish();
				System.out.println("Rejected");

			} else {
				System.out.println("Error: Invalid status - must be APPROVED or REJECTED");
				return;
			}

			// Show updated status
			System.out.println("\nUpdated Status:");
			System.out.println("- New Status: " + targetWish.getStatus());
			if (targetWish.getStatus().equals("Approved")) {
				System.out.println("- Required Level: " + targetWish.getRequiredLevel());
			}
		}

	}

