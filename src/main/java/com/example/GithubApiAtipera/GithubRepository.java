package com.example.GithubApiAtipera;

import java.util.List;

class GithubRepository {
    private String name;
    private GithubOwner owner;
    private List<GithubBranch> branches;
    private boolean fork;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GithubOwner getOwner() {
        return owner;
    }

    public void setOwner(GithubOwner owner) {
        this.owner = owner;
    }

    public List<GithubBranch> getBranches() {
        return branches;
    }

    public void setBranches(List<GithubBranch> branches) {
        this.branches = branches;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }
}