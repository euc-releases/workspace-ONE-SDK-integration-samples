// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:workspaceone_sdk_flutter/workspaceone_sdk_flutter.dart';


class RemoteConfig extends StatefulWidget {
  @override
  _RemoteConfig createState() => _RemoteConfig();

}


class _RemoteConfig extends State<RemoteConfig> {
  String _userName = "Unknown";

 @override
  void initState() {
    super.initState();
    getUser();
   
  }
// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getUser() async {
    String user;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      user = await WorkspaceoneSdkFlutter.customSettings;
    } on PlatformException {
      user = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _userName = user;
    });
  }
  @override
  Widget build(BuildContext context) {
    final appTitle = 'Remote Config/Custom Settings';  
    return Scaffold(  
        appBar: AppBar(  
          title: Text(appTitle),  
        ),  
        body: Text('Remote Config : $_userName'),
      ) ;
  }
}