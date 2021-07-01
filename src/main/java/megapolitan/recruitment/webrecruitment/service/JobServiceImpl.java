package megapolitan.recruitment.webrecruitment.service;

import megapolitan.recruitment.webrecruitment.model.DepartmentModel;
import megapolitan.recruitment.webrecruitment.model.JobModel;

import megapolitan.recruitment.webrecruitment.model.LocationModel;
import megapolitan.recruitment.webrecruitment.repository.JobDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class JobServiceImpl implements JobService{
    @Autowired
    JobDb jobDb;

    @Override
    public List<JobModel> getListJob(){
        return jobDb.findAll();
    }

    @Override
    public Page<JobModel> findPaginated(String sortField, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(0,jobDb.findAll().size(),sort);
        return jobDb.findAll(pageable);
    }

    @Override
    public void addJob(JobModel job){
        jobDb.save(job);
    }

    @Override
    public JobModel getJobByKodeJob(String kodeJob){
        return jobDb.findByKodeJob(kodeJob);
    }

    @Override
    public void deleteJob(JobModel job){
        jobDb.delete(job);
    }

    @Override
    public List<JobModel> getListByDepartment(DepartmentModel department){
        return jobDb.findByDepartment(department);
    }

    @Override
    public List<JobModel> getListByLocation(LocationModel location){
        return jobDb.findByLocation(location);
    }

    @Override
    public List<JobModel> getListByDepartmentAndLocation(DepartmentModel department, LocationModel location){
        return jobDb.findByDepartmentAndLocation(department,location);
    }
}
