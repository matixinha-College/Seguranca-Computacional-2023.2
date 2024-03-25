package Auth.Crypt;

import java.util.Base64;

public class MyBase64 {
    public static String codificar(String msg) {
        byte[] bytesMsg = msg.getBytes();
        return Base64.getEncoder().encodeToString(bytesMsg);
    }

    public static String decodificar(String msgBase64) {
        byte[] bytesMsgBase64 = Base64.getDecoder().decode(msgBase64);
        return new String(bytesMsgBase64);
    }
}
