package vn.hust.restaurant.service;
import java.nio.charset.StandardCharsets;import java.security.*;import java.security.spec.*;import java.util.*;import javax.crypto.SecretKeyFactory;import javax.crypto.spec.PBEKeySpec;
public final class SecurityUtil{
 private SecurityUtil(){}
 public static String hash(String raw){try{Class<?>c=Class.forName("org.mindrot.jbcrypt.BCrypt");String salt=(String)c.getMethod("gensalt",int.class).invoke(null,12);return (String)c.getMethod("hashpw",String.class,String.class).invoke(null,raw,salt);}catch(Exception e){return pbkdf2(raw);}}
 public static boolean verify(String raw,String stored){if(stored==null)return false;try{if(stored.startsWith("$2")){Class<?>c=Class.forName("org.mindrot.jbcrypt.BCrypt");return (Boolean)c.getMethod("checkpw",String.class,String.class).invoke(null,raw,stored);}if(stored.startsWith("PBKDF2$")){String[]p=stored.split("\\$");byte[]salt=Base64.getDecoder().decode(p[1]);byte[]want=Base64.getDecoder().decode(p[2]);byte[]got=derive(raw,salt);return MessageDigest.isEqual(want,got);}return false;}catch(Exception e){return false;}}
 private static String pbkdf2(String raw){try{byte[]salt=new byte[16];SecureRandom.getInstanceStrong().nextBytes(salt);return "PBKDF2$"+Base64.getEncoder().encodeToString(salt)+"$"+Base64.getEncoder().encodeToString(derive(raw,salt));}catch(Exception e){throw new IllegalStateException(e);}}
 private static byte[]derive(String raw,byte[]salt)throws Exception{return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(raw.toCharArray(),salt,120000,256)).getEncoded();}
 public static String otp(){return String.format("%06d",new SecureRandom().nextInt(1_000_000));}
}
