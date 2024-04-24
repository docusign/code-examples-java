package com.docusign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HttpsConnectWebhookhMacValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsConnectWebhookhMacValidation.class);

    /*
     * Useful reference:
     * https://docs.oracle.com/javase/10/docs/api/javax/crypto/Mac.html
     * https://docs.oracle.com/javase/10/docs/specs/security/standard-names.html#mac
     * -algorithms
     */
    //ds-snippet-start:Connect1Step1
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
        boolean isEqual = MessageDigest.isEqual(computedHash.getBytes(StandardCharsets.UTF_8),
                verify.getBytes(StandardCharsets.UTF_8));
        return isEqual;
    }
    //ds-snippet-end:Connect1Step1

    public static void main(String[] args)
    {
        LOGGER.info("DocuSign HMAC Tester");
        try {
            Boolean response = HttpsConnectWebhookhMacValidation.HashIsValid("{DocuSign HMAC private key}",
                    Files.readAllBytes(Paths.get("payload.txt")), "{JSON response Signature}");
            LOGGER.info("is this HMAC Valid? ");
            LOGGER.info(String.valueOf(response));
        } catch (Exception e) {
            LOGGER.error("Error!!!  ");
            LOGGER.error(e.getMessage());
        }
    }
}
