getNavBar();

function getNavBar() {
    $.getJSON("./current", function (user) {
        let buffer = '';
        buffer += '<strong>' + user.email + '</strong>';
        buffer += ' with roles: '
        for (let nameRole of user.roles) {
            buffer += nameRole.role.replace('ROLE_', '') + ' ';
        }
        $("#userH3").html(buffer);
    });
}

getTableForCurrentUser();

function getTableForCurrentUser() {
    $.getJSON("./current", function (user) {

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
    });
}

