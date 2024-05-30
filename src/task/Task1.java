package task;

public class Task1 extends Task {
    public Task1(int ID, int start, int deadline, int duration) {
        super(ID,start,deadline,duration);
    }
    @Override
    public int compareTo(Task t) {
         return deadline-t.deadline;
      }

}
