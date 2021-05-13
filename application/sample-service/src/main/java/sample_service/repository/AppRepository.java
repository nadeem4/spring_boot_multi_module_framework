package sample_service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import sample_service.model.AppModel;


@Repository
public class AppRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // private SimpleJdbcCall simpleJdbcCall;


    public String greetRepository( String name ) {
        return name;
    }

    public void saveUser(AppModel model) {

    }
}
