import java.io.*;
import java.util.List;

public class PowerShellInvoker {

    Utils utils = new Utils();

    /**
     * Pings a given machine using powershell applet Test-Connection.
     *
     * @param machineIP - IP Address of machine being pinged.
     * @return - Returns a true or false depending on success of ping.
     * @throws IOException
     */
    public boolean pingMachine(String machineIP) throws IOException {
        boolean result = false;
        String command = "powershell.exe Test-Connection " + machineIP + " -quiet -count 1";

        Process powerShellProcess = Runtime.getRuntime().exec(command);
        powerShellProcess.getOutputStream().close();
        String line;

        BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
        while ((line = stdout.readLine()) !=null){
            if (line.contains("True")){result = true;}
        }

        return result;
    }

    /**
     * Tests a connection to a given machine by attempting to connect via powershell
     * and pinging back to the host machine.
     *
     * @param machineIP - IP Address of machine being pinged.
     * @return - Returns a true or false depending on success of the connection.
     * @throws IOException
     */
    public boolean testPSConnection(String machineIP) throws IOException {
        boolean result = false;
        String output = executePowershellScript(machineIP, utils.getPropertyValue("PS1.TestConnection"));
        if (output.contains("True")){result = true;}
        return result;
    }

    /**
     * Attempt to execute a given powershell script against a given target machine.
     *
     * @param targetMachine - The machine which the script will be executed against.
     * @param scriptLocation - The script which should be executed against the target machine.
     * @return - Return a string which contains any output the execution produced.
     * @throws IOException - IOException can happen if any of the files involved cannot be accessed.
     */
    public String executePowershellScript(String targetMachine, String scriptLocation) throws IOException {
        String result = "";
        File tempFile = new File("tempScript.ps1");
        String credentialsScript = System.getProperty("user.dir") + utils.getPropertyValue("PS1.SetupCredentials");
        String executionScript = System.getProperty("user.dir") + scriptLocation;

        //create temp ps1 file to execute later
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Could not create temporary ps1 file.");
            e.printStackTrace();
        }

        FileOutputStream outstream = new FileOutputStream(tempFile);

        try {
            BufferedReader reader;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream));

            //add credentials gathering to temp file
            reader = new BufferedReader(new FileReader(credentialsScript));
            String line = reader.readLine();
            while (line!= null) {
                if (line.contains("USERNAME_REPLACEME")){
                    line = line.replace("USERNAME_REPLACEME", utils.getPropertyValue("RemoteLoginUN"));
                } else if (line.contains("PASSWORD_REPLACEME")) {
                    line = line.replace("PASSWORD_REPLACEME", utils.getPropertyValue("RemoteLoginPW"));
                }
                writer.write(line);
                writer.newLine();
                line = reader.readLine();
            }
            reader.close();

            //add invoke-command which executes the provided script using credentials
            line = "Invoke-Command -FilePath " + executionScript + " -ComputerName " + targetMachine + " -Credential $Cred";
            writer.write(line);
            writer.newLine();
            writer.close();
        } catch (IOException e){
            System.out.println("Could not create write ps1 file or read credentials file.");
            e.printStackTrace();
        }

        //execute temporary script file
        String command = "powershell.exe " + tempFile.getAbsolutePath();
        Process powerShellProcess = Runtime.getRuntime().exec(command);
        powerShellProcess.getOutputStream().close();


        //record result and delete temp file
        String line;
        BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
        while ((line = stdout.readLine()) !=null){
            result = result + " ; " + line;
        }

        tempFile.delete();
        clearCredentials();
        return result;
    }

    /**
     * Attempt to execute a given powershell script against a given target machine. Can take arguments to pass to script.
     *
     * @param targetMachine - The machine which the script will be executed against.
     * @param scriptLocation - The script which should be executed against the target machine.
     * @param args - A string list which takes arguments and passes them with the script.
     * @return - Return a string which contains any output the execution produced.
     * @throws IOException - IOException can happen if any of the files involved cannot be accessed.
     */
    public String executePowershellScript(String targetMachine, String scriptLocation, List<String> args) throws IOException {
        String result = "";
        File tempFile = new File("tempScript.ps1");
        String credentialsScript = System.getProperty("user.dir") + utils.getPropertyValue("PS1.SetupCredentials");
        String executionScript = System.getProperty("user.dir") + scriptLocation;

        //create temp ps1 file to execute later
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Could not create temporary ps1 file.");
            e.printStackTrace();
        }

        FileOutputStream outstream = new FileOutputStream(tempFile);

        try {
            BufferedReader reader;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream));

            //add credentials gathering to temp file
            reader = new BufferedReader(new FileReader(credentialsScript));
            String line = reader.readLine();
            while (line!= null) {
                if (line.contains("USERNAME_REPLACEME")){
                    line = line.replace("USERNAME_REPLACEME", utils.getPropertyValue("RemoteLoginUN"));
                } else if (line.contains("PASSWORD_REPLACEME")) {
                    line = line.replace("PASSWORD_REPLACEME", utils.getPropertyValue("RemoteLoginPW"));
                }
                writer.write(line);
                writer.newLine();
                line = reader.readLine();
            }
            reader.close();

            //add invoke-command which executes the provided script using credentials
            line = "Invoke-Command -FilePath " + executionScript + " -ComputerName " + targetMachine + " -Credential $Cred";

            //modify invoke-command to include given arguments
            line = line + " -ArgumentList ";
            int x = 0;
            for (String arg : args) {
                x++;
                line = line + "\"" + arg + "\"";

                if (x!=args.size()) {
                    line = line + ",";
                }
            }

            writer.write(line);
            writer.newLine();
            writer.close();
        } catch (IOException e){
            System.out.println("Could not create write ps1 file or read credentials file.");
            e.printStackTrace();
        }

        //execute temporary script file
        String command = "powershell.exe " + tempFile.getAbsolutePath();
        Process powerShellProcess = Runtime.getRuntime().exec(command);
        powerShellProcess.getOutputStream().close();


        //record result and delete temp file
        String line;
        BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
        while ((line = stdout.readLine()) !=null){
            result = result + " ; " + line;
        }

        tempFile.delete();
        clearCredentials();
        return result;
    }

    /**
     * Part of executePowershellScript() will create a $Cred global variable on the system. This method clears that after use.
     *
     * @throws IOException
     */
    public void clearCredentials() throws IOException {
        String command = "powershell.exe " + System.getProperty("user.dir") + utils.getPropertyValue("PS1.ClearCredentials");
        Process powerShellProcess = Runtime.getRuntime().exec(command);
        powerShellProcess.getOutputStream().close();
    }
}
