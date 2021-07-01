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
@Table(name="location")
public class LocationModel implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location", nullable = false)
    private Long idLocation;

    @NotNull
    @Size(max = 50)
    @Column(name = "nama_location", nullable = false)
    private String namaLocation;

    @NotNull
    @Size(max = 250)
    @Column(name = "desc_location", nullable = false)
    private String descLocation;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<JobModel> listJob;

    public Long getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(Long idLocation) {
        this.idLocation = idLocation;
    }

    public String getNamaLocation() {
        return namaLocation;
    }

    public void setNamaLocation(String namaLocation) {
        this.namaLocation = namaLocation;
    }

    public String getDescLocation() {
        return descLocation;
    }

    public void setDescLocation(String descLocation) {
        this.descLocation = descLocation;
    }
}
