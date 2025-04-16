package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.persistence.repository.AwardRepository;
import com.arman.internshipbookstore.service.dto.AwardDto;
import com.arman.internshipbookstore.service.mapper.AwardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;

    public Award getAwardByName(String name){
        return awardRepository.getAwardByName(name);
    }

    public Award save(AwardDto awardDto){
        Award award = awardMapper.mapDtoToAward(awardDto);

        Award award1 = awardRepository.save(award);

        return award1;
    }

    public Set<String> findAllAwardNames() {
        return awardRepository.findAllAwardNames();
    }

    public List<Award> findAll() {
        return awardRepository.findAll();
    }
}