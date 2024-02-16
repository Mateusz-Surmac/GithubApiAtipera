package com.example.GithubApiAtipera;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GithubApiClient {

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.access.token}")
    private String accessToken;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();
    public List<GithubRepository> getRepositories(String username) throws IOException {
        String url = githubApiUrl + "/users/" + username + "/repos";

        String authHeader = "token " + accessToken;

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.NOT_FOUND.value()) {
                    throw new ResourceNotFoundException("User not found");
                } else {
                    throw new IOException("Failed to get repositories: " + response.code());
                }
            }

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                GithubRepository[] repositories = gson.fromJson(responseBody.string(), GithubRepository[].class);
                List<GithubRepository> nonForkRepositories = new ArrayList<>();
                for (GithubRepository repository : repositories) {
                    if (!repository.isFork()) {
                        List<GithubBranch> branches = getBranchesForRepository(repository, authHeader);
                        repository.setBranches(branches);
                        nonForkRepositories.add(repository);
                    }
                }
                return nonForkRepositories;
            } else {
                throw new IOException("Response body is empty");
            }
        }
    }

    private List<GithubBranch> getBranchesForRepository(GithubRepository repository, String authHeader) throws IOException {
        String url = githubApiUrl + "/repos/" + repository.getOwner().getLogin() + "/" + repository.getName() + "/branches";

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get branches for repository " + repository.getName() + ": " + response.code());
            }

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                GithubBranch[] branches = gson.fromJson(responseBody.string(), GithubBranch[].class);
                List<GithubBranch> githubBranches = new ArrayList<>();
                for (GithubBranch branch : branches) {
                    String lastCommitSha = getLastCommitShaForBranch(repository.getOwner().getLogin(), repository.getName(), branch.getName(), authHeader);
                    branch.setLastCommitSha(lastCommitSha);
                    githubBranches.add(branch);
                }
                return githubBranches;
            } else {
                throw new IOException("Response body is empty");
            }
        }
    }

    private String getLastCommitShaForBranch(String owner, String repo, String branchName, String authHeader) throws IOException {
        String url = String.format("%s/repos/%s/%s/branches/%s", githubApiUrl, owner, repo, branchName);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authHeader)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get last commit SHA for branch " + branchName + " in repository " + repo + ": " + response.code());
            }

            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                JsonObject jsonObject = gson.fromJson(responseBody.string(), JsonObject.class);
                JsonObject commitObject = jsonObject.getAsJsonObject("commit");
                return commitObject.get("sha").getAsString();
            } else {
                throw new IOException("Response body is empty");
            }
        }
    }
}