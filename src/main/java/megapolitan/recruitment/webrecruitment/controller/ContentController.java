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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class ContentController {

    @Qualifier("departmentServiceImpl")
    @Autowired
    DepartmentService departmentService;

    @Qualifier("locationServiceImpl")
    @Autowired
    LocationService locationService;

    @Qualifier("jobServiceImpl")
    @Autowired
    JobService jobService;

    @Qualifier("adminServiceImpl")
    @Autowired
    AdminService adminService;


    @GetMapping("/content")
    private String viewAllJobs(Model model){
        List<JobModel> listJob = jobService.getListJob();
        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        if (listJob.size() == 0){
            model.addAttribute("pesan", "Belum terdapat pekerjaan yang sedang dibuka");
        }

        model.addAttribute("listJob", listJob);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        model.addAttribute("reverseSortDir", "asc");
        return "content/jobs-view-all";
    }

    @GetMapping(value = "/content/sorted")
    public String daftarProdukSort(
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model) {

        Page<JobModel> page =jobService.findPaginated(sortField, sortDir);
        List<JobModel> job = page.getContent();

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("listJob", job);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        return "content/jobs-view-all";
    }

    @GetMapping(value = "/content/add-job")
    public String addJobPage(Model model){

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        List<String> listDesc = new ArrayList<>();
        List<String> listReq = new ArrayList<>();

        model.addAttribute("job", new JobModel());
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);

        return "content/jobs-add-page";
    }

    @PostMapping(value = "/content/add-job")
    public String addJobPageConfirm(
            @ModelAttribute JobModel job,
            Model model){

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

//        String[] tempDesc = job.getDescJob().split("---");
//        String[] tempReq = job.getReqJob().split("---");

        Date date = new Date();
        job.setDatePosted(date);
        job.setKodeJob("JOB");

        if(job.getDatePosted().compareTo(job.getDateClosed()) < 0){
            jobService.addJob(job);
            job.setKodeJob("JOB-" + job.getIdJob());
            jobService.addJob(job);

            model.addAttribute("pop", "green");
            model.addAttribute("msg", "Sucess!");
            model.addAttribute("job", new JobModel());
            model.addAttribute("listDepartment", listDepartment);
            model.addAttribute("listLocation", listLocation);
        } else{
            model.addAttribute("pop", "red");
            model.addAttribute("msg", "Failed!");
            model.addAttribute("job", new JobModel());
            model.addAttribute("listDepartment", listDepartment);
            model.addAttribute("listLocation", listLocation);
        }

        return "content/jobs-add-page";
    }

    @GetMapping(value = "/content/job/{kodeJob}")
    public String viewJobDetail(
            @PathVariable(value = "kodeJob") String kodeJob,
            Model model)
    {
        JobModel job = jobService.getJobByKodeJob(kodeJob);
        Date newDate = new Date();

        model.addAttribute("job", job);
        model.addAttribute("status", (newDate.compareTo(job.getDateClosed()) < 0) ? "Active" : "Inactive");
        model.addAttribute("listDesc", job.getDescJob().split("---"));
        model.addAttribute("listReq", job.getReqJob().split("---"));
        model.addAttribute("pesan", (job.getListApplicant().size() == 0) ? "No one has applied to this job yet" : "");

        return "content/jobs-view-detail";
    }

    @GetMapping(value="/content/confirmation-delete/{kodeJob}")
    public String deleteJobConfirmation(
            @PathVariable String kodeJob,
            Model model)
    {

        List<JobModel> listJob = jobService.getListJob();
        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        JobModel job = jobService.getJobByKodeJob(kodeJob);

        Date today = new Date();

        if (today.compareTo(job.getDateClosed()) < 0){
            model.addAttribute("subMsg", "Masih terdapat waktu sebelum pekerjaan ditutup");
            model.addAttribute("subMsg2", "Apakah anda yakin ingin menghapus pekerjaan ini?");
        }else{
            model.addAttribute("subMsg", "");
            model.addAttribute("subMsg2", "Apakah anda yakin ingin menghapus pekerjaan ini?");
        }

        model.addAttribute("pop", "notification");
        model.addAttribute("msg", "Konfirmasi Penghapusan");
        model.addAttribute("kodeJob", kodeJob);
        model.addAttribute("listJob", listJob);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        model.addAttribute("reverseSortDir", "asc");
        return "content/jobs-view-all";
    }

    @GetMapping(value = "content/delete/{kodeJob}")
    public String deleteJob(
        @PathVariable String kodeJob,
        Model model
    ){
        List<JobModel> listJob = jobService.getListJob();
        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        JobModel job = jobService.getJobByKodeJob(kodeJob);

        jobService.deleteJob(job);

        if (listJob.size() == 0){
            model.addAttribute("pesan", "Belum terdapat pekerjaan yang sedang dibuka");
        }

        model.addAttribute("pop", "green");
        model.addAttribute("msg", "Success!");
        model.addAttribute("kodeJob", kodeJob);
        model.addAttribute("listJob", listJob);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        model.addAttribute("reverseSortDir", "asc");
        return "content/jobs-view-all";
    }

    @GetMapping(value = "content/edit-job/{kodeJob}")
    public String editJobPage(
            @PathVariable String kodeJob,
            Model model
    ){

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        JobModel job = jobService.getJobByKodeJob(kodeJob);
        List<String> listDesc = Arrays.asList(job.getDescJob().split("---"));
        List<String> listReq = Arrays.asList(job.getReqJob().split("---"));

        model.addAttribute("job", job);
        model.addAttribute("listDesc", listDesc);
        model.addAttribute("counterDesc", listDesc.size());
        model.addAttribute("listReq", listReq);
        model.addAttribute("counterReq", listReq.size());
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        return "content/jobs-edit-page";
    }

    @PostMapping(value = "content/edit-job")
    public String editJobPageSuccess(
            @ModelAttribute JobModel job,
            Model model
    ){
        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();



        if(job.getDatePosted().compareTo(job.getDateClosed()) < 0){
            jobService.addJob(job);

            model.addAttribute("pop", "green");
            model.addAttribute("msg", "Sucess!");
        } else{
            model.addAttribute("pop", "red");
            model.addAttribute("msg", "Failed!");
        }

        model.addAttribute("job", job);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);

        return "content/jobs-edit-page";
    }

}
