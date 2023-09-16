@Register
Feature: Registration Feature

  @UI_Test
  Scenario Outline:Register
    Given go to the "https://managementonschools.com"
    Given click on register
    When Enter name "<name>", surname "<surname>",birth_place "<birth_place>", phone_number "<phone_number>",gender "<gender>", birth_day "<birth_day>",ssn "<ssn>",username "<username>",password"<password>"
    Then click on register button
    Then close browser

    Examples:
      | username | birth_day  | birth_place | gender | name | phone_number | ssn         | surname | password |
      | johndoe  | 01-01-2000 | New York    | Male   | John | 377-654-4487 | 179-35-7446 | Doe     | John.123 |

  @Database_Test
  Scenario Outline: Validate registered user on database
    Given connect to database
    When get guest user via username "<username>"
    Then validate  username "<username>" birth_day "<birth_day>"   birth_place "<birth_place>"  gender "<gender>"  name "<name>" phone_number "<phone_number>"  ssn "<ssn>" surname "<surname>"
    And close the connection
    Examples:
      | username    | birth_day  | birth_place | gender | name | phone_number | ssn         | surname |
      | alvaro.howe | 2000-01-01 | New York    | 0      | John | 685 205 2829 | 721-73-5270 | Doe     |

  @Database_Test_Deleted
  Scenario: Validate registered user on database
    Given connect to database
    When get guest user via non-existing username "john123"
    Then validate  if the user is deleted
    And close the connection