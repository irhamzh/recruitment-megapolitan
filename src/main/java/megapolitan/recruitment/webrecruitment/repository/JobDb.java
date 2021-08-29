package megapolitan.recruitment.webrecruitment.repository;

import megapolitan.recruitment.webrecruitment.model.DepartmentModel;
import megapolitan.recruitment.webrecruitment.model.LocationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import megapolitan.recruitment.webrecruitment.model.JobModel;

@Repository
public interface JobDb extends JpaRepository<JobModel,Long>{
    List<JobModel> findAll();
    JobModel findByKodeJob(String kodeJob);
    List<JobModel> findByDepartment(DepartmentModel department);
    List<JobModel> findByLocation(LocationModel location);
    List<JobModel> findByDepartmentAndLocation(DepartmentModel department, LocationModel location);
    List<JobModel> findByPositionIgnoreCaseContaining(String position);
}
