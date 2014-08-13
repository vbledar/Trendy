modules = {
    application {
        resource url:'js/application.js'
    }

    mainCss {
        resource url:'css/main.css'
    }

    customBootstrap {
        dependsOn("jquery")

        resource url:'bootstrap/css/bootstrap.css'
        resource url:'bootstrap/css/bootstrap-theme.css'
        resource url:'bootstrap/js/bootstrap.js'

//        resource url:'datepicker/js/moment.js'
//        resource url:'datepicker/js/bootstrap-datetimepicker.js'
    }
}