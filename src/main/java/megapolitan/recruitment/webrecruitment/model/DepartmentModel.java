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
@Table(name="department")
public class DepartmentModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_department", nullable = false)
    private Long idDepartment;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_department", nullable = false)
    private String namaDepartment;

    @NotNull
    @Size(max = 250)
    @Column(name = "desc_department", nullable = false)
    private String descDepartment;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<JobModel> listJob;

    public Long getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(Long idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getNamaDepartment() {
        return namaDepartment;
    }

    public void setNamaDepartment(String namaDepartment) {
        this.namaDepartment = namaDepartment;
    }

    public String getDescDepartment() {
        return descDepartment;
    }

    public void setDescDepartment(String descDepartment) {
        this.descDepartment = descDepartment;
    }

    public List<JobModel> getListJob() {
        return listJob;
    }

    public void setListJob(List<JobModel> listJob) {
        this.listJob = listJob;
    }
}
