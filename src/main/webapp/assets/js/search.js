const searchBtn = document.querySelector('#searchBtn');
const searchInput = document.querySelector('#searchInput');
searchBtn.addEventListener('click',()=>{
    let key = searchInput.value;
    let target = searchBtn.getAttribute("data-search-target");
    search(key.toLocaleUpperCase("tr"),target);
})
function search(key,target){
    const targetView = document.querySelector(`#${target}`);
    const rowList = targetView.querySelectorAll('tr');
    let count = 0;
    resetSelectedItem();
    rowList.forEach(row =>{
        const columnList = row.querySelectorAll('td');
        let found = false;
        if(!key)
            found = true;
        columnList.forEach(column=>{
            if(key !== null){
                const value = column.innerHTML;
                if(value){
                    if(value.includes(key)){
                        found = true;
                    }
                }
            }
        });
        if(!found){
            row.classList.add('d-none');
        }else{
            row.classList.remove('d-none');
            row.classList.remove('table-primary');   

            if(count % 2 !== 0)
                row.classList.add('table-primary');

            count++;    
                    
        }
    });
    document.getElementById('rowCount').innerHTML = `${count}`;
}
function resetSelectedItem(){
    
    document.querySelector('#allSelect').checked = false;
    const items = document.querySelectorAll('#rowItem');
    items.forEach(item=>{
        item.checked = false;
    })
}