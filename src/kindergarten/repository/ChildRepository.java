package kindergarten.repository;

import kindergarten.model.Child;
import java.util.List;

public interface ChildRepository extends Repository<Child, Integer> {
    List<Child> findByGroupId(Integer groupId);
}