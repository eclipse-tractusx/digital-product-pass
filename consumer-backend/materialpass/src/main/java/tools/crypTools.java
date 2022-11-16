package tools;

import java.util.Base64;

public class crypTools {
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


}
