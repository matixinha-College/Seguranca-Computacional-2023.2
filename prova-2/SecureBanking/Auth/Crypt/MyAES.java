package Auth.Crypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MyAES {
    private SecretKey chave;

    public MyAES() {
        gerarChave();
    }

    public void gerarChave() {
        // Definir uma semente fixa para o gerador de números aleatórios
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(06032024); // Semente fixa para gerar a mesma chave sempre

        try {
            KeyGenerator geradorDeChaves = KeyGenerator.getInstance("AES");
            geradorDeChaves.init(128, secureRandom); // Usar o gerador de números aleatórios com a semente fixa
            chave = geradorDeChaves.generateKey();
            System.out.println("Chave gerada: " + chave.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String cifrar(String textoAberto) {
        byte[] bytesMensagemCifrada;
        Cipher cifrador;
        String mensagemCifrada = null;
        try {
            cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cifrador.init(Cipher.ENCRYPT_MODE, chave);
            bytesMensagemCifrada = cifrador.doFinal(textoAberto.getBytes());
            mensagemCifrada = Base64.getEncoder().encodeToString(bytesMensagemCifrada);
            //System.out.println(">> Mensagem cifrada = " + mensagemCifrada);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        return mensagemCifrada;
    }

    public String decifrar(String textoCifrado) {
        byte[] bytesMensagemCifrada = Base64.getDecoder().decode(textoCifrado);
        Cipher decriptador;
        String mensagemDecifrada = null;
        try {
            decriptador = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decriptador.init(Cipher.DECRYPT_MODE, chave);
            byte[] bytesMensagemDecifrada = decriptador.doFinal(bytesMensagemCifrada);
            mensagemDecifrada = new String(bytesMensagemDecifrada);
            //System.out.println("<< Mensagem decifrada = " + mensagemDecifrada);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        return mensagemDecifrada;
    }
}
