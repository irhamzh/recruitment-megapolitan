package megapolitan.recruitment.webrecruitment.service;

import megapolitan.recruitment.webrecruitment.model.ApplicantModel;

import java.util.List;

public interface ApplicantService {

    void addApplicant(ApplicantModel applicant);

    ApplicantModel getApplicantByIdApplicant(Long idApplicant);
}
