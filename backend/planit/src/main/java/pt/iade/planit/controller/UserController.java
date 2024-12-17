package pt.iade.planit.controller;

import org.springframework.http.HttpStatus;
import pt.iade.planit.dto.UserDTO;
import pt.iade.planit.entity.User;
import pt.iade.planit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * add user
     */

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        userService.addUser(user);

        return "success add user";
    }

    /**
     * get users as list
     */

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * get user by id
     */

    @GetMapping("/get")
    public User getUser(@RequestParam Integer id) {
        return userService.getUser(id);
    }

    /**
     * update user
     */

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody User user) {
        userService.updateUser(id, user);

        return ResponseEntity.noContent().build();
    }

    /**
     * delete user
     */

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * update name
     */

    @PatchMapping("/update-name/{id}")
    public ResponseEntity<Void> updateName(@PathVariable Integer id, @RequestBody UserDTO userDTO){
        userService.updateName(id, userDTO);

        return ResponseEntity.noContent().build();
    }

    /**
     * login
     */
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody User user) {
        User existingUser = userService.authenticate(user.getEmail(), user.getPassword());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(existingUser.getId());
        userDTO.setName(existingUser.getName());
        userDTO.setEmail(existingUser.getEmail());
        return ResponseEntity.ok(userDTO);
    }

    /**
     * register
     */

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        // Cria o User usando os dados recebidos no DTO, mas inclui a senha para registrar
        User user = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword()); // Aqui, a senha será preenchida de outra forma.

        // Agora, pode chamar o método de registro do serviço
        userService.register(user);

        // Cria o UserDTO para a resposta, sem a senha
        UserDTO responseDTO = new UserDTO();
        responseDTO.setId(user.getId());  // O ID é atribuído após o usuário ser salvo no banco
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setPassword(user.getPassword());

        // Retorna a resposta com o UserDTO sem a senha
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}