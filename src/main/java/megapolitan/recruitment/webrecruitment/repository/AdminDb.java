package megapolitan.recruitment.webrecruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import megapolitan.recruitment.webrecruitment.model.AdminModel;

@Repository
public interface AdminDb extends JpaRepository<AdminModel, Long>{
    AdminModel findByNamaPanjang(String namaPanjang);
    AdminModel findByEmail(String email);
}
