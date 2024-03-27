package Auth.Crypt;

public class Util {

    private static MyAES myaes = new MyAES();
    private static String HMac_Key;
    private static String Vernam_Key = "chave456";
    private static String splitter = "||";

    public Util() {
    }


    public static String EncryptMessage(String message, int simetricSeed, String privateKey) {
        myaes.gerarChave(simetricSeed);
        try {
            String aesMessage = myaes.cifrar(message);
            String hash = MyHMAC.hMac(HMac_Key, aesMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DecryptMessage(String message) {
        try {
            String[] parts = message.split("\\|\\|");
             // Corrigido para escapar o caractere '|'
            if (parts.length < 2) {
                throw new Exception("Mensagem recebida inválida: não contém delimitador '||'.");
            }
            String messagebase64 = parts[0];
            String decryptVernam = parts[1];

            String decryptedAES = MyBase64.decodificar(messagebase64);
            if (decryptedAES == null || decryptedAES.isEmpty()) {
                throw new Exception("Erro ao decodificar a parte Base64 da mensagem.");
            }

            String FinalHash = myaes.decifrar(decryptedAES);
            if (FinalHash == null || FinalHash.isEmpty()) {
                throw new Exception("Erro ao decifrar a parte AES da mensagem.");
            }

            String decryptedMessage = MyCifraVernam.decifrar(decryptVernam, Vernam_Key);
            String HMACfinal = MyHMAC.hMac(HMac_Key, decryptedMessage);

            if (FinalHash.equals(HMACfinal)) {
                return decryptedMessage;
            } else {
                throw new Exception("Emissor inválido!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
