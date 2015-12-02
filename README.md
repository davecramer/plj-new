# plj-new

How to build PLJ version beta-0_1_0
Introduction
This howto will describe howto build PL-J, install into a PostgreSQL database server and write your first java UDFs. It won`t be short, so prepare some cookies and coffee.

Where you can find help:
Join our irc chanel: irc://codehaus.org/plj
User mailing list: user@plj.codehaus.org

My environemnt is:

Suse linux 9.1 operating system, with default packages.
WARNING! The HEAD version is for hackers only it is not guaranteed to work or even compile. Please use the latest tagged version from CVS.

Requirements
To build PL-J, you will need to get the following software:

gcc, make, binutils, automake, autoconf http://www.gnu.org/
PostgreSQL 7.4 or 8.0 (prefered) source code (the latest version of the branch) http://www.postgresql.org
JDK 1.4.2_04 http://java.sun.com
loom (1.0-RC3 or higher) http://loom.codehaus.org/
maven 1.0 http://maven.apache.org/start/download.html
Note for JDK 1.5 apply the following patch http://docs.codehaus.org/download/attachments/12950/java-1.5.diff

Other software dependencies are handled by maven, they will be automatically downloaded from the central repository, so stay online at build.


Building the C code
untar the PostgreSQL source code
build PostgreSQL and install with the settings you like
go to pl-j/csrc
run configure with --with-pgsourcetree=<your postgresql source> and --with-pgversion=<pgversion (eg 80)>. The configure script warns you that it will use the default modules, because you didn`t define anything else, ignore it.
run "make"
Installing the language handler function
Start PostgreSQL, and create a database.
now you have a file "plpgj.so" in your directory. Copy this file into the location you like, the PostgreSQL lib directory may be a good choice.
in the directory pl-j/etc/install/, you will find a file named install.sql.template. Make a copy of this file, or edit it directly, and replace the @INSTALL_LIBHOME@ string with the location you copied your plpgj.so file.
run "psql <yourdatabase> < <yourscript>"
run "psql <yourdatabase> < sqlj_schema.sql" this will create the "sqlj" schema, this will install the management UDF's and structures including the class repository.
run "psql <yourdatabase> < febe_config.sql" this will create the configuration table needed by some modules (actualy not only the febe).
run psql
execute "set search_path = sqlj;"
Build the java code
Well if you survived the C build, you already passed the worst.
To build the PL-J java container, you will need network access. Maven downloads all dependencies from the network, and these dependencies will be included in the deployable sar file, so at the end all you have to do is to drop in your file and use it.

Follow the instructions:

From the main distribution directory
Run 'maven jar:install' to install the base API to the local maven repository.
From the main distribution directory
Run 'maven -Dgoal=clean,jar:install multiproject:goal'. This will create the jar files of all PL-J subprojects and install them to the local maven repository.
Enter to the directory src/loom and run 'maven loom:sar', this will create a file 'pl-j.sar' in the directory 'target'.
Or if you don't want to build the java source on your own, download the deployable loom package: http://dist.codehaus.org/plj/sars/pl-j-beta-0.1.0.sar

Deploy and run java code
Install loom (see above), and copy the file 'pl-j.sar' to the directory 'apps'. Optionally, you can remove all the other sar files saving yourself from the loom demo applications, but they won't interfere, so no panic.
Now start the loom application server by runing 'bin/loom.sh start'.
And now you should have a runing PL-J server. That simple.

Configure whatever you want.
You may need to configure the communication or execution flow of the server. This is a long story, just a few points to start:

If you want to configure the server, the file 'apps/pl-j/SAR-INF/config.xml' is for you.
If you have a completely different idea of what your stored procedures should do, you can replace the application building blocks (you may need some help, so join irc, unless you are a really big hacker)
If you want to configure the way the RDBMS does access the PL-J server. the RDBMS table 'sqlj.plj_config' is yours.
Install test UDFs
The build process created a jar file called pl-j-tests-0.1.0.jar. You can install this jar with sqlj.install_jar like this:
sqlj.install_jar('/path/to/jar/plj-j-tests-0.1.0.jar','pl-j-tests-0.1.0',1);
The same way, you will need to install log4j version 1.2.8 for some of the tests.
start psql and run the script "test_install.sql"
Have fun!
Call the test UDF`s one by one.

Important notes
Since 0.0.5, the default configuration uses a JDBC classloader. This, by the default configuration loads classes from the table sqlj.plj_classes. If you wish to run java UDFs and triggers with other SQL users, you may need to grant read access to these users on sqlj.plj_classes.
Not all JDBC function is implemented.
