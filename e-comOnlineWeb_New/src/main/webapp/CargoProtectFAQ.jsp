<%-- 
    Document   : CargoProtectFAQ
    Created on : Jun 19, 2022, 9:15:29 PM
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
<div class="card-header page_header fixed-top"> <img class="header_icon_pages" src="Design/assets/images/page_icons/schdule.png" /> <bean:message key="ecom.cargoprotect.faq" bundle="<%=clientcode%>"/>  </div>
<div class="" id="cargoprotectfaq">
    <div class="col-sm-12 " >
        <div class="col-sm-1"></div>
        <div class="card card-body card-shadow page_bottom_content remove_print">
            <% if ("CH".equalsIgnoreCase(SelectedLanguage)) {%>
            <div class="col-sm-12 ">
                <div style="border: 2px solid;padding: 10px;">
                    <div style="padding: 20px;">
                        <div> <h2>货运险 - 常见问题 </h2></div>
                        <!--<div style="float: right;top: 4px;position: absolute;right: 67px;">
                            <img alt="logo" src="/Ecommaster/cargoprotect.png" />
                        </div>-->
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
                        </div>

                        <div class="col-sm-12">
                            <h6>4) 海运货物保险的保障范围包括哪些？</h6>
                            <p>保单的保障范围基于机构货运条款 (A)，包括所有损失或损坏风险，以及共同海损和救助费用，但条款中排除的情况除外。</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>5) 购买保险需要哪些文件？</h6>
                            <p>除了通常需要在 E-sea 中提供的货物详情外，保险所需的唯一信息即货物价值（保额），购买保险时不需要任何文件。但是，在理赔时需要提供货运文件。</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>6) 海运货物保险是否涵盖所有类型的货物？</h6>
                            <p>我们仅承保集装箱货运，但以下货物除外：铸币（如珠宝、宝石等）、在途现金、债券、贵金属原材料（如金、银及类似物质）、文件、牲畜/牧畜/渔获物、血液制品、血浆、脐带血干细胞、放射性/核材料武器和弹药、项目货物、集装箱船、动力煤、散装货物</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>7) 此保险是否仅在海上航行期间提供保障？</h6>
                            <p>货运保险将针对从申报的起点到申报的目的地之间的正常运输过程中的运输和中间储存时面临的风险提供保障，其中可能包括其他运输方式</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>8) 货运保险是否在所有地点提供保障？</h6>
                            <p>货运保险在全世界范围内提供保障，但下列受制裁国家/地区除外：中非共和国、乍得、刚果民主共和国、克里米亚、古巴、伊朗、朝鲜、索马里、苏丹（北部和南部）、叙利亚、委内瑞拉和也门（此清单将定期更新，并可能根据最新法规进行更改）。</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>9) 如需理赔，我该怎么做？</h6>
                            <p>如需理赔，您可以通过保险证书中的链接访问并执行第一时间损失通知 (FNOL) 条款。瑞士再保险的专家小组将参与其中。</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>10) 理赔流程需要多长时间？</h6>
                            <p>根据案例的复杂性而定。对于简单明了的案例，理赔流程约为 5 至 7 天。较为复杂的案例视具体情况而定。</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>11) 阿联酋航运 (ESL) 为什么与瑞士再保险共同提供此保险？</h6>
                            <p>瑞士再保险是一家世界领先的再保险、保险和其他保险型风险转移方案提供商。该品牌拥有良好的信誉，财务实力雄厚，并拥有深厚的核保专业知识和产品知识。 (www.swissre.com) </p>
                        </div>


                        <div class="col-sm-12">
                            <h6>12) 如何支付货运保险费？</h6>
                            <p>您可以在约定的付款期限内通过银行转账将货运保险费缴付给瑞士再保险。您将收到单独的保费发票。 </p>
                        </div>

                        <div class="col-sm-12">
                            <h6>13) 此保险是否可以取消？</h6>
                            <p>一般情况下，海运货物现货证书不可取消，除非风险不再存在，例如货物在风险起保日前已销毁或托运人取消货运（未进行货运）。</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>14) 我是否会拿到一份保险及其详细信息的书面证书？</h6>
                            <p>在您确认购买保险后，将生成货运保险证书。完整的保单文本可在网站上访问或通过电子邮件发送。</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>15) 如需修改货运信息，我该怎么做？</h6>
                            <p>您可以照常在 E-sea 中进行修改，保险将重新报价以反映保费的任何变化（如有），</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>16) 我可以通过平台投保的货物最大价值是多少？</h6>
                            <p>每批货运的最大保障范围因货物类型而异。部分类型货物的限额最高可达 1 千万美元（或其他货币的等值金额）。</p>
                        </div>

                    </div>
                </div>
            </div>
            <% } else {%>
            <div class="col-sm-12 ">
                <div style="border: 2px solid;padding: 10px;">
                    <div style="padding: 20px;">
                        <div> <h2>Cargo Protect - FAQ </h2></div>
                        <!--<div style="float: right;top: 4px;position: absolute;right: 67px;">
                            <img alt="logo" src="/Ecommaster/cargoprotect.png" />
                        </div>-->
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
                        </div>

                        <div class="col-sm-12">
                            <h6>4) What is the coverage under a marine cargo insurance? </h6>
                            <p>The coverage of the policy is based on the Institute Cargo Clause (A) which covers all risks of loss or damage, and including General Average and Salvage Security Charges except as excluded in the clause wordings.</p>
                        </div>

                        <div class="col-sm-12">
                            <h6>5) What documents are required to purchase insurance? </h6>
                            <p>The only information required for insurance, in addition to cargo details usually provided in E-sea, is the cargo value (sum insured), No documents are required during purchase. However, the shipping documents should be available in the event of a claim. </p>
                        </div>


                        <div class="col-sm-12">
                            <h6>6) Can the marine cargo insurance cover for all types of goods? </h6>
                            <p>We will only cover for containerized shipments with the exception of the following cargo: Specie (jewels, precious stones..), Cash in transit, Bonds, Raw precious metals (gold, silver and similar), Documents, Livestock/bloodstock/fish catch, Blood Products, Blood Plasma, Cord Blood Stem Cell, Radioactive/Nuclear Material Weapons and Ammunition, Project Cargo, Container hull, Thermal coal, Bulk cargo</p>
                        </div>   

                        <div class="col-sm-12">
                            <h6>7) Will the insurance protect only during sea voyage? </h6>
                            <p>The cargo insurance will protect against risks during transportation and intermediate storage in the ordinary course of transit, from declared point of origin to declared point of destination which may include other modes of transportation</p>
                        </div> 

                        <div class="col-sm-12">
                            <h6>8) Does the cargo insurance cover provide coverage in all locations? </h6>
                            <p>The cargo insurance provides worldwide to worldwide cover with a few exceptions relating to sanctioned countries as listed: Central African Republic, Chad, Congo DR, Crimea, Cuba, Iran, North Korea, Somalia, Sudan (North and South), Syria, Venezuela, Yemen (list is regularly updated and may change based on latest regulations).</p>
                        </div>      

                        <div class="col-sm-12">
                            <h6>9) What do I do in case of a claim? </h6>
                            <p>In case of a claim, you may use the First notification of loss (FNOL) provision that is accessible through a link that you will find in the insurance certificate. This will be attended by a team of experts in Swiss Re.</p>
                        </div>      

                        <div class="col-sm-12">
                            <h6>10) How long is the claim processing? </h6>
                            <p>This will vary depending on the complexity of the case. For simple and straightforward claims, the processing time is 5 to 7 days. For more complex cases, it is on case by case basis.</p>
                        </div>      

                        <div class="col-sm-12">
                            <h6>11) Why did ESL partner with Swiss Re to provide this insurance cover?</h6>
                            <p>Swiss Re is one of the world's leading provider of reinsurance, insurance and other forms of insurance-based risk transfer. It is a reputable brand, financially strong, supported by strong underwriting expertise and product knowledge. (www.swissre.com) </p>
                        </div>      

                        <div class="col-sm-12">
                            <h6>12) How do I pay for the cargo insurance? </h6>
                            <p>The insurance premium for your shipment may be paid via bank transfer to Swiss Re within agreed payment terms. You will receive a separate invoice for the premium due. </p>
                        </div>      

                        <div class="col-sm-12">
                            <h6>13) Am I able to cancel the insurance cover?</h6>
                            <p>A Marine Cargo spot certificate, generally, cannot be cancelled, unless the risk does not exist anymore e.g. the cargo was destroyed before risk inception or the shipper will not perform the shipment (the shipment never took place).</p>
                        </div>      

                        <div class="col-sm-12">
                            <h6>14) Do I get a documentary proof of the coverage and its details? </h6>
                            <p>An insurance certificate for the shipment will be generated once you confirm purchase of the cover. A complete set of policy wordings will be accessible on the website/sent via email.</p>
                        </div>      

                        <div class="col-sm-12">
                            <h6>15) What if I need to make amendments to my shipment?</h6>
                            <p>You can proceed with your amendments in E-sea as per usual and the insurance will be re-quoted to reflect any changes in the premium (if any),</p>
                        </div>  

                        <div class="col-sm-12">
                            <h6>16) What is the maximum value of cargo that I can insure via this platform? </h6>
                            <p>The maximum coverage per shipment varies depending on the type of goods. Limit can be up to USD 10Million (or equivalent in other currencies) for select types.</p>
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
    #cargoprotectfaq .faq_header {
        background-color: rgb(236, 236, 236) !important;
        cursor:pointer !important;
        padding: .45rem 1.25rem !important;
    }
    #cargoprotectfaq .faq_box{
        border: 1px solid rgba(0,0,0,.125);
        border-radius: calc(.25rem - 1px) calc(.25rem - 1px) 0 0;
    }
    #cargoprotectfaq .remove_padding {
        padding-left: 0px;
        padding-right: 0px;
    }
    #cargoprotectfaq h2,#cargoprotectfaq h3,#cargoprotectfaq h4,#cargoprotectfaq h5,#cargoprotectfaq h6,#cargoprotectfaq a,#cargoprotectfaq video {
        padding: 10px 0px;
        font-weight: bold;
    }
    #cargoprotectfaq p {
        font-size: 15px;
    }

</style>