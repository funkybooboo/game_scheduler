package task;

public abstract class Task implements Comparable<Task> {
    public int ID;
    public int start;
    public int deadline;
    public int duration;


    public Task(int ID, int start, int deadline, int duration) {
        this.ID = ID;
        this.start = start;
        this.deadline = deadline;
        this.duration = duration;
    }

    public String toString() {
        return "Task " + ID + " [" + start + "-" + deadline + "] " + duration;
    }

}
