package megapolitan.recruitment.webrecruitment.controller;

import megapolitan.recruitment.webrecruitment.model.*;

import megapolitan.recruitment.webrecruitment.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Qualifier("applicantServiceImpl")
    @Autowired
    ApplicantService applicantService;

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

    @GetMapping ("/jobs/apply/{kodeJob}")
    public String applyJob(
            @PathVariable(value = "kodeJob") String kodeJob,
            Model model
    ){
        JobModel job = jobService.getJobByKodeJob(kodeJob);

//        model.addAttribute("applicant", new ApplicantModel());
        model.addAttribute("job", job);
        return "job/jobs-apply";
    }

    @PostMapping ("/jobs/apply/{kodeJob}")
    public String applyJobConfirmed(
            @PathVariable(value = "kodeJob") String kodeJob,
            @RequestParam("content") MultipartFile multipartFile,
            @RequestParam("namaAwal") String namaAwal,
            @RequestParam("namaAkhir") String namaAkhir,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("portUrl") Optional<String> portUrl,
            @RequestParam("linkedInUrl") Optional<String> linkedInUrl,
            @RequestParam("shortDesc") String shortDesc,
            Model model
    ) throws IOException {

        ApplicantModel applicant = new ApplicantModel();

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        JobModel job = jobService.getJobByKodeJob(kodeJob);

        Date date = new Date();
        applicant.setNamaAwal(namaAwal);
        applicant.setNamaAkhir(namaAkhir);
        applicant.setEmail(email);
        applicant.setPhoneNumber(phoneNumber);
        applicant.setShortDesc(shortDesc);

        portUrl.ifPresent(applicant::setPortUrl);

        linkedInUrl.ifPresent(applicant::setLinkedInUrl);

        applicant.setDateSent(date);
        applicant.setNamaFile(fileName);
        applicant.setContent(multipartFile.getBytes());
        applicant.setSizeFile(multipartFile.getSize());
        applicant.setJob(job);

        if (multipartFile.getSize() <= 500000){
            applicantService.addApplicant(applicant);

            model.addAttribute("pop", "green");
            model.addAttribute("msg", "Lamaran telah dikirim!");
        } else{
            model.addAttribute("pop", "red");
            model.addAttribute("msg", "Failed!");
            model.addAttribute("subMsg", "File melebihi 500 KB");

        }
        model.addAttribute("job", job);


        return "job/jobs-apply";
    }




}
