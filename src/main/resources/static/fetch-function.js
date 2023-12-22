function getUser() {
    const urlGetOneUser = 'http://localhost:8080/api/user-by-username/'
    let userName = document.getElementById('authUser').innerText
    fetch((urlGetOneUser + userName)).then(res => res.json()).then(user => {
        const userTabletTBody = document.getElementById('usersTable').getElementsByTagName('tbody')[0]
        let newRow = userTabletTBody.insertRow()

        let newCellId = newRow.insertCell()
        let newTextId = document.createTextNode(user.id);
        newCellId.appendChild(newTextId);

        let newCellUsername = newRow.insertCell()
        let newTextUsername = document.createTextNode(user.username);
        newCellUsername.appendChild(newTextUsername);

        let newCellRole = newRow.insertCell()
        let userRoles = ''
        user.roles.forEach(r => {
            userRoles += r.name + ' '
        })
        let newTextRole = document.createTextNode(userRoles);
        newCellRole.appendChild(newTextRole);
    })
}

function getAllUsers() {
    const urlGetOneAllUsers = 'http://localhost:8080/api/all-user'
    $('#usersTable>tbody>tr').remove()
    fetch(urlGetOneAllUsers).then(res => res.json()).then(users => {
        const userTabletTBody = document.getElementById('usersTable').getElementsByTagName('tbody')[0]
        for (let i = 0; i < users.length; i++) {
            let newRow = userTabletTBody.insertRow()

            let newCellId = newRow.insertCell()
            let newTextId = document.createTextNode(users[i].id);
            newCellId.appendChild(newTextId);

            let newCellUsername = newRow.insertCell()
            let newTextUsername = document.createTextNode(users[i].username);
            newCellUsername.appendChild(newTextUsername);

            let newCellRole = newRow.insertCell()
            let userRoles = ''
            users[i].roles.forEach(r => {
                userRoles += r.name + ' '
            })
            let newTextRole = document.createTextNode(userRoles);
            newCellRole.appendChild(newTextRole);

            let newCellEditButton = newRow.insertCell()
            let newEditButton = document.createElement("button")
            newEditButton.appendChild(document.createTextNode('Edit'))
            newEditButton.setAttribute("type", "button")
            newEditButton.setAttribute("class", "btn btn-info")
            newEditButton.setAttribute("data-id", `${users[i].id}`)
            newEditButton.setAttribute("data-username", `${users[i].username}`)
            newEditButton.onclick = function () {
                var $modal = $('#editUserModal').clone().removeAttr('id');
                $modal.find('#formEditId').val(users[i].id);
                $modal.find('#formEditUsername').val(users[i].username);
                let currentDate = new Date();
                $modal.find('#formEditUser').attr("id", users[i].id + users[i].username + currentDate.getTime());
                $modal.find('#formEditSubmitButton').attr("form", users[i].id + users[i].username + currentDate.getTime());
                $modal.modal('show');
            }
            newCellEditButton.appendChild(newEditButton)

            let newCellDeleteButton = newRow.insertCell()
            let newDeleteButton = document.createElement("button")
            newDeleteButton.appendChild(document.createTextNode('Delete'))
            newDeleteButton.setAttribute("type", "button")
            newDeleteButton.setAttribute("class", "btn btn-danger")
            newDeleteButton.setAttribute("data-id", `${users[i].id}`)
            newDeleteButton.setAttribute("data-username", `${users[i].username}`)
            newDeleteButton.onclick = function () {
                var $modal = $('#deleteUserModal').clone().removeAttr('id');
                $modal.find('#formDeleteId').val(users[i].id);
                $modal.find('#formDeleteUsername').val(users[i].username);
                $modal.find('#formDeleteUser').attr("id", users[i].id + users[i].username + "delete");
                $modal.find('#formDeleteUser').attr("data-user-id", users[i].id);
                $modal.find('#formDeleteSubmitButton').attr("form", users[i].id + users[i].username + "delete");
                $modal.modal('show');
            }
            newCellDeleteButton.appendChild(newDeleteButton)
        }
    })
}

async function deleteUser(id) {
    const urlDeleteUserById = 'http://localhost:8080/api/delete-user'
    await fetch((urlDeleteUserById + '?userId=' + id), {
        method: 'DELETE'
    }).catch(r => console.log(r))
    getAllUsers()
}

async function addUser(username, password, roles = [{"name": "ROLE_USER"}]) {
    const urlAddUser = 'http://localhost:8080/api/add-user'
    const headers = {
        'Content-Type': 'application/json;charset=utf-8'
    }
    const body = {
        "username": username,
        "password": password,
        "roles": roles
    }
    await fetch((urlAddUser), {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(body)
    }).catch(r => alert(r))
    getAllUsers()
}

async function editUser(id, username, password, roles = [{"name": "ROLE_USER"}]) {
    const urlAddUser = 'http://localhost:8080/api/edit-user'
    const headers = {
        'Content-Type': 'application/json;charset=utf-8'
    }
    const body = {
        "id": id,
        "username": username,
        "password": password,
        "roles": roles
    }
    await fetch((urlAddUser), {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(body)
    }).catch(r => alert(r))
    getAllUsers()
}