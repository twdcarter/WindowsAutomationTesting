$un = USERNAME_REPLACEME
$pw = ConvertTo-SecureString PASSWORD_REPLACEME -AsPlainText -Force
$global:Cred = New-Object System.Management.Automation.PSCredential ($un, $pw)