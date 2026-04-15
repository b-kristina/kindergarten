package kindergarten.factory;

import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;
import kindergarten.repository.memory.InMemoryChildRepository;
import kindergarten.repository.memory.InMemoryGroupRepository;

public class RepositoryFactory {

    public static GroupRepository createGroupRepository() {
        return new InMemoryGroupRepository();
    }

    public static ChildRepository createChildRepository() {
        return new InMemoryChildRepository();
    }
}