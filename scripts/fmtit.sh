#!/bin/bash

function fmt_cpp()
{
    SRC_FILE=$1
    
    echo cpp: $SRC_FILE
    
    /cygdrive/c/apps/eclipse/eclipse.exe -application org.eclipse.jdt.core.JavaCodeFormatter -verbose \
            -config $(cygpath -w /cygdrive/c/Users/jgordon/.eclipse-workspace/helmtt/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.cdt.core.prefs) \
             $SRC_FILE
}

function fmt_java()
{
    SRC_FILE=$1
    
    echo java: $SRC_FILE
    
    /cygdrive/c/apps/eclipse/eclipse.exe -application org.eclipse.jdt.core.JavaCodeFormatter -verbose \
            -config $(cygpath -w /cygdrive/c/Users/jgordon/.eclipse-workspace/helmtt/.metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.jdt.core.prefs) \
             $SRC_FILE
}

function fmt_file()
{
    SRC_FILE=$1

    if [[ ! -f $SRC_FILE ]] ; then
        echo "ERROR: $SRC_FILE is not a file or does not exist!"
        exit -1    
    fi
     
    if [[ ${SRC_FILE: -4} == ".cpp" || ${SRC_FILE: -2} == ".c" || ${SRC_FILE: -2} == ".h" ]] ; then
        fmt_cpp $SRC_FILE
    elif [ ${SRC_FILE: -5} == ".java" ] ; then
        fmt_java $SRC_FILE
    fi
}

function fmt_files()
{
    FILE_LIST=$1
     
    if [[ -z "$FILE_LIST" ]] ; then
        echo "ERROR: No file list argument specified!"
        exit -1
    fi

    if [[ ! -f $FILE_LIST ]] ; then
        echo "ERROR: $FILE_LIST is not a file or does not exist!"
        exit -1    
    fi

    while read FILE_PATH; do
        if [[ ! -z "$FILE_PATH" ]] ; then
            fmt_file $FILE_PATH
        fi
    done <$FILE_LIST
}

if [[ "$#" -ne 1 && "$#" -ne 2 ]] ; then
    echo "ERROR: Illegal number of parameters"
    exit -1
elif [ "$#" -eq 1 ] ; then
    fmt_file $1
elif [[ "$#" -eq 2 && $1 = "-f" ]] ; then
    fmt_files $2
else
    echo "ERROR: Unable to parse arguments"
    exit -1
fi

