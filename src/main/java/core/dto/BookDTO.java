package core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "Название обязательно")
    @Size(min = 1, max = 200)
    private String title;

    private String isbn;
    private int publicationYear;
    private Long authorId;
    private Boolean available;
}