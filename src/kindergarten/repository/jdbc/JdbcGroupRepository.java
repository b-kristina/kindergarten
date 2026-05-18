package kindergarten.repository.jdbc;

import kindergarten.model.Group;
import kindergarten.repository.GroupRepository;
import kindergarten.repository.jdbc.mapper.GroupRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JdbcGroupRepository implements GroupRepository {

    private static final String INSERT_SQL = "INSERT INTO groups (name, number) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE groups SET name = ?, number = ? WHERE id = ?";
    private static final String FIND_BY_ID_SQL = "SELECT id, name, number FROM groups WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT id, name, number FROM groups ORDER BY id";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM groups WHERE id = ?";

    private final DatabaseConnectionProvider connectionProvider;
    private final GroupRowMapper rowMapper;

    public JdbcGroupRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "DatabaseConnectionProvider cannot be null");
        this.rowMapper = new GroupRowMapper();
    }

    @Override
    public Group save(Group entity) {
        return findById(entity.getId())
                .map(existing -> updateEntity(entity))
                .orElseGet(() -> insert(entity));
    }

    @Override
    public Optional<Group> findById(Integer id) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_SQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(rowMapper.map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find group by id: " + id, e);
        }
    }

    @Override
    public List<Group> findAll() {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            List<Group> groups = new ArrayList<>();
            while (rs.next()) groups.add(rowMapper.map(rs));
            return groups;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find all groups", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_ID_SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete group: " + id, e);
        }
    }

    @Override
    public void update(Group entity) {
        updateEntity(entity);
    }

    private Group updateEntity(Group group) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, group.getName());
            stmt.setInt(2, group.getNumber());
            stmt.setInt(3, group.getId());
            stmt.executeUpdate();
            return group;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update group", e);
        }
    }

    private Group insert(Group group) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, group.getName());
            stmt.setInt(2, group.getNumber());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (!keys.next()) throw new IllegalStateException("No generated key");
                return new Group(keys.getInt(1), group.getName(), group.getNumber());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert group", e);
        }
    }
}