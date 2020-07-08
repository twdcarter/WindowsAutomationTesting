import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import  org.junit.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class CucumberSteps {

    Utils utils = new Utils();

    @Given("^the Windows OE is on the Network$")
    public void the_Windows_OE_is_on_the_Network() throws Exception {
        String ip = utils.getPropertyValue("TestMachineNetworkIP");
        PowerShellInvoker psi = new PowerShellInvoker();

        if (!psi.pingMachine(ip)){
            Assert.fail("Could not ping target machine: " + ip);
        }
    }

    @When("^we can connect to the Windows OE$")
    public void we_can_connect_to_the_Windows_OE() throws Exception {
        String remoteIP = utils.getPropertyValue("TestMachineNetworkIP");
        PowerShellInvoker psi = new PowerShellInvoker();

        if (!psi.testPSConnection(remoteIP)){
            Assert.fail("Could not connect to target machine via powershell: " + remoteIP);
        }
    }

    @Then("^I can retrieve the IP address from the Windows OE$")
    public void i_can_retrieve_the_IP_address_from_the_Windows_OE() throws Exception {
        String remoteIP  = utils.getPropertyValue("TestMachineNetworkIP");
        String expectedIP = utils.getExpectedResult("expected.IPAddress");
        String psiScript = utils.getPropertyValue("PS1.GetIPv4");
        PowerShellInvoker psi = new PowerShellInvoker();
        String result = psi.executePowershellScript(remoteIP, psiScript);

        if (!result.contains(expectedIP)){
            Assert.fail("Expected IPv4 address not returned from target machine.");
        }
    }

    @Then("^I can retrieve the hostname from the Windows OE$")
    public void i_can_retrieve_the_hostname_from_the_Windows_OE() throws Exception {
        String remoteIP  = utils.getPropertyValue("TestMachineNetworkIP");
        String expectedHostname = utils.getExpectedResult("expected.hostname");
        String psiScript = utils.getPropertyValue("PS1.GetHostname");
        PowerShellInvoker psi = new PowerShellInvoker();
        String result = psi.executePowershellScript(remoteIP, psiScript);

        if (!result.contains(expectedHostname)){
            Assert.fail("Expected hostname not returned from target machine.");
        }
    }

    @Then("^I can retrieve the Total RAM from the Windows OE$")
    public void i_can_retrieve_the_Total_RAM_from_the_Windows_OE() throws Exception {
        String remoteIP  = utils.getPropertyValue("TestMachineNetworkIP");
        String expectedTotalRAM = utils.getExpectedResult("expected.TotalRAM");
        String psiScript = utils.getPropertyValue("PS1.GetTotalRAM");
        PowerShellInvoker psi = new PowerShellInvoker();
        String result = psi.executePowershellScript(remoteIP, psiScript);

        if (!result.contains(expectedTotalRAM)){
            Assert.fail("Expected value of Total RAM not returned from target machine.");
        }
    }

    @Then("^I can retrieve the Processor clockspeed from the Windows OE$")
    public void i_can_retrieve_the_Processor_clockspeed_from_the_Windows_OE() throws Exception {
        String remoteIP  = utils.getPropertyValue("TestMachineNetworkIP");
        String expectedProcessorClockspeed = utils.getExpectedResult("expected.Clockspeed");
        String psiScript = utils.getPropertyValue("PS1.Clockspeed");
        PowerShellInvoker psi = new PowerShellInvoker();
        String result = psi.executePowershellScript(remoteIP, psiScript);

        if (!result.contains(expectedProcessorClockspeed)){
            Assert.fail("Expected value of Processor Clockspeed not returned from target machine.");
        }
    }

    @Then("^I can send an argument based script to the Windows OE$")
    public void i_can_send_an_argument_based_script_to_the_Windows_OE() throws Exception {
        String remoteIP  = utils.getPropertyValue("TestMachineNetworkIP");
        String psiScript = utils.getPropertyValue("PS1.ArgumentsTest");
        String expectedPositive = utils.getExpectedResult("expected.argumentest.positive");
        String expectedNegative = utils.getExpectedResult("expected.argumentest.negative");

        //creating a List of Strings to contain arguments, even if its only 1 argument
        List<String> args = new ArrayList<String>();
        args.add(utils.getPropertyValue("PS1.ArgumentsTest.Arg1"));

        PowerShellInvoker psi = new PowerShellInvoker();

        //overloaded the executePowerShell method to optionally accept a List<String as a 3rd argument
        String result = psi.executePowershellScript(remoteIP, psiScript, args);

        //positive assertion - seeing if expected result is in file
        if(result.contains(expectedPositive)){
            //no need to really have anything in here, junit will assume a pass if there is no fail asserted
        }

        //negative assertion - will fail if expected positive is missing or expected negative is present
        // ! = java for NOT
        // || = java for OR
        if(!result.contains(expectedPositive) || result.contains(expectedNegative)){
            Assert.fail("Either the file didnt contain 'foo' or it did contain 'bar'.");
        }
    }
}
