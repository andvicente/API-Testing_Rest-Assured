package spotify.example;

import org.apache.commons.codec.binary.Base64;

public class EncodeToken {

    public static void main(String[] args) {
        String clientid = "";
        String clientSecret = "";

        String idSecret = clientid +":"+clientSecret;
        byte[] bytesEncoded = Base64.encodeBase64(idSecret.getBytes());
        System.out.println("encoded value is " + new String(bytesEncoded));
    }
}
