package sample_service.dto.mapper;

//import logging.annotations.Loggable;
import sample_service.dto.model.AppModelDTO;
import sample_service.model.AppModel;

public class AppModelMapper {

   //  @Loggable(valueAfter = "Value After", valueAfterReturning = "Value After Returning", valueAround = "Value Around", valueBefore = "Value Before")
    public AppModel convertToModel(AppModelDTO modelDTO) {
        AppModel model = new AppModel();
        model.setId(modelDTO.getId());
        model.setName(modelDTO.getName());
        return model;
    }
}
