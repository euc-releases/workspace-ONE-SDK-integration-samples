# Directory for secrets
This directory is set aside for secrets, such as your signing keystore and
configuration. For context, see the instructions in the
[build](../Documentation/build.md) file.

Create a `keystore` file in here, and a `keystore.gradle` file that applies it
and sets the signing configuration.

## Template for the Signing Configuration
The keystore.gradle could look like this:

    // Get the directory in which this script is located.
    // TOTH: https://stackoverflow.com/a/36527087/7657675
    def dir = buildscript.sourceFile.getParent()

    android {
        signingConfigs {
            debug {
                storeFile new File(dir, "keystore")
                storePassword 'password123'
                keyPassword 'password456'
                keyAlias = 'key0'
            }
        }

        buildTypes {
            release {
                signingConfig signingConfigs.debug
            }
        }

    }

# Notes
The following notes give some details on how the build configuration picks up
the signing configuration from this directory.

## Project Build
The project build.gradle file, [Apps/build.gradle](../Apps/build.gradle),
defines an `ext` variable that holds the location of the keystore.gradle file in
this directory.

In case this has been copied and pasted far away from the original project, the
definition is like this:

    ext {

        keystoreGradle = new RelativePath(
            true, "Secret", "keystore.gradle"
        ).getFile(new File(rootDir.getParent()))

    }

## Application Builds
The configuration and keystore can then be applied to an application by invoking
it as a script plugin from the application module's build.gradle file, like
this:

    android {
        ...
    }

    apply from: keystoreGradle

For an example, see the
[Apps/baseKotlin/buildBase.gradle](../Apps/baseKotlin/buildBase.gradle) file,
which itself is applied from the base application's build.gradle file in the
same directory.

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause