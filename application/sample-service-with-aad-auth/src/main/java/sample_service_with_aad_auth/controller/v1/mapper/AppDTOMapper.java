package sample_service_with_aad_auth.controller.v1.mapper;

import logging.annotations.Loggable;
import sample_service_with_aad_auth.controller.v1.request.AppRequest;
import sample_service_with_aad_auth.dto.model.AppModelDTO;

public class AppDTOMapper {

    @Loggable
    public AppModelDTO convertToDTO(AppRequest request) {
        AppModelDTO appModel = new AppModelDTO();
        appModel.setId(request.getId());
        appModel.setName(request.getName());
        return appModel;
    }
}
