    package tasks;

    import java.util.Objects;

    /**
     * Класс для создания объектов подзадач
     * Класс - наследник от Task
     */

    public class Subtask extends Task {


        private Epic epic;

        public Subtask(String name, String descriptionTask) {
            super(name, descriptionTask);
        }

        public Subtask(String name) {
            super(name);
        }

        //Создание с id
        public Subtask(Integer id, String name, String descriptionTask, Status status, Epic epic) {
            super(id, name, descriptionTask, status);
            this.epic = epic;
        }


        // Обновление подзадачи, когда надо обновить idEpic
        public Subtask(String name, String descriptionTask, Status status, Epic epic) {
            super(name, descriptionTask, status);
            this.epic = epic;
        }

        // Обновление подзадачи, когда не надо обновлять idEpic
        public Subtask(String name, String descriptionTask, Status status) {
            super(name, descriptionTask, status);

        }

        public Epic getEpic() {
            return epic;
        }

        public void setEpic(Epic epic) {
            this.epic = epic;
        }



        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Subtask subtask = (Subtask) o;
            return Objects.equals(epic, subtask.epic);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), epic);
        }
    }

