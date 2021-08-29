console.log("masuk");
var deptTemp = "";
var loctTemp = "";
var department = "";
var lokasi = "";
var baseUrl = "/jobs/";
var baseContentUrl = "/contents/";
var url = "";
var countDept = 0;
var countLoct = 0;

//
function selectOnDept(selectObject){
    deptTemp = selectObject.value;
    console.log(deptTemp);
    countDept++;
}


function selectOnLocation(selectObject){
    loctTemp = selectObject.value;
    console.log(loctTemp);
    countLoct++;
}

function filterJobs(){
    if (countDept === 0 && countLoct === 0){
        if (document.getElementById("selectedDept") !== null){
            department = document.getElementById("selectedDept").value;
        }else {
            department = "";
        }

        if ( document.getElementById("selectedLoct") !== null){
            lokasi = document.getElementById("selectedLoct").value;
        }else{
            lokasi = "";
        }
    } else if (countDept > 0 && countLoct === 0){
        department = deptTemp;
        if ( document.getElementById("selectedLoct") !== null){
            lokasi = document.getElementById("selectedLoct").value;
        }else{
            lokasi = "";
        }
    } else if (countDept === 0 && countLoct > 0){
        lokasi = loctTemp;
        if (document.getElementById("selectedDept") !== null){
            department = document.getElementById("selectedDept").value;
        }else {
            department = "";
        }
    }else{
        department = deptTemp;
        lokasi = loctTemp;
    }

    if(department !== ""){

        url = "?departmentId=" + department;
        // if (lokasi === ""){
        //     url = url + "&lokasiId=";
        // }
    }

    if (lokasi !== ""){
        if(department !== ""){
            url = url + "&lokasiId=" + lokasi;
        } else if (department === ""){
            // url = "?departmentId=&lokasiId=" + lokasi;
            url = "?lokasiId=" + lokasi;
        }
    }

    if (department === "" && lokasi === ""){
        // url = "?departmentId=&lokasiId=";
        url = "";
    }
    document.getElementById("submitButtonFilter").setAttribute("href", baseUrl + url);
    console.log(baseUrl+url);
    // document.getElementById("submitButtonFilter").submit();



}