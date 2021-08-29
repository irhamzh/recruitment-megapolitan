package megapolitan.recruitment.webrecruitment.service;

import megapolitan.recruitment.webrecruitment.model.ApplicantModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ApplicantService {

    void addApplicant(ApplicantModel applicant);

    ApplicantModel getApplicantByIdApplicant(Long idApplicant);

    List<ApplicantModel> getListApplicants();

    Page<ApplicantModel> findPaginated(int pageNo, int pageSize, String sortField, String sortDir);
    
    List<ApplicantModel> getListApplicantByNama(String nama);

    List<ApplicantModel> getListApplicantByEmail(String email);
}
