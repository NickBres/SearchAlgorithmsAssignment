import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Ex1 {

    static boolean  WITH_NAME = true;

    public static void main(String[] args) {

        String filePath = "input.txt";
        String outputFilePath = "output.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            List<Task> tasks = FileReaderUtil.readTasks(filePath);

            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                runTask(task,writer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runTask(Task task, BufferedWriter writer){
        long startTime;
        long endTime;
        String result;
        try {
            Node startNode = new Node(task.initialState);

            if(task.algorithm == Task.Algorithm.BFS) {
                startTime = System.nanoTime();
                result = BFS.search(startNode, task.goalState, task.printOpen);
                endTime = System.nanoTime();
            }else if(task.algorithm == Task.Algorithm.DFID){
                startTime = System.nanoTime();
                result = DFID.search(startNode, task.goalState);
                endTime = System.nanoTime();
            }else if(task.algorithm == Task.Algorithm.A_STAR){
                startTime = System.nanoTime();
                result = A_STAR.search(startNode, task.goalState,task.printOpen);
                endTime = System.nanoTime();
            }else if(task.algorithm == Task.Algorithm.IDA_STAR){
                startTime = System.nanoTime();
                result = IDA_STAR.search(startNode, task.goalState);
                endTime = System.nanoTime();
            }else if(task.algorithm == Task.Algorithm.DFBNB){
                startTime = System.nanoTime();
                result = DFBnB.search(startNode, task.goalState);
                endTime = System.nanoTime();
            }else{
                throw new RuntimeException("Wrong algorithm");
            }

            double durationInSeconds = (endTime - startTime) / 1e9; // Convert to seconds
            if(WITH_NAME){
                writer.write(task.algorithm.toString());
                writer.newLine();
            }
            writer.write(result);
            writer.newLine();

            // Print execution time if the withTime flag is set
            if (task.withTime) {
                writer.write(String.format("%.6f seconds", durationInSeconds));
                writer.newLine();
            }

            writer.newLine(); // Add a blank line between tasks in the file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
