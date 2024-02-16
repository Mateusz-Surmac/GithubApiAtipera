package com.example.GithubApiAtipera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GithubController {

    private final GithubApiClient githubApiClient;

    @Autowired
    public GithubController(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    @GetMapping("/repositories/{username}")
    public ResponseEntity<?> getRepositories(@PathVariable String username) {
        try {
            List<GithubRepository> repositories = githubApiClient.getRepositories(username);

            List<RepositoryInfo> filteredRepositories = RepositoryFilter.filterRepositories(repositories);

            return ResponseEntity.ok(filteredRepositories);
        } catch (ResourceNotFoundException e) {
            GithubErrorResponse errorResponse = new GithubErrorResponse();
            errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            errorResponse.setMessage("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
