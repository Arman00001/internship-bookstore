package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.service.dto.AwardDto;
import org.springframework.stereotype.Component;

@Component
public class AwardMapper {
    public Award mapDtoToAward(AwardDto awardDto){
        Award award = new Award();
        award.setName(awardDto.getName());

        return award;
    }

    public AwardDto mapToDto(Award award){
        AwardDto awardDto = new AwardDto();
        awardDto.setId(award.getId());
        awardDto.setName(award.getName());

        return awardDto;
    }
}
