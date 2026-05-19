package core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO {

    private Long id;

    @NotBlank(message = "Имя автора обязательно")
    @Size(min = 2, max = 100)
    private String name;

    @Size(max = 1000)
    private String biography;
}