package megapolitan.recruitment.webrecruitment.controller;

import megapolitan.recruitment.webrecruitment.model.*;

import megapolitan.recruitment.webrecruitment.service.AdminService;
import megapolitan.recruitment.webrecruitment.service.DepartmentService;
import megapolitan.recruitment.webrecruitment.service.JobService;
import megapolitan.recruitment.webrecruitment.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
public class JobController {

    @Qualifier("departmentServiceImpl")
    @Autowired
    DepartmentService departmentService;

    @Qualifier("locationServiceImpl")
    @Autowired
    LocationService locationService;

    @Qualifier("jobServiceImpl")
    @Autowired
    JobService jobService;

    @GetMapping("/jobs/")
    public String viewAllJobs(
            @RequestParam("departmentId") Optional<Long> departmentId,
            @RequestParam("lokasiId") Optional<Long> lokasiId,
            Model model
    ){

        List<JobModel> listJob = new ArrayList<>();
        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();
        List<JobModel> listJobLocation = new ArrayList<>();
        List<JobModel> listJobDept = new ArrayList<>();

        if (departmentId.isPresent() && lokasiId.isEmpty()){
            DepartmentModel department = departmentService.getDepartmentByIdDepartment(departmentId.get());
            listJob = jobService.getListByDepartment(department);
            model.addAttribute("departmentSelect", department);
//            model.addAttribute("locationSelect", "");


        }else if (lokasiId.isPresent() && departmentId.isEmpty()){
            LocationModel location = locationService.getLocationByIdLocation(lokasiId.get());
            listJob = jobService.getListByLocation(location);
            model.addAttribute("locationSelect", location);
//            model.addAttribute("departmentSelect", "");


        }else if(departmentId.isPresent() && lokasiId.isPresent()){
            DepartmentModel department = departmentService.getDepartmentByIdDepartment(departmentId.get());
            LocationModel location = locationService.getLocationByIdLocation(lokasiId.get());
            listJob = jobService.getListByDepartmentAndLocation(department, location);
            model.addAttribute("departmentSelect", department);
            model.addAttribute("locationSelect", location);

        }else{
            listJob = jobService.getListJob();
        }

        if (listJob.size() == 0){
            model.addAttribute("pesan", "Belum terdapat pekerjaan sesuai filter tersebut");
        }

        model.addAttribute("job", listJob);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        return "job/jobs-viewall";
    }

    @GetMapping("/jobs/{kodeJob}")
    public String viewJobDetailed(
            @PathVariable(value = "kodeJob") String kodeJob,
            Model model
    ){

        JobModel job = jobService.getJobByKodeJob(kodeJob);

        model.addAttribute("job", job);
        model.addAttribute("listDesc", job.getDescJob().split("---"));
        model.addAttribute("listReq", job.getReqJob().split("---"));
        return "job/jobs-detail";
    }




}
