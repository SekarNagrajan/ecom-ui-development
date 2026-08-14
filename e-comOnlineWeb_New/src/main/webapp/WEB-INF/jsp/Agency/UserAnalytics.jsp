<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"
%> <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %> <%@ taglib
uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <!-- amCharts 5 CDN -->
  <script src="Design/assets/plugins/amcharts5/index.js"></script>
  <script src="Design/assets/plugins/amcharts5/xy.js"></script>
  <script src="Design/assets/plugins/amcharts5/Animated.js"></script>
  
  <!-- XLSX Library for Excel Export -->
  <script src="Design/assets/js/xlsx.full.min.js"></script>

  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
        sans-serif;
      margin: 0;
      padding: 10px;
      background-color: #f5f5f5;
      color: #333;
    }

    h1 {
      text-align: center;
      color: #2c3e50;
      margin-bottom: 30px;
      font-size: 2.5em;
      font-weight: 300;
    }

    .grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 16px;
      width: 100%;
      max-width: none;
      margin: 0;
      padding: 0 20px;
    }

    @media (max-width: 920px) {
      .grid {
        grid-template-columns: 1fr;
      }
    }

    .card {
      background: #fff;
      border: 1px solid #e5e7eb;
      border-radius: 12px;
      padding: 15px;
      box-shadow: 0 2px 2px rgba(0, 0, 0, 0.1);
      transition: box-shadow 0.3s ease;
      height: fit-content;
      min-height: 320px;
      display: flex;
      flex-direction: column;
    }

    .card:hover {
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
    }

    .chart {
      width: 100%;
      height: 240px;
      min-height: 240px;
      max-height: 240px;
      overflow: hidden;
      flex: 1;
    }

    h3 {
      margin-top: 0;
      margin-bottom: 10px;
      color: #34495e;
      font-size: 1.1em;
      font-weight: 500;
      border-bottom: 2px solid #3498db;
      padding-bottom: 6px;
      flex-shrink: 0;
    }

    .error-fallback {
      display: none;
      color: #e74c3c;
      text-align: center;
      padding: 20px;
      background: #fdf2f2;
      border: 1px solid #fecaca;
      border-radius: 8px;
    }
  </style>
</head>
<div class="card-header page_header fixed-top">
  <img
    class="header_icon_pages"
    src="Design/assets/images/page_icons/booking.png"
  />
  User Analytics
</div>

<!-- Date Range Header Component -->
<style>
  .filter-container {
    background: #fff;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 16px 24px;
    margin: 20px auto;
    max-width: 1200px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }

  .filter-wrapper {
    display: flex;
    align-items: center;
    gap: 20px;
    flex-wrap: wrap;
  }

  .date-range-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex: 1;
    min-width: 600px;
  }

  .customer-filter-group {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .customer-filter-group label {
    font-weight: 500;
    color: #495057;
    white-space: nowrap;
    margin: 0;
  }

  .customer-filter-group input {
    padding: 6px 12px;
    border: 1px solid #ced4da;
    border-radius: 4px;
    font-size: 14px;
    min-width: 200px;
  }

  .customer-filter-group input:focus {
    outline: none;
    border-color: #3498db;
    box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
  }

  /* Autocomplete Styles */
  .autocomplete-container {
    position: relative;
    display: inline-block;
    width: 100%;
  }

  .autocomplete-dropdown {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    background: white;
    border: 1px solid #ced4da;
    border-top: none;
    border-radius: 0 0 4px 4px;
    max-height: 200px;
    overflow-y: auto;
    z-index: 1000;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .autocomplete-item {
    padding: 8px 12px;
    cursor: pointer;
    border-bottom: 1px solid #f8f9fa;
    font-size: 14px;
    transition: background-color 0.2s ease;
  }

  .autocomplete-item:hover {
    background-color: #f8f9fa;
  }

  .autocomplete-item:last-child {
    border-bottom: none;
  }

  .autocomplete-item.selected {
    background-color: #3498db;
    color: white;
  }

  .autocomplete-item .customer-code {
    font-weight: 600;
    color: #495057;
  }

  .autocomplete-item .customer-name {
    color: #6c757d;
    margin-left: 8px;
  }

  .autocomplete-item.selected .customer-code,
  .autocomplete-item.selected .customer-name {
    color: white;
  }

  .loading-icon {
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    color: #3498db;
    font-size: 14px;
    z-index: 10;
  }

  .filter-actions {
    display: flex;
    gap: 8px;
  }

  .filter-btn {
    background: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 4px;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.2s ease;
    font-size: 14px;
    color: #495057;
  }

  .filter-btn:hover {
    background: #e9ecef;
    border-color: #adb5bd;
  }

  .filter-container .filter-btn.btn-search {
    background: #28a745;
    color: #fff;
    border-color: #28a745;
  }

  .filter-container .filter-btn.btn-search:hover {
    background: #218838;
    border-color: #218838;
  }

  .filter-container .filter-btn.btn-reset {
    background: #6c757d;
    color: #fff;
    border-color: #6c757d;
  }

  .filter-container .filter-btn.btn-reset:hover {
    background: #5a6268;
    border-color: #5a6268;
  }

  @media (max-width: 768px) {
    .filter-wrapper {
      flex-direction: column;
      align-items: stretch;
    }

    .date-range-header {
      min-width: auto;
    }

    .customer-filter-group {
      flex-direction: column;
      align-items: stretch;
    }
  }

  .date-range-title {
    font-size: 18px;
    font-weight: 600;
    color: #2c3e50;
    min-width: 280px;
    text-align: center;
  }

  .date-range-controls {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .date-range-nav {
    background: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 4px;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.2s ease;
    font-size: 18px;
    color: #495057;
    user-select: none;
  }

  .date-range-nav:hover:not(.disabled) {
    background: #e9ecef;
    border-color: #adb5bd;
  }

  .date-range-nav.disabled {
    opacity: 0.4;
    cursor: not-allowed;
    pointer-events: none;
  }

  .date-range-pills {
    display: flex;
    gap: 8px;
    background: #f8f9fa;
    padding: 4px;
    border-radius: 6px;
  }

  .date-range-pill {
    padding: 6px 16px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    font-weight: 500;
    transition: all 0.2s ease;
    color: #6c757d;
    user-select: none;
  }

  .date-range-pill:hover {
    background: #e9ecef;
  }

  .date-range-pill.active {
    background: #3498db;
    color: #fff;
  }

  /* Custom Date Picker Styles */
  .date-picker-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: none;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }

  .date-picker-container {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    max-width: 400px;
    width: 90%;
  }

  .date-picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e5e7eb;
  }

  .date-picker-title {
    font-size: 18px;
    font-weight: 600;
    color: #2c3e50;
  }

  .date-picker-close {
    background: none;
    border: none;
    font-size: 24px;
    cursor: pointer;
    color: #6c757d;
    padding: 0;
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .date-picker-close:hover {
    color: #495057;
  }

  .date-picker-nav {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }

  .date-picker-nav-btn {
    background: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 4px;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    color: #495057;
    font-size: 16px;
  }

  .date-picker-nav-btn:hover {
    background: #e9ecef;
    border-color: #adb5bd;
  }

  .date-picker-nav-btn.disabled {
    opacity: 0.4;
    cursor: not-allowed;
  }

  .date-picker-year {
    font-size: 18px;
    font-weight: 600;
    color: #2c3e50;
    display: block !important;
    visibility: visible !important;
    opacity: 1 !important;
    text-align: center;
    min-width: 150px;
    background-color: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 4px;
    padding: 8px 12px;
    height: auto !important;
    min-height: 40px !important;
  }

  .date-picker-month-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 8px;
    margin-bottom: 20px;
  }

  .date-picker-month {
    padding: 12px 8px;
    text-align: center;
    border-radius: 6px;
    cursor: pointer;
    font-size: 14px;
    font-weight: 500;
    color: #495057;
    transition: all 0.2s ease;
  }

  .date-picker-month:hover {
    background: #f8f9fa;
  }

  .date-picker-month.selected {
    background: #3498db;
    color: white;
  }

  .date-picker-calendar {
    display: none;
  }

  .date-picker-calendar.active {
    display: block;
  }

  .date-picker-weekdays {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 2px;
    margin-bottom: 8px;
  }

  .date-picker-weekday {
    text-align: center;
    font-size: 12px;
    font-weight: 600;
    color: #6c757d;
    padding: 8px 4px;
  }

  .date-picker-days {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 2px;
  }

  .date-picker-day {
    aspect-ratio: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    color: #495057;
    transition: all 0.2s ease;
    position: relative;
  }

  .date-picker-day:hover {
    background: #f8f9fa;
  }

  .date-picker-day.other-month {
    color: #adb5bd;
  }

  .date-picker-day.selected {
    background: #3498db;
    color: white;
  }

  .date-picker-day.range-start,
  .date-picker-day.range-end {
    background: #2c3e50;
    color: white;
  }

  .date-picker-day.in-range {
    background: #e3f2fd;
    color: #1976d2;
  }

  .date-picker-day.disabled {
    color: #adb5bd;
    cursor: not-allowed;
    opacity: 0.5;
  }

  .date-picker-day.disabled:hover {
    background: transparent;
  }

  .date-picker-month.disabled {
    color: #adb5bd;
    cursor: not-allowed;
    opacity: 0.5;
  }

  .date-picker-month.disabled:hover {
    background: transparent;
  }
</style>
<div class="filter-container">
  <div class="filter-wrapper">
<div class="date-range-header">
  <div class="date-range-nav" id="prevBtn" onclick="navigatePrev()">
    <i class="fa fa-chevron-left"></i>
  </div>
  <div class="date-range-title" id="rangeTitle">Loading...</div>
  <div class="date-range-controls">
    <div class="date-range-pills">
      <div class="date-range-pill" id="pillWeek" onclick="switchMode('week')"> Week</div>
      <div class="date-range-pill" id="pillMonth" onclick="switchMode('month')"> Month</div>
    </div>
    <div class="date-range-nav" id="nextBtn" onclick="navigateNext()">
      <i class="fa fa-chevron-right"></i>
    </div>
  </div>
</div>
 <!-- Customer Filter with Autocomplete -->
 <div class="customer-filter-group">
  <label for="customerFilter">Customer Code/Name:</label>
  <div class="autocomplete-container">
    <input type="text" id="customerFilter" name="customerFilter" placeholder="Search customer..." autocomplete="off" />
    <div class="loading-icon" id="customerLoadingIcon" style="display: none;">
      <i class="fa fa-spinner fa-spin"></i>
    </div>
    <div class="autocomplete-dropdown" id="customerDropdown" style="display: none;">
      <!-- Autocomplete suggestions will be populated here -->
    </div>
  </div>
</div>

<!-- Action Buttons -->
<div class="filter-actions">
  <button class="filter-btn btn-reset" onclick="resetFilters()" title="Reset">
    <i class="fa fa-refresh"></i>
  </button>
  <button class="filter-btn btn-search" onclick="exportToExcel()" title="Export Excel">
    <i class="fa fa-file-excel-o"></i>
  </button>
</div>
</div>
</div>

<form id="rangeForm" style="display: none;">
  <input type="hidden" id="rangeInput" name="range" value="" />
  <input type="hidden" id="startInput" name="start" value="" />
  <input type="hidden" id="endInput" name="end" value="" />
</form>

<!-- Custom Date Picker Modal -->
<div class="date-picker-modal" id="datePickerModal">
  <div class="date-picker-container">
    <div class="date-picker-header">
      <div class="date-picker-title" id="datePickerTitle">Select Date</div>
      <button class="date-picker-close" id="datePickerClose">&times;</button>
    </div>
    
    <!-- Month Selection View -->
    <div class="date-picker-month-view" id="monthView">
      <div class="date-picker-nav">
        <button class="date-picker-nav-btn" id="prevYearBtn">&lt;</button>
        <div class="date-picker-year" id="currentYear">2025</div>
        <button class="date-picker-nav-btn" id="nextYearBtn">&gt;</button>
      </div>
      <div class="date-picker-month-grid" id="monthGrid">
        <div class="date-picker-month" data-month="0">Jan</div>
        <div class="date-picker-month" data-month="1">Feb</div>
        <div class="date-picker-month" data-month="2">Mar</div>
        <div class="date-picker-month" data-month="3">Apr</div>
        <div class="date-picker-month" data-month="4">May</div>
        <div class="date-picker-month" data-month="5">Jun</div>
        <div class="date-picker-month" data-month="6">Jul</div>
        <div class="date-picker-month" data-month="7">Aug</div>
        <div class="date-picker-month" data-month="8">Sep</div>
        <div class="date-picker-month" data-month="9">Oct</div>
        <div class="date-picker-month" data-month="10">Nov</div>
        <div class="date-picker-month" data-month="11">Dec</div>
      </div>
    </div>
    
    <!-- Calendar View for Week Selection -->
    <div class="date-picker-calendar" id="calendarView">
      <div class="date-picker-nav">
        <button class="date-picker-nav-btn" id="prevYearBtnCalendar">&lt;&lt;</button>
        <button class="date-picker-nav-btn" id="prevMonthBtn">&lt;</button>
        <div class="date-picker-year" id="currentMonthYear">October 2025</div>
        <button class="date-picker-nav-btn" id="nextMonthBtn">&gt;</button>
        <button class="date-picker-nav-btn" id="nextYearBtnCalendar">&gt;&gt;</button>
      </div>
      <div class="date-picker-weekdays">
        <div class="date-picker-weekday">S</div>
        <div class="date-picker-weekday">M</div>
        <div class="date-picker-weekday">T</div>
        <div class="date-picker-weekday">W</div>
        <div class="date-picker-weekday">T</div>
        <div class="date-picker-weekday">F</div>
        <div class="date-picker-weekday">S</div>
      </div>
      <div class="date-picker-days" id="calendarDays">
        <!-- Days will be populated by JavaScript -->
      </div>
    </div>
    
  </div>
</div>

<%
  // Server-side timestamp so JS date math stays in the DB's timezone
  String _serverNow = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                          .format(new java.util.Date());
%>
<script>
  // Baseline: server time captured at page render (same TZ as DB GETDATE())
  const _SERVER_RENDER_TIME = new Date('<%= _serverNow %>');
  const _CLIENT_RENDER_TIME = new Date();

  // Returns "now" adjusted to the server/DB timezone
  function serverNow() {
    const elapsed = Date.now() - _CLIENT_RENDER_TIME.getTime();
    return new Date(_SERVER_RENDER_TIME.getTime() + elapsed);
  }

  // Date utility functions
  function toSql(dt) {
    const pad = (n) => String(n).padStart(2, '0');
    return dt.getFullYear() + '-' + 
           pad(dt.getMonth() + 1) + '-' + 
           pad(dt.getDate()) + ' ' + 
           pad(dt.getHours()) + ':' + 
           pad(dt.getMinutes()) + ':' + 
           pad(dt.getSeconds());
  }

  function startOfDay(dt) {
    const d = new Date(dt);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  function endOfDay(dt) {
    const d = new Date(dt);
    d.setHours(23, 59, 59, 999);
    return d;
  }

  function startOfWeekSunday(dt) {
    const d = new Date(dt);
    const day = d.getDay();
    const diff = day; // Sunday is 0
    d.setDate(d.getDate() - diff);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  function endOfWeekSaturday(dt) {
    const d = new Date(dt);
    const day = d.getDay();
    const diff = 6 - day; // Saturday is 6
    d.setDate(d.getDate() + diff);
    d.setHours(23, 59, 59, 999);
    return d;
  }

  function startOfMonth(dt) {
    const d = new Date(dt);
    d.setDate(1);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  function endOfMonth(dt) {
    const d = new Date(dt);
    d.setMonth(d.getMonth() + 1);
    d.setDate(0);
    d.setHours(23, 59, 59, 999);
    return d;
  }

  function isSameWeek(d1, d2) {
    const start1 = startOfWeekSunday(d1);
    const start2 = startOfWeekSunday(d2);
    return start1.getTime() === start2.getTime();
  }

  function isSameMonth(d1, d2) {
    return d1.getFullYear() === d2.getFullYear() && d1.getMonth() === d2.getMonth();
  }

  function formatDate(dt) {
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    const pad = (n) => String(n).padStart(2, '0');
    return pad(dt.getDate()) + ' ' + months[dt.getMonth()] + ' ' + dt.getFullYear();
  }

  function formatMonthYear(dt) {
    const months = ['January', 'February', 'March', 'April', 'May', 'June', 
                    'July', 'August', 'September', 'October', 'November', 'December'];
    return months[dt.getMonth()] + ' ' + dt.getFullYear();
  }

  // State management
  let currentMode = 'month'; // 'week' or 'month' - default to month
  let anchorDate = serverNow(); // The date we're viewing (server TZ)

  function parseUrlParams() {
    const params = new URLSearchParams(window.location.search);
    const range = params.get('range');
    const start = params.get('start');
    const end = params.get('end');
    
    if (range && start && end) {
      currentMode = range;
      // Parse start date to set anchor
      const startDate = new Date(start.replace(' ', 'T'));
      if (!isNaN(startDate.getTime())) {
        anchorDate = startDate;
      }
      return true;
    }
    return false;
  }

  function computeRange() {
    const now = serverNow();
    let start, end, title;

    if (currentMode === 'month') {
      start = startOfMonth(anchorDate);
      const isCurrentMonth = isSameMonth(anchorDate, now);
      
      if (isCurrentMonth) {
        end = now;
      } else {
        end = endOfMonth(anchorDate);
      }
      
      title = formatMonthYear(anchorDate);
    } else { // week
      start = startOfWeekSunday(anchorDate);
      const isCurrentWeek = isSameWeek(anchorDate, now);
      
      if (isCurrentWeek) {
        end = now;
      } else {
        end = endOfWeekSaturday(anchorDate);
      }
      
      const endDisplay = isCurrentWeek ? now : endOfWeekSaturday(anchorDate);
      title = formatDate(start) + ' - ' + formatDate(endDisplay);
    }

    return { start, end, title };
  }

  function updateUI() {
    const { start, end, title } = computeRange();
    const now = serverNow();

    // Update title
    document.getElementById('rangeTitle').textContent = title;

    // Update active pill
    document.getElementById('pillWeek').classList.toggle('active', currentMode === 'week');
    document.getElementById('pillMonth').classList.toggle('active', currentMode === 'month');

    // Update hidden inputs
    document.getElementById('rangeInput').value = currentMode;
    document.getElementById('startInput').value = toSql(start);
    document.getElementById('endInput').value = toSql(end);

    // Check if next period would be in future
    let nextPeriodStart;
    if (currentMode === 'month') {
      const nextMonth = new Date(anchorDate);
      nextMonth.setMonth(nextMonth.getMonth() + 1);
      nextPeriodStart = startOfMonth(nextMonth);
    } else {
      const nextWeek = new Date(anchorDate);
      nextWeek.setDate(nextWeek.getDate() + 7);
      nextPeriodStart = startOfWeekSunday(nextWeek);
    }

    const nextBtn = document.getElementById('nextBtn');
    if (nextPeriodStart > now) {
      nextBtn.classList.add('disabled');
    } else {
      nextBtn.classList.remove('disabled');
    }

    // Dispatch custom event
    window.dispatchEvent(new CustomEvent('rangechange', {
      detail: {
        range: currentMode,
        start: toSql(start),
        end: toSql(end)
      }
    }));
  }

  // Common function to handle all date changes
  function handleDateChange(action, mode) {
    console.log('Date change action:', action, 'Mode:', mode);
    
    if (action === 'switchMode') {
      if (currentMode !== mode) {
        currentMode = mode;
        // Reset anchor to current date when switching modes
        anchorDate = serverNow();
        updateUI();
        logDateValues();
        // Reload all charts with new date range
        loadAllCharts();
      }
    } else if (action === 'navigatePrev') {
      if (currentMode === 'month') {
        anchorDate.setMonth(anchorDate.getMonth() - 1);
      } else {
        anchorDate.setDate(anchorDate.getDate() - 7);
      }
      updateUI();
      logDateValues();
      // Reload all charts with new date range
      loadAllCharts();
    } else if (action === 'navigateNext') {
      const now = serverNow();
      let nextPeriodStart;
      
      if (currentMode === 'month') {
        const nextMonth = new Date(anchorDate);
        nextMonth.setMonth(nextMonth.getMonth() + 1);
        nextPeriodStart = startOfMonth(nextMonth);
      } else {
        const nextWeek = new Date(anchorDate);
        nextWeek.setDate(nextWeek.getDate() + 7);
        nextPeriodStart = startOfWeekSunday(nextWeek);
      }

      // Don't navigate if next period is in future
      if (nextPeriodStart > now) {
        return;
      }

      if (currentMode === 'month') {
        anchorDate.setMonth(anchorDate.getMonth() + 1);
      } else {
        anchorDate.setDate(anchorDate.getDate() + 7);
      }
      updateUI();
      logDateValues();
      // Reload all charts with new date range
      loadAllCharts();
    }
  }

  // Function to log current date values
  function logDateValues() {
    const startValue = document.getElementById('startInput').value;
    const endValue = document.getElementById('endInput').value;
    const rangeValue = document.getElementById('rangeInput').value;
    const customerInput = document.getElementById('customerFilter');
    const customerValue = customerInput.value;
    const customerCode = customerInput.dataset.customerCode;
    const finalCustomer = customerCode || customerValue;
    
    console.log('=== Date Values ===');
    console.log('Range:', rangeValue);
    console.log('Start:', startValue);
    console.log('End:', endValue);
    console.log('Customer (display):', customerValue);
    console.log('Customer (code):', customerCode);
    console.log('Customer (final):', finalCustomer);
    console.log('==================');
  }

  function switchMode(mode) {
    handleDateChange('switchMode', mode);
  }

  function navigatePrev() {
    handleDateChange('navigatePrev');
  }

  function navigateNext() {
    handleDateChange('navigateNext');
  }

  // Action button functions
  function applyFilters() {
    console.log('Applying filters...');
    logDateValues();
    loadAllCharts();
  }

  function resetFilters() {
    console.log('Resetting filters...');
    // Reset to current month
    currentMode = 'month';
    anchorDate = serverNow();
    // Clear customer filter completely
    const customerInput = document.getElementById('customerFilter');
    customerInput.value = '';
    customerInput.dataset.customerCode = '';
    // Hide any open dropdown
    hideDropdown();
    updateUI();
    loadAllCharts();
  }

  function exportToExcel() {
    console.log('Exporting to Excel...');
    const startDate = document.getElementById('startInput').value;
    const endDate = document.getElementById('endInput').value;
    const customer = document.getElementById('customerFilter').value;
    
    // Build export URL
    let exportUrl = 'UserAnalytics.do?action=export&start=' + encodeURIComponent(startDate) + 
                   '&end=' + encodeURIComponent(endDate);
    if (customer && customer.trim() !== '') {
      exportUrl += '&customer=' + encodeURIComponent(customer.trim());
    }
    
    // Open in new window to trigger download
    window.open(exportUrl, '_blank');
  }

  // Initialize on page load
  (function initDateRange() {
    // Try to parse URL params first
    if (!parseUrlParams()) {
      // Default to current month
      currentMode = 'month';
      anchorDate = serverNow();
    }
    updateUI();
  })();

  // Helper function to get month name
  function getMonthName(monthIndex) {
    const months = ['January', 'February', 'March', 'April', 'May', 'June',
                   'July', 'August', 'September', 'October', 'November', 'December'];
    const index = parseInt(monthIndex) || 0;
    const monthName = months[index] || 'January';
    console.log('getMonthName - input:', monthIndex, 'parsed:', index, 'result:', monthName);
    return monthName;
  }

  // Custom Date Picker Functionality
  let datePickerState = {
    isOpen: false,
    currentView: 'month', // 'month' or 'calendar'
    selectedYear: serverNow().getFullYear(),
    selectedMonth: serverNow().getMonth(),
    selectedDate: null,
    rangeStart: null,
    rangeEnd: null,
    isSelectingRange: false
  };

  // Initialize date picker
  function initDatePicker() {
    const modal = document.getElementById('datePickerModal');
    const closeBtn = document.getElementById('datePickerClose');
    const rangeTitle = document.getElementById('rangeTitle');

    // Remove any existing event listeners to prevent duplicates
    const newCloseBtn = closeBtn.cloneNode(true);
    closeBtn.parentNode.replaceChild(newCloseBtn, closeBtn);
    
    const newRangeTitle = rangeTitle.cloneNode(true);
    rangeTitle.parentNode.replaceChild(newRangeTitle, rangeTitle);

    // Close modal handlers
    newCloseBtn.addEventListener('click', function(e) {
      e.stopPropagation();
      closeDatePicker();
    });
    modal.addEventListener('click', function(e) {
      if (e.target === modal) {
        closeDatePicker();
      }
    });

    // Month selection handlers
    const monthElements = document.querySelectorAll('.date-picker-month');
    monthElements.forEach(month => {
      month.addEventListener('click', function(e) {
        e.stopPropagation();
        if (!this.classList.contains('disabled')) {
          const monthIndex = parseInt(this.dataset.month);
          selectMonth(monthIndex);
        }
      });
    });

    // Year navigation handlers
    document.getElementById('prevYearBtn').addEventListener('click', function(e) {
      e.stopPropagation();
      datePickerState.selectedYear--;
      updateMonthView();
    });

    document.getElementById('nextYearBtn').addEventListener('click', function(e) {
      e.stopPropagation();
      const now = serverNow();
      if (datePickerState.selectedYear < now.getFullYear()) {
        datePickerState.selectedYear++;
        updateMonthView();
      }
    });

    // Month navigation handlers for calendar view
    document.getElementById('prevMonthBtn').addEventListener('click', function(e) {
      e.stopPropagation();
      datePickerState.selectedMonth--;
      if (datePickerState.selectedMonth < 0) {
        datePickerState.selectedMonth = 11;
        datePickerState.selectedYear--;
      }
      updateCalendarView();
    });

    document.getElementById('nextMonthBtn').addEventListener('click', function(e) {
      e.stopPropagation();
      const now = serverNow();
      const nextMonth = new Date(datePickerState.selectedYear, datePickerState.selectedMonth + 1, 1);
      if (nextMonth <= now) {
        datePickerState.selectedMonth++;
        if (datePickerState.selectedMonth > 11) {
          datePickerState.selectedMonth = 0;
          datePickerState.selectedYear++;
        }
        updateCalendarView();
      }
    });

    // Year navigation handlers for calendar view
    document.getElementById('prevYearBtnCalendar').addEventListener('click', function(e) {
      e.stopPropagation();
      datePickerState.selectedYear--;
      updateCalendarView();
    });

    document.getElementById('nextYearBtnCalendar').addEventListener('click', function(e) {
      e.stopPropagation();
      const now = serverNow();
      if (datePickerState.selectedYear < now.getFullYear()) {
        datePickerState.selectedYear++;
        updateCalendarView();
      }
    });

    // Click handler for date-range-title - open correct picker based on current mode
    newRangeTitle.addEventListener('click', function() {
      openDatePicker();
    });

    // Make range title clickable
    newRangeTitle.style.cursor = 'pointer';
  }

  function openDatePicker() {
    const modal = document.getElementById('datePickerModal');
    const monthView = document.getElementById('monthView');
    const calendarView = document.getElementById('calendarView');
    const title = document.getElementById('datePickerTitle');

    // Close any existing modal first
    if (datePickerState.isOpen) {
      closeDatePicker();
      // Small delay to ensure modal is fully closed
      setTimeout(() => {
        openDatePicker();
      }, 50);
      return;
    }

    // Set initial state based on current mode
    datePickerState.selectedYear = anchorDate.getFullYear();
    datePickerState.selectedMonth = anchorDate.getMonth();
    datePickerState.selectedDate = new Date(anchorDate);
    
    console.log('Debug - anchorDate:', anchorDate);
    console.log('Debug - anchorDate.getMonth():', anchorDate.getMonth());
    console.log('Debug - datePickerState after setting:', datePickerState);

    if (currentMode === 'month') {
      // Show month selection view
      datePickerState.currentView = 'month';
      monthView.style.display = 'block';
      calendarView.style.display = 'none';
      calendarView.classList.remove('active');
      title.textContent = 'Select Month';
      updateMonthView();
    } else {
      // Show calendar view for week selection
      datePickerState.currentView = 'calendar';
      monthView.style.display = 'none';
      calendarView.style.display = 'block';
      calendarView.classList.add('active');
      title.textContent = 'Select Week';
      
      // Add a small delay to ensure DOM is ready
      setTimeout(() => {
        updateCalendarView();
      }, 10);
    }

    modal.style.display = 'flex';
    datePickerState.isOpen = true;
  }

  function closeDatePicker() {
    const modal = document.getElementById('datePickerModal');
    modal.style.display = 'none';
    datePickerState.isOpen = false;
    datePickerState.rangeStart = null;
    datePickerState.rangeEnd = null;
    datePickerState.isSelectingRange = false;
    datePickerState.currentView = 'month'; // Reset to default view
  }

  function updateMonthView() {
    const yearElement = document.getElementById('currentYear');
    const monthElements = document.querySelectorAll('.date-picker-month');
    const now = serverNow();
    const currentYear = now.getFullYear();
    const currentMonth = now.getMonth();

    yearElement.textContent = datePickerState.selectedYear;

    // Update selected month and disable future months
    monthElements.forEach(month => {
      const monthIndex = parseInt(month.dataset.month);
      month.classList.remove('selected', 'disabled');
      
      // Disable future months
      if (datePickerState.selectedYear > currentYear || 
          (datePickerState.selectedYear === currentYear && monthIndex > currentMonth)) {
        month.classList.add('disabled');
      }
      
      // Highlight selected month
      if (parseInt(month.dataset.month) === datePickerState.selectedMonth &&
          datePickerState.selectedYear === anchorDate.getFullYear()) {
        month.classList.add('selected');
      }
    });

    // Disable next year button if we're at current year
    const nextYearBtn = document.getElementById('nextYearBtn');
    if (datePickerState.selectedYear >= currentYear) {
      nextYearBtn.classList.add('disabled');
    } else {
      nextYearBtn.classList.remove('disabled');
    }
  }

  function updateCalendarView() {
    const monthYearElement = document.getElementById('currentMonthYear');
    const daysContainer = document.getElementById('calendarDays');
    const months = ['January', 'February', 'March', 'April', 'May', 'June',
                   'July', 'August', 'September', 'October', 'November', 'December'];
    const now = serverNow();
    
    // Test the months array
    console.log('Test - months[9]:', months[9]);
    console.log('Test - months.length:', months.length);

    // Debug: Check if element exists
    if (!monthYearElement) {
      console.error('currentMonthYear element not found!');
      // Try alternative selector
      const altElement = document.querySelector('#currentMonthYear');
      console.log('Alternative selector result:', altElement);
      return;
    }
    
    // Debug element properties
    console.log('Element found:', monthYearElement);
    console.log('Element tagName:', monthYearElement.tagName);
    console.log('Element className:', monthYearElement.className);
    console.log('Element current text:', monthYearElement.textContent);

    console.log('Debug - datePickerState:', datePickerState);
    console.log('Debug - selectedMonth:', datePickerState.selectedMonth, 'type:', typeof datePickerState.selectedMonth);
    console.log('Debug - months array:', months);
    console.log('Debug - months[selectedMonth]:', months[datePickerState.selectedMonth]);
    
    // Use helper function to get month name
    const monthName = getMonthName(datePickerState.selectedMonth);
    const monthYearText = monthName + ' ' + datePickerState.selectedYear;
    console.log('Setting month year text:', monthYearText, 'for month:', datePickerState.selectedMonth, 'year:', datePickerState.selectedYear);
    
    // Simple approach - just set the text
    monthYearElement.textContent = monthYearText;
    
    console.log('Updated month year text:', monthYearText);

    // Clear existing days
    daysContainer.innerHTML = '';

    // Get first day of month and number of days
    const firstDay = new Date(datePickerState.selectedYear, datePickerState.selectedMonth, 1);
    const lastDay = new Date(datePickerState.selectedYear, datePickerState.selectedMonth + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startDayOfWeek = firstDay.getDay(); // 0 = Sunday

    // Add empty cells for days before the first day of the month
    for (let i = 0; i < startDayOfWeek; i++) {
      const emptyDay = document.createElement('div');
      emptyDay.className = 'date-picker-day other-month';
      const prevMonth = new Date(datePickerState.selectedYear, datePickerState.selectedMonth, 0);
      const prevMonthDays = prevMonth.getDate();
      const dayNumber = prevMonthDays - startDayOfWeek + i + 1;
      emptyDay.textContent = dayNumber;
      
      // Check if this day from previous month is part of current week
      const prevMonthDate = new Date(datePickerState.selectedYear, datePickerState.selectedMonth - 1, dayNumber);
      if (isSameWeek(prevMonthDate, anchorDate)) {
        emptyDay.classList.add('in-range');
        if (prevMonthDate.getDate() === anchorDate.getDate() && 
            prevMonthDate.getMonth() === anchorDate.getMonth() &&
            prevMonthDate.getFullYear() === anchorDate.getFullYear()) {
          emptyDay.classList.add('selected');
        }
      }
      
      daysContainer.appendChild(emptyDay);
    }

    // Add days of the current month
    for (let day = 1; day <= daysInMonth; day++) {
      const dayElement = document.createElement('div');
      dayElement.className = 'date-picker-day';
      dayElement.textContent = day;
      dayElement.dataset.day = day;
      dayElement.dataset.month = datePickerState.selectedMonth;
      dayElement.dataset.year = datePickerState.selectedYear;

      const currentDate = new Date(datePickerState.selectedYear, datePickerState.selectedMonth, day);
      
      // Disable future dates
      if (currentDate > now) {
        dayElement.classList.add('disabled');
      } else {
        // Check if this day is in the current week range
        if (isSameWeek(currentDate, anchorDate)) {
          dayElement.classList.add('in-range');
          if (day === anchorDate.getDate() && 
              datePickerState.selectedMonth === anchorDate.getMonth() &&
              datePickerState.selectedYear === anchorDate.getFullYear()) {
            dayElement.classList.add('selected');
          }
        }

        dayElement.addEventListener('click', function(e) {
          e.stopPropagation();
          selectWeekForDate(currentDate);
        });
      }

      daysContainer.appendChild(dayElement);
    }

    // Add empty cells for days after the last day of the month
    const remainingCells = 42 - (startDayOfWeek + daysInMonth); // 6 rows * 7 days = 42
    for (let i = 1; i <= remainingCells; i++) {
      const emptyDay = document.createElement('div');
      emptyDay.className = 'date-picker-day other-month';
      emptyDay.textContent = i;
      
      // Check if this day from next month is part of current week
      const nextMonthDate = new Date(datePickerState.selectedYear, datePickerState.selectedMonth + 1, i);
      if (isSameWeek(nextMonthDate, anchorDate)) {
        emptyDay.classList.add('in-range');
        if (nextMonthDate.getDate() === anchorDate.getDate() && 
            nextMonthDate.getMonth() === anchorDate.getMonth() &&
            nextMonthDate.getFullYear() === anchorDate.getFullYear()) {
          emptyDay.classList.add('selected');
        }
      }
      
      daysContainer.appendChild(emptyDay);
    }

    // Disable next month button if we're at current month
    const nextMonthBtn = document.getElementById('nextMonthBtn');
    const currentMonth = new Date(datePickerState.selectedYear, datePickerState.selectedMonth + 1, 1);
    if (currentMonth > now) {
      nextMonthBtn.classList.add('disabled');
    } else {
      nextMonthBtn.classList.remove('disabled');
    }

    // Disable next year button if we're at current year
    const nextYearBtnCalendar = document.getElementById('nextYearBtnCalendar');
    if (datePickerState.selectedYear >= now.getFullYear()) {
      nextYearBtnCalendar.classList.add('disabled');
    } else {
      nextYearBtnCalendar.classList.remove('disabled');
    }
  }

  function selectMonth(monthIndex) {
    datePickerState.selectedMonth = monthIndex;
    const selectedDate = new Date(datePickerState.selectedYear, monthIndex, 1);
    
    // Update anchor date to the selected month
    anchorDate = selectedDate;
    currentMode = 'month';
    
    // Apply the selection immediately and close
    updateUI();
    logDateValues();
    loadAllCharts();
    closeDatePicker();
  }

  function selectWeekForDate(selectedDate) {
    // Update anchor date to the selected date
    anchorDate = selectedDate;
    currentMode = 'week';
    
    // Apply the selection immediately and close
    updateUI();
    logDateValues();
    loadAllCharts();
    closeDatePicker();
  }

  // Initialize date picker when DOM is loaded
  document.addEventListener('DOMContentLoaded', function() {
    // Initialize date picker after a small delay to ensure all elements are ready
    setTimeout(() => {
      initDatePicker();
    }, 100);
  });
</script>
<div class="container-fluid page_bottom_content">
  <div class="row">

      <div class="grid">
        <!-- Point 1: Pages Visited -->
        <div class="card">
          <h3>Pages Visited </h3>
          <div id="pagesVisitedChart" class="chart"></div>
          <div class="error-fallback" id="pagesVisitedError">
            Chart failed to load. Please check your internet connection.
          </div>
        </div>

        <!-- Point 2: Avg Time Spent per Module -->
        <div class="card">
          <h3>Avg Time Spent per Module</h3>
          <div id="timeSpentChart" class="chart"></div>
          <div class="error-fallback" id="timeSpentError">
            Chart failed to load. Please check your internet connection.
          </div>
        </div>

        <!-- Points 3 & 4: Abandoned / Completed Flows -->
        <div class="card">
          <h3>Abandoned / Completed Flows</h3>
          <div id="flowsChart" class="chart"></div>
          <div class="error-fallback" id="flowsError">
            Chart failed to load. Please check your internet connection.
          </div>
        </div>

        <!-- Point 5: User Location (by Country) -->
        <div class="card">
          <h3>User Location (by Country)</h3>
          <div id="locationChart" class="chart"></div>
          <div class="error-fallback" id="locationError">
            Chart failed to load. Please check your internet connection.
          </div>
        </div>

        <!-- Point 6: Top Searched Routes -->
        <div class="card">
          <h3>Top Searched Routes</h3>
          <div id="routesChart" class="chart"></div>
          <div class="error-fallback" id="routesError">
            Chart failed to load. Please check your internet connection.
          </div>
        </div>

        <!-- Point 7: Most Accessed Modules -->
        <div class="card">
          <h3>Most Accessed Modules</h3>
          <div id="modulesChart" class="chart"></div>
          <div class="error-fallback" id="modulesError">
            Chart failed to load. Please check your internet connection.
          </div>
        </div>
      </div>
      </div>
    </div>
  <script>
    // Global array to store chart roots for cleanup
    const chartRoots = [];

    // Function to clear all existing charts
    function clearAllCharts() {
      console.log('Clearing existing charts...');
      chartRoots.forEach((root) => {
        if (root && !root.isDisposed()) {
          root.dispose();
        }
      });
      chartRoots.length = 0; // Clear the array
      
      // Clear chart containers
      const chartContainers = [
        'pagesVisitedChart',
        'timeSpentChart', 
        'flowsChart',
        'locationChart',
        'routesChart',
        'modulesChart'
      ];
      
      chartContainers.forEach(containerId => {
        const container = document.getElementById(containerId);
        if (container) {
          container.innerHTML = '';
        }
      });
      
      // Hide error messages
      const errorElements = [
        'pagesVisitedError',
        'timeSpentError',
        'flowsError', 
        'locationError',
        'routesError',
        'modulesError'
      ];
      
      errorElements.forEach(errorId => {
        const errorElement = document.getElementById(errorId);
        if (errorElement) {
          errorElement.style.display = 'none';
        }
      });
    }

    // Helper function to create XY Bar Chart
    function makeXYBarChart(
      containerId,
      data,
      categoryField,
      valueField,
      seriesName,
      errorId
    ) {
      try {
        // Check if container exists and is empty
        const container = document.getElementById(containerId);
        if (!container) {
          console.error(`Container ${containerId} not found`);
          return;
        }
        
        // Create root element
        const root = am5.Root.new(containerId);
        chartRoots.push(root);

        // Hide amCharts logo
        root._logo.dispose();

        // Set themes
        root.setThemes([am5themes_Animated.new(root)]);

        // Create chart
        const chart = root.container.children.push(
          am5xy.XYChart.new(root, {
            panX: true,
            panY: true,
            wheelX: "panX",
            wheelY: "zoomX",
            pinchZoomX: true,
          })
        );

        // Add cursor
        const cursor = chart.set("cursor", am5xy.XYCursor.new(root, {}));
        cursor.lineY.set("visible", false);

        // Create axes
        const xRenderer = am5xy.AxisRendererX.new(root, {
          minGridDistance: 30,
          cellStartLocation: 0.1,
          cellEndLocation: 0.9,
        });

        xRenderer.labels.template.setAll({
          rotation: -45,
          centerY: am5.p50,
          centerX: am5.p100,
          paddingRight: 15,
          fontSize: 10,
        });

        const xAxis = chart.xAxes.push(
          am5xy.CategoryAxis.new(root, {
            maxDeviation: 0.3,
            categoryField: categoryField,
            renderer: xRenderer,
            tooltip: am5.Tooltip.new(root, {}),
          })
        );

        const yRenderer = am5xy.AxisRendererY.new(root, {
          strokeOpacity: 0.1,
        });

        const yAxis = chart.yAxes.push(
          am5xy.ValueAxis.new(root, {
            maxDeviation: 0.3,
            renderer: yRenderer,
          })
        );

        // Create series
        const series = chart.series.push(
          am5xy.ColumnSeries.new(root, {
            name: seriesName,
            xAxis: xAxis,
            yAxis: yAxis,
            valueYField: valueField,
            sequencedInterpolation: true,
            categoryXField: categoryField,
            tooltip: am5.Tooltip.new(root, {
              labelText: "{module}: {valueY}",
            }),
          })
        );

        series.columns.template.setAll({
          cornerRadiusTL: 5,
          cornerRadiusTR: 5,
          strokeOpacity: 0,
        });

        series.set("fill", am5.color("#3498db"));

        // Add legend
        const legend = chart.children.push(
          am5.Legend.new(root, {
            centerX: am5.p50,
            x: am5.p50,
          })
        );
        legend.data.push(series);

        // Set data
        xAxis.data.setAll(data);
        series.data.setAll(data);

        // Make stuff animate on load
        series.appear(1000);
        chart.appear(1000, 100);
      } catch (error) {
        console.error("Error creating bar chart:", error);
        document.getElementById(errorId).style.display = "block";
      }
    }

    // Helper function to create XY Line Chart
    function makeXYLineChart(
      containerId,
      data,
      categoryField,
      valueField,
      seriesName,
      errorId
    ) {
      try {
        // Check if container exists and is empty
        const container = document.getElementById(containerId);
        if (!container) {
          console.error(`Container ${containerId} not found`);
          return;
        }
        
        // Create root element
        const root = am5.Root.new(containerId);
        chartRoots.push(root);

        // Hide amCharts logo
        root._logo.dispose();

        // Set themes
        root.setThemes([am5themes_Animated.new(root)]);

        // Create chart
        const chart = root.container.children.push(
          am5xy.XYChart.new(root, {
            panX: true,
            panY: true,
            wheelX: "panX",
            wheelY: "zoomX",
            pinchZoomX: true,
          })
        );

        // Add cursor
        const cursor = chart.set("cursor", am5xy.XYCursor.new(root, {}));
        cursor.lineY.set("visible", false);

        // Create axes
        const xRenderer = am5xy.AxisRendererX.new(root, {
          minGridDistance: 30,
          cellStartLocation: 0.1,
          cellEndLocation: 0.9,
        });

        xRenderer.labels.template.setAll({
          rotation: -45,
          centerY: am5.p50,
          centerX: am5.p100,
          paddingRight: 15,
          fontSize: 10,
        });

        const xAxis = chart.xAxes.push(
          am5xy.CategoryAxis.new(root, {
            maxDeviation: 0.3,
            categoryField: categoryField,
            renderer: xRenderer,
            tooltip: am5.Tooltip.new(root, {}),
          })
        );

        const yRenderer = am5xy.AxisRendererY.new(root, {
          strokeOpacity: 0.1,
        });

        const yAxis = chart.yAxes.push(
          am5xy.ValueAxis.new(root, {
            maxDeviation: 0.3,
            renderer: yRenderer,
          })
        );

        // Create series
        const series = chart.series.push(
          am5xy.LineSeries.new(root, {
            name: seriesName,
            xAxis: xAxis,
            yAxis: yAxis,
            valueYField: valueField,
            sequencedInterpolation: true,
            categoryXField: categoryField,
            stroke: am5.color("#e74c3c"),
            tooltip: am5.Tooltip.new(root, {
              labelText: "{module}: {valueY}",
            }),
          })
        );

        series.strokes.template.setAll({
          strokeWidth: 3,
        });

        series.set("fill", am5.color("#e74c3c"));
        series.fills.template.setAll({
          fillOpacity: 0.2,
          visible: true,
        });

        // Add intersecting points
        series.bullets.push(function() {
          return am5.Bullet.new(root, {
            sprite: am5.Circle.new(root, {
              radius: 4,
              fill: am5.color("#e74c3c"),
              stroke: am5.color("#ffffff"),
              strokeWidth: 2
            })
          });
        });

        // Add legend
        const legend = chart.children.push(
          am5.Legend.new(root, {
            centerX: am5.p50,
            x: am5.p50,
          })
        );
        legend.data.push(series);

        // Set data
        xAxis.data.setAll(data);
        series.data.setAll(data);

        // Make stuff animate on load
        series.appear(1000);
        chart.appear(1000, 100);
      } catch (error) {
        console.error("Error creating line chart:", error);
        document.getElementById(errorId).style.display = "block";
      }
    }

    // Helper function to create Grouped Bar Chart
    function makeGroupedBarChart(
      containerId,
      data,
      categoryField,
      seriesConfig,
      errorId
    ) {
      try {
        // Check if container exists and is empty
        const container = document.getElementById(containerId);
        if (!container) {
          console.error(`Container ${containerId} not found`);
          return;
        }
        
        // Create root element
        const root = am5.Root.new(containerId);
        chartRoots.push(root);

        // Hide amCharts logo
        root._logo.dispose();

        // Set themes
        root.setThemes([am5themes_Animated.new(root)]);

        // Create chart
        const chart = root.container.children.push(
          am5xy.XYChart.new(root, {
            panX: true,
            panY: true,
            wheelX: "panX",
            wheelY: "zoomX",
            pinchZoomX: true,
          })
        );

        // Add cursor
        const cursor = chart.set("cursor", am5xy.XYCursor.new(root, {}));
        cursor.lineY.set("visible", false);

        // Create axes
        const xRenderer = am5xy.AxisRendererX.new(root, {
          minGridDistance: 30,
          cellStartLocation: 0.1,
          cellEndLocation: 0.9,
        });

        xRenderer.labels.template.setAll({
          rotation: -45,
          centerY: am5.p50,
          centerX: am5.p100,
          paddingRight: 15,
          fontSize: 10,
        });

        const xAxis = chart.xAxes.push(
          am5xy.CategoryAxis.new(root, {
            maxDeviation: 0.3,
            categoryField: categoryField,
            renderer: xRenderer,
            tooltip: am5.Tooltip.new(root, {}),
          })
        );

        const yRenderer = am5xy.AxisRendererY.new(root, {
          strokeOpacity: 0.1,
        });

        const yAxis = chart.yAxes.push(
          am5xy.ValueAxis.new(root, {
            maxDeviation: 0.3,
            renderer: yRenderer,
          })
        );

        // Create series for each data field
        const seriesArray = [];
        seriesConfig.forEach((config) => {
          const series = chart.series.push(
            am5xy.ColumnSeries.new(root, {
              name: config.name,
              xAxis: xAxis,
              yAxis: yAxis,
              valueYField: config.valueField,
              sequencedInterpolation: true,
              categoryXField: categoryField,
              tooltip: am5.Tooltip.new(root, {
                labelText: "{name}: {valueY}",
              }),
            })
          );

          series.columns.template.setAll({
            cornerRadiusTL: 5,
            cornerRadiusTR: 5,
            strokeOpacity: 0,
          });

          series.set("fill", am5.color(config.color));
          seriesArray.push(series);
        });

        // Add legend
        const legend = chart.children.push(
          am5.Legend.new(root, {
            centerX: am5.p50,
            x: am5.p50,
          })
        );
        legend.data.setAll(seriesArray);

        // Set data
        xAxis.data.setAll(data);
        seriesArray.forEach((series) => {
          series.data.setAll(data);
        });

        // Make stuff animate on load
        seriesArray.forEach((series) => {
          series.appear(1000);
        });
        chart.appear(1000, 100);
      } catch (error) {
        console.error("Error creating grouped bar chart:", error);
        document.getElementById(errorId).style.display = "block";
      }
    }

    // Function to fetch data from backend with filters
    async function fetchAnalyticsData(action, startDate, endDate, customer = '') {
      try {
        var URLStr = "UserAnalytics.do?";
        var params = "action=" + action + 
                    "&start=" + encodeURIComponent(startDate) + 
                    "&end=" + encodeURIComponent(endDate);
        
        if (customer && customer.trim() !== '') {
          params += "&customer=" + encodeURIComponent(customer.trim());
        }
        
        const response = await fetch(URLStr + params, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          }
        });
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        console.log("data for " + action + ": " + JSON.stringify(data));
        return data;
      } catch (error) {
        console.error(`Error fetching ${action} data:`, error);
        return [];
      }
    }

    // Module code to name mapping
    const moduleNameMap = {
      'DBD': 'Dashboard',
      'TAR': 'Rates',
      'BLS': 'Schedules',
      'BKG': 'Booking',
      'BOL': 'BL',
      'SHI': 'SI',
      'VGM': 'VGM',
      'CRO': 'CRO',
      'TRK': 'Tracking'
    };

    function getModuleName(code) {
      return moduleNameMap[code] || code;
    }

    // Function to transform module usage data
    function transformModuleUsageData(data) {
      return data.map(item => ({
        module: getModuleName(item.module_code) || 'Unknown',
        moduleCode: item.module_code || 'Unknown',
        views: item.visit_count || 0
      }));
    }

    // Function to transform time spent data
    function transformTimeSpentData(data) {
      return data.map(item => ({
        module: getModuleName(item.module_code) || 'Unknown',
        moduleCode: item.module_code || 'Unknown',
        hours: parseFloat(item.hours_this_week) || 0
      }));
    }

    // Function to transform completed vs abandoned data
    function transformCompletedVsAbandonedData(data) {
      return data.map(item => ({
        module: getModuleName(item.module_code) || 'Unknown',
        moduleCode: item.module_code || 'Unknown',
        abandoned: item.abandoned || 0,
        completed: item.completed || 0
      }));
    }

    // Function to transform user location data
    function transformUserLocationData(data) {
      return data.map(item => ({
        country: item.country_code || 'Unknown',
        sessions: item.unique_customers || 0
      }));
    }

    // Function to transform top routes data
    function transformTopRoutesData(data) {
      return data.map(item => ({
        route: item.route || 'Unknown',
        searches: item.search_count || 0
      }));
    }

    // Function to load all charts with current filter values
    async function loadAllCharts() {
      try {
        const startDate = document.getElementById('startInput').value;
        const endDate = document.getElementById('endInput').value;
        const customerInput = document.getElementById('customerFilter');
        const customer = customerInput.dataset.customerCode || customerInput.value; // Use customer code if available
        
        console.log('Loading all charts with filters:', { startDate, endDate, customer });

        // Clear existing charts first
        clearAllCharts();

        // Load charts sequentially for better performance
        // First: Load module usage data (most important)
        console.log('Loading module usage data...');
        const moduleUsageData = await fetchAnalyticsData('moduleUsage', startDate, endDate, customer);
        const pagesVisitedData = transformModuleUsageData(moduleUsageData);
        makeXYBarChart(
          "pagesVisitedChart",
          pagesVisitedData,
          "module",
          "views",
          "Views",
          "pagesVisitedError"
        );

        // Second: Load time spent data
        console.log('Loading time spent data...');
        const timeSpentData = await fetchAnalyticsData('timeSpent', startDate, endDate, customer);
        const transformedTimeSpentData = transformTimeSpentData(timeSpentData);
        makeXYLineChart(
          "timeSpentChart",
          transformedTimeSpentData,
          "module",
          "hours",
          "Hours/Week",
          "timeSpentError"
        );

        // Third: Load completed vs abandoned data
        console.log('Loading completed vs abandoned data...');
        const completedVsAbandonedData = await fetchAnalyticsData('completedVsAbandoned', startDate, endDate, customer);
        const flowsData = transformCompletedVsAbandonedData(completedVsAbandonedData);
        const flowsSeriesConfig = [
          { name: "Abandoned", valueField: "abandoned", color: "#e74c3c" },
          { name: "Completed", valueField: "completed", color: "#27ae60" },
        ];
        makeGroupedBarChart(
          "flowsChart",
          flowsData,
          "module",
          flowsSeriesConfig,
          "flowsError"
        );

        // Fourth: Load user location data
        console.log('Loading user location data...');
        const userLocationData = await fetchAnalyticsData('userLocation', startDate, endDate, customer);
        const locationData = transformUserLocationData(userLocationData);
        makeXYBarChart(
          "locationChart",
          locationData,
          "country",
          "sessions",
          "Sessions",
          "locationError"
        );

        // Fifth: Load top routes data
        console.log('Loading top routes data...');
        const topRoutesData = await fetchAnalyticsData('topRoutes', startDate, endDate, customer);
        const routesData = transformTopRoutesData(topRoutesData);
        makeXYBarChart(
          "routesChart",
          routesData,
          "route",
          "searches",
          "Searches",
          "routesError"
        );

        // Sixth: Load most accessed modules (reuse module usage data)
        console.log('Loading most accessed modules data...');
        const modulesData = transformModuleUsageData(moduleUsageData);
        makeXYLineChart(
          "modulesChart",
          modulesData,
          "module",
          "views",
          "Views",
          "modulesError"
        );

        console.log('All charts loaded successfully!');

      } catch (error) {
        console.error("Error loading charts:", error);
        // Show error messages for all charts
        document.getElementById("pagesVisitedError").style.display = "block";
        document.getElementById("timeSpentError").style.display = "block";
        document.getElementById("flowsError").style.display = "block";
        document.getElementById("locationError").style.display = "block";
        document.getElementById("routesError").style.display = "block";
        document.getElementById("modulesError").style.display = "block";
      }
    }

    // Initialize all charts when page loads
    document.addEventListener("DOMContentLoaded", async function () {
      // Wait for date range to be initialized first
      setTimeout(() => {
        loadAllCharts();
      }, 100);
    });

    // Cleanup charts on page unload to prevent memory leaks
    window.addEventListener("beforeunload", function () {
      chartRoots.forEach((root) => {
        if (root && !root.isDisposed()) {
          root.dispose();
        }
      });
    });

    // Customer Autocomplete Functionality
    let customerSearchTimeout;
    let currentCustomerData = [];
    let selectedCustomerIndex = -1;
    let allCustomersData = []; // Store all customers for client-side filtering

    // Load all customers on page load
    async function loadAllCustomers() {
      try {
        console.log('Loading all customers...');
        const response = await fetch('UserAnalytics.do?action=loadAllCustomers', {
          method: 'GET'
        });

        console.log('Response status:', response.status);
        console.log('Response headers:', response.headers);
        console.log('Response ok:', response.ok);
        
        if (!response.ok) {
          const errorText = await response.text();
          console.error('Error response:', errorText);
          throw new Error(`Network response was not ok: ${response.status} - ${errorText}`);
        }

        // Get response text first to see raw response
        const responseText = await response.text();
        console.log('Raw response text:', responseText);
        
        let data;
        try {
          data = JSON.parse(responseText);
          console.log('Parsed JSON data:', data);
          console.log('Response type:', typeof data);
          console.log('Is array:', Array.isArray(data));
        } catch (parseError) {
          console.error('Failed to parse JSON response:', parseError);
          console.error('Response text that failed to parse:', responseText);
          allCustomersData = [];
          return;
        }
        
        // Ensure allCustomersData is always an array
        if (Array.isArray(data)) {
          allCustomersData = data;
          console.log('All customers loaded:', data.length, 'customers');
        } else {
          console.warn('Response is not an array:', data);
          console.warn('Response structure:', JSON.stringify(data, null, 2));
          allCustomersData = [];
        }
      } catch (error) {
        console.error('Error loading all customers:', error);
        allCustomersData = [];
      }
    }

    // Initialize autocomplete when DOM is loaded
    document.addEventListener('DOMContentLoaded', function() {
      // Load all customers first
      loadAllCustomers();
      
      const customerInput = document.getElementById('customerFilter');
      const customerDropdown = document.getElementById('customerDropdown');
      
      if (customerInput && customerDropdown) {
        // Add input event listener for client-side filtering
        customerInput.addEventListener('input', function() {
          const searchTerm = this.value.trim();
          const loadingIcon = document.getElementById('customerLoadingIcon');
          
          // Clear customer code if input is empty
          if (searchTerm.length === 0) {
            this.dataset.customerCode = '';
            loadingIcon.style.display = 'none';
            hideDropdown();
            return;
          }
          
          if (searchTerm.length >= 2) {
            // Show loading icon briefly for visual feedback
            loadingIcon.style.display = 'block';
            
            // Try client-side filtering first, fallback to server-side search
            setTimeout(() => {
              if (Array.isArray(allCustomersData) && allCustomersData.length > 0) {
                filterCustomersLocally(searchTerm);
              } else {
                // Fallback to server-side search if client-side data is not available
                console.log('Falling back to server-side search');
                searchCustomers(searchTerm);
              }
              loadingIcon.style.display = 'none';
            }, 100);
          } else {
            loadingIcon.style.display = 'none';
            hideDropdown();
          }
        });

        // Handle keyboard navigation
        customerInput.addEventListener('keydown', function(e) {
          if (customerDropdown.style.display === 'none') return;
          
          switch(e.key) {
            case 'ArrowDown':
              e.preventDefault();
              selectedCustomerIndex = Math.min(selectedCustomerIndex + 1, currentCustomerData.length - 1);
              updateSelection();
              break;
            case 'ArrowUp':
              e.preventDefault();
              selectedCustomerIndex = Math.max(selectedCustomerIndex - 1, -1);
              updateSelection();
              break;
            case 'Enter':
              e.preventDefault();
              if (selectedCustomerIndex >= 0 && currentCustomerData[selectedCustomerIndex]) {
                selectCustomer(currentCustomerData[selectedCustomerIndex]);
              }
              break;
            case 'Escape':
              hideDropdown();
              break;
          }
        });

        // Hide dropdown when clicking outside
        document.addEventListener('click', function(e) {
          if (!customerInput.contains(e.target) && !customerDropdown.contains(e.target)) {
            hideDropdown();
          }
        });
      }
    });

    // Client-side filtering function
    function filterCustomersLocally(searchTerm) {
      console.log('Filtering customers locally for:', searchTerm);
      
      // Ensure allCustomersData is an array before filtering
      if (!Array.isArray(allCustomersData) || allCustomersData.length === 0) {
        console.log('No customer data available yet or data is not an array');
        hideDropdown();
        return;
      }
      
      // Filter customers by name or code (case-insensitive)
      const filteredCustomers = allCustomersData.filter(customer => {
        const custName = (customer.custname || '').toLowerCase();
        const custCode = (customer.custcode || '').toLowerCase();
        const search = searchTerm.toLowerCase();
        
        return custName.includes(search) || custCode.includes(search);
      });
      
      console.log('Filtered customers:', filteredCustomers.length);
      
      currentCustomerData = filteredCustomers;
      selectedCustomerIndex = -1;
      
      if (filteredCustomers.length > 0) {
        showDropdown(filteredCustomers);
      } else {
        hideDropdown();
      }
    }

    // Search customers function (kept for backward compatibility)
    async function searchCustomers(searchTerm) {
      const loadingIcon = document.getElementById('customerLoadingIcon');
      
      try {
        console.log('Searching customers for:', searchTerm);
        
        const response = await fetch('UserAnalytics.do', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: 'action=customerSearch&customer=' + encodeURIComponent(searchTerm)
        });

        if (!response.ok) {
          throw new Error('Network response was not ok');
        }

        const data = await response.json();
        console.log('Customer search results:', data);
        
        currentCustomerData = data;
        selectedCustomerIndex = -1;
        
        if (data && data.length > 0) {
          showDropdown(data);
        } else {
          hideDropdown();
        }
      } catch (error) {
        console.error('Error searching customers:', error);
        hideDropdown();
      } finally {
        // Hide loading icon after EJB response
        loadingIcon.style.display = 'none';
      }
    }

    // Show dropdown with customer data
    function showDropdown(customers) {
      const dropdown = document.getElementById('customerDropdown');
      dropdown.innerHTML = '';
      
      customers.forEach((customer, index) => {
        const item = document.createElement('div');
        item.className = 'autocomplete-item';
        item.innerHTML = '<span class="customer-code">' + customer.custcode + '</span>' +
                        '<span class="customer-name">' + customer.custname + '</span>';
        
        item.addEventListener('click', function() {
          selectCustomer(customer);
        });
        
        dropdown.appendChild(item);
      });
      
      dropdown.style.display = 'block';
    }

    // Hide dropdown
    function hideDropdown() {
      const dropdown = document.getElementById('customerDropdown');
      dropdown.style.display = 'none';
      selectedCustomerIndex = -1;
    }

    // Update selection highlighting
    function updateSelection() {
      const items = document.querySelectorAll('.autocomplete-item');
      items.forEach((item, index) => {
        if (index === selectedCustomerIndex) {
          item.classList.add('selected');
        } else {
          item.classList.remove('selected');
        }
      });
    }

    // Select customer
    function selectCustomer(customer) {
      const customerInput = document.getElementById('customerFilter');
      customerInput.value = customer.custname; // Show only customer name
      customerInput.dataset.customerCode = customer.custcode; // Store customer code for sending
      hideDropdown();
      
      // Trigger chart refresh with selected customer
      setTimeout(() => {
        loadAllCharts();
      }, 100);
    }

    // Excel Export Functionality
    async function exportToExcel() {
      try {
        const startDate = document.getElementById('startInput').value;
        const endDate = document.getElementById('endInput').value;
        const customerInput = document.getElementById('customerFilter');
        const customer = customerInput.dataset.customerCode || customerInput.value;
        
        console.log('Exporting to Excel with filters:', { startDate, endDate, customer });
        
        // Show loading state
        const exportBtn = document.querySelector('button[onclick="exportToExcel()"]');
        const originalContent = exportBtn.innerHTML;
        exportBtn.innerHTML = '<i class="fa fa-spinner fa-spin"></i>';
        exportBtn.disabled = true;
        
        // Fetch all analytics data
        const [moduleUsageData, timeSpentData, completedVsAbandonedData, userLocationData, topRoutesData] = await Promise.all([
          fetchAnalyticsData('moduleUsage', startDate, endDate, customer),
          fetchAnalyticsData('timeSpent', startDate, endDate, customer),
          fetchAnalyticsData('completedVsAbandoned', startDate, endDate, customer),
          fetchAnalyticsData('userLocation', startDate, endDate, customer),
          fetchAnalyticsData('topRoutes', startDate, endDate, customer)
        ]);
        
        // Create Excel workbook
        const workbook = createExcelWorkbook({
          moduleUsage: moduleUsageData,
          timeSpent: timeSpentData,
          completedVsAbandoned: completedVsAbandonedData,
          userLocation: userLocationData,
          topRoutes: topRoutesData,
          filters: { startDate, endDate, customer }
        });
        
        // Download the file
        const fileName = 'UserAnalytics_' + new Date().toISOString().slice(0, 10) + '.xlsx';
        downloadExcelFile(workbook, fileName);
        
      } catch (error) {
        console.error('Error exporting to Excel:', error);
        alert('Error exporting data to Excel. Please try again.');
      } finally {
        // Restore button state
        const exportBtn = document.querySelector('button[onclick="exportToExcel()"]');
        exportBtn.innerHTML = '<i class="fa fa-file-excel-o"></i>';
        exportBtn.disabled = false;
      }
    }

    // Create Excel workbook with multiple sheets
    function createExcelWorkbook(data) {
      const workbook = {
        SheetNames: ['Pages Visited', 'Time Spent', 'Flows', 'User Location', 'Top Routes'],
        Sheets: {}
      };
      
      // Pages Visited Sheet
      workbook.Sheets['Pages Visited'] = createSheet([
        ['Module', 'Views'],
        ...data.moduleUsage.map(item => [item.module_code || 'Unknown', item.visit_count || 0])
      ]);
      
      // Time Spent Sheet
      workbook.Sheets['Time Spent'] = createSheet([
        ['Module', 'Hours/Week'],
        ...data.timeSpent.map(item => [item.module_code || 'Unknown', item.hours_this_week || 0])
      ]);
      
      // Flows Sheet
      workbook.Sheets['Flows'] = createSheet([
        ['Module', 'Completed', 'Abandoned', 'Total', 'Abandoned %'],
        ...data.completedVsAbandoned.map(item => [
          item.module_code || 'Unknown',
          item.completed || 0,
          item.abandoned || 0,
          item.total || 0,
          item.abandoned_pct || 0
        ])
      ]);
      
      // User Location Sheet
      workbook.Sheets['User Location'] = createSheet([
        ['Country', 'Unique Customers'],
        ...data.userLocation.map(item => [item.country_code || 'Unknown', item.unique_customers || 0])
      ]);
      
      // Top Routes Sheet
      workbook.Sheets['Top Routes'] = createSheet([
        ['Route', 'Search Count'],
        ...data.topRoutes.map(item => [item.route || 'Unknown', item.search_count || 0])
      ]);
      
      return workbook;
    }

    // Create a sheet with data
    function createSheet(data) {
      const sheet = {};
      const range = { s: { c: 0, r: 0 }, e: { c: data[0].length - 1, r: data.length - 1 } };
      
      data.forEach((row, rowIndex) => {
        row.forEach((cell, colIndex) => {
          const cellRef = XLSX.utils.encode_cell({ r: rowIndex, c: colIndex });
          sheet[cellRef] = { v: cell };
        });
      });
      
      sheet['!ref'] = XLSX.utils.encode_range(range);
      return sheet;
    }

    // Download Excel file
    function downloadExcelFile(workbook, fileName) {
      const wbout = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
      const blob = new Blob([wbout], { type: 'application/octet-stream' });
      const url = URL.createObjectURL(blob);
      
      const link = document.createElement('a');
      link.href = url;
      link.download = fileName;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
    }
  </script>
</div>
