#!/usr/bin/env python3
"""Generate a professional .docx technical audit report for e-comOnline-EJBModule_New."""

from docx import Document
from docx.shared import Inches, Pt, Cm, RGBColor, Emu
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.section import WD_ORIENT
from docx.oxml.ns import qn, nsdecls
from docx.oxml import parse_xml
import datetime

doc = Document()

# ---------------------------------------------------------------------------
# Global styles
# ---------------------------------------------------------------------------
style = doc.styles["Normal"]
style.font.name = "Calibri"
style.font.size = Pt(11)
style.paragraph_format.space_after = Pt(6)
style.paragraph_format.line_spacing = 1.15

for level in range(1, 5):
    hs = doc.styles[f"Heading {level}"]
    hs.font.name = "Calibri"
    hs.font.color.rgb = RGBColor(0x1B, 0x3A, 0x5C)

# Utility helpers ----------------------------------------------------------

def add_heading(text, level=1):
    doc.add_heading(text, level=level)

def add_para(text, bold=False, italic=False, size=None):
    p = doc.add_paragraph()
    run = p.add_run(text)
    run.bold = bold
    run.italic = italic
    if size:
        run.font.size = Pt(size)
    return p

def add_bullet(text, level=0):
    p = doc.add_paragraph(text, style="List Bullet")
    p.paragraph_format.left_indent = Cm(1.27 * (level + 1))
    return p

def add_table(headers, rows, col_widths=None):
    table = doc.add_table(rows=1, cols=len(headers))
    table.style = "Light Grid Accent 1"
    table.alignment = WD_TABLE_ALIGNMENT.LEFT
    for i, h in enumerate(headers):
        cell = table.rows[0].cells[i]
        cell.text = h
        for paragraph in cell.paragraphs:
            for run in paragraph.runs:
                run.bold = True
                run.font.size = Pt(10)
    for row_data in rows:
        row = table.add_row()
        for i, val in enumerate(row_data):
            row.cells[i].text = str(val)
            for paragraph in row.cells[i].paragraphs:
                for run in paragraph.runs:
                    run.font.size = Pt(10)
    if col_widths:
        for i, w in enumerate(col_widths):
            for row in table.rows:
                row.cells[i].width = Inches(w)
    doc.add_paragraph()
    return table

def add_code_block(text):
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Cm(1)
    run = p.add_run(text)
    run.font.name = "Consolas"
    run.font.size = Pt(9)
    run.font.color.rgb = RGBColor(0x33, 0x33, 0x33)
    return p

def add_mermaid_text(title, code):
    add_para(f"Diagram: {title}", bold=True, italic=True, size=10)
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Cm(0.5)
    run = p.add_run(code)
    run.font.name = "Consolas"
    run.font.size = Pt(8.5)
    run.font.color.rgb = RGBColor(0x2E, 0x86, 0xC1)
    doc.add_paragraph()

# =========================================================================
#  COVER PAGE
# =========================================================================
for _ in range(6):
    doc.add_paragraph()

cover_title = doc.add_paragraph()
cover_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = cover_title.add_run("E-Commerce Online Platform\nEJB Module — Technical & Design-System Audit")
run.bold = True
run.font.size = Pt(28)
run.font.color.rgb = RGBColor(0x1B, 0x3A, 0x5C)

doc.add_paragraph()

cover_sub = doc.add_paragraph()
cover_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = cover_sub.add_run("e-comOnline-EJBModule_New")
run.font.size = Pt(16)
run.font.color.rgb = RGBColor(0x5D, 0x6D, 0x7E)

doc.add_paragraph()

cover_meta = doc.add_paragraph()
cover_meta.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = cover_meta.add_run(
    f"Branch: Ecom_CR_Rls_Aug26\n"
    f"Date: {datetime.date.today().strftime('%B %d, %Y')}\n"
    f"Classification: Internal / Confidential\n"
    f"Module Version: 1.0"
)
run.font.size = Pt(12)
run.font.color.rgb = RGBColor(0x7F, 0x8C, 0x8D)

doc.add_page_break()

# =========================================================================
#  TABLE OF CONTENTS (placeholder — Word will regenerate on open)
# =========================================================================
add_heading("Table of Contents", level=1)
toc_para = doc.add_paragraph()
run = toc_para.add_run(
    "1.  Executive Summary\n"
    "2.  Tech-Stack Analysis\n"
    "3.  Application Architecture\n"
    "4.  Data & API Architecture\n"
    "5.  Component Architecture (EJB Beans)\n"
    "6.  Value Object & Data-Model Inventory\n"
    "7.  VDS Service Layer Analysis\n"
    "8.  Security & Authentication Analysis\n"
    "9.  User Flow Analysis\n"
    "10. UI/UX Pattern Inventory (Scope Note)\n"
    "11. Architecture Diagrams (Mermaid)\n"
    "12. Issues, Technical Debt & Recommendations\n"
    "13. Final Architecture Summary"
)
run.font.size = Pt(11)

doc.add_page_break()

# =========================================================================
#  1. EXECUTIVE SUMMARY
# =========================================================================
add_heading("1. Executive Summary", level=1)

add_para(
    "This document presents a comprehensive technical audit of the e-comOnline-EJBModule_New "
    "codebase — the backend business-logic module of a maritime/shipping e-commerce platform. "
    "The analysis is based entirely on source-code inspection of the Ecom_CR_Rls_Aug26 branch."
)

add_para(
    "The module is a Java EE 7 EJB 3.1 JAR packaged for deployment on JBoss WildFly. "
    "It contains 284 Java source files organized across 11 packages under the root namespace "
    "net.ecom. The architecture follows a classic three-tier Java EE pattern: stateless session "
    "beans expose @Remote interfaces consumed by a separate web-tier WAR module "
    "(e-comOnlineWeb_New)."
)

add_para("Key Findings:", bold=True)
add_bullet("284 Java source files across 11 packages")
add_bullet("22 @Stateless EJB session beans (15 App-Tier + 7 V1 Legacy)")
add_bullet("120+ Value Objects / DTOs for data transfer")
add_bullet("9 VDS (Virtual Data Service) query interfaces with 150+ named queries")
add_bullet("Zero JPA/ORM — all data access is raw JDBC with inline SQL")
add_bullet("9 JNDI datasources connecting to multiple databases (likely SQL Server)")
add_bullet("Dual V1/V2 architecture for backward compatibility with legacy LRP system")
add_bullet("Stripe integration for online payments")
add_bullet("PBEWithMD5AndDES encryption (deprecated cryptographic approach)")
add_bullet("No unit tests found in the module")

doc.add_page_break()

# =========================================================================
#  2. TECH-STACK ANALYSIS
# =========================================================================
add_heading("2. Tech-Stack Analysis", level=1)

add_heading("2.1 Core Technologies", level=2)
add_table(
    ["Category", "Technology", "Version", "Evidence"],
    [
        ["Language", "Java", "1.8 (Java 8)", "pom.xml: <source>1.8</source>"],
        ["Platform", "Java EE", "7.0", "pom.xml: javaee-api 7.0 (provided)"],
        ["EJB Specification", "EJB", "3.1", "maven-ejb-plugin: ejbVersion 3.1"],
        ["Application Server", "JBoss WildFly", "—", "nb-configuration.xml deploy target"],
        ["Build System", "Apache Maven", "—", "pom.xml at project root"],
        ["IDE", "NetBeans", "—", "nb-configuration.xml"],
        ["Source Encoding", "UTF-8", "—", "pom.xml property"],
    ],
    col_widths=[1.5, 1.5, 1.0, 2.5]
)

add_heading("2.2 Dependencies (Libraries)", level=2)
add_table(
    ["Library", "Version", "Scope", "Purpose"],
    [
        ["javax:javaee-api", "7.0", "provided", "Java EE container APIs"],
        ["VDS.jar (com.svm.lrp.nfr)", "1.0", "system", "Virtual Data Service query framework"],
        ["JsonUtil.jar (com.svm.json)", "1.0", "system", "SVM JSON utilities (SvmJsonList, SvmJsonObject)"],
        ["serverutils.jar", "1.0", "system", "JNDI/ServerObject connection factory"],
        ["gson", "2.3", "system", "Google Gson JSON serialization"],
        ["mailapi.jar", "1.0", "system", "Email service (IMail interface)"],
        ["multiagencyutil.jar", "1.0", "system", "Multi-agency/tenant utilities"],
        ["commons-io", "2.4", "system", "Apache Commons IO file utilities"],
    ],
    col_widths=[2.0, 0.8, 0.8, 3.0]
)

add_para(
    "Important: All non-JavaEE dependencies use system scope referencing local filesystem "
    "paths (D:\\LIB\\ECOM_WILDFLY). This hardcoded Windows path creates a build portability "
    "issue — Maven builds will fail on Linux/macOS without path adjustment.",
    italic=True
)

add_heading("2.3 Database Layer", level=2)
add_table(
    ["Aspect", "Details", "Source"],
    [
        ["ORM/JPA", "NOT USED — no persistence.xml, no @Entity annotations", "Full source scan"],
        ["Data Access", "Raw JDBC (PreparedStatement, ResultSet)", "All session beans"],
        ["Connection Method", "@Resource JNDI + JDBCServiceLocatorFactory + ServerObject", "EJB annotations, utils/"],
        ["Database Engine", "Microsoft SQL Server (inferred)", "MSSQLDS name, T-SQL syntax: (nolock), GETDATE(), IDENT_CURRENT"],
        ["Stored Procedures", "Yes — usp_getecombookingsummary, etc.", "EcomBookingBean, EcomBLBean"],
        ["Transaction Mgmt", "Container-Managed Transactions (CMT)", "@Stateless default behavior"],
    ],
    col_widths=[1.5, 3.0, 2.0]
)

add_heading("2.4 JNDI Datasource Map", level=2)
add_table(
    ["Resource Name", "JNDI Mapped Name", "Primary Consumers"],
    [
        ["jdbc/ECOMDB", "java:/ECOMTXDS", "EComCntrlBean, EcomBookingBean, EcomBLBean, most beans"],
        ["jdbc/MASTERDB", "java:/MASTERDS", "EComCntrlBean, EcomRegistrationBean, EcomBookingBean"],
        ["jdbc/LRPDB", "java:/MSSQLDS", "EcomVGMBean, EcomBLBean, EcomNoticeBean"],
        ["jdbc/ReportDB", "java:/REPORTDS", "EcomScheduleBean, EcomAdminCntrlBean"],
        ["jdbc/VSSDB", "java:/VSSDS", "EcomScheduleBean, EcomBookingBean"],
        ["jdbc/DOCDB", "java:/DOCDS", "EcomBookingBean"],
        ["jdbc/COMDB", "java:/COMDS", "EcomPhase2Bean, ContractGlobalSurchargeHelper"],
        ["jdbc/ACCDB", "java:/ACCTXDS", "EcomOnlinePaymentBean"],
        ["jdbc/ECOMDB1", "java:/ECOMDS", "EcomBLBean"],
        ["LRPV1DS", "java:/LRPV1DS", "V1 legacy beans (programmatic lookup)"],
    ],
    col_widths=[1.5, 1.8, 3.2]
)

add_heading("2.5 Third-Party Integrations", level=2)
add_bullet("Stripe Payment Gateway — EcomOnlinePaymentBean handles payment intent creation, webhook events, and refunds")
add_bullet("EDI (Electronic Data Interchange) — EDI109 (Booking), EDI110 (SI) message processing")
add_bullet("Email Service — IMail CDI injection + Mailer/MailerNew utility classes for notification emails")
add_bullet("VDS (Virtual Data Service) — Proprietary query proxy framework (SolverMinds internal)")
add_bullet("Jasper Reports — Booking confirmation report generation (getecomjasperreportpath VDS query)")
add_bullet("FTP Integration — File upload/download for VGM, DG certificates, MSDS documents")
add_bullet("Multi-Agency Framework — MultiAgencyUtil for tenant-level data isolation")

add_heading("2.6 Authentication & Security", level=2)
add_bullet("Password Hashing: SHA-256 (net.ecom.common.SHA256)")
add_bullet("Encryption: PBEWithMD5AndDES via CryptoLibrary (DEPRECATED — weak cipher)")
add_bullet("Session Management: Handled by web-tier (not in this EJB module)")
add_bullet("Password Reset: Token-based flow (PasswordResetTokenVo)")
add_bullet("Admin Auth: Separate admin user table (ecom_adminuser_details)")
add_bullet("User Registration: Multi-step with email verification (random_number activation link)")

doc.add_page_break()

# =========================================================================
#  3. APPLICATION ARCHITECTURE
# =========================================================================
add_heading("3. Application Architecture", level=1)

add_heading("3.1 Overall Architecture", level=2)
add_para(
    "The application follows a classic Java EE multi-tier architecture. This EJB module serves "
    "as the middle tier (business logic), sitting between the web presentation tier "
    "(e-comOnlineWeb_New WAR) and the database tier (multiple SQL Server instances)."
)

add_heading("3.2 Project / Folder Structure", level=2)
add_code_block(
    "e-comOnline-EJBModule_New/\n"
    "├── pom.xml                          # Maven build config\n"
    "├── nb-configuration.xml             # NetBeans IDE / WildFly deploy target\n"
    "├── src/main/java/net/ecom/\n"
    "│   ├── AppTierCntrlBean/            # 15 @Stateless session beans + 14 @Remote interfaces\n"
    "│   ├── v1/cntlbean/                 # 7 legacy V1 session beans + remotes\n"
    "│   ├── v1/vo/                       # V1 master data VO\n"
    "│   ├── vds/service/                 # 9 VDS query service interfaces\n"
    "│   ├── vds/serviceVo/               # 43 VDS result model POJOs\n"
    "│   ├── vo/                          # 120 general Value Objects / DTOs\n"
    "│   ├── si/vo/                       # 22 Shipping Instruction VOs\n"
    "│   ├── phase2/service/vo/           # 21 Phase-2 billing/payment VOs\n"
    "│   ├── common/                      # 18 utility classes\n"
    "│   ├── surcharge/                   # 6 rate-contract surcharge calculators\n"
    "│   └── utils/                       # 1 JDBC connection factory\n"
    "└── src/main/resources/META-INF/\n"
    "    ├── jboss.xml                    # Empty WildFly descriptor\n"
    "    └── MANIFEST.MF                  # JAR manifest"
)

add_heading("3.3 Package Statistics", level=2)
add_table(
    ["Package", "Files", "Role"],
    [
        ["net.ecom.vo", "120", "UI/API transfer objects, form beans, domain DTOs"],
        ["net.ecom.vds.serviceVo", "43", "VDS query result models (Gson-serialized)"],
        ["net.ecom.AppTierCntrlBean", "29", "Main EJB session beans + @Remote interfaces"],
        ["net.ecom.si.vo", "22", "Shipping Instruction JSON/domain models"],
        ["net.ecom.phase2.service.vo", "21", "Phase-2 billing/payment/accounting VOs"],
        ["net.ecom.common", "18", "Utilities: crypto, mail, validation, dates, constants"],
        ["net.ecom.v1.cntlbean", "14", "Legacy V1 EJB beans + remote interfaces"],
        ["net.ecom.vds.service", "9", "VDS service interfaces (annotated query contracts)"],
        ["net.ecom.surcharge", "6", "Rate contract / surcharge calculation helpers"],
        ["net.ecom.v1.vo", "1", "V1 master data cache VO"],
        ["net.ecom.utils", "1", "JDBC/JNDI connection factory"],
        ["TOTAL", "284", ""],
    ],
    col_widths=[2.5, 0.8, 3.5]
)

add_heading("3.4 Backend Architecture Pattern", level=2)
add_para(
    "The module implements a Facade pattern where each @Stateless session bean acts as a "
    "coarse-grained service facade for a functional domain (Booking, BL, SI, VGM, etc.). "
    "These facades combine:"
)
add_bullet("Direct JDBC queries (inline SQL string constants)")
add_bullet("VDS proxy calls (declarative query interface via @VDS annotations)")
add_bullet("Inter-bean delegation via @EJB injection (e.g., EcomBookingBean → EcomV1BookingBean)")
add_bullet("Email notifications via @Inject IMail")
add_bullet("Utility method calls for validation, date conversion, crypto")

add_heading("3.5 Data Flow", level=2)
add_para(
    "1. Web Tier (WAR) invokes @Remote EJB interface method via JNDI lookup\n"
    "2. Session bean receives call, applies business logic\n"
    "3. Bean accesses data via one of three paths:\n"
    "   a. VDS proxy → VDS.jar → named query → database\n"
    "   b. Direct JDBC via @Resource DataSource → PreparedStatement → ResultSet\n"
    "   c. ServerObject(\"DBNAME\").getConnection() → JDBC\n"
    "4. Results mapped to VO/DTO objects\n"
    "5. VO returned to web tier for rendering"
)

add_heading("3.6 Error Handling", level=2)
add_para("Error handling patterns found in source (not recommended):", bold=True)
add_bullet("Most exceptions caught with e.printStackTrace() to stdout — no structured logging framework")
add_bullet("Generic catch(Exception e) blocks used throughout")
add_bullet("Business errors returned as string messages in ReturnVO objects")
add_bullet("JDBC resources (Connection, Statement, ResultSet) use manual try/finally close patterns")
add_bullet("JDBCServiceLocatorFactory.close() methods swallow SQLExceptions silently")
add_bullet("No centralized exception handling or exception hierarchy")

doc.add_page_break()

# =========================================================================
#  4. DATA & API ARCHITECTURE
# =========================================================================
add_heading("4. Data & API Architecture", level=1)

add_heading("4.1 API Layer — @Remote EJB Interfaces", level=2)
add_para(
    "The module exposes its business operations through 21 @Remote EJB interfaces. "
    "These are consumed by the web tier via JNDI lookup. There is no REST or SOAP API layer "
    "in this module."
)

add_table(
    ["Remote Interface", "Bean Implementation", "Domain"],
    [
        ["EComCntrlBeanRemote", "EComCntrlBean", "Central facade: login, config, dashboard, master data"],
        ["EcomBookingBeanRemote", "EcomBookingBean", "Online booking CRUD, templates, haulage, ENS"],
        ["EcomBLBeanRemote", "EcomBLBean", "Bill of Lading management"],
        ["EcomShippingInstructionBeanRemote", "EcomShippingInstructionBean", "Shipping Instructions (SI)"],
        ["EcomScheduleBeanRemote", "EcomScheduleBean", "Vessel schedules and routing"],
        ["EcomRegistrationBeanRemote", "EcomRegistrationBean", "Customer registration and user management"],
        ["EcomOnlinePaymentBeanRemote", "EcomOnlinePaymentBean", "Stripe payment processing"],
        ["EcomVGMBeanRemote", "EcomVGMBean", "Verified Gross Mass declarations"],
        ["EcomPhase2BeanRemote", "EcomPhase2Bean", "Invoices, CRO, customer statements"],
        ["EcomAdminCntrlBeanRemote", "EcomAdminCntrlBean", "Admin operations"],
        ["EcomAnalyticsBeanRemote", "EcomAnalyticsBean", "Analytics and reporting"],
        ["EcomAmendmentBookingBeanRemote", "EcomAmendmentBookingBean", "Booking amendments"],
        ["EcomAmendmentMailBeanRemote", "EcomAmendmentMailBean", "Amendment email notifications"],
        ["EcomMaskBeanRemote", "EcomMaskBean", "Port masking"],
        ["EcomV1BookingBeanRemote", "EcomV1BookingBean", "Legacy V1 booking / EDI"],
        ["EcomV1BLBeanRemote", "EcomV1BLBean", "Legacy V1 Bill of Lading"],
        ["EcomV1SIBeanRemote", "EcomV1SIBean", "Legacy V1 Shipping Instructions"],
        ["EcomV1VGMBeanRemote", "EcomV1VGMBean", "Legacy V1 VGM"],
        ["EcomV1DashboardBeanRemote", "EcomV1DashboardBean", "Legacy V1 dashboard"],
        ["EcomV1MasterBeanRemote", "EcomV1MasterBean", "Legacy V1 master data cache"],
        ["EcomV1AmendmentBookingBeanRemote", "EcomV1AmendmentBookingBean", "Legacy V1 booking amendments"],
    ],
    col_widths=[2.5, 2.0, 2.5]
)

add_heading("4.2 Database Tables (Inferred from SQL)", level=2)
add_para("Key tables referenced in inline SQL queries across the session beans:")
add_table(
    ["Table / Entity", "Domain", "Key Operations"],
    [
        ["ecombooking", "Booking", "CRUD for online bookings; template save/load"],
        ["ecom_bookingcargo", "Booking-Cargo", "Container and cargo detail storage"],
        ["ecom_bookingcommodity", "Booking-Commodity", "Commodity details per equipment"],
        ["ecom_bookingreference", "Booking-Ref", "Export/customer/ACI references"],
        ["ecom_bookingleg", "Booking-Routing", "Leg/routing information per booking"],
        ["editempbooking_*", "EDI Temp", "Temporary EDI booking tables for LRP integration"],
        ["ecom_reg_details", "Registration", "Customer user accounts"],
        ["ecom_tempuserdetail", "Registration", "Pending registration approvals"],
        ["ecom_tempcompany", "Registration", "Company details for registration"],
        ["ecom_messageformat", "Config", "Email templates, messages, UI config"],
        ["ecom_messageformatlang", "Config (i18n)", "Multi-language message formats"],
        ["globalconfigproduct", "Config", "Global system configuration key-values"],
        ["MenuRegistration", "Admin", "Menu/module registration"],
        ["SplPrivilegeMenu", "Admin", "Customer-specific menu privileges"],
        ["vendormodulemapping", "Admin", "Vendor module access mapping"],
        ["ECOM_USERDETAIL", "Auth", "User credentials and profile"],
        ["ecom_adminuser_details", "Auth", "Admin user credentials"],
        ["Ecom_OnlinePayment", "Payment", "Stripe payment records"],
        ["Ecom_OnlinePaymentReference", "Payment", "Payment invoice references"],
        ["Ecom_OnlinePaymentStripeEvent", "Payment", "Stripe webhook event log"],
        ["cratmpSurcharge", "Surcharge", "Temporary surcharge calculation staging"],
    ],
    col_widths=[2.2, 1.5, 3.0]
)

doc.add_page_break()

# =========================================================================
#  5. COMPONENT ARCHITECTURE (EJB Beans)
# =========================================================================
add_heading("5. Component Architecture — EJB Session Beans", level=1)

add_heading("5.1 App-Tier Beans (Primary)", level=2)

beans_data = [
    ["EComCntrlBean", "~6,900", "Central facade — login/auth, dashboard, global config, master data, "
     "menu management, customer advisory, mail alerts, booking templates"],
    ["EcomBookingBean", "~13,350", "LARGEST BEAN — complete booking lifecycle: create, edit, cancel, "
     "template management, haulage, ENS filing, EDI, cargo protect, print, file upload"],
    ["EcomBLBean", "~4,000+", "Bill of Lading: summary, detail view, OBL print, BL submit, "
     "container details, seal management"],
    ["EcomShippingInstructionBean", "~3,500+", "SI creation, editing, submission, container details, "
     "parties management, seal/VGM integration"],
    ["EcomScheduleBean", "~2,000+", "Vessel schedule search by port/vessel, routing information, "
     "transit time calculation"],
    ["EcomRegistrationBean", "~4,200", "Customer registration, user creation, profile management, "
     "password reset, email verification, admin approval"],
    ["EcomOnlinePaymentBean", "~850", "Stripe payment: create intent, confirm, refund, webhook events, "
     "payment history, receipt generation"],
    ["EcomVGMBean", "~2,000+", "VGM declaration submission, editing, weight conversion, "
     "container validation"],
    ["EcomPhase2Bean", "~3,000+", "Phase-2: CRO, invoices, delivery orders, customer statements, "
     "tariff, surcharge calculation"],
    ["EcomAdminCntrlBean", "~1,500+", "Admin: customer management, statistics, report generation, "
     "configuration updates"],
    ["EcomAnalyticsBean", "~800+", "Usage analytics, booking/BL/SI statistics"],
    ["EcomAmendmentBookingBean", "~2,000+", "Post-booking amendments: cargo, routing, schedule changes"],
    ["EcomAmendmentMailBean", "~1,000+", "Amendment notification emails to agencies/customers"],
    ["EcomMaskBean", "~500+", "Port masking: hide/show ports based on configuration"],
    ["EcomNoticeBean", "~300+", "Internal notice management (NO @Remote — local only)"],
]

add_table(
    ["Bean", "~Lines", "Responsibilities"],
    beans_data,
    col_widths=[2.0, 0.8, 4.0]
)

add_heading("5.2 V1 Legacy Beans", level=2)
add_para(
    "The V1 tier provides backward compatibility with the legacy LRP (Line Release Planning) "
    "system. App-tier beans delegate to V1 beans via @EJB injection for operations that need "
    "to interact with the old database schema."
)

add_table(
    ["Bean", "Responsibilities"],
    [
        ["EcomV1BookingBean", "Legacy LRP booking: editempbooking_* table insertion, EDI document processing"],
        ["EcomV1BLBean", "Legacy BL processing against LRP schema"],
        ["EcomV1SIBean", "Legacy Shipping Instruction processing"],
        ["EcomV1VGMBean", "Legacy VGM integration"],
        ["EcomV1DashboardBean", "Legacy dashboard data aggregation"],
        ["EcomV1MasterBean", "Master data cache: ports, vessels, commodities, countries from LRP"],
        ["EcomV1AmendmentBookingBean", "Legacy booking amendment processing"],
    ],
    col_widths=[2.5, 4.5]
)

add_heading("5.3 Bean Dependencies & Injection", level=2)
add_para("Inter-bean dependencies discovered in source:")
add_bullet("EComCntrlBean → @EJB EcomV1DashboardBeanRemote (dashboard delegation)")
add_bullet("EcomBookingBean → @EJB EcomV1BookingBeanRemote (legacy booking)")
add_bullet("EcomBookingBean → @Inject VDSInvoker (VDS proxy creation)")
add_bullet("EcomBookingBean → @Inject IMail (email notifications)")
add_bullet("EcomRegistrationBean → @Inject VDSInvoker + direct JDBC")
add_bullet("EcomOnlinePaymentBean → lrp.serverutils.refs.ServerObject (direct connection)")
add_bullet("ContractGlobalSurchargeHelper → ServerObject(\"COMDB\") (static utility)")

doc.add_page_break()

# =========================================================================
#  6. VALUE OBJECT & DATA-MODEL INVENTORY
# =========================================================================
add_heading("6. Value Object & Data-Model Inventory", level=1)

add_heading("6.1 General VOs (net.ecom.vo) — 120 files", level=2)
add_para(
    "These are plain Java POJOs (no JPA annotations) used as data transfer objects between "
    "the EJB tier and the web tier. They implement Serializable for RMI transport."
)

vo_groups = [
    ["Booking", "BookingVO, EbookingValidatorFormBean, EbookingValidatorFormBeanNew (~4K lines), "
     "EbookingViewAll, AmendmentBookingFormBean, EcomBookingScheduleVo, EcomBookingCustomer, "
     "EcomBookingCargoDetails, BookingHaulagedetailsVo, BookingContDetVO, BkgContainerHaulageDetails, "
     "EcomBkgPrintBookingSaveVo, AutoLegVo, BoundConfigVo, BoundDefultVo"],
    ["Bill of Lading", "BLVO, BLMasterVo, BLSubmitVO, BLPrintingVO, BLEditVo, BLEditDetailsVO, "
     "BLPartiesVO, BLSealVO, BLContainerDetailVo, BLContainerValueVO, OBLPrintVo"],
    ["Shipping Inst.", "SIMasterVO, SIMasterDetailsVO, SISubmitVO, SIPartiesVO, SIContainerVO, "
     "SIContainerDetailVO, SIContainerContentVO, SIConatainerValueVO, SISealVO, SIDtableVO, "
     "SIBLMasterVO, SIBLCommodityVO, SIBLCargoVO, SIsummaryVO"],
    ["VGM", "VgmVO, VGMCommonVO, VGMVOModel"],
    ["Registration / Auth", "RegistrationVO, EComVo, LoginCustomerVo, SecurityVO, PasswordResetTokenVo, "
     "UserData, UserCreationVO, UserAuditVO, AdminCustomerVo, CustomerProfile, CustomerDetailsVO, "
     "CustomerSearch, CustomerType"],
    ["Payment", "OnlinePaymentVO, OnlinePaymentStripeEventVO, OnlinePaymentReferenceVO, ReceiptVO, "
     "ReceiptDetailsVO"],
    ["Schedule / Routing", "SchdeuleVo, ScheduleBlwLocationVo, Route, RouteAvailVo, RoutingVO, "
     "TransScheduleLegVo, LayoverTimeVo, BetPortsSortVo"],
    ["Config / Admin", "GlobalConfigVO, FieldConfigVO, CutoffConfigVO, RegisterMenuVO, MenuBean, "
     "V1V2ServiceConfigVO, ServiceRestrictionVO, MaskPortVO, ColumnList"],
    ["Other Domain", "Agencydto, CityVo, PortVO, Portdto, PortsBean, EquipmentVO, EquipmentBean, "
     "Equipments, ChargesVo, RatesContractsVO, RateVo, RateContractSurchargeVO, ContractVo, "
     "RequestRateVO, CargoProtectVo, ENSDeclarantVo, CommoditySearchVO"],
    ["Communication", "MailFormatVO, MailAlertVO, NotificationVo, CustomerAdvisoryVo, HotlineContactsVO, "
     "VideoHelpVo, SurveyList"],
    ["Misc / Infra", "ReturnVO, Formdto, DropDownVO, DashboardVO, UniqueRandom, ImageUploadVo, "
     "FileDetailsVO, CommonBKGJaserModel, CommonBKGJasperVO, TrackingVo, MovementDetailsVo, "
     "UnclearedCargoVo, OnGoingTransactionVo, TransportServiceVO, TimeZone, TimeZoneList, "
     "NewCustomerstatementvo, BokingDetailds"],
]

add_table(
    ["Domain Group", "Value Objects"],
    vo_groups,
    col_widths=[1.5, 5.5]
)

add_heading("6.2 VDS Service VOs (net.ecom.vds.serviceVo) — 43 files", level=2)
add_para(
    "Result POJOs deserialized from VDS query responses using Gson (@SerializedName / @Expose). "
    "Key classes include: EcomBKGMasterVO, EcomBKGHeader, EcomBKGCargo, EcomBKGCommodity, "
    "EcomBKGCustomer, EcomBKGLeg, EcomBKGRouting, BookingSummaryVo, BlSummaryVo, "
    "CustomerMasterVo, ScheduleCommonVO, DashboardVO, AgencyMaster, VesselMaster."
)

add_heading("6.3 Shipping Instruction VOs (net.ecom.si.vo) — 22 files", level=2)
add_para(
    "Dedicated data models for the SI workflow: CompleteModel (aggregate root), "
    "Bl_header, Bl_cargo, Bl_commodity, Bl_cust, Booking, Booking_cargo, "
    "Booking_commodity, Booking_cust, ShippingInstructionsVO, SIUserrequestVo, SiTemp."
)

add_heading("6.4 Phase-2 VOs (net.ecom.phase2.service.vo) — 21 files", level=2)
add_para(
    "Financial / billing domain: CROSummaryVO, CRODetailsVO, InvoiceSummaryVO, "
    "DOSummaryVO, PaymentHistoryVO, PaymentSummaryVO, AccountSummaryVO, "
    "TariffSummaryVO, DDTariffSummaryVO, SurchargeSummaryVO, OutstandingSummaryVO, "
    "CustStmtCalcVO, Phase2Model (composite wrapper)."
)

doc.add_page_break()

# =========================================================================
#  7. VDS SERVICE LAYER ANALYSIS
# =========================================================================
add_heading("7. VDS Service Layer Analysis", level=1)

add_para(
    "The VDS (Virtual Data Service) layer is a proprietary data-access abstraction built "
    "by SolverMinds. It uses annotation-driven interfaces to declare named queries, which "
    "are resolved at runtime by VDSInvoker proxy instances."
)

add_heading("7.1 VDS Service Interfaces", level=2)
add_table(
    ["Interface", "~Query Count", "Domain"],
    [
        ["EcomBKGVdsService", "~95", "Booking: CRUD, routing, schedule, port details, cargo, customers, EDI"],
        ["EcomBKGSpsService", "~15", "Booking stored procedures (create, update, cancel)"],
        ["EcomBlVdsService", "~20", "Bill of Lading queries and updates"],
        ["EcomSiVdsService", "~15", "Shipping Instruction queries"],
        ["EcomVGMVDSService", "~10", "VGM declaration queries"],
        ["EcomGeneralVDSService", "~10", "Shared/cross-cutting queries"],
        ["EcomDashboardService", "~8", "Dashboard aggregation queries"],
        ["EcomRegVDSService", "~10", "Registration/customer master queries"],
        ["EcomPhase2Service", "~15", "Phase-2 billing, CRO, invoice queries"],
    ],
    col_widths=[2.5, 1.2, 3.5]
)

add_heading("7.2 VDS Annotation Pattern", level=2)
add_code_block(
    "@VDS(name = \"getecombookingsummarydetails\", tenant = \"svm\")\n"
    "public BookingSummaryVo getecombookingsummarydetails(\n"
    "    @FP(name = \"custId\") String custId,\n"
    "    @FP(name = \"bookdate\", condition = Condition.BETWEEN) String bookdate,\n"
    "    @FP(name = \"custtypeid\") String custTypeId\n"
    ");"
)
add_para(
    "Source: net/ecom/vds/service/EcomBKGVdsService.java"
    , italic=True
)
add_para(
    "The @VDS annotation specifies the named query; @FP maps method parameters to query "
    "parameters with optional conditions (BETWEEN, IN, LESSTHANEQUALTO, GREATERTHANEQUALTO). "
    "All queries run under tenant 'svm'."
)

doc.add_page_break()

# =========================================================================
#  8. SECURITY & AUTHENTICATION ANALYSIS
# =========================================================================
add_heading("8. Security & Authentication Analysis", level=1)

add_heading("8.1 Authentication Mechanisms (Found in Source)", level=2)
add_bullet("Customer Login: SQL query against ECOM_USERDETAIL table (loginname + password)")
add_bullet("Admin Login: SQL query against ecom_adminuser_details (userid + password + status='A')")
add_bullet("Password Storage: SHA-256 hashing (SHA256.java)")
add_bullet("Password Reset: Token-based with PasswordResetTokenVo; tokens stored in DB with expiry")
add_bullet("Registration Flow: Random ID activation link → email verification → admin approval")

add_heading("8.2 Encryption (Found in Source — SECURITY CONCERNS)", level=2)
add_para("File: net/ecom/common/CryptoLibrary.java", italic=True)
add_bullet("Algorithm: PBEWithMD5AndDES/CBC/PKCS5Padding — DEPRECATED and cryptographically weak")
add_bullet("Hardcoded passphrase: \"CHANGE THIS TO A BUNCH OF RANDOM CHARACTERS\" — placeholder not changed")
add_bullet("Hardcoded salt: {-93, 33, 36, 44, -14, -46, 62, 25} — static, not randomly generated")
add_bullet("Only 3 iterations for key derivation (PBE spec uses 20) — extremely low")
add_bullet("Uses deprecated sun.misc.BASE64Encoder/Decoder (removed in Java 11+)")

add_para("RECOMMENDATION: Replace with AES-256-GCM, use a proper KDF (PBKDF2 with ≥100,000 "
         "iterations or Argon2), generate random salts, and store the encryption key securely "
         "(e.g., in a vault or app-server keystore).", bold=True)

add_heading("8.3 Authorization", level=2)
add_para(
    "No role-based access control (RBAC) annotations found in the EJB module (@RolesAllowed, "
    "@PermitAll, etc.). Authorization appears to be handled at the web tier level via menu "
    "privileges (SplPrivilegeMenu table) and customer type flags."
)

doc.add_page_break()

# =========================================================================
#  9. USER FLOW ANALYSIS
# =========================================================================
add_heading("9. User Flow Analysis", level=1)

add_heading("9.1 Customer Registration Flow", level=2)
add_para("Source: EcomRegistrationBean.java, EComCntrlBean.java")
add_bullet("1. Customer fills registration form → EcomRegistrationBean creates temp records (ecom_tempuserdetail + ecom_tempcompany)")
add_bullet("2. System generates random_number activation code → sends verification email")
add_bullet("3. Customer clicks activation link → status updated from 'I' (inactive) to 'C' (confirmed)")
add_bullet("4. Agency admin reviews and approves → status changed to 'A' (active)")
add_bullet("5. Approved customer can log in and access the platform")

add_heading("9.2 Online Booking Flow", level=2)
add_para("Source: EcomBookingBean.java (largest bean — ~13,350 lines)")
add_bullet("1. Customer selects route → EcomScheduleBean provides vessel schedules and routing")
add_bullet("2. Customer fills booking form → EbookingValidatorFormBeanNew validates all fields")
add_bullet("3. System calculates surcharges → ContractGlobalSurchargeHelper")
add_bullet("4. Booking saved to ecombooking table + related cargo/commodity/reference/leg tables")
add_bullet("5. EDI document generated → EcomV1BookingBean inserts into editempbooking_* tables")
add_bullet("6. Booking confirmation email sent → IMail + Mailer utility")
add_bullet("7. Customer can print booking confirmation → Jasper Reports integration")
add_bullet("8. Post-booking amendments via EcomAmendmentBookingBean")

add_heading("9.3 Bill of Lading (BL) Flow", level=2)
add_bullet("1. Customer views BL summary by date range → EcomBLBean.getBLSummary()")
add_bullet("2. Customer selects BL → views detail with container/cargo information")
add_bullet("3. Customer can submit BL instructions → BLSubmitVO")
add_bullet("4. OBL (Original BL) printing → EcomBLBean with Jasper Reports")
add_bullet("5. BL amendment via EcomAmendmentBookingBean")

add_heading("9.4 Shipping Instruction (SI) Flow", level=2)
add_bullet("1. Customer selects booking → retrieves booking details for SI pre-population")
add_bullet("2. Fills SI form: parties (shipper, consignee, notify), container details, commodities")
add_bullet("3. SI submitted → processed via EDI (EDI110 format)")
add_bullet("4. Customer can track SI status and make amendments")

add_heading("9.5 VGM (Verified Gross Mass) Flow", level=2)
add_bullet("1. Customer selects booking/BL → enters VGM declaration details")
add_bullet("2. Weight validation and conversion (WeightConversion utility)")
add_bullet("3. Container tare weight lookup via VDS")
add_bullet("4. VGM submitted and confirmation sent")

add_heading("9.6 Online Payment Flow", level=2)
add_para("Source: EcomOnlinePaymentBean.java (Stripe integration)")
add_bullet("1. Customer selects invoices for payment → creates payment request (createPayment)")
add_bullet("2. System creates Stripe PaymentIntent with amount in cents")
add_bullet("3. Customer completes Stripe checkout")
add_bullet("4. Stripe webhook notifies system → updatePaymentByStripeEvent")
add_bullet("5. Payment status updated; receipt generated")
add_bullet("6. Refund capability via processRefund method")

doc.add_page_break()

# =========================================================================
#  10. UI/UX PATTERN INVENTORY (Scope Note)
# =========================================================================
add_heading("10. UI/UX Pattern Inventory — Scope Note", level=1)

add_para(
    "IMPORTANT: This module (e-comOnline-EJBModule_New) is a pure backend EJB JAR. It contains "
    "ZERO frontend files — no JSP, HTML, CSS, JavaScript, or template files. All UI/UX code, "
    "design system, responsive behavior, and visual patterns reside in the companion web module: "
    "e-comOnlineWeb_New (separate WAR project).",
    bold=True
)

add_para("The following UI/UX-related observations can be inferred from the EJB backend data structures:")

add_heading("10.1 UI Patterns Inferred from Backend VOs", level=2)
add_table(
    ["UI Pattern", "Evidence (Backend VO/Method)", "Inferred UI"],
    [
        ["Dashboard", "DashboardVO, DashboardComparator, getEcomOnGoingDashboard()", "Summary cards, charts, recent activity"],
        ["Booking Form", "EbookingValidatorFormBeanNew (~4K lines), 30+ fields", "Multi-step wizard form"],
        ["Booking List/Grid", "BookingSummaryVo, EbookingViewAll", "Searchable data table with filters"],
        ["BL Summary Table", "BlSummaryVo, BLMasterVo", "Sortable data grid"],
        ["SI Form", "SIMasterVO, SIContainerVO, SIPartiesVO", "Multi-section form with dynamic containers"],
        ["Schedule Search", "ScheduleCommonVO, RouteVo", "Search form + results table/calendar"],
        ["Registration Form", "RegistrationVO, CustomerProfile", "Multi-step registration wizard"],
        ["Payment Page", "OnlinePaymentVO, ReceiptVO", "Invoice selection + Stripe checkout"],
        ["Port Dropdowns", "PortVO, Portdto, MaskPortVO", "Searchable dropdown/autocomplete"],
        ["Equipment Select", "EquipmentVO, Equipments", "Dropdown with type filtering"],
        ["File Upload", "FileDetailsVO, ImageUploadVo", "File upload components (VGM, DG certs)"],
        ["Email Templates", "MailFormatVO, ecom_messageformat table", "Rich HTML email templates"],
        ["Notifications", "NotificationVo, CustomerAdvisoryVo", "Alert/notification banner"],
        ["Admin Panel", "AdminCustomerVo, RegisterMenuVO", "Admin CRUD interface"],
    ],
    col_widths=[1.5, 3.0, 2.5]
)

add_heading("10.2 Multi-Language Support", level=2)
add_para(
    "The ecom_messageformatlang table and Forward_lang_query SQL indicate i18n support for "
    "message templates. The 'lang' column/parameter suggests language-specific message formatting "
    "is available."
)

doc.add_page_break()

# =========================================================================
#  11. ARCHITECTURE DIAGRAMS (Mermaid)
# =========================================================================
add_heading("11. Architecture Diagrams (Mermaid Notation)", level=1)

add_para(
    "The following diagrams are in Mermaid syntax. Paste into any Mermaid renderer "
    "(mermaid.live, VS Code plugin, GitLab/GitHub markdown) to visualize."
)

add_heading("11.1 Overall Application Architecture", level=2)
add_mermaid_text("Overall Application Architecture", """graph TB
    subgraph "Web Tier (e-comOnlineWeb_New)"
        WAR[WAR Module<br/>JSP / Servlets / REST]
    end

    subgraph "EJB Tier (e-comOnline-EJBModule_New)"
        subgraph "App-Tier Beans"
            CTRL[EComCntrlBean<br/>Central Facade]
            BKG[EcomBookingBean<br/>~13K lines]
            BL[EcomBLBean]
            SI[EcomShippingInstructionBean]
            SCH[EcomScheduleBean]
            REG[EcomRegistrationBean]
            PAY[EcomOnlinePaymentBean]
            VGM[EcomVGMBean]
            PH2[EcomPhase2Bean]
            ADM[EcomAdminCntrlBean]
            ANA[EcomAnalyticsBean]
            AMD[EcomAmendmentBookingBean]
            MSK[EcomMaskBean]
        end

        subgraph "V1 Legacy Beans"
            V1B[EcomV1BookingBean]
            V1BL[EcomV1BLBean]
            V1SI[EcomV1SIBean]
            V1D[EcomV1DashboardBean]
            V1M[EcomV1MasterBean]
        end

        subgraph "VDS Layer"
            VDSI[VDS Service Interfaces<br/>@VDS + @FP annotations]
            VDSP[VDSInvoker Proxy]
        end

        subgraph "Utilities"
            JDBC[JDBCServiceLocatorFactory]
            CRYPTO[CryptoLibrary / SHA256]
            MAIL[Mailer / MailerNew]
            VALID[Validation]
        end
    end

    subgraph "Database Tier"
        ECOM[(ECOMTXDS<br/>E-com DB)]
        MASTER[(MASTERDS<br/>Master DB)]
        LRP[(MSSQLDS<br/>LRP/Gate DB)]
        REPORT[(REPORTDS<br/>Report DB)]
        VSS[(VSSDS)]
        ACC[(ACCTXDS<br/>Accounts DB)]
        COM[(COMDS<br/>Commercial DB)]
    end

    subgraph "External Services"
        STRIPE[Stripe API]
        EDI[EDI Gateway]
        SMTP[SMTP Mail Server]
        FTP[FTP Server]
    end

    WAR -->|@Remote RMI| CTRL
    WAR -->|@Remote RMI| BKG
    WAR -->|@Remote RMI| BL
    WAR -->|@Remote RMI| SI

    BKG -->|@EJB| V1B
    CTRL -->|@EJB| V1D

    BKG -->|@Inject| VDSP
    VDSP --> VDSI

    BKG --> JDBC
    PAY --> STRIPE
    BKG --> EDI
    REG --> SMTP""")

add_heading("11.2 Data Flow Architecture", level=2)
add_mermaid_text("Data Flow — Three Access Paths", """graph LR
    subgraph "Session Bean"
        BEAN[EJB Business Logic]
    end

    subgraph "Path A: VDS Proxy"
        VDS_IF[VDS Interface<br/>@VDS annotation]
        VDS_INV[VDSInvoker.create]
        VDS_JAR[VDS.jar<br/>Named Queries]
    end

    subgraph "Path B: Direct JDBC"
        DS[@Resource DataSource]
        PS[PreparedStatement<br/>Inline SQL]
    end

    subgraph "Path C: ServerObject"
        SO[ServerObject DBNAME]
        CONN[getConnection]
    end

    subgraph "Database"
        DB[(SQL Server)]
    end

    BEAN --> VDS_IF
    VDS_IF --> VDS_INV
    VDS_INV --> VDS_JAR
    VDS_JAR --> DB

    BEAN --> DS
    DS --> PS
    PS --> DB

    BEAN --> SO
    SO --> CONN
    CONN --> DB""")

add_heading("11.3 User Purchase Flow", level=2)
add_mermaid_text("User Purchase / Booking Flow", """sequenceDiagram
    actor Customer
    participant Web as Web Tier (WAR)
    participant Ctrl as EComCntrlBean
    participant Sch as EcomScheduleBean
    participant Bkg as EcomBookingBean
    participant V1 as EcomV1BookingBean
    participant Pay as EcomOnlinePaymentBean
    participant DB as SQL Server
    participant Stripe as Stripe API

    Customer->>Web: Login
    Web->>Ctrl: validateLogin()
    Ctrl->>DB: SELECT from ECOM_USERDETAIL
    DB-->>Ctrl: User data
    Ctrl-->>Web: LoginCustomerVo

    Customer->>Web: Search Schedules
    Web->>Sch: getSchedules(port, date)
    Sch-->>Web: ScheduleCommonVO list

    Customer->>Web: Create Booking
    Web->>Bkg: saveBooking(FormBean)
    Bkg->>DB: INSERT ecombooking + related tables
    Bkg->>V1: createEDIBooking()
    V1->>DB: INSERT editempbooking_*
    Bkg-->>Web: BookingVO with booking number

    Customer->>Web: Make Payment
    Web->>Pay: createPayment(OnlinePaymentVO)
    Pay->>DB: INSERT Ecom_OnlinePayment
    Pay->>Stripe: Create PaymentIntent
    Stripe-->>Pay: client_secret
    Pay-->>Web: Payment details

    Customer->>Stripe: Complete checkout
    Stripe->>Web: Webhook event
    Web->>Pay: updatePaymentByStripeEvent()
    Pay->>DB: UPDATE payment status""")

add_heading("11.4 Component Hierarchy", level=2)
add_mermaid_text("EJB Bean Component Hierarchy", """graph TB
    subgraph "Tier 1: Central Facades"
        CTRL[EComCntrlBean]
    end

    subgraph "Tier 2: Domain Beans"
        BKG[EcomBookingBean]
        BL[EcomBLBean]
        SI[EcomSIBean]
        VGM[EcomVGMBean]
        SCH[EcomScheduleBean]
        REG[EcomRegistrationBean]
        PAY[EcomOnlinePaymentBean]
        PH2[EcomPhase2Bean]
        ADM[EcomAdminCntrlBean]
    end

    subgraph "Tier 3: Supporting Beans"
        AMD[EcomAmendmentBookingBean]
        AML[EcomAmendmentMailBean]
        ANA[EcomAnalyticsBean]
        MSK[EcomMaskBean]
        NTC[EcomNoticeBean<br/>Local Only]
    end

    subgraph "Tier 4: Legacy V1 Delegation"
        V1B[EcomV1BookingBean]
        V1BL[EcomV1BLBean]
        V1SI[EcomV1SIBean]
        V1VGM[EcomV1VGMBean]
        V1D[EcomV1DashboardBean]
        V1M[EcomV1MasterBean]
        V1A[EcomV1AmendmentBookingBean]
    end

    CTRL -->|@EJB| V1D
    BKG -->|@EJB| V1B
    BL -->|@EJB| V1BL
    SI -->|@EJB| V1SI
    AMD -->|delegates| AML""")

doc.add_page_break()

# =========================================================================
#  12. ISSUES, TECHNICAL DEBT & RECOMMENDATIONS
# =========================================================================
add_heading("12. Issues, Technical Debt & Recommendations", level=1)

add_heading("12.1 Critical Security Issues", level=2)
add_table(
    ["#", "Issue", "Severity", "Location", "Recommendation"],
    [
        ["S1", "PBEWithMD5AndDES encryption — cryptographically broken", "CRITICAL",
         "common/CryptoLibrary.java", "Replace with AES-256-GCM"],
        ["S2", "Hardcoded encryption passphrase (placeholder text)", "CRITICAL",
         "common/CryptoLibrary.java:25", "Use externalized secrets (vault/keystore)"],
        ["S3", "Static, hardcoded salt for encryption", "HIGH",
         "common/CryptoLibrary.java:27", "Generate random salt per operation"],
        ["S4", "sun.misc.BASE64Encoder/Decoder (removed in Java 11+)", "MEDIUM",
         "common/CryptoLibrary.java", "Use java.util.Base64"],
        ["S5", "No RBAC annotations on EJB methods", "MEDIUM",
         "All session beans", "Add @RolesAllowed security constraints"],
        ["S6", "SQL queries use string concatenation in some places", "HIGH",
         "Various beans", "Ensure all queries use PreparedStatement parameters"],
    ],
    col_widths=[0.4, 2.5, 0.9, 1.8, 1.8]
)

add_heading("12.2 Architecture & Design Issues", level=2)
add_table(
    ["#", "Issue", "Severity", "Location", "Recommendation"],
    [
        ["A1", "God-class anti-pattern: EcomBookingBean ~13,350 lines", "HIGH",
         "AppTierCntrlBean/", "Decompose into focused service classes"],
        ["A2", "No separation of concerns: SQL embedded in session beans", "HIGH",
         "All beans", "Extract DAO/Repository layer"],
        ["A3", "No JPA — raw JDBC throughout (~100s of inline SQL strings)", "MEDIUM",
         "All beans", "Evaluate JPA/Hibernate migration"],
        ["A4", "Dual data-access patterns (VDS + JDBC) with no clear boundary", "MEDIUM",
         "EcomBookingBean, EComCntrlBean", "Standardize on single data-access pattern"],
        ["A5", "V1/V2 dual architecture increases complexity", "MEDIUM",
         "v1/cntlbean/", "Plan legacy migration & deprecation"],
        ["A6", "EbookingValidatorFormBeanNew is ~4,000 lines", "MEDIUM",
         "vo/", "Split into domain-specific validation beans"],
        ["A7", "120+ unorganized VOs in flat package", "LOW",
         "net.ecom.vo/", "Organize into sub-packages by domain"],
    ],
    col_widths=[0.4, 2.8, 0.9, 1.5, 1.8]
)

add_heading("12.3 Build & Deployment Issues", level=2)
add_table(
    ["#", "Issue", "Severity", "Recommendation"],
    [
        ["B1", "System-scoped dependencies with hardcoded Windows paths (D:\\LIB\\ECOM_WILDFLY)",
         "HIGH", "Publish internal JARs to Maven repository (Nexus/Artifactory)"],
        ["B2", "No Maven parent POM — no shared dependency management with web module",
         "MEDIUM", "Create multi-module Maven project"],
        ["B3", "maven-compiler-plugin 2.3.2 is extremely outdated (2011)",
         "LOW", "Upgrade to maven-compiler-plugin 3.x"],
        ["B4", "maven-ejb-plugin 2.3 is outdated",
         "LOW", "Upgrade to maven-ejb-plugin 3.x"],
        ["B5", "No CI/CD configuration found",
         "MEDIUM", "Set up GitLab CI pipeline"],
    ],
    col_widths=[0.4, 3.5, 0.8, 2.5]
)

add_heading("12.4 Code Quality Issues", level=2)
add_table(
    ["#", "Issue", "Severity", "Recommendation"],
    [
        ["Q1", "e.printStackTrace() used throughout — no logging framework",
         "HIGH", "Adopt SLF4J + Logback/Log4j2"],
        ["Q2", "Generic catch(Exception e) blocks — swallows specific exceptions",
         "MEDIUM", "Catch specific exceptions, add exception hierarchy"],
        ["Q3", "No unit tests found",
         "HIGH", "Add JUnit/Arquillian test suite"],
        ["Q4", "Duplicated validation classes: Validation.java + Validation1.java",
         "LOW", "Consolidate into single validation utility"],
        ["Q5", "Duplicated mailer classes: Mailer.java + MailerNew.java",
         "LOW", "Consolidate into single mailer implementation"],
        ["Q6", "Duplicated date classes: DateConversion.java + DateConversion_New.java",
         "LOW", "Consolidate and use java.time API"],
        ["Q7", "Typo in class name: BokingDetailds (should be BookingDetails)",
         "LOW", "Rename class"],
        ["Q8", "No Javadoc documentation on public APIs",
         "MEDIUM", "Add Javadoc to @Remote interface methods"],
        ["Q9", "Hashtable usage (legacy) instead of ConcurrentHashMap",
         "LOW", "Modernize collection usage"],
    ],
    col_widths=[0.4, 3.5, 0.8, 2.5]
)

add_heading("12.5 Performance Concerns", level=2)
add_bullet("Static synchronized methods in JDBCServiceLocatorFactory create potential bottleneck under high concurrency")
add_bullet("No connection pooling visible in the factory — relies entirely on app-server DataSource pool config")
add_bullet("Large beans (~13K lines) may have excessive method count impacting classloading")
add_bullet("No caching layer (Redis/EHCache) — all reads go to database")
add_bullet("VDS + JDBC dual access may cause redundant database calls for the same data")

add_heading("12.6 Accessibility & UX (Backend Perspective)", level=2)
add_para("Since this is a backend module, accessibility analysis applies to the web tier. "
         "However, the following backend issues impact UX:")
add_bullet("Error messages are hardcoded English strings in EcomConsts — no i18n for error messages")
add_bullet("Validation messages use HTML (<br/>) in constants — mixing presentation with logic")
add_bullet("No structured error codes — errors returned as free-text strings")

doc.add_page_break()

# =========================================================================
#  13. FINAL ARCHITECTURE SUMMARY
# =========================================================================
add_heading("13. Final Architecture Summary", level=1)

add_heading("13.1 Complete Tech-Stack Summary", level=2)
add_table(
    ["Layer", "Technology"],
    [
        ["Language", "Java 8"],
        ["Platform", "Java EE 7 / EJB 3.1"],
        ["App Server", "JBoss WildFly"],
        ["Build", "Apache Maven"],
        ["IDE", "NetBeans"],
        ["Database", "Microsoft SQL Server (inferred) — Raw JDBC, no ORM"],
        ["Query Framework", "VDS (Virtual Data Service) — proprietary"],
        ["Serialization", "Google Gson 2.3"],
        ["Email", "JavaMail via IMail CDI service"],
        ["Payments", "Stripe API"],
        ["EDI", "EDI109/EDI110 message processing"],
        ["Reports", "Jasper Reports"],
        ["Crypto", "PBEWithMD5AndDES (deprecated), SHA-256"],
        ["File I/O", "Apache Commons IO 2.4"],
    ],
    col_widths=[1.8, 5.0]
)

add_heading("13.2 Architecture Summary", level=2)
add_table(
    ["Aspect", "Assessment"],
    [
        ["Pattern", "Classic 3-tier Java EE: Web (WAR) → EJB (JAR) → Database"],
        ["Bean Style", "@Stateless Session Beans with @Remote interfaces (21 total)"],
        ["Data Access", "Mixed: VDS proxy + inline JDBC + ServerObject — NO JPA"],
        ["Legacy", "Dual V1/V2 architecture for LRP backward compatibility"],
        ["Strengths", "Clear domain separation, VDS abstraction layer, multi-tenant support"],
        ["Weaknesses", "God-class beans, no DAO layer, no tests, security vulnerabilities"],
        ["Scalability", "Stateless design is horizontally scalable; DB is the bottleneck"],
    ],
    col_widths=[1.5, 5.5]
)

add_heading("13.3 Strengths", level=2)
add_bullet("Clear domain module boundaries — each EJB bean handles a specific business domain")
add_bullet("VDS abstraction provides a clean, declarative query interface")
add_bullet("Stateless architecture enables horizontal scaling")
add_bullet("Multi-agency/multi-tenant support built in (MultiAgencyUtil)")
add_bullet("Comprehensive coverage of maritime/shipping workflows (Booking → SI → BL → VGM → Payment)")
add_bullet("V1/V2 dual architecture allows gradual migration from legacy system")
add_bullet("Container-managed transactions (CMT) provide automatic rollback on failure")

add_heading("13.4 Weaknesses", level=2)
add_bullet("God-class anti-pattern: EcomBookingBean at ~13,350 lines is unmaintainable")
add_bullet("No separation of concerns: SQL queries embedded directly in business logic beans")
add_bullet("Critical security vulnerabilities in CryptoLibrary (broken cipher, hardcoded secrets)")
add_bullet("No test suite — zero unit or integration tests")
add_bullet("No structured logging — e.printStackTrace() throughout")
add_bullet("Hardcoded Windows library paths break cross-platform builds")
add_bullet("Outdated dependencies (Gson 2.3 from 2014, Commons IO 2.4)")
add_bullet("No API documentation (no Javadoc, no README)")

add_heading("13.5 Priority Recommendations", level=2)
add_table(
    ["Priority", "Action", "Impact"],
    [
        ["P0 (Immediate)", "Replace CryptoLibrary with modern AES-256 encryption", "Security"],
        ["P0 (Immediate)", "Externalize encryption keys from source code", "Security"],
        ["P1 (Short-term)", "Add SLF4J logging framework, remove e.printStackTrace()", "Operations"],
        ["P1 (Short-term)", "Add JUnit test suite for critical business logic", "Quality"],
        ["P1 (Short-term)", "Publish internal JARs to Maven repository", "Build"],
        ["P2 (Medium-term)", "Decompose EcomBookingBean into focused service classes", "Maintainability"],
        ["P2 (Medium-term)", "Extract DAO/Repository layer from session beans", "Architecture"],
        ["P2 (Medium-term)", "Add @RolesAllowed security annotations", "Security"],
        ["P3 (Long-term)", "Evaluate JPA migration for persistence layer", "Architecture"],
        ["P3 (Long-term)", "Plan V1 legacy deprecation and migration", "Simplification"],
        ["P3 (Long-term)", "Upgrade to Jakarta EE 10 / Java 17+", "Modernization"],
    ],
    col_widths=[1.3, 3.5, 1.5]
)

doc.add_paragraph()
add_para("— End of Document —", bold=True, italic=True)

# =========================================================================
#  SAVE
# =========================================================================
output = "/home/sekarnagarajan/Public/LocalD/E-com Application/E_com My/e-comOnline-EJBModule_New/E-Commerce_EJB_Module_Technical_Audit.docx"
doc.save(output)
print(f"Document saved to:\n{output}")
