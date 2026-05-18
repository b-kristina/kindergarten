package kindergarten.repository.memory;

import kindergarten.model.Group;
import kindergarten.repository.GroupRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryGroupRepository implements GroupRepository {
    private final Map<Integer, Group> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public Group save(Group entity) {
        if (entity.getId() == 0) {
            entity.setId(idCounter.getAndIncrement());
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Group> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Group> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(Integer id) {
        store.remove(id);
    }

    @Override
    public void update(Group entity) {
        store.put(entity.getId(), entity);
    }
}