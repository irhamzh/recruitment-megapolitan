package megapolitan.recruitment.webrecruitment.service;

import megapolitan.recruitment.webrecruitment.model.AdminModel;

public interface AdminService {
    AdminModel addAdmin(AdminModel admin);
    String encrypt(String password);
    AdminModel getAdminByEmail(String email);
}
