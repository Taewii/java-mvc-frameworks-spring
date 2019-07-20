package productshop.services;

import org.springframework.web.multipart.MultipartFile;

public interface GoogleDriveService {

    String uploadFile(MultipartFile image);

    void createFolder(String folderName);

    void downloadFile(String fileId, String fileName, String fileExtension, String path);

    void printFile(String fileId);

    void delete(String fileId);
}
