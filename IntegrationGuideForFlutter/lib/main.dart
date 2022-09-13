// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:workspaceone_sdk_flutter/workspaceone_sdk_flutter.dart';
import 'package:workspaceone_sdk_flutter_example/Information.dart';
import 'package:workspaceone_sdk_flutter_example/authentication.dart';
import 'package:workspaceone_sdk_flutter_example/dlp_settings.dart';
import 'package:workspaceone_sdk_flutter_example/remote_config.dart';
import 'package:workspaceone_sdk_flutter_example/tunneling.dart';

void main() {
  runApp(MaterialApp(
    title: 'SDK Features',
    home: MyApp(),
  ));
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initSDK();

    const channel = EventChannel('workspaceone_sdk_event');
    channel.receiveBroadcastStream().listen((dynamic event) {
      print('Received event: $event');//initSuccess or initFailure
    }, onError: (dynamic error) {
      print('Received error: ${error.message}');

    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initSDK() async {
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      await WorkspaceoneSdkFlutter.startSDK;
    } on PlatformException {
      print('SDK Init Failed with Exception');
    }
  }

  @override
  Widget build(BuildContext context) {
    final appTitle = 'SDK Feature Home';  
    return Scaffold(  
        appBar: AppBar(  
          title: Text(appTitle),  
        ),  
        body: ListView(  
          children: <Widget>[  
            ListTile(  
              title: Text('Information'), 
              subtitle: Text(
                  'Provides user related information.'
                ),
              isThreeLine: true,   
              onTap: (){
                  Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => Information()),
                      );
                  } ,
            ),  
            ListTile(   
              title: Text('Tunneling'), 
              subtitle: Text(
                  'Access your internal websites or APIs.'
                ),
              isThreeLine: true,
              onTap: (){
                  Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => Tunneling()),
                      );
                  } , 
            ),  
            ListTile(   
              title: Text('Authentication'),
              subtitle: Text(
                  'Automated local and network authentication.'
                ),
              isThreeLine: true,    
               onTap: (){
                  Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => Auth()),
                      );
                  } , 
            ),  
            ListTile(  
              title: Text('Remote Config'),
              subtitle: Text(
                  'Push configurations and settings to the app.'
                ),
              isThreeLine: true,   
              onTap: (){
                  Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => RemoteConfig()),
                      );
                  } , 
            ),  
            ListTile(  
              title: Text('Dlp'), 
              subtitle: Text(
                  'Data loss prevention capabilities.'
                ),
              isThreeLine: true,
              onTap: (){
                  Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => DLP()),
                      );
                  } ,   
            ),  
          ],  
        ),  
      );  
  }
}
