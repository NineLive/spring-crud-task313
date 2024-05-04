$('#addNewUser').click(function () {
    addNewUser();
});

$("#allUsersTable").click(function (event){
    let data = event.target.parentElement.parentElement.children;
    let modalInputs = $("#deleteUserModalForm :input");
    for (let i=0; i < modalInputs.length - 1; i++){
        modalInputs[i].value = data[i].innerText;
    }
    let arrayRoles = data[5].innerText.split(" ");
    console.log(arrayRoles);
    modalInputs[5].innerHTML='';
    for (let role of arrayRoles){
        let option = document.createElement('option');
        option.innerHTML = role;
        modalInputs[5].append(option);
    }
})
$("#allUsersTable").click(function (event){
    let data = event.target.parentElement.parentElement.children;
    let modalInputs = $("#editUserModalForm :input");
    for (let i=0; i < modalInputs.length - 2; i++){
        modalInputs[i].value = data[i].innerText;
    }
    let arrayRoles = data[5].innerText.split(" ");
    modalInputs[6].innerHTML='';
    for (let role of arrayRoles){
        let option = document.createElement('option');
        option.innerHTML = role;
        option.value = 'ROLE_' + role;
        option.selected = true;
        modalInputs[6].append(option);
    }
})

$("button.btn-danger:nth-child(2)").click(function (){
    deleteUser();
})

$("button.btn-info:nth-child(2)").click(function (){
    updateUser();
})

function addNewUser() {
    event.preventDefault();
    let user = {};
    $('#newUserForm input').each(function(){
        let attr = $(this)[0].name;
        let value = $(this)[0].value;
        user[attr]=value;
    })

    let roles = [];
    for(let oneRole of $('#newUserForm select').val()){
        let roleObj = {};
        roleObj['role'] = oneRole;
        roles.push(roleObj);
    }
    user['roles']=roles

    $.ajax({
        url: "http://localhost:8080/admin",
        type: "POST",
        data: JSON.stringify(user),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (){
            getTable();
        }
    });
}

function deleteUser() {
    let id = $('#idInDeleteModal').val();
    $.ajax({
        url: `http://localhost:8080/admin/${id}`,
        type: "DELETE",
        success: function (){
            getTable();
        }
    });
}

function updateUser() {
    // let id = $('#idInDeleteModal').val();
    let user = {};
    $('#editUserModalForm input').each(function(){
        let attr = $(this)[0].name;
        let value = $(this)[0].value;
        user[attr]=value;
    })

    let roles = [];
    for(let oneRole of $('#editUserModalForm select').val()){
        let roleObj = {};
        roleObj['role'] = oneRole;
        roles.push(roleObj);
    }
    user['roles']=roles

    $.ajax({
        url: `http://localhost:8080/admin/${user.id}`,
        type: "PATCH",
        data: JSON.stringify(user),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (){
            getTable();
        }
    });
}


getTable();
function getTable() {
    $.getJSON("http://localhost:8080/admin/all", function (data) {
        let dataToInsert = '';
        $.each(data, function (key) {
            let user = data[key]
            dataToInsert += '<tr>';
            dataToInsert += '<td>' + user.id + '</td>';
            dataToInsert += '<td>' + user.name + '</td>';
            dataToInsert += '<td>' + user.lastname + '</td>';
            dataToInsert += '<td>' + user.age + '</td>';
            dataToInsert += '<td>' + user.email + '</td>';
            dataToInsert += '<td>'
            for (let role of user["roles"]) {
                dataToInsert += role.role.replace('ROLE_', '') + " ";
            }
            dataToInsert += '<td><button type="button" class="btn btn-info" data-toggle="modal" data-target="#editUserModalCenter">Edit</button></td>';
            dataToInsert += '<td><button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteUserModalCenter">Delete</button></td>';
            dataToInsert += '</td>';
            dataToInsert += '</tr>';
        });
        $("#allUsersTable").html(dataToInsert);
    });
}

getNavBar();
function getNavBar() {
    $.getJSON("http://localhost:8080/current", function (user) {
        let buffer = '';
        buffer += '<strong>' + user.email + '</strong>';
        buffer += ' with roles: '
        for (let nameRole of user.roles){
            buffer += nameRole.role.replace('ROLE_', '') + ' ';
        }
        $("#adminH3").html(buffer);
    });
}

getTableForCurrentUser();
function getTableForCurrentUser() {
    $.getJSON("http://localhost:8080/current", function (user) {
        let dataToInsert = '';
        dataToInsert += '<tr>';
        dataToInsert += '<td>' + user.id + '</td>';
        dataToInsert += '<td>' + user.name + '</td>';
        dataToInsert += '<td>' + user.lastname + '</td>';
        dataToInsert += '<td>' + user.age + '</td>';
        dataToInsert += '<td>' + user.email + '</td>';
        dataToInsert += '<td>'
        for (let role of user["roles"]) {
            dataToInsert += role.role.replace('ROLE_', '') + " ";
        }
        dataToInsert += '</td>';
        dataToInsert += '</tr>';
        $("#currentUser").html(dataToInsert);
    });
}


