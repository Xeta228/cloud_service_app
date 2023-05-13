package ru.baron.cloudapp.controller;

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
//        if(model.containsAttribute("fileIsEmptyError")){
//           // String errorMessage = (String) model.getAttribute("fileIsEmptyError");
//          //  model.addAttribute("fileIsEmptyError",model.getAttribute("fileIsEmptyError"));
//        }
        model.addAttribute("files",files);
        model.addAttribute("username",user.getUsername());
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
            File uploadDir = new File(UPLOAD_PATH);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            file.transferTo(new File(UPLOAD_PATH + "/" + file.getOriginalFilename()));
            FileData fileData = new FileData(file.getOriginalFilename(),UPLOAD_PATH + "/"
                    + file.getOriginalFilename(), user);
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
            //make this look prettier in the future with custom 404 page
            return ResponseEntity.notFound().build();
        }
    }
}
