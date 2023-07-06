// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:workspaceone_sdk_flutter/workspaceone_sdk_flutter.dart';
import 'package:workspaceone_sdk_flutter_example/web_view_container.dart';
import 'web_view_container.dart';


class Auth extends StatefulWidget {
  @override
  _Auth createState() => _Auth();

}


class _Auth extends State<Auth> {
  var _links = 'https://google.com';

  @override
  Widget build(BuildContext context) {
    final appTitle = 'Authentication';
    return Scaffold(
        appBar: AppBar(
          title: Text(appTitle),
        ),
        body: SafeArea(
            child: SingleChildScrollView(
                child: Column(
                    children : <Widget> [
                      Padding(
                        padding: const EdgeInsets.only(left:15.0,right: 15.0,top:15,bottom: 0),
                        child: TextField(
                          decoration: InputDecoration(
                              border: OutlineInputBorder(),
                              labelText: 'Enter the url with http/https',
                              hintText: 'Enter valid url as https://google.com'),
                          onChanged: (text) {
                            _links = text;
                          },
                        ),
                      ),
                      //     Container(
                      // //       padding: EdgeInsets.all(20.0),
                      // //       child: FlatButton(
                      // //         color: Theme.of(context).primaryColor,
                      // //         padding: const EdgeInsets.symmetric(horizontal: 50.0, vertical: 15.0),
                      // //         child: Text('Go'),
                      // //         onPressed: () => _handleURLButtonPress(context, _links),
                      // // )
                      //     ),
                    ]
                )
            )

        )
    );
  }

  void _handleURLButtonPress(BuildContext context, String url) {
    Navigator.push(context,
        MaterialPageRoute(builder: (context) => WebViewContainer(url, 'Authentication')));
  }
}