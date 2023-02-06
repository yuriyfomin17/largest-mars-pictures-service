package com.nimofy;

import com.nimofy.dto.Picture;
import com.nimofy.dto.PictureRequest;
import com.nimofy.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final Map<String, User> ipToUserMap = new ConcurrentHashMap<>();
    private final NasaClientService nasaClientService;

    void submit(String ip, PictureRequest request) {
        validatePicture(request.picture());
        ipToUserMap.put(ip, request.user());
    }

    private void validatePicture(Picture picture) {
        if (!nasaClientService.getLargetPictureUrl().equals(picture.url())){
            throw new RuntimeException("INCORRECT_PRICTURE!");
        }
    }

    public Collection<User> getAllUsers(){
        return ipToUserMap.values();
    }
}
