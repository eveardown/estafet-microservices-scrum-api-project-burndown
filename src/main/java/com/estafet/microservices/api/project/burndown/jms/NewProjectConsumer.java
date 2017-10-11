package com.estafet.microservices.api.project.burndown.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.burndown.dao.ProjectBurndownDAO;
import com.estafet.microservices.api.project.burndown.model.Project;

@Component
public class NewProjectConsumer {

	@Autowired
	private ProjectBurndownDAO projectBurndownDAO;

	@Transactional
	@JmsListener(destination = "new.project.topic", containerFactory = "myFactory")
	public void onMessage(String message) {
		Project project = Project.fromJSON(message);
		if (projectBurndownDAO.getProjectBurndown(project.getId()) == null) {
			projectBurndownDAO.create(project);
		}
	}

}
