#################################################################################
# Tractus-X - Digital Product Pass Verification Add-on
#
# Copyright (c) 2023, 2024 BMW AG
# Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
# Copyright (c) 2024 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache License, Version 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0.
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
# either express or implied. See the
# License for the specific language govern in permissions and limitations
# under the License.
#
# SPDX-License-Identifier: Apache-2.0
#################################################################################

# Set log levels
from shutil import copyfile, move
import shutil
import os
import sys
import json
import time


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
        op.write_to_file(data=tmp_json_string, filePath=json_file_path,openMode=file_open_mode, end="")
        
    @staticmethod
    def read_json_file(filePath,encoding="utf-8"):
        data=None
        f = open(filePath,"r",encoding=encoding)
        data = json.load(f)
        f.close()
        
        return data  

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
    @staticmethod
    def timestamp(zone=timezone.utc, string=False):
        timestamp = datetime.timestamp(datetime.now(zone))
        
        if (string):
            return str(timestamp)
        

        return timestamp
    @staticmethod
    def get_filedatetime(zone=timezone.utc):
        return datetime.now(zone).strftime("%Y%m%d_%H%M%S")
    
    @staticmethod
    def get_filedate(zone=timezone.utc):
        return datetime.now(zone).strftime("%Y%m%d")

    @ staticmethod
    def get_path_without_file(filePath):
        return os.path.dirname(filePath)

    @ staticmethod
    def write_to_file(data, filePath, openMode="r", end=""):
        if(data == "" or data == None):
            return None

        with open(filePath, openMode, encoding=sys.stdout.encoding) as file:
            file.write(data)
            file.write(end)
        
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
    
    @staticmethod
    def search_element_by_field(array, id, field="id"):
        return next((x for x in array if x[field] == id), None)
