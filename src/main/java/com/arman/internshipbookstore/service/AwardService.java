package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.repository.AwardRepository;
import com.arman.internshipbookstore.service.dto.award.AwardCreateDto;
import com.arman.internshipbookstore.service.dto.award.AwardResponseDto;
import com.arman.internshipbookstore.service.dto.award.AwardUpdateDto;
import com.arman.internshipbookstore.service.exception.AwardAlreadyExistsException;
import com.arman.internshipbookstore.service.exception.AwardNotFoundException;
import com.arman.internshipbookstore.service.mapper.AwardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Award award = awardRepository.findById(id).orElseThrow(() ->
                new AwardNotFoundException("Award with the following id does not exist: " + id));

        return new AwardResponseDto(award.getId(), award.getName());
    }

    public AwardResponseDto getAwardByName(String name) {
        return AwardResponseDto.getAwardResponse(awardRepository.getAwardByName(name));
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

    public AwardResponseDto updateAward(Long id, AwardUpdateDto awardUpdateDto) {
        Award award = awardRepository.findById(id).orElseThrow(() ->
                new AwardNotFoundException("Award with the following id does not exist: " + id));

        if (awardRepository.existsAwardByName(awardUpdateDto.getName())) {
            throw new IllegalArgumentException("Award with the following name already exists: " + awardUpdateDto.getName());
        }

        award.setName(awardUpdateDto.getName());

        return AwardResponseDto.getAwardResponse(awardRepository.save(award));
    }

    public void deleteAward(Long id) {
        Award award = awardRepository.getAwardById(id);
        if (award == null)
            throw new AwardNotFoundException("Award with the following id does not exist: " + id);

        if (!award.getBookAwards().isEmpty())
            throw new IllegalStateException("Cannot delete award: it still has books associated.");

        awardRepository.delete(award);
    }

    public void assignAwardsOfBook(Book book, String awards) {
        Map<Award, List<Integer>> awardYearsMap = getAwardsMap(awards);

        for (Award award : awardYearsMap.keySet()) {
            for (Integer year : awardYearsMap.get(award)) {
                book.addAward(award, year);
            }
        }
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


    private Map<Award, List<Integer>> getAwardsMap(String awardString) {
        Map<Award, List<Integer>> awardYearMap = new HashMap<>();

        awardString = awardString.trim().replace("[", "").replace("]", "");

        for (String awardName : awardString.split(",")) {
            List<String> awardsList = AwardService.splitAwards(awardName.trim());
            for (String awardToken : awardsList) {
                List<Integer> years = AwardService.extractAwardYears(awardToken);

                String cleanAwardName = AwardService.removeYearInfo(awardToken).trim();

                Award award = awardRepository.getAwardByName(cleanAwardName);
                if (award == null)
                    throw new AwardNotFoundException("Award with the following name is not found: " + cleanAwardName);

                if (awardYearMap.containsKey(award)) {
                    awardYearMap.get(award).addAll(years);
                } else {
                    awardYearMap.put(award, new ArrayList<>(years));
                }
            }
        }

        return awardYearMap;
    }
}