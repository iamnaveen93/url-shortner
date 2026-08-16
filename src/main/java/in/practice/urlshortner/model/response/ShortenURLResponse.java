package in.practice.urlshortner.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShortenURLResponse {

    private String shortURLCode;
    private String longURL;
    private LocalDateTime expiryAt;
}
