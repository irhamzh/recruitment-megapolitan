var description = document.getElementById("descTemplate");
var requirement = document.getElementById("reqTemplate");
var divDescInput = document.getElementById("col-desc");
var divDescButton = document.getElementById("col-button-desc");
var divReqInput = document.getElementById("col-req");
var divReqButton = document.getElementById("col-button-req");
var textShortDesc = document.getElementById("textShortDesc");
var counterDesc = document.getElementById("counter-desc-add").value;
var counterReq = document.getElementById("counter-req-add").value;
var listDesc = [];
var listReq = [];
var indexSkipDesc = [];
var indexSkipReq = [];
var formValid = "";
var num1 = 0;
var num2 = 0;
var numRes = 0;
var captchaValid;

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

function selectLocation(selectObject){
    var index = selectObject.value;
    // console.log(text);
    var text = listLocationDesc[index-1].toString();
    textShortDesc.value = text;

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
function submitJobApply(){
    var formValid = document.forms["applyForm"].reportValidity();

    if (formValid){
        submitCaptcha();
        if (captchaValid === "true"){
            var fileUpload = document.getElementById("fileUpload");
            if (typeof (fileUpload.files) != "undefined") {
                var size = parseFloat(fileUpload.files[0].size / 1024).toFixed(2);

                if (size > 500){
                    alert("File melebihi 500 KB")
                }else{
                    // alert(size + " KB.");
                    document.getElementById("applyForm").submit();
                }
            }
        }else if (captchaValid === "random"){
            console.log("cancel")
        }else{
            alert("Captcha Salah \nSilahkan Submit Kembali");
        }
        // document.getElementById("applyForm").submit();
    }

}

function submitCaptcha(){
    num1 = 0;
    num2 = 0;
    numRes = 0;

    num1 = Math.floor(Math.random() * 100) + Math.ceil(Math.random() * 10) ;
    num2 =( Math.floor(Math.random() * 100) - Math.ceil(Math.random() * 10) > 0 ? Math.floor(Math.random() * 100) - Math.ceil(Math.random() * 10) : Math.floor(Math.random() * 100) - Math.ceil(Math.random() * 10) + 10) ;
    numRes = num1 + num2;

    var captcha = prompt("Captcha Validation: \nWhat is " + num1 + " + " + num2 + " ?");
    if (parseInt(captcha) === numRes){
        captchaValid = "true";
    } else if (captcha === null || captcha === ''){
        captchaValid = "random";
    }else{
        captchaValid = "false";
    }

}