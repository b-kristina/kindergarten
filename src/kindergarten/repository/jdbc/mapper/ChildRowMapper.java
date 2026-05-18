package kindergarten.repository.jdbc.mapper;

import kindergarten.model.Child;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ChildRowMapper {

    public static final String COL_ID = "id";
    public static final String COL_FULL_NAME = "full_name";
    public static final String COL_GENDER = "gender";
    public static final String COL_AGE = "age";
    public static final String COL_GROUP_ID = "group_id";

    public Child map(ResultSet rs) throws SQLException {
        return new Child(
                rs.getInt(COL_ID),
                rs.getString(COL_FULL_NAME),
                rs.getString(COL_GENDER),
                rs.getInt(COL_AGE),
                rs.getObject(COL_GROUP_ID, Integer.class)
        );
    }
}