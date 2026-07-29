package fr.diginamic.service;

import fr.diginamic.dao.RoleDao;
import fr.diginamic.entities.Role;

public class RoleService {
    private RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void create(Role role) {
        roleDao.save(role);
    }
}