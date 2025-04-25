package src;

import java.io.*;
import java.util.*;

public class FitBroApp {

    public static final String RESET = "\033[0m";  
    public static final String RED = "\033[31m";    
    public static final String GREEN = "\033[32m";  
    public static final String YELLOW = "\033[33m"; 
    public static final String BLUE = "\033[34m";   
    public static final String PURPLE = "\033[35m"; 
    public static final String CYAN = "\033[36m";   

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FitnessTracker tracker = new FitnessTracker();

        while (true) {
            clearScreen();
            ASCIIArt.displayLogo();

            System.out.println(BLUE + "\n--- Workout ---" + RESET);
            System.out.println("1. Log workout");
            System.out.println("2. View workout progress");

            System.out.println(CYAN + "\n-- Personal --" + RESET);
            System.out.println("3. Personal stats");
            System.out.println("4. Set Fitness goals");
            System.out.println("5. Display Fitness goals");
            System.out.println("6. Edit Fitness goals");

            System.out.println(PURPLE + "\n----------------------" + RESET);
            System.out.println("7. Save Data");
            System.out.println("8. View Saved Data");
            System.out.println("9. Exit");

            System.out.print(YELLOW + "\nEnter your choice: " + RESET);
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character after int input

            clearScreen(); // Clear screen after user input

            switch (choice) {
                case 1 -> logWorkout(scanner, tracker);
                case 2 -> tracker.displayWorkouts();
                case 3 -> setPersonalStats(scanner, tracker);
                case 4 -> setFitnessGoals(scanner, tracker);
                case 5 -> tracker.displayGoals();
                case 6 -> editFitnessGoal(scanner, tracker);
                case 7 -> tracker.saveDataToFile();
                case 9 -> {
                    System.out.println(RED + "\nExiting... Goodbye!" + RESET);
                    return;
                }
                case 8 -> viewSavedData();
                default -> System.out.println(RED + "Invalid choice, please try again." + RESET);
            }

            System.out.println(YELLOW + "\nPress Enter to return to the menu..." + RESET);
            scanner.nextLine(); // Wait for user to press Enter before clearing the screen again
        }
    }

    private static void logWorkout(Scanner scanner, FitnessTracker tracker) {
        System.out.println(GREEN + "\n--- Log Your Workout ---" + RESET);
        System.out.print("Enter the type of exercise (e.g., Running, Cycling): ");
        String exerciseType = scanner.nextLine();
        System.out.print("Enter the duration in minutes: ");
        int duration = scanner.nextInt();
        System.out.print("Enter intensity (1–10): ");
        int intensity = scanner.nextInt();
        System.out.print("Enter number of reps: ");
        int reps = scanner.nextInt();
        System.out.print("Enter number of sets: ");
        int sets = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after int input

        tracker.logWorkout(new Workout(exerciseType, duration, intensity, reps, sets));
        System.out.println(GREEN + "\nWorkout Logged Successfully!" + RESET);
    }

    private static void setFitnessGoals(Scanner scanner, FitnessTracker tracker) {
        System.out.println(GREEN + "\n--- Set Your Fitness Goal ---" + RESET);
        System.out.print("Enter goal: ");
        String goal = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter status (Not Started / Started / Completed): ");
        String status = scanner.nextLine();

        tracker.addGoal(new FitnessGoal(goal, description, status));
        System.out.println(GREEN + "\nGoal added successfully!" + RESET);
    }

    private static void editFitnessGoal(Scanner scanner, FitnessTracker tracker) {
        tracker.displayGoals();
        System.out.print("\nEnter the number of the goal to edit: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();  // Consume newline

        if (index < 0 || index >= tracker.getGoals().size()) {
            System.out.println(RED + "Invalid goal number." + RESET);
            return;
        }

        FitnessGoal goal = tracker.getGoals().get(index);
        System.out.print("Enter new status: ");
        String newStatus = scanner.nextLine();
        goal.updateStatus(newStatus);
        System.out.println(GREEN + "Goal updated successfully!" + RESET);
    }

    private static void setPersonalStats(Scanner scanner, FitnessTracker tracker) {
        System.out.println(GREEN + "\n--- Personal Stats ---" + RESET);
        System.out.print("Enter current weight (kg): ");
        double weight = scanner.nextDouble();
        System.out.print("Enter waist size (cm): ");
        double waist = scanner.nextDouble();
        System.out.print("Enter chest size (cm): ");
        double chest = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character after double input

        tracker.setPersonalStats(weight, waist, chest);
        System.out.println(GREEN + "\nStats updated successfully!" + RESET);
    }

    private static void viewSavedData() {
        try {
            File file = new File("fitness_data.txt");
            if (!file.exists()) {
                System.out.println(RED + "No saved data found." + RESET);
                return;
            }

            System.out.println(GREEN + "\n--- Saved Data ---\n" + RESET);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(RED + "Error reading saved data." + RESET);
            e.printStackTrace();
        }
    }
}

// ASCII Art class
class ASCIIArt {
    public static void displayLogo() {
        // Use color constants from the FitBroApp class
        System.out.println(FitBroApp.PURPLE +
                " ___________.__  __ ___." + "\n" +
                "\\_   _____/|__|/  |\\_ |_________  ____ " + "\n" +
                " |    __)  |  \\   __\\ __ \\_  __ \\/  _ \\" + "\n" +
                " |     \\   |  ||  | | \\_\\ \\  | \\(  <_> )" + "\n" +
                " \\___  /   |__||__| |___  /__|   \\____/" + "\n" +
                "     \\/                 \\/" + FitBroApp.RESET);
    }
}

// Workout class
class Workout {
    private String exerciseType;
    private int duration, intensity, reps, sets;

    public Workout(String exerciseType, int duration, int intensity, int reps, int sets) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.intensity = intensity;
        this.reps = reps;
        this.sets = sets;
    }

    // Getter methods for private fields
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
        int cals = switch (exerciseType.toLowerCase()) {
            case "running" -> 8;
            case "cycling" -> 6;
            case "weightlifting" -> 5;
            default -> 4;
        };
        return cals * duration * (intensity / 10);
    }

    @Override
    public String toString() {
        return "Exercise: " + exerciseType + ", Duration: " + duration + " mins, Intensity: " + intensity +
                ", Sets: " + sets + ", Reps: " + reps;
    }
}

// Fitness Goal class
class FitnessGoal {
    private String goal, description, status;

    public FitnessGoal(String goal, String description, String status) {
        this.goal = goal;
        this.description = description;
        this.status = status;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return goal + " - Status: " + status + "\n  Description: " + description;
    }
}

// Fitness Tracker
class FitnessTracker {
    private List<Workout> workouts = new ArrayList<>();
    private List<FitnessGoal> goals = new ArrayList<>();
    private double weight, waist, chest;
    private int totalWorkoutTime = 0, totalCaloriesBurned = 0;

    public void logWorkout(Workout workout) {
        workouts.add(workout);
        totalWorkoutTime += workout.getDuration();
        totalCaloriesBurned += workout.calculateCalories();
    }

    public void addGoal(FitnessGoal goal) {
        goals.add(goal);
    }

    public List<FitnessGoal> getGoals() {
        return goals;
    }

    public void displayWorkouts() {
        System.out.println(FitBroApp.CYAN + "\n--- Workout Progress ---" + FitBroApp.RESET);
        System.out.println("Total Workout Time: " + totalWorkoutTime + " minutes");
        System.out.println("Total Calories Burned: " + totalCaloriesBurned);
        for (Workout w : workouts) {
            System.out.println(w);
        }
    }

    public void displayGoals() {
        System.out.println(FitBroApp.CYAN + "\n--- Fitness Goals ---" + FitBroApp.RESET);
        for (int i = 0; i < goals.size(); i++) {
            System.out.println((i + 1) + ". " + goals.get(i));
        }
    }

    public void setPersonalStats(double weight, double waist, double chest) {
        this.weight = weight;
        this.waist = waist;
        this.chest = chest;
    }

    public void saveDataToFile() {
        try {
            System.out.println("\nSaving data...");
    
            FileWriter writer = new FileWriter("fitness_data.txt", true); // <- 'true' enables appending
    
            // Get current date and time
            String timestamp = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm").format(new java.util.Date());
            writer.write("\n--- " + timestamp + " New Entry ---\n");
    
            // Save workout data
            writer.write("--- Workout Logs ---\n");
            for (Workout w : workouts) {
                writer.write(w.getExerciseType() + "," + w.getDuration() + "," + w.getIntensity() + ","
                        + w.getReps() + "," + w.getSets() + "\n");
            }
    
            // Save fitness goals
            writer.write("\n--- Fitness Goals ---\n");
            for (FitnessGoal g : goals) {
                writer.write(g.toString() + "\n");
            }
    
            // Save personal stats
            writer.write("\n--- Personal Stats ---\n");
            writer.write("Weight: " + weight + " kg\n");
            writer.write("Waist: " + waist + " cm\n");
            writer.write("Chest: " + chest + " cm\n");
    
            writer.close();
            System.out.println(FitBroApp.GREEN + "Data saved successfully!" + FitBroApp.RESET);
        } catch (IOException e) {
            System.out.println(FitBroApp.RED + "Error saving data." + FitBroApp.RESET);
            e.printStackTrace();
        }
    }
    
    public void loadDataFromFile() {
        try {
            File file = new File("fitness_data.txt");
            if (!file.exists()) {
                System.out.println(FitBroApp.RED + "No saved data found." + FitBroApp.RESET);
                return;
            }
    
            Scanner reader = new Scanner(file);
            String section = "";
    
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
    
                // Update section
                if (line.equals("--- Workout Logs ---")) {
                    section = "workouts";
                    continue;
                } else if (line.equals("--- Fitness Goals ---")) {
                    section = "goals";
                    continue;
                } else if (line.equals("--- Personal Stats ---")) {
                    section = "stats";
                    continue;
                }
    
                // Parse workout logs
                if (section.equals("workouts") && !line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        try {
                            String exerciseType = parts[0].trim();
                            int duration = Integer.parseInt(parts[1].trim());
                            int intensity = Integer.parseInt(parts[2].trim());
                            int reps = Integer.parseInt(parts[3].trim());
                            int sets = Integer.parseInt(parts[4].trim());
                            workouts.add(new Workout(exerciseType, duration, intensity, reps, sets));
                        } catch (NumberFormatException e) {
                            System.out.println(FitBroApp.YELLOW + "Skipping invalid workout entry: " + line + FitBroApp.RESET);
                        }
                    }
                }
    
                // Parse fitness goals
                if (section.equals("goals") && !line.isEmpty()) {
                    if (line.contains(" - Status: ") && reader.hasNextLine()) {
                        String goalLine = line;
                        String descLine = reader.nextLine().trim();
                        try {
                            String goal = goalLine.split(" - Status: ")[0].trim();
                            String status = goalLine.split(" - Status: ")[1].trim();
                            if (descLine.startsWith("Description: ")) {
                                String description = descLine.substring("Description: ".length()).trim();
                                goals.add(new FitnessGoal(goal, description, status));
                            }
                        } catch (Exception e) {
                            System.out.println(FitBroApp.YELLOW + "Skipping invalid goal entry: " + goalLine + FitBroApp.RESET);
                        }
                    }
                }
    
                // Parse personal stats
                if (section.equals("stats") && !line.isEmpty()) {
                    try {
                        if (line.startsWith("Weight: ")) {
                            this.weight = Double.parseDouble(line.substring("Weight: ".length()).split(" ")[0]);
                        } else if (line.startsWith("Waist: ")) {
                            this.waist = Double.parseDouble(line.substring("Waist: ".length()).split(" ")[0]);
                        } else if (line.startsWith("Chest: ")) {
                            this.chest = Double.parseDouble(line.substring("Chest: ".length()).split(" ")[0]);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(FitBroApp.YELLOW + "Skipping invalid stat line: " + line + FitBroApp.RESET);
                    }
                }
            }
    
            reader.close();
            System.out.println(FitBroApp.GREEN + "Data loaded successfully!" + FitBroApp.RESET);
        } catch (IOException e) {
            System.out.println(FitBroApp.RED + "Error reading saved data." + FitBroApp.RESET);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(FitBroApp.RED + "Error parsing saved data." + FitBroApp.RESET);
            e.printStackTrace();
        }
    }
}