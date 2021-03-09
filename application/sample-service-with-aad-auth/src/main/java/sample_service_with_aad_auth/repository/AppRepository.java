package sample_service_with_aad_auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import sample_service_with_aad_auth.model.AppModel;

import javax.annotation.PostConstruct;

@Repository
public class AppRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall simpleJdbcCall;

    @PostConstruct
    public void init() { jdbcTemplate.setResultsMapCaseInsensitive(true);}

    public String greetRepository( String name ) {
        return name;
    }

    public void saveUser(AppModel model) {

    }
}
