package rs.ac.uns.ftn.administratorappapi.storage;


import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.administratorappapi.exception.PkiMalfunctionException;
import rs.ac.uns.ftn.administratorappapi.model.CertificateType;
import rs.ac.uns.ftn.administratorappapi.model.IssuerData;
import rs.ac.uns.ftn.administratorappapi.util.helper.CertificatesAndKeyHolder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class CertificateStorage {

    @Value( "${pki.key-store-password}" )
    private char[] keyStorePassword;

    @Value( "${pki.trust-store-password}" )
    private char[] trustStorePassword;

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


    public void storeCertificateChain(X509Certificate[] chain, PrivateKey privateKey) {
        String serialNumber = chain[0].getSerialNumber().toString();
        Path pathToKeyStore = Paths.get("src", "main", "resources", "keystore", this.keyStoreFileName);
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
            throw new PkiMalfunctionException("Error while storing certificate chain into the local keystore.");
        }
    }

    public String[] storeCertificateDistributionFiles(String serialNumber, CertificateType type) {

        // Experiment
        Path pathToKeyStore = Paths.get("src", "main", "resources", "keystore", "KeyStore.p12");
        CertificatesAndKeyHolder ckh = this.getPrivateKeyAndChain(
                pathToKeyStore.toString(),
                serialNumber
        );

        Path storagePath = Paths.get("src", "main", "resources", "storage", type.toString());

        try {
            String certFileName = "cert_" + serialNumber + ".pem";
            String keyStoreFileName = "keyStore_" + serialNumber + ".p12";
            String trustStoreFileName = "trustStore_" + serialNumber + ".p12";

            String certFilePath = Paths.get(storagePath.toString(), certFileName).toString();
            String keyStoreFilePath = Paths.get(storagePath.toString(), keyStoreFileName).toString();
            String trustStoreFilePath = Paths.get(storagePath.toString(), trustStoreFileName).toString();

            // Store certificate chain to PEM file
            JcaPEMWriter pemWrt = new JcaPEMWriter(new FileWriter(certFilePath));
            for (int i = 0; i < ckh.getChain().length; i++) {
                pemWrt.writeObject(ckh.getChain()[i]);
                pemWrt.flush();

            }
            pemWrt.close();

            // Store private key of certificate and certificate chain to keystore
            this.createKeyStore(
                    ckh.getChain(),
                    ckh.getPrivateKey(),
                    keyStoreFilePath
            );

            // Store certificate to new  trust store
            this.createTrustStore(
                    ckh.getChain()[0],
                    trustStoreFilePath
            );

            return new String[]{certFilePath, keyStoreFilePath, trustStoreFilePath};

        } catch (Exception e) {
            e.printStackTrace();
            throw new PkiMalfunctionException("Error while storing certificate distribution files.");
        }
    }

    public CertificatesAndKeyHolder getPrivateKeyAndChain(String storePath, String certAlias) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            keyStore.load(new FileInputStream(storePath), this.keyStorePassword);
            Key key = keyStore.getKey(certAlias, this.keyStorePassword);

            if (key instanceof PrivateKey) {
                Certificate[] certificates = keyStore.getCertificateChain(certAlias);

                /* Convert to X509Certificate[]*/
                ArrayList<Certificate> certificateList = new ArrayList<>(Arrays.asList(certificates));
                X509Certificate[] x509Certificates = certificateList.toArray(new X509Certificate[certificateList.size()]);

                return new CertificatesAndKeyHolder(x509Certificates, (PrivateKey)key);
            } else {
                throw new PkiMalfunctionException("Error while loading certificate chain. Bad private key.");
            }

        } catch (KeyStoreException |
                IOException |
                NoSuchAlgorithmException |
                CertificateException |
                UnrecoverableKeyException e) {
            e.printStackTrace();
            throw new PkiMalfunctionException("Error while loading certificate chain.");
        }
    }

    public void createKeyStore(X509Certificate[] chain, PrivateKey privateKey, String storePath) {
        String serialNumber = chain[0].getSerialNumber().toString();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try {
                keyStore.load(new FileInputStream(storePath.toString()), this.keyStorePassword);
            } catch (IOException e) {
                keyStore.load(null, null);
            }

            keyStore.setKeyEntry(serialNumber, privateKey, this.keyStorePassword, chain);
            keyStore.store(new FileOutputStream(storePath.toString()), this.keyStorePassword);
        } catch (KeyStoreException |
                NoSuchAlgorithmException |
                CertificateException |
                IOException e) {
            e.printStackTrace();
            throw new PkiMalfunctionException("Error while storing certificate chain into the local keystore.");
        }
    }

    private void createTrustStore (
            X509Certificate certificate,
            String storePath)
    {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);
            keyStore.setCertificateEntry(certificate.getSerialNumber().toString(), certificate);
            keyStore.store(new FileOutputStream(storePath), this.trustStorePassword);

        } catch (KeyStoreException |
                IOException |
                NoSuchAlgorithmException |
                CertificateException e) {
            throw new PkiMalfunctionException("Error while storing certificate to trust store.");
        }
    }



}
