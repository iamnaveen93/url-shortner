package in.practice.urlshortner.service.impl;

import in.practice.urlshortner.cache.CacheTemplate;
import in.practice.urlshortner.exception.ShortCodeGenerationException;
import in.practice.urlshortner.exception.URLExpiredException;
import in.practice.urlshortner.exception.URLNotFoundException;
import in.practice.urlshortner.utility.UrlServiceMapperUtility;
import in.practice.urlshortner.entity.UrlMapping;
import in.practice.urlshortner.model.request.ShortenURLRequest;
import in.practice.urlshortner.model.response.ShortenURLResponse;
import in.practice.urlshortner.repository.UrlMappingRepository;
import in.practice.urlshortner.service.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UrlServiceImpl implements UrlService {

    @Value("${expiry_days}")
    private long expiryDays;

    private final UrlMappingRepository urlMappingRepository;
    private final CacheTemplate cacheTemplate;

    public UrlServiceImpl(UrlMappingRepository urlMappingRepository, CacheTemplate cacheTemplate) {
        this.urlMappingRepository = urlMappingRepository;
        this.cacheTemplate = cacheTemplate;
    }

    @Override
    public ShortenURLResponse createShortenURL(ShortenURLRequest shortenURLRequest) {

        final String shortCode = generateUniqueShortCode();

        final LocalDateTime expiryAt = LocalDateTime.now().plusDays(expiryDays);

        //save db
        saveDetails(shortCode, shortenURLRequest.getLongURL(), expiryAt);
        //updating cache
        updateCache(shortCode, shortenURLRequest.getLongURL(), expiryAt);

        ShortenURLResponse shortenURLResponse = new ShortenURLResponse();
        shortenURLResponse.setShortURLCode(shortCode);
        shortenURLResponse.setLongURL(shortenURLRequest.getLongURL());
        shortenURLResponse.setExpiryAt(expiryAt);
        return shortenURLResponse;
    }

    private void saveDetails(final String shortCode, final String longURL, final LocalDateTime expiryAt) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongURL(longURL);
        urlMapping.setExpiryAt(expiryAt);
        urlMapping.setShortURLCode(shortCode);
        urlMappingRepository.save(urlMapping);
    }

    private void updateCache(final String shortCode, final String longURL, final LocalDateTime expiryAt) {
        cacheTemplate.setCache(shortCode, longURL, expiryAt);
    }

    private String generateUniqueShortCode() {
        String finalShortCode;
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        do {
            finalShortCode = UrlServiceMapperUtility.generateShortCode();
            attempts++;
            if (attempts > MAX_ATTEMPTS) {
                throw new ShortCodeGenerationException(
                        "Failed to generate unique short code after " + MAX_ATTEMPTS + " attempts");
            }
        } while (urlMappingRepository.existsByShortURLCode(finalShortCode));
        return finalShortCode;
    }

    @Override
    public String getLongURL(String shortCode) {
        String cacheUrl = cacheTemplate.getURLFromCache(shortCode);
        if (cacheUrl != null) {
            log.info("Cache found for shortCode: {} , value : {} ", shortCode, cacheUrl);
            return cacheUrl;
        }
        log.info("Cache not found for shortCode: {}", shortCode);
        UrlMapping availableUrlMappingEntity = urlMappingRepository.findByShortURLCode(shortCode)
                .orElseThrow(() -> new URLNotFoundException("No URL found for code:" + shortCode));

        if (availableUrlMappingEntity.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new URLExpiredException("URL expired code:" + shortCode);
        }
        
        //updating cache
        updateCache(shortCode, availableUrlMappingEntity.getLongURL(), availableUrlMappingEntity.getExpiryAt());
        return availableUrlMappingEntity.getLongURL();
    }
}
