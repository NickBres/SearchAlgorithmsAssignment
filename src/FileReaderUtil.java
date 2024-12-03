import java.io.*;
import java.util.*;

public class FileReaderUtil {

    public static List<Task> readTasks(String filePath) throws IOException {
        List<Task> tasks = new ArrayList<>();

        // Map input algorithm names to Task.Algorithm enum values
        Map<String, Task.Algorithm> algorithmMap = new HashMap<>();
        algorithmMap.put("DFBnB", Task.Algorithm.DFBNB);
        algorithmMap.put("IDA*", Task.Algorithm.IDA_STAR);
        algorithmMap.put("A*", Task.Algorithm.A_STAR);
        algorithmMap.put("DFID", Task.Algorithm.DFID);
        algorithmMap.put("BFS", Task.Algorithm.BFS);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Skip blank lines

                // Algorithm name
                Task.Algorithm algorithm = algorithmMap.get(line);
                if (algorithm == null) {
                    throw new IllegalArgumentException("Unknown algorithm: " + line);
                }

                // Time calculation flag
                line = reader.readLine().trim();
                boolean withTime = line.equalsIgnoreCase("with time");

                // Open state printing flag
                line = reader.readLine().trim();
                boolean printOpen = line.equalsIgnoreCase("with open");

                // Initial state
                List<String> initialBoardLines = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    initialBoardLines.add(reader.readLine().trim());
                }
                State initialState = State.convertInputToState(initialBoardLines);

                // Skip delimiter line
                reader.readLine();

                // Goal state
                List<String> goalBoardLines = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    goalBoardLines.add(reader.readLine().trim());
                }
                State goalState = State.convertInputToState(goalBoardLines);

                // Create Task and add to the list
                tasks.add(new Task(algorithm, withTime, printOpen, initialState, goalState));
            }
        }

        return tasks;
    }
}
