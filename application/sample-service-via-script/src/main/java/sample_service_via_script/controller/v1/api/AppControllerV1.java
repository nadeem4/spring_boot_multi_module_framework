package sample_service_via_script.controller.v1.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sample_service.controller.v1.mapper.AppDTOMapper;
import sample_service.controller.v1.request.AppRequest;
import sample_service.service.AppService;
import utility.annotations.ControllerV1;
import utility.constant.ActionType;
import utility.constant.EntityType;
import utility.constant.Messages;
import utility.custom_data_type.ValidList;
import utility.dto.ResponseDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ControllerV1
public class AppControllerV1 {

    @Autowired
    private AppService service;
}

