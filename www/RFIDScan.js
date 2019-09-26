/*global cordova, module*/
module.exports = {
    ReadTIDStart: function (callback, successCallback, errorCallback) {
        document.addEventListener("EPC", callback, false);
        cordova.exec(successCallback, errorCallback, "RFIDScan", "ReadTIDStart", []);
    },
    ReadTIDStop: function (callback, successCallback, errorCallback) {
        document.removeEventListener("EPC", callback, false);
        cordova.exec(successCallback, errorCallback, "RFIDScan", "ReadTIDStop", []);
    },

    getStatusDescribe: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "RFIDScan", "getStatusDescribe", []);
    }
};
