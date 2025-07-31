let list = null;
const patientListContainer = document.getElementById('patientList');
const filters = document.querySelectorAll('.filters');
const category_filters = document.querySelectorAll('.category_filters');
const allSelect = document.querySelector('#allSelect');
let selectedCode = null;
const userID = document.querySelector('#userID').value;

const url = 'https://hastaizleme.com/api/patient/list?userID=' + userID;
fetch(url,{
    method: 'GET',
    headers: {
        Accept: 'application/json',
    },
}).then(function(response){
    if(response.ok){
        return response.json();
    }
}).then(function(data){
    if(data.isSuccess){
        list = data.patientList;
        let listView = '';
        for(let i = 0; i< list.length; i++){
            listView += createRow(list[i],i);
        }
        document.getElementById('rowCount').innerHTML = `${list.length}`;
        patientListContainer.innerHTML = listView;
    }else{
        document.querySelector('#errorModalContent').innerHTML = data.message;
        var errorModal = new bootstrap.Modal(document.getElementById('errorModal'), {
            keyboard: false
          });
        errorModal.toggle();
    }
});
filters.forEach(filter=>{
    filter.addEventListener('click',()=>{
        PatientListRefresh(selectedCode);
    });
});
category_filters.forEach(category=>{
    category.addEventListener('click',()=>{
        selectedCode = category.value;
        console.log(selectedCode);
        PatientListRefresh(selectedCode);
    });
});
allSelect.addEventListener('click',()=>{
    const items = document.querySelectorAll('#rowItem');
    items.forEach(item=>{
        item.checked = false;
        const parent = item.parentNode.parentNode;
        let isAvailable = parent.classList.contains('d-none');
        console.log(isAvailable);
        if(parent && !parent.classList.contains('d-none')){
            item.checked = allSelect.checked;
        }
    });
});
function PatientListRefresh(code){
    let listView = '';
    let keyMap = {}
    let isCheckedItem = false;
    let count = 0;
    for(let j = 0 ; j < filters.length; j++ ){
        if(filters[j].checked){
            let filterParse = filters[j].value.split('#');
            keyMap[filterParse[0]] = filterParse[1];
            isCheckedItem = true;
        }
    }
    if(allSelect !== null){
        allSelect.checked = false;
    }
    if(isCheckedItem){
        for(let i = 0; i< list.length; i++){
            const treatmentDoctor = list[i].treatmentDoctor;
            if(!code || isAvailableDoctorCode(code,treatmentDoctor))
                if(isAvailable(keyMap,list[i])){
                    listView += createRow(list[i],count);
                    count++;
                }
        }
    }else{
        for(let i = 0; i< list.length; i++){
            const treatmentDoctor = list[i].treatmentDoctor;
            if(!code || isAvailableDoctorCode(code,treatmentDoctor)){
                listView += createRow(list[i],count);
                count++;
            }
        }
    }
    document.getElementById('rowCount').innerHTML = `${count}`;
    patientListContainer.innerHTML = listView;
}
function isAvailableDoctorCode(drCode,treatmentDoctor){
    if(treatmentDoctor !== null){
        if(treatmentDoctor.length > 4){
            if(treatmentDoctor.substring(0,4).match(/[a-zA-Z]/)){
                let code = treatmentDoctor.substring(0,2);
                return (code === drCode);
            }else{
                return false;
            }
        }else{
            return false;
        }
    }else{
        return false;
    }
}
function isAvailable(keyMap,data){
    const survey = JSON.parse(data.survey);
    const warningDate = data.warningDate;
    const userDrug = data.isUserDrug;
    let result = true;
    let keys = Object.keys(keyMap);
    keys.forEach(key=>{
        const value = keyMap[key];
        console.log(key);
        if(key === "warning"){
            result &= attributeAvailable(value,warningDate);
        }else if(key === "userDrug"){
            result &= attributeAvailable(value,userDrug);
        }else if(key === "survey"){
            result &= attributeAvailable(value,survey);
        }else{
            if(survey !== null){
                result = result && (survey[key].includes(keyMap[key]));
            }else{
                result = false;
            }
        }
    })
    return result;
}
function attributeAvailable(key, data){
    let result = true;
    if(key !== "null"){
        if(key === "true"){
            if(!data){
                result = false;
            }
        }else{
            if(data){
                result = false;
            }
        }
    }
    return result;
}
function createRow(patient,index){
    const tableColor = (index % 2 === 0)?"":"table-primary";
    return `
        <tr class="${tableColor}">
            <th scope="row"><input class="form-check" id="rowItem" type="checkbox" name="patients" value="${patient.userID}"/></th>
            <td>${patient.tcNo}</td>
            <td>${patient.name}</td>
            <td>${patient.surname}</td>
            <td>${patient.phone}</td>
            <td>${patient.mail}</td>
            <td>${patient.treatmentDoctor}</td>
        </tr>
    `
}