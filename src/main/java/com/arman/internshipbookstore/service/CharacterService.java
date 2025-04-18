package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Characters;
import com.arman.internshipbookstore.persistence.repository.CharacterRepository;
import com.arman.internshipbookstore.service.dto.CharacterDto;
import com.arman.internshipbookstore.service.exception.CharacterAlreadyExistsException;
import com.arman.internshipbookstore.service.mapper.CharacterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;

    public Characters getCharacterByName(String name){
        return characterRepository.getCharactersByName(name);
    }

    public Characters save(CharacterDto characterDto){
        Characters characters = characterRepository.getCharactersByName(characterDto.getName());
        if(characters!=null) return characters;

        characters = characterMapper.mapDtoToCharacter(characterDto);

        Characters characters1 = characterRepository.save(characters);

        return characters1;
    }

    public CharacterDto addCharacter(CharacterDto characterDto){
        if(characterRepository.getCharactersByName(characterDto.getName())==null){
            Characters character = characterMapper.mapDtoToCharacter(characterDto);

            Characters character1 = characterRepository.save(character);

            return characterMapper.mapToDto(character1);
        }

        throw new CharacterAlreadyExistsException("Character with the following name already exists: "+characterDto.getName());
    }

    public List<Characters> findAll() {
        return characterRepository.findAll();
    }

    public void deleteCharacterOfBook(Characters character) {
        characterRepository.delete(character);
    }
}
