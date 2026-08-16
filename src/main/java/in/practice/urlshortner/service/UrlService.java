package in.practice.urlshortner.service;

import in.practice.urlshortner.model.request.ShortenURLRequest;
import in.practice.urlshortner.model.response.ShortenURLResponse;
import org.springframework.stereotype.Service;


@Service
public interface UrlService {

    ShortenURLResponse createShortenURL(ShortenURLRequest shortenURLRequest);

    String getLongURL(final String shortCode);
}
