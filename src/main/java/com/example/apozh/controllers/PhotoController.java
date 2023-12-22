package com.example.apozh.controllers;

import com.example.apozh.Repository.PhotoRepository;
import com.example.apozh.entity.Photo;
import com.example.apozh.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoController(PhotoService photoService, PhotoRepository photoRepository) {
        this.photoService = photoService;
        this.photoRepository = photoRepository;
    }

    @GetMapping("/scrape")
    public CompletableFuture<List<Photo>> scrapePhotos() {
        return CompletableFuture.supplyAsync(() -> photoService.scrapeAndSavePhotos());
    }
    @GetMapping("/all")
    public List<Photo> all(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Photo> photoPage = photoRepository.findAll(pageable);
        List<Photo> photos = photoPage.getContent();
        System.out.println(photos);
        return photos;
    }

}
