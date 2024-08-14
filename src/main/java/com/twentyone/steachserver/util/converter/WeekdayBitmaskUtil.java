package com.twentyone.steachserver.util.converter;

public class WeekdayBitmaskUtil {
    public static byte convert(String bitmaskString) {
        if (bitmaskString.length() != 7) {
            throw new RuntimeException("bitmask 이상함");
        }

        if (!bitmaskString.matches("[01]*")) {
            throw new RuntimeException("bitmask 이상함");
        }

        return (byte) Integer.parseInt(bitmaskString, 2);
    }

    public static String convert(byte bitmask) {
        String weekDaysBitmaskString = Integer.toBinaryString(bitmask);

        // 길이가 7이 되도록 0으로 패딩
        String paddedWeekDaysBitmask = String.format("%0" + 7 + "d", Integer.parseInt(weekDaysBitmaskString));

        return paddedWeekDaysBitmask;
    }
}
