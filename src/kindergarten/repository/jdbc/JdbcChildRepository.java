package kindergarten.repository.jdbc;

import kindergarten.model.Child;
import kindergarten.repository.ChildRepository;
import kindergarten.repository.jdbc.mapper.ChildRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JdbcChildRepository implements ChildRepository {

    private static final String INSERT_SQL = "INSERT INTO children (full_name, gender, age, group_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE children SET full_name = ?, gender = ?, age = ?, group_id = ? WHERE id = ?";
    private static final String FIND_BY_ID_SQL = "SELECT id, full_name, gender, age, group_id FROM children WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT id, full_name, gender, age, group_id FROM children ORDER BY id";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM children WHERE id = ?";
    private static final String FIND_BY_GROUP_ID_SQL = "SELECT id, full_name, gender, age, group_id FROM children WHERE group_id = ?";

    private final DatabaseConnectionProvider connectionProvider;
    private final ChildRowMapper rowMapper;

    public JdbcChildRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = Objects.requireNonNull(connectionProvider, "DatabaseConnectionProvider cannot be null");
        this.rowMapper = new ChildRowMapper();
    }

    @Override
    public Child save(Child entity) {
        return findById(entity.getId())
                .map(existing -> updateEntity(entity))
                .orElseGet(() -> insert(entity));
    }

    @Override
    public Optional<Child> findById(Integer id) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_SQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(rowMapper.map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find child: " + id, e);
        }
    }

    @Override
    public List<Child> findAll() {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            List<Child> children = new ArrayList<>();
            while (rs.next()) children.add(rowMapper.map(rs));
            return children;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find all children", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_ID_SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete child", e);
        }
    }

    @Override
    public void update(Child entity) {
        updateEntity(entity);
    }

    private Child updateEntity(Child child) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, child.getFullName());
            stmt.setString(2, child.getGender());
            stmt.setInt(3, child.getAge());
            stmt.setObject(4, child.getGroupId());
            stmt.setInt(5, child.getId());
            stmt.executeUpdate();
            return child;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update child", e);
        }
    }

    private Child insert(Child child) {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, child.getFullName());
            stmt.setString(2, child.getGender());
            stmt.setInt(3, child.getAge());
            stmt.setObject(4, child.getGroupId());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (!keys.next()) throw new IllegalStateException("No generated key");
                return new Child(keys.getInt(1), child.getFullName(), child.getGender(),
                        child.getAge(), child.getGroupId());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert child", e);
        }
    }

    @Override
    public List<Child> findByGroupId(Integer groupId) {
        if (groupId == null || groupId == 0) return new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_GROUP_ID_SQL)) {
            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Child> children = new ArrayList<>();
                while (rs.next()) children.add(rowMapper.map(rs));
                return children;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find children by group", e);
        }
    }
}