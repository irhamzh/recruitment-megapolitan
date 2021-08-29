package megapolitan.recruitment.webrecruitment.service;

import megapolitan.recruitment.webrecruitment.model.ApplicantModel;
import megapolitan.recruitment.webrecruitment.repository.ApplicantDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public List<ApplicantModel> getListApplicants(){
        return applicantDb.findAll();
    }

    @Override
    public Page<ApplicantModel> findPaginated(int pageNo, int pageSize, String sortField, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize,sort);
        return applicantDb.findAll(pageable);
    }
    
    @Override
    public List<ApplicantModel> getListApplicantByNama(String nama){
        Set<ApplicantModel> tempList =  new HashSet<>();
        List<ApplicantModel> listApplicant = new ArrayList<>();

        List<ApplicantModel> listApplicantFirst = applicantDb.findByNamaAwalIgnoreCaseContaining(nama);
        List<ApplicantModel> listApplicantLast = applicantDb.findByNamaAkhirIgnoreCaseContaining(nama);
        if (nama.split(" ").length > 1){
            List<ApplicantModel> listApplicantBoth = applicantDb.findByNamaAwalAndNamaAkhirIgnoreCaseContaining(nama.split(" ",2)[0], nama.split(" ",2)[1]);
            tempList.addAll(listApplicantBoth);

        }
        tempList.addAll(listApplicantFirst);
        tempList.addAll(listApplicantLast);

        listApplicant.addAll(tempList);
        return listApplicant;
    }

    @Override
    public List<ApplicantModel> getListApplicantByEmail(String email){
        return  applicantDb.findByEmail(email);
    }

}
