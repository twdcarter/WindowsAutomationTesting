(systeminfo | Select-String 'Total Physical Memory:').ToString().Split(':')[1].Trim()