@login

Feature: Testing the Compass Journey for Login

  @eng
  Scenario:  Compass Login Journey tested for already signed up user in English
    Given I am existing Compass user
    Then I am on CompassMemberLogin page
    When I enter "AROG001" in field userName in Login Page
    And I enter "pass4portal@" in field password in Login Page
    And I click the loginSubmit button in Login Page
    And I wait for 5 seconds to complete
    Then make sure I logged out successfully
    And I wait for 5 seconds to complete

  @french
  Scenario:  Compass Login Journey tested for already signed up user in English
    Given I am existing Compass user
    Then I am on CompassMemberLogin page
    When I enter "AROG001" in field userName in Login Page
    And I enter "pass4portal@" in field password in Login Page
    And I click the loginSubmit button in Login Page
    And I click the Francais link in Home Page to Change in Francais
    And I wait for 5 seconds to complete
    Then make sure I logged out successfully
    And I wait for 5 seconds to complete