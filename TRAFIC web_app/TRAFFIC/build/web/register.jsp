
<%@taglib prefix="police" uri="Police"%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.jsp");
    }
%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register - Add New Officer</title>

        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="font-awesome/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="css/local.css" />

        <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
        <script src="js/jquery.validate.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>  
        <script src="js/dataTables/jquery.dataTables.js"></script>
        <script src="js/dataTables/dataTables.bootstrap.js"></script>

        <style>

            div {
                padding-bottom:20px;
            }

        </style>
        <script type="text/javascript">
            var USER_ACTIVE = 1;
            var USER_DEACTIVE = 2;
            $(function () {
                $("#header").load("header.html");
                $('#newOfficer').hide();
                $('#fromHq').hide();
                dataToTables();
                $('#addOfficer').validate({
                    rules: {
                        txtOfficerId: {
                            required: true
                        },
                        txtName: {
                            required: true
                        },
                        txtUserName: {
                            required: true
                        },
                        txtPassword: {
                            required: true
                        }
                    },
                    messages: {
                        txtOfficerId: {
                            required: "Please enter OFFICER ID"
                        },
                        txtName: {
                            required: "Please enter OFFICER NAME"
                        },
                        txtUserName: {
                            required: "Please enter an USERNAME for Officer"
                        },
                        txtPassword: {
                            required: "Please enter a PASSWORD for Officer"
                        }
                    },
                    errorPlacement: function (error, element) {
                        error.insertAfter(element);
                    },
                    submitHandler: function (form) {
                        $.ajax({
                            url: "AddOfficer",
                            type: "post",
                            data: $(form).serialize(),
                            success: function (data, status) {
                                if (status === 'success' && data !== 'null') {
                                    alert(data);
                                    window.location.reload();
                                } else {
                                    alert('Something went wrong');
                                }
                            },
                            error: function (request, status, error) {
                                alert('Please Check Your Internet Connection');
                            }
                        });
                    }
                });
                $('#assignFromHQ').validate({
                    rules: {
                        txtOfficerIdHQ: {
                            required: true
                        }
                    },
                    messages: {
                        txtOfficerIdHQ: {
                            required: "Please select a OFFICER ID"
                        }
                    },
                    errorPlacement: function (error, element) {
                        error.insertAfter(element);
                    },
                    submitHandler: function (form) {
                        $.ajax({
                            url: "AddFromHQ",
                            type: "post",
                            data: {
                                id: $('#officer_list option').text()
                            },
                            success: function (data, status) {
                                if (status === 'success' && data !== 'null') {
                                    alert(data);
                                    window.location.reload();
                                } else {
                                    alert('Something went wrong');
                                }
                            },
                            error: function (request, status, error) {
                                alert('Please Check Your Internet Connection');
                            }
                        });
                    }
                });
                $('#cmbOption').change(function () {
                    if ($(this).val() === '0') {
                        $('#newOfficer').hide();
                        $('#fromHq').hide();
                    } else if ($(this).val() === '1') {
                        $('#newOfficer').show();
                        $('#fromHq').hide();
                    } else if ($(this).val() === '2') {
                        getOfficerListHQ();
                        $('#newOfficer').hide();
                        $('#fromHq').show();
                    }
                });
            });
            function dataToTables() {
                $('#officer_details').dataTable();
            }
            function changeStatus(officerId, status) {
                var s = status === USER_ACTIVE ? USER_DEACTIVE : USER_ACTIVE;
                $.ajax({
                    url: "ChangeUserStatus",
                    type: "post",
                    data: {
                        id: officerId,
                        stat: s
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
            }
            function resetPassword(officerId) {
                var result = prompt("Enter new password");
                if (result.toString().length > 0) {
                    $.ajax({
                        url: "UpdatePassword",
                        type: "post",
                        data: {
                            id: officerId,
                            pass: result
                        },
                        success: function (data, status) {
                            if (status === 'success' && data === '1') {
                                alert("Password Updated !!");
                            } else {
                                alert('Something went wrong');
                            }
                        },
                        error: function (request, status, error) {
                            alert('Please Check Your Internet Connection');
                        }
                    });
                }
            }
            function removeOfficer(officerId) {
                $.ajax({
                    url: "RemoveOfficer",
                    type: "post",
                    data: {
                        id: officerId
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
            }
            function getOfficerListHQ() {
                $.getJSON('GetHQOfficers', function (json) {
                    for (key in json) {
                        $.each(json[key], function (k, arrayItem) {
//                            alert(arrayItem.name);
//                            $('#station_list').empty();
                            $('#officer_list').append($('<option></option>').val(arrayItem.name).html(arrayItem.id));
                        });
                    }
                });
            }
        </script>
    </head>
    <body>
        <div id="header"></div>
        <div id="wrapper">
            <div class="row text-center">
                <h2>Add New Traffic Officer</h2>
            </div>
            <div class="row">
                <label for="txtSelect" class="col-md-2">
                    Select Option :
                </label>
                <div class="col-md-3">
                    <select class="form-control" id="cmbOption">
                        <option value="0">Select</option>
                        <option value="1">New Officer</option>
                        <option value="2">From Headquarters</option>
                    </select>
                </div>
            </div>
            <div id="newOfficer">
                <form id="addOfficer">
                    <div>
                        <label for="txtOfficerId" class="col-md-2">
                            Officer ID:
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" name="txtOfficerId" id="txtOfficerId" placeholder="Enter Officer ID">
                        </div>
                    </div>        
                    <div>
                        <label for="txtName" class="col-md-2">
                            Name:
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" name="txtName" id="txtName" placeholder="Enter Name">
                        </div>
                    </div>
                    <div>
                        <label for="txtUserName" class="col-md-2">
                            Username:
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" name="txtUserName" id="txtUserName" placeholder="Enter Username">
                        </div>
                    </div>
                    <div>
                        <label for="txtPassword" class="col-md-2">
                            Password:
                        </label>
                        <div class="col-md-9">
                            <input type="password" class="form-control" name="txtPassword" id="txtPassword" placeholder="Enter Password">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-2">
                        </div>
                        <div class="col-md-10">
                            <button type="submit" class="btn btn-info">
                                Add Officer
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <div id="fromHq">
                <form id="assignFromHQ">
                    <label for="txtOfficerIdHQ" class="col-md-2">
                        Officer ID:
                    </label>
                    <div class="col-md-3">
                        <input type="text" class="form-control" name="txtOfficerIdHQ" id="txtOfficerIdHQ" list="officer_list" placeholder="Select Officer ID">
                        <datalist id="officer_list"></datalist>
                    </div>
                    <div class="row">
                        <div class="col-md-5">
                        </div>
                        <div class="col-md-10">
                            <button type="submit" class="btn btn-info">
                                Add Officer
                            </button>
                        </div>
                    </div>
                </form>  
            </div>

            <div class="col-md-10">
                <h3>Manage Officer Details</h3><br/>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover" id="officer_details">
                        <thead>
                            <tr>
                                <th>Officer Name</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Status</th>
                                <th>Action</th>
                                <th>Remove Officer</th>
                            </tr>
                        </thead>
                        <tbody>
                            <police:getOfficers/>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
