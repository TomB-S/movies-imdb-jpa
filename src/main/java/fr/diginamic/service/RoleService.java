package fr.diginamic.service;

import fr.diginamic.dao.RoleDao;
import fr.diginamic.entities.Role;

/**
 * Service pour l'entité Role.
 * Pas besoin de vérifier les doublons : l'id est auto-généré sans contrainte d'unicité.
 */
public class RoleService {
    private RoleDao roleDao;

    /**
     * Constructeur pour créer un objet RoleService
     * @param roleDao le DAO utilisé pour accéder aux données
     */
    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * Enregistre un rôle en base
     * @param role le rôle à enregistrer
     */
    public void create(Role role) {
        roleDao.save(role);
    }
}