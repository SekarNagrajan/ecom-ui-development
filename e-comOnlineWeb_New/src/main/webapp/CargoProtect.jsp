<%-- 
    Document   : CargoProtect
    Created on : Jun 18, 2022, 11:56:07 PM
    Author     : gobinath.t
--%>

<%@page import="net.ecom.common.GeneralConf"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%
    String clientcode = (String) session.getAttribute("clientcode");
    String carriercode = (String) getServletContext().getAttribute(GeneralConf.CARRIER_CODE);
    String SelectedLanguage = (String) session.getAttribute("SelectedLanguage");
%>
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/schdule.png" /> <bean:message key="ecom.cargoprotect.insurance" bundle="<%=clientcode%>"/>  </div>
<div class="" id="cargoprotect">
    <div class="col-sm-12 " >
        <div class="col-sm-1"></div>
        <div class="card card-body card-shadow page_bottom_content remove_print">
            <% if ("CH".equalsIgnoreCase(SelectedLanguage)) {%>
            <div class="col-sm-12 ">
                <div style="border: 2px solid;padding: 10px;">
                    <div style="padding: 30px;">
                        <span style="display: flex;"> <h3> 货运保障 </h3><h4 style="margin-top: 2px;">&nbsp;（由瑞再企商提供支持）</h4></span>
                        <!-- <div style="float: right;top: 4px;position: absolute;right: 67px;">
                             <img alt="logo" src="/Ecommaster/cargoprotect.png" />
                         </div>-->
                        <h5>您的货物是否受到保护？</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>
                                阿联酋航运（ESL）将竭尽所能确保您的货物安全抵达且状况良好。
                            </p>

                            <p>
                                但有些事情超出我们的控制范围，您的货物在运输和储存过程中面临不同的风险和危险，这可能会对您的业务造成不利的财务影响。
                            </p>

                            <p>
                                为此，阿联酋航运（ESL）与瑞再企商合作为您提供基于全数字化的简便的线上货运保险解决方案。
                            </p>
                            <p>
                                货运保障将向所有我们 E-Sea and的用户提供自起运地至目的地的保障
                            </p>

                        </div>
                        <div class="col-sm-12 remove_padding">
                            <video controls style="width: auto;max-height: 500px;justify-content: center;margin-left: auto;margin-right: auto;display: block;">
                                <source src="https://media10.simplex.tv/content/170/5179/195041/simvid_1.mp4" >
                            </video>
                        </div>
                        <h5 class="text-center" style="margin-top: 15px;">数字化货运保险，保护您的货物。即时生效，放心无忧。</h5>
                        <div class="col-sm-12" style="display: flex;">
                            <div class="col-sm-1"></div>
                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/SimpleandConvenient.jpg"/>
                                <p>简单快捷，通过E-Sea享受一流的货运保险</p>
                            </div>

                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/InstantQuotation.jpg"/>
                                <p>仅需几次点击，立即为货物提供保障</p>
                            </div>

                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/FullyDigitalServicing.jpg"/>
                                <p>从报价到出单和理赔全程数字化服务体验</p>
                            </div>

                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/AllInformation.jpg"/>
                                <p>任何时间都能从在同一平台获取所有与航运和保险相关的信息</p>
                            </div>

                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/ZeroEffort.jpg"/>
                                <p>轻松零成本管理您的保险保障</p>
                            </div>

                        </div>
                        <h5>承保范围详情</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>
                                主要承保范围基于标准国际通用条款，协会货物条款 (A)，这是基于一切险的保障（涵盖所有风险，不包括除外条款中列明的风险） 。
                            </p>

                            <p>
                                该保障为由阿联酋航运承运并且已签发提单上列明的货物提供保障，该保障自货物从装载作业的指定出发地举升离开地面开始， 在正常运输过程中持续有效，包括正常运输途中的临时储存，直到货物放置在指定目的地的地面上， 以上在任何时候都受协会货物条款（A）对于运输期限的相关条款约束。
                            </p>

                            <p>
                                保单将根据不同类型货物的申报金额赔偿每次运输最高达 1000 万美元的赔偿金。
                            </p>

                            <p>
                                本保障承保由全封闭集装箱装运的货物，不承保散货或超大/超限 (OOG) 货物。
                            </p>

                            <p>
                                本保单不承保以下货物：特定货物、在途现金（现金、纸币、硬币、储值 卡、流通票据、现金卡和类似物品）、债券、未加工的贵金属（金、银和类似物品）、文件、 家畜/血肉/鱼获，血液制品，血浆， 脐带血干细胞、生物、化学和集束武器， 杀伤人员地雷、放射性/核材料武器和 弹药、集装箱和动力煤。
                            </p>

                        </div>

                        <h5>简便的投保</h5>

                        <div class="col-sm-12 remove_padding">
                            <p>
                                货运保障已经内嵌在我们的预订平台E-Sea，您可以通过平台仅需几秒钟便能完成投保。预订舱位的同时，添加您的货物运输金额即可获得保障。
                            </p>
                            <p>
                                如果您的货物不幸遭受损失或损坏或被分摊共同海损或救助费用，您可通过我们的第一时间损失通知（FNOL）电子工具轻松便捷地申报索赔...
                            </p>
                        </div>

                        <div class='col-sm-12'>
                            <h3 style="text-align: center;margin-left: auto;margin-right: auto;display: block;">如何为您的货物投保货运保险</h3>
                            <video controls style="width: auto;max-height: 500px;justify-content: center;margin-left: auto;margin-right: auto;display: block;">
                                <source src="https://media10.simplex.tv/content/170/5179/195042/simvid_1.mp4" >
                            </video>
                        </div>
                        <div class='col-sm-12'>
                            <h3 style="text-align: center;margin-left: auto;margin-right: auto;display: block;">发生损失时如何通知保险公司</h3>
                            <video controls style="width: auto;max-height: 500px;justify-content: center;margin-left: auto;margin-right: auto;display: block;">
                                <source src="https://media10.simplex.tv/content/170/5179/195043/simvid_1.mp4" >
                            </video>
                        </div>

                        <h5>产品适用</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>货运保障目前仅适用于位于中国大陆和香港的 ESL 客户。我们计划在不久的将来扩展到其他市场。保障目前仅开放单票运输，暂未提供年度预约保单及开口保单保障。</p>
                        </div>

                        <h5>合作伙伴及中介</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>ESL集团旗下阿联酋航运代理（中国）有限公司是瑞士再保险的持牌中介，香港保险业监管局注册代理牌照号为： GA1036 。</p>
                            <p>货物运输保险解决方案将由瑞士再保险集团旗下的瑞再企商承保，该集团是一家领先且高度多元化的全球再保险/保险公司，于 1863 年在苏黎世（瑞士）成立。</p>
                        </div>

                        <h5>适用法律和管辖权</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>本保单之条款及细则受香港法律管辖。任何相关争议均应专属香港法院管辖。</p>
                        </div>

                        <h5>常见问题</h5>
                        <div class="col-sm-12 remove_padding">
                            <div class="col-sm-12">
                                <h6>1) 为什么我需要为自己的货物投保？</h6>
                                <p>货物运输过程中面临着各种风险，不仅可能会损坏您的货物，还可能导致您的业务运营中断，从而给您的公司带来重大财务损失。此外，根据国际贸易术语，托运人在某些情况下也有义务为货物投保。</p>
                            </div>


                            <div class="col-sm-12">
                                <h6>2) 货物的损失或损坏不是应由承运人承担吗？ </h6>
                                <p>承运人可提供基于责任险的赔偿，但额度有限，可能远远低于您所运输商品的实际商业价值。</p>
                            </div>

                            <div class="col-sm-12">
                                <h6>3) 我应该为我的商品投保多少金额？</h6>
                                <p>在货物保单中，保额按照发票金额和运费加上预订时可能未知的附带运输费用的预估区间（通常为 10%）进行估值。</p>
                                <a href="cargoprotect.do?type=FAQ">详细回复请见我们的FAQ部分.</a>
                            </div>  
                            <div class="col-sm-12">
                                <a href="Accesslogin.do?type=contactus">如果您对我们的货运保险有任何疑问或 需要更多信息，请联系我们。</a>  
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <% } else {%>
            <div class="col-sm-12 ">
                <div style="border: 2px solid;padding: 10px;">
                    <div style="padding: 30px;">
                        <span style="display: flex;"> <h3> CARGO PROTECT </h3><h4 style="margin-top: 2px;"> &nbsp;(powered by Swiss Re Corporate Solutions)</h4></span>
                        <!--<div style="float: right;top: 4px;position: absolute;right: 67px;">
                            <img alt="logo" src="/Ecommaster/cargoprotect.png" />
                        </div>-->
                        <h5>Is your shipment protected?</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>
                                Here at Emirates Shipping Line we do everything in our power to make sure your cargo arrives safely and in good condition.
                            </p>

                            <p>
                                But there are things beyond our control and your cargo is exposed to different risks and hazards during transportation and storage, which can lead to adverse financial impact on your business.
                            </p>

                            <p>
                                That's  why Emirates Shipping Line introduces Cargo Protect, an easy-access digital cargo insurance brought to you in partnership with commercial insurer Swiss Re Corporate Solutions. 
                            </p>
                            <p>
                                Cargo Protect is available for all of our customers who are users of E-Sea and will protect your cargo while in transit from origin to destination.
                            </p>


                        </div>
                        <div class="col-sm-12 remove_padding">
                            <video  controls style="width: auto;max-height: 500px;justify-content: center;margin-left: auto;margin-right: auto;display: block;">
                                <source src="https://media10.simplex.tv/content/170/5179/195038/simvid_1.mp4" >
                            </video>
                        </div>
                        <h5 class="text-center" style="margin-top: 15px;"> Digital Cargo Insurance to protect your shipment. Instant and hassle free.</h5>
                        <div class="col-sm-12" style="display: flex;">
                            <div class="col-sm-1"></div>
                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/SimpleandConvenient.jpg"/>
                                <p>Simple and convenient access to best-in-class cargo insurance directly through E-Sea</p>
                            </div>

                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/InstantQuotation.jpg"/>
                                <p>Instant quotation and coverage for your shipment within just a few clicks</p>
                            </div>

                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/FullyDigitalServicing.jpg"/>
                                <p>Fully digital servicing from quoting to policy issuance, including easy to use claims notification</p>
                            </div>

                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/AllInformation.jpg"/>
                                <p>All shipping- and insurance-related information accessible on one single platform at any point in time</p>
                            </div>

                            <div class="col-sm-2">
                                <img src="Design/assets/images/cargoprotectimage/ZeroEffort.jpg"/>
                                <p>Zero effort and cost to manage your insurance provision</p>
                            </div>

                        </div>
                        <h5> What does Cargo Protect cover?</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>
                                The main coverage is based on standard international wording, Institute Cargo Clause (A), which is an All-risks cover (covering all perils except as explicitly excluded).
                            </p>

                            <p>
                                The Policy provides cover for shipments while on transit under the Emirates Shipping Lines Bill of Lading and commences when the goods lifted from the ground at the place of the named departure for the loading operations, continues during the ordinary course of transit, including incidental storage and remains in force until the goods are placed on the ground in the named place of destination, subject at all times to the Duration of transit provision of the Institute Cargo Clause(A).
                            </p>

                            <p>
                                The Policy pays indemnity based on declared full value of the cargo for up to $10M per shipment for selected cargo types.
                            </p>

                            <p>
                                The product covers for cargoes which are shipped in fully enclosed containers and will not cover for bulk or oversized/out of gauge (OOG) cargoes.
                            </p>

                            <p>
                                The Policy does not cover for the following cargoes: Specie, Cash-In-Transit (Cash, Currency Notes, Coins, Store Value Cards, Negotiable Instruments, Cash Cards and similar), Bonds, Raw Precious Metals (Gold, Silver and similar), Documents, Livestock/Bloodstock/Fish Catch, Blood Products, Blood Plasma, Cord Blood Stem Cell, Biological, chemical and cluster weapons, anti-personnel mines, radioactive/Nuclear Material Weapons and Ammunition, Container and Thermal Coal. 
                            </p>

                        </div>

                        <h5> Cargo Insurance made easy</h5>

                        <div class="col-sm-12 remove_padding">
                            <p>
                                Cargo Protect is fully embedded within our booking platform E-Sea and gives you access to best in class cargo insurance cover in a matter of seconds. Simply book your shipment, add your shipments' value and you're covered. 
                            </p>
                            <p>
                                In the unfortunate event of a loss or damage to your cargo or receipt of general acerage or salvage charges, conveniently file a claim using using our electronic First Notice of Loss facility.
                            </p>
                        </div>
                        <div class='col-sm-12'>
                            <h3 style="text-align: center;margin-left: auto;margin-right: auto;display: block;">How to book your cargo insurance</h3>
                            <video controls style="width: auto;max-height: 500px;justify-content: center;margin-left: auto;margin-right: auto;display: block;">
                                <source src="https://media10.simplex.tv/content/170/5179/195039/simvid_1.mp4" >
                            </video>
                        </div>
                        <div class='col-sm-12'>
                            <h3 style="text-align: center;margin-left: auto;margin-right: auto;display: block;">How to report a loss</h3>
                            <video controls style="width: auto;max-height: 500px;justify-content: center;margin-left: auto;margin-right: auto;display: block;">
                                <source src="https://media10.simplex.tv/content/170/5179/195040/simvid_1.mp4" >
                            </video>
                        </div>

                        <h5>Product Availability</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>Cargo Protect is currently available only for ESL customers who are based in China and Hong Kong. Expansion to other markets is planned for the near future. The cover is provided on a per shipment basis and is not offered on an annual or open cover setup at the moment.</p>
                        </div>

                        <h5>Partnership and Intermediation</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>Emirates Shipping Agencies (China) Limited, ESL's own agent, is a licensed insurance intermediary of Swiss Re International SE, Hong Kong Branch (HKIA's insurance agency licence no. GA1036). </p>
                            <p>The Cargo insurance is underwritten by Swiss Re International SE, Hong Kong Branch, a part of the Swiss Re Group founded in Zurich (Switzerland) in 1863, which is a leading and highly diversified global re/insurer.</p>
                        </div>

                        <h5>Governing law and jurisdiction</h5>
                        <div class="col-sm-12 remove_padding">
                            <p>The terms and conditions of the Policy shall be governed by Hong Kong laws. Any related dispute shall be submitted exclusively to the jurisdiction of Hong Kong courts. </p>
                        </div>

                        <h5>Frequently Asked Questions</h5>
                        <div class="col-sm-12 remove_padding">
                            <div class="col-sm-12">
                                <h6>1) Why do I need to secure own insurance for my cargo?</h6>
                                <p>Cargo transport operations are exposed to various risks which cannot only damage your goods but also result in interruptions to your business operations and considerable financial losses to your company. Furthermore, in some cases, the shipper is also obliged to secure cargo insurance for the shipment as per Incoterms.</p>
                            </div>

                            <div class="col-sm-12">
                                <h6>2) Aren't loss or damage to my cargo covered by the carrier?</h6>
                                <p>The compensation provided by the carrier is based on their liability cover which is limited and may be much lower than the actual commercial value of your shipped goods.</p>
                            </div>

                            <div class="col-sm-12">
                                <h6>3) How much should I insure my goods for?</h6>
                                <p>In a cargo policy, the sum insured is valued at invoice cost and freight charges plus a buffer for incidental shipping expenses which may not be known at the time of booking, usually 10%.</p>
                                <a href="cargoprotect.do?type=FAQ">Please visit our full FAQ section for more information.</a>  
                            </div>
                            <div class="col-sm-12">
                                <a href="Accesslogin.do?type=contactus">If you have any questions or need further information on our Cargo Insurance, please contact us.</a>  
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <%}%>
        </div>
        <div class="col-sm-1"></div>
    </div>
</div>
<style >
    #cargoprotect .faq_header {
        background-color: rgb(236, 236, 236) !important;
        cursor:pointer !important;
        padding: .45rem 1.25rem !important;
    }
    #cargoprotect .faq_box{
        border: 1px solid rgba(0,0,0,.125);
        border-radius: calc(.25rem - 1px) calc(.25rem - 1px) 0 0;
    }
    #cargoprotect .remove_padding {
        padding-left: 0px;
        padding-right: 0px;
    }
    #cargoprotect h2,#cargoprotect h3,#cargoprotect h4,#cargoprotect h5,#cargoprotect h6,#cargoprotect a,#cargoprotect video {
        padding: 10px 0px;
        font-weight: bold;
    }
    #cargoprotect p {
        font-size: 15px;
    }

</style>





