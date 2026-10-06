param(
    [ValidateSet('build', 'verify', 'install')]
    [string]$Action = 'build',
    [string]$JdkHome = '',
    [string]$SdkHome = ''
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot

if (!$JdkHome) {
    $candidates = @(
        'D:\Android\Other AS Version\android-studio-2023.2.1.14-windows\android-studio\jbr',
        'D:\Android\Other AS Version\android-studio-2022.2.1.20-windows\android-studio\jbr',
        $env:JAVA_HOME,
        'D:\Android\Other AS Version\Android Studio Panda 2\jbr',
        "$env:ProgramFiles\Android\Android Studio\jbr"
    )
    foreach ($candidate in $candidates) {
        if (!$candidate -or !(Test-Path (Join-Path $candidate 'bin/java.exe'))) { continue }
        $releaseFile = Join-Path $candidate 'release'
        if (Test-Path $releaseFile) {
            $releaseText = Get-Content $releaseFile -Raw
            if ($releaseText -match 'JAVA_VERSION="(17|21)\.') { $JdkHome = $candidate; break }
        }
    }
}
if (!$JdkHome -or !(Test-Path (Join-Path $JdkHome 'bin/java.exe'))) {
    throw 'Pass a JDK 17 directory with -JdkHome. The system Java 8 cannot build this project.'
}

if (!$SdkHome) {
    $SdkHome = @($env:ANDROID_HOME, 'D:\Android\SDK', "$env:LOCALAPPDATA\Android\Sdk") |
        Where-Object { $_ -and (Test-Path (Join-Path $_ 'platforms')) } | Select-Object -First 1
}
if (!$SdkHome -or !(Test-Path (Join-Path $SdkHome 'platforms'))) {
    throw 'Pass the Android SDK directory with -SdkHome.'
}

$localProperties = Join-Path $projectRoot 'local.properties'
if (!(Test-Path $localProperties)) {
    Set-Content -LiteralPath $localProperties -Value ('sdk.dir=' + $SdkHome.Replace('\', '/')) -Encoding ascii
}

$previousJava = $env:JAVA_HOME
$previousAndroid = $env:ANDROID_HOME
Push-Location $projectRoot
try {
    $env:JAVA_HOME = $JdkHome
    $env:ANDROID_HOME = $SdkHome
    Write-Host "JDK: $JdkHome"
    Write-Host "SDK: $SdkHome"
    [string[]]$tasks = switch ($Action) {
        'verify' { @(':androidApp:assembleDebug', ':androidApp:lintDebug') }
        'install' { @(':androidApp:installDebug') }
        default { @(':androidApp:assembleDebug') }
    }
    & "$projectRoot\gradlew.bat" @tasks --console=plain
    if ($LASTEXITCODE -ne 0) { throw "Gradle failed: $LASTEXITCODE" }
    Write-Host "APK: $projectRoot\androidApp\build\outputs\apk\debug\androidApp-debug.apk"
} finally {
    $env:JAVA_HOME = $previousJava
    $env:ANDROID_HOME = $previousAndroid
    Pop-Location
}
