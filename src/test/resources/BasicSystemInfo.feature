Feature: Get basic system info from Windows OE.

  Scenario: Get IP address from Windows OE
    Given the Windows OE is on the Network
    When we can connect to the Windows OE
    Then I can retrieve the IP address from the Windows OE

  Scenario: Get hostname from Windows OE
    Given the Windows OE is on the Network
    When we can connect to the Windows OE
    Then I can retrieve the hostname from the Windows OE

  Scenario: Get total RAM from Windows OE
    Given the Windows OE is on the Network
    When we can connect to the Windows OE
    Then I can retrieve the Total RAM from the Windows OE

  Scenario: Get total RAM from Windows OE
    Given the Windows OE is on the Network
    When we can connect to the Windows OE
    Then I can retrieve the Processor clockspeed from the Windows OE
