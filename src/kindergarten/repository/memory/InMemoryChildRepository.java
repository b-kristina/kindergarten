package kindergarten.repository.memory;

import kindergarten.model.Child;
import kindergarten.repository.ChildRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryChildRepository implements ChildRepository {
    private final Map<Integer, Child> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public Child save(Child entity) {
        if (entity.getId() == 0) {
            entity.setId(idCounter.getAndIncrement());
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Child> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Child> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(Integer id) {
        store.remove(id);
    }

    @Override
    public void update(Child entity) {
        store.put(entity.getId(), entity);
    }

    @Override
    public List<Child> findByGroupId(Integer groupId) {
        return store.values().stream()
                .filter(c -> groupId.equals(c.getGroupId()))
                .collect(Collectors.toList());
    }
}