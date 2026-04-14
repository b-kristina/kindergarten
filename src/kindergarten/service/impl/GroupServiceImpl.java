package kindergarten.service.impl;

import kindergarten.model.Child;
import kindergarten.model.Group;
import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;
import kindergarten.service.GroupService;
import java.util.List;

public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final ChildRepository childRepository;

    public GroupServiceImpl(GroupRepository groupRepository, ChildRepository childRepository) {
        this.groupRepository = groupRepository;
        this.childRepository = childRepository;
    }

    @Override
    public Group createGroup(String name, int number) {
        validateGroupName(name);
        validateGroupNumber(number);
        validateGroupUnique(name, number, null);

        return groupRepository.save(new Group(0, name, number));
    }

    @Override
    public Group getGroupById(int id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Group updateGroup(int id, String name, int number) {
        validateGroupName(name);
        validateGroupNumber(number);
        validateGroupUnique(name, number, id);

        Group group = getGroupById(id);
        group.setName(name);
        group.setNumber(number);
        groupRepository.update(group);
        return group;
    }

    @Override
    public void deleteGroup(int id) {
        List<Child> children = childRepository.findByGroupId(id);
        for (Child child : children) {
            childRepository.delete(child.getId());
        }
        groupRepository.delete(id);
    }

    private void validateGroupName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Название группы не может быть пустым!");
        }
    }

    private void validateGroupNumber(int number) {
        if (number <= 0) {
            throw new RuntimeException("Номер группы должен быть положительным!");
        }
    }

    private void validateGroupUnique(String name, int number, Integer excludeId) {
        List<Group> allGroups = groupRepository.findAll();
        for (Group group : allGroups) {
            if (excludeId != null && group.getId() == excludeId) {
                continue;
            }
            if (group.getName().equalsIgnoreCase(name.trim())) {
                throw new RuntimeException("Группа с названием \"" + name + "\" уже существует!");
            }
            if (group.getNumber() == number) {
                throw new RuntimeException("Группа с номером " + number + " уже существует!");
            }
        }
    }
}