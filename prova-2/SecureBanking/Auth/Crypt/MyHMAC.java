package Auth.Crypt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class MyHMAC {
    public static final String ALG = "HmacSHA256";

    public static String hMac(String chave, String mensagem) {
        try {
            Mac shaHMAC = Mac.getInstance(ALG);
            SecretKeySpec chaveMAC = new SecretKeySpec(chave.getBytes(StandardCharsets.UTF_8), ALG);
            shaHMAC.init(chaveMAC);
            byte[] bytesHMAC = shaHMAC.doFinal(mensagem.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytesHMAC);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}