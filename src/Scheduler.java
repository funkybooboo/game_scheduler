import heap.leftist_heap.LeftistHeap;
import heap.leftist_heap.MinLeftistHeap;
import task.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Scheduler {

    public static void readTasks(String filename, List<Task> tasks1, List<Task> tasks2, List<Task> tasks3) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        int ID = 0;
        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().strip().replace("\t", " ").split(" ");
            int start = Integer.parseInt(line[0]);
            int deadline = Integer.parseInt(line[1]);
            int duration = Integer.parseInt(line[2]);
            tasks1.add(new Task1(ID, start, deadline, duration));
            tasks2.add(new Task2(ID, start, deadline, duration));
            tasks3.add(new Task3(ID, start, deadline, duration));
            ID++;
        }
    }

    public void makeSchedule(String title, List<Task> tasks) {
        System.out.println();
        System.out.println(title);
        List<List<Task>> taskStartTimes = new ArrayList<>();
        LeftistHeap<Task> queue = new MinLeftistHeap<>();
        fillTaskStartTimes(tasks, taskStartTimes);
        schedule(queue, taskStartTimes);

    }

    private static void schedule(LeftistHeap<Task> queue, List<List<Task>> taskStartTimes) {
        for (Task task : taskStartTimes.getFirst()) {
                queue.insert(task);
        }
        int lateCount = 0;
        int time = 1;
        while (!queue.isEmpty()) {
            if (time != 1 && time < taskStartTimes.size()) {
                for (Task task : taskStartTimes.get(time-1)) {
                    queue.insert(task);
                }
            }
            Task task = queue.delete();
            System.out.print("Time " + time + ": " + task);
            task.duration--;
            if (task.duration > 0) {
                queue.insert(task);
            }
            else {
                System.out.print(" *");
                if (time > task.deadline) {
                    System.out.print(" Late");
                    lateCount++;
                }
            }
            System.out.println();
            time++;
        }
        System.out.println("Number Late: " + lateCount);
    }

    private static void fillTaskStartTimes(List<Task> tasks, List<List<Task>> taskStartTimes) {
        int maxStartTime = 0;
        for (Task task : tasks) {
            int start = task.start;
            if (start > maxStartTime) maxStartTime = start;
        }
        for (int i = 0; i < maxStartTime; i++) {
            taskStartTimes.add(new ArrayList<>());
        }
        for (Task task : tasks) {
            List<Task> taskTime = taskStartTimes.get(task.start-1);
            taskTime.add(task);
        }
    }

    public static void main(String args[]) {
        Scheduler scheduler = new Scheduler();
        String [] filenames = {"data/tasksetA.txt", "data/tasksetB.txt", "data/tasksetC.txt", "data/tasksetD.txt", "data/tasksetE.txt"};
        try {
            for (String filename : filenames) {
                List<Task> tasks1 = new ArrayList<>();    // elements are Task1
                List<Task> tasks2 = new ArrayList<>();    // elements are Task2
                List<Task> tasks3 = new ArrayList<>();    // elements are Task3
                readTasks(filename, tasks1, tasks2, tasks3);
                scheduler.makeSchedule("Deadline Priority "+ filename, tasks1);
                scheduler.makeSchedule("Start time Priority " + filename, tasks2);
                scheduler.makeSchedule("Duration priority " + filename, tasks3);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: ");
            System.out.println(e.toString());
        }
    }
}
