package com.MindMate.controller.mediaController;

import com.MindMate.dto.PageResponseDto;
import com.MindMate.dto.mediaDto.ImageResponseDto;
import com.MindMate.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/media/access")
public class MediaAccessController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/getAll")
    public ResponseEntity<PageResponseDto<ImageResponseDto>> getAllMedia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(mediaService.geAllMedia(page,size));
    }
}
