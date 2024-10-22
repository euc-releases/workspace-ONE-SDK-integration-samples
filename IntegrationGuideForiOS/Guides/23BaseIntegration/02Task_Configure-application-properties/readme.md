# Task: Configure application properties
Configuring application properties is a Workspace ONE platform integration task
for mobile application developers. This task is dependent on all the tasks in
the Integration Preparation Guide, as discussed in
the [Welcome](../01Welcome/readme.md)
section. The following instructions assume that the dependent tasks are complete
already.

These are the required configurations.

-   The Workspace ONE Intelligent Hub app will use a **custom URL scheme** to
    send enrollment details to your app. Therefore your app must declare a
    custom URL scheme.

-   Particular **Queried URL Schemes** will be used to discover Hub
    communication channels and must be declared by all SDK apps.

-   A **Camera Usage Description** must be declared by all SDK apps in order to
    support Workspace ONE QR code enrollment. If a description isn't declared
    then the operating system blocks the app from access to the device camera.

-   A **Face ID Usage Description** must be declared in order to support face
    recognition for biometric authentication. If a description isn't declared
    then the operating system blocks the app from access to Face ID. Note that
    no declaration is needed for access to Touch ID.
    
    Biometric authentication may be allowed in the Workspace ONE unified
    endpoint manager (UEM) console, as a security policy. Therefore it must be
    supported by all SDK apps.

-   **Workspace ONE doesn't support multiple windows** at time of writing. If
    your app has a scene manifest then it must declare that multiple windows
    aren't supported.

-   If you are developing more than one app, then add a
    **shared keychain group** for secure inter-application communication. The
    shared keychain group must be named `asdk` and be first in the Keychain
    Groups list.

You can set those configurations as you like if you are familiar with the Xcode
project user interface already. Skip ahead to the screen captures at the end of
the second and third sections for reference. Or you can follow these
step-by-step instructions.

## [Declare a custom URL scheme](01Declare-a-custom-URL-scheme/readme.md)

## [Add Queried URL Schemes and other required properties](02Add-Queried-URL-Schemes-and-other-required-properties/readme.md)

## [Add a shared keychain group](03Add-a-shared-keychain-group/readme.md)

## License

This software is licensed under the [Omnissa Software Development Kit (SDK) License Agreement](https://static.omnissa.com/sites/default/files/omnissa-sdk-agreement.pdf); you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

This software may also utilize Third-Pary Open Source Software as detailed within the [open_source_licenses.txt](open_source_licenses.txt) file.
