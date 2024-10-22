# Task: Initialize the software development kit runtime
Initializing the SDK runtime is a Workspace ONE platform integration task for
mobile application developers. This task is dependent on the
[Task: Add the software development kit package](../03Task_Add-the-software-development-kit-package/readme.md).
The following instructions assume that the dependent task is complete already.

Before you begin, you will need to know the Team ID of your Apple developer
account. Instructions for locating the required value can be found on the Apple
developer website, for example here.  
[developer.apple.com/help/account/manage-your-team/locate-your-team-id](https://developer.apple.com/help/account/manage-your-team/locate-your-team-id)

Note that your app must have the same bundle identifier as it did when the
Integration Preparation Guide was being followed, as discussed in the
[Welcome](../01Welcome/readme.md) section. This could mean that you must
continue to use the same Team ID also.

How you initialize the SDK runtime depends on how the app user interface is
implemented.

-   If the app user interface is implemented in SwiftUI, follow
    the [Initialize from SwiftUI](01Initialize-from-SwiftUI/readme.md)
    instructions.

-   If the app user interface is implemented in a storyboard, follow
    the [Initialize from Storyboard](./02Initialize-from-Storyboard/readme.md)
    instructions.

## License

This software is licensed under the [Omnissa Software Development Kit (SDK) License Agreement](https://static.omnissa.com/sites/default/files/omnissa-sdk-agreement.pdf); you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

This software may also utilize Third-Pary Open Source Software as detailed within the [open_source_licenses.txt](open_source_licenses.txt) file.
