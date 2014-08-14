<div class="form-container">
    <div class="page-header">
        <h3>Sign In <small>using a username and a password...</small></h3>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <g:form controller="user" action="authenticate" class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="username" class="col-sm-3 control-label">
                        <g:message code="form.field.username.label" default="Username"/>
                    </label>
                    <div class="col-sm-9">
                        <input id="username" name="username" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="password" class="col-sm-3 control-label">
                        <g:message code="form.field.password.label" default="Password"/>
                    </label>
                    <div class="col-sm-9">
                        <input id="password" name="password" type="password" class="form-control"/>
                    </div>
                </div>

                <div class="btn-group pull-right">
                    <g:link controller="landing" action="authenticateUser" class="btn btn-default">
                        <g:img dir="images/glyphicons/png" file="glyphicons_203_lock.png" style="max-width: 13px; max-height: 13px;"/> <g:message code="button.label.login.with.application"/>
                    </g:link>
                </div>
            </g:form>

        </div>
    </div>
</div>