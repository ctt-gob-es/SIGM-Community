/**
 *
 */
package com.ieci.tecdoc.isicres.session.distribution;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.entity.dao.DBEntityDAOFactory;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.DistributionException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrDistreg;
import com.ieci.tecdoc.common.isicres.Keys;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.common.keys.ServerKeys;
import com.ieci.tecdoc.common.utils.ISDistribution;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.isicres.events.exception.EventException;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.Validator;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

/**
 * @author 66575267
 *
 * Esta clase nos proporciona metodos especiales para realizar operaciones de
 * distribución.
 *
 */
public class DistributionSessionEx extends DistributionSessionUtil implements
		ServerKeys, Keys, HibernateKeys {

	public static void acceptDistributionEx(String sessionID, List ids,
			String entidad) throws BookException, DistributionException,
			SessionException, ValidationException {

		Validator.validate_String_NotNull_LengthMayorZero(sessionID,
				ValidationException.ATTRIBUTE_SESSION);

		updateAcceptDistribution(sessionID, ids, null, entidad);

	}

	public static void rejectDistributionEx(String sessionID, List ids,
			String remarks, String entidad) throws DistributionException,
			SessionException, ValidationException {

		Validator.validate_String_NotNull_LengthMayorZero(sessionID,
				ValidationException.ATTRIBUTE_SESSION);
		Transaction tran = null;
		HibernateUtil hibernateUtil = new HibernateUtil();
		try {

			Session session = hibernateUtil.currentSession(entidad);
			tran = session.beginTransaction();

			// Recuperamos la sesión
			CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
					sessionID);
			AuthenticationUser user = (AuthenticationUser) cacheBag
					.get(HIBERNATE_Iuseruserhdr);

			for (Iterator it = ids.iterator(); it.hasNext();) {
				Integer ids1 = (Integer) it.next();
				ScrDistreg scrDistReg = (ScrDistreg) session.load(
						ScrDistreg.class, ids1);
				if (scrDistReg.getState() == ISDistribution.STATE_PENDIENTE
						|| scrDistReg.getState() == ISDistribution.STATE_ACEPTADO) {

					scrDistReg = updateDistReg(session, user, sessionID,
							DISTRIBUTION_REJECT_EVENT, scrDistReg,
							ISDistribution.STATE_RECHAZADO, ids1, remarks,
							entidad);

					DBEntityDAOFactory.getCurrentDBEntityDAO()
							.deleteDistAccept(
									new Integer(scrDistReg.getIdArch()),
									scrDistReg.getIdFdr(), entidad);
				}
			}

			hibernateUtil.commitTransaction(tran);
		} catch (DistributionException e) {
			hibernateUtil.rollbackTransaction(tran);
			throw e;
		} catch (SessionException sE) {
			hibernateUtil.rollbackTransaction(tran);
			throw sE;
		} catch (EventException eE){
			hibernateUtil.rollbackTransaction(tran);
			throw eE;
		} catch (Exception e) {
			hibernateUtil.rollbackTransaction(tran);
			throw new DistributionException(
					DistributionException.ERROR_CANNOT_REJECT_DISTRIBUTION);
		} finally {
			hibernateUtil.closeSession(entidad);
		}
	}

	public static void saveDistributionEx(String sessionID, List ids,
			String entidad) throws DistributionException, SessionException,
			ValidationException {
	    HibernateUtil hibernateUtil = new HibernateUtil();
		Validator.validate_String_NotNull_LengthMayorZero(sessionID,
				ValidationException.ATTRIBUTE_SESSION);
		Transaction tran = null;

		try {
			Session session = hibernateUtil.currentSession(entidad);
			tran = session.beginTransaction();

			// Recuperamos la sesión
			CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
					sessionID);
			AuthenticationUser user = (AuthenticationUser) cacheBag
					.get(HIBERNATE_Iuseruserhdr);

			for (Iterator it = ids.iterator(); it.hasNext();) {
				Integer ids1 = (Integer) it.next();
				ScrDistreg scrDistReg = (ScrDistreg) session.load(
						ScrDistreg.class, ids1);
				if (scrDistReg.getState() == ISDistribution.STATE_ACEPTADO) {
					scrDistReg = updateDistReg(session, user, sessionID,
							DISTRIBUTION_ARCHIVE_EVENT, scrDistReg,
							ISDistribution.STATE_ARCHIVADO, ids1, null, entidad);
				}
			}

			hibernateUtil.commitTransaction(tran);

		} catch (DistributionException e) {
			hibernateUtil.rollbackTransaction(tran);
			throw e;
		} catch (SessionException sE) {
			hibernateUtil.rollbackTransaction(tran);
			throw sE;
		} catch (EventException eE){
			hibernateUtil.rollbackTransaction(tran);
			throw eE;
		} catch (Exception e) {
			hibernateUtil.rollbackTransaction(tran);
			throw new DistributionException(
					DistributionException.ERROR_CANNOT_SAVE_DISTRIBUTION);
		} finally {
			hibernateUtil.closeSession(entidad);
		}
	}

	public static ScrDistreg getDistributionByIdEx(String sessionID,
			Integer distId, String entidad) throws ValidationException,
			DistributionException {
		Validator.validate_String_NotNull_LengthMayorZero(sessionID,
				ValidationException.ATTRIBUTE_SESSION);
		Transaction tran = null;
		HibernateUtil hibernateUtil = new HibernateUtil();
		ScrDistreg scrDistreg = null;
		try {
			Session session = hibernateUtil.currentSession(entidad);
			tran = session.beginTransaction();

			scrDistreg = ISicresQueries.getScrDistreg(session, distId);

			hibernateUtil.commitTransaction(tran);

		} catch (Exception e) {
			hibernateUtil.rollbackTransaction(tran);
			throw new DistributionException(
					DistributionException.ERROR_CANNOT_SAVE_DISTRIBUTION);
		} finally {
			hibernateUtil.closeSession(entidad);
		}

		return scrDistreg;
	}

}
