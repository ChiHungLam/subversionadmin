package com.peterfranza.svnadmin.server.acldb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.peterfranza.svnadmin.server.acldb.ACLDB.ACLItem;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Group;
import com.peterfranza.svnadmin.server.acldb.ACLDB.Subscription;
import com.peterfranza.svnadmin.server.acldb.ACLDB.User;
import com.peterfranza.svnadmin.server.acldb.utils.UnixCrypt;

public class ACLOperationsDelegate {
	
	private static Object lock = new Object();
	
	private static ACLOperationsDelegate instance = null;

	public synchronized static ACLOperationsDelegate getInstance() {
		if(instance == null) {
			instance = new ACLOperationsDelegate();
			instance.reload();
		}
		return instance;
	}
	private ACLDBFileDelegate acl;
	
	private UserOperations userOps = new UserOperations();
	private GroupOperations groupOps = new GroupOperations();
	
	private ACLOperationsDelegate() {}
	
	public UserOperations getUserOperations() {
		return userOps;
	}
	
	public GroupOperations getGroupOperations() {
		return groupOps;
	}

	
	public List<String> getAddressesSubscribedTo(List<String> changes) {
		List<String> addresses = new ArrayList<String>();
		synchronized(lock) {
			for(User u: acl.getACL().getUsers()) {
				if(isSubscribedTo(u, changes)) {
					addresses.add(u.getEmail());
				}
			}
		}
				
		return addresses;
	}
	
	private void reload() {
		try {
			synchronized(lock) {
			acl = new ACLDBFileDelegate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void save() {
		acl.save();
		reload();
	}






	private boolean isSubscribedTo(User u, List<String> changes) {
		for (String change : changes) {
			for(Subscription s: u.getSubscriptions()) {
				if(change.startsWith(s.getPath())) {
					return true;
				}
			}
		}
		return false;
	}


	
	public class GroupOperations {
		
		private GroupOperations() {}
		
		public List<String> getGroupNames() {
			List<String> l = new ArrayList<String>();
			synchronized(lock) {
				for(Group g: acl.getACL().getGroups()) {
					l.add(g.getName());
				}
			}
			Collections.sort(l);
			return l;
		}

		public void modifyGroup(String groupName, List<String> addUsers,
				List<String> removeUsers) {
			synchronized(lock) {
				Group g = getGroup(groupName);
				for (Iterator<ACLItem> iterator = g.getMembers().iterator(); iterator.hasNext();) {
					ACLItem item = (ACLItem) iterator.next();
					if (item instanceof Group) {
						Group group = (Group) item;
						if(removeUsers.contains(group.getName())) {
							iterator.remove();
						}
					} else {
						User user = (User) item;
						if(removeUsers.contains(user.getUsername())) {
							iterator.remove();
						}
					}
				}
				
				for(String add: addUsers) {
					Group grp = getGroup(add);
					if(grp != null) {
						g.getMembers().add(grp);
					} else {
						User u = getUserOperations().getUser(add);
						if(u != null) {
							g.getMembers().add(u);
						}
					}		
				}
				save();
			}
		}

		private Group getGroup(String groupName) {
			for(Group g: acl.getACL().getGroups()) {
				if(g.getName().equals(groupName)) {
					return g;
				}
			}
			return null;
		}
		
		public void createGroup(String groupName) {
			Group g = new Group();
			g.setName(groupName);
			synchronized(lock) {
				acl.getACL().getGroups().add(g);
				save();
			}
		}
		
		public boolean isGroup(String groupName){
			synchronized(lock) {
				return getGroup(groupName) != null;
			}
		}
		
		public List<String> getGroupMembership(String username) {
			List<String> l = new ArrayList<String>();
			synchronized(lock) {
				for(Group g: acl.getACL().getGroups()) {
					if(isMemeberOfGroup(username, g)) {
						l.add(g.getName());
					}
				}
			}
			return l;
		}

		private boolean isMemeberOfGroup(String username, Group g) {
			for(ACLItem i: g.getMembers()) {
				if(i instanceof Group) {
					boolean flag = isMemeberOfGroup(username, (Group)i);
					if(flag) {
						return true;
					}
				} else {
					User u = (User)i;
					if(u.getUsername().equals(username)) {
						return true;
					}
				}
			}
			return false;
		}
		
	}
	
	public class UserOperations {

		private UserOperations() {}

		
		public boolean authenticate(String username, String enteredPassword) {
			synchronized(lock) {
				User u = getUserOperations().getUser(username);
				if(u != null) {
					return UnixCrypt.matches(u.getPassword(), enteredPassword);
				} 
				return false;
			}
		}
		
		public void setPassword(String username, String newPassword) {
			synchronized(lock) {
				User u = getUserOperations().getUser(username);
				if(u != null) {
					u.setPassword(UnixCrypt.crypt(newPassword));
					save();
				}
			}
		}
		
		public void addNewUser(String username, String password,
				String email) {
			synchronized(lock) {
				User u = new User(username, UnixCrypt.crypt(password));
				u.setEmail(email);
				acl.getACL().getUsers().add(u);
				save();
			}
		}

		public void updateUser(String username, String email,
				String admin, String password) {
			synchronized(lock) {			
				User u = getUser(username);
				u.setEmail(email);
				u.setAdmin(Boolean.valueOf(admin));
				if(password != null && password.length() > 1) {
					u.setPassword(UnixCrypt.crypt(password));
				}
				save();
			}
		}

		public void deleteUser(String username) {
			synchronized(lock) {			
				User u = getUser(username);
				acl.getACL().getUsers().remove(u);
				save();
			}
		}

		public String getEmailForUser(String username) {
			synchronized(lock) {
				User u = getUser(username);
				if(u != null) {
					return u.getEmail();
				} 
			}
			return null;
		}

		public User getUser(String username) {
			for(User u: acl.getACL().getUsers()) {
				if(u.getUsername().equals(username)) {
					return u;
				}
			}
			return null;
		}

		public List<String> getUsernames() {
			List<String> names = new ArrayList<String>();
			synchronized(lock) {
				for(User u: acl.getACL().getUsers()) {
					names.add(u.getUsername());
				}
			}
			return names;
		}

		public boolean isAdmin(String username) {
			synchronized(lock) {
				User u = getUser(username);
				if(u != null) {
					return u.isAdmin();
				}
			}
			return false;
		}



	}
	
}
