package com.soham.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.soham.gitanalyzer.GitHubAnalyzer;

@RunWith(MockitoJUnitRunner.class)
public class GitHubAnalyzerTest {

	@Mock
	private User user;
	
	@Mock
	private Repository repository;
	
	@Mock
	private RepositoryService repositoryService;
	
	@InjectMocks
	private GitHubAnalyzer gitHubAnalyzer = new GitHubAnalyzer();
	
	@Before
	public void create() throws IOException {
		MockitoAnnotations.initMocks(this);
		user.setName("someone");
		repository.setOwner(user);
		repository.setName("something");
		when(repositoryService.getRepository("someone", "something")).thenReturn(repository);
	}
	
    @Test
    public void testRepository() {
    	assertEquals(gitHubAnalyzer.getRepository("someone", "something"), repository);
    }

}
