package com.example.secondskill.service;

import com.example.secondskill.dao.UserDao;
import com.example.secondskill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public User getById(int id) {
		 return userDao.getById(id);
	}

	@Transactional
	public boolean tx() {
		User u1= new User();
		u1.setId(2);
		u1.setName("2222");
		userDao.insert(u1);
		
		User u2= new User();
		u2.setId(3);
		u2.setName("333333");
		userDao.insert(u2);
		
		return true;
	}
	
}
