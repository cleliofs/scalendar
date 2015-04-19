This is a scala project to provide Calendar functions, such as booking of events, and so on.

Requirements:

"A System" should "allow to add/register new users"
    When "user enter their details of name and surname"
    Then "the user information is persisted"
    
"A System" should "show the list of registered users"
    When "a list of users registered in the system is invoked"
    Then "the list of registered users should be returned"
    
"A System" should "allow users to change their personal information"
    When "a user changes their personal details (name, email, etc)"
    Then "a user can see their details updated"
