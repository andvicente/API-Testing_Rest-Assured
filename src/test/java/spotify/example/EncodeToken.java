package spotify.example;

import org.apache.commons.codec.binary.Base64;

public class EncodeToken {

    public static String getAuthToken(String clientid, String clientSecret){
        String idSecret = clientid +":"+clientSecret;
        byte[] bytesEncoded = Base64.encodeBase64(idSecret.getBytes());
        return new String(bytesEncoded);
    }

}
