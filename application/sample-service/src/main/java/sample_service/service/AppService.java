package sample_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample_service.dto.model.AppModelDTO;
import sample_service.repository.AppRepository;

@Service
public class AppService {

    @Autowired
    private AppRepository repository;

    public String greetService( String name ) {
        return repository.greetRepository(name);
    }

    public void saveUser(AppModelDTO model) {

    }
}
