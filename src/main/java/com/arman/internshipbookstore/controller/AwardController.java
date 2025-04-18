package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.service.AwardService;
import com.arman.internshipbookstore.service.dto.AwardDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public AwardDto addAward(@RequestBody @Valid AwardDto awardDto){
        return awardService.addAward(awardDto);
    }

    @DeleteMapping("/deleteAward")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAward(@RequestParam("id") Long id){
        awardService.delete(id);
    }
}
