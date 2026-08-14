<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="card-header page_header fixed-top">
    <img class="header_icon_pages" src="Design/assets/images/page_icons/booking.png" /> Payment Cancelled
</div>

<div class="details_search page_bottom_content">
    <div class="card card-shadow">
        <div class="card-body" style="padding:40px 30px;">

            <div style="text-align:center; margin-bottom:25px;">
                <i class="fa fa-times-circle" style="font-size:50px; color:#dc3545;"></i>
                <h4 style="color:#dc3545; margin:10px 0 5px;">Payment Cancelled</h4>
                <p style="color:#888; font-size:13px; margin:0;">
                    Your payment has been cancelled. No amount has been charged.
                </p>
            </div>

            <hr style="margin:30px auto 20px; max-width:400px; border-color:#e0e0e0;">

            <div style="text-align:center; display:flex; justify-content:center; gap:12px; flex-wrap:wrap;">
                <a href="ViewBookingAll.do?BookingRequestType=ViewBookingAll" class="btn btn-info" style="padding:10px 30px;">
                    <i class="fa fa-arrow-left"></i> Back to Bookings
                </a>
                <a href="Welcome.do" class="btn btn-outline-secondary" style="padding:10px 30px;">
                    <i class="fa fa-home"></i> Back to Home
                </a>
            </div>

        </div>
    </div>
</div>
