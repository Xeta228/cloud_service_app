package ru.baron.cloudapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.baron.cloudapp.entity.FileData;

import java.util.List;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    List<FileData> findAllByUserId(Long userId);

    void deleteByName(String name);

    FileData findByName(String name);


}