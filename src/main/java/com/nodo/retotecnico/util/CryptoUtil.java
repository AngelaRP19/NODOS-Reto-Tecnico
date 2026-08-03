package com.nodo.retotecnico.util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cifra/descifra el body de las peticiones sensibles (login, registro) con
 * AES-256-GCM, usando una clave simétrica compartida con el front
 * (crypto.secret-key en application.yml, mismo valor en VITE_CRYPTO_SECRET_KEY
 * del front). El front cifra antes de enviar; acá se descifra al recibir.
 *
 * Nota: esto protege lo que se ve al inspeccionar el payload de la petición
 * (Network tab), pero no reemplaza a HTTPS como protección contra quien
 * intercepta el tráfico en la red.
 */
public class CryptoUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH_BITS = 128;
    private static final int IV_LENGTH_BYTES = 12;

    private CryptoUtil() {
    }

    public static String decrypt(String base64CipherText, String base64Iv, String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        byte[] iv = Base64.getDecoder().decode(base64Iv);
        byte[] cipherBytes = Base64.getDecoder().decode(base64CipherText);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

        byte[] plainBytes = cipher.doFinal(cipherBytes);
        return new String(plainBytes, "UTF-8");
    }

    /** Devuelve [cipherTextBase64, ivBase64]. Se usa sobre todo para probar el flujo desde el back. */
    public static String[] encrypt(String plainText, String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        byte[] iv = new byte[IV_LENGTH_BYTES];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

        byte[] cipherBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

        return new String[] {
            Base64.getEncoder().encodeToString(cipherBytes),
            Base64.getEncoder().encodeToString(iv)
        };
    }
}