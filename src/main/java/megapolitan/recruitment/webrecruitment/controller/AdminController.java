package megapolitan.recruitment.webrecruitment.controller;
import java.security.Principal;
import java.util.List;

import megapolitan.recruitment.webrecruitment.model.*;
import megapolitan.recruitment.webrecruitment.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import megapolitan.recruitment.webrecruitment.service.DepartmentService;
import megapolitan.recruitment.webrecruitment.service.LocationService;
import megapolitan.recruitment.webrecruitment.model.LocationModel;
import megapolitan.recruitment.webrecruitment.model.AdminModel;
import megapolitan.recruitment.webrecruitment.service.AdminService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class AdminController {
    @Qualifier("departmentServiceImpl")
    @Autowired
    DepartmentService departmentService;

    @Qualifier("locationServiceImpl")
    @Autowired
    LocationService locationService;

    @Qualifier("adminServiceImpl")
    @Autowired
    AdminService adminService;

    @GetMapping("/")
    public String home(Model model) {
        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        Date newDate = new Date();
        
        for (int i = 0; i < listDepartment.size(); i ++){
            List<JobModel> jobCatch =  new ArrayList<>();
            for (int j = 0; j < listDepartment.get(i).getListJob().size(); j++){
                if (newDate.compareTo(listDepartment.get(i).getListJob().get(j).getDateClosed()) < 0){
                    jobCatch.add(listDepartment.get(i).getListJob().get(j));
                }
            }
            listDepartment.get(i).getListJob().clear();
            listDepartment.get(i).getListJob().addAll(jobCatch);
        }
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        return "main";
    }

    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }

    @GetMapping(value = "/admin-register")
    public String addAdmin(Model model) {
        model.addAttribute("admin", new AdminModel());
        return "admin/register-admin";
    }

    @PostMapping("/admin-register")
    public String addUserSubmit(@ModelAttribute AdminModel admin, Model model) {
        try {
            if (adminService.getAdminByEmail(admin.getEmail()) == null) {
//                admin.setAdmin(true);
                adminService.addAdmin(admin);
                return "redirect:/login";
            } else {
                model.addAttribute("msg", "Email Sudah Terdaftar di Database");
                return "admin/register-admin";
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Email Sudah Terdaftar di Database");
            model.addAttribute("admin", new AdminModel());
            return "admin/register-admin";
        }

    }

}
