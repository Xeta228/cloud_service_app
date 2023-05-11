package ru.baron.cloudapp.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.baron.cloudapp.entity.User;
import ru.baron.cloudapp.repository.UserDataRepository;

@Service
public class UserService implements UserDetailsService {

    private UserDataRepository repository;

    public UserService(UserDataRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByLogin(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public void save(User user){
        repository.save(user);
    }
}
