package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Characters;
import com.arman.internshipbookstore.persistence.repository.CharacterRepository;
import com.arman.internshipbookstore.service.dto.CharacterDto;
import com.arman.internshipbookstore.service.mapper.CharacterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    public CharacterDto getCharacterByName(String name){
        Characters character = characterRepository.getCharactersByName(name);

        return characterMapper.mapToDto(character);
    }

    public Characters save(CharacterDto characterDto){
        Characters characters = characterRepository.getCharactersByName(characterDto.getName());
        if(characters!=null) return characters;

        characters = characterMapper.mapDtoToCharacter(characterDto);

        Characters characters1 = characterRepository.save(characters);

        return characters1;
    }

    public List<Characters> findAll() {
        return characterRepository.findAll();
    }
}
