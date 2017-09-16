package com.estafet.microservices.api.project.burndown.jms;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.entity.Project;
import com.estafet.microservices.api.project.burndown.message.Story;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "storyConsumer")
public class NewStoryConsumer {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;
	
	public void onMessage(String message) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Story story  = mapper.readValue(message, Story.class);
			Project project = projectBurndownDAO.getProjectBurndown(story.getProjectId());
			if (project == null) {
				project = new Project().setId(story.getProjectId()).start();
				projectBurndownDAO.create(project.addStory(story));
			} else {
				projectBurndownDAO.update(project.addStory(story));	
			}
		} catch (IOException  e) {
			throw new RuntimeException(e);
		} 
	}
	
}
