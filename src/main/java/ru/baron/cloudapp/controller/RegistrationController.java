package ru.baron.cloudapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.baron.cloudapp.entity.User;
import ru.baron.cloudapp.service.UserService;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String resolveRegistrationPage(){
        return "registration";
    }

    @PostMapping("/registration")
    public String register(@RequestParam("username")String username, @RequestParam("password")String password,
                           @RequestParam("confirmPassword")String confirmPassword){
        if(!password.equals(confirmPassword)) return "redirect:/registration";
        User user = new User(username, password);
        userService.save(user);
        return "login";
    }


}
