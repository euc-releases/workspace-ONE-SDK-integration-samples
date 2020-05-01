# Backlog
The following pieces of work have been identified to do in the Workspace ONE
Integration Guid for Android repository. For an introduction to the repository,
see the [parent directory](..) readme file.

For some specific notes on how the code in this repository is structured, see
the [contributing](contributing.md) file.

-   Create an AirWatchSDKBaseIntentService subclass for Framework that calls the
    data wipe method.

        SDKContextManager.getSDKContext().sdkClearAction.clear(SDKClearAction.Type.ALL)
    
    That should be in the integration guide too.

-   Maybe the BrandingApplication and BrandingAWApplication classes shouldn't be
    subclasses. Having them as subclasses means there isn't a single source file
    that serves as a snippet.

-   Add some Gradle code that sets the ext.airwatchVersion variable by
    inspecting the Download/ directory.

# License
The Workspace ONE Mobile Software Development Kit Integration Samples are:  
Copyright 2020 VMware, Inc.  
And licensed under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause