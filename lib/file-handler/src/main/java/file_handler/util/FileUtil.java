package file_handler.util;


import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import exception.custom.FileHandlingException;
import file_handler.constant.OperationType;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;


@Configuration
@Component
public class FileUtil {
	
	@Value ("${azure.storage.account-name}")
	private String accountName;
	
	@Value ("${azure.storage.account-key}")
	private String accountKey;
	
	@Value ("${azure.storage.container-name}")
	private String containerName;

	/**
	 *
	 * @param file
	 * @param blobName
	 * @return
	 */
	public URI uploadFile( MultipartFile file, String blobName ) {
		String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=" + accountName + ";"
				+ "AccountKey=" + accountKey;
		try {
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			final CloudBlobContainer container = blobClient.getContainerReference(blobName.toLowerCase());
			container.createIfNotExists();
			CloudBlockBlob blob = container.getBlockBlobReference(blobName);
			blob.upload(file.getInputStream(), file.getSize());
			return blob.getUri();
		} catch (StorageException e) {
			throw new FileHandlingException(file.getName(), OperationType.UPLOAD.getValues(), e.getLocalizedMessage());
		} catch (IOException e) {
			throw new FileHandlingException(file.getName(), OperationType.UPLOAD.getValues(), e.getLocalizedMessage());
		} catch (URISyntaxException e) {
			throw new FileHandlingException(file.getName(), OperationType.UPLOAD.getValues(), e.getLocalizedMessage());
		} catch (InvalidKeyException e) {
			throw new FileHandlingException(file.getName(), OperationType.UPLOAD.getValues(), e.getLocalizedMessage());
		} catch (Exception e) {
			throw new FileHandlingException(file.getName(), OperationType.UPLOAD.getValues(), e.getLocalizedMessage());
		}
		
	}

	/**
	 *
	 * @param url
	 * @param blobName
	 */
	public void deleteBlob( String url, String blobName ) {
		String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=" + accountName + ";"
				+ "AccountKey=" + accountKey;
		String fileId = url.substring(url.lastIndexOf('/') + 1);
		fileId = fileId.replaceAll("\\+", "PlusSignEncoded");
		fileId = URLDecoder.decode(fileId, StandardCharsets.UTF_8);
		fileId = fileId.replaceAll("PlusSignEncoded", "+");
		try {
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
			
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			
			final CloudBlobContainer container = blobClient.getContainerReference(blobName.toLowerCase());
			
			CloudBlockBlob blob = container.getBlockBlobReference(fileId);
			
			blob.delete();
			
		} catch (Exception e) {
			throw new FileHandlingException(fileId, OperationType.DELETE.getValues(), e.getLocalizedMessage());
		}
	}

	/**
	 *
	 * @param blobName
	 * @param url
	 * @return
	 */
	public ResponseEntity<Resource> downloadBlob( String url, String blobName ) {
		String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=" + accountName + ";"
				+ "AccountKey=" + accountKey;
		
		//getting file name from url and decoding it.
		String fileName = url.substring(url.lastIndexOf('/') + 1);
		fileName = fileName.replaceAll("\\+", "PlusSignEncoded");
		fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
		fileName = fileName.replaceAll("PlusSignEncoded", "+");
		
		//downloading file from azure
		CloudStorageAccount storageAccount = null;
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
		} catch (URISyntaxException e) {
			throw new FileHandlingException(fileName, OperationType.DELETE.getValues(), e.getLocalizedMessage());
		} catch (InvalidKeyException e) {
			throw new FileHandlingException(fileName, OperationType.DELETE.getValues(), e.getLocalizedMessage());
		}
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		final CloudBlobContainer container;
		try {
			container = blobClient.getContainerReference(blobName);
			CloudBlockBlob blob = container.getBlockBlobReference(fileName);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			blob.download(outputStream);
			
			//converting outputstream to byteArrayResource
			ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
			
			//returning resource using headers
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.headers(headers)
					.body(resource);
		} catch (URISyntaxException e) {
			throw new FileHandlingException(fileName, OperationType.DELETE.getValues(), e.getLocalizedMessage());
		} catch (StorageException e) {
			throw new FileHandlingException(fileName, OperationType.DELETE.getValues(), e.getLocalizedMessage());
		}
		
		
		
	}
}
