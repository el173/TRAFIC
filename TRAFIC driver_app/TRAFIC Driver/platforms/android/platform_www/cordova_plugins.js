cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "id": "card.io.cordova.mobilesdk.CardIO",
        "file": "plugins/card.io.cordova.mobilesdk/www/cdv-plugin-card-io.js",
        "pluginId": "card.io.cordova.mobilesdk",
        "clobbers": [
            "CardIO"
        ]
    },
    {
        "id": "com.paypal.cordova.mobilesdk.PayPalMobile",
        "file": "plugins/com.paypal.cordova.mobilesdk/www/cdv-plugin-paypal-mobile-sdk.js",
        "pluginId": "com.paypal.cordova.mobilesdk",
        "clobbers": [
            "PayPalMobile"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.3.0",
    "cordova-plugin-console": "1.0.4",
    "card.io.cordova.mobilesdk": "2.1.0",
    "com.paypal.cordova.mobilesdk": "3.3.1"
};
// BOTTOM OF METADATA
});