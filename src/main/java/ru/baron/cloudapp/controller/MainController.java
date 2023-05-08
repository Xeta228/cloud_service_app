package ru.baron.cloudapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.baron.cloudapp.entity.FileData;
import ru.baron.cloudapp.service.FileDataService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class MainController {
    @Value("${uploadpath}")
    private String UPLOAD_PATH;
    private final FileDataService service;

    public MainController(FileDataService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String resolveMainPage(){
        return "main";
    }

    @GetMapping("/files")
    public String resolveFilesPage(Model model){
        List<FileData> files = service.findAll();
        model.addAttribute("files",files);
        return "files";
    }

    @PostMapping("/files")
    public String uploadFile(@RequestParam("file") MultipartFile file){
        //later implement the logics that if a file is not choosen just return error through binding result.
        if(file.isEmpty()) return "redirect:/files";
        try {
            File uploadDir = new File(UPLOAD_PATH);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            file.transferTo(new File(UPLOAD_PATH + "/" + file.getOriginalFilename()));
            FileData fileData = new FileData(file.getOriginalFilename(),UPLOAD_PATH + "/"
                    + file.getOriginalFilename());
            service.save(fileData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/files";
    }
   // http://localhost:8080/files/images.jpeg
    @GetMapping("/files/{file}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable File file){
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(UPLOAD_PATH + "/" + file.getName()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.attachment().filename(file.getName()).build());
            return ResponseEntity.ok().headers(headers).body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
