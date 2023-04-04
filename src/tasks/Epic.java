package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс создания объектов эпика
 * Класс - наследник от Task
 */


public class Epic extends Task {
    //Хранение в списке всех подзадач эпика
    private final List<Subtask> subtasks = new ArrayList<>();
    //дата и время окончания эпика
    private LocalDateTime endTime;

    //Создание с id
    public Epic(Integer id, String name, String descriptionTask, Status status) {
        super(id, name, descriptionTask, status);
    }

    //Создание с описанием
    public Epic(String name, String descriptionTask) {
        super(name, descriptionTask);
    }

    //Создание без описания
    public Epic(String name) {
        super(name);
    }

    //обновление
    public Epic(String name, String descriptionTask, Status status) {
        super(name, descriptionTask, status);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    //Метод установки статуса Эпика
    public void setStatusEpic() {
        boolean cheakStatus = false;
        // Если подзадачи отстутсвтвуют устанавливаем статус NEW и завершаем метод
        if (subtasks.size() == 0) {
            status = Status.NEW;
            return;
        }
        //Проходим циклом по задачам
        for (Subtask subtask : subtasks) {
            //если хоть одна задача находится в прогрессе, переключаем cheakStatus на true, изначально false.
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                cheakStatus = true;
                break;
            }
        }
        //Если хоть одна задача была в прогрессе, то устанавливаем статус "IN_PROGRESS", т.к. cheakStatus = true
        if (cheakStatus) {
            status = Status.IN_PROGRESS;
            //Обрабатываем остальные случаи
        } else {
            boolean allTaskIsNew = true;
            boolean allTaskIsDone = true;
            //В остальных случаях, снова идем циклом по задачам
            for (Subtask subtask : subtasks) {
                //Если есть хоть 1 подзадача не NEW, устанавливаем false
                if (!(subtask.getStatus().equals(Status.NEW))) {
                    allTaskIsNew = false;
                }
                //Если есть не DONE устанавливаем false
                if (!(subtask.getStatus().equals(Status.DONE))) {
                    allTaskIsDone = false;
                }
            }
            //Если все DONE, устанавливаем DONE
            if (allTaskIsDone) {
                status = Status.DONE;
                //Если все NEW устанавливаем NEW
            } else if (allTaskIsNew) {
                status = Status.NEW;
                //В остальных случаях - в прогрессе
            } else {
                status = Status.IN_PROGRESS;
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public LocalDateTime getEndTime() {
        return findLateEndTimeSubtasks();
    }

    //расчет продолжительности эпика
    public void updateStartTimeAndDuration() {
        //Обработка продолжительности задачи,
        //Продолжительность эпика - сумма продолжительности всех его подзадач.
        duration = null;
        startTime = null;
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                //Если у подзадачи есть продолжительность с ней можно работать
                if (subtask.getDuration() != null) {
                    if (duration == null) {
                        duration = subtask.getDuration();
                    } else {
                        duration = duration.plus(subtask.getDuration());
                    }
                }
            }
            //инициализация старта по времени для эпика
            startTime = findEarlyStartTimeSubtasks();
        }
    }
    //расчет старта эпика
    private LocalDateTime findEarlyStartTimeSubtasks() {
        LocalDateTime earlyStartTime = subtasks.get(0).getStartTime();
        if (subtasks.size() > 1) {
            for (int i = 1; i < subtasks.size(); i++) {
                if (earlyStartTime == null)
                    earlyStartTime = subtasks.get(i).getStartTime();
                else if (subtasks.get(i).getStartTime() != null
                        && subtasks.get(i).getStartTime().isBefore(earlyStartTime)
                ) {
                    earlyStartTime = subtasks.get(i).getStartTime();
                }
            }
        }
        return earlyStartTime;
    }
    //расчет времени конца эпика
    private LocalDateTime findLateEndTimeSubtasks() {
        LocalDateTime lateEndTime = subtasks.get(0).getEndTime();
        if (subtasks.size() > 1) {
            for (int i = 1; i < subtasks.size(); i++) {
                if (lateEndTime == null)
                    lateEndTime = subtasks.get(i).getEndTime();
                else if (subtasks.get(i).getEndTime() != null
                        && subtasks.get(i).getEndTime().isAfter(lateEndTime)
                ) {
                    lateEndTime = subtasks.get(i).getEndTime();
                }
            }
        }
        return lateEndTime;
    }
}
