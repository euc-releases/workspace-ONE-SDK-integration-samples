# Task: Generate an application installer file
Generating an application installer file is a task for application developers.
The task is dependent on the
[Task: Select or create an application to integrate](../03Task_Select-or-create-an-application-to-integrate/readme.md).
The following instructions assume that the dependent task is complete already.

You will need to generate an application installer file for your app at least
once so that your app can be trusted by the Workspace ONE UEM console and Hub
app. Note that you don't need to do any Workspace ONE integration in the
application at this stage.

The installer file will be in a format known as IPA, now a back-formed
abbreviation for iOS Package Archive. You can generate an IPA file for your app
with Xcode. Instructions are provided here for convenience; for definitive
information, see the Apple developer website.

You will need an Apple account to generate an IPA file. You will also need to
know what type of account you have, specifically whether you have a developer
account or not.

-   If you have a developer account then you should use that when you are
    building your app. Follow
    the [Instructions for developer accounts](01Instructions-for-developer-accounts/readme.md)
    to generate the IPA file.

-   If you don't have a developer account, then you will have to follow
    the [Instructions for personal accounts](02Instructions-for-personal-accounts/readme.md)
    to generate the IPA file.

In case you are unsure what account type you have, see the
[Appendix: Apple Accounts](../21Appendix_Apple-Accounts/readme.md).

# License
Copyright 2023 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause