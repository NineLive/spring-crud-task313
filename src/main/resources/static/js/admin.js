$('#addNewUser').click(function () {
    addNewUser();
});

$("button.btn-danger:nth-child(2)").click(function () {
    deleteUser();
})

$("button.btn-info:nth-child(2)").click(function () {
    updateUser();
})

//Заполнение модальных окон инфой
// DELETE окно
$("#allUsersTable").click(function (event) {
    let data = event.target.parentElement.parentElement.children;
    let modalInputs = $("#deleteUserModalForm :input");
    for (let i = 0; i < modalInputs.length - 1; i++) {
        modalInputs[i].value = data[i].innerText;
    }
    let arrayRoles = data[5].innerText.split(" ");
    modalInputs[5].innerHTML = '';
    for (let role of arrayRoles) {
        let option = document.createElement('option');
        option.innerHTML = role;
        modalInputs[5].append(option);
    }
})
// EDIT окно
$("#allUsersTable").click(function (event) {
    let data = event.target.parentElement.parentElement.children;
    let modalInputs = $("#editUserModalForm :input");
    for (let i = 0; i < modalInputs.length - 2; i++) {
        modalInputs[i].value = data[i].innerText;
    }
    let arrayRoles = data[6].innerText.split(" ");
    //очищаем select с ролями
    modalInputs[7].innerHTML = '';
    let options = new Map;
    options["USER"] = document.createElement('option');
    options["USER"].innerHTML = 'USER';
    options["USER"].value = 'ROLE_USER';
    options["ADMIN"] = document.createElement('option');
    options["ADMIN"].innerHTML = 'ADMIN';
    options["ADMIN"].value = 'ROLE_ADMIN';
    for (let role of arrayRoles) {
        options[role].selected = true;
    }
    //добавляем роли в select, текущие роли будут выбраны
    modalInputs[7].append(options["ADMIN"]);
    modalInputs[7].append(options["USER"]);
})


function addNewUser() {
    event.preventDefault();
    let user = {};
    $('#newUserForm input').each(function () {
        let attr = $(this)[0].name;
        let value = $(this)[0].value;
        user[attr] = value;
    })

    let roles = [];
    for (let oneRole of $('#newUserForm select').val()) {
        let roleObj = {};
        roleObj['role'] = oneRole;
        roles.push(roleObj);
    }
    user['roles'] = roles

    $.ajax({
        url: "./admin",
        type: "POST",
        data: JSON.stringify(user),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function () {
            getTable();
        }
    });
}

function deleteUser() {
    let id = $('#idInDeleteModal').val();
    $.ajax({
        url: `./admin/${id}`,
        type: "DELETE",
        success: function () {
            getTable();
        }
    });
}

function updateUser() {
    let user = {};
    $('#editUserModalForm input').each(function () {
        let attr = $(this)[0].name;
        let value = $(this)[0].value;
        user[attr] = value;
    })

    let roles = [];
    for (let oneRole of $('#editUserModalForm select').val()) {
        let roleObj = {};
        roleObj['role'] = oneRole;
        roles.push(roleObj);
    }
    user['roles'] = roles

    $.ajax({
        url: `./admin/${user.id}`,
        type: "PATCH",
        data: JSON.stringify(user),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function () {
            getTable();
            getTableForCurrentUser();
        }
    });
}


getTable();

function getTable() {
    $.getJSON("./admin/all", function (data) {
        let dataToInsert = '';
        $.each(data, function (key) {
            let user = data[key]
            dataToInsert += '<tr>';
            dataToInsert += '<td>' + user.id + '</td>';
            dataToInsert += '<td>' + user.name + '</td>';
            dataToInsert += '<td>' + user.lastname + '</td>';
            dataToInsert += '<td>' + user.age + '</td>';
            dataToInsert += '<td>' + user.email + '</td>';
            dataToInsert += '<td>' + user.address + '</td>';
            dataToInsert += '<td>'
            for (let role of user["roles"]) {
                dataToInsert += role.role.replace('ROLE_', '') + " ";
            }
            dataToInsert += '</td>';
            dataToInsert += '<td><button type="button" class="btn btn-info" data-toggle="modal" data-target="#editUserModalCenter">Edit</button></td>';
            dataToInsert += '<td><button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteUserModalCenter">Delete</button></td>';
            dataToInsert += '</tr>';
        });
        $("#allUsersTable").html(dataToInsert);
    });
}

getCurrentUser();
function getCurrentUser() {
    $.getJSON("./current", function (user) {
        createNavBar(user);
        createTableForCurrentUser(user);
    });
}

function createTableForCurrentUser(user) {
    let dataToInsert = '';
    dataToInsert += '<tr>';
    dataToInsert += '<td>' + user.id + '</td>';
    dataToInsert += '<td>' + user.name + '</td>';
    dataToInsert += '<td>' + user.lastname + '</td>';
    dataToInsert += '<td>' + user.age + '</td>';
    dataToInsert += '<td>' + user.email + '</td>';
    dataToInsert += '<td>' + user.address + '</td>';
    dataToInsert += '<td id="rain">' + '</td>';
    dataToInsert += '<td>'
    for (let role of user["roles"]) {
        dataToInsert += role.role.replace('ROLE_', '') + " ";
    }
    dataToInsert += '</td>';
    dataToInsert += '</tr>';
    $("#currentUser").html(dataToInsert);

    if (user.addressHasRain) {
        $("#rain").html("&#9730;");
    } else {
        $("#rain").html("&#127774;");
    }
}

function createNavBar(user) {
    let buffer = '';
    buffer += '<strong>' + user.email + '</strong>';
    buffer += ' with roles: '
    for (let nameRole of user.roles) {
        buffer += nameRole.role.replace('ROLE_', '') + ' ';
    }
    $("#adminH3").html(buffer);
}