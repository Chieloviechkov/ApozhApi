package com.example.apozh.service;

import com.example.apozh.Repository.PhotoRepository;
import com.example.apozh.entity.Photo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private String currentPageHash = "";
    @Value("${photo.scraping.url}")
    private String scrapingUrl;
    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo> scrapeAndSavePhotos(int blockNumber) {
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);
        String url = scrapingUrl;
        driver.get(url);

        String pageContent = driver.getPageSource();
        String currentPageHash = calculateHash(pageContent);

        if (!currentPageHash.equals(this.currentPageHash)) {
            this.currentPageHash = currentPageHash;

            List<WebElement> photoElements = driver.findElements(By.className("photo__item"));
            List<String> photoUrls = new ArrayList<>();

            int startBlock = (blockNumber - 1) * 6;
            int endBlock = blockNumber * 6;

            for (int i = startBlock; i < endBlock && i < photoElements.size(); i++) {
                WebElement photoElement = photoElements.get(i);
                String imageUrl = photoElement.findElement(By.tagName("a")).getAttribute("href");
                photoUrls.add(imageUrl);
            }

            driver.quit();
            List<Photo> photos = processPhotoUrls(photoUrls, blockNumber);
            photoRepository.saveAll(photos);

            return photos;
        } else {
            driver.quit();
            return Collections.emptyList();
        }
    }
    public List<Photo> getPhotosByBlockNumber(int blockNumber) {
        return photoRepository.findByBlockNumber(blockNumber);
    }

    private String calculateHash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            BigInteger bigInt = new BigInteger(1, hash);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<Photo> processPhotoUrls(List<String> photoUrls, int blockNumber) {
        List<Photo> photos = new ArrayList<>();

        for (String imageUrl : photoUrls) {
            try {
                Document doc = Jsoup.connect(imageUrl).get();
                String title = doc.select("h2.media-title").text();

                Elements mediaItems = doc.select("img.js-media-item");

                for (Element mediaItem : mediaItems) {
                    imageUrl = mediaItem.attr("data-image");

                    if (!photoRepository.existsByImageUrl(imageUrl)) {
                        Photo photo = new Photo();
                        photo.setImageUrl(imageUrl);
                        photo.setTitle(title);
                        photo.setBlockNumber(blockNumber);

                        photos.add(photo);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return photos;
    }
}
