(function (window, $) {
    'use strict';

    var activeRequest = null;
    var activeBlock = null;
    var $modal = null;

    function getConfig() {
        return window.scheduleCarbonConfig || {};
    }

    function getMessages() {
        return getConfig().messages || {};
    }

    function getEndpoint() {
        return getConfig().endpoint || '';
    }

    /** One modal in DOM: prefer fresh AJAX result, then body; drop duplicate copies. */
    function resolveCarbonModal() {
        var $pick = $('#locationSearch .carbon-modal#schd_carbon_modal').first();
        if (!$pick.length) {
            $pick = $('body > .carbon-modal#schd_carbon_modal').first();
        }
        if (!$pick.length) {
            $pick = $('.carbon-modal#schd_carbon_modal').first();
        }
        if (!$pick.length) {
            $pick = $('#schd_carbon_modal').first();
        }
        $('#locationSearchtable #schd_carbon_modal').remove();
        $('#schd_carbon_modal').not($pick).remove();
        if ($pick.length && !$pick.parent().is('body')) {
            $pick.appendTo('body');
        }
        $modal = $pick;
        return $pick;
    }

    function $carbon(selector) {
        if (!$modal || !$modal.length) {
            resolveCarbonModal();
        }
        return ($modal && $modal.length) ? $modal.find(selector) : $(selector);
    }

    function showError(message) {
        clearCalculationFailure();
        $carbon('#schdCarbonModalErrorForm').text(message || '').toggle(!!message);
    }

    function showCalculationFailure(message) {
        clearError();
        var messages = getMessages();
        var text = message || messages.calculationFailed;
        if (text === messages.requestFailed) {
            text = messages.calculationFailed;
        }
        $carbon('#schdCarbonModalError').text(text).show();
    }

    function clearCalculationFailure() {
        $carbon('#schdCarbonModalError').hide().text('');
    }

    function clearError() {
        $carbon('#schdCarbonModalErrorForm').text('').hide();
        clearCalculationFailure();
    }

    function setLoading(on) {
        $carbon('#schdCarbonModalCalculate').prop('disabled', on);
        $('.schd_carbon_open_btn').prop('disabled', on);
        $carbon('#schdCarbonModalLoading').toggle(!!on);
    }

    function formatGrams(grams, valueSelector, subSelector) {
        if (grams === null || grams === undefined || isNaN(grams)) {
            $carbon(valueSelector).text('\u2014');
            $carbon(subSelector).text('');
            return;
        }
        var g = Number(grams);
        var kg = g / 1000;
        $carbon(valueSelector).html(
            kg.toLocaleString(undefined, { maximumFractionDigits: 2 }) +
            ' <span class="carbon-unit">kg</span>' +
            '<div class="carbon-grams">' + g.toLocaleString(undefined, { maximumFractionDigits: 0 }) + ' g</div>'
        );
        $carbon(subSelector).text('CO\u2082e');
    }

    function resetResultsPanel() {
        formatGrams(null, '#schdCarbonCo2Total', '#schdCarbonCo2TotalSub');
        formatGrams(null, '#schdCarbonCo2Ttw', '#schdCarbonCo2TtwSub');
        formatGrams(null, '#schdCarbonCo2Wtt', '#schdCarbonCo2WttSub');
        $carbon('#schdCarbonDistance').text('\u2014');
    }

    function openPurposeAccordion() {
        $carbon('.schd-carbon-info-card').removeClass('is-open')
            .find('.schd-carbon-info-toggle').attr('aria-expanded', 'false');
        $carbon('.schd-carbon-info-card--purpose').addClass('is-open')
            .find('.schd-carbon-info-toggle').attr('aria-expanded', 'true');
    }

    function renderResults(data) {
        var co2eRaw = data.co2e || (data.emissions ? data.emissions.co2e : null);
        var totalG = null;
        var ttwG = null;
        var wttG = null;

        if (co2eRaw && typeof co2eRaw === 'object') {
            var block = (co2eRaw.total && typeof co2eRaw.total === 'object') ? co2eRaw.total : co2eRaw;
            totalG = block.total;
            ttwG = block.ttw;
            wttG = block.wtt;
        }

        formatGrams(totalG, '#schdCarbonCo2Total', '#schdCarbonCo2TotalSub');
        formatGrams(ttwG, '#schdCarbonCo2Ttw', '#schdCarbonCo2TtwSub');
        formatGrams(wttG, '#schdCarbonCo2Wtt', '#schdCarbonCo2WttSub');

        if (data.distance_km !== null && data.distance_km !== undefined) {
            $carbon('#schdCarbonDistance').text(Number(data.distance_km).toLocaleString(undefined, { maximumFractionDigits: 0 }));
        } else {
            $carbon('#schdCarbonDistance').text('\u2014');
        }

        $carbon('#schdCarbonResultsPanel').show();
        openPurposeAccordion();
    }

    function validateForm($block) {
        var messages = getMessages();
        var quantity = $.trim($carbon('#schdCarbonModalQty').val());
        var weight = $.trim($carbon('#schdCarbonModalWeight').val());

        if (!quantity || isNaN(quantity) || Number(quantity) <= 0) {
            showError(messages.quantityRequired);
            return null;
        }
        if (!weight || isNaN(weight) || Number(weight) <= 0) {
            showError(messages.weightRequired);
            return null;
        }
        if (!$block || !$block.length) {
            showError(messages.scheduleIndexInvalid);
            return null;
        }
        var index = $block.attr('data-schedule-index');
        if (index === undefined || index === null || index === '') {
            showError(messages.scheduleIndexInvalid);
            return null;
        }
        var scheduleRouteIndex = parseInt(index, 10);
        if (isNaN(scheduleRouteIndex)) {
            showError(messages.scheduleIndexInvalid);
            return null;
        }

        return {
            schedule_route_index: scheduleRouteIndex,
            weight_kg: weight,
            quantity: quantity
        };
    }

    function parseErrorMessage(xhr) {
        var messages = getMessages();
        if (xhr.responseJSON && xhr.responseJSON.error) {
            return xhr.responseJSON.error;
        }
        if (xhr.responseText) {
            try {
                var parsed = JSON.parse(xhr.responseText);
                if (parsed.error) {
                    return parsed.error;
                }
            } catch (ignore) {
                // fall through to status-based messages
            }
        }
        if (xhr.status === 404) {
            return messages.endpointMissing;
        }
        if (xhr.status === 503) {
            return messages.apiNotConfigured;
        }
        return messages.requestFailed;
    }

    function resetModalState() {
        resetResultsPanel();
        $carbon('#schdCarbonResultsPanel').hide();
        $carbon('#schdCarbonModalLoading').hide();
        clearError();
        openPurposeAccordion();
    }

    function showCarbonModal() {
        var $el = resolveCarbonModal();
        if (!$el.length) {
            return false;
        }
        if (typeof $.fn.modal === 'function') {
            if (!$el.data('bs.modal')) {
                $el.modal({ backdrop: false, show: false });
            }
            $el.modal('show');
            return true;
        }
        $el.addClass('in show').css({ display: 'block', paddingRight: '17px' });
        $('body').addClass('modal-open');
        if (!$('.modal-backdrop').length) {
            $('body').append('<div class="modal-backdrop fade in"></div>');
        }
        return true;
    }

    function openCarbonModal($block) {
        if (!$block || !$block.length) {
            return false;
        }
        resolveCarbonModal();
        activeBlock = $block;
        $carbon('#schdCarbonModalRoutePreview').text($block.attr('data-route-label') || '');
        $carbon('#schdCarbonModalQty').val('1');
        $carbon('#schdCarbonModalWeight').val('');
        resetModalState();
        setLoading(false);
        if (!showCarbonModal()) {
            $('.schd_carbon_open_btn').prop('disabled', false);
            return false;
        }
        return true;
    }

    function calculateEmissions() {
        var messages = getMessages();
        var endpoint = getEndpoint();

        if (!activeBlock || !activeBlock.length) {
            return;
        }
        if (!endpoint) {
            showError(messages.endpointMissing);
            return;
        }

        clearError();
        $carbon('#schdCarbonResultsPanel').hide();
        resetResultsPanel();

        var payload = validateForm(activeBlock);
        if (!payload) {
            return;
        }

        if (activeRequest && activeRequest.readyState !== 4) {
            activeRequest.abort();
        }
        setLoading(true);

        activeRequest = $.ajax({
            url: endpoint,
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            data: JSON.stringify({
                schedule_route_index: payload.schedule_route_index,
                weight_kg: payload.weight_kg,
                quantity: payload.quantity
            }),
            success: function (data) {
                setLoading(false);
                if (data && data.error) {
                    $carbon('#schdCarbonResultsPanel').hide();
                    resetResultsPanel();
                    showCalculationFailure(data.error);
                    return;
                }
                var climatiqResponse = data && data.climatiq_response ? data.climatiq_response : null;
                if (!climatiqResponse) {
                    $carbon('#schdCarbonResultsPanel').hide();
                    resetResultsPanel();
                    showCalculationFailure(messages.calculationFailed);
                    return;
                }
                if (climatiqResponse.error) {
                    $carbon('#schdCarbonResultsPanel').hide();
                    resetResultsPanel();
                    showCalculationFailure(climatiqResponse.error);
                    return;
                }
                clearCalculationFailure();
                renderResults(climatiqResponse);
            },
            error: function (xhr) {
                setLoading(false);
                if (xhr.status === 403) {
                    window.location.href = 'SessionExpired.jsp';
                    return;
                }
                if (xhr.statusText === 'abort') {
                    return;
                }
                $carbon('#schdCarbonResultsPanel').hide();
                resetResultsPanel();
                showCalculationFailure(parseErrorMessage(xhr));
            }
        });
    }

    function findScheduleBlock(el) {
        return $(el).closest('[data-schedule-index]');
    }

    function toggleInfoCard($toggle) {
        var $card = $toggle.closest('.schd-carbon-info-card');
        var open = !$card.hasClass('is-open');
        $card.toggleClass('is-open', open);
        $toggle.attr('aria-expanded', open ? 'true' : 'false');
    }

    function bindEventHandlers() {
        $(document).off('click.scheduleCarbon', '.schd_carbon_open_btn').on('click.scheduleCarbon', '.schd_carbon_open_btn', function (event) {
            event.preventDefault();
            event.stopPropagation();
            openCarbonModal(findScheduleBlock(this));
        });

        $(document).off('click.scheduleCarbon', '#schdCarbonModalCalculate').on('click.scheduleCarbon', '#schdCarbonModalCalculate', function (event) {
            event.preventDefault();
            calculateEmissions();
        });

        $(document).off('click.scheduleCarbon', '.schd-carbon-info-toggle').on('click.scheduleCarbon', '.schd-carbon-info-toggle', function (event) {
            event.preventDefault();
            toggleInfoCard($(this));
        });

        resolveCarbonModal().off('hidden.bs.modal.scheduleCarbon').on('hidden.bs.modal.scheduleCarbon', function () {
            if (activeRequest && activeRequest.readyState !== 4) {
                activeRequest.abort();
            }
            activeBlock = null;
            setLoading(false);
            resetModalState();
            $(this).removeClass('in show').css('display', 'none');
            $('.modal-backdrop').remove();
            if (!$('.modal.in').length) {
                $('body').removeClass('modal-open');
            }
        });
    }

    window.initScheduleCarbon = function () {
        $modal = null;
        resolveCarbonModal();
        bindEventHandlers();
    };
}(window, jQuery));
