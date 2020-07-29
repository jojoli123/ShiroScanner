package com.core;

import java.security.AlgorithmParameters;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//import org.apache.commons.codec.binary.Base64;

public class AesCrypt {
	public static String encrypt(String key, byte[] payload){
        try{
//            byte[] raw = Base64.decodeBase64(key);
        	Decoder decoder = Base64.getDecoder();
        	Encoder encoder=Base64.getEncoder();
        	byte[] raw=decoder.decode(key);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameters params = cipher.getParameters();
            byte[] ivs = params.getParameterSpec(IvParameterSpec.class).getIV();
            IvParameterSpec iv = new IvParameterSpec(ivs);
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(payload);

            byte[] result = new byte[ivs.length + encrypted.length];
            System.arraycopy(ivs,0, result, 0, ivs.length);
            System.arraycopy(encrypted, 0, result, ivs.length, encrypted.length);

            return encoder.encodeToString(result);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
