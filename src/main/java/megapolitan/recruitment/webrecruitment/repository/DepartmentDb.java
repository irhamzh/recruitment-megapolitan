package megapolitan.recruitment.webrecruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import megapolitan.recruitment.webrecruitment.model.DepartmentModel;

@Repository
public interface DepartmentDb extends JpaRepository<DepartmentModel, Long> {
    List<DepartmentModel> findAll();
    DepartmentModel findByIdDepartment(Long idDepartment);

}
