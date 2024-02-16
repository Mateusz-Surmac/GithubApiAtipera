# Github API Client

## Description

GitHub API Client is an application that allows users to retrieve information about GitHub users' repositories. The application is written in Java using the Spring Boot framework.

## Features

-   **Fetching User Repositories**: The application allows fetching a list of repositories for a specified GitHub user.
-   **Repositories Filtering**: Repositories fetched from GitHub are filtered to exclude forked repositories.
-   **Branches Information Retrieval**: For each repository, information about branches is retrieved, including the last commit SHA for each branch.

## Usage Instructions

1.  **Running the Application**: The application is launched as a Spring Boot application. It can be run locally or on any Java-compatible server.
2.  **Calling Endpoints**: The application provides HTTP endpoints for fetching GitHub user repositories. These endpoints can be called by providing the username in the URL.
    -   Example request: `GET /repositories/{username}`
3. **Obtaining Personal Access Token**: To access GitHub's API, you'll need a personal access token. You can generate one by following the instructions [here](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens).

## Configuration

The application requires configuring the GitHub API URL and access token. In the `application.properties` file, set the appropriate values for:

- `github.api.url`: The GitHub API URL.
-   `github.access.token`: The access token for the GitHub API.

## Technologies Used

-   Java
-   Spring Boot
-   OkHttp
-   Gson

## Author

*Mateusz-Surmac*
