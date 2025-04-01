# MathOps Commons

This is a collection of useful utility classes and packages that could be considered extensions to the standard Java
Library. It contains the following subpackages

## collections

This package has primitive-valued collections (simple implementations, to avoid a dependency on a more robust/larger
library of such). At the moment, this package only contains an integer-valued map used for counting objects.

## file

A utility class to load the contents of a file as a byte array, a String, a list of String lines, a BufferedImage, or a
Properties. This package also has a generic file filter based on file extension.

## installation

Utility classes to manage an "installation", which is a base directory and a set of named subdirectories. This allows
code to be installed in multiple locations and to be able to locate the "current" installation's directories.

## log

A simple but capable logging system with a compact syntax for logging within code. Includes log file rotation and
retention control and supports logging (in the short term) to an in-memory list of log messages that can be printed
later.

## model

Classes to support generalized trees of nodes, where each node can have strongly-typed attributes, properties, and data.
This is designed to support many use cases from CSS and DOM to XML/HTML and general data structures. Attribute,
property, and data values can be stored in string formats and then parsed into strongly-typed values with codecs
associated with each attribute/property in the spirit of CSS property values.

## number

Some additional numeric types including Rational and BigRational (these track integer numerator and denominator, are
always in reduced form). This package includes arithmetic opertations on these types that preserve "rational" values
(so 1/3 can be represented exactly, for example). This package also includes an Irrational type that handles common use
cases in algebra and trigonometry (rational multiples of pi, e, or roots, like "5 root 3 over 2" or "5 pi over 6").

## res

This package supports internationalization of strings based on Locales, with a compact format in code to access those
strings.

## ui

Some simple utilities for user interfaces, including two useful Swing layout managers (StackedBorderLayout and
AlignedFlowLayout), utilities to perform common tasks, and a database of X11 color names with their corresponding AWT
colors.

## other classes

The commons library has some other useful utilities.

- A set of core constants that can be referenced throughout code
- A utility to list all classes in a directory/path
- A database of MIME types with associated file extensions
- A hex encoder/decoder
- A random number generator that allows much larger seed values than the built-in Random
- Utilities to parse and format dates and times

