package pt.iade.planit.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private String password;
}
