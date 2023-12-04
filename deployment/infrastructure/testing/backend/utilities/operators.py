# Set log levels
from shutil import copyfile, move
import shutil
from os.path import isfile, join
from os import listdir
import os
import sys
import json
import time

LOGLEVELS = {"NONE": 0, "CRITICAL": 1, "EXCEPTION": 2,
             "ERROR": 3, "WARNING": 4, "INFO": 5, "STATS": 6, "DEBUG": 7}
LOGLEVEL = LOGLEVELS["STATS"]


from datetime import datetime, timezone

"""
Class that defines operations in files, directories, clases, ...
"""
class op:
    @staticmethod
    def json_string_to_object(json_string):
        data = json.loads(json_string)
        return data

    @staticmethod
    def to_json(source_object,indent=0,ensure_ascii=True):
        return json.dumps(obj=source_object,indent=indent,ensure_ascii=ensure_ascii)
    
    @staticmethod
    def to_json_file(source_object,json_file_path,file_open_mode="w",indent=2):
        tmp_json_string=op.to_json(source_object=source_object,indent=indent)
        op.writeToFile(data=tmp_json_string, file_path=json_file_path,open_mode=file_open_mode, end="")
        
    @staticmethod
    def read_json_file(filePath,encoding="utf-8"):
        data=None
        f = open(filePath,"r",encoding=encoding)
        data = json.load(f)
        f.close()
        
        return data  
      
    @staticmethod
    def timestamp(zone=timezone.utc, string=False):
        timestamp = datetime.timestamp(datetime.now(zone))
        if (string):
            return str(timestamp)
        
        return timestamp
    
    @staticmethod
    def get_datetime(zone=timezone.utc, string=False):
        date = datetime.now(zone)
        if (string):
            return str(date)
        
        return date
    
    @staticmethod
    def startLog(logFile=None):
        if(logFile == None):
            logFile = f"./log/generator-{op.timestamp()}.log"
        openMessage = "Starting Server Log Messages..."
        op.writeToFile(data=openMessage, filePath=logFile,
                       openMode="w+", end="\n")

    @staticmethod
    def value_empty(obj):
        return "" if obj == None or obj == "" else obj

    # Method to set value none by default
    @staticmethod
    def value_none(obj):
        return None if obj == None or obj == "" else obj

    # Print log Operation
    @staticmethod
    def print_log(messageStr, logType="DEBUG", e=None):
        global LOGLEVEL, LOGLEVELS, LOGFILE
        if(LOGFILE == None):
            op.startLog()
        # If the log level requested is lower than the actual log level
        if LOGLEVEL < LOGLEVELS[logType]:
            return None

        # Add the log description and exception if not empty
        logInfo = "["+logType+"]:"
        if(e != None):
            logInfo += "[" + str(e) + "]."

        # Print the log
        logData = " ".join([logInfo, messageStr])
        op.writeToFile(data=logData, filePath=LOGFILE, openMode="a+", end="\n")
        return print(logData)

    # Init dynamically the new class
    @ staticmethod
    def create_class(newClass, *args, **kwargs):
        # Get slices
        slices = newClass.split('.')

        # Count the number of slices
        lenSlices = len(slices)

        # Check if the number of slices
        if lenSlices == 1:
            slices.append(slices[0])

        # Import Class
        importedClass = __import__(".".join(slices[:-1]))

        # For every component add attributes
        for component in slices[1:]:
            importedClass = getattr(importedClass, component)

        # Create the class
        return importedClass(*args, **kwargs)

    @ staticmethod
    def path_exists(pathName):
        pathFile = os.path.exists(pathName)
        return pathFile

    @ staticmethod
    def make_dir(nameDir, permits=0o777):
        if op.path_exists(nameDir):
            return True
        os.makedirs(nameDir, permits)
        return True
    
    @ staticmethod
    def delete_dir(nameDir):
        if not op.path_exists(nameDir):
            return False
        
        shutil.rmtree(nameDir)
    @ staticmethod
    def copy_file(src, dst):
        return copyfile(src, dst)

    @ staticmethod
    def move_file(src, dst):
        return move(src, dst)

    @ staticmethod
    def to_string(inputFile, openmode="r", encoding=sys.stdout.encoding):
        str = open(inputFile, openmode, encoding=encoding).read()
        return str

    @ staticmethod
    def delete_file(filePath):
        if not op.path_exists(filePath):
            return None

        os.remove(filePath)
        return True

    @ staticmethod
    def get_path_without_file(filePath):
        return os.path.dirname(filePath)

    @ staticmethod
    def write_to_file(data, filePath, openMode="r", end=""):
        if(data == "" or data == None):
            return None

        data = data + end
        path = op.getPathWithoutFile(filePath)

        if path == None or not op.path_exists(path):
            op.makeDir(path)

        file = open(filePath,
                    openMode, encoding=sys.stdout.encoding)
        file.write(data)
        file.close()
        return True
    
    @ staticmethod
    def wait(seconds):
        return time.sleep(seconds)
    
    @staticmethod
    def get_attribute(sourceObject,attrPath,defaultValue=None,pathSep="."):
        tmpRet=defaultValue
        if sourceObject == None:
            return tmpRet
    
        if pathSep == None or pathSep == "":
            return tmpRet
    
        tmpParts=attrPath.split(pathSep)
        if tmpParts == None or tmpParts == "":
            return tmpRet
        for part in tmpParts:
            if not part in sourceObject:
                return tmpRet
            sourceObject=sourceObject[part]
        tmpRet=sourceObject
        return tmpRet
