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

var trafficTicketID = 0;

var app = {
    // Application Constructor
    initialize: function () {
        this.bindEvents();
        $('#driverHome').hide();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function () {
        document.addEventListener('deviceready', this.onDeviceReady, false);

        $('#btnCheckClose').on('click', function () {
            $('#btnCheckLicence').removeClass('ui-state-disabled');
            $('#checkLicence').popup("close");
            $('#checkLicence h4').html("Please enter your driving licence number to check your driving licence status");
            $('#checkLicence h4').removeClass('error');
        });

        $.validator.addMethod("numberFormat", function (value, element) {
            return validateLicenceNumber(value);
        }, "Please enter an valid Licence Number");

        $('#formCheck').validate({
            rules: {
                txtCheckNumber: {
                    required: true,
                    numberFormat: true
                }
            },
            messages: {
                txtCheckNumber: {
                    required: "Please enter a licence number"
                }
            },
            errorPlacement: function (error, element) {
                error.appendTo(element.parent().prev());
            },
            submitHandler: function (form) {
                $('#btnCheckLicence').addClass('ui-state-disabled');
                trafficTicketID = 0;
                $.ajax({
                    url: url + "CheckLicenceStatus",
                    type: "post",
                    data: $(form).serialize(),
                    success: function (data, status) {
                        if (status === 'success') {
                            if (data === "0") {
                                $("#checkLicence h4").html("No Record founded !! please try againg with valid licence number");
                                $("#checkLicence h4").addClass('error');
                                $('#btnCheckLicence').removeClass('ui-state-disabled');
                            } else {
                                $('#home').hide();
                                $('#driverHome').show();
                                $('#btnCheckClose').click();
                                var c = JSON.parse(data);
                                for (var i = 0; i < c.all.tickelist.ticket.length; i++) {
                                    var place = c.all.tickelist.ticket[i];
//                                alert(c.all.tickelist.ticket[i].name + "  - " + c.all.tickelist.ticket[i].id);
                                    var html = '<tr role="row"> \n\
                                            <td>' + getDateWithStatus(place.date, place.status) + '</td>\n\
                                            <td id="amount[]">' + place.amount + '</td> \n\
                                            <td>' + getPayNowButton(place.status, place.id, place.amount, place.paid) + '</td> \n\
                                            <td>' + place.police + '</td> \n\
                                            <td>' + place.officer + '</td>\n\
                                            </tr>';
                                    $('#tbody').append(html);
                                }
                                $('#driverView').DataTable({
                                    rowReorder: {
                                        selector: 'td:nth-child(2)'
                                    },
                                    responsive: true
                                });
                            }
                        } else {
                            $('#btnCheckMain').addClass('ui-state-disabled');
                            $('#btnCheckLicence').removeClass('ui-state-disabled');
                            $('#btnCheckClose').click();
//                        alert('nan');
                        }
                    },
                    error: function (request, status, error) {
                        $("#checkLicence h4").html("Connection faild with the server please check your internet connection");
                        $("#checkLicence h4").addClass('error');
                        $('#btnCheckLicence').removeClass('ui-state-disabled');
                    }
                });
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
        console.log('Received Event: ' + id);
        app.initPaymentUI();
    },
    initPaymentUI: function () {
        var clientIDs = {
            "PayPalEnvironmentProduction": "1",
            "PayPalEnvironmentSandbox": "Ac67-EGnXAMAlItxFxkoKaKh5UT8VanYOCPl9gpj5sqD1Pyefcsm7bWouTCPk_J9Fg9IdedEFa6-nquV"
        };
        PayPalMobile.init(clientIDs, app.onPayPalMobileInit);

    },
    onSuccesfulPayment: function (payment) {
        console.log("payment success: " + JSON.stringify(payment, null, 4));
//        alert(JSON.parse(JSON.stringify(payment, null, 4)).response.state);
        $.post(url + 'MakeTicketPayment', {
            ticketId: trafficTicketID
        }, function (data, status) {
            if (data === '1') {
                alert('Payment Done');
                window.location.reload();
            } else {
                alert('Payment did not completed please try again');
            }
        });
    },
    // This code is only used for independent card.io scanning abilities
    onCardIOComplete: function (card) {
        console.log("Card Scanned success: " + JSON.stringify(card, null, 4));
    },
    onAuthorizationCallback: function (authorization) {
        console.log("authorization: " + JSON.stringify(authorization, null, 4));
    },
    createPayment: function (amount) {
        // for simplicity use predefined amount
        // optional payment details for more information check [helper js file](https://github.com/paypal/PayPal-Cordova-Plugin/blob/master/www/paypal-mobile-js-helper.js)
        var paymentDetails = new PayPalPaymentDetails(amount, "0.00", "0.00");
        var payment = new PayPalPayment(amount, "USD", "Ticket Payment", "Sale", paymentDetails);
        return payment;
    },
    configuration: function () {
        // for more options see `paypal-mobile-js-helper.js`
        var config = new PayPalConfiguration({merchantName: "TRAFIC Driver", merchantPrivacyPolicyURL: "https://trafic.lk/policy", merchantUserAgreementURL: "https://trafic.lk/agreement"});
        return config;
    },
    onPrepareRender: function () {

        var buyNowBtn = document.getElementById("buyNowBtn");
        var cardScanBtn = document.getElementById("cardScanBtn");

        buyNowBtn.onclick = function (e) {
            // single payment
            PayPalMobile.renderSinglePaymentUI(app.createPayment(500), app.onSuccesfulPayment, app.onUserCanceled);
        };

        cardScanBtn.onclick = function (e) {
            // card.io scanning independent of paypal payments. 
            // This is used for cases where you only need to scan credit cards and not use PayPal as funding option.
            CardIO.scan({
                "requireExpiry": true,
                "requireCVV": false,
                "requirePostalCode": false,
                "restrictPostalCodeToNumericOnly": true
            },
            app.onCardIOComplete,
                    app.onUserCanceled
                    );
        };
    },
    onPayPalMobileInit: function () {
        // must be called
        // use PayPalEnvironmentNoNetwork mode to get look and feel of the flow
        PayPalMobile.prepareToRender("PayPalEnvironmentSandbox", app.configuration(), app.onPrepareRender);
    },
    onUserCanceled: function (result) {
        console.log(result);
    }
};

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

function isAlphaOrParen(str) {
    return /^[a-zA-Z()]+$/.test(str);
}

function getPayNowButton(status, ticketId, amount, paid) {
    if (status.toString().toLowerCase() === 'paid') {
        return '<a style="color:green;" disabled="">Paid</a>';
    } else {
        if (paid.toString().toLowerCase() === 'outdate') {
            return '<p style="color:red;">Please Contact the relevant police</p>';
        } else {
            return '<button style="color:red;" onclick="payNow(' + ticketId + ',' + amount + ')">Pay</button>';
        }
    }
}

function payNow(ticketId, amount) {
    var price = CurrencyConvetor(amount, "LKR", "USD").toString();
    trafficTicketID = ticketId;
//    alert(trafficTicketID);
//    alert(price);
    if (trafficTicketID !== 0) {
        var createPayment = app.createPayment(price);
        PayPalMobile.renderSinglePaymentUI(createPayment, app.onSuccesfulPayment, app.onUserCanceled);
    } else {
        alert('Somethig wrong please restart the application');
    }
}

function getDateWithStatus(date, status) {
    if (status.toString().toLowerCase() === 'paid') {
        return '<lable style="color:green;">' + date + '</lable>';
    } else {
        return '<lable style="color:red;">' + date + '</lable>';
    }
}

function CurrencyConvetor(amount, from, to) {
    var result = '';
    var url = "https://www.google.com/finance/converter?a=" + amount + "&from=" + from + "&to=" + to;
    $.ajaxSetup({async: false});
    $.get(url,
            function (data) {
                var startPos = data.search('<div id=currency_converter_result>');
                var endPos = data.search('<input type=submit value="Convert">');
                if (startPos > 0) {
                    result = data.substring(startPos, endPos);
                    result = result.replace('<div id=currency_converter_result>', '');
                    result = result.replace('<span class=bld>', '');
                    result = result.replace('</span>', '');
                }
            });
    var usd = result.split('=')[1];
    var usd_amount = usd.split('USD')[0];
    return Math.round(usd_amount * 100) / 100;
}

app.initialize();
