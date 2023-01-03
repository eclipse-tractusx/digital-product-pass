package tools;

import com.google.common.hash.Hashing;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class crypTools {
    private crypTools() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }
    public static String toBase64(String str){
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
    public static String fromBase64(String base64){
        return new String(Base64.getDecoder().decode(base64));
    }
    public static String toBase64Url(String str){
        return Base64.getUrlEncoder().encodeToString(str.getBytes());
    }
    public static String fromBase64Url(String base64){
        return new String(Base64.getUrlDecoder().decode(base64));
    }


    public static String sha256(String digest){
        return Hashing.sha256()
                .hashString(digest, StandardCharsets.UTF_8)
                .toString();
    }
    public static String decodeFromUtf8(String encodedURL){
        return URLDecoder.decode(encodedURL, StandardCharsets.UTF_8);
    }
    public static String encodeToUtf8(String decodedURL){
        return URLEncoder.encode(decodedURL, StandardCharsets.UTF_8);
    }

}
