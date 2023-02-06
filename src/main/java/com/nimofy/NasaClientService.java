package com.nimofy;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class NasaClientService {
    private final RestTemplate restTemplate;
    @Value("${nasa.api.url}")
    private String nasaApiUrl;
    @Value("${nasa.api.key}")
    private String nasaApiKey;
    @Value("${nasa.api.sol}")
    private String nasaApiSol;


    @Cacheable("getLargetPictureUrl")
    public String getLargetPictureUrl(){
        var pictureUrl = buildUrl();
        var responseJson = restTemplate.getForObject(pictureUrl, JsonNode.class);
        assert responseJson != null;
        var imageUrls = StreamSupport.stream(responseJson.get("photos").spliterator(), false)
                .map(jsonNode -> jsonNode.get("img_src").asText())
                .toList();
        var maxImage = imageUrls.parallelStream()
                .map(this::getImage)
                .max(Comparator.comparing(Image::size))
                .orElseThrow();
        return maxImage.url;

    }

    public Image getImage(String url){
        var headerResponse = restTemplate.headForHeaders(url);
        var imageUrl = Objects.requireNonNull(headerResponse.getLocation()).toString();
        var imageSize  =  restTemplate.headForHeaders(imageUrl).getContentLength();
        return new Image(imageSize, imageUrl);
    }

    record Image(long size, String url){}
    private URI buildUrl(){
        return UriComponentsBuilder.fromHttpUrl(nasaApiUrl)
                .queryParam("api_key", nasaApiKey)
                .queryParam("sol", nasaApiSol)
                .build().toUri();
    }
}
