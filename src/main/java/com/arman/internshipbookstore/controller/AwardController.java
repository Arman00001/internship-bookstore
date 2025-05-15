package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.service.AwardService;
import com.arman.internshipbookstore.service.dto.award.AwardCreateDto;
import com.arman.internshipbookstore.service.dto.award.AwardDto;
import com.arman.internshipbookstore.service.dto.award.AwardOfBookResponseDto;
import com.arman.internshipbookstore.service.dto.award.AwardResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @GetMapping("/{id}")
    public AwardResponseDto getAwardById(@PathVariable("id") Long id){
        return awardService.getAwardResponseById(id);
    }

    @GetMapping("/name")
    public Award getAwardByName(@RequestParam String name){
        return awardService.getAwardByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AwardResponseDto addAward(@RequestBody @Valid AwardCreateDto awardCreateDto){
        return awardService.addAward(awardCreateDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAward(@RequestParam("id") Long id){
        awardService.deleteAward(id);
    }
}
