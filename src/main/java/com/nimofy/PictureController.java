package com.nimofy;

import com.nimofy.dto.PictureRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/mars/pictures/largest")
@RequiredArgsConstructor
public class PictureController {
    private final PictureService pictureService;
    private final HttpServletRequest servletRequest;
    @PostMapping
    public void submit(@RequestBody PictureRequest request){
        System.out.println("It is not a reguraler HttpServletRequest" + servletRequest.getClass());
        pictureService.submit(servletRequest.getRemoteAddr(), request);

    }
    @GetMapping
    public String getAllUsers(){
        return pictureService.getAllUsers()
                .stream()
                .map(u -> String.format("%s, %s <br>", u.firstName(), u.lastName()))
                .collect(Collectors.joining());
    }
}





