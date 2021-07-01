package megapolitan.recruitment.webrecruitment.service;

import java.util.List;
import megapolitan.recruitment.webrecruitment.model.LocationModel;

public interface LocationService {
    List<LocationModel> getListLocation();

    LocationModel getLocationByIdLocation(Long idLocation);
}
