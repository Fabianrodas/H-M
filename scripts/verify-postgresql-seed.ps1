param(
    [string] $DumpPath = (Join-Path $PSScriptRoot '..\dump.sql'),
    [string] $SeedPath = (Join-Path $PSScriptRoot '..\database\postgresql\seed.sql')
)

$pattern = '^INSERT INTO\s+\x60?([A-Za-z_][A-Za-z0-9_]*)\x60?(.*)$'

function Get-NormalizedInsertStatements([string] $Path) {
    $lines = @(Get-Content -LiteralPath $Path | Where-Object { $_ -cmatch '^INSERT INTO\b' })
    return @($lines | ForEach-Object {
        $match = [regex]::Match($_, $pattern)
        if (-not $match.Success) {
            throw "Unexpected INSERT statement format in $Path."
        }
        'INSERT INTO ' + $match.Groups[1].Value.ToLowerInvariant() + $match.Groups[2].Value
    })
}

$original = @(Get-NormalizedInsertStatements -Path $DumpPath)
$postgres = @(Get-NormalizedInsertStatements -Path $SeedPath)

if ($original.Count -ne 6) {
    throw "Expected six original INSERT statements; found $($original.Count)."
}
if ($postgres.Count -ne $original.Count) {
    throw "Expected $($original.Count) PostgreSQL INSERT statements; found $($postgres.Count)."
}

for ($index = 0; $index -lt $original.Count; $index++) {
    if ($original[$index] -cne $postgres[$index]) {
        throw "PostgreSQL seed statement $($index + 1) differs from the original dump."
    }
}

& (Join-Path $PSScriptRoot 'verify-dump-inserts.ps1') -DumpPath $DumpPath
Write-Output "Verified all $($postgres.Count) PostgreSQL seed statements preserve the original values and order."
