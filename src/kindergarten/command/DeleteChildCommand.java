package kindergarten.command;

import kindergarten.model.Child;
import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;
import java.util.Scanner;

public class DeleteChildCommand implements Command {
    private final ChildService childService;
    private final GroupService groupService;
    private final Scanner scanner;

    public DeleteChildCommand(ChildService childService, GroupService groupService, Scanner scanner) {
        this.childService = childService;
        this.groupService = groupService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Child> children = childService.getAllChildren();
        if (children.isEmpty()) {
            System.out.println("Детей для удаления нет.");
            return;
        }

        System.out.println("\n--- Доступные дети ---");
        for (Child child : children) {
            String groupName = getGroupName(child.getGroupId());
            System.out.println("  [" + child.getId() + "] " + child.getFullName()
                    + ", " + child.getAge() + " лет, пол: " + child.getGender()
                    + " | Группа: " + groupName);
        }

        System.out.print("\nВведите ID ребенка для удаления: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID!");
            return;
        }

        try {
            childService.deleteChild(id);
            System.out.println("Ребенок удален!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private String getGroupName(Integer groupId) {
        if (groupId == null || groupId == 0) {
            return "без группы";
        }
        try {
            Group group = groupService.getGroupById(groupId);
            return group.getName() + " №" + group.getNumber();
        } catch (RuntimeException e) {
            return "группа удалена";
        }
    }

    @Override
    public String getDescription() {
        return "Удалить ребенка";
    }
}