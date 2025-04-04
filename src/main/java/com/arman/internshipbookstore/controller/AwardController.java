package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.persistence.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AwardController {

    private final AwardRepository awardRepository;

    @GetMapping("/getAwardByName")
    public Award getPublisherByName(@RequestParam String name){
        return awardRepository.getAwardByName(name);
    }

    @PostMapping("/addAward")
    public void addPublisher(@RequestBody Award award){
        awardRepository.save(award);
    }
}
