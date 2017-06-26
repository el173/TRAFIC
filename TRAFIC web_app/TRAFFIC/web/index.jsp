<%-- 
    Document   : index
    Created on : 16-Nov-2016, 11:00:37
    Author     : Hashith
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            request.getSession().invalidate();
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>TRAFIC Login Form</title>

        <!-- CSS -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link rel="stylesheet" href="css/bootstrap.css">
        <link rel="stylesheet" href="font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="css/login_form-elements.css">
        <link rel="stylesheet" href="css/login_style.css">
    </head>
    <body onload="checkLogin('${requestScope.login}')" onpageshow="if (event.persisted) noBack();" onunload="">
        <!-- Top content -->
        <div class="top-content">  	
            <div class="inner-bg">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-8 col-sm-offset-2 text">
                            <h1><strong>TRAFIC</strong> Police Station Login</h1>
                            <div class="description">
                                <p>
                                    The centralize traffic violation and driver details system of the <a href="http://www.police.lk/" target="_blank"><strong>Sri Lankan Police.</strong></a> 
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                            <div class="form-top">
                                <div class="form-top-left">
                                    <h3>Login to TRAFIC</h3>
                                    <p id="logText">Enter your login details to log on:</p>
                                </div>
                                <div class="form-top-right">
                                    <i class="fa fa-lock"></i>
                                </div>
                            </div>
                            <div class="form-bottom">
                                <form role="form" action="ActionController" method="post" class="login-form">
                                    <div class="form-group">
                                        <label class="sr-only" for="username">Username</label>
                                        <input type="text" name="username" placeholder="Username..." class="form-username form-control" id="form-username">
                                    </div>
                                    <div class="form-group">
                                        <label class="sr-only" for="password">Password</label>
                                        <input type="password" name="password" placeholder="Password..." class="form-password form-control" id="form-password">
                                    </div>
<!--                                    <div class="form-group">
                                        <label class="sr-only" for="police">Police Station</label>
                                        <input id="form-police" type="text" name="police" placeholder="Select..." class="form-username form-control" list="station_list" >
                                        <datalist id="station_list"></datalist>
                                    </div>-->
                                    <button type="submit" class="btn">Sign in</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>   
        </div>
        <!-- Javascript -->
        <script src="js/login_jquery-1.11.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/login_jquery.backstretch.min.js"></script>
        <script src="js/login_scripts.js"></script>
        <script type="text/javascript">
        $(document).ready(function () {
//            $.getJSON('GetPoliceStationList', function (json) {
//                for (key in json) {
//                    $.each(json[key], function (k, arrayItem) {
////                            alert(arrayItem.name);
////                            $('#station_list').empty();
//                        $('#station_list').append($('<option></option>').val(arrayItem.name).html(arrayItem.id));
//                    });
//                }
//            });
//                alert(getUrlParameter('login'));

        });
        var getUrlParameter = function getUrlParameter(sParam) {
            var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                    sURLVariables = sPageURL.split('&'),
                    sParameterName,
                    i;
            for (i = 0; i < sURLVariables.length; i++) {
                sParameterName = sURLVariables[i].split('=');

                if (sParameterName[0] === sParam) {
                    return sParameterName[1] === undefined ? true : sParameterName[1];
                }
            }
        };
        function checkLogin(status) {
            noBack();
            if (!$.isEmptyObject(status)) {
                $('#logText').text("Login Faild Please try Againg !!!");
                $('#logText').css({'color': 'red', 'font-size': '100%', 'font-weight': 'bold'});
                $('#form-username').addClass('input-error');
                $('#form-password').addClass('input-error');
                $('#form-police').addClass('input-error');
            }
        }
        window.history.forward();
        function noBack() {
            window.history.forward();
        }
        </script>
    </body>
</html>
