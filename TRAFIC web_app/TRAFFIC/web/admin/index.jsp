<%-- 
    Document   : login
    Created on : 24-Dec-2015, 12:21:45
    Author     : Hashith
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/login.css">
        <link rel="stylesheet" type="text/css" href="../bootstrap/css/bootstrap.css">
        <title>Admin Login</title>
    </head>
    <body>
        <br><br><br>
        <div class="container col-lg-offset-4 col-lg-6">
            <form action="../AdminLogin" method="post">
                <div class="row">
                    <div class="col-lg-8 center-block">
                        <h3>TRAFFIC Administrator Login</h3>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="col-lg-2">
                        <p>Username</p>
                    </div>
                    <div class="col-lg-6">
                        <input class="form-control" type="password" name="username" required=""/>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="col-lg-2">
                        <p>Password</p>
                    </div>
                    <div class="col-lg-6">
                        <input class="form-control" type="password" name="pass" required=""/>
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="col-lg-8">
                        <input class="btn btn-block btn-success" type="submit" value="Submit"/>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
