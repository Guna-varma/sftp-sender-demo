//package com.demo.d2s_demo.config;
//
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.sftp.DefaultSftpSessionFactory;
//import org.springframework.integration.sftp.session.SessionFactory;
//import org.springframework.integration.sftp.session.CachingSessionFactory;
//import org.springframework.integration.sftp.outbound.SftpTemplate;
//import com.jcraft.jsch.ChannelSftp.LsEntry;
//import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
//
//@Configuration
//@EnableConfigurationProperties
//public class SftpConfig {
//
//    @Bean
//    public SessionFactory<LsEntry> sftpSessionFactory() {
//        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
//        factory.setHost("localhost");
//        factory.setPort(22);
//        factory.setUser("sftpUser");
//        factory.setPassword("SftpUser123!");
//        factory.setAllowUnknownKeys(true); // For local testing, you might need to disable known hosts checking
//        return new CachingSessionFactory<>(factory);
//    }
//
//    @Bean
//    public SftpRemoteFileTemplate sftpTemplate() throws Exception {
//        return new SftpRemoteFileTemplate(sftpSessionFactory());
//    }
//}
