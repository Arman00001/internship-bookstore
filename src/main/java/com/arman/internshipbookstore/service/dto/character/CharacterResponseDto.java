package com.arman.internshipbookstore.service.dto.character;

import com.arman.internshipbookstore.persistence.entity.Characters;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CharacterResponseDto {

    private Long id;

    private String name;

    public CharacterResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<CharacterResponseDto> getCharacterResponseList(Set<Characters> characters) {
        List<CharacterResponseDto> characterResponseDtos = new ArrayList<>();
        for (Characters character : characters) {
            CharacterResponseDto characterResponseDto = new CharacterResponseDto(character.getId(),character.getName());
            characterResponseDtos.add(characterResponseDto);
        }

        return characterResponseDtos;
    }
}
