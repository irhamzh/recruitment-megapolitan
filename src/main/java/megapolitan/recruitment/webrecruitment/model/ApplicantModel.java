package megapolitan.recruitment.webrecruitment.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="applicant")
public class ApplicantModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_applicant", nullable = false)
    private Long idApplicant;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_name", nullable = false)
    private String namaAwal;

    @NotNull
    @Size(max = 50)
    @Column(name = "last_name", nullable = false)
    private String namaAkhir;

//    @NotNull
//    @Size(max = 50)
//    @Column(name = "email", nullable = false, unique = true)
//    private String email;

    @NotNull
    @Size(max = 50)
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Size(max = 50)
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Size(max = 1500)
    @Column(name = "short_description", nullable = false)
    private String shortDesc;

    @NotNull
    @Size(max = 50)
    @Column(name = "linkedIn_url", nullable = true)
    private String linkedInUrl;

    @NotNull
    @Size(max = 250)
    @Column(name = "portofolio_url", nullable = true)
    private String portUrl;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_sent", nullable = false)
    private Date dateSent;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_job", referencedColumnName = "id_job", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private JobModel job;

    //DOCUMENT FILE

    @NotNull
    @Size(max = 50)
    @Column(name = "file_name", nullable = false)
    private String namaFile;

    @NotNull
    @Column(name = "file_size", nullable = false)
    private Long sizeFile;

    @NotNull
    @Column(name = "file_content", nullable = false)
    private byte[] content;

    public Long getIdApplicant() {
        return idApplicant;
    }

    public void setIdApplicant(Long idApplicant) {
        this.idApplicant = idApplicant;
    }

    public String getNamaAwal() {
        return namaAwal;
    }

    public void setNamaAwal(String namaAwal) {
        this.namaAwal = namaAwal;
    }

    public String getNamaAkhir() {
        return namaAkhir;
    }

    public void setNamaAkhir(String namaAkhir) {
        this.namaAkhir = namaAkhir;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public String getPortUrl() {
        return portUrl;
    }

    public void setPortUrl(String portUrl) {
        this.portUrl = portUrl;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public JobModel getJob() {
        return job;
    }

    public void setJob(JobModel job) {
        this.job = job;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }

    public Long getSizeFile() {
        return sizeFile;
    }

    public void setSizeFile(Long sizeFile) {
        this.sizeFile = sizeFile;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
