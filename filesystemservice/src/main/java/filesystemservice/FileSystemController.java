package filesystemservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/fileSystem")
public class FileSystemController {

    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/upload")
    public ResponseEntity<String> storeVideo(@RequestPart("ownerUsername") String ownerUsername, @RequestPart("file") MultipartFile file) {
        try {
            System.out.println("File uploaded");
            System.out.println("Owner: " + ownerUsername);
            System.out.println("File: " + file.getOriginalFilename());

            // get resource directory path
            String baseDirectory = "/storage/";


            String ownerDirectoryPath = baseDirectory + ownerUsername + "/";
            File ownerDirectory = new File(ownerDirectoryPath);

            if (!ownerDirectory.exists()) {
                ownerDirectory.mkdirs();
            }

            String filePath = ownerDirectoryPath + file.getOriginalFilename();
            String[] fileNameParts = file.getOriginalFilename().split("\\.");
            if (fileNameParts.length > 1) {
                String fileExtension = fileNameParts[fileNameParts.length - 1];
                filePath = ownerDirectoryPath + fileNameParts[0] + System.currentTimeMillis() + "." + fileExtension;
            }
            System.out.println("Owner directory path: " + ownerDirectoryPath);
            System.out.println("File path: " + filePath);
            File newFile = new File(filePath);

            file.transferTo(newFile);
            String escapedFilePath = filePath.replace("\\", "\\\\");
            return ResponseEntity.ok("{\"path\": \"" + escapedFilePath + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload the file");
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("path") String path) {
        System.out.println("File path: " + path);
        try {
            File file = new File(path);
            if (file.delete()) {
                return ResponseEntity.ok("File deleted successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to delete the file");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to delete the file");
        }
    }

    @PostMapping("/stream")
    public ResponseEntity<byte[]> streamVideoFromServer(@RequestParam("videoPath") String videoPath) {

        byte[] videoContent = new byte[0];
        try {
            Resource resource = resourceLoader.getResource("file:" + videoPath);
            videoContent = resource.getInputStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(videoContent, headers, HttpStatus.OK);
    }

}

