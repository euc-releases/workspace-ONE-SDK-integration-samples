# Copyright 2020 VMware, Inc.
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

    @property
    def dryRun(self):
        return self._dryRun
    @dryRun.setter
    def dryRun(self, dryRun):
        self._dryRun = dryRun

    @staticmethod
    def project_deletes(projectPath):
        for glob in (
            "*.iml", ".idea", "build", ".gradle", "gradle", "gradlew", "gradlew.bat"
        ):
            for path in projectPath.rglob(glob):
                yield path

    def __call__(self):
        root = Path(__file__).parent
        count = 0
        for deletion in sorted(self.project_deletes(root)):
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
                print("Don't know how to delete", deletion)
        print(f'Deleted:{count}.')
        if self.dryRun:
            print("Dry run, didn't delete.")

def main(commandLine):
    argumentParser = argparse.ArgumentParser(
        formatter_class=argparse.RawDescriptionHelpFormatter,
        description=textwrap.dedent(__doc__))
    argumentParser.add_argument(
        '-d', '--dry-run', dest='dryRun', action='store_true', help=
        "Print deletions that would be made but don't delete anything.")

    argumentParser.parse_args(commandLine[1:], Cleaner())()
    return 0

if __name__ == '__main__':
    sys.exit(main(sys.argv))
