package kindergarten.service.impl;

import kindergarten.model.Child;
import kindergarten.repository.ChildRepository;
import kindergarten.repository.GroupRepository;
import kindergarten.service.ChildService;
import java.util.List;

public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final GroupRepository groupRepository;

    public ChildServiceImpl(ChildRepository childRepository, GroupRepository groupRepository) {
        this.childRepository = childRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public Child createChild(String fullName, String gender, int age, Integer groupId) {
        validateFullName(fullName);
        validateAge(age);
        validateGroupExists(groupId);

        return childRepository.save(new Child(0, fullName.trim(), gender, age, groupId));
    }

    @Override
    public Child getChildById(int id) {
        return childRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ребенок не найден"));
    }

    @Override
    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }

    @Override
    public List<Child> getChildrenByGroupId(Integer groupId) {
        return childRepository.findByGroupId(groupId);
    }

    @Override
    public Child updateChild(int id, String fullName, String gender, int age, Integer groupId) {
        validateFullName(fullName);
        validateAge(age);

        Child child = getChildById(id);
        child.setFullName(fullName.trim());
        child.setGender(gender);
        child.setAge(age);
        child.setGroupId(groupId);
        childRepository.update(child);
        return child;
    }

    @Override
    public void deleteChild(int id) {
        childRepository.delete(id);
    }

    private void validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new RuntimeException("ФИО ребенка не может быть пустым!");
        }
    }

    private void validateAge(int age) {
        if (age <= 0) {
            throw new RuntimeException("Возраст должен быть положительным!");
        }
    }

    private void validateGroupExists(Integer groupId) {
        if (groupId == null || groupId == 0) {
            throw new RuntimeException("Группа не может быть пустой!");
        }
        groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа с ID " + groupId + " не найдена"));
    }
}