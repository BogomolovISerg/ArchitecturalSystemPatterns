package orm;

import java.sql.Connection;
import java.util.Optional;

public class UserRepository {

    private final UserMapper mapper;
    private UnitOfWork unitOfWork;
    private final Connection conn;

    public UserRepository(Connection conn) {
        this.conn = conn;
        this.mapper = new UserMapper(conn);
        this.unitOfWork = new UnitOfWork(conn);
    }

    public Optional<User> findById(long id){
        return mapper.findById(id);
    }

    public void insert(User user){
        unitOfWork.addNew(user);
    }

    public void beginTransactiom(){
        this.unitOfWork = new UnitOfWork(conn);
    }

    public void update(User user){
        unitOfWork.updateUser(user);
    }

    public void delete(User user){
        unitOfWork.deleteUser(user);
    }

    public void commitTransactiom(){
        unitOfWork.commit();
    }

}
