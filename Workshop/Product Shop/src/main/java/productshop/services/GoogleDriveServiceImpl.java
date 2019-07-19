package productshop.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final int MAX_ALLOWED_SIZE_IN_MB = 3;
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>() {{
        add("image/bmp");
        add("image/jpeg");
        add("image/png");
        add("image/svg+xml");
    }};

    private final NetHttpTransport HTTP_TRANSPORT;
    private final Drive service;

    public GoogleDriveServiceImpl() throws GeneralSecurityException, IOException {
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        try {
            java.io.File tempFile = java.io.File.createTempFile("temp", ".tmp");
            long sizeInBytes = tempFile.length() / (1024 * 1024);

            if (sizeInBytes > MAX_ALLOWED_SIZE_IN_MB || !ALLOWED_MIME_TYPES.contains(multipartFile.getContentType())) {
                throw new IllegalArgumentException("File doesn't meet the requirements");
            }

            tempFile.deleteOnExit();
            multipartFile.transferTo(tempFile);

            File fileMetadata = new File();
            fileMetadata.setName(multipartFile.getOriginalFilename());
            fileMetadata.setParents(Collections.singletonList("1F3PAqn2xgUtQ77hkogwvcIafHBKyTXtn"));
            FileContent mediaContent = new FileContent(multipartFile.getContentType(), tempFile);

            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            tempFile.delete();
            return file.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void createFolder(String folderName) {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        File file;
        try {
            file = service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder ID: " + file.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadFile(String fileId, String fileName, String fileExtension, String path) {
        OutputStream out;
        try {
            out = new FileOutputStream(path + fileName + "." + fileExtension);
            Drive.Files.Get get = service.files().get(fileId);
            get.executeMediaAndDownloadTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printFile(String fileId) {
        try {
            File file = service.files().get(fileId).execute();

            System.out.println("Title: " + file.getName());
            System.out.println("Description: " + file.getDescription());
            System.out.println("MIME type: " + file.getMimeType());
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    private Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleDriveServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}