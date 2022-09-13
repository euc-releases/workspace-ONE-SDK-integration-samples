// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:workspaceone_sdk_flutter/workspaceone_sdk_flutter.dart';



class DLP extends StatefulWidget {
  @override
  _DLP createState() => _DLP();

}


class _DLP extends State<DLP> {

  bool _allowCopyPaste = false;
  bool _offlineAllowed = false;
  bool _restrictDocToApps = false;
  bool _isCompliant = false;
  bool _isCompromised = false;

 @override
  void initState() {
    super.initState();
    getCopyPaste();
    getOffline();
    getRestrictDocToApps();
    getIsCompliant();
    getIsCompromised();
  }
// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getCopyPaste() async {
    bool copyPaste;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      copyPaste = await WorkspaceoneSdkFlutter.allowCopyPaste;
    } on PlatformException {
      copyPaste = false;
      print('Failed to get Copy Paste');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _allowCopyPaste = copyPaste;
    });
  }

// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getOffline() async {
    bool offline;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      offline = await WorkspaceoneSdkFlutter.allowOffline;
    } on PlatformException {
      offline = false; 
      print('Failed to get Allow Offline');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _offlineAllowed = offline;
    });
  }


// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getRestrictDocToApps() async {
    bool restrictDocToApps;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      restrictDocToApps = await WorkspaceoneSdkFlutter.restrictDocumentToApps;
    } on PlatformException {
      restrictDocToApps = false;
      print('Failed to get Restrict Docs To App');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _restrictDocToApps = restrictDocToApps;
    });
  }

// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getIsCompromised() async {
    bool isCompromised;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      isCompromised = await WorkspaceoneSdkFlutter.isCompromised;
    } on PlatformException {
      isCompromised = false;
      print('Failed to get isCompromised');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _isCompromised = isCompromised;
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getIsCompliant() async {
    bool isCompliant;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      isCompliant = await WorkspaceoneSdkFlutter.isCompliant;
    } on PlatformException {
      isCompliant = false;
      print('Failed to get isCmpliant');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _isCompliant = isCompliant;
    });
  }

  @override
  Widget build(BuildContext context) {
    final appTitle = 'DLPSettings';  
    return Scaffold(  
        appBar: AppBar(  
          title: Text(appTitle),  
        ), 
         body: ListView(  
          children: <Widget>[  
            ListTile(  
              title: Text('Enable Copy Paste  : $_allowCopyPaste'), 
            ),  
            ListTile(   
              title: Text('Allow Offline: $_offlineAllowed'), 
            ),  
            ListTile(   
              title: Text('Restrict Document To Apps :  $_restrictDocToApps'),
            ), 
            ListTile(   
              title: Text('Is Compliant  :  $_isCompliant'),
            ),
            ListTile(   
              title: Text('Is Compromised :  $_isCompromised'),
            ),
          ],  
        ),    
      ) ;
  }
}