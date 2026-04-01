package kindergarten.command;

import kindergarten.model.Child;
import kindergarten.model.Group;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import java.util.List;

public class ShowGroupsCommand implements Command {
    private final GroupService groupService;
    private final ChildService childService;

    public ShowGroupsCommand(GroupService groupService, ChildService childService) {
        this.groupService = groupService;
        this.childService = childService;
    }

    @Override
    public void execute() {
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Групп пока нет.");
            return;
        }

        for (Group group : groups) {
            System.out.println("\n=== Группа: " + group.getName() + " №" + group.getNumber()
                    + " (ID: " + group.getId() + ") ===");

            List<Child> children = childService.getChildrenByGroupId(group.getId());
            if (children.isEmpty()) {
                System.out.println("  (детей нет)");
            } else {
                for (Child child : children) {
                    System.out.println("  [" + child.getId() + "] " + child.getFullName()
                            + ", " + child.getAge() + " лет, пол: " + child.getGender());
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Показать все группы с детьми";
    }
}