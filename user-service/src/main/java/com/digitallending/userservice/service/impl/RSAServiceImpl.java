package com.digitallending.userservice.service.impl;

import com.digitallending.userservice.exception.RSADecryptionException;
import com.digitallending.userservice.exception.RSAEncryptionException;
import com.digitallending.userservice.service.def.RSAService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class RSAServiceImpl implements RSAService {

    @Value("${rsa_public_key}")
    private String publicKeyString;


    @Value("${rsa_private_key}")
    private String privateKeyString;

    @Value("${cipher_instance_type}")
    private String cipherInstanceType;

    @Value("${encryption_algo}")
    private String encryptionAlgorithm;


    @Override
    public String getPublicKey() {
        return publicKeyString;
    }

    @Override
    public String encodeMessage(String string) {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString.getBytes());
        byte[] encodedStringByte;

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(encryptionAlgorithm);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Cipher cipher = Cipher.getInstance(cipherInstanceType);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedStringByte = cipher.doFinal(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
                 InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            throw new RSAEncryptionException(e.getMessage());
        }


        return Base64.getEncoder().encodeToString(encodedStringByte);
    }


    @Override
    public String decodeMessage(String encodedString) {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString.getBytes());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        byte[] decodedStringByte;

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(encryptionAlgorithm);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            Cipher cipher = Cipher.getInstance(cipherInstanceType);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decodedStringByte = cipher.doFinal(Base64.getDecoder().decode(encodedString));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
                 InvalidKeySpecException | BadPaddingException | InvalidKeyException e) {
            throw new RSADecryptionException(e.getMessage());
        }


        return new String(decodedStringByte, StandardCharsets.UTF_8);
    }

}
