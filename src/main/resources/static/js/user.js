getNavBar();
function getNavBar() {
    $.getJSON("http://localhost:8080/current", function (user) {
        let buffer = '';
        buffer += '<strong>' + user.email + '</strong>';
        buffer += ' with roles: '
        for (let nameRole of user.roles){
            buffer += nameRole.role.replace('ROLE_', '') + ' ';
        }
        $("#userH3").html(buffer);
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

// let globalUser = {};
// function getUser(callback) {
//     $.getJSON("http://localhost:8080/current", function (user) {
//         callback(user);
//     });
// }
// function setUser(user) {
//     globalUser = user;
// }
// getUser(setUser);
// console.log(globalUser);
//
// function createTableForUser(user) {
//     let dataToInsert = '';
//     dataToInsert += '<tr>';
//     dataToInsert += '<td>' + user.id + '</td>';
//     dataToInsert += '<td>' + user.name + '</td>';
//     dataToInsert += '<td>' + user.lastname + '</td>';
//     dataToInsert += '<td>' + user.age + '</td>';
//     dataToInsert += '<td>' + user.email + '</td>';
//     dataToInsert += '<td>'
//     for (let role of user["roles"]) {
//         dataToInsert += role.role.replace('ROLE_', '') + " ";
//     }
//     dataToInsert += '</td>';
//     dataToInsert += '</tr>';
//     return dataToInsert;
// }
