package com.demo.d2s_demo.service;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

@Service
public class SFTPService {

    private static final Logger logger = LoggerFactory.getLogger(SFTPService.class);

    @Value("${sftp.host}")
    private String sftpHost;

    @Value("${sftp.port}")
    private int sftpPort;

    @Value("${sftp.user}")
    private String sftpUser;

    @Value("${sftp.password}")
    private String sftpPassword;

    @Value("${sftp.remote-directory}")
    private String remoteDirectory;

    public void transferFile(String localFilePath) throws Exception {
        logger.info("Starting SFTP transfer for file: " + localFilePath);

        // Check if the local file exists
        File localFile = new File(localFilePath);
        if (!localFile.exists()) {
            logger.error("Local file does not exist: " + localFilePath);
            throw new IOException("Local file does not exist: " + localFilePath);
        }

        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, sftpPort);
        session.setPassword(sftpPassword);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        try (FileInputStream fis = new FileInputStream(localFile)) {
            String remoteFilePath = remoteDirectory + "/" + localFile.getName();
            logger.info("Uploading file to remote directory: " + remoteFilePath);

            // Ensure the remote directory exists
            try {
                channel.cd(remoteDirectory);
            } catch (SftpException e) {
                logger.warn("Remote directory does not exist, creating: " + remoteDirectory);
                channel.mkdir(remoteDirectory);
                channel.cd(remoteDirectory);
            }

            channel.put(fis, remoteFilePath);
            logger.info("File successfully uploaded: " + remoteFilePath);
        } catch (Exception e) {
            logger.error("Error during SFTP transfer for file: " + localFilePath, e);
            throw new Exception("Error during SFTP transfer for file: " + localFilePath, e);
        } finally {
            channel.disconnect();
            session.disconnect();
            logger.info("SFTP transfer completed for file: " + localFilePath);
        }
    }
}
//@Service
//public class SFTPService {
//
//    // Adjusted to your FRN system's remote directory
//    private static final String REMOTE_DIR = "C:/projects/demo-projects/sftp-frn-receiver/uploads/";
//
//    public void transferFile(String localFilePath) throws Exception {
//        JSch jsch = new JSch();
//        Session session = jsch.getSession("sftpUser", "sftp.server.com", 22);
//        session.setPassword("password");
//
//        Properties config = new Properties();
//        config.put("StrictHostKeyChecking", "no");
//        session.setConfig(config);
//        session.connect();
//
//        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
//        channel.connect();
//
//        try (FileInputStream fis = new FileInputStream(new File(localFilePath))) {
//            channel.put(fis, REMOTE_DIR + new File(localFilePath).getName());
//        } finally {
//            channel.disconnect();
//            session.disconnect();
//        }
//    }
//}
