package glp.dao;

import glp.domain.Annonce;
import glp.domain.Categorie;
import glp.domain.Stats;
import glp.domain.Utilisateur;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class AnnonceDaoImpl implements AnnonceDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int insertRow(Annonce ann) {
		Session session = sessionFactory.getCurrentSession();
		ann.setDate_deb(new Date());
		session.saveOrUpdate(ann);
		Serializable id = session.getIdentifier(ann);
		return (Integer) id;
	}

	@Override
	public List<Annonce> getList() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = session.createQuery("from Annonce").list();
		return annonceList;
	}

	@Override
	public List<Annonce> getListValides() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		// FALSE = refus�e - NULL = en attente - TRUE = valid�e
		List<Annonce> annonceList = session.createQuery(
				"FROM Annonce WHERE ann_valide=TRUE").list();
		return annonceList;
	}

	@Override
	public Annonce getRowById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Annonce ann = (Annonce) session.createCriteria(Annonce.class)
				.add(Restrictions.idEq(id)).uniqueResult();
		return ann;
	}

	@Override
	public int updateRow(Annonce ann) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(ann);
		Serializable id = session.getIdentifier(ann);
		return (Integer) id;
	}

	@Override
	public int deleteRow(int id) {
		Session session = sessionFactory.getCurrentSession();
		Annonce ann = (Annonce) session.load(Annonce.class, id);
		session.delete(ann);
		Serializable idann = session.getIdentifier(ann);
		return (Integer) idann;
	}

	@Override
	public List<Annonce> getListRecent(int catId) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "FROM Annonce where cat_id= :catID ORDER BY ann_date_debut";
		Query q = session.createQuery(sql).setParameter("catID", catId);
		q.setFirstResult(0);
		q.setMaxResults(5); // on obtient les 10 dernières annonces
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = q.list();
		System.out.println("taille " + annonceList.size());
		return annonceList;
	}

	@Override
	public List<Annonce> getListByMot(String searchText) {

		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = session
				.createQuery(
						"from Annonce"
								+ " where ann_desc like :searchText or ann_titre like:searchText")
				.setParameter("searchText", "%" + searchText + "%").list();
		return annonceList;

	}

	@Override
	public List<Annonce> getListByCat(int catId) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = session.createCriteria(Annonce.class)
				.add(Restrictions.ge("date_fin", new Date()))
				.createAlias("categorie", "c")
				.add(Restrictions.eq("c.id", catId)).list();
		return annonceList;
	}

	@Transactional
	@Override
	public List<Annonce> getListByUtilisateur(Utilisateur u) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = session.createCriteria(Annonce.class)
				.add(Restrictions.eq("auteur", u)).list();
		return annonceList;
	}

	@Override
	public List<Annonce> getListByCatEtMot(int idCat, String motcle) {
		Session session = sessionFactory.getCurrentSession();
//		@SuppressWarnings("unchecked")
//		List<Annonce> annonceList = session
//				.createQuery(
//						"from Annonce where cat_id= :catID"
//								+ " and (ann_desc like :searchText or ann_titre like:searchText )")
//				.setParameter("searchText", "%" + motcle + "%")
//				.setParameter("catID", idCat).list();
//		return annonceList;
		
//		if(!motcle.trim().isEmpty()) {
			@SuppressWarnings("unchecked")
			List<Annonce> annonceList = session.createCriteria(Annonce.class)
					.add(Restrictions.or(Restrictions.ilike("desc", "%"+motcle+"%"), Restrictions.ilike("titre", "%"+motcle+"%")))
					.add(Restrictions.ge("date_fin", new Date()))
					.createAlias("categorie", "c")
					.add(Restrictions.eq("c.id", idCat))
					.list();
			return annonceList;
//		} else
//			return getListByCat(idCat);
	}

	@Override
	public List<Annonce> getListAModerer() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		// FALSE = refus�e - NULL = en attente - TRUE = valid�e
		List<Annonce> annonceList = session.createQuery(
				"FROM Annonce WHERE ann_valide=NULL").list();
		return annonceList;
	}

	@Transactional
	@Override
	public void supprimerAnnoncesUtilisateur(Utilisateur u) {
		List<Annonce> listeAnnonces = getListByUtilisateur(u);
		if (listeAnnonces != null)
			for (Annonce a : listeAnnonces)
				deleteRow(a.getId());
	}

	@Transactional
	@Override
	public void supprimerAnnoncesCategorie(int catId) {
		List<Annonce> listeAnnonces = getListByCat(catId);
		if (listeAnnonces != null)
			for (Annonce a : listeAnnonces)
				deleteRow(a.getId());
	}

	@Transactional
	@Override
	public int nbAnnonceEnLigne() {
		Session session = sessionFactory.getCurrentSession();
		Date d = new Date();
		int nbAnnonces = ((Long) session.createCriteria(Annonce.class)
				.add(Restrictions.ge("date_fin", d))
				.add(Restrictions.eq("valide", true))
				.setProjection(Projections.rowCount()).uniqueResult())
				.intValue();
		System.out.println("nombre d'annonces en ligne " + nbAnnonces);
		return nbAnnonces;
	}

	@Transactional
	@Override
	public Map<String, Integer> getNbByCategorie() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Categorie> catList = session.createCriteria(Categorie.class)
				.addOrder(Order.asc("id")).list();
		Map<String, Integer> mapCategorieNbAnnonces = new HashMap<String, Integer>();
		for (Categorie c : catList) {
			int nbAnnonces = ((Long) session.createCriteria(Annonce.class)
					.add(Restrictions.eq("categorie", c))
					.setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			mapCategorieNbAnnonces.put(c.getLib(), nbAnnonces);
		}
		return mapCategorieNbAnnonces;
	}


	@Override
	public void incrementNbAnnCrees() {
		Session session = sessionFactory.getCurrentSession();

		Query query = session
				.createQuery("UPDATE Stats SET stats_nb_ann_crees=stats_nb_ann_crees+1");
		query.executeUpdate();
	}

	@Override
	public int getNbAnnCrees() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("SELECT max(stats_nb_ann_crees) FROM Stats");
		if (query.uniqueResult() == null)
			return 0;

		else
			return (Integer) query.uniqueResult();
	}

	@Override
	public Stats getStats() {
		Session session = sessionFactory.getCurrentSession();
		Stats stat = (Stats) session.createCriteria(Stats.class)
				.add(Restrictions.idEq(1)).uniqueResult();
		return stat;
	}

	@Override
	public void setDureeVieAnnonce(int duree_vie) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session
				.createQuery("UPDATE Stats SET nb_jours_fin_annonce= :duree_vie").setParameter("duree_vie", duree_vie);
		query.executeUpdate();
	}

@Override
public Date getDateLimite() {
	Session session = sessionFactory.getCurrentSession();
	
	return null;
}


	
	@Transactional
	@Override
	public List<Annonce> getListAnnoncesProposees() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = session.createCriteria(Annonce.class)
				.add(Restrictions.eq("valide", true))
				.add(Restrictions.eq("type", "propose"))
				.add(Restrictions.ge("date_fin", new Date()))
				.createAlias("categorie", "c")
				.add(Restrictions.neOrIsNotNull("c.lib", "Covoiturage"))
				.list();
		return annonceList;
	}

	@Transactional
	@Override
	public List<Annonce> getListAnnoncesDemandees() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = session.createCriteria(Annonce.class)
				.add(Restrictions.eq("valide", true))
				.add(Restrictions.eq("type", "demande"))
				.add(Restrictions.ge("date_fin", new Date()))
				.createAlias("categorie", "c")
				.add(Restrictions.neOrIsNotNull("c.lib", "Covoiturage"))
				.list();
		return annonceList;
	}

	@Transactional
	@Override
	public List<Annonce> getListCovProposes() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = session.createCriteria(Annonce.class)
				.add(Restrictions.eq("valide", true))
				.add(Restrictions.eq("type", "propose"))
				.add(Restrictions.ge("date_fin", new Date()))
				.createAlias("categorie", "c")
				.add(Restrictions.eq("c.lib", "Covoiturage"))
				.list();
		return annonceList;
	}

	@Transactional
	@Override
	public List<Annonce> getListCovDemandes() {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Annonce> annonceList = session.createCriteria(Annonce.class)
				.add(Restrictions.eq("valide", true))
				.add(Restrictions.eq("type", "demande"))
				.add(Restrictions.ge("date_fin", new Date()))
				.createAlias("categorie", "c")
				.add(Restrictions.eq("c.lib", "Covoiturage"))
				.list();
		return annonceList;
	}
}
