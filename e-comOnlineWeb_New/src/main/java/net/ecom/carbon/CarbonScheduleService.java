package net.ecom.carbon;

import com.google.gson.Gson;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import net.ecom.AppTierCntrlBean.EcomBookingBeanRemote;
import net.ecom.common.GeneralConf;
import net.ecom.common.MaskPort;
import net.ecom.common.Validation;
import net.ecom.common.utils.DateConversion_New;
import net.ecom.common.utils.MaskPortUtil;
import net.ecom.common.utils.ScheduleAPIService;
import net.ecom.lookup.EJBLookUp;
import net.ecom.vo.Route;

public class CarbonScheduleService {

    /** Session key set by {@code SchedulebetweenlocationView.jsp} when schedule results are rendered. */
    public static final String SCHEDULE_INFO_SESSION_KEY = "Schedule_info";

    private static final String FALLBACK_ERROR = "We could not perform the calculations at this time. Please try again.";
    private static final String LOG_PREFIX = "[ECOM-WAR] CarbonScheduleService";

    private final ClimatiqService climatiqService;
    private final Gson gson = new Gson();

    public CarbonScheduleService() {
        this(new ClimatiqService());
    }

    public CarbonScheduleService(ClimatiqService climatiqService) {
        this.climatiqService = climatiqService;
    }

    public CarbonScheduleResult calculateForRoute(String originCode, String deliveryCode,
            String weightKg, String quantity, ServletContext ctx, HttpSession session) {
        try {
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=start origin=" + originCode + " delivery=" + deliveryCode
                    + " weightKg=" + weightKg + " quantity=" + quantity);

            if (ctx == null) {
                return failureResult("ServletContext is required.");
            }

            String resolvedOrigin = MaskPortUtil.swapmaskporttoactual(Validation.checkNull(originCode));
            String resolvedDelivery = MaskPortUtil.swapmaskporttoactual(Validation.checkNull(deliveryCode));
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=maskPortsResolved origin=" + resolvedOrigin
                    + " delivery=" + resolvedDelivery);

            if (!MaskPort.isMaskPorts(resolvedOrigin + "," + resolvedDelivery, ctx)) {
                CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=maskPortGateBlocked");
                return failureResult(FALLBACK_ERROR);
            }

            double parsedWeight = parseWeight(weightKg);
            int parsedQuantity = parseQuantity(quantity);
            DateRange dateRange = buildScheduleDateRange(ctx);
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=dateRange fromdate=" + dateRange.fromDate
                    + " todate=" + dateRange.toDate);

            EcomBookingBeanRemote bookingObj = EJBLookUp.getBookingBeanLookUp();
            Map<String, List> scheduleResult = fetchSchedule(ctx, session, bookingObj, resolvedOrigin,
                    resolvedDelivery, dateRange);
            List scheduleOptions = scheduleResult == null ? null : scheduleResult.get("actualschedule");
            int scheduleOptionsCount = scheduleOptions == null ? 0 : scheduleOptions.size();
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleFetched options=" + scheduleOptionsCount);

            List<Route> selectedRoute = selectRoute(ctx, scheduleOptions);
            if (selectedRoute == null || selectedRoute.isEmpty()) {
                CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=noRouteSelected");
                return failureResult(FALLBACK_ERROR);
            }

            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=routeSelected legs=" + selectedRoute.size());

            return calculateFromRouteLegs(selectedRoute, parsedWeight, parsedQuantity, ctx,
                    hasDefaultRoute(selectedRoute), scheduleOptionsCount);
        } catch (Exception ex) {
            ex.printStackTrace();
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=failed message=" + ex.getMessage());
            return failureResult(FALLBACK_ERROR);
        }
    }

    /**
     * Card-level carbon: reuses the itinerary already stored in session after a schedule search.
     * Does not call {@link #fetchSchedule(ServletContext, HttpSession, EcomBookingBeanRemote, String, String, DateRange)}
     * or {@link #selectRoute(ServletContext, List)}.
     */
    public CarbonScheduleResult calculateForScheduleRouteIndex(int scheduleRouteIndex, String weightKg,
            String quantity, ServletContext ctx, HttpSession session) {
        try {
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardStart index=" + scheduleRouteIndex
                    + " weightKg=" + weightKg + " quantity=" + quantity);

            if (ctx == null) {
                return failureResult("ServletContext is required.");
            }
            if (session == null) {
                CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardNoSession");
                return failureResult(FALLBACK_ERROR);
            }

            List<Route> selectedRoute = resolveScheduleRouteFromSession(session, scheduleRouteIndex, ctx);
            if (selectedRoute == null || selectedRoute.isEmpty()) {
                CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardRouteMissing index=" + scheduleRouteIndex);
                return failureResult(FALLBACK_ERROR);
            }

            String[] endpoints = extractEndpointPorts(selectedRoute);
            if (endpoints == null) {
                CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardInvalidEndpoints index=" + scheduleRouteIndex);
                return failureResult(FALLBACK_ERROR);
            }

            String resolvedOrigin = MaskPortUtil.swapmaskporttoactual(endpoints[0]);
            String resolvedDelivery = MaskPortUtil.swapmaskporttoactual(endpoints[1]);
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardMaskPorts origin=" + resolvedOrigin
                    + " delivery=" + resolvedDelivery);

            if (!MaskPort.isMaskPorts(resolvedOrigin + "," + resolvedDelivery, ctx)) {
                CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardMaskPortGateBlocked");
                return failureResult(FALLBACK_ERROR);
            }

            double parsedWeight = parseWeight(weightKg);
            int parsedQuantity = parseQuantity(quantity);
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardRouteResolved index=" + scheduleRouteIndex
                    + " legs=" + selectedRoute.size());

            return calculateFromRouteLegs(selectedRoute, parsedWeight, parsedQuantity, ctx,
                    hasDefaultRoute(selectedRoute), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardFailed message=" + ex.getMessage());
            return failureResult(FALLBACK_ERROR);
        }
    }

    private CarbonScheduleResult calculateFromRouteLegs(List<Route> selectedRoute, double parsedWeight,
            int parsedQuantity, ServletContext ctx, boolean selectedRouteDefault, int scheduleOptionsCount) {
        ScheduleCarbonPayloadMapper.BuildResult payloadBuild = ScheduleCarbonPayloadMapper.buildFromRoutes(
                selectedRoute, parsedWeight, parsedQuantity);
        ClimatiqRequest climatiqRequest = payloadBuild.getClimatiqRequest();
        CarbonScheduleMetadata metadata = new CarbonScheduleMetadata(
                selectedRouteDefault,
                scheduleOptionsCount,
                payloadBuild.getLegMetadata());
        CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=payloadMapped locations=" + gson.toJson(climatiqRequest.getLocations())
                + " legs=" + (climatiqRequest.getLegs() == null ? "[]" : climatiqRequest.getLegs().toString())
                + " routingDetails=" + gson.toJson(climatiqRequest.getRoutingDetails()));

        if (climatiqRequest.getLocations().size() < 2
                || climatiqRequest.getLegs() == null
                || climatiqRequest.getLegs().size() != climatiqRequest.getLocations().size() - 1) {
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=invalidPayloadChain");
            return failureResult(FALLBACK_ERROR);
        }

        ClimatiqResponse climatiqResponse;
        try {
            climatiqResponse = climatiqService.calculate(climatiqRequest, ctx);
        } catch (IOException ex) {
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=climatiqIoFailed message=" + ex.getMessage());
            return failureResult(FALLBACK_ERROR);
        }
        CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=climatiqComplete httpStatus=" + climatiqResponse.getHttpStatus()
                + " response=" + gson.toJson(climatiqResponse.getBody()));
        return new CarbonScheduleResult(climatiqRequest, climatiqResponse, metadata);
    }

    @SuppressWarnings("unchecked")
    private List<Route> resolveScheduleRouteFromSession(HttpSession session, int scheduleRouteIndex,
            ServletContext ctx) {
        Object scheduleInfoObj = session.getAttribute(SCHEDULE_INFO_SESSION_KEY);
        if (!(scheduleInfoObj instanceof List)) {
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardNoScheduleInfo");
            return null;
        }
        List scheduleInfo = (List) scheduleInfoObj;
        if (scheduleRouteIndex < 0 || scheduleRouteIndex >= scheduleInfo.size()) {
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleCardIndexOutOfBounds index=" + scheduleRouteIndex
                    + " size=" + scheduleInfo.size());
            return null;
        }
        Object routeOption = scheduleInfo.get(scheduleRouteIndex);
        return toRouteList(routeOption);
    }

    private String[] extractEndpointPorts(List<Route> routes) {
        ScheduleCarbonPayloadMapper.BuildResult build = ScheduleCarbonPayloadMapper.buildFromRoutes(routes, 1, 1);
        List<String> locations = build.getClimatiqRequest().getLocations();
        if (locations == null || locations.size() < 2) {
            return null;
        }
        return new String[] { locations.get(0), locations.get(locations.size() - 1) };
    }

    private Map<String, List> fetchSchedule(ServletContext ctx, HttpSession session,
            EcomBookingBeanRemote bookingObj, String origin, String delivery, DateRange dateRange) throws Exception {
        String feederRestriction = (String) ctx.getAttribute(GeneralConf.FEEDER_RESTRICTION);
        String strdefroute = (String) ctx.getAttribute(GeneralConf.ENABLE_DEFAULT_ROUTE_ONLY);
        String enableNonmatchingSchd = (String) ctx.getAttribute(GeneralConf.ENABLE_NON_MATCHING_SCHD);
        String fetchScheduleFromAPI = (String) ctx.getAttribute(GeneralConf.Fetch_Schedule_From_API);
        String userfuritcust = session == null ? null : (String) session.getAttribute("SESSION_USER_FURIT_CUSTOMER");

        String porttype = "N";
        String pol = origin;
        String pod = delivery;
        String origintype = Validation.checkNull(bookingObj.getPortType(origin));
        String deliverytype = Validation.checkNull(bookingObj.getPortType(delivery));
        if ("I".equalsIgnoreCase(origintype)) {
            pol = bookingObj.getseaportfrominland(origin);
            porttype = "Y";
        }
        if ("I".equalsIgnoreCase(deliverytype)) {
            pod = bookingObj.getseaportfrominland(delivery);
            porttype = "Y";
        }

        List<String> servList = null;
        Map<String, List<String>> resSrvcMap = (Map<String, List<String>>) ctx.getAttribute("CACHERESTRICTEDSERVICES");
        if (resSrvcMap != null) {
            servList = resSrvcMap.get(origin + "#" + delivery);
        }

        List<String> feederlis = null;
        if ("Y".equalsIgnoreCase(feederRestriction) && !("Y".equalsIgnoreCase(userfuritcust))) {
            feederlis = bookingObj.getFeederService();
        }

        String searchType = "";
        if ("Y".equalsIgnoreCase(enableNonmatchingSchd)) {
            searchType = "schedule";
        }

        Map ecomplugin = new HashMap();
        ecomplugin.put("Enable_Routing_Pol_Pod_Search", ctx.getAttribute(GeneralConf.Enable_Routing_Pol_Pod_Search));
        ecomplugin.put("Display_LCT_CUTOFF", ctx.getAttribute(GeneralConf.Display_LCT_CUTOFF));
        ecomplugin.put(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours,
                ctx.getAttribute(GeneralConf.Schedule_Ori_Dep_POL_Arr_Haulage_Hours));
        ecomplugin.put(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours,
                ctx.getAttribute(GeneralConf.Schedule_POL_Arr_POL_Dep_Haulage_Hours));
        ecomplugin.put(GeneralConf.Sort_Schedule_Based_On_Depdate,
                ctx.getAttribute(GeneralConf.Sort_Schedule_Based_On_Depdate));
        ecomplugin.put(GeneralConf.Refer_Routing_Transtime,
                ctx.getAttribute(GeneralConf.Refer_Routing_Transtime));
        ecomplugin.put(GeneralConf.REMOVE_SAME_VESSEL_DETAILS,
                ctx.getAttribute(GeneralConf.REMOVE_SAME_VESSEL_DETAILS));
        ecomplugin.put(GeneralConf.SCHEDULE_TRANS_LAYOVER_PORT_BASED,
                ctx.getAttribute(GeneralConf.SCHEDULE_TRANS_LAYOVER_PORT_BASED));

        if ("Y".equalsIgnoreCase(fetchScheduleFromAPI)) {
            String commonWSPath = (String) ctx.getAttribute(GeneralConf.WSURL);
            ScheduleAPIService scheduleAPIService = new ScheduleAPIService(commonWSPath, feederRestriction);
            CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleSource api fromdate=" + dateRange.fromDate
                    + " todate=" + dateRange.toDate);
            return scheduleAPIService.getScheduleFromAPI(origin, delivery, dateRange.fromDate, dateRange.toDate);
        }

        CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=scheduleSource ejb fromdate=" + dateRange.fromDate
                + " todate=" + dateRange.toDate);
        return bookingObj.geteBookRoutingInfo(origin, delivery, dateRange.fromDate, "", "", "",
                porttype, pol, pod, dateRange.fromDate, dateRange.toDate, origintype, deliverytype, servList,
                feederlis, strdefroute, searchType, ecomplugin);
    }

    private List<Route> selectRoute(ServletContext ctx, List scheduleOptions) {
        if (scheduleOptions == null || scheduleOptions.isEmpty()) {
            return null;
        }

        List<Route> defaultRoute = null;
        for (Object option : scheduleOptions) {
            List<Route> routeList = toRouteList(option);
            if (routeList == null || routeList.isEmpty()) {
                continue;
            }
            if (defaultRoute == null) {
                defaultRoute = routeList;
            }
            if (hasDefaultRoute(routeList)) {
                CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=defaultRouteMatched legs=" + routeList.size());
                return routeList;
            }
        }

        CarbonApiLogger.log(ctx, LOG_PREFIX + " workflow=defaultRouteFallback legs="
                + (defaultRoute == null ? 0 : defaultRoute.size()));
        return defaultRoute;
    }

    private boolean hasDefaultRoute(List<Route> routeList) {
        for (Route route : routeList) {
            if (route != null && "Y".equalsIgnoreCase(Validation.checkNull(route.getDefRoute()))) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private List<Route> toRouteList(Object option) {
        if (!(option instanceof List)) {
            return null;
        }
        return (List<Route>) option;
    }

    private DateRange buildScheduleDateRange(ServletContext ctx) throws Exception {
        String displayFormat = GeneralConf.getStrConfDefDate();
        String dbFormat = GeneralConf.getStrDbFormat();
        SimpleDateFormat displayParser = new SimpleDateFormat(displayFormat);
        SimpleDateFormat dbFormatter = new SimpleDateFormat(dbFormat);

        Calendar fromCalendar = Calendar.getInstance();
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.add(Calendar.DAY_OF_MONTH, 30);

        String fromdate = displayParser.format(fromCalendar.getTime());
        String todate = displayParser.format(toCalendar.getTime());

        String disablecurrentdateschedule = (String) ctx.getAttribute(GeneralConf.Disable_Current_Date_Schedule);
        Date fromDate = displayParser.parse(fromdate);
        Date toDate = displayParser.parse(todate);
        Date currentdate = new Date();
        boolean fromDateIsPast = currentdate.compareTo(fromDate) == 1;

        if ("Y".equalsIgnoreCase(disablecurrentdateschedule) && fromDateIsPast) {
            fromdate = dbFormatter.format(DateConversion_New.convertaddDays(fromDate, 1));
        } else {
            fromdate = DateConversion_New.convertDefaultDBFormatwithoutTime(fromdate);
        }

        todate = dbFormatter.format(DateConversion_New.convertaddDays(toDate, 1));
        return new DateRange(fromdate, todate);
    }

    private double parseWeight(String weightKg) {
        try {
            double parsed = Double.parseDouble(Validation.checkNull(weightKg).trim());
            return parsed > 0 ? parsed : 10000D;
        } catch (Exception ex) {
            return 10000D;
        }
    }

    private int parseQuantity(String quantity) {
        try {
            int parsed = Integer.parseInt(Validation.checkNull(quantity).trim());
            return parsed > 0 ? parsed : 1;
        } catch (Exception ex) {
            return 1;
        }
    }

    private CarbonScheduleResult failureResult(String message) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("error", message);
        return new CarbonScheduleResult(null, new ClimatiqResponse(500, body), null);
    }

    private static final class DateRange {
        private final String fromDate;
        private final String toDate;

        private DateRange(String fromDate, String toDate) {
            this.fromDate = fromDate;
            this.toDate = toDate;
        }
    }
}
