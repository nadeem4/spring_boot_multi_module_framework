package notification.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import notification.dto.GetNotificationDTO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationMapper {

    public GetNotificationDTO convertToDTO(Object notificationObj) {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> notificationMap = mapper.convertValue(notificationObj, Map.class);
        GetNotificationDTO getNotificationDTO = new GetNotificationDTO();
        getNotificationDTO.setEntity((String) notificationMap.get("entity"))
                .setEntityKey((String) notificationMap.get("entityKey"))
                .setId((Integer) notificationMap.get("id"))
                .setMessage((String) notificationMap.get("message"))
                .setStatus((Integer) notificationMap.get("status"))
                .setTimestamp(String.valueOf(notificationMap.get("timestamp")))
                .setUserId((Integer) notificationMap.get("userId"));
        return getNotificationDTO;
    }
}
