package in.practice.urlshortner.controller;

import in.practice.urlshortner.model.request.ShortenURLRequest;
import in.practice.urlshortner.model.response.ShortenURLResponse;
import in.practice.urlshortner.service.UrlService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/v1/url")
@Slf4j
public class URLController {

    private final UrlService urlService;

    public URLController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShortenURLResponse> createURL(@RequestBody @Valid ShortenURLRequest shortenURLRequest) {
        return ResponseEntity.ok(urlService.createShortenURL(shortenURLRequest));
    }

    @GetMapping(path = "/{shortCode}")
    public ResponseEntity<String> getLongURL(@PathVariable final String shortCode) {
        final String url = urlService.getLongURL(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url)).build();
    }

}
