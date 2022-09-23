import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class httpsconnectwebhookhmacvalidation {
    /*
     * Useful reference:
     * https://docs.oracle.com/javase/10/docs/api/javax/crypto/Mac.html
     * https://docs.oracle.com/javase/10/docs/specs/security/standard-names.html#mac
     * -algorithms
     */
    private static String ComputeHash(String secret, byte[] payload)
            throws InvalidKeyException, NoSuchAlgorithmException {
        String digest = "HmacSHA256";
        Mac mac = Mac.getInstance(digest);
        mac.init(new SecretKeySpec(secret.getBytes(), digest));
        String base64Hash = new String(Base64.getEncoder().encode(mac.doFinal(payload)));
        return base64Hash;
    }

    public static boolean HashIsValid(String secret, byte[] payload, String verify)
            throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String computedHash = ComputeHash(secret, payload);
        boolean isEqual = MessageDigest.isEqual(computedHash.getBytes("UTF-8"),
                verify.getBytes("UTF-8"));
        return isEqual;
    }

    public static void main(String[] args) {

        System.out.println("DocuSign HMAC Tester");
        try {

            Boolean response = httpsconnectwebhookhmacvalidation.HashIsValid("{DocuSign HMAC private key}",
                    Files.readAllBytes(Paths.get("payload.txt")), "{JSON response Signature}");
            System.out.printf("is this HMAC Valid? ");
            System.out.println(response);

        } catch (Exception e) {

            System.out.print("Error!!!  ");
            System.out.print(e.getMessage());

        }

    }

}
