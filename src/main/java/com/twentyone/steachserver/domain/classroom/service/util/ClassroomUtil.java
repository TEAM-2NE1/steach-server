package com.twentyone.steachserver.domain.classroom.service.util;

public class ClassroomUtil {
    public static String generateSessionId(Integer lectureId) {
        try {
            String currentTime = String.valueOf(System.currentTimeMillis());
            String dataToEncrypt = lectureId + ":" + currentTime;
            return EncryptionUtil.encrypt(dataToEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptSessionId(String sessionId) {
        try {
            String decryptedData = EncryptionUtil.decrypt(sessionId);
            return decryptedData.split(":")[0]; // Extracting the lectureId part
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
