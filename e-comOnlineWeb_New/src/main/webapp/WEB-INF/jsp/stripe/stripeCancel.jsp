<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="card-header page_header fixed-top">
    <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Payment Status
</div>

<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body" style="text-align:center; padding:50px 30px;">

            <div style="margin-bottom:20px;">
                <i class="fa fa-times-circle" style="font-size:60px; color:#dc3545;"></i>
            </div>
            <h4 style="color:#dc3545; margin-bottom:10px;">Payment Cancelled</h4>
            <p style="color:#666; font-size:14px;">Your payment has been cancelled. No amount has been charged.</p>

            <hr style="margin:25px auto; max-width:300px; border-color:#e0e0e0;">

            <div style="display:flex; justify-content:center; gap:12px; flex-wrap:wrap;">
                <a href="paymentHistory.do" class="btn btn-info" style="padding:10px 30px;">
                    <i class="fa fa-list"></i> Payment History
                </a>
                <a href="Welcome.do" class="btn btn-outline-secondary" style="padding:10px 30px;">
                    <i class="fa fa-home"></i> Back to Home
                </a>
            </div>

        </div>
    </div>
</div>
