package glp.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import glp.domain.Annonce;
import glp.domain.Forum;
import glp.domain.Signalisation;

public class SignalisationDaoImpl implements SignalisationDao{

	
	
	@Autowired  
	 private SessionFactory sessionFactory;  
	
	
	@Override
	public int insertRow(Signalisation signal) {
		Session session = sessionFactory.getCurrentSession();
		session.save(signal);
		Serializable id = session.getIdentifier(signal);
		return (Integer) id;
	}
	
	@Override
	public int deleteRow(int id) {
		Session session = sessionFactory.getCurrentSession();
		Signalisation signalisation = (Signalisation) session.load(Signalisation.class, id);
		session.delete(signalisation);
		Serializable idSign = session.getIdentifier(signalisation);
		return (Integer) idSign;
	}


	@Override
	public List<Signalisation> getListRecent() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Signalisation> signalList = session.createQuery("from Forum ORDER BY forum_id DESC").list();
		return signalList;
	}


	@Override
	public List<Signalisation> getListSignalements(Forum forum) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Signalisation> listSignalements = session.createCriteria(Signalisation.class)
				.add(Restrictions.eq("forum", forum))
				.list();
		return listSignalements;
	}
	

	@Transactional
	@Override
	public List<Signalisation> getList() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Signalisation> listSignalements = session.createCriteria(Signalisation.class)
				.list();
		return listSignalements;
	}

}
