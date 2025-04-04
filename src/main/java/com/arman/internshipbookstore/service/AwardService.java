package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.persistence.repository.AwardRepository;
import com.arman.internshipbookstore.persistence.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;

    public Award getAwardByName(String name){
        return awardRepository.getAwardByName(name);
    }

    public Award save(Award award){
        awardRepository.save(award);

        Award award1 = awardRepository.getAwardByName(award.getName());

        return award1;
    }
}