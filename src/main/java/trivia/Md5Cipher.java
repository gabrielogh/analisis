 package trivia;

//MD5
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import java.util.logging.Level;
import java.util.logging.Logger;


 public class Md5Cipher{ 
  private String hash;

  public Md5Cipher(String pass){
    hash = md5Encode(pass);
  }

  public String getHash(){
    return hash;
  }

  private static String md5Encode(String texto) {
    String output = "";
    try {
      byte[] defaultBytes = texto.getBytes();
      MessageDigest algorithm = MessageDigest.getInstance("MD5");
      algorithm.reset();
      algorithm.update(defaultBytes);
      byte messageDigest[] = algorithm.digest();

      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++) {
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      }
      //String foo = messageDigest.toString();
      
      output = hexString + "";
      
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      System.out.println("Error");
    }
    return output;
  }
}