package megapolitan.recruitment.webrecruitment.service;

import megapolitan.recruitment.webrecruitment.model.DepartmentModel;
import megapolitan.recruitment.webrecruitment.repository.DepartmentDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService{
    @Autowired
    DepartmentDb departmentDb;

    @Override
    public List<DepartmentModel> getListDepartment(){
        return departmentDb.findAll();
    }

    @Override
    public DepartmentModel getDepartmentByIdDepartment(Long idDepartment){
        return departmentDb.findByIdDepartment(idDepartment);
    }

}
