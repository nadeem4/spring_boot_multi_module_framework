package security.hashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class MD5 {
	
	public static String generateHash(String value) throws NoSuchAlgorithmException {
		
		Date currentTimeInMillis = new Date();
		SecureRandom secureRandom = new SecureRandom();
		String secureString = value
				.concat(String.valueOf(currentTimeInMillis.getTime()))
				.concat(String.valueOf(secureRandom.nextLong()));
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		
		byte[] messageDigest = md5.digest(secureString.getBytes());
		BigInteger no = new BigInteger(1, messageDigest);
		String hashedText = no.toString(16);
		while (hashedText.length() < 32) {
			hashedText = "0" + hashedText;
		}
		return hashedText;
	}
}
