package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.persistence.repository.AwardRepository;
import com.arman.internshipbookstore.service.dto.award.AwardCreateDto;
import com.arman.internshipbookstore.service.dto.award.AwardResponseDto;
import com.arman.internshipbookstore.service.exception.AwardAlreadyExistsException;
import com.arman.internshipbookstore.service.exception.AwardNotFoundException;
import com.arman.internshipbookstore.service.mapper.AwardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;


    public List<Award> findAll() {
        return awardRepository.findAll();
    }

    public AwardResponseDto getAwardResponseById(Long id) {
        Award award = awardRepository.getAwardById(id);

        if (award == null) throw new AwardNotFoundException("Award with the following id does not exist: " + id);

        return new AwardResponseDto(award.getId(), award.getName());
    }

    public Award getAwardByName(String name) {
        return awardRepository.getAwardByName(name);
    }

    public AwardResponseDto addAward(AwardCreateDto awardCreateDto) {
        Award award = awardRepository.getAwardByName(awardCreateDto.getName());
        if (award == null) {
            award = awardMapper.mapCreateDtoToAward(awardCreateDto);

            Award award1 = awardRepository.save(award);

            return new AwardResponseDto(award1.getId(), award1.getName());
        }
        throw new AwardAlreadyExistsException("Award with the following name already exists: " + awardCreateDto.getName());
    }

    public void deleteAward(Long id) {
        Award award = awardRepository.getAwardById(id);
        if (award == null)
            throw new AwardNotFoundException("Award with the following id does not exist: " + id);

        if (!award.getBookAwards().isEmpty())
            throw new IllegalStateException("Cannot delete award: it still has books associated.");

        awardRepository.delete(award);
    }


    public static List<String> splitAwards(String awardNames) {
        List<String> awards = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int parenthesesLevel = 0;
        int i = 0;
        while (i < awardNames.length()) {
            char c = awardNames.charAt(i);
            if (c == '(') {
                parenthesesLevel++;
            } else if (c == ')') {
                parenthesesLevel--;
            }

            if (parenthesesLevel == 0 && i + 5 <= awardNames.length() && awardNames.substring(i, i + 5).equals(" and ")) {
                awards.add(sb.toString().trim());
                sb.setLength(0);
                i += 5;
                continue;
            }
            sb.append(c);
            i++;
        }
        if (sb.length() > 0) {
            awards.add(sb.toString().trim());
        }
        return awards;
    }

    public static List<Integer> extractAwardYears(String input) {
        List<Integer> years = new ArrayList<>();
        Pattern yearPattern = Pattern.compile("\\((\\d{4})\\)");
        Matcher matcher = yearPattern.matcher(input);
        while (matcher.find()) {
            years.add(Integer.parseInt(matcher.group(1)));
        }

        return years;
    }

    public static String removeYearInfo(String input) {
        return input.replaceAll("\\(\\d{4}\\)", "").trim();
    }
}