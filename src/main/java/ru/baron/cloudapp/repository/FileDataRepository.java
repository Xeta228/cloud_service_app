package ru.baron.cloudapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.baron.cloudapp.entity.FileData;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
}
