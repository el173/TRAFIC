/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//var url = "http://127.0.0.1:8080/TRAFIC/";
//var url = "http://10.0.2.2:8080/TRAFIC/";
var url = "http://192.168.0.125:8080/TRAFIC/";

var officerId = 1;
var totalAmout = 0;
var rowCount = 0;
var logitidue;
var latitude;
var licenseNumber;

var app = {
    // Application Constructor
    initialize: function () {
        this.bindEvents();
//        getPoliceStationList();
        getDriverList();
        $('#officerHome').hide();
//        $('#login').hide();
        $('#driverDetails').hide();
        $('#viewTicket').hide();
        $('#ticketItem').hide();

        getPosition();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function () {
        document.addEventListener('deviceready', this.onDeviceReady, false);

        $('#formLogin').validate({
            rules: {
                txtUserName: {
                    required: true
                },
                txtPassword: {
                    required: true
                },
                txtPolice: {
                    required: true
                }
            },
            messages: {
                txtUserName: {
                    required: "Please enter your username"
                },
                txtPassword: {
                    required: "Please enter your password"
                },
                txtPolice: {
                    required: "Select your police station"
                }
            },
            errorPlacement: function (error, element) {
                error.appendTo(element.parent().prev());
            },
            submitHandler: function (form) {
                $.ajax({
                    url: url + "OfficerLogin",
                    type: "post",
                    data: $(form).serialize(),
                    success: function (data, status) {
                        if (status === 'success' && data !== '0') {
                            officerId = data;
                            $('#login').hide();
                            $('#officerHome').show();
                        } else {
                            alert('Login Faild !!!');
                        }
                        $('#txtUserName').val(null);
                        $('#txtPassword').val(null);
                        $('#txtPolice').val(null);
                    },
                    error: function (request, status, error) {
                        alert('Please Check Your Internet Connection');
                    }
                });
            }
        });

        $.validator.addMethod("numberFormat", function (value, element) {
            return validateLicenceNumber(value);
        }, "Please enter an valid Licence Number");

        $('#formDriverCheck').validate({
            rules: {
                txtLicenseNumber: {
                    required: true,
                    numberFormat: true
                }
            },
            messages: {
                txtLicenseNumber: {
                    required: "Please enter a license number"
                }
            },
            errorPlacement: function (error, element) {
                error.appendTo(element.parent().prev());
            },
            submitHandler: function (form) {
                $.ajax({
                    url: url + "CheckLicense",
                    type: "post",
                    data: $(form).serialize(),
                    success: function (data, status) {
                        if (status === 'success' && data !== 'null') {
                            var obj = $.parseJSON(data);
                            $('#driverName').html("<b>" + obj.name + "</b>");
                            $('#driverStatus').html("<b>" + obj.status + "</b>");
                            if (obj.status.toLocaleLowerCase() === 'active') {
                                $('#driverStatus').css({'color': 'green'});
                            } else {
                                $('#driverStatus').css({'color': 'red'});
                            }
                            $('#driverHistory').html("<b>" + obj.history + "</b>");
                            $('#driverDetails').show();
                            getOffenseList();
                            $('#viewTicket').show();
                            licenseNumber = $('#txtLicenseNumber').val();
                        } else {
                            alert('Invalid Driving License');
                        }
                    },
                    error: function (request, status, error) {
                        alert('Please Check Your Internet Connection');
                    }
                });
            }
        });

        $('#btnAddTicket').on('click', function () {
            if ($('#txtOffence').val().length !== 0) {
                if (!$('#ticketItem').is(':visible')) {
                    $('#ticketItem').show();
                }
                $('#offense_list option').each(function (index) {
                    var value = $(this).val();
                    var id = $(this).text();
                    if ($('#txtOffence').val().toLowerCase() === value.toLowerCase() && !isAdded(id)) {
                        $('#tblOfence > tbody:last-child').append(
                                '<tr>'
                                + '<td id="tic' + rowCount + '">' + id + '</td>'
                                + '<td>' + value.toString().split('-')[0] + '</td>'
                                + '<td>' + value.toString().split('-')[1] + '</td>'
                                + '</tr>');
                        totalAmout += parseInt(value.toString().split('-')[1]);
                        rowCount++;
                        return false;
                    }
                });
                $('#total_amout').html('Total :' + totalAmout + ' LKR').css({'color': 'red'});
                $('#txtOffence').val(null);
            } else {
                alert('Please enter offense');
            }
        });

        $('#btnIssueTicket').on('click', function () {
            if ($('#txtVechicle').val().length !== 0) {
                var ofensess = new Array();
                for (var i = 0; i <= rowCount; i++) {
                    ofensess[i] = ($('#tic' + i).html());
                }

                $.ajax({
                    url: url + "AddTrafficTicket",
                    type: "post",
                    data: {
                        officerID: officerId,
                        DLnumber: licenseNumber,
                        total: totalAmout,
                        logi: logitidue,
                        lati: latitude,
                        vchNum: $('#txtVechicle').val().toUpperCase(),
                        ofens: ofensess
                    },
                    success: function (data, status) {
//                        alert(data+'   '+status);
                        if (data === '1' && status.toLowerCase() === 'success') {
                            alert('Traffic Ticket Added');
                            hideDetails();
                        }else{
                            alert('Something went wrong please try again!!');
                        }
                    },
                    error: function (request, status, error) {
//                    alert(request+' '+status+' '+error);
                        alert('Please Check Your Internet Connection');
                    }
                });
            } else {
                alert('Please enter the VEHICLE NUMBER');
            }
        });

        $('#btnSignOut').on('click', function () {
            if (confirm('Are You Sure Do You Want to Sign Out ??')) {
                $('#officerHome').hide();
                $('#login').show();
            }
        });
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function () {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function (id) {
//        var parentElement = document.getElementById(id);
//        var listeningElement = parentElement.querySelector('.listening');
//        var receivedElement = parentElement.querySelector('.received');
//
//        listeningElement.setAttribute('style', 'display:none;');
//        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();

function getPoliceStationList() {
    $.getJSON(url + 'GetPoliceStationList', function (json) {
        for (key in json) {
            $.each(json[key], function (k, arrayItem) {
//                            alert(arrayItem.name);
//                            $('#station_list').empty();
                $('#station_list').append($('<option></option>').val(arrayItem.name).html(arrayItem.id));
            });
        }
    });
}

function getDriverList() {
    $.getJSON(url + 'GetDriverList', function (json) {
        for (key in json) {
            $.each(json[key], function (k, arrayItem) {
//                            alert(arrayItem.name);
//                            $('#station_list').empty();
                $('#license_list').append($('<option></option>').val(arrayItem.num).html(arrayItem.id));
            });
        }
    });
}

function getPosition() {

    var options = {
        enableHighAccuracy: true,
        maximumAge: 3600000
    };

    var watchID = navigator.geolocation.getCurrentPosition(onSuccess, onError, options);

    function onSuccess(position) {

//        alert('Latitude: ' + position.coords.latitude + '\n' +
//                'Longitude: ' + position.coords.longitude + '\n' +
//                'Altitude: ' + position.coords.altitude + '\n' +
//                'Accuracy: ' + position.coords.accuracy + '\n' +
//                'Altitude Accuracy: ' + position.coords.altitudeAccuracy + '\n' +
//                'Heading: ' + position.coords.heading + '\n' +
//                'Speed: ' + position.coords.speed + '\n' +
//                'Timestamp: ' + position.timestamp + '\n');
        latitude = position.coords.latitude;
        logitidue = position.coords.longitude;
    }
    ;

    function onError(error) {
        alert('code: ' + error.code + '\n' + 'message: ' + error.message + '\n');
    }
}

function isAlphaOrParen(str) {
    return /^[a-zA-Z()]+$/.test(str);
}

function validateLicenceNumber(licenceNumber) {
    try {
        var number = licenceNumber.toString();
        if (number.length !== 8) {
            throw Error("digits count dosen't match");
        }
        var letter = number.charAt(0);
        var digits = parseInt(number.substring(1, 8));
        if (digits.toString().length !== 7 || digits === null || !(isAlphaOrParen(letter))) {
            throw Error("digits dosen't match");
        }

        return true;
    } catch (e) {
        console.log(e);
        return false;
    }
}

function isAdded(ofenseId) {
    for (var i = 0; i <= rowCount; i++) {
        if (ofenseId === $('#tic' + i).html()) {
            return true;
        }
    }
}

function getOffenseList() {
    $.getJSON(url + 'GetOfence', function (json) {
        for (key in json) {
            $.each(json[key], function (k, arrayItem) {
                $('#offense_list').append($('<option></option>').val(arrayItem.name + " - " + arrayItem.amount).html(arrayItem.id));
            });
        }
    });
}

function itemAlreadyExsits() {
    $('#tblOfence .idcell').each(function () {
        alert($(this).html());
    });
}

function hideDetails(){
    $('#driverDetails').hide();
    $('#viewTicket').hide();
    licenseNumber = 0;
    $('#txtLicenseNumber').val(null);
}