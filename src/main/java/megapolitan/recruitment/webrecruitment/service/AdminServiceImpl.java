package megapolitan.recruitment.webrecruitment.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import megapolitan.recruitment.webrecruitment.model.AdminModel;
import megapolitan.recruitment.webrecruitment.repository.AdminDb;

@Service
@Transactional
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminDb adminDb;

    @Override
    public AdminModel addAdmin(AdminModel admin){
        String password = encrypt(admin.getPassword());
        admin.setPassword(password);
        return adminDb.save(admin);

    }

    @Override
    public String encrypt(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public AdminModel getAdminByEmail(String email){
        return adminDb.findByEmail(email);
    }


}
