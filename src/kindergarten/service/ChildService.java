package kindergarten.service;

import kindergarten.model.Child;
import java.util.List;

public interface ChildService {
    Child createChild(String fullName, String gender, int age, Integer groupId);
    Child getChildById(int id);
    List<Child> getAllChildren();
    List<Child> getChildrenByGroupId(Integer groupId);
    Child updateChild(int id, String fullName, String gender, int age, Integer groupId);
    void deleteChild(int id);
}