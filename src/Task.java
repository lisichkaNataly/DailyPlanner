import java.time.LocalDate;
import java.time.LocalDateTime;
 public abstract class Task implements Comparable <Task>{
     private static int idCounter = 0;
     private final int id;
    private  final String title;
    private final String description;
    private final LocalDateTime taskDateTime;
    private final TaskType taskType;

     public Task(String title, String description, LocalDateTime taskDateTime, TaskType taskType) {
         this.id = idCounter++;
         this.title = title;
         this.description = description;
         this.taskDateTime = taskDateTime;
         this.taskType = taskType;
     }

     public int getId() {
         return id;
     }

     public String getTitle() {
         return title;
     }

     public String getDescription() {
         return description;
     }

     public TaskType getTaskType() {
         return taskType;
     }

     public LocalDateTime getTaskDateTime() {
         return taskDateTime;
     }

     public abstract boolean appearsIn(LocalDate localDate);

     public abstract Repeatable getRepeatabilityType();

     @Override
     public int compareTo(Task otherTask) {
         if (otherTask == null) {
             return 1;
         }
         return this.taskDateTime.toLocalTime().compareTo(otherTask.taskDateTime.toLocalTime());
     }
 }


