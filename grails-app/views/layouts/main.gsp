<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <title>
            <g:layoutTitle default="Trendy"/>
        </title>

        <r:require modules="mainCss, customBootstrap" />

		<g:layoutHead/>
        <r:layoutResources />
	</head>
	<body>

        <g:render template="/common/menu/mainMenuBar"/>

        <div class="container-fluid">
            <g:render template="/common/userInformationMessage"/>
        </div>

        <div class="container-fluid">
		    <g:layoutBody/>
        </div>

        <g:render template="/common/footer"/>

		<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
		<g:javascript library="application"/>
        <r:layoutResources />
	</body>
</html>