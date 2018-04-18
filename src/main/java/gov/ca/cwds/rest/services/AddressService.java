package gov.ca.cwds.rest.services;

import java.io.Serializable;

import javax.transaction.UserTransaction;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.google.inject.Inject;

import gov.ca.cwds.data.Dao;
import gov.ca.cwds.data.ns.AddressDao;
import gov.ca.cwds.data.ns.XaNsAddressDao;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.api.domain.Address;
import gov.ca.cwds.rest.api.domain.PostedAddress;
import gov.ca.cwds.rest.filters.RequestExecutionContext;

/**
 * Business layer object to work on Postgres (NS) {@link Address}.
 * 
 * @author CWDS API Team
 */
public class AddressService implements CrudsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);

  private AddressDao addressDao;
  private XaNsAddressDao xaAddressDao;

  /**
   * Constructor
   * 
   * @param addressDao The {@link Dao} handling {@link gov.ca.cwds.data.persistence.ns.Address}
   *        objects.
   */
  @Inject
  public AddressService(AddressDao addressDao, XaNsAddressDao xaAddressDao) {
    this.addressDao = addressDao;
    this.xaAddressDao = xaAddressDao;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#find(java.io.Serializable)
   */
  @Override
  public Address find(Serializable primaryKey) {
    assert primaryKey instanceof Long;

    gov.ca.cwds.data.persistence.ns.Address persistedAddress = addressDao.find(primaryKey);
    if (persistedAddress != null) {
      return new Address(persistedAddress);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#delete(java.io.Serializable)
   */
  @Override
  public Response delete(Serializable primaryKey) {
    assert primaryKey instanceof Long;
    throw new NotImplementedException("Delete is not implemented");
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#create(gov.ca.cwds.rest.api.Request)
   */
  @Override
  public PostedAddress create(Request request) {
    assert request instanceof Address;

    final Address address = (Address) request;
    gov.ca.cwds.data.persistence.ns.Address managed =
        new gov.ca.cwds.data.persistence.ns.Address(address, null, null);

    managed = addressDao.create(managed);
    return new PostedAddress(managed);
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Update NS and CMS with XA transaction. See INT-1592.
   * </p>
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#update(java.io.Serializable,
   *      gov.ca.cwds.rest.api.Request)
   */
  @Override
  public Response update(Serializable primaryKey, Request request) {
    assert primaryKey instanceof Long;
    assert request instanceof Address;

    final String strId = String.valueOf((long) primaryKey);
    final Address reqAddress = (Address) request;
    final RequestExecutionContext ctx = RequestExecutionContext.instance();
    final String staffId = ctx.getStaffId();

    final UserTransaction txn = new UserTransactionImp();
    try {
      // Start XA transaction:
      txn.setTransactionTimeout(80);
      txn.begin();

      // Do work:
      final gov.ca.cwds.data.persistence.ns.Addresses persistedAddress = xaAddressDao.find(strId);
      persistedAddress.setStreetAddress(reqAddress.getStreetAddress());
      final gov.ca.cwds.data.persistence.ns.Addresses ret = xaAddressDao.update(persistedAddress);

      // Commit XA transaction:
      txn.commit();

      // Return results.
      return new PostedAddress(ret);
    } catch (Exception e) {
      try {
        txn.rollback();
      } catch (Exception e2) {
        LOGGER.warn(e2.getMessage(), e2);
      }

      final String oops =
          String.format("XA TRANSACTION ERROR! stack trace: %s", ExceptionUtils.getStackTrace(e));
      throw new ServiceException(oops, e);
    }
  }

}
