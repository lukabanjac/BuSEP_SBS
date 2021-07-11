package rs.ac.uns.ftn.administratorappapi.util;

import lombok.experimental.UtilityClass;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.model.*;

import java.math.BigInteger;
import java.security.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class DataGenerator {

    public IssuerData generateIssuer(PrivateKey issuerKey) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "Luka Banjac");
        builder.addRDN(BCStyle.SURNAME, "Banjac");
        builder.addRDN(BCStyle.GIVENNAME, "Luka");
        builder.addRDN(BCStyle.O, "UNS-FTN");
        builder.addRDN(BCStyle.OU, "Katedra za informatiku");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "lukabanjac@uns.ac.rs");

        // UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, "123456");

        // Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
        // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
        // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
        return new IssuerData(issuerKey, builder.build());
    }

    /*
    public SubjectData generateSubject(User user) {
        try {
            KeyPair keyPairSubject = generateKeyPair();

            // Datumi od kad do kad vazi sertifikat
            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse(String.valueOf(LocalDateTime.now()));
            Date endDate = iso8601Formater.parse(String.valueOf(LocalDateTime.now().plusYears(1)));

            // Serijski broj sertifikata
            String sn = String.valueOf(System.currentTimeMillis());

            // klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, user.getFirstName() + user.getLastName());
            builder.addRDN(BCStyle.SURNAME, user.getLastName());
            builder.addRDN(BCStyle.GIVENNAME, user.getFirstName());
            builder.addRDN(BCStyle.O, "UNS-FTN"); // TODO: Kada se zavrsi frontend, dodacemo funkcionalnosti da se moze mijenjati i O, OU i C
            builder.addRDN(BCStyle.OU, "Katedra za informatiku");
            builder.addRDN(BCStyle.C, "RS");
            builder.addRDN(BCStyle.E, user.getEmail());

            // UID (USER ID) je ID korisnika
            builder.addRDN(BCStyle.UID, String.valueOf(user.getId()));

            // Kreiraju se podaci za sertifikat, sto ukljucuje:
            // - javni kljuc koji se vezuje za sertifikat
            // - podatke o vlasniku
            // - serijski broj sertifikata
            // - od kada do kada vazi sertifikat
            return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SubjectData generateSubjectExample() {
        try {
            KeyPair keyPairSubject = generateKeyPair();

            // Datumi od kad do kad vazi sertifikat
            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse("2021-03-01");
            Date endDate = iso8601Formater.parse("2022-03-01");

            // Serijski broj sertifikata
            String sn = "1";

            // klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, "Marija Kovacevic");
            builder.addRDN(BCStyle.SURNAME, "Kovacevic");
            builder.addRDN(BCStyle.GIVENNAME, "Marija");
            builder.addRDN(BCStyle.O, "UNS-FTN");
            builder.addRDN(BCStyle.OU, "Katedra za informatiku");
            builder.addRDN(BCStyle.C, "RS");
            builder.addRDN(BCStyle.E, "marija.kovacevic@uns.ac.rs");

            // UID (USER ID) je ID korisnika
            builder.addRDN(BCStyle.UID, "654321");

            // Kreiraju se podaci za sertifikat, sto ukljucuje:
            // - javni kljuc koji se vezuje za sertifikat
            // - podatke o vlasniku
            // - serijski broj sertifikata
            // - od kada do kada vazi sertifikat
            return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
