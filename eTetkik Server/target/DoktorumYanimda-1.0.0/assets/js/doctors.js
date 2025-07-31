let list = null;
const doctorListContainer = document.getElementById('doctorList');
const allSelect = document.querySelector('#allSelect');


const userID = document.querySelector('#userID').value;
const url = 'https://hastaizleme.com/api/doctor/list?userID=' + userID;
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
        list = data.informationList;
        let listView = '';
        for(let i = 0; i< list.length; i++){
            listView += createRow(list[i],i);
        }
        document.getElementById('rowCount').innerHTML = `${list.length}`;
        doctorListContainer.innerHTML = listView;
    }else{
        swal({
            title: "Error",
            text: data.message,
            type: "error",
            closeOnConfirm: true
        });
    }
});
allSelect.addEventListener('click',()=>{
    const items = document.querySelectorAll('#rowItem');
    for(let i = 0; i < items.length; i++){
        items[i].checked = allSelect.checked;
    }
});
function createRow(patient,index){
    const tableColor = (index % 2 === 0)?"":"table-primary";
    return `
        <tr class="${tableColor}">
            <th scope="row"><input class="form-check" id="rowItem" type="checkbox" name="patients" value="${patient.userID}"/></th>
            <td>${patient.degree}</td>
            <td>${patient.name}</td>
            <td>${patient.surname}</td>
            <td>${patient.phone}</td>
            <td>${patient.doctorID}</td>
        </tr>
    `
}