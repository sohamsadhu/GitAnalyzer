package com.soham.gitanalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitHubAnalyzer {

	private RepositoryService repositoryService;
	private ContentsService contentsService;

	public GitHubAnalyzer() {
		this.repositoryService = new RepositoryService();
		this.contentsService = new ContentsService();
	}

	public Repository getRepository(String owner, String repositoryName) {
		Repository repository = null;
		try {
			repository = repositoryService.getRepository(owner, repositoryName);
		} catch (IOException ioex) {
			System.err.println("Could not locate repository:" + repositoryName + " for owner:" + owner);
			ioex.printStackTrace();
		}
		return repository;
	}

	public List<RepositoryBranch> getBranches(Repository repository) {
		List<RepositoryBranch> branches = null;
		try {
			branches = repositoryService.getBranches(repository);
		} catch (IOException ioex) {
			System.err.println("Could not locate branches for repository:" + repository.getName());
			ioex.printStackTrace();
		}
		return branches;
	}

	public List<RepositoryTag> getTags(Repository repository) {
		List<RepositoryTag> tags = null;
		try {
			tags = repositoryService.getTags(repository);
		} catch (IOException ioex) {
			System.err.println("Could not locate tags for repository:" + repository.getName());
			ioex.printStackTrace();
		}
		return tags;
	}

	public List<RepositoryContents> getContents(Repository repository, String path, String reference) {
		List<RepositoryContents> contents = null;
		try {
			contents = contentsService.getContents(repository, path, reference);
		} catch (IOException ioex) {
			System.err.println("Contents for repository " + repository.getName() + " for tag/branch " + reference
					+ " on path " + ((path == null) ? "root" : path) + " could not be found.");
			ioex.printStackTrace();
		}
		return contents;
	}

	public List<String> notContainsFile(Repository repository, List<String> references, String path, String fileName) {
		List<String> referenceNames = new ArrayList<String>();
		boolean notFound;
		for (String reference : references) {
			List<RepositoryContents> contents = getContents(repository, null, reference);
			notFound = true;
			if (contents != null) {
				for (RepositoryContents content : contents) {
					if (content.getName().equals(fileName)) {
						notFound = false;
						break;
					}
				}
				if (notFound) {
					referenceNames.add(reference);
				}
			}
		}
		return referenceNames;
	}

	public void analyze() {
		Repository repository = getRepository("ruby", "ruby");
		String fileNotContains = new String(".travis.yml");
		if (repository != null) {
			System.out.println("Repository URL is: " + repository.getHtmlUrl());
			List<RepositoryBranch> branches = getBranches(repository);
			List<RepositoryTag> tags = getTags(repository);
			List<String> branchNamesNotHavingFile = null;
			List<String> tagNamesNotHavingFile = null;
			if (branches != null) {
				System.out.println("====================================");
				System.out.println("Repository: " + repository.getName() + " has following branches.");
				String branchName;
				List<String> branchNames = new ArrayList<>();
				for (RepositoryBranch branch : branches) {
					branchName = branch.getName();
					System.out.println(branchName);
					branchNames.add(branchName);
				}
				branchNamesNotHavingFile = notContainsFile(repository, branchNames, "null", fileNotContains);
			}
			if (tags != null) {
				System.out.println("====================================");
				System.out.println("Repository: " + repository.getName() + " has following tags.");
				String tagName;
				List<String> tagNames = new ArrayList<>();
				for (RepositoryTag tag : tags) {
					tagName = tag.getName();
					System.out.println(tagName);
					tagNames.add(tagName);
				}
				tagNamesNotHavingFile = notContainsFile(repository, tagNames, "null", fileNotContains);
			}
			if ((branchNamesNotHavingFile == null) || (0 == branchNamesNotHavingFile.size())) {
				System.out.println("====================================");
				System.out.println(
						"All the branches in repository " + repository.getName() + " have the file " + fileNotContains);
			} else {
				System.out.println("====================================");
				System.out.println("Branches that do not have " + fileNotContains + " in root folder are");
				for (String branch : branchNamesNotHavingFile) {
					System.out.println(branch);
				}
			}
			if ((tagNamesNotHavingFile == null) || (0 == tagNamesNotHavingFile.size())) {
				System.out.println("====================================");
				System.out.println(
						"All the tags in repository " + repository.getName() + " have the file " + fileNotContains);
			} else {
				System.out.println("====================================");
				System.out.println("Tags that do not have " + fileNotContains + " in root folder are");
				for (String tag : tagNamesNotHavingFile) {
					System.out.println(tag);
				}
			}
		}
	}

	public static void main(String[] args) {
		GitHubAnalyzer gitHubAnalyzer = new GitHubAnalyzer();
		gitHubAnalyzer.analyze();
	}
}
