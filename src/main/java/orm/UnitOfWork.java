package orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {

    private final Connection conn;
    private final List<User> newUsers = new ArrayList<>();
    private final List<User> updateUsers = new ArrayList<>();
    private final List<User> deleteUsers = new ArrayList<>();


    public void addNew(User user){
        this.newUsers.add(user);
    }

    public void updateUser(User user){
        this.updateUsers.add(user);
    }

    public void deleteUser(User user){
        this.deleteUsers.add(user);
    }

    public UnitOfWork(Connection conn) {
        this.conn = conn;
    }

    public void commit(){

      insert();
      update();
      delete();

    }

    private void insert(){
        try {
            PreparedStatement p = this.conn.prepareStatement("insert into users (id,login,password) VALUES (?,?,?);");
            for(User u:this.newUsers){
                p.setLong(1, u.getId());
                p.setString(2, u.getLogin());
                p.setString(3, u.getPassword());
                p.executeQuery();
            }
            newUsers.clear();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void update(){
        try {
            PreparedStatement p = this.conn.prepareStatement("update users set login=?, password=? where id=?;");
            for(User u:this.updateUsers){
                p.setString(1, u.getLogin());
                p.setString(2, u.getPassword());
                p.setLong(3, u.getId());
                p.executeQuery();
            }
            updateUsers.clear();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void delete(){
        try {
            PreparedStatement p = this.conn.prepareStatement("delete from users where id=?;");
            for(User u:this.deleteUsers){
                p.setLong(1, u.getId());
                p.executeQuery();
            }
            deleteUsers.clear();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
