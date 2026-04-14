package kindergarten.command;

public enum CommandType {
    ADD_GROUP("добавить-группу", "Добавить группу"),
    ADD_CHILD("добавить-ребенка", "Добавить ребенка"),
    SHOW_GROUPS("показать-группы", "Показать все группы с детьми"),
    DELETE_GROUP("удалить-группу", "Удалить группу"),
    DELETE_CHILD("удалить-ребенка", "Удалить ребенка"),
    EDIT_GROUP("редактировать-группу", "Редактировать группу"),
    EDIT_CHILD("редактировать-ребенка", "Редактировать ребенка"),
    EXIT("выход", "Выход");

    private final String commandKey;
    private final String description;

    CommandType(String commandKey, String description) {
        this.commandKey = commandKey;
        this.description = description;
    }

    public String getCommandKey() {
        return commandKey;
    }

    public String getDescription() {
        return description;
    }

    public static CommandType fromKey(String key) {
        for (CommandType type : values()) {
            if (type.commandKey.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}