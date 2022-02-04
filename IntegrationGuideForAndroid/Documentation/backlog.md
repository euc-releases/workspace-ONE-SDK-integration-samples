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

-   Add a splash screen to the sample apps.

    -   https://levelup.gitconnected.com/implement-a-slash-screen-in-android-6589fa539317

-   Maybe add gesture navigation.

    If the sample app UI gets more screens added to it, maybe use gestures to
    navigate between them. See:

    -   https://developer.android.com/training/gestures/detector#kotlin

-   Maybe use a TextView and layout instead of Canvas.drawText()

    A change could be made in the brandDynamic apps, in the
    BitmapBrandingManager class, in the makeBitmap() method. Instead of
    instantiating a Canvas and then using drawText to add text, a TextView could
    be inflated from a layout on the fly. The text being painted could then have
    styling applied to it by the built-in platform mechanism.

    See some of the following.

    -   https://developer.android.com/reference/android/widget/TextView
    -   https://developer.android.com/reference/android/view/LayoutInflater.html
    -   https://developer.android.com/reference/android/text/StaticLayout
    -   https://stackoverflow.com/questions/7913496/how-to-inflate-framelayout#7916281
    -   https://stackoverflow.com/questions/13599553/how-to-a-apply-style-to-all-the-textviews-i-have#13599666
    -   https://stackoverflow.com/questions/41779934/how-is-staticlayout-used-in-android#41779935

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause