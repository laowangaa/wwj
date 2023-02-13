package cn.cyberict.ncha.business.commons.utils;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;


@SuppressWarnings("restriction")
public class DesUtil {

    private final static String DES = "DES";

    public final static String key = "e69bb9e69da8e68891e788b1e4bda0e59180";

    public static void main(String[] args) throws Exception {
        String data = "RAES";

        String msg = encrypt(data);
        System.out.println(msg);
        String decryptResult = decrypt(msg);
        System.out.println(decryptResult);
    }

    
    public static String encrypt(String msg) throws Exception {
        return encrypt(msg, key);
    }

    
    public static String decrypt(String msg) throws Exception {
        return decrypt(msg, key);
    }

    
    public static String encrypt(String msg, String key) throws Exception {
        byte[] data = msg.getBytes("utf-8");
        byte[] realKey = key.getBytes("utf-8");
        
        SecureRandom sr = new SecureRandom();

        
        DESKeySpec dks = new DESKeySpec(realKey);

        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        
        Cipher cipher = Cipher.getInstance(DES);

        
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return new String(Base64Utils.encode(cipher.doFinal(data)), "utf-8");
    }

    
    public static String decrypt(String msg, String key) throws Exception {
        byte[] data = Base64Utils.decode(msg.getBytes("utf-8"));
        byte[] realKey = key.getBytes("utf-8");
        
        SecureRandom sr = new SecureRandom();

        
        DESKeySpec dks = new DESKeySpec(realKey);

        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        
        Cipher cipher = Cipher.getInstance(DES);

        
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return new String(cipher.doFinal(data), "utf-8");
    }
}
