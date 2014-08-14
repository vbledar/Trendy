<%--
  Created by IntelliJ IDEA.
  User: nvasili
  Date: 14/8/2014
  Time: 10:16 πμ
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>

    <meta name="layout" content="main">

    <title>
        <g:message code="page.title.user.registration"/>
    </title>
</head>

<body>

<div class="row">
    <div class="col-sm-6 form-container">
        <g:form controller="registration" action="registerUser">
            <g:render template="registrationFields"/>

            <div class="page-header" style="color: darkorange">
            </div>

            <g:submitButton name="Register Me" class="btn btn-default pull-right"/>
        </g:form>
    </div>
    <div class="col-sm-6">
        <div class="row">
            <div class="col-sm-12">
                <g:render template="/common/userLoginOnly" />
            </div>
        </div>

        <div class="spacer15"></div>

        <div class="row">
            <div class="col-sm-12">
                <g:render template="/common/socialNetworkLogin"/>
            </div>
        </div>
    </div>
</div>


</body>
</html>