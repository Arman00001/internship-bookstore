package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.service.AwardService;
import com.arman.internshipbookstore.service.dto.AwardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @GetMapping("/getAwardByName")
    public Award getAwardByName(@RequestParam String name){
        return awardService.getAwardByName(name);
    }

    @PostMapping("/addAward")
    public void addAward(@RequestBody AwardDto awardDto){
        awardService.save(awardDto);
    }
}
