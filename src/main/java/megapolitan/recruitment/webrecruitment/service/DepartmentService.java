package megapolitan.recruitment.webrecruitment.service;

import java.util.List;

import megapolitan.recruitment.webrecruitment.model.DepartmentModel;

public interface DepartmentService {
    List<DepartmentModel> getListDepartment();

    DepartmentModel getDepartmentByIdDepartment(Long idDepartment);
}
