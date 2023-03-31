/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package utils;

import utils.exceptions.UtilException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public final class FileUtil {
    private FileUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }
    public static String toFile(String filePath, String content, Boolean append) throws IOException {
        FileUtil.createFile(filePath);
        try(
            FileWriter fw = new FileWriter(filePath,append)
        ){
            fw.write(content);
        }
        catch(Exception ioe)
        {
            throw new UtilException(FileUtil.class, ioe, "It was not possible to create file ["+filePath+"]");
        }
        return filePath;
    }
    public static File newFile(String filePath){
        return new File(filePath);
    }

    public static String getWorkdirPath(){
        return Paths.get(System.getProperty("user.dir")).toString();
    }

    public static String getBaseClassDir(Class selectedClass){
        return FileUtil.normalizePath(selectedClass.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    public static String createFile(String filePath){
        try {
            File myObj = new File(filePath);
            myObj.getParentFile().mkdirs();
            if (myObj.createNewFile()) {
               LogUtil.printMessage("File created in path [" + filePath + "]");
            }
            return myObj.getPath();
        } catch (Exception e) {
            throw new UtilException(FileUtil.class,"It was not possible to create new file at ["+filePath+"], " + e.getMessage()) ;
        }
    }
    public static String getResourceAsString(InputStream fileContent){
        InputStreamReader fileContentReader =  new InputStreamReader(
                fileContent,
                StandardCharsets.UTF_8);
        StringBuilder text = new StringBuilder();
        try (Reader reader = new BufferedReader(
                fileContentReader)) {

            int c;
            while ((c = reader.read()) >= 0) {
                text.append(c);
            }
        } catch (Exception e) {
            throw new UtilException(FileUtil.class,"It was not possible to read resource, " + e.getMessage()) ;
        }

        return text.toString();
    }

    public static InputStream getResourceContent(Class selectedClass, String resourcePath){
        try {
            return selectedClass.getClassLoader().getResourceAsStream(resourcePath);
        }catch (Exception e) {
            throw new UtilException(FileUtil.class,"[ERROR] Something when wrong when reading file in path [" + resourcePath + "], " + e.getMessage());
        }
    }
    public static String getResourcePath(Class selectedClass, String resourcePath){
        try {
            return FileUtil.normalizePath(selectedClass.getClassLoader().getResource(resourcePath).getPath());
        }catch (Exception e) {
            throw new UtilException(FileUtil.class,"[ERROR] Something when wrong when reading file in path [" + resourcePath + "], " + e.getMessage());
        }
    }

    public static String createDataDir(String name){
        String workDir = FileUtil.getWorkdirPath();
        String path = Paths.get(workDir ,"data" , name).toAbsolutePath().toString();
        return FileUtil.createDir(path);
    }
    public static String createTmpDir(String name){
        String workDir = FileUtil.getWorkdirPath();
        String path = Paths.get(workDir ,"tmp" , name).toAbsolutePath().toString();
        return FileUtil.createDir(path);
    }

    public static String getTmpDir(String dirName){
        try {
            return Files.createTempDirectory(dirName).toFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createDir(String dirPath){
        try {
            return Files.createDirectories(Path.of(dirPath)).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create dir [" + dirPath + "]");
        }
    }
    public static String readFile(String path){

            try {
                if(!FileUtil.pathExists(path)) {
                    LogUtil.printError("The file does not exists in [" + path + "]!");
                    return null;
                }
                return new String(Files.readAllBytes(Paths.get(path)));
            } catch (Exception e) {
                throw new UtilException(FileUtil.class, "It was not possible to read file in [" + path + "]");
            }

    }

    public static String getRootPath(){
        try {
            return System.getProperty("user.dir");
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "It was not possible to get root path");
        }
    }
    public static String readFile(Path path){

        try {
            if(!FileUtil.pathExists(path)) {
                LogUtil.printError("The file does not exists in path [" + path.toString() + "]!");
                return null;
            }
            return new String(Files.readAllBytes(path));
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "It was not possible to read file in path [" + path + "]");
        }

    }
    public static String createDir(Path dirPath){
        try {
            return Files.createDirectories(dirPath).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create dir [" + dirPath + "]");
        }
    }
    public static String createSubDir(String dirPath, String subDirName){
        try {
            if(!FileUtil.pathExists(dirPath)){
                throw new UtilException(FileUtil.class, "Path " + dirPath + " does not exist! Can't create subdir: " + subDirName);
            }
            return Files.createDirectories(Path.of(dirPath)).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create subdir [" + subDirName + "] in [" + dirPath + "]");
        }
    }
    public static String getTmpFile(String fileName, String extension){
        try {
            return Files.createTempFile(fileName, extension).getFileName().toAbsolutePath().toString();
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "Could not create temporary file ["+fileName+"."+extension+"]! "+ e.getMessage());
        }
    }

    public static Boolean pathExists(String path){
        return Files.exists(Path.of(path));
    }
    public static Boolean fileExists(File file){
        return file.exists();
    }
    public static Boolean pathExists(Path path){
        return Files.exists(path);
    }
    public static String normalizePath(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class,"[ERROR] It was not possible to normalize path ["+path+"]");
        }
    }
    public static String getClassFile(Class selectedClass){
        return selectedClass.getName().replace(".", "/") + ".java";
    }
    public static String getClassPackageDir(Class selectedClass){
        return selectedClass.getPackageName().replace(".", "/");
    }
}
