var description = document.getElementById("descTemplate");
var requirement = document.getElementById("reqTemplate");
var divDescInput = document.getElementById("col-desc");
var divDescButton = document.getElementById("col-button-desc");
var divReqInput = document.getElementById("col-req");
var divReqButton = document.getElementById("col-button-req");
var counterDesc = document.getElementById("counter-desc-add").value;
var counterReq = document.getElementById("counter-req-add").value;
var listDesc = [];
var listReq = [];
var indexSkipDesc = [];
var indexSkipReq = [];
var formValid = "";

function addBarisDesc(){
    console.log("indexskipdesc = " + indexSkipDesc);
    var i;
    for (i=1; i<= counterDesc; i ++){
        if (!indexSkipDesc.includes(i.toString())){
            listDesc.push(document.getElementById("desc-" + i).value);
        }else{
            listDesc.push("null");
        }
    }
    console.log("listdesc = " + listDesc);
    counterDesc++;
    divDescInput.innerHTML+='<input  type="text" placeholder="Enter a description" class="form-control" id="desc-'+counterDesc+'" style="margin-bottom: 28px;" required>';
    divDescButton.innerHTML+='<button id="hapusDesc-'+counterDesc+'" type="button" onclick="hapusBaris(this)" class="btn but-danger" style="margin-bottom: 24px;"> <i class="fa fa-trash"></i></button>';
    for (i=1; i < counterDesc; i ++){
        if (!indexSkipDesc.includes(i.toString())){
            document.getElementById("desc-" + i).setAttribute("value", listDesc[i-1]);
        }
    }
    listDesc = [];
}

function addBarisReq(){
    var i;
    for (i=1; i<= counterReq; i ++){
        if (!indexSkipReq.includes(i.toString())){
            listReq.push(document.getElementById("req-" + i).value);
        }else{
            listReq.push("null");
        }
    }
    counterReq++;
    divReqInput.innerHTML+='<input  type="text" placeholder="Enter a requirement" class="form-control" id="req-'+counterReq+'" style="margin-bottom: 28px;" required>';
    divReqButton.innerHTML+='<button id="hapusReq-'+counterReq+'" type="button" onclick="hapusBaris(this)" class="btn but-danger" style="margin-bottom: 24px;"> <i class="fa fa-trash"></i></button>';
    for (i=1; i< counterReq; i ++){
        if (!indexSkipReq.includes(i.toString())){
            document.getElementById("req-" + i).setAttribute("value", listReq[i-1]);
        }
    }
    listReq = [];
}

function hapusBaris(selectObject){
    var index = selectObject.id;
    // console.log(index.substring(10));
    if (index.includes("Desc")){
        indexSkipDesc.push(index.substring(10));
        document.getElementById("desc-" + index.substring(10)).remove();
        document.getElementById("hapusDesc-" + index.substring(10)).remove();
    }else{
        indexSkipReq.push(index.substring(9));
        document.getElementById("req-" + index.substring(9)).remove();
        document.getElementById("hapusReq-" + index.substring(9)).remove();

    }
    console.log(indexSkipDesc);
    console.log(indexSkipReq);

}

function submitJob(){
    var jobdesc = "";
    var jobreq = "";
    var i;
    for (i = 1; i <= counterDesc; i++){
        if (!indexSkipDesc.includes(i.toString())){
            var temp;
            if (document.getElementById("desc-" + i).value === ""){
                temp = "EMPTY";
            }else{
                temp = document.getElementById("desc-" + i).value;
            }
            jobdesc+= "---" +  temp;
        }
    }
    console.log(jobdesc);

    for (i = 1; i <= counterReq; i++){
        if (!indexSkipReq.includes(i.toString())){
            var temp;
            if (document.getElementById("req-" + i).value === ""){
                temp = "EMPTY";
            }else{
                temp = document.getElementById("req-" + i).value;
            }
            jobreq+= "---" +  temp;
        }
    }
    console.log(jobreq);
    formValid = document.forms["submitForm"].reportValidity();

    if (formValid){
        console.log(jobdesc.substring(3));
        console.log(jobreq.substring(3))
        description.setAttribute("value", jobdesc.substring(3));
        requirement.setAttribute("value", jobreq.substring(3));
        document.getElementById("submitForm").submit();
    }





}