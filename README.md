glaciercmd - a simple tool to consume Amazon's Glacier AWS service
-------------------------------------------------------------------

**glaciercmd** is a simple tool to consume Amazon's Glacier AWS service, using the AWS REST API.

There are several modes of operation, all of which can be found in the synopsis below. Each mode has a different set of required and optional arguments, which can also be found in the synopsis. Help mode can be used in a context sensitive manner. For example, *-h* will show all the modes, and *-h LIST* will show help about the LIST mode.

All commands require proper authentication. This can be provided on the command line by using *-k -s -r* or by creating a configuration file and specifying it with *-c config-file*

**glaciercmd** is licensed under the GPLv3 license. For more information, see the *LICENSE* file.
This project uses libraries and routines which may have a different license. Refer to the included licenses in the source files and/or JAR files for more information.

BUILDING
--------
Building **glaciercmd** requires the following:

1. Oracle Java or OpenJDK >= 6
2. Apache Ant >= 1.8

Then you can simply call `ant dist` to create a *dist* folder with everything glaciercmd needs to run. You can also use `ant package-tar` to create a tarball

Alternatively, if using Ubuntu or Debian, you can try using the glaciercmd PPA at: https://launchpad.net/~lordgaav/+archive/glaciercmd

SYNOPSIS
--------
	
	glaciercmd [-c <FILE>] -d | -h <COMMAND> | -i | -j | -l <TYPE> | -m | -n | -o | -u | -v    [-k <KEY>]     [-r <REGION>] [-s <SECRET>]

**HELP**

	glaciercmd -h <COMMAND>

**VERSION**

	glaciercmd -v

**LIST**

	glaciercmd -l <TYPE> --vault <VAULT>

**UPLOAD**

	glaciercmd [--description <DESC>] --input <INPUT> -u --vault <VAULT>

**DOWNLOAD**

	glaciercmd --archive <ARCHIVE> -d --output <OUTPUT> --vault <VAULT>

**INITIATEDOWNLOAD**

	glaciercmd --archive <ARCHIVE> -m --vault <VAULT>

**INITIATEINVENTORY**

	glaciercmd -i --vault <VAULT>

**GETDOWNLOAD**

	glaciercmd --job-id <JOBID> -n --output <OUTPUT> --vault <VAULT>

**GETINVENTORY**

	glaciercmd -j --job-id <JOBID> --vault <VAULT>

**DELETEARCHIVE**

	glaciercmd --archive <ARCHIVE> -o --vault <VAULT>

OPTIONS
-------
**--archive** *ARCHIVE* 

Use this archive id

**-c** **--config** *FILE* 

Use a configuration file

**-d** **--download** 

Download file from a vault. Note: this command will block until the file has been downloaded, and will probably take multiple hours.

**--description** *DESC* 

Use this description. If omitted the name of the file is used.

**-h** **--help** *COMMAND* 

Show help and examples

**-i** **--init-inventory** 

Request an inventory from Glacier. This command returns a job id which can be fed to get-inventory.

**--input** *INPUT* 

Use this file as input

**-j** **--get-inventory** 

Retrieve an inventory from Glacier. If the inventory is not yet available, this method will block until it is.

**--job-id** *JOBID* 

Use this job id

**-k** **--key** *KEY* 

Amazon AWS user key

**-l** **--list** *TYPE* 

List Glacier objects (vault|archive)

**-m** **--init-download** 

Request a download from Glacier. This command returns a job id which can be fed to get-download.

**-n** **--get-download** 

Retrieve a download from Glacier. If the download is not yet available, this method will block until it is.

**-o** **--delete-archive** 

Delete an archive from a vault.

**--output** *OUTPUT* 

Use this file as output

**-r** **--region** *REGION* 

Amazon AWS region

**-s** **--secret** *SECRET* 

Amazon AWS user secret

**-u** **--upload** 

Upload file into a vault

**--vault** *VAULT* 

Select this vault

**-v** **--version** 

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

REGIONS
-------
**glaciercmd** supports the following AWS Glacier regions:

1. VIRGINIA - glacier.us-east-1.amazonaws.com
2. OREGON - glacier.us-west-2.amazonaws.com
3. CALIFORNIA - glacier.us-west-1.amazonaws.com
4. IRELAND - glacier.eu-west-1.amazonaws.com
5. TOKYO - glacier.ap-northeast-1.amazonaws.com

BUGS
----
No known bugs currently exist.

AUTHOR
------
Nick Douma (n.douma@nekoconeko.nl)

