package com.snowroad.util;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class H2Functions {

    public static Date strToDate(String date, String format) {
        try {
            // SQL 포맷을 Java SimpleDateFormat 포맷으로 변환, "%Y%m%d" -> "yyyyMMdd"
            // 실제 STR_TO_DATE 함수가 받는 포맷은 "%Y%m%d"이므로,
            // H2Functions.strToDate 내부에서 이 포맷을 java.text.SimpleDateFormat에 맞게 변환해야 합니다.
            String javaFormat = format.replace("%Y", "yyyy")
                    .replace("%m", "MM")
                    .replace("%d", "dd");
            SimpleDateFormat sdf = new SimpleDateFormat(javaFormat);
            java.util.Date parsed = sdf.parse(date);
            return new Date(parsed.getTime());
        } catch (Exception e) {
            System.err.println("strToDate - Parsing Error: " + e.getMessage()); // 오류 메시지 출력
            e.printStackTrace(); // 스택 트레이스 출력하여 정확한 원인 확인
            return null;
        }
    }

//    // MySQL DATEDIFF(date1, date2)는 date1 - date2 이므로, H2 DATEDIFF('DAY', date2, date1)과 동일
//    public static Long datediff(Date date1, Date date2) {
//        try {
//            System.out.println("datediff date1:" + date1 +", date2 : " + date2);
//            if (date1 == null || date2 == null) {
//                return null;
//            }
//            LocalDate localDate1 = date1.toLocalDate();
//            LocalDate localDate2 = date2.toLocalDate();
//            return ChronoUnit.DAYS.between(localDate2, localDate1); // (date1 - date2)
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

//    // 3. CONCAT 함수 (H2는 || 또는 CONCAT_WS를 지원하지만, 문자열 연결 함수를 명시적으로 등록)
//    public static String concat(String... args) {
//
//        try {
//            System.out.println("concat args:" + args);
//            if (args == null) {
//                return null;
//            }
//            StringBuilder sb = new StringBuilder();
//            for (String arg : args) {
//                if (arg != null) {
//                    sb.append(arg);
//                }
//            }
//            return sb.toString();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public static Integer findLastPosition(String substring, String string) {
        if (string == null || substring == null || substring.isEmpty()) {
            return 0;
        }
        String reversedString = new StringBuilder(string).reverse().toString();
        String reversedSubstring = new StringBuilder(substring).reverse().toString();

        int locateResult = reversedString.indexOf(reversedSubstring);
        if (locateResult == -1) {
            return 0;
        }
        return string.length() - locateResult;
    }

    /**
     * 문자열을 뒤집습니다. MySQL의 REVERSE() 함수와 동일하게 동작합니다.
     * H2 DB의 사용자 정의 함수로 등록될 예정입니다.
     * @param inputString 뒤집을 문자열
     * @return 뒤집힌 문자열
     */
    public static String reverse(String inputString) {
        if (inputString == null) {
            return null;
        }
        return new StringBuilder(inputString).reverse().toString();
    }

    public static Date curDate() {
        return new Date(System.currentTimeMillis()); // 현재 날짜를 SQL Date 타입으로 반환
    }

}
