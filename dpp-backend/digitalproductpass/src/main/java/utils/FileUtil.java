/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
 *
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

import org.springframework.stereotype.Service;
import utils.exceptions.UtilException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;


/**
 * This class consists exclusively of methods to operate on Files and Folders.
 *
 * <p> The methods defined here are intended to create, get, check, read and delete folders and files.
 *
 */
@Service
public final class FileUtil {

    /**
     * Writes the given {@code String} content into the file specified by the filePath with optional appending.
     * <p>
     * @param   filePath
     *          the path to the target file as a String.
     * @param   content
     *          the content to write on the file as a String.
     * @param   append
     *          if true, then data will be written to the end of the file rather than the beginning.
     *
     * @return  a {@code String} filePath to the file.
     *
     * @throws  UtilException
     *          if unable to write to the file.
     */
    synchronized public String toFile(String filePath, String content, Boolean append) throws IOException {
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

    /**
     * Creates a new File instance.
     * <p>
     * @param   filePath
     *          the path to the target file as a String.
     *
     * @return  an empty {@code File} object with the given file path.
     *
     */
    public File newFile(String filePath){
        return new File(filePath);
    }

    /**
     * Gets the user's work directory.
     * <p>
     *
     * @return  a {@code String} path of the user's work directory.
     *
     */
    public static String getWorkdirPath(){
        return Paths.get(System.getProperty("user.dir")).toString();
    }

    /**
     * Gets the given Class base directory.
     * <p>
     * @param   selectedClass
     *          the Class type to get the base directory from.
     *
     * @return  a {@code String} path of the class base directory.
     *
     */
    @SuppressWarnings("Unused")
    public String getBaseClassDir(Class selectedClass){
        return this.normalizePath(selectedClass.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    /**
     * Creates a new File.
     * <p>
     * @param   filePath
     *          the path to the target file as a String.
     *
     * @return  a {@code String} filePath to the file.
     *
     * @throws  UtilException
     *          if unable to create the file.
     */
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

    /**
     * Parses the content of the given InputStream to a String.
     * <p>
     * @param   fileContent
     *          the {@code InputStream} representing the file content.
     *
     * @return  a {@code String} object with input stream content.
     *
     * @throws  UtilException
     *          if unable to read the input stream.
     */
    @SuppressWarnings("Unused")
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

    /**
     * Parses a given resource to an InputStream.
     * <p>
     * @param   selectedClass
     *          the Class type of the resource.
     * @param   resourcePath
     *          the resource name.
     *
     * @return  a {@code InputStream} object for reading the resource.
     *
     * @throws  UtilException
     *          if unable to read the resource name.
     */
    @SuppressWarnings("Unused")
    public InputStream getResourceContent(Class selectedClass, String resourcePath){
        try {
            return selectedClass.getClassLoader().getResourceAsStream(resourcePath);
        }catch (Exception e) {
            throw new UtilException(FileUtil.class,"[ERROR] Something when wrong when reading file in path [" + resourcePath + "], " + e.getMessage());
        }
    }

    /**
     * Gets the path of a given Resource.
     * <p>
     * @param   selectedClass
     *          the Class type of the resource.
     * @param   resourcePath
     *          the resource name.
     *
     * @return  a {@code String} object with the resource's path.
     *
     * @throws  UtilException
     *          if unable to read the resource name.
     */
    @SuppressWarnings("Unused")
    public String getResourcePath(Class selectedClass, String resourcePath){
        try {
            return this.normalizePath(selectedClass.getClassLoader().getResource(resourcePath).getPath());
        }catch (Exception e) {
            throw new UtilException(FileUtil.class,"[ERROR] Something when wrong when reading file in path [" + resourcePath + "], " + e.getMessage());
        }
    }

    /**
     * Gets the path of the data directory of the application.
     * <p>
     *
     * @return  a {@code String} path of the app's data directory.
     *
     */
    public String getDataDir(){
        String workDir = this.getWorkdirPath();
        return Paths.get(workDir ,"data").toAbsolutePath().toString();
    }

    /**
     * Gets the path of the tmp directory of the application.
     * <p>
     *
     * @return  a {@code String} path of the app's tmp directory.
     *
     */
    public String getTmpDir(){
        String workDir = this.getWorkdirPath();
        return Paths.get(workDir ,"tmp").toAbsolutePath().toString();
    }

    /**
     * Creates a directory in the data directory of the application.
     * <p>
     * @param   name
     *          the name of the new directory.
     *
     * @return  a {@code String} path of the created directory.
     *
     */
    public String createDataDir(String name){
        String workDir = this.getWorkdirPath();
        String path = Paths.get(workDir ,"data" , name).toAbsolutePath().toString();
        return this.createDir(path);
    }

    /**
     * Creates a directory in the tmp directory of the application.
     * <p>
     * @param   name
     *          the name of the new directory.
     *
     * @return  a {@code String} path of the created directory.
     *
     */
    @SuppressWarnings("Unused")
    public String createTmpDir(String name){
        String workDir = this.getWorkdirPath();
        String path = Paths.get(workDir ,"tmp" , name).toAbsolutePath().toString();
        return this.createDir(path);
    }

    /**
     * Creates a temporary directory in the work directory of the application.
     * <p>
     * @param   dirName
     *          the name of the temporary directory.
     *
     * @return  a {@code String} path of the created directory.
     *
     * @throws  UtilException
     *          if unable to create the directory.
     */
    @SuppressWarnings("Unused")
    public  String createTempDir(String dirName){
        try {
            return Files.createTempDirectory(dirName).toFile().getAbsolutePath();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create dir [" + dirName + "]");
        }
    }
    /**
     * Moves a file from a directory to another directory
     * <p>
     * @param   fromPath
     *          source file path directory
     * @param   toPath
     *          target file path directory
     *
     * @return  a {@code String} path of the target file in the directory
     *
     * @throws  UtilException
     *          if unable to create the directory.
     */
    public String moveFile(String fromPath, String toPath){
        try {
            return Files.move(Paths.get(fromPath), Paths.get(toPath), StandardCopyOption.REPLACE_EXISTING).toFile().getAbsolutePath();
        }catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to move file from [" + fromPath + "] to [" + toPath + "]");
        }
    }

    /**
     * Creates a directory in the given path and creates all the non-existent directories in the path.
     * <p>
     * @param   dirPath
     *          the complete path to the new directory as a String.
     *
     * @return  a {@code String} path of the created directory.
     *
     * @throws  UtilException
     *          if unable to create the directory.
     */
    public  String createDir(String dirPath){
        try {
            return Files.createDirectories(Path.of(dirPath)).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create dir [" + dirPath + "]");
        }
    }

    /**
     * Reads a file from the given path, if exists.
     * <p>
     * @param   path
     *          the path to the file as a String.
     *
     * @return  a {@code String} with the content of the file.
     *
     * @throws  UtilException
     *          if unable to read the file.
     */
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

    /**
     * Deletes a directory from the given path, if exists.
     * <p>
     * @param   path
     *          the path to the intended directory as a String.
     *
     * @throws  UtilException
     *          if unable to delete the directory.
     */
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

    /**
     * Deletes a file from the given path, if exists.
     * <p>
     * @param   path
     *          the path to the intended file as a String.
     *
     * @throws  UtilException
     *          if unable to delete the file.
     */
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

    /**
     * Gets the root path of the user's working directory of the application.
     * <p>
     * @return  a {@code String} with the root path.
     *
     * @throws  UtilException
     *          if unable to get the root path.
     */
    @SuppressWarnings("Unused")
    public  String getRootPath(){
        try {
            return System.getProperty("user.dir");
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "It was not possible to get root path");
        }
    }

    /**
     * Reads a file from the given path, if exists.
     * <p>
     * @param   path
     *          the path to the file as a Path.
     *
     * @return  a {@code String} with the content of the file.
     *
     * @throws  UtilException
     *          if unable to read the file.
     */
    @SuppressWarnings("Unused")
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

    /**
     * Creates a directory in the given path and creates all the non-existent directories in the path.
     * <p>
     * @param   dirPath
     *          the complete path to the new directory as a Path.
     *
     * @return  a {@code String} path of the created directory.
     *
     * @throws  UtilException
     *          if unable to create the directory.
     */
    @SuppressWarnings("Unused")
    public  String createDir(Path dirPath){
        try {
            return Files.createDirectories(dirPath).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class, "It was not possible to create dir [" + dirPath + "]");
        }
    }

    /**
     * Creates a subdirectory in the given path and creates all the non-existent directories in the path.
     * <p>
     * @param   dirPath
     *          the complete path to the new subdirectory as a String.
     * @param   subDirName
     *          the new subdirectory name.
     *
     * @return  a {@code String} path of the created subdirectory.
     *
     * @throws  UtilException
     *          if unable to create the subdirectory.
     */
    @SuppressWarnings("Unused")
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

    /**
     * Creates a temporary file in the work directory of the application.
     * <p>
     * @param   fileName
     *          the name of the temporary file.
     * @param   extension
     *          the extension of the file as a String (e.g: ".json", ".txt", etc.).
     *
     * @return  a {@code String} path of the new file created.
     *
     * @throws  UtilException
     *          if unable to create the temporary file.
     */
    @SuppressWarnings("Unused")
    public  String createTmpFile(String fileName, String extension){
        try {
            return Files.createTempFile(fileName, extension).getFileName().toAbsolutePath().toString();
        } catch (Exception e) {
            throw new UtilException(FileUtil.class, "Could not create temporary file ["+fileName+"."+extension+"]! "+ e.getMessage());
        }
    }

    /**
     * Checks if a path exists.
     * <p>
     * @param   path
     *          the path to check as a String.
     *
     * @return  true if the path exists, false otherwise.
     *
     */
    public  Boolean pathExists(String path){
        return Files.exists(Path.of(path));
    }

    /**
     * Checks if a path exists.
     * <p>
     * @param   file
     *          the {@code File} instance.
     *
     * @return  true if the file exists, false otherwise.
     *
     */
    @SuppressWarnings("Unused")
    public  Boolean fileExists(File file){
        return file.exists();
    }

    /**
     * Checks if a path exists.
     * <p>
     * @param   path
     *          the path to check as a Path.
     *
     * @return  true if the path exists, false otherwise.
     *
     */
    public  Boolean pathExists(Path path){
        return Files.exists(path);
    }

    /**
     * Normalizes the given path to its canonical path.
     * <p>
     * @param   path
     *          the path to normalize as a String.
     *
     * @return  the {@code String} canonical path string denoting the same file or directory.
     *
     * @throws  UtilException
     *          if unable to normalize the path.
     */
    public  String normalizePath(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            throw new UtilException(FileUtil.class,"[ERROR] It was not possible to normalize path ["+path+"]");
        }
    }

    /**
     * Gets the Java file of the given Class type.
     * <p>
     * @param   selectedClass
     *          the Class type to get the Java file from.
     *
     * @return  the {@code String} Java file name of the given Class type (the ".java" file).
     *
     */
    @SuppressWarnings("Unused")
    public  String getClassFile(Class selectedClass){
        return selectedClass.getName().replace(".", "/") + ".java";
    }

    /**
     * Gets the Package directory of the given Class type.
     * <p>
     * @param   selectedClass
     *          the Class type to get the Java file from.
     *
     * @return  the {@code String} package directory of the given Class type.
     *
     */
    @SuppressWarnings("Unused")
    public  String getClassPackageDir(Class selectedClass){
        return selectedClass.getPackageName().replace(".", "/");
    }
}
