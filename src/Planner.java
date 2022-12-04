import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Planner {
    private final Map<Integer, Task> tasks = new HashMap<>();

    // метод добавления задачи
    public void addTask(Task task) {
        this.tasks.put(task.getId(), task);
    }


    //метод удаления задачи
    public void removeTask(int id) throws TaskNotFoundException {
        if (this.tasks.containsKey(id)) {
            this.tasks.remove(id);
        } else {
            throw new TaskNotFoundException();
        }

    }

    // получить все задачи
    public Collection<Task> getAllTasks() {
        return this.tasks.values();
    }



    // получить задачи на день
    public Collection<Task> getTasksForDate(LocalDate date){
        TreeSet<Task> tasksForDate = new TreeSet<>();
        for (Task task : tasks.values()) {
            if (task.appearsIn(date)) {
                tasksForDate.add(task);
            }
        }
        return tasksForDate;
    }


}
