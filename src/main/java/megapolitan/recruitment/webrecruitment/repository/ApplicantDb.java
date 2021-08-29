package megapolitan.recruitment.webrecruitment.repository;

import megapolitan.recruitment.webrecruitment.model.ApplicantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantDb extends JpaRepository<ApplicantModel, Long>{
    ApplicantModel findByIdApplicant(Long idApplicant);
    List<ApplicantModel> findByEmail(String email);
    List<ApplicantModel> findByNamaAwalIgnoreCaseContaining(String namaAwal);
    List<ApplicantModel> findByNamaAkhirIgnoreCaseContaining(String namaAkhir);
    List<ApplicantModel> findByNamaAwalAndNamaAkhirIgnoreCaseContaining(String namaAwal, String namaAkhir);

}
