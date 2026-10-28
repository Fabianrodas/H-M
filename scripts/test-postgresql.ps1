$ErrorActionPreference = 'Stop'

$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$targetRoot = [System.IO.Path]::GetFullPath((Join-Path $projectRoot 'target'))
$postgresBin = 'C:\Program Files\PostgreSQL\18\bin'
$initdb = Join-Path $postgresBin 'initdb.exe'
$pgCtl = Join-Path $postgresBin 'pg_ctl.exe'
$createdb = Join-Path $postgresBin 'createdb.exe'
$psql = Join-Path $postgresBin 'psql.exe'

foreach ($binary in @($initdb, $pgCtl, $createdb, $psql)) {
    if (-not (Test-Path -LiteralPath $binary -PathType Leaf)) {
        throw "PostgreSQL 18 utility was not found: $binary"
    }
}

New-Item -ItemType Directory -Force -Path $targetRoot | Out-Null
$runId = [guid]::NewGuid().ToString('N')
$runRoot = Join-Path $targetRoot "hm-postgres-test-$runId"
$dataRoot = Join-Path $runRoot 'data'
$passwordFile = Join-Path $runRoot 'init-password.txt'
$envFile = Join-Path $runRoot 'test.env'
$logFile = Join-Path $runRoot 'postgres.log'
$databasePassword = 'hm-test-only-password'
$portListener = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Loopback, 0)
$portListener.Start()
$port = ([System.Net.IPEndPoint] $portListener.LocalEndpoint).Port
$portListener.Stop()
$serverStarted = $false
$previousHmEnv = [System.Environment]::GetEnvironmentVariable('HM_ENV_FILE', 'Process')
$previousPgPassword = [System.Environment]::GetEnvironmentVariable('PGPASSWORD', 'Process')
$mavenExitCode = 1

try {
    New-Item -ItemType Directory -Path $runRoot | Out-Null
    [System.IO.File]::WriteAllText($passwordFile, $databasePassword, [System.Text.Encoding]::ASCII)
    [System.IO.File]::WriteAllText(
            $envFile,
            "HM_DB_HOST=127.0.0.1`nHM_DB_PORT=$port`nHM_DB_NAME=hm-db`nHM_DB_USER=hm_test`nHM_DB_PASSWORD=$databasePassword`nADMIN_EMAIL=admin@example.test`n",
            [System.Text.UTF8Encoding]::new($false))

    & $initdb -D $dataRoot -U hm_test -A scram-sha-256 --pwfile=$passwordFile --encoding=UTF8 --no-locale
    if ($LASTEXITCODE -ne 0) {
        throw "initdb exited with code $LASTEXITCODE."
    }

    & $pgCtl -D $dataRoot -o "-h 127.0.0.1 -p $port" -l $logFile -w start
    if ($LASTEXITCODE -ne 0) {
        throw "pg_ctl start exited with code $LASTEXITCODE."
    }
    $serverStarted = $true
    $env:PGPASSWORD = $databasePassword

    & $createdb -h 127.0.0.1 -p $port -U hm_test hm-db
    if ($LASTEXITCODE -ne 0) {
        throw "createdb exited with code $LASTEXITCODE."
    }

    $sqlFiles = @(
        (Join-Path $projectRoot 'database\postgresql\schema.sql'),
        (Join-Path $projectRoot 'database\postgresql\seed.sql'),
        (Join-Path $projectRoot 'database\postgresql\sync-identities.sql'))
    $migrationDirectory = Join-Path $projectRoot 'database\postgresql\migrations'
    $migrationFiles = @(Get-ChildItem -LiteralPath $migrationDirectory -Filter '*.sql' -File |
        Sort-Object -Property Name |
        Select-Object -ExpandProperty FullName)

    foreach ($sqlFile in $sqlFiles) {
        & $psql -X -v ON_ERROR_STOP=1 -h 127.0.0.1 -p $port -U hm_test -d hm-db -f $sqlFile
        if ($LASTEXITCODE -ne 0) {
            throw "PostgreSQL import failed for $([System.IO.Path]::GetFileName($sqlFile)) (exit $LASTEXITCODE)."
        }
    }

    $rowCounts = & $psql -X -A -t -h 127.0.0.1 -p $port -U hm_test -d hm-db -c `
        "SELECT (SELECT COUNT(*) FROM categoria) || '|' || (SELECT COUNT(*) FROM cliente) || '|' || (SELECT COUNT(*) FROM genero) || '|' || (SELECT COUNT(*) FROM producto) || '|' || (SELECT COUNT(*) FROM subcategoria) || '|' || (SELECT COUNT(*) FROM subsub)"
    if ($LASTEXITCODE -ne 0 -or $rowCounts.Trim() -ne '4|1|3|120|13|40') {
        throw "Unexpected PostgreSQL seed row counts: $($rowCounts -join '')."
    }

    $mixedCaseColumnCount = & $psql -X -A -t -h 127.0.0.1 -p $port -U hm_test -d hm-db -c `
        "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema='public' AND column_name ~ '[A-Z]'"
    if ($LASTEXITCODE -ne 0 -or $mixedCaseColumnCount.Trim() -ne '24') {
        throw "Expected 24 case-sensitive imported columns; found $($mixedCaseColumnCount -join '')."
    }

    $identityColumnCount = & $psql -X -A -t -h 127.0.0.1 -p $port -U hm_test -d hm-db -c `
        "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema='public' AND is_identity='YES'"
    if ($LASTEXITCODE -ne 0 -or $identityColumnCount.Trim() -ne '10') {
        throw "Expected ten PostgreSQL identity columns; found $($identityColumnCount -join '')."
    }
    Write-Output 'Verified PostgreSQL seed row counts, quoted imported column names, and ten identity columns.'

    foreach ($sqlFile in $migrationFiles) {
        & $psql -X -v ON_ERROR_STOP=1 -h 127.0.0.1 -p $port -U hm_test -d hm-db -f $sqlFile
        if ($LASTEXITCODE -ne 0) {
            throw "PostgreSQL migration failed for $([System.IO.Path]::GetFileName($sqlFile)) (exit $LASTEXITCODE)."
        }
    }

    $env:HM_ENV_FILE = $envFile
    & (Join-Path $PSScriptRoot 'verify-postgresql-seed.ps1')
    if ($LASTEXITCODE -ne 0) {
        throw "PostgreSQL seed verification exited with code $LASTEXITCODE."
    }

    & mvn -B -ntp test
    $mavenExitCode = $LASTEXITCODE
} finally {
    if ($serverStarted) {
        & $pgCtl -D $dataRoot -m fast -w stop
        if ($LASTEXITCODE -ne 0) {
            Write-Warning 'PostgreSQL test server did not stop cleanly; the temporary files were retained.'
        }
    }

    if ($null -eq $previousHmEnv) {
        Remove-Item Env:HM_ENV_FILE -ErrorAction SilentlyContinue
    } else {
        $env:HM_ENV_FILE = $previousHmEnv
    }
    if ($null -eq $previousPgPassword) {
        Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue
    } else {
        $env:PGPASSWORD = $previousPgPassword
    }

    $resolvedRunRoot = [System.IO.Path]::GetFullPath($runRoot)
    $resolvedTargetRoot = [System.IO.Path]::GetFullPath($targetRoot)
    if ([System.IO.Directory]::Exists($resolvedRunRoot)) {
        $parent = [System.IO.Directory]::GetParent($resolvedRunRoot).FullName
        $safeName = [System.IO.Path]::GetFileName($resolvedRunRoot) -match '^hm-postgres-test-[0-9a-f]{32}$'
        if ($parent -ne $resolvedTargetRoot -or -not $safeName) {
            throw 'Refusing to remove a PostgreSQL test folder outside its exact target subdirectory.'
        }
        if (-not $serverStarted -or $LASTEXITCODE -eq 0) {
            Remove-Item -LiteralPath $resolvedRunRoot -Recurse -Force
        }
    }
}

if ($mavenExitCode -ne 0) {
    exit $mavenExitCode
}
