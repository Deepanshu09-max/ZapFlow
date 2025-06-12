@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.

@REM Maven Wrapper launcher
@echo off
@setlocal enableextensions enabledelayedexpansion

if "%MAVEN_WRAPPER_DEBUG%" == "true" (
  echo Initializing Maven Wrapper with Java %JAVA_HOME%
)

set MAVEN_PROJECTBASEDIR=%~dp0

@REM Detect if we are running on Windows or Windows NT
if "%OS%" == "Windows_NT" goto WinNTStart

echo This batch file can only be run on Windows NT 4.0 or later.
exit /b 99

:WinNTStart
@REM Find Maven wrapper JAR
if exist "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" goto RUN
echo Maven wrapper not found at %MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
exit /b 1

:RUN
set MAVEN_WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar

@REM Find Java executable
if not "%JAVA_HOME%" == "" goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
"%JAVA_EXE%" %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% -cp "%MAVEN_WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
if ERRORLEVEL 1 goto fail

goto end

:fail
exit /b 1

:end
