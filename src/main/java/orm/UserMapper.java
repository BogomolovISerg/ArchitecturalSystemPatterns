package orm;

import java.sql.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserMapper {

    private final Connection Conn;
    private final PreparedStatement selectUser;

    private final Map<Long, User> identMap = new HashMap<>();

    public UserMapper(Connection conn) {
        this.Conn = conn;
        try{
            this.selectUser = conn.prepareStatement("select id, username, password from users where id=?;");
        }catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }

    public Optional<User> findById(long id){
        User user = identMap.get(id);
        if(user != null){
            return Optional.of(user);
        }
        try {
            selectUser.setLong(1, id);
            ResultSet rs = selectUser.executeQuery();
            if(rs.next()){
                user = new User(rs.getInt(1),rs.getString(2),rs.getString(3));
                identMap.put(id, user);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
