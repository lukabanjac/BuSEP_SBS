package rs.ac.uns.ftn.administratorappapi.storage;


import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.administratorappapi.model.IssuerData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class CertificateStorage {

    @Value( "${pki.key-store-password}" )
    private char[] keyStorePassword;

    /*@Value( "${pki.trust-store-password}" )
    private char[] trustStorePassword;*/

    @Value("${pki.keystore-filename}")
    private String keyStoreFileName;


    public IssuerData getIssuerDataBySerialNumber(String serialNumber) {

        Path pathToKeyStore = Paths.get("src", "main", "resources", "keystore", this.keyStoreFileName);
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(pathToKeyStore.toString()), this.keyStorePassword);

            Key key = keyStore.getKey(serialNumber, this.keyStorePassword);
            if (key instanceof PrivateKey) {
                X509Certificate cert = (X509Certificate) keyStore.getCertificate(serialNumber);
                return new IssuerData(
                        (PrivateKey) key,
                        new JcaX509CertificateHolder(cert).getSubject(),
                        cert.getPublicKey(),
                        cert.getSerialNumber());
            }
        } catch (KeyStoreException |
                IOException |
                NoSuchAlgorithmException |
                CertificateException |
                UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void storeCertificateChan(X509Certificate[] chain, PrivateKey privateKey) {
        String serialNumber = chain[0].getSerialNumber().toString();
        Path pathToKeyStore = Paths.get("src", "main", "resources", "keystore", this.keyStoreFileName);
        System.out.println(pathToKeyStore.toString());
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try {
                keyStore.load(new FileInputStream(pathToKeyStore.toString()), this.keyStorePassword);
            } catch (IOException e) {
                keyStore.load(null, null);
            }

            keyStore.setKeyEntry(serialNumber, privateKey, this.keyStorePassword, chain);
            keyStore.store(new FileOutputStream(pathToKeyStore.toString()), this.keyStorePassword);
        } catch (KeyStoreException |
                NoSuchAlgorithmException |
                CertificateException |
                IOException e) {
            e.printStackTrace();
        }

    }






}
