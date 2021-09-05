package rs.ac.uns.ftn.administratorappapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "certificate")
public class Certificate {


    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="serial_number", nullable = false, unique = true)
    private BigInteger serialNumber;


    @Column(name="ca_serial_number", nullable = false)
    private BigInteger caSerialNumber;

    @Column(name="is_ca", nullable = false)
    private Boolean ca;

    @Column(name="type", nullable = false)
    private CertificateType type;

    /*@Column(name="issuer", nullable = false)
    private String issuer;

    @Column(name="subject", nullable = false)
    private String subject;*/

    @Column(name="cert_file_path")
    private String certFilePath;

    @Column(name="key_store_file_path")
    private String keyStoreFilePath;

    @Column(name="trust_store_file_path")
    private String trustStoreFilePath;

    @Column(name="revoked", nullable = false)
    private Boolean revoked;

    @Column(name="revoked_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date revokedAt;

    @Column(name="revoke_reason")
    private String revokeReason;

    @Column(name="issued_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date issuedAt;

    @Column(name="expiring_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date expiringAt;


    @OneToOne
    @JsonManagedReference
    private User user;

    public Certificate() {
    }

    public Certificate(BigInteger serialNumber,
                       BigInteger caSerialNumber,
                       CertificateType type,
                       Boolean CA,
                       String certFilePath,
                       String keyStoreFilePath,
                       String trustStoreFilePath,
                       Boolean revoked,
                       Date revokedAt,
                       String revokeReason,
                       Date issuedAt,
                       Date expiringAt
    )
    {
        this.serialNumber = serialNumber;
        this.caSerialNumber = caSerialNumber;
        this.type = type;
        this.ca = CA;
        this.certFilePath = certFilePath;
        this.keyStoreFilePath = keyStoreFilePath;
        this.trustStoreFilePath = trustStoreFilePath;
        this.revoked = revoked;
        this.revokedAt = revokedAt;
        this.revokeReason = revokeReason;
        this.issuedAt = issuedAt;
        this.expiringAt = expiringAt;
    }


    public Certificate(Long id, BigInteger serialNumber, BigInteger caSerialNumber, Boolean ca, CertificateType type, String certFilePath, String keyStoreFilePath, String trustStoreFilePath, Boolean revoked, Date revokedAt, String revokeReason, Date issuedAt, Date expiringAt) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.caSerialNumber = caSerialNumber;
        this.ca = ca;
        this.type = type;
        this.certFilePath = certFilePath;
        this.keyStoreFilePath = keyStoreFilePath;
        this.trustStoreFilePath = trustStoreFilePath;
        this.revoked = revoked;
        this.revokedAt = revokedAt;
        this.revokeReason = revokeReason;
        this.issuedAt = issuedAt;
        this.expiringAt = expiringAt;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setCaSerialNumber(BigInteger caSerialNumber) {
        this.caSerialNumber = caSerialNumber;
    }

    public BigInteger getCaSerialNumber() {
        return caSerialNumber;
    }

    public Boolean isCa() {
        return ca;
    }

    public void setCa(Boolean ca) {
        this.ca = ca;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public CertificateType getType() {
        return type;
    }

    public void setType(CertificateType type) {
        this.type = type;
    }

    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    public String getKeyStoreFilePath() {
        return keyStoreFilePath;
    }

    public void setKeyStoreFilePath(String keyStoreFilePath) {
        this.keyStoreFilePath = keyStoreFilePath;
    }

    public String getTrustStoreFilePath() {
        return trustStoreFilePath;
    }

    public void setTrustStoreFilePath(String trustStoreFilePath) {
        this.trustStoreFilePath = trustStoreFilePath;
    }

    public Boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public Date getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Date revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiringAt() {
        return expiringAt;
    }

    public void setExpiringAt(Date expiringAt) {
        this.expiringAt = expiringAt;
    }

    public Boolean getCa() {
        return ca;
    }

    public Boolean getRevoked() {
        return revoked;
    }

}
