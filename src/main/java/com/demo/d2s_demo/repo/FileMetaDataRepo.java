package com.demo.d2s_demo.repo;

import com.demo.d2s_demo.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepo extends JpaRepository<FileMetadata, Long> {

}
