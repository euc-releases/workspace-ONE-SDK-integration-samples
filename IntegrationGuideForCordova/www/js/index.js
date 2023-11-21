/**
 *  index.js
 *  WS1 SDK integration with Apache Cordova Sample App
 *
 *  Copyright 2022 VMware, Inc.
 *  SPDX-License-Identifier: BSD-2-Clause
 */

document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
    console.log('Running cordova' + cordova.platformId + '@' + cordova.version);
    initialiseSDK()
}

/**
 * @function setSDKEventListener
 * when device is ready, call setSDKEventListener API to set event handler to receive events from the SDK.
 *
 * @param {function(event,info)} listener callback
 */
function initialiseSDK() {
    window.plugins.airwatch.setSDKEventListener(function(event, error) {
        if (event === "initSuccess") {
            console.log('Init Success');
        }
        else if(event === "initFailure") {
            alert("Init Failure");
        }
    });
}

// Information settings page is ready.
function loadInformation() {
    document.addEventListener('deviceready', fetchInformation, false);
}

// Data loss protection settings page is ready.
function loadDLPSettings() {
    document.addEventListener('deviceready', fetchDLPSettings, false);
}

// Custom settings page is ready.
function loadConfigSettings() {
    document.addEventListener('deviceready', fetchCustomSettings, false);
}

//fetch details to display information settings
function fetchInformation() {
    getUsername();
    getGroupID();
    getServerName();
    loadURL();
}

/**
 *  @function username
 *  Gets the enrolled user's username
 *
 *  @param {function(string)} Success callback
 *  @param {function} Error callback
 *
 */
function getUsername() {
    window.plugins.airwatch.username(function(username) {
            document.getElementById("username").innerHTML = username;
        },
        function() {
            console.log("Error while fetching username");
        });
}

/**
 *  @function groupId
 *  Gets the enrolled user's group ID
 *
 *  @param {function(string)} Success callback
 *  @param {function} Error callback
 */
function getGroupID() {
    window.plugins.airwatch.groupId(function(groupID) {
            document.getElementById("groupID").innerHTML = groupID;
        },
        function() {
            console.log("Error while fetching group ID");
        });
}

/**
 *  @function serverName
 *  Gets the name of the server to which the device is enrolled
 *
 *  @param {function(string)} Success callback
 *  @param {function} Error callback
 */
function getServerName() {
    window.plugins.airwatch.serverName(function(serverName) {
            document.getElementById("serverName").innerHTML = serverName;
        },
        function() {
            console.log("Error while fetching server name");
        });
}

//fetch details to display DLP settings
function fetchDLPSettings() {
    enableCopyPasteSettings();
    allowOfflineSettings();
    restrictDocumentToAppsSettings();
}

/**
 *  @function allowCopyPaste
 *  Gets the "allow copy/paste" setting for the profile.
 *  If true, then the user can copy and paste between managed apps.
 *  If false then the user cannot copy and paste between managed apps.
 *
 *  @param {function(boolean)} Success callback
 *  @param {function} Error callback
 */
function enableCopyPasteSettings() {
    window.plugins.airwatch.allowCopyPaste(function(isCopyPasteEnabled) {
            document.getElementById("enableCopyPaste").innerHTML = isCopyPasteEnabled;
        },
        function() {
            console.log("Error while fetching allow copy/paste settings");
        });
}

/**
 *  @function allowOffline
 *  Gets the "allow offline use" setting for the profile.
 *  If true, then the user can use managed apps when not connected to the network.
 *  If false, the user cannot use managed apps when not connected to the network.
 *
 *  @param {function(boolean)} Success callback
 *  @param {function} Error callback
 */
function allowOfflineSettings() {
    window.plugins.airwatch.allowOffline(function(allowOfflineEnabled) {
            document.getElementById("allowOffline").innerHTML = allowOfflineEnabled;
        },
        function() {
            console.log("Error while fetching allow offline settings ");
        });
}

/**
 *  @function restrictDocumentToApps
 *  Gets the "restrict documents to apps" setting for the profile.
 *
 *  @param {function(boolean)} Success callback
 *  @param {function} Error callback
 */
function restrictDocumentToAppsSettings() {
    window.plugins.airwatch.restrictDocumentToApps(function(isRestrictDocumentsToAppsEnabled) {
            document.getElementById("restrictDocumentsToApps").innerHTML = isRestrictDocumentsToAppsEnabled;
        },
        function() {
            console.log("Error while fetching restrict documents settings");
        });
}

/**
 *  @function customSettings
 *  Gets any custom settings provided in the app's profile
 *
 *  @param {function(string)} Success callback
 *  @param {function} Error callback
 */
function fetchCustomSettings() {
    window.plugins.airwatch.customSettings(function(customSettings) {
            document.getElementById("customSettings").innerHTML = customSettings;
        },
        function() {
            console.log("Error while fetching custom settings");
        });
}

/**
 *  @function loadURL
 *  Gets response for the provided url , performs NTLM authentication if required
 *
 *  @param {function(event,Object)} Success or Error callback
 *  @param {String} URL to load
 */
function loadURL(url) {
window.plugins.airwatch.setLoadURLEventListener(function(event, response) {
                 if (event === "loadSuccess") {
                          console.log("Load Success");
                          document.getElementById("response").innerHTML = Object.values(response);
                          }
                 else if(event === "loadFailure") {
                           console.log("Load Failure");
                           document.getElementById("response").innerHTML = Object.values(response);
                           }
          }, url);
}