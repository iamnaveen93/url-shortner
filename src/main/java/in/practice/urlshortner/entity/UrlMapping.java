package in.practice.urlshortner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table
@Entity(name = "url_mapping")
@Getter
@Setter
public class UrlMapping extends BaseEntity {

    @Column(name = "long_url" , nullable = false)
    private String longURL;
    @Column(name = "short_code", nullable = false, unique = true, length = 10)
    private String shortURLCode;
    @Column(name = "expiry_time" , nullable = false)
    private LocalDateTime expiryAt;
}
