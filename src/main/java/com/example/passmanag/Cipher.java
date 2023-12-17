package com.example.passmanag;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Cipher {

    public static void main(String[] args) throws Exception {
        NewDataWindow newDataWindow = new NewDataWindow();
        String originalString = newDataWindow.getNewlogfield().getText();
        String encryptedString = "";

        //AES-256
        SecretKey secretKeyAES = generateAESKey();

        //TwoFish
        byte[] secretKeyTwo = generateTwoFishKey();

    }
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    public static  byte[] generateTwoFishKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);
        return keyBytes;
    }
    public static String encryptTwoFish(String plaintext, byte[] key){
        BlockCipher cipher = new TwofishEngine();
        BufferedBlockCipher cipherWithMode = new BufferedBlockCipher(new CFBBlockCipher(cipher, cipher.getBlockSize()));

        CipherParameters keyParam = new KeyParameter(key);

        // Генерация случайного вектора инициализации
        byte[] iv = new byte[cipher.getBlockSize()];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        CipherParameters paramsWithIV = new ParametersWithIV(keyParam, iv);

        cipherWithMode.init(true, paramsWithIV);

        byte[] input = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] output = new byte[cipherWithMode.getOutputSize(input.length)];
        int length = cipherWithMode.processBytes(input, 0, input.length, output, 0);
        try {
            cipherWithMode.doFinal(output, length);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }

        // Вектор инициализации добавляется к зашифрованным данным
        byte[] result = new byte[iv.length + output.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(output, 0, result, iv.length, output.length);

        return new String(result, StandardCharsets.ISO_8859_1);
    }
    public static String decryptTwoFish(String ciphertext, byte[] key) throws Exception {
        BlockCipher cipher = new TwofishEngine();
        BufferedBlockCipher cipherWithMode = new BufferedBlockCipher(new CFBBlockCipher(cipher, cipher.getBlockSize()));

        CipherParameters keyParam = new KeyParameter(key);

        byte[] data = ciphertext.getBytes(StandardCharsets.ISO_8859_1);

        // Извлечение вектора инициализации из зашифрованных данных
        byte[] iv = new byte[cipher.getBlockSize()];
        System.arraycopy(data, 0, iv, 0, iv.length);
        CipherParameters paramsWithIV = new ParametersWithIV(keyParam, iv);

        cipherWithMode.init(false, paramsWithIV);

        byte[] input = new byte[data.length - iv.length];
        System.arraycopy(data, iv.length, input, 0, input.length);
        byte[] output = new byte[cipherWithMode.getOutputSize(input.length)];
        int length = cipherWithMode.processBytes(input, 0, input.length, output, 0);
        cipherWithMode.doFinal(output, length);

        return new String(output, StandardCharsets.UTF_8);
    }

    public static String encryptAES(String input, Key key) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);

        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = cipher.doFinal(inputBytes);

        return bytesToHex(encryptedBytes);

    }

    public static String decryptAES(String encryptedInput, SecretKey key) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("  AES");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);

        byte[] encryptedBytes = hexToBytes(encryptedInput);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }



    // Вспомогательные методы для преобразования байтов в шестнадцатеричную строку и обратно
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}