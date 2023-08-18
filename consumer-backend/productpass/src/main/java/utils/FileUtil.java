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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import utils.exceptions.UtilException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
public final class FileUtil {


    public String toFile(String filePath, String content, Boolean append) throws IOException {
        this.createFile(filePath);
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
    public File newFile(String filePath){
        return new File(filePath);
    }

    public static String getWorkdirPath(){
        return Paths.get(System.getProperty("user.dir")).toString();
    }

    public String getBaseClassDir(Class selectedClass){
        return this.normalizePath(selectedClass.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    public String createFile(String filePath){
        try {
            File myObj = new File(filePath);
            myObj.getParentFile().mkdirs();
            myObj.createNewFile();
            return myObj.getPath();
        } catch (Exception e) {
            throw new UtilException(FileUtil.class,"It was not possible to create new file at ["+filePath+"], " + e.getMessage()) ;
        }
    }
    public String getResourceAsString(InputStream fileContent){
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

    public InputStream getResourceContent(Class selectedClass, String resourcePath){
        try {
            return selectedClass.getClassLoader().getResourceAsStream(resourcePath);
        }catch (Exception e) {
            throw new UtilException(FileUtil.class,"[ERROR] Something when wrong when reading file in path [" + resourcePath + "], " + e.getMessage());
        }
    }
    public String getResourcePath(Class selectedClass, String resourcePath){
        try {
            return this.normalizePath(selectedClass.getClassLoader().getResource(resourcePath).getPath());
        }catch (Exception e) {
            throw new UtilException(FileUtil.class,"[ERROR] Something when wrong when reading file in path [" + resourcePath + "], " + e.getMessage());
        }
    }
    public String getDataDir(){
        String workDir = this.getWorkdirPath();
        return Paths.get(workDir ,"data").toAbsolutePath().toString();
    }
    public String getTmpDir(){
        String workDir = this.getWorkdirPath();
        return Paths.get(workDir ,"tmp").toAbsolutePath().toString();
    }
    public String createDataDir(String name){
        String workDir = this.getWorkdirPath();
        String path = Paths.get(workDir ,"data" , name).toAbsolutePath().toString();
        return this.createDir(path);
    }
    public String createTmpDir(String name){
        String workDir = this.getWorkdirPath();
        String path = Paths.get(workDir ,"tmp" , name).toAbsolutePath().toString();
        return this.createDir(path);
    }

    public  String createTempDir(String dirName){
        try {
            return Files.createTempDirectory(dirName).toFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  String createDir(String dirPath){
        try {
            return Files.createDirectories(Path.of(dirPath)).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create dir [" + dirPath + "]");
        }
    }
    public String readFile(String path){

            try {
                if(!this.pathExists(path)) {
                    LogUtil.printError("The file does not exists in [" + path + "]!");
                    return null;
                }
                return new String(Files.readAllBytes(Paths.get(path)));
            } catch (Exception e) {
                throw new UtilException(FileUtil.class, "It was not possible to read file in [" + path + "]");
            }

    }

    public void deleteDir(String path){
        try {
            Path dir = Path.of(path);
            if(!this.pathExists(path)) {
                LogUtil.printError("The file does not exists in [" + path + "]!");
            }
            try(Stream<Path> paths =  Files.walk(dir)){
                paths.sorted(Comparator.reverseOrder())
                .forEach(filePath -> {
                    try {
                        Files.delete(filePath);
                    } catch (IOException e) {
                        LogUtil.printError("It was not possible to delete the file [" + filePath + "] in dir [" + path + "]");
                    }
                });
            }catch (Exception e) {
                throw new UtilException(FileUtil.class, "It was not possible to delete dir [" + path + "] because the stream closed!");
            }
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "It was not possible to delete dir [" + path + "]");
        }
    }

    public Boolean deleteFile(String path){
        try {
            if(!this.pathExists(path)) {
                LogUtil.printError("The file does not exists in [" + path + "]!");
                return null;
            }
            return Files.deleteIfExists(Paths.get(path));
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "It was not possible to delete file [" + path + "]");
        }
    }

    public  String getRootPath(){
        try {
            return System.getProperty("user.dir");
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "It was not possible to get root path");
        }
    }
    public String readFile(Path path){

        try {
            if(!this.pathExists(path)) {
                LogUtil.printError("The file does not exists in path [" + path.toString() + "]!");
                return null;
            }
            return new String(Files.readAllBytes(path));
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "It was not possible to read file in path [" + path + "]");
        }

    }
    public  String createDir(Path dirPath){
        try {
            return Files.createDirectories(dirPath).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create dir [" + dirPath + "]");
        }
    }
    public  String createSubDir(String dirPath, String subDirName){
        try {
            if(!this.pathExists(dirPath)){
                throw new UtilException(FileUtil.class, "Path " + dirPath + " does not exist! Can't create subdir: " + subDirName);
            }
            return Files.createDirectories(Path.of(dirPath)).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create subdir [" + subDirName + "] in [" + dirPath + "]");
        }
    }
    public  String getTmpFile(String fileName, String extension){
        try {
            return Files.createTempFile(fileName, extension).getFileName().toAbsolutePath().toString();
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "Could not create temporary file ["+fileName+"."+extension+"]! "+ e.getMessage());
        }
    }

    public  Boolean pathExists(String path){
        return Files.exists(Path.of(path));
    }
    public  Boolean fileExists(File file){
        return file.exists();
    }
    public  Boolean pathExists(Path path){
        return Files.exists(path);
    }
    public  String normalizePath(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class,"[ERROR] It was not possible to normalize path ["+path+"]");
        }
    }
    public  String getClassFile(Class selectedClass){
        return selectedClass.getName().replace(".", "/") + ".java";
    }
    public  String getClassPackageDir(Class selectedClass){
        return selectedClass.getPackageName().replace(".", "/");
    }
}
