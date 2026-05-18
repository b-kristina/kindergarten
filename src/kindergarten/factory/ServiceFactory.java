package kindergarten.factory;

import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;
import kindergarten.service.ChildService;
import kindergarten.service.GroupService;
import kindergarten.service.impl.ChildServiceImpl;
import kindergarten.service.impl.GroupServiceImpl;

public class ServiceFactory {

    public static GroupService createGroupService(
            GroupRepository groupRepository,
            ChildRepository childRepository) {
        return new GroupServiceImpl(groupRepository, childRepository);
    }

    public static ChildService createChildService(
            ChildRepository childRepository,
            GroupRepository groupRepository) {
        return new ChildServiceImpl(childRepository, groupRepository);
    }
}