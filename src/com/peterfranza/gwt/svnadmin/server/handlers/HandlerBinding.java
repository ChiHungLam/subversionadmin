package com.peterfranza.gwt.svnadmin.server.handlers;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;

import com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt.AddUserToGroupHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt.CreateGroupHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt.ListGroupHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt.ListUsersInGroupHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt.RemoveGroupHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.groupmgmt.RemoveUserFromGroupHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.AddProjectHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.AddProjectMemberHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ListProjectMembersHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ListProjectsHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ProjectAddUserSubscriptionHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ProjectListUserSubscriptionHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ProjectRemoveUserSubscriptionHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ProjectSetMemberAccessHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.RemoveProjectMemberHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.projectmgmt.ScanProjectHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.ChangeUserEmailHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.ChangeUserPasswordHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.ChangeUserRightsHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.CreateUserHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.FetchUserDetailsHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.ListUsersHandler;
import com.peterfranza.gwt.svnadmin.server.handlers.usermgmt.RemoveUserHandler;

public class HandlerBinding extends ActionHandlerModule {

	@Override
	protected void configureHandlers() {
		bindHandler(CapabilitiesRequestHandler.class);
		bindHandler(AuthenticationHandler.class);
		
		bindHandler(AddProjectHandler.class);
		bindHandler(AddProjectMemberHandler.class);
		bindHandler(ListProjectMembersHandler.class);
		bindHandler(ListProjectsHandler.class);
		bindHandler(ProjectAddUserSubscriptionHandler.class);
		bindHandler(ProjectListUserSubscriptionHandler.class);
		bindHandler(ProjectRemoveUserSubscriptionHandler.class);
		bindHandler(ProjectSetMemberAccessHandler.class);
		bindHandler(RemoveProjectMemberHandler.class);
		bindHandler(ScanProjectHandler.class);
		
		bindHandler(ChangeUserEmailHandler.class);
		bindHandler(ChangeUserPasswordHandler.class);
		bindHandler(ChangeUserRightsHandler.class);
		bindHandler(CreateUserHandler.class);
		bindHandler(FetchUserDetailsHandler.class);
		bindHandler(ListUsersHandler.class);
		bindHandler(RemoveUserHandler.class);
		
		bindHandler(AddUserToGroupHandler.class);
		bindHandler(CreateGroupHandler.class);
		bindHandler(ListGroupHandler.class);
		bindHandler(RemoveGroupHandler.class);
		bindHandler(RemoveUserFromGroupHandler.class);
		bindHandler(ListUsersInGroupHandler.class);
	}

}
