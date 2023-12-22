package com.example.apozh.controllers;

import com.example.apozh.Repository.PhotoRepository;
import com.example.apozh.entity.Photo;
import com.example.apozh.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/scrapeOne")
    public CompletableFuture<List<Photo>> scrapePhotos() {
        int blockNumber = 1;
        return CompletableFuture.supplyAsync(() -> photoService.scrapeAndSavePhotos(blockNumber));
    }
    @GetMapping("/scrapeTwo")
    public CompletableFuture<List<Photo>> scrapePhotosTwo() {
        int blockNumber = 2;
        return CompletableFuture.supplyAsync(() -> photoService.scrapeAndSavePhotos(blockNumber));
    }
    @GetMapping("/blockOne")
    public List<Photo> blockOne(){
        System.out.println(photoRepository.findByBlockNumber(1));
        return photoRepository.findByBlockNumber(1);
    }
    @GetMapping("/blockTwo")
    public List<Photo> blockTwo(){
        System.out.println(photoRepository.findByBlockNumber(2));
        return photoRepository.findByBlockNumber(2);
    }
    @GetMapping("/all")
    public List<Photo> all(){
        System.out.println(photoRepository.findAll());
       return photoRepository.findAll();
    }
}
