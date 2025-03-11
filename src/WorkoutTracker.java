package src;
import java.util.Scanner;

public class WorkoutTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter workout name: ");
        String workoutName = scanner.nextLine();

        System.out.print("Enter number of sets: ");
        int sets = scanner.nextInt();

        System.out.print("Enter number of reps done: ");
        int reps = scanner.nextInt();

        System.out.println("Workout Summary:");
        System.out.println("Name: " + workoutName);
        System.out.println(sets + "x" + reps);
    }
}