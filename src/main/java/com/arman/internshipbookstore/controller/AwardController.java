package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.service.AwardService;
import com.arman.internshipbookstore.service.criteria.AwardSearchCriteria;
import com.arman.internshipbookstore.service.dto.PageResponseDto;
import com.arman.internshipbookstore.service.dto.award.AwardCreateDto;
import com.arman.internshipbookstore.service.dto.award.AwardResponseDto;
import com.arman.internshipbookstore.service.dto.award.AwardUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @GetMapping("/{id}")
    public ResponseEntity<AwardResponseDto> getAwardById(@PathVariable("id") Long id){
        return ResponseEntity.ok(awardService.getAwardResponseById(id));
    }

    @GetMapping("/name")
    public PageResponseDto<AwardResponseDto> getAwardByName(@ModelAttribute @Valid AwardSearchCriteria criteria){
        return awardService.getAwardsByName(criteria);
    }

    @PostMapping
    public ResponseEntity<AwardResponseDto> addAward(@RequestBody @Valid AwardCreateDto awardCreateDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(awardService.addAward(awardCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AwardResponseDto> updateAward(@PathVariable("id") Long id,
                                                        AwardUpdateDto awardUpdateDto){
        return ResponseEntity.ok(awardService.updateAward(id,awardUpdateDto));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAward(@RequestParam("id") Long id){
        awardService.deleteAward(id);
    }
}
