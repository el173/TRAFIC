
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
        <title>Tickets</title>

        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="font-awesome/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="css/local.css" />

        <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
        <script src="js/dataTables/jquery.dataTables.js"></script>
        <script src="js/dataTables/dataTables.bootstrap.js"></script>
        <!--<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCwjb8INn0GSRBM2gyGM4M1MxVw5xaJxKA&callback=myMap"></script>-->
        <script type="text/javascript">
            $(function () {
                $("#header").load("header.html");
                $('#ticket_details').dataTable();
            });
            function markAsPaid(ticketId) {
                if (confirm('This is can not be undone please mind what you are doing')) {
                    $.ajax({
                        url: "MarkAsPaid",
                        type: "post",
                        data: {
                            id: ticketId
                        },
                        success: function (data, status) {
                            if (status === 'success' && data !== 'null') {
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
            }
            function closeWarrant(id) {
                if (confirm('This is can not be undone please mind what you are doing')) {
                    $.ajax({
                        url: "CloseWarrant",
                        type: "post",
                        data: {
                            id: id
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
            }
            function markAsWarrant(id) {
                if (confirm('This is can not be undone please mind what you are doing')) {
                    $.ajax({
                        url: "MarkAsWarrant",
                        type: "post",
                        data: {
                            id: id
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
            }
            
        </script>
    </head>
    <body>
        <div id="wrapper">
            <div id="header"></div>
            <div class="container">
                <div class="row">
                    <div class="text-center">
                        <h1>Traffic Ticket Details</h1>
                    </div>
                    <p class="error">* NOTE: Tickets which are older than 14 days; tickets date are marked in red colour.</p>
                    <br>
                    <div class="col-lg-12">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover" id="ticket_details">
                                <thead>
                                    <tr>
                                        <th>Issue Date</th>
                                        <th>Driver</th>
                                        <th>Officer</th>
                                        <th>Fines</th>
                                        <th>Vehicle</th>
                                        <th>Location</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <police:getTickets/> 
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
