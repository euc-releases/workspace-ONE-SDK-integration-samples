# Copyright 2023 VMware, Inc.
# SPDX-License-Identifier: BSD-2-Clause

# Run with Python 3.7
"""Script to clean Android Studio and Gradle files and directories."""
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
# Module for recursive directory tree deletion.
# https://docs.python.org/3.5/library/shutil.html#shutil.rmtree
import shutil
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

class Cleaner:
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
    def cache(self):
        return self._cache
    @cache.setter
    def cache(self, cache):
        self._cache = cache

    @property
    def userHomeDir(self):
        return self._userHomeDir
    @userHomeDir.setter
    def userHomeDir(self, userHomeDir):
        self._userHomeDir = userHomeDir

    # End of command line properties.

    @staticmethod
    def project_deletes(projectPath):
        for glob in (
            "*.iml", ".idea", "build", ".gradle", "gradle", "gradlew"
            , "gradlew.bat"
        ):
            for path in projectPath.rglob(glob):
                yield path

    @classmethod
    def cache_deletes(cls, projectPath, userHomeDir=None):
        if userHomeDir is None:
            userHomeDir = cls.gradle_user_home_dir(projectPath)
        for sub0 in (Path(userHomeDir, 'caches').iterdir()):
            if not sub0.name.startswith('modules-'):
                continue
            for sub1 in sub0.iterdir():
                if sub1.name.startswith('files-'):
                    for sub2 in sub1.iterdir():
                        if sub2.name.startswith('com.airwatch.'):
                            yield sub2
                elif sub1.name.startswith ('metadata-'):
                    yield sub1

    @staticmethod
    def gradle_user_home_dir(projectPath):
        # Run a task defined in the project build.gradle file that prints the
        # user home directory. It'll be ~/.gradle/ on macOS, unless customised
        # somewhere.
        with subprocess.Popen(
            ('./gradlew', 'printUserHomeDir', '--console=plain', '--quiet')
            ,stdout=subprocess.PIPE, text=True, cwd=projectPath
        ) as gradleProcess:
            with gradleProcess.stdout as gradleOutput:
                lines = gradleOutput.readlines()
                if len(lines) == 1:
                    return lines[0].splitlines()[0]
                else:
                    lines[:0] = [
                        'Unexpected number of lines in Gradle output.',
                        f'Expected:1, actual:{len(lines)}. Output:'
                    ]
                    raise RuntimeError('\n'.join(lines))

    def __call__(self):
        root = Path(__file__).parent
        count = 0
        if self.cache:
            deletions = self.cache_deletes(root, self.userHomeDir)
        else:
            deletions = self.project_deletes(root)

        for deletion in sorted(deletions):
            if deletion.is_file():
                print(f'unlink "{deletion}".')
                if not self.dryRun:
                    deletion.unlink()
                count += 1
            elif deletion.is_dir():
                print(f'rmtree "{deletion}".')
                if not self.dryRun:
                    shutil.rmtree(deletion)
                count += 1
            else:
                print("Don't " f'know how to delete "{deletion}".')
        print(f'Deleted:{count}.')
        if self.dryRun:
            print("Dry run, didn't delete.")

def main(commandLine):
    argumentParser = argparse.ArgumentParser(
        formatter_class=argparse.RawDescriptionHelpFormatter,
        description=textwrap.dedent(__doc__))
    argumentParser.add_argument(
        '-c', '--cache', action='store_true', help=
        "Delete Gradle cached downloads of VMware Workspace ONE mobile SDK"
        " dependencies and all the Gradle cache metadata. Useful for forcing"
        " download or testing secret override properties.  Default is to delete"
        " Gradle build files instead.")
    argumentParser.add_argument(
        '-d', '--dry-run', dest='dryRun', action='store_true', help=
        "Print deletions that would be made but don't delete anything.")
    argumentParser.add_argument(
        '-u', '--user-home-dir', dest='userHomeDir', type=str, help=
        "Set the Gradle user home directory. Default is to run a Gradle task,"
        " printUserHomeDir, that prints the required value. Use this option if"
        " there isn't a gradlew executable in the project directory. A typical"
        " value is ~/.gradle")

    argumentParser.parse_args(commandLine[1:], Cleaner())()
    return 0

if __name__ == '__main__':
    sys.exit(main(sys.argv))
