package com.example.GithubApiAtipera;

import java.util.ArrayList;
import java.util.List;

class RepositoryFilter {

    public static List<RepositoryInfo> filterRepositories(List<GithubRepository> repositories) {
        List<RepositoryInfo> filteredRepositories = new ArrayList<>();
        for (GithubRepository repository : repositories) {
            // Filtruj repozytoria, które nie są zforkowane
            if (!repository.isFork()) {
                RepositoryInfo repositoryInfo = new RepositoryInfo();
                repositoryInfo.setName(repository.getName());
                repositoryInfo.setOwner(repository.getOwner().getLogin());
                repositoryInfo.setBranches(getBranchesInfo(repository));
                filteredRepositories.add(repositoryInfo);
            }
        }
        return filteredRepositories;
    }

    private static List<BranchInfo> getBranchesInfo(GithubRepository repository) {
        List<BranchInfo> branchesInfo = new ArrayList<>();
        List<GithubBranch> branches = repository.getBranches();
        if (branches != null) {
            for (GithubBranch branch : branches) {
                BranchInfo branchInfo = new BranchInfo();
                branchInfo.setName(branch.getName());
                branchInfo.setLastCommitSha(branch.getLastCommitSha());
                branchesInfo.add(branchInfo);
            }
        }
        return branchesInfo;
    }
}