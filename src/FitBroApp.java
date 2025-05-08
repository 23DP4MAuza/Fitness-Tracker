package src;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        tracker.loadDataFromFile();

        while (true) {
            clearScreen();
            ASCIIArt.displayLogo();

            System.out.println(BLUE + "\n--- Workout ---" + RESET);
            System.out.println("1. Log workout");
            System.out.println("2. View workout progress");
            System.out.println("3. Edit workout");
            System.out.println("4. Delete workout");

            System.out.println(CYAN + "\n-- Personal --" + RESET);
            System.out.println("5. View personal stats");
            System.out.println("6. Edit personal stats");
            System.out.println("7. Set Fitness goals");
            System.out.println("8. Display Fitness goals");
            System.out.println("9. Edit Fitness goals");
            System.out.println("10. Delete Fitness goal");

            System.out.println(PURPLE + "\n----------------------" + RESET);
            System.out.println("11. Save Data");
            System.out.println("12. View/Filter/Sort Data");
            System.out.println("13. Exit");

            System.out.print(YELLOW + "\nEnter your choice: " + RESET);

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println(RED + "Invalid input! Please enter a number between 1 and 13." + RESET);
                scanner.nextLine();
                waitForEnter(scanner);
                continue;
            }
            scanner.nextLine();

            clearScreen();

            switch (choice) {
                case 1 -> logWorkout(scanner, tracker);
                case 2 -> tracker.displayWorkouts();
                case 3 -> editWorkout(scanner, tracker);
                case 4 -> deleteWorkout(scanner, tracker);
                case 5 -> tracker.displayPersonalStats();
                case 6 -> editPersonalStats(scanner, tracker);
                case 7 -> setFitnessGoals(scanner, tracker);
                case 8 -> tracker.displayGoals();
                case 9 -> editFitnessGoal(scanner, tracker);
                case 10 -> deleteFitnessGoal(scanner, tracker);
                case 11 -> tracker.saveDataToFile();
                case 12 -> dataAnalysisMenu(scanner);
                case 13 -> {
                    System.out.println(RED + "\nExiting... Goodbye!" + RESET);
                    return;
                }
                default -> System.out.println(RED + "Invalid choice, please enter a number from 1 to 13." + RESET);
            }

            waitForEnter(scanner);
        }
    }

    private static void dataAnalysisMenu(Scanner scanner) {
        List<Workout> workouts = readWorkoutsFromFile();
        if (workouts.isEmpty()) {
            System.out.println(RED + "No workouts found in file!" + RESET);
            return;
        }

        while (true) {
            clearScreen();
            System.out.println(CYAN + "\n--- Data Analysis Menu ---" + RESET);
            System.out.println("1. Sort workouts");
            System.out.println("2. Filter workouts");
            System.out.println("3. View raw data");
            System.out.println("4. Back to main menu");
            System.out.print(YELLOW + "\nEnter your choice: " + RESET);

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\nSort by:");
                    System.out.println("1. Reps (high to low)");
                    System.out.println("2. Intensity (high to low)");
                    System.out.println("3. Sets (high to low)");
                    System.out.println("4. Duration (high to low)");
                    System.out.print(YELLOW + "Enter sorting criteria: " + RESET);
                    
                    int sortChoice = scanner.nextInt();
                    scanner.nextLine();
                    
                    switch (sortChoice) {
                        case 1 -> sortWorkouts(workouts, "reps", true);
                        case 2 -> sortWorkouts(workouts, "intensity", true);
                        case 3 -> sortWorkouts(workouts, "sets", true);
                        case 4 -> sortWorkouts(workouts, "duration", true);
                        default -> System.out.println(RED + "Invalid choice!" + RESET);
                    }
                    waitForEnter(scanner);
                }
                case 2 -> {
                    System.out.println("\nFilter by:");
                    System.out.println("1. Reps range");
                    System.out.println("2. Intensity range");
                    System.out.println("3. Sets range");
                    System.out.println("4. Minimum duration");
                    System.out.print(YELLOW + "Enter filtering criteria: " + RESET);
                    
                    int filterChoice = scanner.nextInt();
                    scanner.nextLine();
                    
                    switch (filterChoice) {
                        case 1 -> {
                            System.out.print("Enter minimum reps: ");
                            int min = scanner.nextInt();
                            System.out.print("Enter maximum reps: ");
                            int max = scanner.nextInt();
                            scanner.nextLine();
                            filterWorkouts(workouts, "reps", min, max);
                        }
                        case 2 -> {
                            System.out.print("Enter minimum intensity (1-10): ");
                            int min = scanner.nextInt();
                            System.out.print("Enter maximum intensity (1-10): ");
                            int max = scanner.nextInt();
                            scanner.nextLine();
                            filterWorkouts(workouts, "intensity", min, max);
                        }
                        case 3 -> {
                            System.out.print("Enter minimum sets: ");
                            int min = scanner.nextInt();
                            System.out.print("Enter maximum sets: ");
                            int max = scanner.nextInt();
                            scanner.nextLine();
                            filterWorkouts(workouts, "sets", min, max);
                        }
                        case 4 -> {
                            System.out.print("Enter minimum duration (minutes): ");
                            int min = scanner.nextInt();
                            scanner.nextLine();
                            filterWorkouts(workouts, "duration", min, 0);
                        }
                        default -> System.out.println(RED + "Invalid choice!" + RESET);
                    }
                    waitForEnter(scanner);
                }
                case 3 -> {
                    viewRawDataFile();
                    waitForEnter(scanner);
                }
                case 4 -> {
                    return;
                }
                default -> System.out.println(RED + "Invalid choice!" + RESET);
            }
        }
    }

    private static List<Workout> readWorkoutsFromFile() {
        List<Workout> workouts = new ArrayList<>();
        try {
            File file = new File("fitness_data.txt");
            if (!file.exists()) return workouts;

            try (Scanner scanner = new Scanner(file)) {
                boolean inWorkoutsSection = false;
                
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    
                    if (line.startsWith("--- Workouts ---")) {
                        inWorkoutsSection = true;
                        continue;
                    } else if (line.startsWith("--- ")) {
                        inWorkoutsSection = false;
                        continue;
                    }

                    if (inWorkoutsSection && !line.isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length == 5) {
                            workouts.add(new Workout(
                                parts[0].trim(),
                                Integer.parseInt(parts[1].trim()),
                                Integer.parseInt(parts[2].trim()),
                                Integer.parseInt(parts[3].trim()),
                                Integer.parseInt(parts[4].trim())
                            ));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(RED + "Error reading workouts from file: " + e.getMessage() + RESET);
        }
        return workouts;
    }

    private static void sortWorkouts(List<Workout> workouts, String criteria, boolean descending) {
        switch (criteria.toLowerCase()) {
            case "reps" -> workouts.sort((w1, w2) -> descending ? 
                Integer.compare(w2.getReps(), w1.getReps()) : 
                Integer.compare(w1.getReps(), w2.getReps()));
            case "intensity" -> workouts.sort((w1, w2) -> descending ? 
                Integer.compare(w2.getIntensity(), w1.getIntensity()) : 
                Integer.compare(w1.getIntensity(), w2.getIntensity()));
            case "sets" -> workouts.sort((w1, w2) -> descending ? 
                Integer.compare(w2.getSets(), w1.getSets()) : 
                Integer.compare(w1.getSets(), w2.getSets()));
            case "duration" -> workouts.sort((w1, w2) -> descending ? 
                Integer.compare(w2.getDuration(), w1.getDuration()) : 
                Integer.compare(w1.getDuration(), w2.getDuration()));
        }

        System.out.println(CYAN + "\n--- Sorted Workouts (" + criteria + 
                         ", " + (descending ? "high to low" : "low to high") + ") ---" + RESET);
        workouts.forEach(System.out::println);
    }

    private static void filterWorkouts(List<Workout> workouts, String criteria, int min, int max) {
        List<Workout> filtered = new ArrayList<>();
        
        switch (criteria.toLowerCase()) {
            case "reps" -> filtered = workouts.stream()
                .filter(w -> w.getReps() >= min && (max == 0 || w.getReps() <= max))
                .collect(Collectors.toList());
            case "intensity" -> filtered = workouts.stream()
                .filter(w -> w.getIntensity() >= min && (max == 0 || w.getIntensity() <= max))
                .collect(Collectors.toList());
            case "sets" -> filtered = workouts.stream()
                .filter(w -> w.getSets() >= min && (max == 0 || w.getSets() <= max))
                .collect(Collectors.toList());
            case "duration" -> filtered = workouts.stream()
                .filter(w -> w.getDuration() >= min && (max == 0 || w.getDuration() <= max))
                .collect(Collectors.toList());
        }

        System.out.println(CYAN + "\n--- Filtered Workouts (" + criteria + 
                         (max > 0 ? " between " + min + " and " + max : " >= " + min) + ") ---" + RESET);
        if (filtered.isEmpty()) {
            System.out.println(YELLOW + "No workouts match the criteria." + RESET);
        } else {
            filtered.forEach(System.out::println);
        }
    }

    private static void viewRawDataFile() {
        try {
            File file = new File("fitness_data.txt");
            if (!file.exists()) {
                System.out.println(RED + "No saved data found." + RESET);
                return;
            }

            System.out.println(GREEN + "\n--- Raw Data File Contents ---\n" + RESET);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(RED + "Error reading saved data." + RESET);
        }
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.println(YELLOW + "\nPress Enter to continue..." + RESET);
        scanner.nextLine();
    }

    private static void logWorkout(Scanner scanner, FitnessTracker tracker) {
        System.out.println(GREEN + "\n--- Log Your Workout ---" + RESET);
        
        String exerciseType;
        do {
            System.out.print("Enter the type of exercise (letters only, e.g., Running, Cycling): ");
            exerciseType = scanner.nextLine().trim();
            if (!exerciseType.matches("[a-zA-Z ]+")) {
                System.out.println(RED + "Invalid input! Please use letters only." + RESET);
            }
        } while (exerciseType.isEmpty() || !exerciseType.matches("[a-zA-Z ]+"));

        int duration = 0;
        while (duration <= 0) {
            System.out.print("Enter the duration in minutes (1+): ");
            try {
                duration = scanner.nextInt();
                if (duration <= 0) {
                    System.out.println(RED + "Duration must be at least 1 minute." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a valid number." + RESET);
                scanner.next();
            }
        }
        
        int intensity = 0;
        while (intensity < 1 || intensity > 10) {
            System.out.print("Enter intensity (1-10): ");
            try {
                intensity = scanner.nextInt();
                if (intensity < 1 || intensity > 10) {
                    System.out.println(RED + "Intensity must be between 1 and 10." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a number between 1 and 10." + RESET);
                scanner.next();
            }
        }
        
        int reps = 0;
        while (reps <= 0) {
            System.out.print("Enter number of reps (1+): ");
            try {
                reps = scanner.nextInt();
                if (reps <= 0) {
                    System.out.println(RED + "Reps must be at least 1." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a valid number." + RESET);
                scanner.next();
            }
        }
        
        int sets = 0;
        while (sets <= 0) {
            System.out.print("Enter number of sets (1+): ");
            try {
                sets = scanner.nextInt();
                if (sets <= 0) {
                    System.out.println(RED + "Sets must be at least 1." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a valid number." + RESET);
                scanner.next();
            }
        }
        scanner.nextLine();

        tracker.logWorkout(new Workout(exerciseType, duration, intensity, reps, sets));
        System.out.println(GREEN + "\nWorkout Logged Successfully!" + RESET);
    }

    private static void editWorkout(Scanner scanner, FitnessTracker tracker) {
        tracker.displayWorkouts();
        if (tracker.getWorkouts().isEmpty()) {
            System.out.println(RED + "No workouts to edit." + RESET);
            return;
        }
        
        System.out.print("\nEnter the number of the workout to edit: ");
        int index = -1;
        while (index < 0 || index >= tracker.getWorkouts().size()) {
            try {
                index = scanner.nextInt() - 1;
                if (index < 0 || index >= tracker.getWorkouts().size()) {
                    System.out.print(RED + "Invalid number. Please enter between 1-" + 
                        tracker.getWorkouts().size() + ": " + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.print(RED + "Please enter a valid number: " + RESET);
                scanner.next();
            }
        }
        scanner.nextLine();

        Workout workout = tracker.getWorkouts().get(index);
        System.out.println("Current workout: " + workout);
        
        String type;
        do {
            System.out.print("Enter new exercise type (cannot be blank, enter '-' to keep current): ");
            type = scanner.nextLine().trim();
            if (type.equals("-")) {
                type = workout.getExerciseType();
                break;
            }
        } while (type.isEmpty());
        workout.setExerciseType(type);
        
        int duration = -1;
        while (duration < 0) {
            System.out.print("Enter new duration (minimum 1 minute, 0 to keep current): ");
            try {
                duration = scanner.nextInt();
                if (duration == 0) {
                    duration = workout.getDuration();
                } else if (duration < 1) {
                    System.out.println(RED + "Duration must be at least 1 minute" + RESET);
                    duration = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a valid number" + RESET);
                scanner.next();
            }
        }
        scanner.nextLine();
        
        int intensity = -1;
        while (intensity < 0) {
            System.out.print("Enter new intensity (1-10, 0 to keep current): ");
            try {
                intensity = scanner.nextInt();
                if (intensity == 0) {
                    intensity = workout.getIntensity();
                } else if (intensity < 1 || intensity > 10) {
                    System.out.println(RED + "Intensity must be between 1-10" + RESET);
                    intensity = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a valid number" + RESET);
                scanner.next();
            }
        }
        scanner.nextLine();
        
        int reps = -1;
        while (reps < 0) {
            System.out.print("Enter new reps (minimum 1, 0 to keep current): ");
            try {
                reps = scanner.nextInt();
                if (reps == 0) {
                    reps = workout.getReps();
                } else if (reps < 1) {
                    System.out.println(RED + "Reps must be at least 1" + RESET);
                    reps = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a valid number" + RESET);
                scanner.next();
            }
        }
        scanner.nextLine();
        
        int sets = -1;
        while (sets < 0) {
            System.out.print("Enter new sets (minimum 1, 0 to keep current): ");
            try {
                sets = scanner.nextInt();
                if (sets == 0) {
                    sets = workout.getSets();
                } else if (sets < 1) {
                    System.out.println(RED + "Sets must be at least 1" + RESET);
                    sets = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Please enter a valid number" + RESET);
                scanner.next();
            }
        }
        scanner.nextLine();

        tracker.recalculateTotals();
        System.out.println(GREEN + "Workout updated successfully!" + RESET);
    }

    private static void deleteWorkout(Scanner scanner, FitnessTracker tracker) {
        tracker.displayWorkouts();
        if (tracker.getWorkouts().isEmpty()) {
            System.out.println(RED + "No workouts to delete." + RESET);
            return;
        }
        
        System.out.print("\nEnter the number of the workout to delete: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (tracker.deleteWorkout(index)) {
            System.out.println(GREEN + "Workout deleted successfully!" + RESET);
        } else {
            System.out.println(RED + "Invalid workout number." + RESET);
        }
    }

    private static void setFitnessGoals(Scanner scanner, FitnessTracker tracker) {
        System.out.println(GREEN + "\n--- Set Your Fitness Goal ---" + RESET);
        System.out.print("Enter goal: ");
        String goal = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter status (Not Started/Started/Completed): ");
        String status = scanner.nextLine();

        tracker.addGoal(new FitnessGoal(goal, description, status));
        System.out.println(GREEN + "\nGoal added successfully!" + RESET);
    }

    private static void editFitnessGoal(Scanner scanner, FitnessTracker tracker) {
        tracker.displayGoals();
        if (tracker.getGoals().isEmpty()) {
            System.out.println(RED + "No goals to edit." + RESET);
            return;
        }
        
        System.out.print("\nEnter the number of the goal to edit: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

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

    private static void deleteFitnessGoal(Scanner scanner, FitnessTracker tracker) {
        tracker.displayGoals();
        if (tracker.getGoals().isEmpty()) {
            System.out.println(RED + "No goals to delete." + RESET);
            return;
        }
        
        System.out.print("\nEnter the number of the goal to delete: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (tracker.deleteGoal(index)) {
            System.out.println(GREEN + "Goal deleted successfully!" + RESET);
        } else {
            System.out.println(RED + "Invalid goal number." + RESET);
        }
    }

    private static void editPersonalStats(Scanner scanner, FitnessTracker tracker) {
        tracker.displayPersonalStats();
        System.out.println(GREEN + "\n--- Edit Personal Stats ---" + RESET);
        
        double weight = 0;
        while (true) {
            System.out.print("Enter new weight (0 to keep current): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(RED + "Error: Please input a value (cannot be blank)" + RESET);
                continue;
            }
            try {
                weight = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Error: Please enter a valid number" + RESET);
            }
        }
        if (weight > 0) tracker.setWeight(weight);
        
        double waist = 0;
        while (true) {
            System.out.print("Enter new waist size (0 to keep current): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(RED + "Error: Please input a value (cannot be blank)" + RESET);
                continue;
            }
            try {
                waist = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Error: Please enter a valid number" + RESET);
            }
        }
        if (waist > 0) tracker.setWaist(waist);
        
        double chest = 0;
        while (true) {
            System.out.print("Enter new chest size (0 to keep current): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(RED + "Error: Please input a value (cannot be blank)" + RESET);
                continue;
            }
            try {
                chest = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Error: Please enter a valid number" + RESET);
            }
        }
        if (chest > 0) tracker.setChest(chest);

        System.out.println(GREEN + "Personal stats updated successfully!" + RESET);
    }

    static class ASCIIArt {
        public static void displayLogo() {
            System.out.println(PURPLE +
                " ___________.__  __ ___." + "\n" +
                "\\_   _____/|__|/  |\\_ |_________  ____ " + "\n" +
                " |    __)  |  \\   __\\ __ \\_  __ \\/  _ \\" + "\n" +
                " |     \\   |  ||  | | \\_\\ \\  | \\(  <_> )" + "\n" +
                " \\___  /   |__||__| |___  /__|   \\____/" + "\n" +
                "     \\/                 \\/" + RESET);
        }
    }

    static class Workout {
        private String exerciseType;
        private int duration, intensity, reps, sets;

        public Workout(String exerciseType, int duration, int intensity, int reps, int sets) {
            this.exerciseType = exerciseType;
            this.duration = duration;
            this.intensity = intensity;
            this.reps = reps;
            this.sets = sets;
        }

        public String getExerciseType() { return exerciseType; }
        public int getDuration() { return duration; }
        public int getIntensity() { return intensity; }
        public int getReps() { return reps; }
        public int getSets() { return sets; }

        public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }
        public void setDuration(int duration) { this.duration = duration; }
        public void setIntensity(int intensity) { this.intensity = intensity; }
        public void setReps(int reps) { this.reps = reps; }
        public void setSets(int sets) { this.sets = sets; }

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

    static class FitnessGoal {
        private String goal, description, status;

        public FitnessGoal(String goal, String description, String status) {
            this.goal = goal;
            this.description = description;
            this.status = status;
        }

        public void updateStatus(String status) { this.status = status; }
        public String getGoal() { return goal; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }

        @Override
        public String toString() {
            return goal + " - Status: " + status + "\n  Description: " + description;
        }
    }

    static class FitnessTracker {
        private List<Workout> workouts = new ArrayList<>();
        private List<FitnessGoal> goals = new ArrayList<>();
        private double weight, waist, chest;
        private int totalWorkoutTime = 0, totalCaloriesBurned = 0;

        public void logWorkout(Workout workout) {
            workouts.add(workout);
            totalWorkoutTime += workout.getDuration();
            totalCaloriesBurned += workout.calculateCalories();
        }

        public void addGoal(FitnessGoal goal) { goals.add(goal); }
        public List<FitnessGoal> getGoals() { return goals; }
        public List<Workout> getWorkouts() { return workouts; }

        public boolean deleteWorkout(int index) {
            if (index >= 0 && index < workouts.size()) {
                Workout removed = workouts.remove(index);
                totalWorkoutTime -= removed.getDuration();
                totalCaloriesBurned -= removed.calculateCalories();
                return true;
            }
            return false;
        }

        public boolean deleteGoal(int index) {
            if (index >= 0 && index < goals.size()) {
                goals.remove(index);
                return true;
            }
            return false;
        }

        public void displayWorkouts() {
            System.out.println(CYAN + "\n--- Workout Progress ---" + RESET);
            System.out.println("Total Workout Time: " + totalWorkoutTime + " minutes");
            System.out.println("Total Calories Burned: " + totalCaloriesBurned);
            for (int i = 0; i < workouts.size(); i++) {
                System.out.println((i + 1) + ". " + workouts.get(i));
            }
        }

        public void displayGoals() {
            System.out.println(CYAN + "\n--- Fitness Goals ---" + RESET);
            for (int i = 0; i < goals.size(); i++) {
                System.out.println((i + 1) + ". " + goals.get(i));
            }
        }

        public void setPersonalStats(double weight, double waist, double chest) {
            this.weight = weight;
            this.waist = waist;
            this.chest = chest;
        }

        public void displayPersonalStats() {
            System.out.println(CYAN + "\n--- Personal Stats ---" + RESET);
            System.out.println("Weight: " + weight + " kg");
            System.out.println("Waist: " + waist + " cm");
            System.out.println("Chest: " + chest + " cm");
        }

        public void setWeight(double weight) { this.weight = weight; }
        public void setWaist(double waist) { this.waist = waist; }
        public void setChest(double chest) { this.chest = chest; }

        public void recalculateTotals() {
            totalWorkoutTime = 0;
            totalCaloriesBurned = 0;
            for (Workout w : workouts) {
                totalWorkoutTime += w.getDuration();
                totalCaloriesBurned += w.calculateCalories();
            }
        }

        public void saveDataToFile() {
            try {
                List<String> existingWorkouts = new ArrayList<>();
                List<String> existingGoals = new ArrayList<>();
                List<String> existingStats = new ArrayList<>();
                
                File file = new File("fitness_data.txt");
                if (file.exists()) {
                    try (Scanner scanner = new Scanner(file)) {
                        String section = "";
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine().trim();
                            if (line.startsWith("--- Workouts ---")) {
                                section = "workouts";
                                continue;
                            } else if (line.startsWith("--- Goals ---")) {
                                section = "goals";
                                continue;
                            } else if (line.startsWith("--- Stats ---")) {
                                section = "stats";
                                continue;
                            } else if (line.startsWith("--- ")) {
                                section = "";
                                continue;
                            }
                            
                            if (!line.isEmpty()) {
                                switch (section) {
                                    case "workouts" -> existingWorkouts.add(line);
                                    case "goals" -> existingGoals.add(line);
                                    case "stats" -> existingStats.add(line);
                                }
                            }
                        }
                    }
                }

                try (FileWriter writer = new FileWriter("fitness_data.txt")) {
                    String timestamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
                    writer.write("--- " + timestamp + " ---\n");

                    writer.write("--- Workouts ---\n");
                    for (String workout : existingWorkouts) {
                        writer.write(workout + "\n");
                    }
                    for (Workout w : workouts) {
                        writer.write(w.getExerciseType() + "," + w.getDuration() + "," + 
                                   w.getIntensity() + "," + w.getReps() + "," + w.getSets() + "\n");
                    }

                    writer.write("\n--- Goals ---\n");
                    for (String goal : existingGoals) {
                        writer.write(goal + "\n");
                    }
                    for (FitnessGoal g : goals) {
                        writer.write(g.getGoal() + "|" + g.getDescription() + "|" + g.getStatus() + "\n");
                    }

                    writer.write("\n--- Stats ---\n");
                    if (!existingStats.isEmpty()) {
                        writer.write(existingStats.get(existingStats.size()-1) + "\n");
                    } else {
                        writer.write("Weight:" + weight + "\n");
                        writer.write("Waist:" + waist + "\n");
                        writer.write("Chest:" + chest + "\n");
                    }
                }

                System.out.println(GREEN + "Data saved successfully!" + RESET);
            } catch (IOException e) {
                System.out.println(RED + "Error saving data: " + e.getMessage() + RESET);
            }
        }

        public void loadDataFromFile() {
            File file = new File("fitness_data.txt");
            if (!file.exists()) return;

            try (Scanner scanner = new Scanner(file)) {
                String section = "";
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    
                    if (line.startsWith("--- ")) {
                        if (line.contains("Workouts")) section = "workouts";
                        else if (line.contains("Goals")) section = "goals";
                        else if (line.contains("Stats")) section = "stats";
                        continue;
                    }

                    switch (section) {
                        case "workouts":
                            if (!line.isEmpty()) {
                                String[] parts = line.split(",");
                                if (parts.length == 5) {
                                    workouts.add(new Workout(
                                        parts[0].trim(),
                                        Integer.parseInt(parts[1].trim()),
                                        Integer.parseInt(parts[2].trim()),
                                        Integer.parseInt(parts[3].trim()),
                                        Integer.parseInt(parts[4].trim())
                                    ));
                                }
                            }
                            break;
                        case "goals":
                            if (!line.isEmpty()) {
                                String[] parts = line.split("\\|");
                                if (parts.length == 3) {
                                    goals.add(new FitnessGoal(
                                        parts[0].trim(),
                                        parts[1].trim(),
                                        parts[2].trim()
                                    ));
                                }
                            }
                            break;
                        case "stats":
                            if (line.startsWith("Weight:")) {
                                weight = Double.parseDouble(line.substring(7).trim());
                            } else if (line.startsWith("Waist:")) {
                                waist = Double.parseDouble(line.substring(6).trim());git push origin main

                            } else if (line.startsWith("Chest:")) {
                                chest = Double.parseDouble(line.substring(6).trim());
                            }
                            break;
                    }
                }
                recalculateTotals();
            } catch (IOException e) {
                System.out.println(RED + "Error loading data: " + e.getMessage() + RESET);
            }
        }
    }
}