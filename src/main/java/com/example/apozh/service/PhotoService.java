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

    public List<Photo> scrapeAndSavePhotos() {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");
        WebDriver driver = new FirefoxDriver(options);
        String url = scrapingUrl;
        driver.get(url);

        String pageContent = driver.getPageSource();
        String currentPageHash = calculateHash(pageContent);

        if (!currentPageHash.equals(this.currentPageHash)) {
            this.currentPageHash = currentPageHash;

            List<WebElement> photoElements = driver.findElements(By.className("photo__item"));
            List<String> photoUrls = new ArrayList<>();

            for (WebElement photoElement : photoElements) {
                String imageUrl = photoElement.findElement(By.tagName("a")).getAttribute("href");
                photoUrls.add(imageUrl);
            }

            driver.quit();
            List<Photo> photos = processPhotoUrls(photoUrls);
            photoRepository.saveAll(photos);

            return photos;
        } else {
            driver.quit();
            return Collections.emptyList();
        }
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

    public List<Photo> processPhotoUrls(List<String> photoUrls) {
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
