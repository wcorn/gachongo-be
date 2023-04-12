package gcu.project.gachongo.global.util;

import gcu.project.gachongo.global.common.api.ErrorCode;
import gcu.project.gachongo.global.common.exception.CustomException;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 형식 체크하는 곳 */
public class Regex {
    /* 가천대 이메일 인지 */
    public static void isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@gachon.ac.kr";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        if(!matcher.find()){
            throw new CustomException(ErrorCode.REGEX_FAILED_EMAIL);
        }
    }
}