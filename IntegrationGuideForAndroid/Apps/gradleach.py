# Copyright 2023 VMware, Inc.
# SPDX-License-Identifier: BSD-2-Clause

# Run with Python 3.7
"""Script to run a Gradle task in each sub-project, one at a time."""
#
# Uses the following recent Python features.
# -   Python 3.4 pathlib Path class.
# -   Python 3.6 f-strings aka format strings.
#
# Standard library imports, in alphabetic order.
# 
# Module for command line switches.
# Tutorial: https://docs.python.org/3/howto/argparse.html
# Reference: https://docs.python.org/3/library/argparse.html
import argparse
#
# Module for OO path handling.
# https://docs.python.org/3/library/pathlib.html
from pathlib import Path
#
# Module for spawning a process to run a command.
# https://docs.python.org/3/library/subprocess.html
import subprocess
#
# Module for shell return code.
# https://docs.python.org/3/library/sys.html
import sys
#
# Module for text dedentation.
# Only used for --help description.
# https://docs.python.org/3/library/textwrap.html
import textwrap

class GradleEach:
    def __init__(self):
        self._dryRun = False

    # Command line properties.
    @property
    def dryRun(self):
        return self._dryRun
    @dryRun.setter
    def dryRun(self, dryRun):
        self._dryRun = dryRun

    @property
    def one(self):
        return self._one
    @one.setter
    def one(self, one):
        self._one = one

    @property
    def gradleTask(self):
        return self._gradleTask
    @gradleTask.setter
    def gradleTask(self, gradleTask):
        self._gradleTask = gradleTask

    # End of command line properties.

    def __call__(self):
        root = Path(__file__).parent
        # The glob pattern on the next line skips the build.gradle in the
        # project root and includes only build.gradle files from one level down.
        for buildGradle in root.glob("*/build.gradle"):
            # On the next line, parts[-2] will be the last path segment before
            # `build.gradle` which will also be the sub-project name. It's a
            # Python negative array index.
            commandLine = (
                './gradlew', f':{buildGradle.parts[-2]}:{self.gradleTask}'
                , '--console=plain', '--quiet')
            if self.dryRun:
                print(" ".join(commandLine))
            else:
                ran = subprocess.run(commandLine, cwd=root)
                if ran.returncode != 0:
                    break
            if self.one:
                break

def main(commandLine):
    defaultTask = "testNoAppUninstall"
    argumentParser = argparse.ArgumentParser(
        formatter_class=argparse.RawDescriptionHelpFormatter,
        description=textwrap.dedent(__doc__))
    argumentParser.add_argument(
        '-t', '--task', dest='gradleTask', type=str, default=defaultTask, help=
        f'Task to execute. Default: "{defaultTask}".')
    argumentParser.add_argument(
        '-d', '--dry-run', dest='dryRun', action='store_true', help=
        "Print command lines that would be run but don't run them.")
    argumentParser.add_argument(
        '-o', '--one', action='store_true', help=
        "Test by only processing one sub-project."
        " Default is to process all sub-projects.")
    argumentParser.parse_args(commandLine[1:], GradleEach())()
    return 0

if __name__ == '__main__':
    sys.exit(main(sys.argv))
