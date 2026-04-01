package kindergarten.command;

import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Scanner;

public class DeleteGroupCommand implements Command {
    private final GroupService groupService;
    private final ChildService childService;
    private final Scanner scanner;

    public DeleteGroupCommand(GroupService groupService, ChildService childService, Scanner scanner) {
        this.groupService = groupService;
        this.childService = childService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп для удаления нет.");
            return;
        }

        System.out.println("\n--- Доступные группы ---");
        for (Group group : groups) {
            int childCount = childService.getChildrenByGroupId(group.getId()).size();
            System.out.println("  [" + group.getId() + "] " + group.getName()
                    + " №" + group.getNumber() + " (детей: " + childCount + ")");
        }

        System.out.print("\nВведите ID группы для удаления: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID!");
            return;
        }

        try {
            groupService.deleteGroup(id);
            System.out.println("Группа удалена!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Удалить группу";
    }
}