// Copyright 2022 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:workspaceone_sdk_flutter_example/main.dart';

void main() {
  testWidgets('Verify Platform version', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(MyApp());

    // Verify that platform version is retrieved.
    expect(
      find.byWidgetPredicate(
        (Widget widget) => widget is Text &&
                           widget.data.startsWith('Running on:'),
      ),
      findsOneWidget,
    );
  });
}
