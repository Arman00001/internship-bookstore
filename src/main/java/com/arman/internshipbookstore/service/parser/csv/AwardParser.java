package com.arman.internshipbookstore.service.parser.csv;

import com.arman.internshipbookstore.persistence.entity.Award;
import com.arman.internshipbookstore.service.AwardService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.arman.internshipbookstore.service.util.StringUtils.removeSquareBrackets;

public class AwardParser {

    public static Map<Award, List<Integer>> getAwardsMap(Map<String, Award> awardMap, String awardString) {
        Map<Award, List<Integer>> awardYearMap = new HashMap<>();

        awardString = removeSquareBrackets(awardString.trim());


        Pattern quotePattern = Pattern.compile("(['\"])(.*?)\\1");
        Matcher matcher = quotePattern.matcher(awardString);

        while (matcher.find()) {
            String awardName = matcher.group(2);

            List<String> awardsList = AwardService.splitAwards(awardName);
            for (String awardToken : awardsList) {
                List<Integer> years = AwardService.extractAwardYears(awardToken);

                String cleanAwardName = AwardService.removeYearInfo(awardToken).trim();

                Award award = awardMap.get(cleanAwardName);
                if (award == null) {
                    award = new Award();
                    award.setName(cleanAwardName);
                    awardMap.put(cleanAwardName, award);
                }

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
