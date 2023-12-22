function onSubmitDelete(form) {
    let userId = form.userId.value
    deleteUser(userId)
    $('.modal').modal('hide');
    return false
}

function onSubmitAddUser(form) {
    let username = form.usernameAddUserForm.value
    let password = form.passwordAddUserForm.value
    let roles = []
    roles.push(form.formAddRole.value)
    addUser(username, password, roles)
    $('#home-tab').trigger('click')
    return false
}

function onSubmitEditUser(form) {
    let id = form.userIdEditForm.value
    let username = form.usernameEditForm.value
    let password = form.passwordEditForm.value
    let roles = []
    roles.push(form.formEditRole.value)
    editUser(id, username, password, roles)
    $('.modal').modal('hide');
    return false
}