package com.twentyone.steachserver.util.converter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static String convert(LocalDate localDate) {
        // 시간 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // LocalTime을 지정된 형식의 문자열로 변환
        String formattedTime = localDate.format(formatter);

        return formattedTime;
    }

    public static String convert(LocalTime localTime) {
        // 시간 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // LocalTime을 지정된 형식의 문자열로 변환
        String formattedTime = localTime.format(formatter);

        return formattedTime;
    }
}
