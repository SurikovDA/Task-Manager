package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    //Идентификационный номер задачи
    protected Integer id = 0;
    //Название задачи
    protected String name;
    //Описание задачи
    protected String descriptionTask;
    //Статус
    protected Status status;
    //продолжительность задачи, оценка того, сколько времени она займёт
    protected Duration duration;
    //дата, когда предполагается приступить к выполнению задачи
    protected LocalDateTime startTime;
    //формат даты и времени для работы
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");

    //Работа со временем:
    //установка продолжительности числом
    public void setDuration(long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

    //Установка времени начала задачи строкой
    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    //Установка времени начала задачи
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    //Время завершения задачи
    public LocalDateTime getEndTime() {
        if (duration == null)
            return null;
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    //Получение продолжительности
    public Duration getDuration() {
        return duration;
    }

    //Обнуление времени
    public void resetStartTimeAndDuration() {
        startTime = null;
        duration = null;
    }


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
