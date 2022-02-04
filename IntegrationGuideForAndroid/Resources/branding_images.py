# Copyright 2022 VMware, Inc.
# SPDX-License-Identifier: BSD-2-Clause

# Run with Python 3.7
"""\
Script to generate a set of branding images for upload to a VMware Workspace ONE
unified endpoint manager (UEM).
"""
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
# Module for getting the command line and returning a status.
# https://docs.python.org/3/library/sys.html
import sys
#
# Module for text dedentation.
# Only used for --help description.
# https://docs.python.org/3/library/textwrap.html
import textwrap
#
# PIP Module imports, in alphabetic order.
#
# Python Image Library fork.
# https://pillow.readthedocs.io/
# https://pillow.readthedocs.io/en/stable/reference/ImageColor.html
# https://pillow.readthedocs.io/en/stable/reference/ImageDraw.html
from PIL import Image, ImageColor, ImageDraw, ImageFont

textListsInput = """\
Background
Image
Extra Large

Background
Image
Large

Background
Image
Medium

Background
Image
Small

Company
Logo
Phone

Company
Logo
Phone
High Res

Company
Logo
Tablet

Company
Logo
Tablet
High
Resolution
"""

class BrandingImages:

    # Properties for command line switches.

    @property
    def backgroundColour(self):
        return self._backgroundColour
    @backgroundColour.setter
    def backgroundColour(self, backgroundColour):
        self._backgroundColour = backgroundColour
        self._backgroundColourRGB = ImageColor.getrgb(backgroundColour)

    @property
    def textColour(self):
        return self._textColour
    @textColour.setter
    def textColour(self, textColour):
        self._textColour = textColour
        self._textColourRGB = ImageColor.getrgb(textColour)

    @property
    def brandName(self):
        return self._brandName
    @brandName.setter
    def brandName(self, brandName):
        self._brandName = brandName

    @property
    def fontFile(self):
        return self._fontFile
    @fontFile.setter
    def fontFile(self, fontFile):
        self._fontFile = fontFile

    @property
    def imageFormat(self):
        return self._imageFormat
    @imageFormat.setter
    def imageFormat(self, imageFormat):
        self._imageFormat = imageFormat
        self._imageSuffix = "." + imageFormat.lower()

    @property
    def height(self):
        return self._height
    @height.setter
    def height(self, height):
        self._height = height

    @property
    def margin(self):
        return self._margin
    @margin.setter
    def margin(self, margin):
        self._margin = margin

    @property
    def width(self):
        return self._width
    @width.setter
    def width(self, width):
        self._width = width

    # End of command line switch properties.

    def textLists(self):
        lines = None
        for line in self.textListsInput.splitlines():
            if line == "":
                yield lines
                lines = None
            else:
                if lines is None:
                    lines = []
                lines.append(line)
        if lines is not None:
            yield lines

    def __init__(self):
        self.textListsInput = textListsInput
        self._scaleSize = 20

    def __call__(self):
        self._scaleFont = (
            None if self.fontFile is None
            else ImageFont.truetype(self.fontFile, size=self._scaleSize))

        for textList in self.textLists():
            print(self._branding_image(textList))

        return True

    def _branding_image(self, textList):
        text = "\n".join((self.brandName,) + tuple(textList))

        # Create a new image in memory and a drawing handle.
        image = Image.new(
            'RGBA', (self.width, self.height), color=self._backgroundColourRGB)
        draw = ImageDraw.Draw(image)

        # Determine the font size necessary to fill the image, as follows.
        #
        # 1.  Draw the text at a known size.
        # 2.  Determine the scaling necessary for the text to fill the width or
        #     height of the image, leaving a margin.
        # 3.  Select whichever scaling is smaller.
        # 4.  Multiply the initial known size by the selected scaling.
        #
        # TOTH https://stackoverflow.com/a/59104505
        scaleWidth, scaleHeight = draw.textsize(text, font=self._scaleFont)
        heightScale = (image.height - self.margin) / scaleHeight
        widthScale = (image.width - self.margin) / scaleWidth
        drawSize = int(
            self._scaleSize
            * (widthScale if widthScale < heightScale else heightScale)
        )
        drawFont = (
            None if self.fontFile is None
            else ImageFont.truetype(self.fontFile, size=drawSize))
        drawWidth, drawHeight = draw.textsize(text, font=drawFont)

        # Position the text in the centre of the image.
        draw.multiline_text(
            ((image.width - drawWidth) / 2, (image.height - drawHeight) / 2),
            text, font=drawFont, fill=self._textColourRGB, align="center")

        # Generate the file name from the brand name concatenated with all the 
        # image texts but without spaces.
        filename = Path("".join(text.split())).with_suffix(self._imageSuffix)

        # Write the image to the file and return the path.
        image.save(filename)
        return filename

def main(commandLine):
    argumentParser = argparse.ArgumentParser(
        formatter_class=argparse.RawDescriptionHelpFormatter,
        description=textwrap.dedent(__doc__))
    argumentParser.add_argument(
        '-b', '--backgroundColour', default="gray", type=str, help=
        'Colour for the background, default is gray.')
    argumentParser.add_argument(
        '-c', '--textColour', default="#fffeb0", type=str, help=
        'Colour for the text, default is a pale yellow.')
    argumentParser.add_argument(
        '-f', '--fontFile', type=str, help=
        'Path to an OTF or TTF font file. Default is to use a default font.')
    argumentParser.add_argument(
        '--imageFormat', type=str, default="PNG", help=
        'Image format, default is PNG.')
    argumentParser.add_argument(
        '-n', '--brandName', type=str, default="enterprise", help=
        'Brand name to appear in the images, default is "enterprise".')
    argumentParser.add_argument(
        '-H', '--height', type=int, default=600, help=
        'Image height in pixels, default is 600.')
    argumentParser.add_argument(
        '-W', '--width', type=int, default=600, help=
        'Image width in pixels, default is 600')
    argumentParser.add_argument(
        '-M', '--margin', type=int, default=20, help=
        'The text will be scaled to fill the image, leaving a margin of the'
        ' specified width in pixels. Default is 20.')
    return (
        0 if argumentParser.parse_args(commandLine[1:], BrandingImages())()
        else 1)

if __name__ == '__main__':
    sys.exit(main(sys.argv))
