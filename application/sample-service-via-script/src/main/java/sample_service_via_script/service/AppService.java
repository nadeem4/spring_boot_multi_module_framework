package sample_service_via_script.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sample_service.dto.model.AppModelDTO;
import sample_service.repository.AppRepository;
import java.net.URI;

@Service
public class AppService {
 
    @Autowired
    private AppRepository repository;

}
