param(
    [string] $DumpPath = (Join-Path $PSScriptRoot '..\dump.sql')
)

$expectedHash = 'A6E739D9885CBEC22754FB0E02503010C3E2E7CC55B901A071B4F67D8819DA05'
$insertLines = Get-Content -LiteralPath $DumpPath | Where-Object { $_ -cmatch '^INSERT INTO\b' }
$canonicalText = [string]::Join("`n", $insertLines)
$bytes = [System.Text.UTF8Encoding]::new($false).GetBytes($canonicalText)
$sha = [System.Security.Cryptography.SHA256]::Create()
try {
    $actualHash = [Convert]::ToHexString($sha.ComputeHash($bytes))
} finally {
    $sha.Dispose()
}

if ($actualHash -ne $expectedHash) {
    throw "INSERT statements changed. Expected SHA-256 $expectedHash; got $actualHash."
}

Write-Output "Verified $($insertLines.Count) unchanged INSERT statements (SHA-256 $actualHash)."
