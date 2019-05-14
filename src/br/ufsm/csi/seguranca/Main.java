package br.ufsm.csi.seguranca;

import br.ufsm.csi.seguranca.pila.model.PilaCoin;

import javax.crypto.KeyGenerator;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        // Gera par de chaves
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Salva a chave publica em disco
        FileOutputStream fileOutputStream = new FileOutputStream("chave_publica");
        fileOutputStream.write(keyPair.getPublic().getEncoded());

        // Salva chave privada em dico
        fileOutputStream = new FileOutputStream("chave_privada");
        fileOutputStream.write(keyPair.getPrivate().getEncoded());

        // Carrega chave pública
        FileInputStream fileInputStream = new FileInputStream("chave_publica");

        // Remonta a chave publica de byte array para public key.
        byte[] bPublica = Files.readAllBytes(Paths.get("chave_publica"));

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bPublica);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey chavePublicaCriador = keyFactory.generatePublic(spec);



        PilaCoin pilaCoin = new PilaCoin();
        pilaCoin.setDataCriacao(new Date());
        pilaCoin.setIdCriador("anderson-id-256");
        pilaCoin.setChaveCriador(chavePublicaCriador);

        //  OKAY

        pilaCoin = achaMagico(pilaCoin);



    }

    public static PilaCoin achaMagico(PilaCoin pilaCoin) throws IOException, NoSuchAlgorithmException {
        SecureRandom secureRandom = new SecureRandom();
        Long magico;
        while (true) {
            magico = secureRandom.nextLong();
            pilaCoin.setNumeroMagico(magico);

            BigInteger numero = new BigInteger("99999998000000000000000000000000000000000000000000000000000000000000000");
            byte[] pilaSerializado = serializaPila(pilaCoin);
            byte[] hashPilaSerializado = hashSha256(pilaSerializado);
            BigInteger numeroHash = new BigInteger(hashPilaSerializado);

            System.out.println(numero + "\n" + numeroHash);

            if (numeroHash.compareTo(numero) < 0) {
                System.out.println("Achou magico!");
                System.out.println(magico);
                System.out.println("Numero hash: " + numeroHash);
                return pilaCoin;
            }
        }

    }

    public static byte[] serializaPila(PilaCoin pilaCoin) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oops = new ObjectOutputStream(byteArrayOutputStream);
        oops.writeObject(pilaCoin);
        byte[] pilaSerializado = byteArrayOutputStream.toByteArray();
        return pilaSerializado;
    }

    public static byte[] hashSha256(byte[] pilaSerializado) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(pilaSerializado);
    }

}
