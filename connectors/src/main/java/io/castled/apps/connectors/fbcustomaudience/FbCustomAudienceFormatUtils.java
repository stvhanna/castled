package io.castled.apps.connectors.fbcustomaudience;

import io.castled.apps.connectors.fbcustomaudience.client.dtos.FbAudienceUserFields;
import io.castled.exceptions.CastledRuntimeException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.MonthDay;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FbCustomAudienceFormatUtils {

    public static Map<String, FbAudienceUserFields> fieldEnumMap = Arrays.stream(FbAudienceUserFields.values())
            .collect(Collectors.toMap(FbAudienceUserFields::getDisplayName, Function.identity()));

    public static String formatValue(Object value, String fieldName) {
        FbAudienceUserFields field = fieldEnumMap.get(fieldName);
        String val = (String) value;
        switch (field) {
            case DOBY:
                DateTimeFormatter formatY = DateTimeFormatter.ofPattern("yyyy");
                return Year.of(Integer.parseInt(val)).format(formatY);
            case DOBM:
                DateTimeFormatter formatM = DateTimeFormatter.ofPattern("MM");
                return MonthDay.of(Integer.parseInt(val), 1).format(formatM);
            case DOBD:
                DateTimeFormatter formatD =  DateTimeFormatter.ofPattern( "dd");
                return MonthDay.of(1, Integer.parseInt(val)).format(formatD);
            case EMAIL:
            case PHONE:
            case LN:
            case FN:
            case ZIP:
            case CT:
            case ST:
            case COUNTRY:
            case MADID:
                return toNormalizedString(val);
            case GEN:
                return normalizeGender(val);
            case EXTERN_ID:
                return val;
            default:
                throw new CastledRuntimeException("Invalid customer filed type!");
        }
    }

    public static String normalizeGender(String gender) {
        String nGender = toNormalizedString(gender);
        if ("male".contains(nGender)) {
            return "m";
        } else {
            return "f";
        }
    }

    public static String hashValue(String value, String fieldName) {
        FbAudienceUserFields field = fieldEnumMap.get(fieldName);
        if (field.isHashable()) {
            return toSHA256String(value);
        } else {
            return value;
        }
    }

    private static String toSHA256String(String str) {
        MessageDigest digest = getSHA256MessageDigest();
        byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static MessageDigest getSHA256MessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Missing SHA-256 algorithm implementation.", e);
        }
    }

    private static String toNormalizedString(String value) {
        return value.trim().toLowerCase();
    }
}
