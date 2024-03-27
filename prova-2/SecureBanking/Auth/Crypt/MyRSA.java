package Auth.Crypt;

import java.math.BigInteger;
import java.util.Random;

public class MyRSA {
    private BigInteger publicKey;
    private BigInteger privateKey;
    private static BigInteger modulus;

    public MyRSA() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        // Gerar números primos grandes (p e q)
        BigInteger p = generatePrimeNumber();
        BigInteger q = generatePrimeNumber();

        // Calcular n (módulo)
        modulus = p.multiply(q);

        // Calcular totiente de Euler (φ(n))
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Escolher um número inteiro e relativamente primo a φ(n) como a chave pública
        // (e)
        publicKey = BigInteger.probablePrime(16, new Random());

        // Calcular a chave privada (d) usando inverso multiplicativo modular de e mod
        // φ(n)
        privateKey = publicKey.modInverse(phi);
    }

    // Gerar um número primo grande
    private BigInteger generatePrimeNumber() {
        return BigInteger.probablePrime(1024, new Random());
    }

    // Obter a chave pública
    public BigInteger getPublicKey() {
        return publicKey;
    }

    // Obter a chave privada
    public BigInteger getPrivateKey() {
        return privateKey;
    }

    // Obter o módulo
    public BigInteger getModulus() {
        return modulus;
    }

    // Method to encrypt a hexadecimal message using RSA with sender's private key
    public static BigInteger encrypt(String messageHex, BigInteger privateKey) {
        BigInteger plaintext = new BigInteger(messageHex, 16); // Convert hex string to BigInteger
        return plaintext.modPow(privateKey, modulus);
    }

    // Method to decrypt a message (BigInteger) using RSA with sender's public key
    public static String decrypt(BigInteger encryptedMessage, BigInteger publicKey) {
        BigInteger plaintext = encryptedMessage.modPow(publicKey, modulus);
        return plaintext.toString(16); // Convert BigInteger to hex string
    }

    public static void main(String[] args) {
        // Create instances of MyRSA for Server (A) and Client (B)
        MyRSA serverRSA = new MyRSA();
        MyRSA clientRSA = new MyRSA();
        MyAES aes = new MyAES();

        // Generate key pairs for Server and Client
        serverRSA.generateKeyPair();
        clientRSA.generateKeyPair();

        // Get public and private keys for Server and Client
        BigInteger serverPublicKey = serverRSA.getPublicKey();
        BigInteger serverPrivateKey = serverRSA.getPrivateKey();
        BigInteger clientPublicKey = clientRSA.getPublicKey();
        BigInteger clientPrivateKey = clientRSA.getPrivateKey();

        // Print keys for Server and Client
        System.out.println("Server Public Key: " + serverPublicKey);
        System.out.println("Server Private Key: " + serverPrivateKey);
        System.out.println("Client Public Key: " + clientPublicKey);
        System.out.println("Client Private Key: " + clientPrivateKey);

        String HMac_Key = "05d39add-cf30-4c9b-a922-808aa201ed38";

        // Message to be sent from Client to Server
        String messageFromClient = "Hello from Client";

        // CLIENT SIDE
        // Encrypt using AES
        String aesMessage = aes.cifrar(messageFromClient);
        // Calculate HMAC
        String hash = MyHMAC.hMac(HMac_Key, aesMessage);
        // Encrypt HMAC using Client's private key
        BigInteger encryptedHash = MyRSA.encrypt(hash, clientPrivateKey);

        // SERVER SIDE
        // Decrypt HMAC using Client's public key
        String decryptedHash = MyRSA.decrypt(encryptedHash, clientPublicKey);
        // Verify if the decrypted hash matches the original hash
        boolean result = decryptedHash.equals(hash);

        // Output results
        System.out.println("Encrypted hash with RSA " + encryptedHash);
        System.out.println("Original Hash: " + hash);
        System.out.println("Decrypted Hash: " + decryptedHash);
        System.out.println("Result: " + result);
    }

}
