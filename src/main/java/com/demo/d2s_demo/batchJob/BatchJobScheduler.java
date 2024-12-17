package com.demo.d2s_demo.batchJob;

import com.demo.d2s_demo.service.SFTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;

@Component
public class BatchJobScheduler {

    @Autowired
    private SFTPService sftpService;

    @Scheduled(fixedRate = 60000)
    public void processBatchFiles() {

        File dir = Paths.get("C:/projects/demo-projects/sftp-frn-receiver/tempFiles/").toFile();
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                try {
                    sftpService.transferFile(file.getAbsolutePath());
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
