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
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

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

    @Qualifier("applicantServiceImpl")
    @Autowired
    ApplicantService applicantService;


    @GetMapping("/content")
    private String viewAllJobs(Model model){

        Page<JobModel> page =jobService.findPaginated(1, 10, "datePosted", "asc");
        List<JobModel> listJob = page.getContent();

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        if (listJob.size() == 0){
            model.addAttribute("pesan", "There are no open job applications yet");
        }

        model.addAttribute("listJob", listJob);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        model.addAttribute("reverseSortDir", "asc");
        model.addAttribute("sortField", "none");
        model.addAttribute("sortDir", "none");

        model.addAttribute("pageNo", 1);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("totalItem", page.getTotalElements());
        return "content/jobs-view-all";
    }

    @GetMapping(value = "/content/page/{pageNo}")
    private String viewJobSorted(
            @PathVariable("pageNo") int pageNo,
            @RequestParam("sortField") Optional<String> sortField,
            @RequestParam("sortDir") Optional<String> sortDir,
            Model model) {

        int pageSize = 10;

        Page<JobModel> page =jobService.findPaginated(1, 10, "datePosted", "asc");

        if (sortField.isEmpty()){
            page =jobService.findPaginated(pageNo, pageSize, "datePosted", "asc");
            model.addAttribute("sortField", "none");
            model.addAttribute("sortDir", "none");
            model.addAttribute("reverseSortDir", "asc");


        }else{
            page =jobService.findPaginated(pageNo, pageSize, sortField.get(), sortDir.get());
            model.addAttribute("sortField", sortField.get());
            model.addAttribute("sortDir", sortDir.get());
            model.addAttribute("reverseSortDir", sortDir.get().equals("asc") ? "desc" : "asc");

        }
        List<JobModel> job = page.getContent();

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("totalItem", page.getTotalElements());
        model.addAttribute("listJob", job);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        return "content/jobs-view-all";
    }

    @RequestMapping(value = "/contents", method = RequestMethod.POST)
    private String viewAllJobsSearchFilter(
            Model model,
            @RequestParam("search") Optional<String> search,
            @RequestParam("departmentId") Optional<Long> departmentId,
            @RequestParam("lokasiId") Optional<Long> lokasiId
    ){

        List<JobModel> listAllJob = jobService.getListJob();

        Page<JobModel> page =jobService.findPaginated(1, listAllJob.size(), "datePosted", "asc");
        List<JobModel> baseListJob = page.getContent();



        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        List<JobModel> listJob = new ArrayList<>();
        List<JobModel> listJobFirst = new ArrayList<>();
        List<JobModel> listJobBefore = new ArrayList<>();
        List<JobModel> listJobLocation = new ArrayList<>();
        List<JobModel> listJobDept = new ArrayList<>();
        List<JobModel> listJobSearch = new ArrayList<>();
        List<JobModel> listJobBoth = new ArrayList<>();
        Set<JobModel> filtered =  new HashSet<>();

        if (!search.get().equals("")){
            listJobSearch = jobService.getListBySearch(search.get());
            model.addAttribute("searchkey", search.get());
            System.out.println("search is present");
            System.out.println(search.get());


            if (departmentId.isPresent() && lokasiId.isEmpty()){
                DepartmentModel department = departmentService.getDepartmentByIdDepartment(departmentId.get());
                listJobDept = jobService.getListByDepartment(department);
                model.addAttribute("departmentSelect", department);

                listJobBefore.addAll(listJobSearch);
                listJobBefore.addAll(listJobDept);

                for (int i = 0; i < listJobBefore.size(); i++){
                    if (listJobSearch.contains(listJobBefore.get(i)) && listJobDept.contains(listJobBefore.get(i))) {
                        filtered.add(listJobBefore.get(i));
                    }
                }

                listJobFirst.addAll(filtered);

                System.out.println("dept is present");
                model.addAttribute("departmentId", departmentId.get());
                model.addAttribute("lokasiId", "");




            }else if (lokasiId.isPresent() && departmentId.isEmpty()){
                LocationModel location = locationService.getLocationByIdLocation(lokasiId.get());
                listJobLocation = jobService.getListByLocation(location);
                model.addAttribute("locationSelect", location);

                listJobBefore.addAll(listJobSearch);
                listJobBefore.addAll(listJobLocation);

                for (int i = 0; i < listJobBefore.size(); i++){
                    if (listJobSearch.contains(listJobBefore.get(i)) && listJobLocation.contains(listJobBefore.get(i))) {
                        filtered.add(listJobBefore.get(i));
                    }
                }

//                listJob.addAll(filtered);
                listJobFirst.addAll(filtered);

                System.out.println("lokasi is present");
                model.addAttribute("lokasiId", lokasiId.get());
                model.addAttribute("departmentId", "");



            }else if(departmentId.isPresent() && lokasiId.isPresent()){
                DepartmentModel department = departmentService.getDepartmentByIdDepartment(departmentId.get());
                LocationModel location = locationService.getLocationByIdLocation(lokasiId.get());
                listJobBoth = jobService.getListByDepartmentAndLocation(department, location);
                model.addAttribute("departmentSelect", department);
                model.addAttribute("locationSelect", location);

                listJobBefore.addAll(listJobSearch);
                listJobBefore.addAll(listJobBoth);

                for (int i = 0; i < listJobBefore.size(); i++){
                    if (listJobSearch.contains(listJobBefore.get(i)) && listJobBoth.contains(listJobBefore.get(i))) {
                        filtered.add(listJobBefore.get(i));
                    }
                }

//                listJob.addAll(filtered);
                listJobFirst.addAll(filtered);

                System.out.println("both are present");
                model.addAttribute("departmentId", departmentId.get());
                model.addAttribute("lokasiId", lokasiId.get());



            }else{
//                listJob = listJobSearch;
                listJobFirst.addAll(listJobSearch);

                model.addAttribute("departmentId", "");
                model.addAttribute("lokasiId", "");


                System.out.println("only search is present");
            }

        } else if(search.get().equals("")){
            System.out.println("search is not present");

            if (departmentId.isPresent() && lokasiId.isEmpty()){
                DepartmentModel department = departmentService.getDepartmentByIdDepartment(departmentId.get());
                listJobFirst = jobService.getListByDepartment(department);
                model.addAttribute("departmentSelect", department);
                System.out.println("dept is present");
                model.addAttribute("departmentId", departmentId.get());
                model.addAttribute("lokasiId", "");



            }else if (lokasiId.isPresent() && departmentId.isEmpty()){
                LocationModel location = locationService.getLocationByIdLocation(lokasiId.get());
                listJobFirst = jobService.getListByLocation(location);
                model.addAttribute("locationSelect", location);
                System.out.println("lokasi is present");
                model.addAttribute("lokasiId", lokasiId.get());
                model.addAttribute("departmentId", "");




            }else if(departmentId.isPresent() && lokasiId.isPresent()){
                DepartmentModel department = departmentService.getDepartmentByIdDepartment(departmentId.get());
                LocationModel location = locationService.getLocationByIdLocation(lokasiId.get());
                listJobFirst = jobService.getListByDepartmentAndLocation(department, location);
                model.addAttribute("departmentSelect", department);
                model.addAttribute("locationSelect", location);
                System.out.println("both is present");
                model.addAttribute("departmentId", departmentId.get());
                model.addAttribute("lokasiId", lokasiId.get());

            }else{
                listJobFirst = jobService.getListJob();

                System.out.println("none is present");

                model.addAttribute("departmentId", "");
                model.addAttribute("lokasiId", "");
            }
        }

        if (listJobFirst.size() <= baseListJob.size()){
            for (int i = 0; i < baseListJob.size(); i++){
                if (listJobFirst.contains(baseListJob.get(i))){
                    listJob.add(baseListJob.get(i));
                }
            }
        }
        model.addAttribute("totalPage", listJob.size()%10 == 0 ?
                listJob.size()/10 - 1 : listJob.size()/10 + 1) ;

        if(listJob.size() > 10){
            int size = listJob.size();
            for (int i = 10; i < size; i++ ){
                listJob.remove(listJob.size() - 1);
            }
        }else if (listJob.size() == 0){
            model.addAttribute("pesan", "There are no open job applications with that filter yet");
        }

        model.addAttribute("listJob", listJob);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        model.addAttribute("reverseSortDir", "asc");
        model.addAttribute("sortField", "datePosted");
        model.addAttribute("sortDir", "asc");

        model.addAttribute("pageNo", 1);
//        model.addAttribute("totalPage", listJob.size()/10 + 1);
        model.addAttribute("totalItem", listJobFirst.size());

        model.addAttribute("search", search.get());

        return "content/jobs-view-all-searchfilter";
    }

    @GetMapping(value = "/contents/page/{pageNo}")
    public String viewAllJobsSearchFilterSorted(
            @PathVariable("pageNo") int pageNo,
            @RequestParam("search") String search,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("lokasiId") String lokasiId,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model)
    {
        int pageSize = 10;

        List<JobModel> listAllJob = jobService.getListJob();

        Page<JobModel> page =jobService.findPaginated(1, listAllJob.size(), sortField, sortDir);
        List<JobModel> baseListJob = page.getContent();

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        List<JobModel> listJob = new ArrayList<>();
        List<JobModel> listJobFirst = new ArrayList<>();
        List<JobModel> listJobBefore = new ArrayList<>();
        List<JobModel> listJobLocation = new ArrayList<>();
        List<JobModel> listJobDept = new ArrayList<>();
        List<JobModel> listJobSearch = new ArrayList<>();
        List<JobModel> listJobBoth = new ArrayList<>();
        Set<JobModel> filtered =  new HashSet<>();
        List<JobModel> listJobFinal = new ArrayList<>();

        if (!search.equals("")){
            listJobSearch = jobService.getListBySearch(search);
            model.addAttribute("searchkey", search);
            System.out.println("search is present");
//            System.out.println(search.get());

            if (!departmentId.equals("") && lokasiId.equals("")){
                DepartmentModel department = departmentService.getDepartmentByIdDepartment(Long.parseLong(departmentId));
                listJobDept = jobService.getListByDepartment(department);
                model.addAttribute("departmentSelect", department);

                listJobBefore.addAll(listJobSearch);
                listJobBefore.addAll(listJobDept);

                for (int i = 0; i < listJobBefore.size(); i++){
                    if (listJobSearch.contains(listJobBefore.get(i)) && listJobDept.contains(listJobBefore.get(i))) {
                        filtered.add(listJobBefore.get(i));
                    }
                }

                listJobFirst.addAll(filtered);

//                System.out.println("dept is present");
                model.addAttribute("departmentId", departmentId);
                model.addAttribute("lokasiId", "");

            }else if (!lokasiId.equals("") && departmentId.equals("")){
                LocationModel location = locationService.getLocationByIdLocation(Long.parseLong(lokasiId));
                listJobLocation = jobService.getListByLocation(location);
                model.addAttribute("locationSelect", location);

                listJobBefore.addAll(listJobSearch);
                listJobBefore.addAll(listJobLocation);

                for (int i = 0; i < listJobBefore.size(); i++){
                    if (listJobSearch.contains(listJobBefore.get(i)) && listJobLocation.contains(listJobBefore.get(i))) {
                        filtered.add(listJobBefore.get(i));
                    }
                }

                listJobFirst.addAll(filtered);
//                System.out.println("lokasi is present");
                model.addAttribute("lokasiId", lokasiId);
                model.addAttribute("departmentId", "");

            }else if(!departmentId.equals("") && !lokasiId.equals("")){
                DepartmentModel department = departmentService.getDepartmentByIdDepartment(Long.parseLong(departmentId));
                LocationModel location = locationService.getLocationByIdLocation(Long.parseLong(lokasiId));
                listJobBoth = jobService.getListByDepartmentAndLocation(department, location);
                model.addAttribute("departmentSelect", department);
                model.addAttribute("locationSelect", location);

                listJobBefore.addAll(listJobSearch);
                listJobBefore.addAll(listJobBoth);

                for (int i = 0; i < listJobBefore.size(); i++){
                    if (listJobSearch.contains(listJobBefore.get(i)) && listJobBoth.contains(listJobBefore.get(i))) {
                        filtered.add(listJobBefore.get(i));
                    }
                }

                listJobFirst.addAll(filtered);
//                System.out.println("both are present");
                model.addAttribute("departmentId", departmentId);
                model.addAttribute("lokasiId", lokasiId);

            }else{
                listJobFirst.addAll(listJobSearch);

                model.addAttribute("departmentId", "");
                model.addAttribute("lokasiId", "");
//                System.out.println("only search is present");
            }

        } else if(search.equals("")){
            System.out.println("search is not present");

            if (!departmentId.equals("") && lokasiId.equals("")){
                DepartmentModel department = departmentService.getDepartmentByIdDepartment(Long.parseLong(departmentId));
                listJobFirst = jobService.getListByDepartment(department);
                model.addAttribute("departmentSelect", department);
//                System.out.println("dept is present");
                model.addAttribute("departmentId", departmentId);
                model.addAttribute("lokasiId", "");

            }else if (!lokasiId.equals("") && departmentId.equals("")){
                LocationModel location = locationService.getLocationByIdLocation(Long.parseLong(lokasiId));
                listJobFirst = jobService.getListByLocation(location);
                model.addAttribute("locationSelect", location);
//                System.out.println("lokasi is present");
                model.addAttribute("lokasiId", lokasiId);
                model.addAttribute("departmentId", "");

            }else if(!departmentId.equals("") && !lokasiId.equals("")){
                DepartmentModel department = departmentService.getDepartmentByIdDepartment(Long.parseLong(departmentId));
                LocationModel location = locationService.getLocationByIdLocation(Long.parseLong(lokasiId));
                listJobFirst = jobService.getListByDepartmentAndLocation(department, location);
                model.addAttribute("departmentSelect", department);
                model.addAttribute("locationSelect", location);
//                System.out.println("both is present");
                model.addAttribute("departmentId", departmentId);
                model.addAttribute("lokasiId", lokasiId);

            }else{
                listJobFirst = jobService.getListJob();
//                System.out.println("none is present");
                model.addAttribute("departmentId", "");
                model.addAttribute("lokasiId", "");
            }
        }
        System.out.println("listjobfirst = " + listJobFirst.size());

        if (listJobFirst.size() <= baseListJob.size()){
            for (int i = 0; i < baseListJob.size(); i++){
                if (listJobFirst.contains(baseListJob.get(i))){
                    listJob.add(baseListJob.get(i));
                }
            }
        }

        model.addAttribute("totalPage", listJob.size()%10 == 0 ?
                listJob.size()/10 - 1 : listJob.size()/10 + 1) ;

        int indexStart = pageNo*pageSize - pageSize;
        if (indexStart + 10 <= listJob.size()){
            System.out.println("atas");
            for (int i = indexStart; i < indexStart + pageSize; i++){
               listJobFinal.add(listJob.get(i));
            }
        } else if(indexStart + 10 > listJob.size()){
            System.out.println("bawah");
            for (int i = indexStart; i < listJob.size(); i++){
                listJobFinal.add(listJob.get(i));
            }
        }

        System.out.println("listJob = " + listJob.size());

        model.addAttribute("listJob", listJobFinal);
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        model.addAttribute("pageNo", pageNo);
//        model.addAttribute("totalPage", listJob.size()/10 + 1);
        model.addAttribute("totalItem", listJobFirst.size());

        model.addAttribute("search", search);

        return "content/jobs-view-all-searchfilter";
    }

    @GetMapping(value = "/content/add-job")
    public String addJobPage(Model model){

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();

        List<String> listLocationDesc = new ArrayList<>();

        for (int i = 0; i < listLocation.size(); i ++){
            listLocationDesc.add(listLocation.get(i).getDescLocation());
        }
//        List<String> listReq = new ArrayList<>();

        model.addAttribute("job", new JobModel());
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        model.addAttribute("listLocationDesc", listLocationDesc);

        return "content/jobs-add-page";
    }

    @PostMapping(value = "/content/add-job")
    public String addJobPageConfirm(
            @ModelAttribute JobModel job,
            Model model){

        List<DepartmentModel> listDepartment = departmentService.getListDepartment();
        List<LocationModel> listLocation = locationService.getListLocation();


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
            model.addAttribute("subMsg", "There's still time before this job's deadline");
            model.addAttribute("subMsg2", "Do you want to delete this job application?");
        }else{
            model.addAttribute("subMsg", "");
            model.addAttribute("subMsg2", "Do you want to delete this job application?");
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
            model.addAttribute("pesan", "There are no open job applications yet");
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

        List<String> listLocationDesc = new ArrayList<>();

        for (int i = 0; i < listLocation.size(); i ++){
            listLocationDesc.add(listLocation.get(i).getDescLocation());
        }

        model.addAttribute("job", job);
        model.addAttribute("listDesc", listDesc);
        model.addAttribute("counterDesc", listDesc.size());
        model.addAttribute("listReq", listReq);
        model.addAttribute("counterReq", listReq.size());
        model.addAttribute("listDepartment", listDepartment);
        model.addAttribute("listLocation", listLocation);
        model.addAttribute("listLocationDesc", listLocationDesc);
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

    @GetMapping(value = "/content/viewall-applicants")
    private String viewAllApplicant(Model model){

        Page<ApplicantModel> page = applicantService.findPaginated(1, 10, "dateSent", "asc");
        List<ApplicantModel> listApplicant = page.getContent();

        if (listApplicant.size() == 0){
            model.addAttribute("pesan", "There are no applicants yet");
        }

        model.addAttribute("listApplicant", listApplicant);
        model.addAttribute("reverseSortDir", "asc");
        model.addAttribute("sortField", "none");
        model.addAttribute("sortDir", "none");

        model.addAttribute("pageNo", 1);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("totalItem", page.getTotalElements());

        return "content/applicant-view-all";

    }

    @GetMapping(value = "/content/viewall-applicants/page/{pageNo}")
    private String viewAllApplicantSorted(
            @PathVariable("pageNo") int pageNo,
            @RequestParam("sortField") Optional<String> sortField,
            @RequestParam("sortDir") Optional<String> sortDir,
            Model model){

        int pageSize = 10;

        Page<ApplicantModel> page = applicantService.findPaginated(1, 10, "dateSent", "asc");

        if (sortField.isEmpty()){
            page =applicantService.findPaginated(pageNo, pageSize, "dateSent", "asc");
            model.addAttribute("sortField", "none");
            model.addAttribute("sortDir", "none");
            model.addAttribute("reverseSortDir", "asc");


        }else{
            page =applicantService.findPaginated(pageNo, pageSize, sortField.get(), sortDir.get());
            model.addAttribute("sortField", sortField.get());
            model.addAttribute("sortDir", sortDir.get());
            model.addAttribute("reverseSortDir", sortDir.get().equals("asc") ? "desc" : "asc");

        }
        List<ApplicantModel> applicant = page.getContent();

        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("totalItem", page.getTotalElements());
        model.addAttribute("listApplicant", applicant);

        return "content/applicant-view-all";
    }

    @RequestMapping(value = "contents/viewall-applicants", method = RequestMethod.POST)
    private String viewAllApplicantsSearch(
            @RequestParam("search") Optional<String> search,
            Model model
            ){
        List<ApplicantModel> listApplicants = new ArrayList<>();
        List<ApplicantModel> listApplicantsFirst = new ArrayList<>();
        List<ApplicantModel> listAllApllicants = applicantService.getListApplicants();
        
        Page<ApplicantModel> page = applicantService.findPaginated(1, listAllApllicants.size(), "dateSent", "asc");
        List<ApplicantModel> baseListApplicants = page.getContent();

        if (search.get().equals("")){
            listApplicantsFirst.addAll(listAllApllicants);

        }else if (!search.get().equals("")){
            listApplicantsFirst = applicantService.getListApplicantByNama(search.get());
        }

        if (listApplicantsFirst.size() <= baseListApplicants.size()){
            for (int i = 0; i < baseListApplicants.size(); i++){
                if (listApplicantsFirst.contains(baseListApplicants.get(i))){
                    listApplicants.add(baseListApplicants.get(i));
                }
            }
        }
        model.addAttribute("totalPage", listApplicants.size()%10 == 0 ?
                listApplicants.size()/10 - 1 : listApplicants.size()/10 + 1);

        if(listApplicants.size() > 10){
            int size = listApplicants.size();
            for (int i = 10; i < size; i++ ){
                listApplicants.remove(listApplicants.size() - 1);
            }
        }else if (listApplicants.size() == 0){
            model.addAttribute("pesan", "There are no applicants with that name yet");
        }

        System.out.println(listApplicants.size());
        System.out.println(listApplicants.size()%10 == 0);

        model.addAttribute("listApplicant", listApplicants);
        model.addAttribute("reverseSortDir", "asc");
        model.addAttribute("sortField", "dateSent");
        model.addAttribute("sortDir", "asc");
        model.addAttribute("pageNo", 1);
//        model.addAttribute("totalPage", listApplicants.size()%10 == 0 ?
//                listApplicants.size()/10 - 1 : listApplicants.size()/10 + 1);
//        model.addAttribute("totalPage", listApplicants.size()/10 + 1);
        model.addAttribute("totalItem", listApplicantsFirst.size());
        model.addAttribute("search", search.get());
        
        return "content/applicant-view-all-searchfilter";
    }

    @GetMapping(value = "contents/viewall-applicants/page/{pageNo}")
    private String viewAllApplicantsSearchFilterSorted_(
            @PathVariable("pageNo") int pageNo,
            @RequestParam("search") String search,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,
            Model model
    ){
        int pageSize = 10;

        List<ApplicantModel> listApplicants = new ArrayList<>();
        List<ApplicantModel> listApplicantsFirst = new ArrayList<>();
        List<ApplicantModel> listApplicantsFinal = new ArrayList<>();
        List<ApplicantModel> listAllApllicants = applicantService.getListApplicants();

        Page<ApplicantModel> page = applicantService.findPaginated(1, listAllApllicants.size(), sortField, sortDir);
        List<ApplicantModel> baseListApplicants = page.getContent();

        if (search.equals("")){
            listApplicantsFirst.addAll(listAllApllicants);
        }else if (!search.equals("")){
            listApplicantsFirst = applicantService.getListApplicantByNama(search);
        }


        if (listApplicantsFirst.size() <= baseListApplicants.size()){
            for (int i = 0; i < baseListApplicants.size(); i++){
                if (listApplicantsFirst.contains(baseListApplicants.get(i))){
                    listApplicants.add(baseListApplicants.get(i));
                }
            }
        }

        model.addAttribute("totalPage", listApplicants.size()%10 == 0 ?
                listApplicants.size()/10 - 1 : listApplicants.size()/10 + 1) ;

        int indexStart = pageNo*pageSize - pageSize;
        if (indexStart + 10 <= listApplicants.size()){
            for (int i = indexStart; i < indexStart + pageSize; i++){
                listApplicantsFinal.add(listApplicants.get(i));
            }
        } else if(indexStart + 10 > listApplicants.size()){
            for (int i = indexStart; i < listApplicants.size(); i++){
                listApplicantsFinal.add(listApplicants.get(i));
            }
        }

        model.addAttribute("listApplicant", listApplicantsFinal);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalItem", listApplicantsFirst.size());
        model.addAttribute("search", search);


        return "content/applicant-view-all-searchfilter";
    }


    @GetMapping(value = "content/applicant/{idApplicant}")
    public String viewApplicant(
            @PathVariable Long idApplicant,
            Model model
    ){
        ApplicantModel applicant = applicantService.getApplicantByIdApplicant(idApplicant);

        model.addAttribute("applicant", applicant);
        model.addAttribute("fileSize", (int)Math.floor(applicant.getSizeFile()/1024));
        return "content/applicant-detail";
    }

    @GetMapping(value = "content/applicants/{idApplicant}")
    public String viewApplicants(
            @PathVariable Long idApplicant,
            Model model
    ){
        ApplicantModel applicant = applicantService.getApplicantByIdApplicant(idApplicant);

        model.addAttribute("applicant", applicant);
        model.addAttribute("fileSize", (int)Math.floor(applicant.getSizeFile()/1024));
        return "content/applicants-detail";
    }

    @GetMapping(value = "content/download/")
    public void downloadFile(
            @RequestParam("idApplicant") Long idApplicant,
            HttpServletResponse response
    ) throws IOException {

        ApplicantModel applicant = applicantService.getApplicantByIdApplicant(idApplicant);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + applicant.getNamaFile();

        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();

        outputStream.write(applicant.getContent());
        outputStream.close();


    }



}
