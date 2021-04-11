;Based on NSIS example written by Joost Verburg

;copyed from Ferruh Mavituna 
;--------------------------------
;Installer	: Copies java and lists to selected install dir
;Uninstaller	: Uninstall all files and shortcuts in the install dir
;--------------------------------

!define JRE_VERSION "1.6"

!define TEMP $R0
!define TEMP2 $R1
!define VAL1 $R2
!define VAL2 $R3


!define DOWNLOAD_JRE_FLAG $8

Var MUI_TEMP
  Var STARTMENU_FOLDER


;--------------------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
;General

  ;Name and file
  Name "DirBuster"
  OutFile "DirBusterSetup.exe"
  BrandingText "DirBuster Installer"
  
  ;Default installation folder
  InstallDir "$PROGRAMFILES\DirBuster"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\DirBuster" ""

  ;Vista redirects $SMPROGRAMS to all users without this
  RequestExecutionLevel admin
  
;	Section
;		Call DetectJRE
;		Exch $R3	; Get return value from stack
;	        Pop $R3
;		StrCmp $R3 0 +3
;		Goto +3
;		; else
;			MessageBox MB_OK ".NET Framework 2.0 is not installed. Installer will continue but you may need to download .NET Framework 2.0 before run BSQL Hacker"
;			ExecShell "open" "http://www.microsoft.com/downloads/details.aspx?familyid=0856eacb-4362-4b0d-8edd-aab15c5e04f5&displaylang=en"
;	SectionEnd

;--------------------------------
;Variables



;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE "Lisence.txt"
  ;!insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  
  ;Start Menu Folder Page Configuration
  !define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
  !define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\DirBuster" 
  !define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"
  
  !insertmacro MUI_PAGE_STARTMENU Application $STARTMENU_FOLDER
  
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections


Section "DirBuster" SecDummy

  SetOutPath "$INSTDIR"
  SetOverwrite On
  
  ;Core Files
  File "DirBuster.bat"
  File "..\dist\DirBuster.jar"
  File "owasp.ico"
  
  ;the lists
  File "..\lists\*.txt"

  
  ;Copy the apis used
  SetOutPath "$INSTDIR\lib"
  File "..\dist\lib\*.jar"

  ;reset the output path
  SetOutPath "$INSTDIR"
      
  ;Store installation folder
  WriteRegStr HKCU "Software\DirBuster" "" $INSTDIR
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    
    ;Create shortcuts
    CreateDirectory "$SMPROGRAMS\$STARTMENU_FOLDER"
    
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\DirBuster.lnk" "$INSTDIR\DirBuster.bat" "DirBuster.jar" "$INSTDIR\owasp.ico" 0
    
    CreateShortCut "$SMPROGRAMS\$STARTMENU_FOLDER\Uninstall.lnk" "$INSTDIR\Uninstall.exe"
  
  !insertmacro MUI_STARTMENU_WRITE_END

SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecDummy ${LANG_ENGLISH} "Installing DirBuster"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecDummy} $(DESC_SecDummy)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END
 
;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ;Remove the files
  Delete "$INSTDIR\libs\*.*"
  RmDir /r "$INSTDIR\libs"
  Delete "$INSTDIR\*.*"
  RMDir /r "$INSTDIR"
  
  !insertmacro MUI_STARTMENU_GETFOLDER Application $MUI_TEMP
    
  Delete "$SMPROGRAMS\$MUI_TEMP\*.*"
  
  ;Delete empty start menu parent diretories
  StrCpy $MUI_TEMP "$SMPROGRAMS\$MUI_TEMP"
 
  startMenuDeleteLoop:
	ClearErrors
    RMDir $MUI_TEMP
    GetFullPathName $MUI_TEMP "$MUI_TEMP\.."
    
    IfErrors startMenuDeleteLoopDone
  
    StrCmp $MUI_TEMP $SMPROGRAMS startMenuDeleteLoopDone startMenuDeleteLoop
  startMenuDeleteLoopDone:

  DeleteRegKey /ifempty HKCU "Software\DirBuster"

SectionEnd
;--------------------------------
 

; Returns: 0 - JRE not found. -1 - JRE found but too old. Otherwise - Path to JAVA EXE

; DetectJRE. Version requested is on the stack.
; Returns (on stack)	"0" on failure (java too old or not installed), otherwise path to java interpreter
; Stack value will be overwritten!

;Function DetectJRE
;  Exch $0	; Get version requested
		; Now the previous value of $0 is on the stack, and the asked for version of JDK is in $0
  ;Push $1	; $1 = Java version string (ie 1.5.0)
  ;Push $2	; $2 = Javahome
  ;Push $3	; $3 and $4 are used for checking the major/minor version of java
  ;Push $4
  ;MessageBox MB_OK "Detecting JRE"
  ;ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ;MessageBox MB_OK "Read : $1"
  ;StrCmp $1 "" DetectTry2
  ;ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
  ;MessageBox MB_OK "Read 3: $2"
  ;StrCmp $2 "" DetectTry2
  ;Goto GetJRE

;DetectTry2:
  ;ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ;MessageBox MB_OK "Detect Read : $1"
  ;StrCmp $1 "" NoFound
  ;ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$1" "JavaHome"
  ;MessageBox MB_OK "Detect Read 3: $2"
  ;StrCmp $2 "" NoFound

;GetJRE:
; $0 = version requested. $1 = version found. $2 = javaHome
;  MessageBox MB_OK "Getting JRE"
;  IfFileExists "$2\bin\java.exe" 0 NoFound
;  StrCpy $3 $0 1			; Get major version. Example: $1 = 1.5.0, now $3 = 1
;  StrCpy $4 $1 1			; $3 = major version requested, $4 = major version found
;  MessageBox MB_OK "Want $3 , found $4"
;  IntCmp $4 $3 0 FoundOld FoundNew
;  StrCpy $3 $0 1 2
;  StrCpy $4 $1 1 2			; Same as above. $3 is minor version requested, $4 is minor version installed
;  MessageBox MB_OK "Want $3 , found $4"
;  IntCmp $4 $3 FoundNew FoundOld FoundNew

;NoFound:
;  MessageBox MB_OK "JRE not found"
;  Push "0"
;  Goto DetectJREEnd

;FoundOld:
;  MessageBox MB_OK "JRE too old: $3 is older than $4"
;;  Push ${TEMP2}
;  Push "-1"
;  Goto DetectJREEnd
;FoundNew:
;  MessageBox MB_OK "JRE is new: $3 is newer than $4"

;Push "$2\bin\java.exe"
;  Push "OK"
;  Return
;   Goto DetectJREEnd
;DetectJREEnd:
;	; Top of stack is return value, then r4,r3,r2,r1
	;Exch	; => r4,rv,r3,r2,r1,r0
	;Pop $4	; => rv,r3,r2,r1r,r0
	;Exch	; => r3,rv,r2,r1,r0
	;Pop $3	; => rv,r2,r1,r0
	;Exch 	; => r2,rv,r1,r0
	;Pop $2	; => rv,r1,r0
	;Exch	; => r1,rv,r0
	;Pop $1	; => rv,r0
	;Exch	; => r0,rv
	;Pop $0	; => rv
;FunctionEnd