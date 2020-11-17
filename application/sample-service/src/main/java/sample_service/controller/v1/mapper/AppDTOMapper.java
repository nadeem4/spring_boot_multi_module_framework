package sample_service.controller.v1.mapper;

import logging.annotations.Loggable;
import sample_service.controller.v1.request.AppRequest;
import sample_service.dto.model.AppModelDTO;

public class AppDTOMapper {

    @Loggable
    public AppModelDTO convertToDTO(AppRequest request) {
        AppModelDTO appModel = new AppModelDTO();
        appModel.setId(request.getId());
        appModel.setName(request.getName());
        return appModel;
    }
}
