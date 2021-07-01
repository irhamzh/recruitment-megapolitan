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
@Table(name="job")
public class JobModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_job", nullable = false)
    private Long idJob;

    @NotNull
    @Size(max = 50)
    @Column(name = "kode_job", nullable = false)
    private String kodeJob;

    @NotNull
    @Size(max = 50)
    @Column(name = "position", nullable = false)
    private String position;

    @NotNull
    @Size(max = 250)
    @Column(name = "desc_job", nullable = false)
    private String descJob;

    @NotNull
    @Size(max = 250)
    @Column(name = "req_job", nullable = false)
    private String reqJob;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_posted", nullable = false)
    private Date datePosted;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_closed", nullable = false)
    private Date dateClosed;

    //type refers to full time or level entry or manager or something
    // *can be changed, not final*
    @NotNull
    @Size(max = 50)
    @Column(name = "type_job", nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_department", referencedColumnName = "id_department", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private DepartmentModel department;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_location", referencedColumnName = "id_location", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private LocationModel location;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ApplicantModel> listApplicant;

    public Long getIdJob() {
        return idJob;
    }

    public void setIdJob(Long idJob) {
        this.idJob = idJob;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescJob() {
        return descJob;
    }

    public void setDescJob(String descJob) {
        this.descJob = descJob;
    }

    public String getReqJob() {
        return reqJob;
    }

    public void setReqJob(String reqJob) {
        this.reqJob = reqJob;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DepartmentModel getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentModel department) {
        this.department = department;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public List<ApplicantModel> getListApplicant() {
        return listApplicant;
    }

    public void setListApplicant(List<ApplicantModel> listApplicant) {
        this.listApplicant = listApplicant;
    }

    public String getKodeJob() {
        return kodeJob;
    }

    public void setKodeJob(String kodeJob) {
        this.kodeJob = kodeJob;
    }
}
