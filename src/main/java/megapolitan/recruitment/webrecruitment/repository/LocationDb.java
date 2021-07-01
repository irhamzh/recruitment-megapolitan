package megapolitan.recruitment.webrecruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import megapolitan.recruitment.webrecruitment.model.LocationModel;

@Repository
public interface LocationDb extends JpaRepository<LocationModel, Long> {
    List<LocationModel> findAll();
    LocationModel findByIdLocation(Long idLocation);
}
