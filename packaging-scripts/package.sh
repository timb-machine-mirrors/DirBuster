#!/bin/sh
#
# usage ./package.sh <verison>
#scripts to package dirbuster up for relase
#
mkdir DirBuster-$1
mkdir DirBuster-$1/lib

cd DirBuster-$1

#create two start scripts
echo -e "#!/bin/bash\njava -Xmx256M -jar DirBuster-$1.jar\n" > DirBuster-$1.sh
echo -e "java -Xmx256M -jar DirBuster-$1.jar\n" > DirBuster-$1.bat

chmod +x DirBuster-$1.sh

#Copy the core files over
cp ../../dist/DirBuster.jar DirBuster-$1.jar
cp -R ../../dist/lib/ lib/ 
cp ../../lists/* .

cd ../

#produce both tar.bz2 and zip versions

tar -cvjf DirBuster-$1.tar.bz2 DirBuster-$1
zip -r DirBuster-$1.zip DirBuster-$1/*

#build the src files

mkdir DirBuster-$1-src

cp -R ../src/*  DirBuster-$1-src

cd DirBuster-$1-src

# ensure that all CVS files are removed
find . -name CVS -exec rm -rf '{}' \;

cd ../
tar -cvjf DirBuster-$1-src.tar.bz2 DirBuster-$1-src
