package src;
import java.util.Scanner;

public class OvertimeProgressionTracker {
    private int previousWorkoutDuration;
    private int previousWorkoutIntensity;
    private int previousWorkoutRepetitions;

    public OvertimeProgressionTracker() {
        this.previousWorkoutDuration = 0;
        this.previousWorkoutIntensity = 0;
        this.previousWorkoutRepetitions = 0;
    }

    public void trackProgress() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the current workout duration (in minutes): ");
        int currentWorkoutDuration = scanner.nextInt();

        System.out.print("Enter the current workout intensity (1-10): ");
        int currentWorkoutIntensity = scanner.nextInt();

        System.out.print("Enter the current workout repetitions: ");
        int currentWorkoutRepetitions = scanner.nextInt();

        if (previousWorkoutDuration == 0) {
            System.out.println("No previous workout data available.");
        } else {
            System.out.println("Progression Results:");
            System.out.println("Duration: " + (currentWorkoutDuration - previousWorkoutDuration) + " minutes");
            System.out.println("Intensity: " + (currentWorkoutIntensity - previousWorkoutIntensity));
            System.out.println("Repetitions: " + (currentWorkoutRepetitions - previousWorkoutRepetitions));
        }

        previousWorkoutDuration = currentWorkoutDuration;
        previousWorkoutIntensity = currentWorkoutIntensity;
        previousWorkoutRepetitions = currentWorkoutRepetitions;
    }

    public static void main(String[] args) {
        OvertimeProgressionTracker tracker = new OvertimeProgressionTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Track Progress");
            System.out.println("2. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    tracker.trackProgress();
                    break;
                case 2:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}