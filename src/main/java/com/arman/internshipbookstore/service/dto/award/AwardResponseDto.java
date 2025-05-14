package com.arman.internshipbookstore.service.dto.award;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AwardResponseDto {

    private Long id;

    private String name;

    private Integer year;

    public AwardResponseDto(Long id,
                            String name,
                            Integer year){
        this.id=id;
        this.name=name;
        this.year=year;
    }
}
