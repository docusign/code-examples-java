package com.docusign.controller.connect.services;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class ValidateUsingHmacService {
    public static String computeHash(String secret, byte[] payload)
            throws InvalidKeyException, NoSuchAlgorithmException {
        String digest = "HmacSHA256";
        Mac mac = Mac.getInstance(digest);
        mac.init(new SecretKeySpec(secret.getBytes(), digest));
        String base64Hash = new String(Base64.getEncoder().encode(mac.doFinal(payload)));
        return base64Hash;
    }

    public static boolean isValid(String secret, byte[] payload, String verify)
            throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String computedHash = computeHash(secret, payload);
        return MessageDigest.isEqual(computedHash.getBytes("UTF-8"),
            verify.getBytes("UTF-8"));
    }
}
