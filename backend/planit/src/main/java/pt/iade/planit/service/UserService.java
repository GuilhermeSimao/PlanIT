package pt.iade.planit.service;

import pt.iade.planit.dto.UserDTO;
import pt.iade.planit.entity.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    List<User> getUsers();

    User getUser(Integer id);

    void updateUser(Integer id, User user);

    void deleteUser(Integer id);

    void updateName(Integer id, UserDTO userDTO);

    User authenticate(String email, String password);

    void register(User user);
}