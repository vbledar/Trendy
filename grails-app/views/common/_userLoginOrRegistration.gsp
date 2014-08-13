<div class="form-container">
    <div class="page-header" style="color: darkorange">
        <h3>Sign In <small style="color: darkorange">or sign up if you haven't done already...</small></h3>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <g:form controller="user" action="authenticate" class="form-horizontal" role="form">
                <div class="form-group">
                    <label for="username" class="col-sm-3 control-label" style="color: darkorange">
                        <g:message code="form.field.username.label" default="Username"/>
                    </label>
                    <div class="col-sm-9">
                        <input id="username" name="username" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="password" class="col-sm-3 control-label" style="color: darkorange">
                        <g:message code="form.field.password.label" default="Password"/>
                    </label>
                    <div class="col-sm-9">
                        <input id="password" name="password" type="password" class="form-control"/>
                    </div>
                </div>

                <div class="btn-group pull-right">
                    <g:link controller="landing" action="authenticateUser" class="btn btn-default">
                        <img src="images/glyphicons/png/glyphicons_203_lock.png" style="max-height: 13px; max-width: 13px;"/> <g:message code="button.label.login.with.application"/>
                    </g:link>
                    <g:link controller="landing" action="facebookLogin" class="btn btn-default">
                        <img src="images/glyphicons/png/glyphicons_006_user_add.png" style="max-height: 15px; max-width: 15px;"/> <g:message code="button.label.register.with.application"/>
                    </g:link>
                </div>
            </g:form>

        </div>
    </div>
</div>