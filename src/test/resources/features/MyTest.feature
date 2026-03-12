Feature:

#  Background:
#    Given the application is running

  @DemoTest
  Scenario Outline: Successful login
    Given I calculate two numbers which is <a> and <b>
    Examples:
      | a | b |
      | 5  | 6 |
      | 5  | 10 |
      | 51  | 6 |
      | 15  | 6 |
      | 522  | 10 |
      | 5113  | 613 |

