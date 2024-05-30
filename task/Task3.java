package task;

public class Task3 extends Task {
    public Task3(int ID, int start, int deadline, int duration) {
        super(ID,start,deadline,duration);
    }
    @Override
    public int compareTo(Task t) {
         return duration-t.duration;
      }

}
