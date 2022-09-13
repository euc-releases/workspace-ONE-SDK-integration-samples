// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:workspaceone_sdk_flutter/workspaceone_sdk_flutter.dart';

class Information extends StatefulWidget {
  @override
  _Information createState() => _Information();

}

class _Information extends State<Information> {

  String _userName = "Unknown";
  String _groupId = "Unknown";
  String _serverName = "Unknown";

 @override
  void initState() {
    super.initState();
    getUser();
    getGroupID();
    getServer();
  }
// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getUser() async {
    String user;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      user = await WorkspaceoneSdkFlutter.userName;
    } on PlatformException {
      print('Failed to get user name.');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _userName = user;
    });
  }

// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getGroupID() async {
    String groupId;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      groupId = await WorkspaceoneSdkFlutter.groupId;
    } on PlatformException {
      print('Failed to get group id.');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _groupId = groupId;
    });
  }


// Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getServer() async {
    String serverName;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      serverName = await WorkspaceoneSdkFlutter.serverName;
    } on PlatformException {
      print('Failed to get server name.');
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _serverName = serverName;
    });
  }
  @override
  Widget build(BuildContext context) {
    final appTitle = 'Information';  
    return Scaffold(  
        appBar: AppBar(  
          title: Text(appTitle),  
        ), 
        body: ListView(  
          children: <Widget>[  
            ListTile(  
              title: Text('User name : $_userName'), 
            ),  
            ListTile(   
              title: Text('Group ID : $_groupId'), 
            ),  
            ListTile(   
              title: Text('Server :  $_serverName'),
            ), 
          ],  
        ),   
      ) ;
  }
}