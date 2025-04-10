package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ASCIIArt {
    public static void displayLogo() {
        System.out.println(" ___________.__  __ ___.                  ");
        System.out.println("\\_   _____/|__|/  |\\_ |_________  ____  ");
        System.out.println(" |    __)  |  \\   __\\ __ \\_  __ \\/  _ \\ ");
        System.out.println(" |     \\   |  ||  | | \\_\\ \\  | \\(  <_> )");
        System.out.println(" \\___  /   |__||__| |___  /__|   \\____/ ");
        System.out.println("     \\/                 \\/               ");
    }
}


class Workout {
    private String exerciseType;
    private int duration;
    private int intensity;
    private int reps;
    private int sets;

    public Workout(String exerciseType, int duration, int intensity, int reps, int sets) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.intensity = intensity;
        this.reps = reps;
        this.sets = sets;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public int getDuration() {
        return duration;
    }

    public int getIntensity() {
        return intensity;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public int calculateCalories() {
        int caloriesPerMinute = 0;
        switch (exerciseType.toLowerCase()) {
            case "running":
                caloriesPerMinute = 8;
                break;
            case "cycling":
                caloriesPerMinute = 6;
                break;
            case "weightlifting":
                caloriesPerMinute = 5;
                break;
            default:
                caloriesPerMinute = 4;
        }
        return caloriesPerMinute * duration * (intensity / 10);
    }

    @Override
    public String toString() {
        return "Exercise: " + exerciseType + ", Duration: " + duration + " minutes, Intensity: " + intensity +
                ", Sets: " + sets + ", Reps: " + reps;
    }
}

class FitnessGoal {
    private String goal;
    private String description;
    private String status;

    public FitnessGoal(String goal, String description, String status) {
        this.goal = goal;
        this.description = description;
        this.status = status;
    }

    public String getGoal() {
        return goal;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return goal + " - Status: " + status + "\n  Description: " + description;
    }
}

class FitnessTracker {
    private List<Workout> workouts;
    private List<FitnessGoal> goals;
    private int totalWorkoutTime;
    private int totalCaloriesBurned;

    public FitnessTracker() {
        workouts = new ArrayList<>();
        goals = new ArrayList<>();
        totalWorkoutTime = 0;
        totalCaloriesBurned = 0;
    }

    public void logWorkout(Workout workout) {
        workouts.add(workout);
        totalWorkoutTime += workout.getDuration();
        totalCaloriesBurned += workout.calculateCalories();
    }

    public void addGoal(FitnessGoal goal) {
        goals.add(goal);
    }

    public void displayWorkouts() {
        System.out.println("\nTotal Workout Time: " + totalWorkoutTime + " minutes");
        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
        System.out.println("\nLogged Workouts: ");
        for (Workout workout : workouts) {
            System.out.println(workout);
        }
    }

    public void displayGoals() {
        System.out.println("\nFitness Goals:");
        for (FitnessGoal goal : goals) {
            System.out.println(goal);
        }
    }

    public void saveDataToFile() {
        try {
            System.out.println("\nSaving data...");

            String loadingBar = "Progress: [..........] ";
            for (int i = 0; i <= 10; i++) {
                Thread.sleep(300); 
                loadingBar = "Progress: [" + ".".repeat(i) + " ".repeat(10 - i) + "]";
                System.out.print("\r" + loadingBar);
            }

            File file = new File("fitness_data.txt");
            FileWriter writer = new FileWriter(file);

            writer.write("Workout Logs:\n");
            for (Workout workout : workouts) {
                writer.write(workout + "\n");
            }
            writer.write("\nTotal Workout Time: " + totalWorkoutTime + " minutes\n");
            writer.write("Total Calories Burned: " + totalCaloriesBurned + "\n\n");

            writer.write("Fitness Goals:\n");
            for (FitnessGoal goal : goals) {
                writer.write(goal + "\n");
            }

            writer.close();
            System.out.println("\n\nData saved successfully to fitness_data.txt!");
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while saving data.");
            e.printStackTrace();
        }
    }
}

public class FitBroApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FitnessTracker tracker = new FitnessTracker();

        ASCIIArt.displayLogo();

        while (true) {
            System.out.println("\n=========================================");
            System.out.println("Choose an option:");
            System.out.println("1. Log Workout");
            System.out.println("2. View Workout Progress");
            System.out.println("3. Set Fitness Goals");
            System.out.println("4. Display Fitness Goals");
            System.out.println("5. Save Data");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    logWorkout(scanner, tracker);
                    break;
                case 2:
                    tracker.displayWorkouts();
                    break;
                case 3:
                    setFitnessGoals(scanner, tracker);
                    break;
                case 4:
                    tracker.displayGoals();
                    break;
                case 5:
                    tracker.saveDataToFile();
                    break;
                case 6:
                    System.out.println("\nExiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void logWorkout(Scanner scanner, FitnessTracker tracker) {
        System.out.println("\n--- Log Your Workout ---");
        System.out.print("Enter the type of exercise (e.g., Running, Cycling): ");
        String exerciseType = scanner.nextLine();
        System.out.print("Enter the duration of workout in minutes: ");
        int duration = scanner.nextInt();
        System.out.print("Enter the intensity level (1-10): ");
        int intensity = scanner.nextInt();
        System.out.print("Enter the number of reps: ");
        int reps = scanner.nextInt();
        System.out.print("Enter the number of sets: ");
        int sets = scanner.nextInt();
        scanner.nextLine();  

        Workout workout = new Workout(exerciseType, duration, intensity, reps, sets);
        tracker.logWorkout(workout);
        System.out.println("\nWorkout Logged Successfully!");
    }

    private static void setFitnessGoals(Scanner scanner, FitnessTracker tracker) {
        System.out.println("\n--- Set Your Fitness Goal ---");
        System.out.print("Enter your fitness goal (e.g., Weight Loss, Strength): ");
        String goal = scanner.nextLine();
        System.out.print("Enter a description for your goal: ");
        String description = scanner.nextLine();
        System.out.print("Enter the status of your goal (e.g., Not Started, Started, Completed): ");
        String status = scanner.nextLine();

        FitnessGoal fitnessGoal = new FitnessGoal(goal, description, status);
        tracker.addGoal(fitnessGoal);
        System.out.println("\nGoal added successfully: " + goal);
    }
}
