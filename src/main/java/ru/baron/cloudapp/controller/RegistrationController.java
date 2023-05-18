package ru.baron.cloudapp.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.baron.cloudapp.entity.FileData;
import ru.baron.cloudapp.entity.User;
import ru.baron.cloudapp.service.UserService;
import ru.baron.cloudapp.util.ControllerUtils;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String resolveRegistrationPage(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/registration")
    public String register(@RequestParam("confirmPassword")String confirmPassword, @Valid User user,
                           BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.getErrors(bindingResult));
            return "registration";
        }
        if(userService.findUserByLogin(user.getLogin())!=null){
            model.addAttribute("userExistsError","user with such login already exists");
            return "registration";
        }
        if(!user.getPassword().equals(confirmPassword)){
            model.addAttribute("confirmPasswordError","passwords should match");
            return "registration";
        }
       // User user = new User(username, password);

        return "login";
    }


}
