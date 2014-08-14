<div class="page-header">
    <h3>Account Information <small>all fields are required!</small></h3>
</div>

<div class="form-group">
    <label for="username">
        <g:message code="form.field.username.label"/>
    </label>
    <input id="username" name="username" type="text" class="form-control" placeholder="Enter username..." value="${userProfile?.username}">
</div>

<div class="form-group">
    <label for="password">
        <g:message code="form.field.password.label"/>
    </label>
    <input id="password" name="password" type="password" class="form-control" placeholder="Enter password...">
</div>

<div class="form-group">
    <label for="passwordRepeat">
        <g:message code="form.field.password.repeat.label"/>
    </label>
    <input id="passwordRepeat" name="passwordRepeat" type="password" class="form-control" placeholder="Repeat password...">
</div>

<div class="page-header">
    <h3>Personal Information <small>only email is required!</small></h3>
</div>

<div class="form-group">
    <label for="firstname">
        <g:message code="form.field.firstname.label"/>
    </label>
    <input id="firstname" name="firstname" type="text" class="form-control" placeholder="Enter firstname..." value="${userProfile?.firstname}">
</div>

<div class="form-group">
    <label for="lastname">
        <g:message code="form.field.lastname.label"/>
    </label>
    <input id="lastname" name="lastname" type="text" class="form-control" placeholder="Enter lastname..." value="${userProfile?.lastname}">
</div>

<div class="form-group">
    <label for="email">
        <g:message code="form.field.email.label"/>
    </label>
    <input id="email" name="email" type="email" class="form-control" placeholder="Enter email..." value="${userProfile?.email}">
</div>