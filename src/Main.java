import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.SplittableRandom;

public class Main {

    private static final Planner PLANNER = new Planner();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH.mm");
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                printMenu();
                System.out.print("Выберите пункт меню: ");
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            addTask(scanner);
                            break;
                        case 2:
                            removeTasks(scanner);
                            break;
                        case 3:
                            printTaskForDate(scanner);
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.println("Выберите пункт меню из списка!");
                }
            }
        }
    }


    private static void printMenu() {
        System.out.println(
                """
                        1. Добавить задачу
                        2. Удалить задачу
                        3. Получить задачу на указанный день
                        0. Выход
                        """
        );
    }


    // добавить задачу
    private static void addTask(Scanner scanner) {
        String title = readString("Введите название задачи:", scanner);
        String description = readString("Введите описание задачи:", scanner);
        LocalDateTime taskDate = readDateTime(scanner);
        TaskType taskType = readType(scanner);
        Repeatable repeatable = readRepeatable(scanner);
        Task task = switch (repeatable) {
            case ONCE -> new OnceTask(title,description,taskDate,taskType);
            case DAILY -> new DailyTask(title,description,taskDate,taskType);
            case WEEKLY -> new WeeklyTask(title,description,taskDate,taskType);
            case MONTHLY -> new MonthlyTask(title,description,taskDate,taskType);
            case YEARLY -> new YearlyTask(title,description,taskDate,taskType);
        };
        PLANNER.addTask(task);

    }

    private static Repeatable readRepeatable(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Выберите тип повторяемости: ");
                for (Repeatable repeatable : Repeatable.values()) {
                    System.out.println(repeatable.ordinal() + ", " + localizeRepeatability(repeatable));
                }
                System.out.print("Введите тип повторяемости задачи: ");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return Repeatable.values()[ordinal];
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный тип повторяемости");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Тип повторяемости не найден");
            }
        }
    }


    private static TaskType readType(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Выберите тип задачи: ");
                for (TaskType taskType : TaskType.values()) {
                    System.out.println(taskType.ordinal() + ", " + localizeType(taskType));
                }
                System.out.print("Введите тип задачи: ");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return TaskType.values()[ordinal];
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный номер типа задачи");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Тип задачи не найден");
            }
        }
    }

    private static LocalDateTime readDateTime(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        LocalTime localTime = readTime(scanner);
        return localDate.atTime(localTime);
    }

    private static String readString(String message, Scanner scanner) {
        while (true) {
                System.out.printf(message);
                String readString = scanner.nextLine();
            if (readString == null || readString.isBlank()) {
                System.out.println("Введено пустое значение");
            } else {
                return readString;
            }
        }
    }

    //удалить задачу
    public static void removeTasks(Scanner scanner) {
        System.out.println("Все задачи:");
        for (Task task : PLANNER.getAllTasks()) {
            System.out.printf("%d. %s [%s](%s)%n",
                    task.getId(),
                    task.getTitle(),
                    localizeType(task.getTaskType()),
                    localizeRepeatability(task.getRepeatabilityType()));
        }
        while (true) {
            try {
                System.out.print("Выберите задачу для удаления: ");
                String idLine = scanner.nextLine();
                int id = Integer.parseInt(idLine);
                PLANNER.removeTask(id);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный id задачи");
            } catch (TaskNotFoundException e) {
                System.out.println("Задача для удаления не найдена");
            }
        }
    }

    //получить задачу на указанный день
    public static void printTaskForDate(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        Collection<Task> taskForDate = PLANNER.getTasksForDate(localDate);
        System.out.println("Задачи на " + localDate.format(DATE_FORMAT));
        for (Task task : taskForDate) {
            System.out.printf("[%s]%s: %s (%s)%n",
                    localizeType(task.getTaskType()),
                    task.getTitle(),
                    task.getTaskDateTime().format(TIME_FORMAT),
                    task.getDescription());
        }

    }

    private static LocalDate readDate(Scanner scanner) {
        while (true) {
            try {
                System.out.printf("Введите дату задачи в формате dd.mm.yyyy: ");
                String dateLine = scanner.nextLine();
                return LocalDate.parse(dateLine, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Введена дата в неверном формате");
            }

        }

    }
    private static LocalTime readTime(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Введите время задачи в формате hh.mm : ");
                String dateLine = scanner.nextLine();
                return LocalTime.parse(dateLine, TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Введено время в неверном формате");
            }

        }

    }

    private static String localizeType(TaskType taskType) {
        return switch (taskType) {
            case WORK -> "Рабочая задача";
            case PERSONAL -> "Персональная задача";
            default -> "Неизвестная задача";
        };
    }

    private static String localizeRepeatability(Repeatable repeatable) {
        return switch (repeatable) {
            case ONCE -> "Однокраная задача";
            case DAILY -> "Ежедневная задача";
            case WEEKLY -> "Еженедельная задача";
            case MONTHLY -> "Ежемесячная задача";
            case YEARLY -> "Ежегодная задача";
            default -> "Неизвестно";
        };
    }


}
