<%-- 
    Document   : admin_home
    Created on : 24-May-2017, 01:08:54
    Author     : Hashith
--%>
<%@taglib prefix="admin" uri="admin"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (session.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp?msg=end");
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="../bootstrap/js/bootstrap.js"></script>
        <script src="../js/dataTables/jquery.dataTables.js"></script>
        <script src="../js/dataTables/dataTables.bootstrap.js"></script>
        <link rel="stylesheet" type="text/css" href="../bootstrap/css/bootstrap.css">\
        <script type="text/javascript">
            function dataToTables() {
                $('#officer_list').dataTable();
                $('#police_list').dataTable();
                $('#fine_list').dataTable();

                $('#addPolice').on('click', function () {
                    if ($('#p_name').val().length !== 0 && $('#p_u_name').val().length !== 0
                            && $('#p_pass').val().length !== 0) {
                        $.ajax({
                            url: "../AddPolice",
                            type: "post",
                            data: {
                                pname: $('#p_name').val(),
                                uname: $('#p_u_name').val(),
                                pass: $('#p_pass').val()
                            },
                            success: function (data, status) {
                                if (status === 'success' && data === '1') {
                                    window.location.reload();
                                } else {
                                    alert('Something went wrong');
                                }
                            },
                            error: function (request, status, error) {
                                alert('Please Check Your Internet Connection');
                            }
                        });
                    } else {
                        alert('Invalid Input');
                    }
                });
                $('#addFine').on('click', function () {
                    if ($('#f_name').val().length !== 0 && $('#f_amount').val().length !== 0) {
                        $.ajax({
                            url: "../AddFine",
                            type: "post",
                            data: {
                                dcsr: $('#f_name').val(),
                                amount: $('#f_amount').val()
                            },
                            success: function (data, status) {
                                if (status === 'success' && data === '1') {
                                    window.location.reload();
                                } else {
                                    alert('Something went wrong');
                                }
                            },
                            error: function (request, status, error) {
                                alert('Please Check Your Internet Connection');
                            }
                        });
                    } else {
                        alert('Invalid Input');
                    }
                });
            }
            function showOfficer() {
                $('html,body').animate({scrollTop: $('#officer').offset().top}, 1000);
            }
            function showPolice() {
                $('html,body').animate({scrollTop: $('#police').offset().top}, 1000);
            }
            function showFine() {
                $('html,body').animate({scrollTop: $('#fine').offset().top}, 1000);
            }
        </script>
        <title>Admin Home</title>
    </head>
    <body onload="dataToTables()">
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="admin_home.jsp">TRAFIC Admin Panel  v1.0 (beta)</a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="#" onclick="showOfficer()"><span class="glyphicon glyphicon-user"></span> View Officer Details</a></li>
                        <li><a href="#" onclick="showPolice()"><span class="glyphicon glyphicon-list"></span> View Police Details</a></li>
                        <li><a href="#" onclick="showFine()"><span class="glyphicon glyphicon-briefcase"></span> View Fine Details</a></li>
                        <li><a href="../AdminLogout"><span class="glyphicon glyphicon-log-out"></span> Log Out</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <br><br>
        <br>
        <div class="container">
            <div class="row" id="officer">
                <div class="panel panel-default">
                    <div class="page-header">
                        <p class="center-block h3">&nbsp;<span class="glyphicon glyphicon-user"></span>  Officer Details</p>
                    </div>
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover" id="officer_list">
                                <thead>
                                    <tr>
                                        <th>Officer Name</th>
                                        <th>Officer ID</th>
                                        <th>Police</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <admin:getOfficerList/>                                    
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-primary">
                <div class="page-header">
                    <p class="center-block h3">&nbsp;<span class="glyphicon glyphicon-list"></span> Add Police</p>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-3">
                            Police Name: 
                            <input type="text" class="form-control" id="p_name"/>
                        </div>
                        <div class="col-md-3">
                            Username:
                            <input type="text" class="form-control" id="p_u_name"/>
                        </div>
                        <div class="col-md-3">
                            Password: 
                            <input type="text" class="form-control" id="p_pass"/>
                        </div>
                        <div class="col-md-2">
                            <br>
                            <input type="button" class="btn btn-primary" value="Add Police" id="addPolice"/>
                        </div>
                    </div> 
                </div> 
            </div>
            <div class="row" id="police">
                <div class="panel panel-default">
                    <div class="page-header">
                        <p class="center-block h3">&nbsp;<span class="glyphicon glyphicon-list"></span>  Police Details</p>
                    </div> 
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover" id="police_list">
                                <thead>
                                    <tr>
                                        <th>Police ID</th>
                                        <th>Police Name</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <admin:getPolicerList/>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-danger">
                <div class="page-header">
                    <p class="center-block h3">&nbsp;<span class="glyphicon glyphicon-briefcase"></span> Add Fine</p>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-5">
                            Fine Description: 
                            <input type="text" class="form-control" id="f_name"/>
                        </div>
                        <div class="col-md-5">
                            Fine Amount: 
                            <input type="number" class="form-control" id="f_amount"/>
                        </div>
                        <div class="col-md-2">
                            <br>
                            <input type="button" class="btn btn-success" value="Add Fine" id="addFine"/>
                        </div>
                    </div> 
                </div> 
            </div>
            <div class="row" id="fine">
                <div class="panel panel-default">
                    <div class="page-header">
                        <p class="center-block h3">&nbsp;<span class="glyphicon glyphicon-briefcase"></span>  Fine Details</p>
                    </div>
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover" id="fine_list">
                                <thead>
                                    <tr>
                                        <th>Fine Description</th>
                                        <th>Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <admin:getFineList/>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
