import kindergarten.repository.memory.InMemoryChildRepository;
import kindergarten.repository.memory.InMemoryGroupRepository;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import kindergarten.service.impl.ChildServiceImpl;
import kindergarten.service.impl.GroupServiceImpl;
import kindergarten.ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        InMemoryGroupRepository groupRepository = new InMemoryGroupRepository();
        InMemoryChildRepository childRepository = new InMemoryChildRepository();

        GroupService groupService = new GroupServiceImpl(groupRepository, childRepository);
        ChildService childService = new ChildServiceImpl(childRepository, groupRepository);

        ConsoleMenu menu = new ConsoleMenu(childService, groupService);
        menu.start();
    }
}