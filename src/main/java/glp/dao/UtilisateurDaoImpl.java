package glp.dao;

import glp.domain.Utilisateur;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UtilisateurDaoImpl implements UtilisateurDao {
	
	@Autowired  
	SessionFactory sessionFactory;  

	@Override
	public Integer insertRow(Utilisateur u) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(u);
		Serializable id = session.getIdentifier(u);
		return (Integer) id;
	}

	@Override
	public List<Utilisateur> getList() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Utilisateur> utilisateursList = session.createQuery("from Utilisateur").list();
		return utilisateursList;
	}

	@Override
	public Utilisateur getRowById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Utilisateur u = (Utilisateur) session.load(Utilisateur.class, id);
		return u;
	}

	@Override
	public Integer updateRow(Utilisateur u) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(u);
		Serializable id = session.getIdentifier(u);
		return (Integer) id;
	}

	@Override
	public Integer deleteRow(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Utilisateur u = (Utilisateur) session.load(Utilisateur.class, id);
		session.delete(u);
		Serializable iduti = session.getIdentifier(u);
		return (Integer) iduti;
	}

	@Override
	public void updateContactAutreMail(Utilisateur u, boolean contactAutreMail) {
		Session session = sessionFactory.getCurrentSession();
		u.setContactAutreMail(contactAutreMail);
		session.saveOrUpdate(u);
	}


}
