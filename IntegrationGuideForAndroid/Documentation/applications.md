# Applications
The project in the Workspace ONE Integration Guide for Android code repository
contains a number of demonstration applications. Links to the application code
are here. For an introduction to the repository, see the [parent directory](..)
readme file.

There is a single project under the [Apps](../Apps) directory. Individual
applications are in sub-directories, each holding a complete application written
either in Java or in Kotlin, as follows.

# Base Application
The base applications have no SDK integration. The code is used as a base for
other applications.

-   [Apps/baseJava](../Apps/baseJava)
-   [Apps/baseKotlin](../Apps/baseKotlin)

# Base Integration Guide
The following applications are demonstrations for the Base Integration Guide.

-   Client level integration:
    -   [Apps/clientJava](../Apps/clientJava)
    -   [Apps/clientKotlin](../Apps/clientKotlin)

-   Framework level integration, either by delegation or by extension:
    -   [Apps/frameworkDelegateJava](../Apps/frameworkDelegateJava)
    -   [Apps/frameworkDelegateKotlin](../Apps/frameworkDelegateKotlin)
    -   [Apps/frameworkExtendJava](../Apps/frameworkExtendJava)
    -   [Apps/frameworkExtendKotlin](../Apps/frameworkExtendKotlin)

# Branding Integration Guide
The following applications are demonstrations for the Branding Integration
Guide.

These applications are all integrated to the Framework level, either by
delegation or by extension.

-   Enterprise branding support only:
    -   [Apps/brandEnterpriseOnlyDelegateJava](../Apps/brandEnterpriseOnlyDelegateJava)
    -   [Apps/brandEnterpriseOnlyDelegateKotlin](../Apps/brandEnterpriseOnlyDelegateKotlin)
    -   [Apps/brandEnterpriseOnlyExtendJava](../Apps/brandEnterpriseOnlyExtendJava)
    -   [Apps/brandEnterpriseOnlyExtendKotlin](../Apps/brandEnterpriseOnlyExtendKotlin)

-   Static application branding:
    -   [Apps/brandStaticDelegateJava](../Apps/brandStaticDelegateJava)
    -   [Apps/brandStaticDelegateKotlin](../Apps/brandStaticDelegateKotlin)
    -   [Apps/brandStaticExtendJava](../Apps/brandStaticExtendJava)
    -   [Apps/brandStaticExtendKotlin](../Apps/brandStaticExtendKotlin)

-   Static application branding with optional override to enterprise branding:
    -   [Apps/brandEnterprisePriorityDelegateJava](../Apps/brandEnterprisePriorityDelegateJava)
    -   [Apps/brandEnterprisePriorityDelegateKotlin](../Apps/brandEnterprisePriorityDelegateKotlin)
    -   [Apps/brandEnterprisePriorityExtendJava](../Apps/brandEnterprisePriorityExtendJava)
    -   [Apps/brandEnterprisePriorityExtendKotlin](../Apps/brandEnterprisePriorityExtendKotlin)

-   Dynamic branding:
    -   [Apps/brandDynamicDelegateJava](../Apps/brandDynamicDelegateJava)
    -   [Apps/brandDynamicDelegateKotlin](../Apps/brandDynamicDelegateKotlin)
    -   [Apps/brandDynamicExtendJava](../Apps/brandDynamicExtendJava)
    -   [Apps/brandDynamicExtendKotlin](../Apps/brandDynamicExtendKotlin)

# Duplication
A lot of the code in the project is duplicated between applications. In theory,
code could be, for example, pulled in from common directories by Gradle or a
custom tool. In practice, those approaches have limitations, lead to more
maintenance overhead than duplication, and don't result in a repository that is
easy to understand for prospective application developers.  
Duplication is managed by the maintainers of the repository.

# License
Copyright 2020 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause