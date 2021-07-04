package megapolitan.recruitment.webrecruitment.service;

import megapolitan.recruitment.webrecruitment.model.ApplicantModel;
import megapolitan.recruitment.webrecruitment.repository.ApplicantDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {
    @Autowired
    ApplicantDb applicantDb;

    @Override
    public void addApplicant(ApplicantModel applicant){
        applicantDb.save(applicant);
    }

    @Override
    public ApplicantModel getApplicantByIdApplicant(Long idApplicant){
        return applicantDb.findByIdApplicant(idApplicant);
    }
}
