package megapolitan.recruitment.webrecruitment.service;

import megapolitan.recruitment.webrecruitment.model.DepartmentModel;
import megapolitan.recruitment.webrecruitment.model.JobModel;
import megapolitan.recruitment.webrecruitment.model.LocationModel;
import org.springframework.data.domain.Page;

import java.util.List;


public interface JobService {
    List<JobModel> getListJob();

    Page<JobModel> findPaginated(String sortField, String sortDir);

    void addJob(JobModel job);

    JobModel getJobByKodeJob(String kodeJob);

    void deleteJob(JobModel job);

    List<JobModel> getListByDepartment(DepartmentModel department);

    List<JobModel> getListByLocation(LocationModel location);

    List<JobModel> getListByDepartmentAndLocation(DepartmentModel department, LocationModel location);
}
