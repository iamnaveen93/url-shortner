package in.practice.urlshortner.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenURLRequest {

    @NotBlank(message = "URL cannot be empty or blank")
    @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
    private String longURL;
}
