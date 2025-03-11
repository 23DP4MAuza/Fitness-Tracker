package src;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FitnessGoalTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Goal> goals = new ArrayList<>();

        while (true) {
            System.out.println("1. Add goal");
            System.out.println("2. View goals");
            System.out.println("3. Exit");

            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    addGoal(scanner, goals);
                    break;
                case 2:
                    viewGoals(goals);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static void addGoal(Scanner scanner, List<Goal> goals) {
        System.out.print("Enter goal name: ");
        String name = scanner.nextLine();

        System.out.print("Enter goal description: ");
        String description = scanner.nextLine();

        System.out.print("Enter goal target date (yyyy-MM-dd): ");
        String targetDate = scanner.nextLine();

        Goal goal = new Goal(name, description, targetDate);
        goals.add(goal);

        System.out.println("Goal added successfully!");
    }

    private static void viewGoals(List<Goal> goals) {
        if (goals.isEmpty()) {
            System.out.println();
            System.out.println("No goals added yet.");
        } else {
            System.out.println("Your goals:");
            System.out.println();
            for (int i = 0; i < goals.size(); i++) {
                Goal goal = goals.get(i);
                System.out.println((i + 1) + ". " + goal.getName());
                System.out.println("Description: " + goal.getDescription());
                System.out.println("Target Date: " + goal.getTargetDate());
                System.out.println();
            }
        }
    }

    private static class Goal {
        private String name;
        private String description;
        private String targetDate;

        public Goal(String name, String description, String targetDate) {
            this.name = name;
            this.description = description;
            this.targetDate = targetDate;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getTargetDate() {
            return targetDate;
        }
    }
}