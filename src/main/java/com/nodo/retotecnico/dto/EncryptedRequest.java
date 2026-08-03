package com.nodo.retotecnico.dto;

/**
 * Envoltorio del body cifrado que manda el front para endpoints sensibles
 * (login, registro). "data" es el texto cifrado en base64, "iv" es el vector
 * de inicialización (también base64) usado en el cifrado AES-GCM.
 */
public class EncryptedRequest {
    private String data;
    private String iv;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}