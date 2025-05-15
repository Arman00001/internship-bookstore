package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.Characters;
import com.arman.internshipbookstore.persistence.repository.CharacterRepository;
import com.arman.internshipbookstore.service.dto.character.CharacterDto;
import com.arman.internshipbookstore.service.dto.character.CharacterResponseDto;
import com.arman.internshipbookstore.service.exception.CharacterAlreadyExistsException;
import com.arman.internshipbookstore.service.mapper.CharacterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.arman.internshipbookstore.service.util.StringUtils.removeSingleQuotes;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final CharacterMapper characterMapper;


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

    public void assignCharactersOfBook(Book book, String characters) {
        List<Characters> charactersList = createOrGetCharacters(characters);

        for (Characters character : charactersList) {
            book.addCharacter(character);
        }
    }


    private List<Characters> createOrGetCharacters(String characterNames) {
        List<Characters> characterList = new ArrayList<>();

        String[] characters = characterNames.split(", ");

        for (String characterName : characters) {
            String name = removeSingleQuotes(characterName);
            Characters character = characterRepository.getCharactersByName(name);
            if (character == null) {
                character = new Characters();
                character.setName(name);
            }
            characterList.add(character);
        }

        return characterList;
    }
}
