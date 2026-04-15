package kindergarten.service;

import kindergarten.model.Group;
import java.util.List;

public interface GroupService {
    Group createGroup(String name, int number);
    Group getGroupById(int id);
    List<Group> getAllGroups();
    Group updateGroup(int id, String name, int number);
    void deleteGroup(int id);
}