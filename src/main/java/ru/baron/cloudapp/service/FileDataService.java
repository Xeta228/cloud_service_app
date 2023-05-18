package ru.baron.cloudapp.service;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import ru.baron.cloudapp.entity.FileData;
import ru.baron.cloudapp.repository.FileDataRepository;

import java.io.File;
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


    public void delete(FileData fileData){repository.deleteByName(fileData.getName());}

    public FileData findByName(String name) {
        return repository.findByName(name);
    }


    @Transactional
    public void updateFileData(FileData fileData) throws InterruptedException {
        repository.delete(fileData);
        repository.save(fileData);
    }


}
