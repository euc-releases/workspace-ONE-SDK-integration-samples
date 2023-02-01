# Copyright 2023 VMware, Inc.
# SPDX-License-Identifier: BSD-2-Clause

# Run with Python 3.7
"""Script to check duplication of source files and resources."""
#
# Standard library imports, in alphabetic order.
#
# Module for command line switches.
# Tutorial: https://docs.python.org/3/howto/argparse.html
# Reference: https://docs.python.org/3/library/argparse.html
import argparse
#
# Date module.
# https://docs.python.org/3/library/datetime.html
import datetime
#
# Sequence comparison module.
# https://docs.python.org/3/library/difflib.html#difflib.context_diff
from difflib import context_diff
#
# Enumeration class module.
# https://docs.python.org/3/library/enum.html
import enum
#
# Module for old school paths. Only used to get commonpath(), which doesn't have
# an equivalent in OO paths.
# https://docs.python.org/3/library/os.path.html
import os.path
#
# Module for OO path handling.
# https://docs.python.org/3/library/pathlib.html
from pathlib import Path
#
# Regular expressions module.
# https://docs.python.org/3/library/re.html
import re
#
# Module for file and directory handling.
# https://docs.python.org/3.5/library/shutil.html
import shutil
#
# Module for spawning a process to run a command.
# https://docs.python.org/3/library/subprocess.html
import subprocess
#
# Module for getting the command line.
# https://docs.python.org/3/library/sys.html
import sys
#
# Temporary file module.
# https://docs.python.org/3/library/tempfile.html
from tempfile import NamedTemporaryFile
#
# Module for text dedentation.
# Only used for --help description.
# https://docs.python.org/3/library/textwrap.html
import textwrap

def glob_each(path, patterns):
    matchCount = 0
    match1 = None
    for pattern in patterns:
        matches = []
        for match in path.glob(pattern):
            matches.append(match)
            yield match

        if len(matches) == 0:
            raise ValueError(f'Failed, no match for "{pattern}".')
        
        matchCount += len(matches)
        match1 = matches[0]

    if matchCount == 1:
        raise ValueError(
            f'Failed, only one match for "{patterns}": {match1}.')

class Rules:

    # Maybe some uncommon Python syntax here:
    #
    # -   Function parameters have leading * to make them be a vararg type of
    #     list.
    # -   Format string, aka f-string, with interpolated values.
    # -   Nested list comprehensions, but without any parentheses.
    #
    # Where the function is called, it is prefixed with another *, as the spread
    # operator.
    def _java_kt(*stubs):
        return [
            f'**/src/**/{stub}.{extension}'
            for stub in stubs
            for extension in ('java', 'kt')
        ]

    # Some patterns have /**/src/**/ embedded. This is to prevent matching files
    # of the same name in a build directory. For example:
    #
    # -   AndroidManifest.xml matching intermediate manifests under the build
    #     directory.
    # -   Java files generated for Kotlin files by some annotation processor or
    #     other.
    simplePatterns = [
        # Deliberate failures.
        'nuffin', 'samers*',

        # Files that should be the same everywhere.
        '**/proguard-rules.pro', '**/buildBase.gradle',
        '**/publicMavenClient.gradle', '**/publicMavenFramework.gradle',
        *_java_kt(
            'AirWatchSDKIntentService', 'BaseActivity', 'Application',
            'AWApplication', 'BrandingManager', 'BitmapBrandingManager'
        ),
        # User interface tests are written in Kotlin, even for Java sample
        # applications. The CamelSpaced String extension is required by the test
        # code, and used by the Kotlin sample code, but isn't used by the Java
        # sample code. The CamelSpaced.kt file will be in the main/ source for a
        # Kotlin sample app but in the androidTest/ source for a Java sample
        # app.
        '**/CamelSpaced.kt', '**/BaseActivityTest.kt',

        # Commented out because there's only one of each.
        # '**/DynamicBrandApplication.java', '**/DynamicBrandApplication.kt',

        # build.gradle files that aren't the same as the framework.
        # Pre-framework builds have different dependencies.
        'base*/build.gradle', 'client*/build.gradle',

        # application.properties files that set applicationClass values
        # different to any framework builds.
        'brandDynamicDelegate*/**/application.properties',
        'brandDynamicExtend*/**/application.properties',
        'brandEnterprise*Delegate*/**/application.properties',
        'brandEnterprise*Extend*/**/application.properties',

        # XML files that can have a simple pattern.
        'brandStatic*/**/values/styles.xml',
        'brandStatic*/**/values-v31/styles.xml',
        'brandDynamic*/**/strings.xml', 'brandDynamic*/**/activity_main.xml',
        'base*/**/strings.xml', 'client*/**/strings.xml',

        # Pre-Framework Android manifests.
        'base*/**/src/**/AndroidManifest.xml',
        'client*/**/src/**/AndroidManifest.xml',

        # MainActivity classes that aren't the same as the framework.
        'brandDynamic*Java/**/MainActivity.java',
        'brandDynamic*Kotlin/**/MainActivity.kt'
    ]

    namedPatternLists = {
        "unbranded values/styles.xml files": [
            'base*/**/values/styles.xml',
            'client*/**/values/styles.xml',
            'framework*/**/values/styles.xml'
        ],

        "unbranded values-v31/styles.xml files": [
            # Next two patterns are commented out because they don't match
            # anything, in turn because the base* apps and client* apps don't
            # yet have v31 style resources.
            # 'base*/**/values-v31/styles.xml',
            # 'client*/**/values-v31/styles.xml',
            'framework*/**/values-v31/styles.xml'
        ],

        "unbranded strings.xml files": [
            'framework*/**/strings.xml',
            'brandEnterpriseOnly*/**/strings.xml'
        ],
        "application branded strings.xml files": [
            'brandStatic*/**/strings.xml',
            'brandEnterprisePriority*/**/strings.xml'
        ],

        "unbranded layout": [
            'client*/**/activity_main.xml',
            'framework*/**/activity_main.xml',
            'brandEnterpriseOnly*/**/activity_main.xml'
        ],
        "single logo layout": [
            'brandEnterprisePriority*/**/activity_main.xml',
            'brandStatic*/**/activity_main.xml'
        ],

        "framework manifests": [
            'framework*/**/src/**/AndroidManifest.xml',
            'brand*/**/src/**/AndroidManifest.xml'
        ],
        "public Maven framework build.gradle files": [
            'brandDynamic*/**/build.gradle',
            'brandEnterprise*/**/build.gradle',
            'brandStatic*/**/build.gradle',
            'dlp*/**/build.gradle',
            'framework*/**/build.gradle'
        ],
        "framework delegate application.properties files": [
            'frameworkDelegate*/**/application.properties',
            'brandStaticDelegate*/**/application.properties'
        ],
        "framework extend application.properties files": [
            'frameworkExtend*/**/application.properties',
            'brandStaticExtend*/**/application.properties',
            'dlpExtend*/**/application.properties'
        ],
        "base MainActivity.java files": [
            'framework*/**/src/**/MainActivity.java',
            'brandEnterprise*/**/src/**/MainActivity.java',
            'brandStatic*/**/src/**/MainActivity.java'
        ],
        "base MainActivity.kt files": [
            'framework*/**/MainActivity.kt',
            'brandEnterprise*/**/MainActivity.kt',
            'brandStatic*/**/MainActivity.kt'
        ]
    }

    # ToDo: Directory trees that should be the same
    # 'appBase*/src/main/',

    def __init__(self, topPath):
        super()
        self._topPath = topPath

    @classmethod
    def named_lists(cls):
        for simplePattern in cls.simplePatterns:
            yield f'"{simplePattern}"', [simplePattern]

        for description, patterns in cls.namedPatternLists.items():
            yield description, patterns
    
    def named_globs(self):
        for description, patterns in self.named_lists():
            yield description, glob_each(self._topPath, patterns)

    def patterns_matched_by(self, targetPath):
        for description, patterns in self.named_lists():
            matched = False
            for pattern in patterns:
                for path in self._topPath.glob(pattern):
                    if path == targetPath:
                        matched = True
                        break
            if matched:
                yield description, patterns
                break

# Jim had hoped to code the patterns_matched_by method without having to glob
# out every pattern. It didn't work, see following transcript.
#
#     $ python3
#     Python 3.7.2 (v3.7.2:9a3ffc0492, Dec 24 2018, 02:44:43) 
#     [Clang 6.0 (clang-600.0.57)] on darwin
#     Type "help", "copyright", "credits" or "license" for more information.
#     >>> from pathlib import Path
#     >>> ori = Path('baseKotlin/src/main/res/values/styles.xml')
#     >>> ori
#     PosixPath('baseKotlin/src/main/res/values/styles.xml')
#     >>> ori.match('styles.xml')
#     True
#     >>> ori.match('*styles.xml')
#     True
#     >>> ori.match('**/styles.xml')
#     True
#     >>> ori.match('base*/**/styles.xml')
#     False

def diff_each(paths, pathLeft=None):
    linesLeft = None
    if pathLeft is not None:
        linesLeft = tuple(read_diff_lines(pathLeft))

    for path in paths:
        lines = tuple(read_diff_lines(path))

        if pathLeft is None:
            pathLeft = path
            linesLeft = lines
            continue
        
        if path == pathLeft:
            continue
        
        differences = [diff for diff in context_diff(
            linesLeft, lines, fromfile=str(pathLeft), tofile=str(path)
        )]
        
        yield pathLeft, path, differences if len(differences) > 0 else None

packageRE = re.compile(r'^\s*package\s+(?P<package>[^\s;]+)')

def read_diff_lines(path):
    with path.open() as file:
        for line in file.readlines():
            match = packageRE.match(line)
            if match is not None:
                line = "".join((
                    line[: match.start('package')],
                    "<package.name.deleted>",
                    line[ match.end('package') :]
                ))
            
            yield line

class NoticesState(enum.Enum):
    EXEMPT = enum.auto()
    NEEDED = enum.auto()
    CORRECT_DATE = enum.auto()
    INCORRECT_DATE = enum.auto()

class NoticesEditor:
    _leader_suffixes_map = {
        "#": ['.gitignore', '.pro', '.properties', '.py'],
        "//": ['.gradle', '.java', '.kt']
    }

    _exempt_suffixes = [
        '.png', # Exempt because it's a binary format.
        '.md'   # Exempt here because Markdown files have a different copyright
                # notice.
    ]


    @classmethod
    def exemptPath(cls, path):
        return (cls._effective_suffix(Path(path)) in cls._exempt_suffixes)

    _custom_suffixes_map = {'.xml': 'xml_editor'}

    # Regular expression for a copyright year in a notice. Has a capture group
    # for the year digits.
    _copyrightYear = re.compile(r'copyright\s+(\d{1,4})', re.IGNORECASE)
    
    def __init__(self, noticesPath, date):
        with Path(noticesPath).open() as noticesFile: self._noticesLines = [
            date.strftime(line.strip()) for line in noticesFile.readlines()]
        self._noticesXML = "\n".join((
            "<!--", *["    " + line for line in self._noticesLines], "-->\n"))

    def check(self, path):
        path = Path(path)
        if self.exemptPath(path):
            return NoticesState.EXEMPT, None

        linesFound = 0
        dateMatch = NoticesState.CORRECT_DATE
        with path.open('r') as file:
            for line in file:
                try:
                    needle = self._noticesLines[linesFound]
                    if line.find(needle) >= 0:
                        linesFound += 1
                        continue

                    if self._copyrightYear.search(needle) is None:
                        continue
                    if self._copyrightYear.search(line) is None:
                        continue

                    # By now, the lines both match the copyright year pattern
                    # but don't match each other. Conclusion is that the year is
                    # different.
                    dateMatch = NoticesState.INCORRECT_DATE
                    linesFound += 1

                except IndexError:
                    # By now, linesFound is one more than the index of the last
                    # line in the notices. That happens to be the same as the
                    # number of lines in the notices.
                    return dateMatch, linesFound

        # Read the whole file and didn't find the notice. Conclusion is that
        # it's missing.
        return NoticesState.NEEDED, linesFound

    def update(self, originalPath):
        editedPath = None
        originalPath = Path(originalPath)

        linesFound = 0
        linesRequired = len(self._noticesLines)
        with self._editing_file(originalPath) as editedFile, \
            originalPath.open('rt') as originalFile \
        :
            editedPath = Path(editedFile.name)
            for line in originalFile:
                if linesFound >= linesRequired:
                    editedFile.write(line)
                    continue

                needle = self._noticesLines[linesFound]
                if line.find(needle) >= 0:
                    editedFile.write(line)
                    linesFound += 1
                    continue

                needleMatch = self._copyrightYear.search(needle)
                lineMatch = (
                    None if needleMatch is None
                    else self._copyrightYear.search(line))

                if lineMatch is None:
                    editedFile.write(line)
                    continue

                # By now, the lines both match the copyright year pattern but
                # don't match each other. Conclusion is that the year is
                # different.

                # Write the part of the original line before the year ...
                editedFile.write(line[:lineMatch.start(1)])
                #
                # ... The year from the notice ...
                editedFile.write(needleMatch.group(1))
                #
                # ... The part of the original line after the year.
                editedFile.write(line[lineMatch.end(1):])
                linesFound += 1

        if linesFound != linesRequired:
            raise RuntimeError(
                'Notice update failed to find required number of lines.'
                , f'Required:{linesRequired}. Found:{linesFound}.'
                , f'Path "{originalPath}"')

        return editedPath, self._editor_differences(originalPath, editedPath)

    @staticmethod
    def _effective_suffix(path):
        if path.suffix == "" and path.name.startswith("."):
            return path.name
        return path.suffix

    def insert(self, originalPath):
        editedPath = None
        originalPath = Path(originalPath)
        suffix = self._effective_suffix(originalPath)

        try:
            customEditorName = self._custom_suffixes_map[suffix]
            # Next line sets customEditor to the bound method.
            customEditor = getattr(self, customEditorName)
        except KeyError:
            customEditor = None

        for key, suffixList in self._leader_suffixes_map.items():
            if suffix in suffixList:
                commentLead = key
                break

        with self._editing_file(originalPath) as editedFile, \
            originalPath.open('rt') as originalFile \
        :
            editedPath = Path(editedFile.name)
            if customEditor is None:
                self._leader_editor(commentLead, originalFile, editedFile)
            else:
                customEditor(originalFile, editedFile)

        return editedPath, self._editor_differences(originalPath, editedPath)
    
    @staticmethod
    def _editing_file(originalPath):
        return NamedTemporaryFile(
            mode='wt',
            delete=False,
            prefix=originalPath.stem + '_' ,
            suffix=originalPath.suffix
        )

    # Simple editor that inserts the notices at the start of the file.
    #
    # Each notice line is prefixed by a specified leader and a space.  
    # If the first line of the original file wasn't a blank line, then a blank
    # line is inserted after the notice lines.  
    # Then append the rest of the original file.
    def _leader_editor(self, commentLead, originalFile, editedFile):
        for line in self._noticesLines:
            editedFile.write(commentLead)
            editedFile.write(" ")
            editedFile.write(line)
            editedFile.write("\n")

        for index, line in enumerate(originalFile):
            if index == 0 and line.strip() != "":
                editedFile.write("\n")
            editedFile.write(line)
    
    def _editor_differences(self, originalPath, editedPath):
        with originalPath.open() as file: originalLines = file.readlines()
        with editedPath.open() as file: editedLines = file.readlines()

        return [diff for diff in context_diff(
            originalLines, editedLines,
            fromfile=str(originalPath), tofile="Edited"
        )]

    # Custom editor for XML files.
    #
    # If the first line is an XML declaration, put the notices XML comment after
    # it. Otherwise, put the notices first. Then append the rest of the file
    # unchanged.
    #
    # Simple way to identify the XML declaration is that it starts `<?xml`.
    #
    # This code mightn't behave correctly if the xml file is empty. That
    # probably isn't valid XML anyway.
    def xml_editor(self, originalFile, editedFile):
        line = originalFile.readline()
        if line.startswith("<?xml"):
            editedFile.write(line)
            editedFile.write(self._noticesXML)
        else:
            editedFile.write(self._noticesXML)
            editedFile.write(line)
        line = originalFile.readline()
        while line != '':
            editedFile.write(line)
            line = originalFile.readline()

        # Following code would do something more fancy. It looks for the first
        # end tag, `>` character, and then inserts the notice XML comment after
        # it. The output wasn't as clean, and is different to what Jim did in
        # another Open Source repository, so it's commented out.
        #
        # inserted = False
        # while line != '':
        #     if inserted:
        #         editedFile.write(line)
        #     else:
        #         partition = line.partition('>')
        #         editedFile.write(partition[0])
        #         editedFile.write(partition[1])
        #         if partition[1] != '':
        #             if partition[0].endswith("?"):
        #                 editedFile.write("\n")
        #             editedFile.write("\n".join((
        #                 "<!--", *self._noticesLines, "-->"
        #             )))
        #             inserted = True
        #         editedFile.write(partition[2])
        #     line = originalFile.readline()

class DuplicatorJob:

    # Properties that are set by the CLI.
    #
    @property
    def counting(self):
        return self._counting
    @counting.setter
    def counting(self, counting):
        self._counting = counting

    @property
    def find(self):
        return self._find
    @find.setter
    def find(self, find):
        self._find = find

    @property
    def gitModified(self):
        return self._gitModified
    @gitModified.setter
    def gitModified(self, gitModified):
        self._gitModified = gitModified

    @property
    def insertNotices(self):
        return self._insertNotices
    @insertNotices.setter
    def insertNotices(self, insertNotices):
        self._insertNotices = insertNotices

    @property
    def notices(self):
        return self._notices
    @notices.setter
    def notices(self, notices):
        self._notices = notices
        self._noticesPath = Path(notices)

    @property
    def originals(self):
        return self._originals
    @originals.setter
    def originals(self, originals):
        self._originals = originals

    @property
    def top(self):
        return self._top
    @top.setter
    def top(self, top):
        self._top = top
        self._topPath = Path(top)

    @property
    def verbose(self):
        return self._verbose
    @verbose.setter
    def verbose(self, verbose):
        self._verbose = verbose

    @property
    def yes(self):
        return self._yes
    @yes.setter
    def yes(self, yes):
        self._yes = yes
    #
    # End of CLI propperties.

    def __call__(self):
        self._rules = Rules(self._topPath)
        self._noticesEditors = {} if self.insertNotices else None

        if self.find:
            return self._find_business()

        originalsChecked = 0
        edited = 0
        for original in self._specified_originals():
            originalsChecked += 1
            if self.insertNotices:
                edited += self._notices_business(Path(original))
            else:
                self.process_original(original)

        if originalsChecked == 0:
            if self.insertNotices:
                printDot =(
                    len(self.originals) == 0
                    and self.counting
                    and not self.verbose
                )
                for path in self.git_ls_files():
                    originalsChecked += 1
                    editedNow = self._notices_business(path)
                    edited += editedNow
                    if printDot:
                        print(
                            "." if editedNow == 0 else "+" * editedNow,
                            end='', flush=True)
                if printDot:
                    print(tuple(self._noticesEditors.keys()))
            else:
                self.diff_all()

        if self.insertNotices:
            print(f"Checked:{originalsChecked}. Edited:{edited}.")
        return 0
    
    def _find_business(self):
        modifiedPaths = tuple(path for path in self.git_ls_files('--modified'))
        modifiedNames = frozenset(
            modifiedPath.name for modifiedPath in modifiedPaths)
        for modifiedName in modifiedNames:
            print(modifiedName)
            for path in self.git_ls_files(modifiedName, f'*/{modifiedName}'):
                status = 'M' if path in modifiedPaths else ''
                print(f'{status:<2}{path}')
            print()
        return 0
    
    def _specified_originals(self):
        if self.gitModified:
            yielded = None
            for original in self.git_ls_files('--modified'):
                if yielded is None:
                    yielded = 0
                if len(self.originals) <= 0 or original in self.originals:
                    yielded += 1
                    yield original
            if yielded is None:
                raise RuntimeError(
                    "Command line switch -m or --modified given but"
                    " no files are modified in Git.")
            if yielded <= 0:
                raise RuntimeError(
                    "None of the specified original files are modified in Git.")
        else:
            yield from self.originals
    
    def _notices_business(self, path):
        if path.is_dir():
            return 0
        
        if NoticesEditor.exemptPath(path):
            noticesEditor = None
            state = NoticesState.EXEMPT
        else:
            modifiedDate = self.git_modified_date(path)
            try:
                noticesEditor = self._noticesEditors[modifiedDate.year]
            except KeyError:
                noticesEditor = NoticesEditor(self._noticesPath, modifiedDate)
                self._noticesEditors[modifiedDate.year] = noticesEditor
            state, linesFound = noticesEditor.check(path)

        change = None
        if state is NoticesState.CORRECT_DATE:
            if self.verbose:
                print(f'Notice lines {linesFound} in "{path}"')
        elif state is NoticesState.EXEMPT:
            if self.verbose:
                print(
                    f'No notice insertion for suffix "{path.suffix}",'
                    f' skipping "{path}"')
        elif state is NoticesState.INCORRECT_DATE:
            change = f'Copyright notices with incorrect date in file "{path}"'
        elif state is NoticesState.NEEDED:
            change =f'No copyright notices in file "{path}"'
        else:
            raise NotImplementedError(f'Unknown NoticesState {state}.')

        if change is None:
            return 0
        if self.verbose or not self.counting:
            print(change)
        if self.counting:
            return 1

        if state is NoticesState.INCORRECT_DATE:
            editedPath, differences = noticesEditor.update(path)
        elif state is NoticesState.NEEDED:
            editedPath, differences = noticesEditor.insert(path)

        overwritten = self._ask_overwrite(editedPath, path, differences)

        if overwritten:
            state, linesFound = noticesEditor.check(path)
            if state:
                if self.verbose:
                    print('Overwritten file OK.')
            else:
                raise RuntimeError("Overwritten file doesn't have notices.")

        return 1 if overwritten else 0

    def git_ls_files(self, *switches):
        # See: https://git-scm.com/docs/git-ls-files  
        # -z switch specifies null-terminators instead of newlines, and verbatim
        # file names for unprintable values.
        with subprocess.Popen(
            ('git', 'ls-files', '-z', *switches)
            ,stdout=subprocess.PIPE, text=True, cwd=self.top
        ) as gitProcess:
            with gitProcess.stdout as gitOutput:
                name = []
                while True:
                    readChar = gitOutput.read(1)
                    if readChar == "":
                        return
                    if readChar == "\x00":
                        yield Path(self.top, ''.join(name))
                        name = []
                    else:
                        name.append(readChar)

    def git_modified_date(self, path):
        # Get the date of the last commit.
        with subprocess.Popen((
            'git', 'log', '--max-count=1', '--pretty=format:%cd'
            , '--date=format:%Y %m %d', path
        ), stdout=subprocess.PIPE, text=True, cwd=self.top) as gitProcess:
            with gitProcess.stdout as gitOutput:
                # The line will be year, month, day separated by spaces. Put
                # them into a tuple, then spread the tuple into the date()
                # constructor.
                components = tuple(
                    int(piece) for piece in gitOutput.readline().split())
                return datetime.date(*components)

    def process_original(self, original):
        originalPath = Path(original)
        matchCount = 0
        for description, patterns in self._rules.patterns_matched_by(
            originalPath
        ):
            matchCount += 1
            print(f'{original}\nMatches {description}:')
            try:
                for pathLeft, path, differences in diff_each(
                    glob_each(self._topPath, patterns), originalPath
                ):
                    self._ask_overwrite(pathLeft, path, differences)
            except ValueError as error:
                print(str(error))

        if matchCount <= 0:
            print(f'{original}\nNo matches.')
    
    def _ask_overwrite(self, pathSource, pathDestination, differences):
        if differences is None:
            print(f'    Same "{pathDestination}"')
            return None

        print(f'    Different "{pathDestination}"')
        if self.verbose:
            print(''.join(differences))

        while True:
            response = (
                "yes" if self.yes else input('    Overwrite? (Y/n/?)').lower())
            if response == "" or response.startswith("y"):
                print('Overwriting.')
                shutil.copy(pathSource, pathDestination)
                return True
            elif response.startswith("n"):
                print('Keeping')
                return False
            elif response == "?":
                print(''.join(differences))
            else:
                print(f'Unrecognised "{response}". Ctrl-C to quit.')
    
    def diff_all(self, report=sys.stdout):
        for description, paths in self._rules.named_globs():
            pathsFound = None
            pathDifferences = []
            lineDifferences = []
            try:
                for pathLeft, path, differences in diff_each(paths):
                    if pathsFound is None:
                        pathsFound = [pathLeft]
                    pathsFound.append(path)
                    if differences is not None:
                        pathDifferences.append(str(path))
                        lineDifferences.extend(differences)
            except ValueError as error:
                # ToDo sort this out a bit better. Right now, the code gets here
                # if a sub-pattern matches zero or one items. That's different
                # to a "main" pattern matching zero or one items.
                report.write(str(error))
                report.write("\n")
                continue

            pathCount = 0 if pathsFound is None else len(pathsFound)
            if pathCount <= 0:
                report.write(f'Failed, no match for {description}.\n')
            elif pathCount < 2:
                report.write(
                    f'Failed, only one match for {description}:'
                    f' "{pathsFound}"\n')
            elif len(pathDifferences) > 0:
                report.write(f'Differences {description} "{pathsFound[0]}"\n')
                if self.verbose:
                    for difference in lineDifferences:
                        report.write("    " + difference)
                else:
                    for difference in pathDifferences:
                        report.write(f'    "{difference}"\n')
            else:
                report.write(f'OK {pathCount:>2} matches for {description}.\n')

def main(commandLine):
    argumentParser = argparse.ArgumentParser(
        formatter_class=argparse.RawDescriptionHelpFormatter,
        description=textwrap.dedent(__doc__))
    defaultNotices = "copyrightnotices.txt"
    argumentParser.add_argument(
        '-c', '--count', dest='counting', action='store_true', help=
        "Count the number of files that require notice insertion but don't"
        " edit any.")
    argumentParser.add_argument(
        '-f', '--find', action='store_true', help=
        "Find mode: List path and Git status of all files with the same name"
        " as a modified file.")
    argumentParser.add_argument(
        '-i', '--insert-notices', dest='insertNotices', action='store_true'
        , help=
        "Notice insertion mode: Insert notices into files that are in Git but"
        " don't have the notices.")
    argumentParser.add_argument(
        '-m', '--modified', dest='gitModified', action='store_true', help=
        "Check files that are modified according to Git. If no originals are"
        " specified, then check all files that are modified. Otherwise, filter"
        " the list of originals to include only modified files. Ignored in"
        " notice insertion mode.")
    argumentParser.add_argument(
        '-n', '--notices', default=defaultNotices, type=str, help=
        f'Path to the notices file. Default: "{defaultNotices}"')
    argumentParser.add_argument(
        '--top', type=str, default=str(Path(__file__).parent), help=
        "Top of the source directory tree."
        " Default is the directory that contains this script.")
    argumentParser.add_argument(
        '-v', '--verbose', action='store_true', help="Verbose output.")
    argumentParser.add_argument(
        '--yes', action='store_true', help=
        "Overwrite without prompting. Overridden by --count.")
    argumentParser.add_argument(
        metavar='original', type=str, nargs='*', dest='originals', help=
        "Path of a file to check. Default is to check everything in the"
        " directory tree, or everything in Git.")

    return argumentParser.parse_args(commandLine[1:], DuplicatorJob())()

if __name__ == '__main__':
    sys.exit(main(sys.argv))

# ToDo: Mode in which it checks that everything in Git is covered by a rule.
