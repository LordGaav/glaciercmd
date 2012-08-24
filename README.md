glaciercmd - a simple tool to consume Amazon's Glacier AWS service
-------------------------------------------------------------------

**glaciercmd** is a simple tool to consume Amazon's Glacier AWS service, using the AWS REST API.

There are several modes of operation, all of which can be found in the synopsis below. Each mode has a different set of required and optional arguments, which can also be found in the synopsis. Help mode can be used in a context sensitive manner. For example, *-h* will show all the modes, and *-h LIST* will show help about the LIST mode.

All commands require proper authentication. This can be provided on the command line by using *-k -s -r* or by creating a configuration file and specifying it with *-c config-file*

BUILDING
--------
Building **glaciercmd** requires the following:

1. Oracle Java or OpenJDK >= 6
2. Apache Ant >= 1.8

Then you can simply call `ant dist` to create a *dist* folder with everything glaciercmd needs to run. You can also use `ant package-tar` to create a tarball

SYNOPSIS
--------
	
	glaciercmd [-c <FILE>] -h <COMMAND> | -l <TYPE> | -v [-k <KEY>]  [-r <REGION>] [-s <SECRET>]

**HELP**

	glaciercmd -h <COMMAND>

**VERSION**

	glaciercmd -v

**LIST**

	glaciercmd [-c <FILE>] [-k <KEY>] -l <TYPE> [-r <REGION>] [-s <SECRET>]

OPTIONS
-------
**-c** **--config** *FILE* 

Use a configuration file

**-h** **--help** *COMMAND* 

Show help and examples

**-k** **--key** *KEY* 

Amazon AWS user key

**-l** **--list** *TYPE* 

List Glacier objects (vault|archive)

**-r** **--region** *REGION* 

Amazon AWS region

**-s** **--secret** *SECRET* 

Amazon AWS user secret

**-v** **--version** *arg* 

Show version information

CONFIGURATION
-------------
All command line parameters can optionally be provided using a configuration file. Exception on this are the mode selectors. The configuration file uses a simple format, which is:

	option=value

*option* is the same as the long options which can be specified on the command line. For example, this is a valid configuration line:

	region=ireland

Configuration options are parsed in the following order: 

1. The *-c* option.
2. All options provided on the command line, in the order they are specified.

It is possible to override already specified configuration options by specifying them again. Duplicate options will take the value of the last one specified. An example configuration file can be found in the distribution package.

BUGS
----
No major known bugs exist at this time.

AUTHOR
------
Nick Douma (n.douma@nekoconeko.nl)

