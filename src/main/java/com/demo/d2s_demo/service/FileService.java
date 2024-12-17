package com.demo.d2s_demo.service;

import com.demo.d2s_demo.model.FileMetadata;
import com.demo.d2s_demo.repo.FileMetaDataRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Value("${sftp.local.dir}")
    private String localDir;

    @Value("${sftp.temp-files-dir}")
    private String tempFilesDir;

    @Autowired
    private FileMetaDataRepo repo;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private SFTPService sftpService;

    @Transactional
    public String validateAndStore(MultipartFile file) {
        // Validate file naming convention
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.matches("DL_\\d{4}-\\d{2}-\\d{2}_[A-Za-z0-9]+\\.csv")) {
            return "Invalid file name";
        }

        // Validate file size
        if (file.getSize() > 100 * 1024 * 1024) {
            return "File size exceeds limit of 100MB";
        }

        // Store file temporarily
        try {
            Path tempFilePath = Paths.get(tempFilesDir, filename);

            // Check if the temporary file path exists
            if (!Files.exists(tempFilePath.getParent())) {
                logger.error("Temporary directory does not exist: " + tempFilePath.getParent());
                return "Temporary directory does not exist";
            }

            Files.createDirectories(tempFilePath.getParent());
            file.transferTo(tempFilePath);

            // Log successful file transfer to temp directory
            logger.info("File saved temporarily at: " + tempFilePath.toString());

            // Save metadata
            FileMetadata metadata = new FileMetadata(filename, file.getSize(), LocalDateTime.now());
            repo.save(metadata);

            // Transfer file to SFTP
            logger.info("Starting file transfer to SFTP: " + filename);
            sftpService.transferFile(tempFilePath.toString());

            // Send Kafka notification
            kafkaProducerService.sendNotification("File uploaded and transferred successfully: " + filename);

        } catch (IOException e) {
            logger.error("Failed to store file", e);
            return "Failed to store file: " + e.getMessage();
        } catch (Exception e) {
            logger.error("Error during SFTP transfer or Kafka notification", e);
            return "Error during SFTP transfer or Kafka notification: " + e.getMessage();
        }

        return "File uploaded, transferred, and notification sent successfully";
    }
}