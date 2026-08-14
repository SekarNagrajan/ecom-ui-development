package net.ecom.AppTierCntrlBean;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

/**
 *
 * @author prakash.mani
 */
@Remote
public interface EcomAnalyticsBeanRemote {

   public long insertStart(String countryCode, String custCode, String moduleCode);

   public int closeEndTime(long id);

   public long insertModuleStatus(String custcode, String moduleCode, String actionStatus);

   public int closeModuleStatus(long id);

   public long insertRouteLog(String custcode, String originPort, String destPort);

   // Analytics methods with date range and optional customer filtering
   public List<Map<String, Object>> moduleUsageCounts(String startDate, String endDate, String custcode);
   
   public List<Map<String, Object>> hoursThisWeekPerModule(String startDate, String endDate, String custcode);
   
   public List<Map<String, Object>> completedVsAbandoned(String startDate, String endDate, String custcode);
   
   public List<Map<String, Object>> uniqueCustomersByCountry(String startDate, String endDate, String custcode);
   
   public List<Map<String, Object>> topRoutes(String startDate, String endDate, String custcode);
   
   public List<Map<String, Object>> customerSearch(String searchTerm);
   
   public List<Map<String, Object>> loadAllCustomers();

}
