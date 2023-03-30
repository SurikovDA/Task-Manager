package tasks;

import java.util.Objects;
import static tasks.Status.NEW;

/**
 * Класс для создания объектов - задач.
 * Класс - родитель, классов Epic и Subtask
 */

public class Task {
    /**
     * Как классу - родителю созданы:
     * все конструкторы, геттеры, сеттеры,
     * equals, hashCode(), toString().
     */
    protected Integer id = 0; //Идентификационный номер задачи
    protected String name;  //Название задачи
    protected String descriptionTask; //Описание задачи
    protected Status status; //Статус

    //Конструкторы:
    //Только название задачи
    public Task(String name) {
        this.name = name;
        this.descriptionTask = null;
        this.status = NEW;
    }

    //C описанем задачи
    public Task(String name, String descriptionTask) {
        this.name = name;
        this.descriptionTask = descriptionTask;
        this.status = NEW;
    }


    //Конструктор с id
    public Task(Integer id, String name, String descriptionTask, Status status) {
        this.id = id;
        this.name = name;
        this.descriptionTask = descriptionTask;
        this.status = status;
    }


    //Конструктор для обновления задачи
    public Task(String name, String descriptionTask, Status status) {
        if (name != null)
            this.name = name;
        if (descriptionTask != null)
            this.descriptionTask = descriptionTask;
        this.status = status;
    }

    //Геттеры и Сеттеры:
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //equals, hashCode, toString:
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) &&
                Objects.equals(descriptionTask, task.descriptionTask) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, descriptionTask, status);
    }

    @Override
    public String toString() {
        return "Задача{" +
                "№=" + id +
                ", Название='" + name + '\'' +
                ", Описание ='" + descriptionTask + '\'' +
                ", Статус=" + status +
                '}';
    }
}
