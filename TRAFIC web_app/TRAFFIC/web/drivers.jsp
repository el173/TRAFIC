
<!DOCTYPE html>
<%
//    response.setHeader("Cache-Control", "no-cache");
//    response.setHeader("Cache-Control", "no-store");
//    response.setHeader("Pragma", "no-cache");
//    response.setDateHeader("Expires", 0);
//    if (session.getAttribute("user") == null) {
//        response.sendRedirect("index.jsp");
//    }
%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Driver Details</title>

        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="font-awesome/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="css/local.css" />

        <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script> 
        <script type="text/javascript">
            $(function () {
                $("#header").load("header.html");
            });
        </script>
    </head>
    <body>
        <div id="wrapper">
            <div id="header"></div>
            <div id="page-wrapper">
                <div class="row">

                </div>
            </div>
        </div>    
    </body>
</html>
