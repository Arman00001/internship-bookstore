package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Characters;
import com.arman.internshipbookstore.service.dto.character.CharacterDto;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {
    public Characters mapDtoToCharacter(CharacterDto characterDto){
        Characters characters = new Characters();
        characters.setName(characterDto.getName());

        return characters;
    }

    public CharacterDto mapToDto(Characters characters){
        CharacterDto characterDto = new CharacterDto();
        characterDto.setId(characters.getId());
        characterDto.setName(characterDto.getName());

        return characterDto;
    }
}
