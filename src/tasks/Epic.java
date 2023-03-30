package tasks;

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
}
