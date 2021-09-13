package rs.ac.uns.ftn.administratorappapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "certificate")
public class Certificate {


    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="serial_number", nullable = false)
    private String serialNumber;


    @Column(name="ca_serial_number", nullable = false)
    private String caSerialNumber;

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
    private LocalDateTime issuedAt;

    @Column(name="expiring_at")
    private LocalDateTime expiringAt;

//    @Column(name="issued_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    private java.util.Date issuedAt;
//
//    @Column(name="expiring_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    private java.util.Date expiringAt;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "certificate")
    @JsonIgnore
    private Device device;

//    @OneToOne(targetEntity = Device.class, fetch = FetchType.EAGER)
//    @JoinColumn(name = "device_id")
//    private Device device;

//    @OneToOne(mappedBy = "certificate")
//    private User user;

    public Certificate() {
    }

    public Certificate(String serialNumber,
                       String caSerialNumber,
                       CertificateType type,
                       Boolean CA,
                       String certFilePath,
                       String keyStoreFilePath,
                       String trustStoreFilePath,
                       Boolean revoked,
                       Date revokedAt,
                       String revokeReason,
                       LocalDateTime issuedAt,
                       LocalDateTime expiringAt
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


    public Certificate(Long id, String serialNumber, String caSerialNumber, Boolean ca, CertificateType type, String certFilePath, String keyStoreFilePath, String trustStoreFilePath, Boolean revoked, Date revokedAt, String revokeReason, LocalDateTime issuedAt, LocalDateTime expiringAt) {
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setCaSerialNumber(String caSerialNumber) {
        this.caSerialNumber = caSerialNumber;
    }

    public String getCaSerialNumber() {
        return caSerialNumber;
    }

    public Boolean isCa() {
        return ca;
    }

    public void setCa(Boolean ca) {
        this.ca = ca;
    }

    public void setSerialNumber(String serialNumber) {
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

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getExpiringAt() {
        return expiringAt;
    }

    public void setExpiringAt(LocalDateTime expiringAt) {
        this.expiringAt = expiringAt;
    }


    //    public Date getIssuedAt() {
//        return issuedAt;
//    }
//
//    public void setIssuedAt(Date issuedAt) {
//        this.issuedAt = issuedAt;
//    }
//
//    public Date getExpiringAt() {
//        return expiringAt;
//    }
//
//    public void setExpiringAt(Date expiringAt) {
//        this.expiringAt = expiringAt;
//    }

    public Boolean getCa() {
        return ca;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
