package Auth.Crypt;

public class Util {

    private String HMac_Key;
    private String Vernam_Key;
    private MyAES myaes = new MyAES();

    public Util() {
        this.HMac_Key = "chave456";
        this.Vernam_Key = "chave789";
    }
  
    public Util(String HMac_Key) {
        this.HMac_Key = HMac_Key;
        this.Vernam_Key = "chave789"; // Mantém a chave de Vernam padrão
    }

    public Util(String HMac_Key, String Vernam_Key) {
        this.HMac_Key = HMac_Key;
        this.Vernam_Key = Vernam_Key;
    }

    public String EncryptMessage(String message) throws Exception {
        String encryptHMAC = MyHMAC.hMac(HMac_Key, message);
        String encryptAES = myaes.cifrar(encryptHMAC);
        String encryptbase64 = MyBase64.codificar(encryptAES);
        String encryptVernam = MyCifraVernam.cifrar(message, Vernam_Key);

        String encryptedMessageFinal = encryptbase64 + "||" + encryptVernam;
        return encryptedMessageFinal;
    }

    public String DecryptMessage(String message) throws Exception {
        String[] splitter = message.split("\\|\\|"); // Corrigido para escapar o caractere '|'
        if (splitter.length < 2) {
            throw new Exception("Mensagem recebida inválida: não contém delimitador '||'.");
        }
        String messagebase64 = splitter[0];
        String decryptVernam = splitter[1];

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
    }
}
