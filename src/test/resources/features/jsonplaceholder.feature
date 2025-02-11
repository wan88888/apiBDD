@jsonplaceholder-api
Feature: JSONPlaceholder API Testing

  Scenario: Get all posts
    Given Endpoint "/posts"
    When Send "GET" request
    Then Response status code should be 200
    And Response should not be empty
    And Response payload match json "get-posts-schema.json"
    And Response should contain field "userId"
    And Response should contain field "id"
    And Response should contain field "title"
    And Response should contain field "body"

  Scenario: Create a new post
    Given Endpoint "/posts"
    And Set request "create-post.json" and update request payload
      | title  | foo   |
      | body   | bar   |
      | userId | 1     |
    When Send "POST" request
    Then Response status code should be 201
    And Response should contain field "id"
    And Response field "title" should be "foo"
    And Response field "body" should be "bar"
    And Response field "userId" should be "1"