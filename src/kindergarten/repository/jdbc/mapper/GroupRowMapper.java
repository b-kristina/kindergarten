package kindergarten.repository.jdbc.mapper;

import kindergarten.model.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class GroupRowMapper {

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_NUMBER = "number";

    public Group map(ResultSet rs) throws SQLException {
        return new Group(
                rs.getInt(COL_ID),
                rs.getString(COL_NAME),
                rs.getInt(COL_NUMBER)
        );
    }
}