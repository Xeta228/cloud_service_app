package ru.baron.cloudapp.service;
import org.springframework.stereotype.Service;
import ru.baron.cloudapp.entity.FileData;
import ru.baron.cloudapp.repository.FileDataRepository;

import java.util.List;

@Service
public class FileDataService {

    private FileDataRepository repository;

    public FileDataService(FileDataRepository repository) {
        this.repository = repository;
    }

    public List<FileData> findAll() {
        return repository.findAll();
    }

    public List<FileData> findAllByUserId(Long id){
        return repository.findAllByUserId(id);
    }
    public void save(FileData fileData) {
        repository.save(fileData);
    }
}
