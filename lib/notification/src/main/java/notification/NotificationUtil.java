
package notification;
    

import exception.custom.NotificationException;
import notification.constant.NotificationMethod;
import notification.mapper.NotificationMapper;
import notification.dto.GetNotificationDTO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import notification.SignalR.SignalRMessage;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;


@Component
public class NotificationUtil {
	
	private static final String HUB_NAME = "WorkOrder";
	
	@Value("${app.url}")
	private String url;
	
	@Value("${app.signalR.name}")
	private String signalRName;
	
	@Value("${app.signalR.key}")
	private String signalRServiceKey;
	
	@Autowired
	private NotificationMapper notificationMapper;
	
	public void broadcastMessage(List<Object> getNotificationObjs) {
		
		String signalRServiceBaseEndpoint = "https://" + signalRName + ".service.signalr.net";
		for (Object getNotificationObj : getNotificationObjs) {
			GetNotificationDTO getNotificationDTO = notificationMapper.convertToDTO(getNotificationObj);
			JSONObject bodyJsonObject = new JSONObject();
			try {
				bodyJsonObject.put("message", getNotificationDTO.getMessage());
				bodyJsonObject.put("id", getNotificationDTO.getId());
				bodyJsonObject.put("status", getNotificationDTO.getStatus());
				bodyJsonObject.put("timestamp", getNotificationDTO.getTimestamp());
				bodyJsonObject.put("entity", getNotificationDTO.getEntityKey());
				
			} catch (JSONException e) {
				throw new NotificationException("Error occurred while broadcasting notification", NotificationMethod.SIGNALR.getValue());
			}
			String hubUrl = signalRServiceBaseEndpoint + "/api/v1/hubs/" + HUB_NAME + "/users/" + getNotificationDTO.getUserId();
			String accessKey = generateJwt(hubUrl, getNotificationDTO.getUserId());
			
			Unirest.post(hubUrl)
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer " + accessKey)
					.body(new SignalRMessage("newMessage", new Object[]{bodyJsonObject}))
					.asEmpty();
		}
		
	}
	
	public String generateJwt(String audience, Integer userId) {
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		
		long expMillis = nowMillis + (12 * 60 * 60 * 1000);
		Date exp = new Date(expMillis);
		
		byte[] apiKeySecretBytes = signalRServiceKey.getBytes(StandardCharsets.UTF_8);
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
		JwtBuilder builder = Jwts.builder()
				.setAudience(audience)
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(signingKey);
		
		if (userId != null) {
			builder.claim("nameid", userId);
		}
		
		return builder.compact();
	}
}

