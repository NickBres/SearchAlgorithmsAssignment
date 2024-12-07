import java.io.*;
import java.util.*;

public class FileReaderUtil {

    public static Task readTask(String filePath) throws IOException {
        // Map input algorithm names to Task.Algorithm enum values
        Map<String, Task.Algorithm> algorithmMap = new HashMap<>();
        algorithmMap.put("DFBnB", Task.Algorithm.DFBNB);
        algorithmMap.put("IDA*", Task.Algorithm.IDA_STAR);
        algorithmMap.put("A*", Task.Algorithm.A_STAR);
        algorithmMap.put("DFID", Task.Algorithm.DFID);
        algorithmMap.put("BFS", Task.Algorithm.BFS);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read and process the algorithm name
            String line = reader.readLine().trim();
            Task.Algorithm algorithm = algorithmMap.get(line);
            if (algorithm == null) {
                throw new IllegalArgumentException("Unknown algorithm: " + line);
            }

            // Read the time calculation flag
            line = reader.readLine().trim();
            boolean withTime = line.equalsIgnoreCase("with time");

            // Read the open state printing flag
            line = reader.readLine().trim();
            boolean printOpen = line.equalsIgnoreCase("with open");

            // Read the initial state
            List<String> initialBoardLines = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                initialBoardLines.add(reader.readLine().trim());
            }
            State initialState = State.convertInputToState(initialBoardLines);

            // Skip delimiter line
            reader.readLine();

            // Read the goal state
            List<String> goalBoardLines = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                goalBoardLines.add(reader.readLine().trim());
            }
            State goalState = State.convertInputToState(goalBoardLines);

            return new Task(algorithm, withTime, printOpen, initialState, goalState);
        }
    }
}