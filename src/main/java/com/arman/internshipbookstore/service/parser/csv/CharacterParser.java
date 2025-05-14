package com.arman.internshipbookstore.service.parser.csv;

import com.arman.internshipbookstore.persistence.entity.Characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.arman.internshipbookstore.service.util.StringUtils.removeSingleQuotes;

public class CharacterParser {

    public static List<Characters> createOrGetCharacters(Map<String, Characters> characterMap, String characterNames) {
        List<Characters> characterList = new ArrayList<>();

        String[] characters = characterNames.split(", ");

        for (String characterName : characters) {
            String name = removeSingleQuotes(characterName);
            Characters character = characterMap.get(name);
            if (character == null) {
                character = new Characters();
                character.setName(name);
                characterMap.put(name, character);
            }
            characterList.add(character);
        }

        return characterList;
    }
}
