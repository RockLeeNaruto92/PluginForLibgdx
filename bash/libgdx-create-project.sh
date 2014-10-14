#!/bin/bash
PROJECT_PARENT_FOLDER=/home/superman/commandLineUsing
PROJECT_PATTERN_FOLDER=$PROJECT_PARENT_FOLDER/pattern
PROJECT_NAME=MyLibgdxGame
PROJECT_MAIN_PACKAGE=your.company.package
PROJECT_MAIN_CLASS=LibgdxGame

PATTERN_GWT_XML_FILE=$PROJECT_PATTERN_FOLDER/PROJECT_NAME.gwt.xml
PATTERN_SRC_PATH=com/me/mygdxgame
PATTERN_MAIN_JAVA_FILE=$PROJECT_PATTERN_FOLDER/MyGdxGame.java
PATTERN_MAIN_PACKAGE_NAME=com.me.mygdxgame

LIBGDX_PATH=/home/superman/commandLineUsing/libgdx-nightly-latest
LIBGDX_GDX_SOURCES_JAR_FILE_PATH=$LIBGDX_PATH/sources/gdx-sources.jar
LIBGDX_GDX_JAR_FILE_PATH=$LIBGDX_PATH/gdx.jar

DESKTOP_EXTENSTION="-desktop"
ANDROID_EXTENSTION="-android"

####################################################
############### FUNCTION ###########################
# create project structure follow by packageName
# param
#   $1: package name
function createFolderStructure {
	packageName=$1
	
	numberOfPattern=$((`echo $packageName|sed 's/[^.]//g'|wc -m`-1))

	IFS=. read -a ARRAY <<< "$packageName" # one-line solution

	for i in `seq 0 $numberOfPattern`;
        do
		mkdir ${ARRAY[$i]}
		cd ${ARRAY[i]}
        done
}

# Get src path from package name
# param:
#   $1: package name
function getSrcPathFromPackage {
  packageName=$1
  
  numberOfPattern=$((`echo $packageName|sed 's/[^.]//g'|wc -m`-1))

  IFS=. read -a ARRAY <<< "$packageName" # one-line solution
  
  for i in `seq 0 $(($numberOfPattern - 1))`;
    do
      srcPath=$srcPath${ARRAY[$i]}/
    done
  srcPath=$srcPath${ARRAY[$numberOfPattern]}
  
  echo $srcPath
}

# create libgdx project's structure folder
# param
#  $1: PROJECT_FOLDER
#  $2: 
function createCoreProject {
	mkdir $1
	cd $1
	
	mkdir "bin"
	mkdir "libs"
	mkdir "src"

	#### Create bin folder ####
	cd bin
	# TODO
	# get src path from package name
	srcPath=$(getSrcPathFromPackage $2)
	createGwtXmlFile "$PROJECT_NAME.gwt.xml" $srcPath
	createFolderStructure $2
	
	#### Create libs folder ####
	cd $PROJECT_PARENT_FOLDER/$PROJECT_NAME/libs
	cp $LIBGDX_GDX_SOURCES_JAR_FILE_PATH ./
	cp $LIBGDX_GDX_JAR_FILE_PATH ./
	
	#### Create src folder ####
	cd $PROJECT_PARENT_FOLDER/$PROJECT_NAME/src
	cp $PROJECT_PARENT_FOLDER/$PROJECT_NAME/bin/$PROJECT_NAME.gwt.xml ./
	createFolderStructure $2
	createMainClassJavaFile $PROJECT_MAIN_CLASS $PROJECT_MAIN_PACKAGE 
}

# Function create project_name.gwt.xml fileContent
# Param:
#  $1: project_name.gwt.xml
#  $2: src path
function createGwtXmlFile {
  fileName=$1
  srcPath=$2

  fileContent=$(<$PATTERN_GWT_XML_FILE)
  fileContent="${fileContent/"$PATTERN_SRC_PATH"/$srcPath}"
  echo $fileContent > $fileName
}

# Create main class of project
# Param:
#   $1: class name
#   $2: package name
#   $3: src path
function createMainClassJavaFile {
  className=$1
  packageName=$2
  fileName=$className.java

  fileContent=$(<$PATTERN_MAIN_JAVA_FILE)
  fileContent=${fileContent/"$PATTERN_MAIN_PACKAGE_NAME"/$packageName}
  fileContent=${fileContent/"$PATTERN_MAIN_CLASS"/$className}
  echo $fileContent > $fileName
}
 
###############################################
############### MAIN  ########################

createCoreProject $PROJECT_NAME $PROJECT_MAIN_PACKAGE