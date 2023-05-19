package ru.baron.cloudapp.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.baron.cloudapp.entity.FileData;
import ru.baron.cloudapp.entity.User;
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
    public String resolveFilesPage(Model model, @AuthenticationPrincipal User user){
        List<FileData> files = service.findAllByUserId(user.getId());
        model.addAttribute("files",files);
        model.addAttribute("username",user.getUsername());
        Double totalFileSize = files.stream().mapToDouble(FileData::getSize).sum();
        model.addAttribute("totalsize",totalFileSize);
        return "files";
    }

    @PostMapping("/files")
    public String uploadFile(@RequestParam(value = "file") MultipartFile file
                            , @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes){
        if(file.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileIsEmptyError"
                    ,"the file you are submitting cannot be empty");
            return "redirect:/files";
        }
        try {
            String pathToUpload = UPLOAD_PATH + '/' + user.getId();
            File uploadDir = new File(pathToUpload);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            file.transferTo(new File(pathToUpload + "/" + file.getOriginalFilename()));
            FileData fileData = new FileData(file.getOriginalFilename(),pathToUpload + "/"
                    + file.getOriginalFilename(), user,(double)file.getSize()/1024);

            //Bug where file of other user is replaced
            List<FileData> filesFromDb = service.findAllByUserLogin(user.getLogin());
            if(filesFromDb.contains(fileData)){
                service.updateFileData(fileData);
                redirectAttributes.addFlashAttribute("fileUpdateWarning"
                        ,"the file already exists. It has been updated");
            }

            else {
                service.save(fileData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/files";
    }
   // http://localhost:8080/files/images.jpeg
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id, @AuthenticationPrincipal User user){
        FileData file = service.findById(id);
        if(user.equals(file.getUser())) {
            try {

                byte[] fileContent = Files.readAllBytes(Paths.get(file.getPath()));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(ContentDisposition.attachment().filename(file.getPath()).build());
                return ResponseEntity.ok().headers(headers).body(fileContent);
            } catch (IOException e) {
                return ResponseEntity.notFound().build();
            }
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
