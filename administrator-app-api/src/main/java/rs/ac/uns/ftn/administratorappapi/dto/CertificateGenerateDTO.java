package rs.ac.uns.ftn.administratorappapi.dto;

import rs.ac.uns.ftn.administratorappapi.model.CertificateType;

public class CertificateGenerateDTO {
    private IssuerDataDTO issuerDataDTO;
    private SubjectDataDTO subjectDataDTO;
    private CertificateType certificateType;

    public CertificateGenerateDTO(){

    }

    public CertificateGenerateDTO(IssuerDataDTO issuerDataDTO, SubjectDataDTO subjectDataDTO, CertificateType certificateType) {
        this.issuerDataDTO = issuerDataDTO;
        this.subjectDataDTO = subjectDataDTO;
        this.certificateType = certificateType;
    }


    public IssuerDataDTO getIssuerDataDTO() {
        return issuerDataDTO;
    }

    public void setIssuerDataDTO(IssuerDataDTO issuerDataDTO) {
        this.issuerDataDTO = issuerDataDTO;
    }

    public SubjectDataDTO getSubjectDataDTO() {
        return subjectDataDTO;
    }

    public void setSubjectDataDTO(SubjectDataDTO subjectDataDTO) {
        this.subjectDataDTO = subjectDataDTO;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    @Override
    public String toString() {
        return "CertificateGenerateDTO{" +
                "issuerDataDTO=" + issuerDataDTO +
                ", subjectDataDTO=" + subjectDataDTO +
                ", certificateType=" + certificateType +
                '}';
    }
}
