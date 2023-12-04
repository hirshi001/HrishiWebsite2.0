package com.hirshi001.springbootmicroservices.libraries;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GithubSignatureAuthenticator {

    public static boolean verifySignature(String payloadBody, String secretToken, String signatureHeader) throws NoSuchAlgorithmException, InvalidKeyException {

        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secretToken.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmac.init(secretKey);
        byte[] calculatedHmac = hmac.doFinal(payloadBody.getBytes(StandardCharsets.UTF_8));

        // Convert the calculated HMAC to a hexadecimal string
        StringBuilder sb = new StringBuilder();
        sb.append("sha256=");
        for (byte b : calculatedHmac) {
            sb.append(String.format("%02X", b));
        }
        String calculatedSignature = sb.toString().toLowerCase();
        return MessageDigest.isEqual(calculatedSignature.getBytes(StandardCharsets.UTF_8), signatureHeader.getBytes(StandardCharsets.UTF_8));

    }




}
