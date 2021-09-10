package rs.ac.uns.ftn.administratorappapi.dto;

import rs.ac.uns.ftn.administratorappapi.model.Certificate;

public class CertificateDTO {

    private long id;
    private String serialNumber;
    private String caSerialNumber;
    private Boolean ca;
    private Boolean revoked;
    private String revokedAt;
    private String revokeReason;
    private String type;
    private String issuedAt;
    private String expiringAt;

    public CertificateDTO() {}

    public CertificateDTO(Certificate c) {
        this.id = c.getId();
        this.serialNumber = c.getSerialNumber().toString();
        this.caSerialNumber = c.getCaSerialNumber().toString();
        this.ca = c.isCa();
        this.revoked = c.isRevoked();
        if (c.getRevokedAt() != null) {
            this.revokedAt = c.getRevokedAt().toString();
        } else {
            this.revokedAt = "--";
        }
        this.revokeReason = c.getRevokeReason();
        this.type = c.getType().toString();
        this.issuedAt = c.getIssuedAt().toString();
        this.expiringAt = c.getExpiringAt().toString();
    }

    public CertificateDTO(String serialNumber, String revokeReason) {
        this.serialNumber = serialNumber;
        this.revokeReason = revokeReason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCaSerialNumber() {
        return caSerialNumber;
    }

    public void setCaSerialNumber(String caSerialNumber) {
        this.caSerialNumber = caSerialNumber;
    }

    public Boolean isCa() {
        return ca;
    }

    public void setCa(Boolean ca) {
        this.ca = ca;
    }

    public Boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public String getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(String revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getExpiringAt() {
        return expiringAt;
    }

    public void setExpiringAt(String expiringAt) {
        this.expiringAt = expiringAt;
    }
}
