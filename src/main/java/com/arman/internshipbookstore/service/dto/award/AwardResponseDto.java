package com.arman.internshipbookstore.service.dto.award;

import com.arman.internshipbookstore.persistence.entity.Award;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwardResponseDto {

    private Long id;

    private String name;

    public AwardResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AwardResponseDto getAwardResponse(Award award){
        return new AwardResponseDto(award.getId(),award.getName());
    }
}
