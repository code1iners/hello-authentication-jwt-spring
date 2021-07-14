package hello.condeliner.jwt.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {
    @NotNull
    @Size(min = 3, max = 30)
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}
