package megapolitan.recruitment.webrecruitment.service;
import java.util.List;

import megapolitan.recruitment.webrecruitment.model.LocationModel;
import megapolitan.recruitment.webrecruitment.repository.LocationDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LocationServiceImpl implements LocationService{
    @Autowired
    LocationDb locationDb;

    @Override
    public List<LocationModel> getListLocation(){
        return locationDb.findAll();
    }

    @Override
    public LocationModel getLocationByIdLocation(Long idLocation){
        return locationDb.findByIdLocation(idLocation);
    }
}

