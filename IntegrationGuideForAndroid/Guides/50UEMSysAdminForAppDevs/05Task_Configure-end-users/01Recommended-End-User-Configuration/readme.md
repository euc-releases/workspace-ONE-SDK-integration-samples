## Recommended End User Configuration
This guide recommends configuring UEM *Basic* end users to support application
development. Basic end users enroll by entering a user name and password at the
device.

Basic user accounts exist only in the UEM management console and aren't linked
to, for example, users in a Lightweight Directory Access Protocol (LDAP)
directory. For that reason they could be the easiest type of account to set up
and manage for development purposes.

Every UEM supports Basic users by default. In some deployments, Basic users will
be the only option. 

The following recommendations are also made.

-   Set users as managed by, and enrolling into, the root OG. See the
    [Task: Configure management console enrollment](../../03Task_Configure-management-console-enrollment/readme.md) for details of 
    what is meant by root OG.

-   Create no more users than are needed. One might be enough.

-   Set each user's password to be the same as its username. This would be bad
    practice in production, but is OK during development.

-   Use short values for username and password. During development you might
    enroll and unenroll frequently. Single-letter values, such as "a", are
    supported.

Your TestDrive UEM will come with a single automatic end user account. You
mightn't be allowed to change the automatic account's username to comply with
the above recommendations. In that case, add a new user that does comply.

Follow the [How to create an end user account](../02How-to-create-an-end-user-account/readme.md) instructions to create an end user with the above configuration.

# License
Copyright 2022 VMware, Inc. All rights reserved.  
The Workspace ONE Software Development Kit integration samples are licensed
under a two-clause BSD license.  
SPDX-License-Identifier: BSD-2-Clause