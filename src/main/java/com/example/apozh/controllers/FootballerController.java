    package com.example.apozh.controllers;

    import com.example.apozh.Repository.FootballerRepository;
    import com.example.apozh.entity.Footballer;
    import com.example.apozh.service.FootballerService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.List;

    @RestController
    @RequestMapping("/footballers")
    public class FootballerController {
        private final FootballerService footballerService;
        private final FootballerRepository footballerRepository;
        @Value("${footballers.scraping.url}")
        private String footballersScrapingUrl;
        @Autowired
        public FootballerController(FootballerService footballerService, FootballerRepository footballerRepository) {
            this.footballerService = footballerService;
            this.footballerRepository = footballerRepository;
        }
        @GetMapping("/scrape")
        public List<Footballer> scrapeFootballers() {
            return footballerService.scrapeFootballers(footballersScrapingUrl);
        }

        @GetMapping("/all")
        public List<Footballer> all(){
            return footballerRepository.findAll();
        }
    }
