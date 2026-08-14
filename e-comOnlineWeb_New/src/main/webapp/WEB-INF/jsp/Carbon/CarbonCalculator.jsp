<%@ page contentType="text/html; charset=UTF-8" language="java" import="net.ecom.common.GeneralConf,com.google.gson.Gson,java.util.HashMap,java.util.Map" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%
    String clientcode = (String) session.getAttribute("clientcode");
    String contextRoot = request.getContextPath();
    int jsno = Integer.parseInt((String) getServletContext().getAttribute(GeneralConf.JAVASCRIPT_VERSION_NO));
    String loginCarbonType = (String) request.getAttribute("carbontype");
    String logincarbonpol = (String) request.getAttribute("logincarbonpol");
    String logincarbonpod = (String) request.getAttribute("logincarbonpod");
    String logincarbonqty = (String) request.getAttribute("logincarbonqty");
    String logincarbonweight = (String) request.getAttribute("logincarbonweight");
    Map<String, String> carbonPrefill = new HashMap<String, String>();
    if ("logincarbon".equalsIgnoreCase(loginCarbonType)) {
        carbonPrefill.put("pol", logincarbonpol != null ? logincarbonpol : "");
        carbonPrefill.put("pod", logincarbonpod != null ? logincarbonpod : "");
        carbonPrefill.put("qty", logincarbonqty != null ? logincarbonqty : "");
        carbonPrefill.put("weight", logincarbonweight != null ? logincarbonweight : "");
    }
    String carbonPrefillJson = new Gson().toJson(carbonPrefill);
%>
<script type="text/javascript" src="/Ecommaster/ports.js?<%=jsno%>"></script>
<script type="text/javascript" src="/Ecommaster/routingports.js?<%=jsno%>"></script>
<div class="card-header page_header fixed-top">
    <img class="header_icon_pages" src="<%=contextRoot%>/Design/assets/images/page_icons/schdule.png" alt=""/>
    <bean:message key="ecom.carboncalculator" bundle="<%=clientcode%>"/>
</div>
<div class="card card-body card-shadow page_bottom_content remove_print">
    <div class="container carbon-calculator-page">
        <div id="carbonCalculatorRoot" class="details_search carbon-content-wrapper">
            <div class="form-group">
                <div class="row">
                    <div class="col-md-4 offset-md-2 col-sm-4">
                        <div class="full-input portsnotes">
                            <label><bean:message key="ecom.carbon.origin" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" alt=""/>
                            <input type="text" id="carbonOrigin" class="required" autocomplete="off"
                                   placeholder="<bean:message key="ecom.carbon.port.placeholder" bundle="<%=clientcode%>"/>"/>
                            <input type="hidden" id="carbonOriginCode" value=""/>
                        </div>
                    </div>
                    <a href="javascript:void(0);" id="carbonSwapBtn" title="Swap origin and delivery">
                        <img class="swap_btn" src="Design/assets/images/page_icons/swap_btn.svg" alt="Swap"/>
                    </a>
                    <div class="col col-sm-4">
                        <div class="full-input portsnotes">
                            <label><bean:message key="ecom.carbon.delivery" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <img class="input_icon" src="Design/assets/images/page_icons/marker.svg" alt=""/>
                            <input type="text" id="carbonDelivery" class="required" autocomplete="off"
                                   placeholder="<bean:message key="ecom.carbon.port.placeholder" bundle="<%=clientcode%>"/>"/>
                            <input type="hidden" id="carbonDeliveryCode" value=""/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-md-4 offset-md-2 col-sm-4">
                        <div class="full-input">
                            <label><bean:message key="ecom.carbon.quantity" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <input type="number" min="1" step="1" id="carbonQuantity" class="required" value="1"/>
                        </div>
                    </div>
                    <div class="col-sm-4 m-l-30">
                        <div class="full-input">
                            <label><bean:message key="ecom.carbon.weight" bundle="<%=clientcode%>"/><span class="mandatory">*</span></label>
                            <input type="number" min="1" step="1" id="carbonWeight" class="required"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <div class="col-xs-12 col-sm-4 swap_input_width"></div>
                    <div class="col-xs-12 col-sm-4 swap_input_width m-l-30">
                        <button type="button" id="carbonCalcBtn" class="btn btn-info btn_block_custom">
                            <span id="carbonSpinner" class="carbon-spinner" style="display:none;"></span>
                            <span id="carbonBtnText"><bean:message key="ecom.carbon.calculate" bundle="<%=clientcode%>"/></span>
                        </button>
                    </div>
                </div>
            </div>
            <div id="carbonErrorBox" class="alert alert-danger carbon-error" style="display:none;"></div>
        </div>
    </div>
</div>

<div id="carbonCalcMessage" class="page_bottom_content remove_print" style="display:none;" aria-live="polite">
    <div class="col-sm-12">
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-10">
                <div class="box_no_rls">
                    <div class="text-center">
                        <h2 class="txt_no_msg m-b-5" id="carbonCalcMessageText"></h2>
                    </div>
                </div>
            </div>
            <div class="col-sm-1"></div>
        </div>
    </div>
</div>

<div class="carbon-calculator-lower remove_print">
    <div class="container-fluid carbon-calculator-lower-inner carbon-calculator-lower-inner--align">
        <div id="carbonResults" class="carbon-results carbon-results--stack" style="display:none;">
            <div class="carbon-results-panel">
                <div class="carbon-results-inner">
                    <div class="carbon-route-label" id="carbonRouteLabel"></div>
                    <div class="carbon-metrics-row">
                        <div class="carbon-metric">
                            <div class="carbon-metric-label"><bean:message key="ecom.carbon.total" bundle="<%=clientcode%>"/></div>
                            <div class="carbon-metric-value" id="carbonCo2Total">—</div>
                            <div class="carbon-metric-sub" id="carbonCo2TotalSub"></div>
                        </div>
                        <div class="carbon-metric">
                            <div class="carbon-metric-label"><bean:message key="ecom.carbon.ttw" bundle="<%=clientcode%>"/></div>
                            <div class="carbon-metric-value" id="carbonCo2Ttw">—</div>
                            <div class="carbon-metric-sub" id="carbonCo2TtwSub"></div>
                        </div>
                        <div class="carbon-metric">
                            <div class="carbon-metric-label"><bean:message key="ecom.carbon.wtt" bundle="<%=clientcode%>"/></div>
                            <div class="carbon-metric-value" id="carbonCo2Wtt">—</div>
                            <div class="carbon-metric-sub" id="carbonCo2WttSub"></div>
                        </div>
                        <div class="carbon-metric">
                            <div class="carbon-metric-label"><bean:message key="ecom.carbon.distance" bundle="<%=clientcode%>"/></div>
                            <div class="carbon-metric-value" id="carbonDistance">—</div>
                            <div class="carbon-metric-sub">km</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row carbon-info-sections carbon-info-sections--wide" aria-label="Eco calculator information">
            <div class="col-lg-12 col-md-12 col-sm-12 carbon-info-col">
                <div class="details_panel card card-body card-shadow carbon-info-card carbon-info-card--purpose">
                    <div class="carbon-info-card-header">
                        <h3 class="carbon-info-heading">
                            <span class="carbon-info-heading-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <circle cx="10" cy="10" r="9" stroke="#009ace" stroke-width="1.5"/>
                                    <path d="M10 9V14" stroke="#009ace" stroke-width="1.5" stroke-linecap="round"/>
                                    <circle cx="10" cy="6.25" r="1" fill="#009ace"/>
                                </svg>
                            </span>
                            Purpose of our Eco Calculator
                        </h3>
                    </div>
                    <div class="carbon-info-body">
                        <span class="lbl_clr_txt">As part of our sustainability strategy and commitment to reducing greenhouse gas emissions, we developed this calculator to support customers and partners in managing their cargo-related carbon footprint.</span>
                            <span class="lbl_clr_txt">This tool provides an easy way to estimate <strong>CO<sub>2</sub> emissions for shipments</strong>, helping stakeholders make informed decisions toward greener transport solutions.</span>
                            <span class="lbl_clr_txt">The calculation is based on globally recognized methodologies such as the <strong>GLEC Framework</strong> and <strong>Clean Cargo guidelines</strong>, ensuring transparency and accuracy in reporting.</span>
                    </div>
                </div>
            </div>
       
        </div>
        
        <div class="row carbon-info-sections carbon-info-sections--wide" aria-label="Eco calculator information">
           
            <div class="col-lg-12 col-md-12 col-sm-12 carbon-info-col">
                <div class="details_panel card card-body card-shadow carbon-info-card carbon-info-card--methodology">
                    <div class="carbon-info-card-header">
                        <h3 class="carbon-info-heading">
                            <span class="carbon-info-heading-icon" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M5 3.5H12L15.5 7V16.5C15.5 17.05 15.05 17.5 14.5 17.5H5.5C4.95 17.5 4.5 17.05 4.5 16.5V4.5C4.5 3.95 4.95 3.5 5 3.5Z" stroke="#009ace" stroke-width="1.5" stroke-linejoin="round"/>
                                    <path d="M12 3.5V7H15.5" stroke="#009ace" stroke-width="1.5" stroke-linejoin="round"/>
                                    <path d="M7 10.5H13" stroke="#009ace" stroke-width="1.5" stroke-linecap="round"/>
                                    <path d="M7 13.5H11" stroke="#009ace" stroke-width="1.5" stroke-linecap="round"/>
                                </svg>
                            </span>
                            Methodology
                        </h3>
                    </div>
                    <div class="carbon-info-body">
                        <span class="lbl_clr_txt">Our calculator estimates emissions based on actual route distances, vessel type, and fuel consumption using standards such as <strong>IMO</strong> and the <strong>GLEC Framework</strong>. Emission factors for fuel types are applied to calculate CO<sub>2</sub> equivalent per shipment, considering cargo weight and distance traveled. The methodology covers only ocean-leg emissions, focusing on tank-to-wheel and optionally well-to-wheel for upstream impacts. Please note that this is an estimate, and actual emissions may vary depending on voyage-specific factors such as weather conditions, port congestion, and operational variations.</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .carbon-calculator-page {
        padding-bottom: 0;
    }
    /* Same rhythm as eCommSchedules.jsp: outer card = white panel; swap uses global .swap_btn only */
    #carbonCalculatorRoot.carbon-content-wrapper {
        background: transparent;
        border: none;
        border-radius: 0;
        box-shadow: none;
        margin-left: auto;
        margin-right: auto;
        max-width: 100%;
        padding: 8px 4px 4px;
        position: relative;
    }
    @media (min-width: 768px) {
        #carbonCalculatorRoot.carbon-content-wrapper {
            padding: 12px 8px 4px;
        }
    }
    .carbon-calculator-page .details_search {
        margin-bottom: 0;
    }
    #carbonCalculatorRoot .form-group {
        margin-bottom: 1rem;
    }
    #carbonCalculatorRoot #carbonSwapBtn {
        text-decoration: none;
    }
    #carbonCalculatorRoot #carbonSwapBtn:hover,
    #carbonCalculatorRoot #carbonSwapBtn:focus {
        outline: none;
        text-decoration: none;
    }
    /* Match Schedules mobile gutter reset (ecom.css uses 980px for #searchbylocation) */
    @media only screen and (max-width: 980px) {
        #carbonCalculatorRoot .form-group .m-l-30 {
            margin-left: 0 !important;
        }
    }
    #carbonCalculatorRoot input[type="number"] {
        -moz-appearance: textfield;
    }
    #carbonCalculatorRoot input[type="number"]::-webkit-outer-spin-button,
    #carbonCalculatorRoot input[type="number"]::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }
    .carbon-spinner {
        border: 2px solid rgba(255, 255, 255, 0.35);
        border-radius: 50%;
        border-top-color: #fff;
        display: inline-block;
        height: 14px;
        margin-right: 8px;
        vertical-align: middle;
        width: 14px;
        animation: carbon-spin 0.6s linear infinite;
    }
    @keyframes carbon-spin { to { transform: rotate(360deg); } }
    .carbon-error {
        border-radius: 8px;
        margin: 18px auto 0;
        max-width: 100%;
        text-align: left;
    }
    @media (min-width: 768px) {
        .carbon-error {
            max-width: 90%;
        }
    }
    .carbon-calculator-lower {
        margin-top: 20px;
        padding-bottom: 32px;
    }
    .carbon-calculator-lower-inner.carbon-calculator-lower-inner--align {
        margin-left: auto;
        margin-right: auto;
        max-width: 100%;
        padding-left: 18px;
        padding-right: 18px;
        width: 100%;
    }
    @media (min-width: 992px) {
        .carbon-calculator-lower-inner.carbon-calculator-lower-inner--align {
            max-width: 96%;
            padding-left: 24px;
            padding-right: 24px;
        }
    }
    @media (min-width: 1200px) {
        .carbon-calculator-lower-inner.carbon-calculator-lower-inner--align {
            max-width: 1420px;
            padding-left: 32px;
            padding-right: 32px;
        }
    }
    @media (min-width: 1400px) {
        .carbon-calculator-lower-inner.carbon-calculator-lower-inner--align {
            max-width: 1520px;
        }
    }
    .carbon-results.carbon-results--stack {
        margin-bottom: 24px;
        margin-top: 0;
        text-align: left;
        width: 100%;
    }
    .carbon-results-panel {
        background: #fff;
        border: 1px solid #e2e8f0;
        border-radius: 14px;
        box-shadow:
            0 1px 2px rgba(15, 23, 42, 0.04),
            0 10px 28px rgba(15, 23, 42, 0.06);
        overflow: hidden;
        padding: 0;
        position: relative;
        width: 100%;
    }
    .carbon-results-panel::before {
        background: linear-gradient(180deg, #009ace 0%, #38bdf8 100%);
        content: "";
        height: 100%;
        left: 0;
        position: absolute;
        top: 0;
        width: 4px;
    }
    .carbon-results-inner {
        margin-left: auto;
        margin-right: auto;
        max-width: 100%;
        padding: 32px 40px 30px 32px;
        text-align: left;
        width: 100%;
    }
    .carbon-route-label {
        border-bottom: 1px solid #eef2f7;
        color: #0f172a;
        font-size: 17px;
        font-weight: 600;
        letter-spacing: 0.01em;
        line-height: 1;
        margin-bottom: 28px;
        padding-bottom: 4px;
    }
    .carbon-metrics-row {
        align-items: stretch;
        display: flex;
        flex-wrap: wrap;
        gap: 16px;
        justify-content: space-between;
        width: 100%;
    }
    .carbon-metric {
        background: linear-gradient(180deg, #f8fafc 0%, #ffffff 55%);
        border: 1px solid #e8eef5;
        border-radius: 12px;
        box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04);
        flex: 1 1 calc(25% - 12px);
        min-height: 158px;
        min-width: 0;
        padding: 24px 26px 10px;
        transition: border-color 0.18s ease, box-shadow 0.18s ease;
    }
    .carbon-metric:hover {
        border-color: #c5e6f3;
        box-shadow: 0 4px 14px rgba(0, 154, 206, 0.12);
    }
    .carbon-metric-label {
        color: #64748b;
        font-size: 11px;
        font-weight: 600;
        letter-spacing: 0.08em;
        margin-bottom: 10px;
        text-transform: uppercase;
    }
    .carbon-metric-value {
        color: #0c4a6e;
        font-size: 25px;
        font-weight: 700;
        letter-spacing: -0.02em;
        line-height: 1.22;
    }
    .carbon-metric-sub,
    .carbon-unit,
    .carbon-grams {
        color: #64748b;
        font-size: 12px;
        line-height: 1.45;
        margin-top: 8px;
    }
    .carbon-grams { margin-top: 4px; }
    @media (max-width: 991px) {
        .carbon-metric {
            flex: 1 1 calc(50% - 9px);
        }
    }
    @media (max-width: 767px) {
        .carbon-calculator-lower {
            margin-top: 16px;
            padding-bottom: 24px;
        }
        .carbon-results-inner {
            padding: 26px 30px 22px 24px;
        }
        .carbon-route-label {
            font-size: 15px;
            line-height: 1.5;
            margin-bottom: 22px;
            padding-bottom: 18px;
        }
        .carbon-metrics-row {
            gap: 12px;
        }
        .carbon-metric {
            flex: 1 1 calc(50% - 6px);
            min-height: 128px;
            padding: 15px 19px 16px 22px;
        }
        .carbon-metric-value {
            font-size: 21px;
        }
    }
    @media (max-width: 479px) {
        .carbon-metrics-row {
            flex-wrap: nowrap;
            gap: 10px;
            overflow-x: auto;
            -webkit-overflow-scrolling: touch;
            padding-bottom: 8px;
        }
        .carbon-metric {
            flex: 0 0 74%;
            min-width: 152px;
        }
    }
    .carbon-info-sections {
        background: transparent;
        border-top: none;
        margin: 32px 0 0;
        padding: 0;
        text-align: left;
    }
    .carbon-info-sections--wide {
        margin-left: -10px;
        margin-right: -10px;
        width: calc(100% + 20px);
    }
    .carbon-info-sections--wide .carbon-info-col {
        padding-left: 10px;
        padding-right: 10px;
    }
    .carbon-info-sections .carbon-info-col {
        display: flex;
        margin-bottom: 22px;
    }
    .carbon-info-sections .carbon-info-col:last-child {
        margin-bottom: 0;
    }
    @media (min-width: 768px) {
        .carbon-info-sections .carbon-info-col {
            margin-bottom: 0;
        }
    }
    .carbon-info-sections .carbon-info-card.details_panel {
        background: #fff;
        border: 1px solid #e2e8f0;
        border-radius: 14px;
        box-shadow:
            0 1px 2px rgba(15, 23, 42, 0.04),
            0 10px 28px rgba(15, 23, 42, 0.06);
        display: flex;
        flex-direction: column;
        height: 88%;
        margin-bottom: 0;
        min-height: 164px;
        overflow: hidden;
        padding: 0;
        position: relative;
        transition: border-color 0.2s ease, box-shadow 0.2s ease;
        width: 100%;
    }
    .carbon-info-sections .carbon-info-card.details_panel::before {
        background: linear-gradient(180deg, #009ace 0%, #38bdf8 100%);
        content: "";
        height: 100%;
        left: 0;
        position: absolute;
        top: 0;
        width: 4px;
    }
    .carbon-info-sections .carbon-info-card.details_panel:hover {
        border-color: #d0dde9;
        box-shadow:
            0 2px 4px rgba(15, 23, 42, 0.05),
            0 14px 36px rgba(15, 23, 42, 0.09);
    }
    .carbon-info-card--purpose.details_panel {
        background: linear-gradient(180deg, #f8fdfb 0%, #ffffff 42%);
    }
    .carbon-info-card--methodology.details_panel {
        background: linear-gradient(180deg, #f7fbfd 0%, #ffffff 42%);
    }
    .carbon-info-card-header {
        border-bottom: 1px solid #eef2f7;
        padding: 11px 15px 13px 21px;
        position: relative;
    }
    .carbon-info-card-header::before {
        content: "";
        height: 3px;
        left: 24px;
        position: absolute;
        right: 24px;
        top: 0;
    }
   
    .carbon-info-card--purpose .carbon-info-heading,
    .carbon-info-card--methodology .carbon-info-heading {
        align-items: center;
        border-bottom: none;
        display: flex;
        font-size: 18px;
        font-weight: 700;
        gap: 10px;
        letter-spacing: 0.01em;
        line-height: 1.4;
        margin: 0;
        padding: 0;
    }
    .carbon-info-heading-icon {
        display: inline-flex;
        flex-shrink: 0;
        line-height: 0;
    }
    .carbon-info-body strong {
        color: #334155;
        font-weight: 700;
    }
    .carbon-info-card--purpose .carbon-info-heading {
        color: #0f5132;
    }
    .carbon-info-card--methodology .carbon-info-heading {
        color: #0c4a6e;
    }
    .carbon-info-body {
        color: #475569;
        flex: 1 1 auto;
        font-size: 10px;
        padding: 18px 33px 21px 26px;
    }
    .carbon-info-card--purpose .carbon-info-body {
        color: #3f5364;
    }
    .carbon-info-card--methodology .carbon-info-body {
        color: #3f5364;
    }
    .carbon-calculator-lower .lbl_clr_txt {
        color: #3f5364 !important;
    }

    .carbon-info-body sub {
        font-size: 0.75em;
    }
    @media (max-width: 767px) {
       
        .carbon-info-sections .carbon-info-card.details_panel {
            min-height: 198px;
        }
        .carbon-info-card-header {
            padding: 20px 20px 18px 26px;
        }
        .carbon-info-card-header::before {
            left: 26px;
            right: 20px;
        }
        .carbon-info-card--purpose .carbon-info-heading,
        .carbon-info-card--methodology .carbon-info-heading {
            font-size: 17px;
        }
        .carbon-info-body {
            font-size: 10px;
            padding: 17px 15px 17px 20px;
        }
    }
</style>

<script type="text/javascript">
    var carbonCalculatorEndpoint = '<%=contextRoot%>/CarbonScheduleAjax';
    var carbonCalculatorMessages = {
        calculate: '<bean:message key="ecom.carbon.calculate" bundle="<%=clientcode%>"/>',
        calculating: '<bean:message key="ecom.carbon.calculating" bundle="<%=clientcode%>"/>',
        originDeliveryRequired: '<bean:message key="ecom.carbon.error.originDelivery" bundle="<%=clientcode%>"/>',
        diffPort: '<bean:message key="ecom.msg.diffport" bundle="<%=clientcode%>"/>',
        quantityRequired: '<bean:message key="ecom.carbon.error.quantity" bundle="<%=clientcode%>"/>',
        weightRequired: '<bean:message key="ecom.carbon.error.weight" bundle="<%=clientcode%>"/>',
        requestFailed: '<bean:message key="ecom.carbon.error.request" bundle="<%=clientcode%>"/>',
        calculationFailed: '<bean:message key="ecom.carbon.error.calculation" bundle="<%=clientcode%>"/>',
        endpointMissing: '<bean:message key="ecom.carbon.error.endpointMissing" bundle="<%=clientcode%>"/>',
        apiNotConfigured: '<bean:message key="ecom.carbon.error.apiNotConfigured" bundle="<%=clientcode%>"/>',
        portDataMissing: '<bean:message key="ecom.carbon.error.portDataMissing" bundle="<%=clientcode%>"/>',
        typeaheadFailed: '<bean:message key="ecom.carbon.error.typeaheadFailed" bundle="<%=clientcode%>"/>',
        scriptMissing: '<bean:message key="ecom.carbon.error.scriptMissing" bundle="<%=clientcode%>"/>'
    };
    function startCarbonCalculatorPage() {
        if (typeof window.jQuery === 'undefined' && typeof window.$ === 'undefined') {
            $('#carbonCalcMessageText').text(carbonCalculatorMessages.scriptMissing);
            $('#carbonCalcMessage').show();
            return;
        }
        if (typeof window.initCarbonCalculator !== 'function') {
            $('#carbonCalcMessageText').text(carbonCalculatorMessages.scriptMissing);
            $('#carbonCalcMessage').show();
            return;
        }
        window.initCarbonCalculator();
        var _carbonPrefill = <%= carbonPrefillJson %>;
        if (_carbonPrefill && (_carbonPrefill.pol || _carbonPrefill.pod) && typeof window.applyCarbonLoginPrefill === 'function') {
            window.applyCarbonLoginPrefill(_carbonPrefill.pol, _carbonPrefill.pod, _carbonPrefill.qty, _carbonPrefill.weight);
        }
        var _autoRun = _carbonPrefill && $.trim(_carbonPrefill.pol || '') && $.trim(_carbonPrefill.pod || '')
            && $.trim(_carbonPrefill.weight || '') && $.trim(_carbonPrefill.qty || '');
        if (_autoRun && typeof window.triggerCarbonCalculatorCalculate === 'function') {
            setTimeout(function () {
                window.triggerCarbonCalculatorCalculate();
            }, 450);
        }
    }
</script>
<script type="text/javascript" src="js/carbon-calculator.js?<%=jsno%>"></script>
<script type="text/javascript">
    $(document).ready(function () {
        startCarbonCalculatorPage();
    });
</script>
