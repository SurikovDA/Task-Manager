package tests;

import managers.history.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager inMemoryHistoryManager;
    private Task test1;
    private Task test2;
    private Task test3;

    @BeforeEach
    public void createObjectInMemoryHistoryManager() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        test1 = new Task("test1");
        test1.setId(1);
        test2 = new Task("test2");
        test2.setId(2);
        test3 = new Task("test3");
        test3.setId(3);
    }

    @Test
    void test1_shouldReturnEmptyTaskHistory() {
        Assertions.assertNull(inMemoryHistoryManager.getHistory());
    }

    @Test
    void test2_shouldHistoryListBeOfSizeOne() {
        //проверка на дублирование истории
        inMemoryHistoryManager.add(test1);
        inMemoryHistoryManager.add(test1);
        inMemoryHistoryManager.add(test1);

        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void test3_shouldDeletionAtBeginningOfStory() {
        inMemoryHistoryManager.add(test1);
        inMemoryHistoryManager.add(test2);
        inMemoryHistoryManager.add(test3);

        inMemoryHistoryManager.remove(test1.getId());

        Task[] expectedHistory = new Task[]{test2, test3};

        Assertions.assertArrayEquals(
                expectedHistory,
                inMemoryHistoryManager.getHistory().toArray(Task[]::new),
                "Не верно удален элемент в начале"
        );
    }

    @Test
    void test4_shouldDeletionAtEndOfStory() {
        inMemoryHistoryManager.add(test1);
        inMemoryHistoryManager.add(test2);
        inMemoryHistoryManager.add(test3);

        inMemoryHistoryManager.remove(test3.getId());

        Task[] expectedHistory = new Task[]{test1, test2};

        Assertions.assertArrayEquals(
                expectedHistory,
                inMemoryHistoryManager.getHistory().toArray(Task[]::new),
                "Не верно удалён элемент в конце"
        );
    }

    @Test
    void test5_shouldDeletingInMiddleOfStory() {
        inMemoryHistoryManager.add(test1);
        inMemoryHistoryManager.add(test2);
        inMemoryHistoryManager.add(test3);

        inMemoryHistoryManager.remove(test2.getId());

        Task[] expectedHistory = new Task[]{test1, test3};

        Assertions.assertArrayEquals(
                expectedHistory,
                inMemoryHistoryManager.getHistory().toArray(Task[]::new),
                "Не верно удалён элемент из середины"
        );
    }

    @Test
    void test6_addHistoryWhenSizeHistory() {
        Task test4 = new Task("test4");
        test4.setId(4);
        Task test5 = new Task("test5");
        test5.setId(5);
        Task test6 = new Task("test6");
        test6.setId(6);
        Task test7 = new Task("test7");
        test7.setId(7);
        Task test8 = new Task("test8");
        test8.setId(8);
        Task test9 = new Task("test9");
        test9.setId(9);
        Task test10 = new Task("test10");
        test10.setId(10);
        Task test11 = new Task("test11");
        test11.setId(11);
        Task test12 = new Task("test12");
        test12.setId(12);

        inMemoryHistoryManager.add(test1);
        inMemoryHistoryManager.add(test2);
        inMemoryHistoryManager.add(test3);
        inMemoryHistoryManager.add(test4);
        inMemoryHistoryManager.add(test5);
        inMemoryHistoryManager.add(test6);
        inMemoryHistoryManager.add(test7);
        inMemoryHistoryManager.add(test8);
        inMemoryHistoryManager.add(test9);
        inMemoryHistoryManager.add(test10);
        inMemoryHistoryManager.add(test11);
        inMemoryHistoryManager.add(test12);

        Task[] expectedHistory = new Task[]{
                test1,
                test2,
                test3,
                test4,
                test5,
                test6,
                test7,
                test8,
                test9,
                test10,
                test11,
                test12
        };

        Assertions.assertArrayEquals(expectedHistory,
                inMemoryHistoryManager.getHistory().toArray(Task[]::new),
                "Не верная история"
        );
    }
}
