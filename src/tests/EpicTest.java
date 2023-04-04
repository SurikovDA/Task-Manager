package tests;

import managers.Managers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EpicTest {
    private final Managers managers = new Managers();
    private Epic epic;
    private Subtask subtask;
    private Subtask subtask1;

    @BeforeEach
    private void beforeEachCreateEpicAndSubtasks() {
        epic = new Epic("Сходить в магазин");
        subtask = new Subtask("Выйти из дома");
        subtask1 = new Subtask("Купить продуктов");
    }

    @Test
    public void shouldBeEmptyListOfSubtasks() {
        //Пустой список подзадач.
        assertTrue(epic.getSubtasks().isEmpty());
    }

    @Test
    public void shouldAllSubtasksWithNewStatus() {
        //Все подзадачи со статусом NEW
        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask1);

        subtask.setEpic(epic);
        subtask1.setEpic(epic);

        //проверка
        assertEquals(Status.NEW, epic.getStatus());
    }


    @Test
    public void shouldAllSubtasksWithDoneStatus() {
        //Все подзадачи со статусом DONE
        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask1);

        subtask.setEpic(epic);
        subtask1.setEpic(epic);

        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        //проверка статусов у Subtasks Epic'ов
        epic.setStatusEpic();
        //проверка
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldAllSubtasksWithInProgressStatus() {
        //Все подзадачи со статусом IN_PROGRESS
        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask1);

        subtask.setEpic(epic);
        subtask1.setEpic(epic);

        subtask.setStatus(Status.IN_PROGRESS);
        boolean beINProgress = false;

        for (Subtask epicSubtask : epic.getSubtasks()) {
            if (epicSubtask.getStatus().equals(Status.IN_PROGRESS)) {
                beINProgress = true;
                break;
            }
        }
        assertTrue(beINProgress);
    }

    @Test
    public void shouldAllSubtasksWithNewOrDoneStatus() {
        //Все подзадачи со статусом New и Done
        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask1);

        subtask.setEpic(epic);
        subtask1.setEpic(epic);

        subtask.setStatus(Status.DONE);
        boolean beNew = false;
        boolean beDone = false;

        for (Subtask epicSubtask : epic.getSubtasks()) {
            if (epicSubtask.getStatus().equals(Status.NEW))
                beNew = true;
            else if (epicSubtask.getStatus().equals(Status.DONE))
                beDone = true;
        }

        assertTrue(beNew && beDone);
    }
}