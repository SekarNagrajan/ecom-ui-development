(function (window, $) {
    'use strict';

    function showError(message) {
        clearCalculationFailure();
        var box = $('#carbonErrorBox');
        box.text(message || carbonCalculatorMessages.requestFailed).show();
    }

    function showCalculationFailure(message) {
        clearError();
        var text = message || carbonCalculatorMessages.calculationFailed;
        if (text === carbonCalculatorMessages.requestFailed) {
            text = carbonCalculatorMessages.calculationFailed;
        }
        $('#carbonCalcMessageText').text(text);
        $('#carbonCalcMessage').show();
    }

    function clearCalculationFailure() {
        $('#carbonCalcMessage').hide();
        $('#carbonCalcMessageText').text('');
    }

    function clearError() {
        $('#carbonErrorBox').hide().text('');
        clearCalculationFailure();
    }

    function setLoading(on) {
        $('#carbonCalcBtn').prop('disabled', on);
        $('#carbonSpinner').toggle(on);
        $('#carbonBtnText').text(on ? carbonCalculatorMessages.calculating : carbonCalculatorMessages.calculate);
    }

    function getTypeaheadValue(inputId) {
        var $input = $('#' + inputId);
        if ($input.data('ttTypeahead')) {
            return $.trim($input.typeahead('val'));
        }
        return $.trim($input.val());
    }

    function getPortCode(inputId, hiddenId) {
        var hidden = $.trim($('#' + hiddenId).val());
        if (hidden) {
            return hidden.toUpperCase();
        }

        var raw = getTypeaheadValue(inputId);
        if (!raw) {
            raw = $.trim($('#' + inputId).val());
        }
        if (!raw) {
            return '';
        }

        var code = raw.indexOf(' - ') >= 0 ? raw.split(' - ')[0] : raw.split('-')[0];
        return $.trim(code).toUpperCase();
    }

    /**
     * Typeahead only updates hidden codes on select/autocomplete. If the user edits the visible
     * text afterward, clear the hidden so getPortCode does not submit a stale pair (which reused
     * previous successful results with no error).
     */
    function reconcilePortHiddenWithDisplay(inputId, hiddenId) {
        var hidden = $.trim($('#' + hiddenId).val());
        if (!hidden) {
            return;
        }
        var visible = getTypeaheadValue(inputId);
        if (!visible) {
            visible = $.trim($('#' + inputId).val());
        }
        if (!visible) {
            $('#' + hiddenId).val('');
            return;
        }
        var codeFromVisible;
        if (visible.indexOf(' - ') >= 0) {
            codeFromVisible = $.trim(visible.split(' - ')[0]).toUpperCase();
        } else if (visible.indexOf('-') >= 0) {
            codeFromVisible = $.trim(visible.split('-')[0]).toUpperCase();
        } else {
            codeFromVisible = $.trim(visible).toUpperCase();
        }
        if (codeFromVisible !== hidden.toUpperCase()) {
            $('#' + hiddenId).val('');
        }
    }

    function swapPorts() {
        var originVal = getTypeaheadValue('carbonOrigin');
        var deliveryVal = getTypeaheadValue('carbonDelivery');
        var originCode = $('#carbonOriginCode').val();
        var deliveryCode = $('#carbonDeliveryCode').val();

        if ($('#carbonOrigin').data('ttTypeahead')) {
            $('#carbonOrigin').typeahead('val', deliveryVal);
            $('#carbonDelivery').typeahead('val', originVal);
        } else {
            $('#carbonOrigin').val(deliveryVal);
            $('#carbonDelivery').val(originVal);
        }

        $('#carbonOriginCode').val(deliveryCode);
        $('#carbonDeliveryCode').val(originCode);
    }

    function formatGrams(grams, valueSelector, subSelector) {
        if (grams === null || grams === undefined || isNaN(grams)) {
            $(valueSelector).text('\u2014');
            $(subSelector).text('');
            return;
        }
        var g = Number(grams);
        var kg = g / 1000;
        $(valueSelector).html(
            kg.toLocaleString(undefined, { maximumFractionDigits: 2 }) +
            ' <span class="carbon-unit">kg</span>' +
            '<div class="carbon-grams">' + g.toLocaleString(undefined, { maximumFractionDigits: 0 }) + ' g</div>'
        );
        $(subSelector).text('CO\u2082e');
    }

    function resetCarbonResultsPanel() {
        $('#carbonRouteLabel').text('');
        formatGrams(null, '#carbonCo2Total', '#carbonCo2TotalSub');
        formatGrams(null, '#carbonCo2Ttw', '#carbonCo2TtwSub');
        formatGrams(null, '#carbonCo2Wtt', '#carbonCo2WttSub');
        $('#carbonDistance').text('\u2014');
    }

    function renderResults(data, originLabel, deliveryLabel) {
        $('#carbonRouteLabel').text(originLabel + '  \u2192  ' + deliveryLabel);

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

        formatGrams(totalG, '#carbonCo2Total', '#carbonCo2TotalSub');
        formatGrams(ttwG, '#carbonCo2Ttw', '#carbonCo2TtwSub');
        formatGrams(wttG, '#carbonCo2Wtt', '#carbonCo2WttSub');

        if (data.distance_km !== null && data.distance_km !== undefined) {
            $('#carbonDistance').text(Number(data.distance_km).toLocaleString(undefined, { maximumFractionDigits: 0 }));
        } else {
            $('#carbonDistance').text('\u2014');
        }

        $('#carbonResults').show();
    }

    function validateForm() {
        var origin = getPortCode('carbonOrigin', 'carbonOriginCode');
        var delivery = getPortCode('carbonDelivery', 'carbonDeliveryCode');
        var quantity = $.trim($('#carbonQuantity').val());
        var weight = $.trim($('#carbonWeight').val());

        if (!origin || !delivery) {
            showError(carbonCalculatorMessages.originDeliveryRequired);
            return null;
        }
        if (origin === delivery) {
            showError(carbonCalculatorMessages.diffPort);
            return null;
        }
        if (!quantity || isNaN(quantity) || Number(quantity) <= 0) {
            showError(carbonCalculatorMessages.quantityRequired);
            return null;
        }
        if (!weight || isNaN(weight) || Number(weight) <= 0) {
            showError(carbonCalculatorMessages.weightRequired);
            return null;
        }

        return {
            origin: origin,
            delivery: delivery,
            weight_kg: weight,
            quantity: quantity,
            originLabel: getTypeaheadValue('carbonOrigin') || origin,
            deliveryLabel: getTypeaheadValue('carbonDelivery') || delivery
        };
    }

    function parseErrorMessage(xhr) {
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
            return carbonCalculatorMessages.endpointMissing;
        }
        if (xhr.status === 503) {
            return carbonCalculatorMessages.apiNotConfigured;
        }
        return carbonCalculatorMessages.requestFailed;
    }

    function calculateEmissions() {
        clearError();
        $('#carbonResults').hide();
        resetCarbonResultsPanel();
        reconcilePortHiddenWithDisplay('carbonOrigin', 'carbonOriginCode');
        reconcilePortHiddenWithDisplay('carbonDelivery', 'carbonDeliveryCode');

        var payload = validateForm();
        if (!payload) {
            return;
        }

        setLoading(true);
        $.ajax({
            url: carbonCalculatorEndpoint,
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            dataType: 'json',
            data: JSON.stringify({
                origin: payload.origin,
                delivery: payload.delivery,
                weight_kg: payload.weight_kg,
                quantity: payload.quantity
            }),
            success: function (data) {
                setLoading(false);
                if (data && data.error) {
                    $('#carbonResults').hide();
                    resetCarbonResultsPanel();
                    showCalculationFailure(data.error);
                    return;
                }
                var climatiqResponse = data && data.climatiq_response ? data.climatiq_response : null;
                if (!climatiqResponse) {
                    $('#carbonResults').hide();
                    resetCarbonResultsPanel();
                    showCalculationFailure(carbonCalculatorMessages.calculationFailed);
                    return;
                }
                if (climatiqResponse.error) {
                    $('#carbonResults').hide();
                    resetCarbonResultsPanel();
                    showCalculationFailure(climatiqResponse.error);
                    return;
                }
                renderResults(climatiqResponse, payload.originLabel, payload.deliveryLabel);
            },
            error: function (xhr) {
                setLoading(false);
                if (xhr.status === 403) {
                    window.location.href = 'SessionExpired.jsp';
                    return;
                }
                $('#carbonResults').hide();
                resetCarbonResultsPanel();
                showCalculationFailure(parseErrorMessage(xhr));
            }
        });
    }

    function bindPortTypeahead(inputId, hiddenId, sourceList) {
        if (typeof Bloodhound === 'undefined' || !$.isArray(sourceList)) {
            return false;
        }

        var ports = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('PortName', 'PortCode'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: sourceList
        });
        ports.initialize();

        $('#' + inputId).typeahead({
            hint: true,
            highlight: true,
            minLength: 1
        }, {
            name: inputId + '-ports',
            limit: 20,
            displayKey: function (port) {
                return port.PortCode + ' - ' + port.PortName;
            },
            source: ports.ttAdapter()
        }).on('typeahead:selected', function (event, datum) {
            $('#' + hiddenId).val(datum.PortCode);
        }).on('typeahead:autocomplete', function (event, datum) {
            $('#' + hiddenId).val(datum.PortCode);
        });

        return true;
    }

    function resolvePortList(list) {
        if (!list) {
            return [];
        }
        if (typeof updateMaskPortList === 'function') {
            return updateMaskPortList(list);
        }
        return list;
    }

    function bindEventHandlers() {
        $('#carbonSwapBtn').off('click.carbon').on('click.carbon', function (event) {
            event.preventDefault();
            swapPorts();
        });

        $('#carbonCalcBtn').off('click.carbon').on('click.carbon', function (event) {
            event.preventDefault();
            calculateEmissions();
        });
    }

    function bindPortTypeaheads() {
        var originList = resolvePortList(window.RoutingOrigin);
        var deliveryList = resolvePortList(window.RoutingDelivery);
        var originReady = bindPortTypeahead('carbonOrigin', 'carbonOriginCode', originList);
        var deliveryReady = bindPortTypeahead('carbonDelivery', 'carbonDeliveryCode', deliveryList);
        return originReady && deliveryReady;
    }

    window.initCarbonCalculator = function () {
        bindEventHandlers();

        if (typeof window.RoutingOrigin === 'undefined' || typeof window.RoutingDelivery === 'undefined') {
            showError(carbonCalculatorMessages.portDataMissing);
            return;
        }

        try {
            bindPortTypeaheads();
        } catch (err) {
            if (window.console && console.warn) {
                console.warn('Carbon calculator port typeahead failed to initialize.', err);
            }
            showError(carbonCalculatorMessages.typeaheadFailed);
        }
    };

    /** Run calculation using current field values (e.g. after login prefill). */
    window.triggerCarbonCalculatorCalculate = function () {
        calculateEmissions();
    };

    /**
     * Prefill main calculator fields after init (e.g. from login page POST via CarbonFromLoginAction).
     * @param {string} pol Display string, typically "CODE - Name"
     * @param {string} pod Display string for delivery port
     * @param {string|number} qty TEU quantity
     * @param {string|number} weight Cargo weight (kg)
     */
    window.applyCarbonLoginPrefill = function (pol, pod, qty, weight) {
        pol = pol || '';
        pod = pod || '';

        function extractCode(raw) {
            raw = (raw || '').trim();
            if (!raw) {
                return '';
            }
            if (raw.indexOf(' - ') >= 0) {
                return raw.split(' - ')[0].trim().toUpperCase();
            }
            var dash = raw.indexOf('-');
            if (dash > 0) {
                return raw.substring(0, dash).trim().toUpperCase();
            }
            return raw.toUpperCase();
        }

        var cPol = extractCode(pol);
        var cPod = extractCode(pod);

        if ($('#carbonOrigin').data('ttTypeahead')) {
            $('#carbonOrigin').typeahead('val', pol);
        } else {
            $('#carbonOrigin').val(pol);
        }
        $('#carbonOriginCode').val(cPol);

        if ($('#carbonDelivery').data('ttTypeahead')) {
            $('#carbonDelivery').typeahead('val', pod);
        } else {
            $('#carbonDelivery').val(pod);
        }
        $('#carbonDeliveryCode').val(cPod);

        if (qty !== undefined && qty !== null && String(qty).length > 0) {
            $('#carbonQuantity').val(qty);
        }
        if (weight !== undefined && weight !== null && String(weight).length > 0) {
            $('#carbonWeight').val(weight);
        }
    };
}(window, window.jQuery || window.$));
