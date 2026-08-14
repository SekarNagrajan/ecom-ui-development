/**
 * Google address search - registration company name field only.
 * Apply class "google-address-search" to #companyname on Registration.jsp.
 */
var EcomGoogleAddressSearch = (function () {
    var AJAX_URL = window.ecomGoogleAddressAjaxUrl || 'AddressLookupAjax';
    var DELEGATION_ATTR = 'data-google-address-delegation';
    var MIN_CHARS = 2;
    var DEBOUNCE_MS = 300;
    var ACTIVE_CLASS = 'google-address-search-active';
    var BOUND_ATTR = 'data-google-address-bound';
    var debounceTimers = {};
    var pendingRequests = {};
    var activeDropdown = null;
    var activeInput = null;
    var scrollHandlerBound = false;

    function isEnabled() {
        return window.ecomGoogleAddressSearchEnabled === 'Y';
    }

    function getCompanyInput() {
        return document.getElementById('companyname');
    }

    function resolvePrefix(inputId) {
        if (!inputId) {
            return '';
        }
        if (inputId.indexOf('nameheader') === inputId.length - 'nameheader'.length) {
            return inputId.substring(0, inputId.length - 'nameheader'.length);
        }
        if (inputId.indexOf('name') === inputId.length - 'name'.length) {
            return inputId.substring(0, inputId.length - 'name'.length);
        }
        return inputId;
    }

    function setField(fieldId, value) {
        var field = document.getElementById(fieldId);
        if (field && !field.disabled) {
            field.value = value || '';
        }
    }

    function setFields(prefix, suffixes, value) {
        var i;
        for (i = 0; i < suffixes.length; i++) {
            setField(prefix + suffixes[i], value);
        }
    }

    function resolveCountryDisplay(data) {
        if (data.countryDisplay) {
            return data.countryDisplay;
        }
        if (data.countryCode && data.countryName) {
            return data.countryCode.toUpperCase() + ' - ' + data.countryName.toUpperCase();
        }
        return data.countryCode || data.countryName || '';
    }

    function digitsOnly(value) {
        return String(value || '').replace(/\D/g, '');
    }

    /**
     * Split Google place phone into registration area-code + local-number fields.
     * Examples:
     *   "044 4202 0233"      -> area 044, number 42020233
     *   "+91 44 4202 0233"   -> area 44,  number 42020233
     *   "(044) 4202-0233"    -> area 044, number 42020233
     * Country dial code is left to applyRegistrationCountryChange / setCountryDialCode.
     */
    function parseRegistrationPhone(phone) {
        var raw = String(phone || '').trim();
        if (!raw) {
            return { areaCode: '', phoneNo: '' };
        }

        var parts = raw.replace(/[()]/g, ' ').split(/[\s\-./]+/).filter(function (part) {
            return part && digitsOnly(part).length > 0;
        });

        if (!parts.length) {
            return { areaCode: '', phoneNo: '' };
        }

        // International format only: drop leading dial-code token (e.g. "+91 44 4202 0233")
        if (raw.charAt(0) === '+') {
            parts = parts.slice(1);
        }

        if (parts.length >= 2) {
            return {
                areaCode: digitsOnly(parts[0]).substring(0, 4),
                phoneNo: digitsOnly(parts.slice(1).join('')).substring(0, 10)
            };
        }

        return {
            areaCode: '',
            phoneNo: digitsOnly(parts[0] || '').substring(0, 10)
        };
    }

    function applyRegistrationPhone(phone) {
        var parsed = parseRegistrationPhone(phone);
        if (!parsed.areaCode && !parsed.phoneNo) {
            return;
        }

        if (parsed.areaCode) {
            setField('company_phone_code', parsed.areaCode);
        }
        if (parsed.phoneNo) {
            setField('company_phone_no', parsed.phoneNo);
        }

        if (typeof jQuery !== 'undefined') {
            jQuery('#company_phone_code, #company_phone_no').removeClass('error');
            jQuery('#company_phone_code, #company_phone_no').next('label.error').remove();
        }
    }

    function fillRegistrationAddress(input, data) {
        var placeName = data.placeName || input.value || '';
        var countryCode = data.countryCode || '';
        var city = data.city || '';
        var state = data.state || '';

        setField('companyname', placeName);
        setField('hiddencustname', placeName);
        setField('company_address', data.street || data.fullAddress || '');
        setField('company_address1', data.addressLine2 || '');
        setField('other_city', city);
        setField('company_postcode', data.zipcode || '');
        setField('company_state', state);
        setField('hiddenstate', state);
        setField('hiddencity', city);
        setField('hiddencountry', countryCode);

        if (city && typeof setCityDesc === 'function') {
            setCityDesc('other_city');
        }

        // Apply country first so dial code (+91 etc.) is set on company_country_code
        if (countryCode && document.getElementById('country')) {
            if (typeof applyRegistrationCountryChange === 'function') {
                applyRegistrationCountryChange(countryCode);
            } else {
                document.getElementById('country').value = countryCode;
                if (typeof jQuery !== 'undefined') {
                    jQuery('#country').trigger('change');
                }
            }
        }

        // Then map Google phone into area code + local number
        applyRegistrationPhone(data.phone);

        if (typeof setcompany === 'function') {
            setcompany('companyname');
        }
    }

    function fillAddressFields(input, data) {
        if (input.id === 'companyname') {
            fillRegistrationAddress(input, data);
            return;
        }

        var prefix = resolvePrefix(input.id);
        var countryDisplay = resolveCountryDisplay(data);
        var countryCode = data.countryCode || '';
        var city = data.city || '';
        var state = data.state || '';
        var placeName = data.placeName || input.value || '';

        setFields(prefix, ['name', 'nameheader'], placeName);
        setFields(prefix, ['address'], data.street || data.fullAddress || '');
        setFields(prefix, ['address1'], data.addressLine2 || '');
        setFields(prefix, ['city_desc', 'city'], city);
        setFields(prefix, ['state'], state);
        setFields(prefix, ['country_name'], countryDisplay);
        setFields(prefix, ['country'], countryCode);
        setFields(prefix, ['zipcode'], data.zipcode || '');
        setFields(prefix, ['phone'], data.phone || '');

        if (typeof setCountry_New === 'function') {
            setCountry_New(prefix + 'country_name', prefix + 'country');
        }
        if (typeof setCityDesc_New === 'function') {
            if (document.getElementById(prefix + 'city_desc')) {
                setCityDesc_New(prefix + 'city_desc', prefix + 'city');
            } else if (document.getElementById(prefix + 'city')) {
                setCityDesc_New(prefix + 'city', prefix + 'city_desc');
            }
        }
        if (typeof jQuery !== 'undefined') {
            if (document.getElementById(prefix + 'country_name')) {
                jQuery('#' + prefix + 'country_name').trigger('change').trigger('blur');
            }
            if (document.getElementById(prefix + 'state')) {
                jQuery('#' + prefix + 'state').typeahead('val', state);
            }
        }
    }

    function closeDropdown() {
        if (activeDropdown && activeDropdown.parentNode) {
            activeDropdown.parentNode.removeChild(activeDropdown);
        }
        activeDropdown = null;
        activeInput = null;
        document.querySelectorAll('.' + ACTIVE_CLASS).forEach(function (el) {
            el.classList.remove(ACTIVE_CLASS);
        });
    }

    function positionDropdown(input, dropdown) {
        var rect = input.getBoundingClientRect();
        dropdown.style.position = 'fixed';
        dropdown.style.left = Math.max(0, rect.left) + 'px';
        dropdown.style.top = rect.bottom + 'px';
        dropdown.style.width = Math.max(rect.width, 200) + 'px';
        dropdown.style.zIndex = '10050';
    }

    function bindScrollReposition() {
        if (scrollHandlerBound) {
            return;
        }
        scrollHandlerBound = true;
        window.addEventListener('scroll', function () {
            if (activeDropdown && activeInput) {
                positionDropdown(activeInput, activeDropdown);
            }
        }, true);
        window.addEventListener('resize', function () {
            if (activeDropdown && activeInput) {
                positionDropdown(activeInput, activeDropdown);
            }
        });
    }

    function getDropdownContainer() {
        return document.body;
    }

    function renderDropdown(input, predictions) {
        closeDropdown();
        if (!predictions || !predictions.length) {
            return;
        }

        var container = getDropdownContainer();
        var dropdown = document.createElement('div');
        dropdown.className = 'google-address-search-dropdown';
        dropdown.setAttribute('role', 'listbox');

        predictions.forEach(function (item) {
            var option = document.createElement('div');
            option.className = 'google-address-search-item';
            option.setAttribute('role', 'option');
            option.setAttribute('data-place-id', item.placeId || '');

            var mainText = item.mainText || item.description || '';
            var secondaryText = item.secondaryText || '';
            option.innerHTML = '<div class="google-address-search-main">' + escapeHtml(mainText) + '</div>'
                    + (secondaryText ? '<div class="google-address-search-secondary">' + escapeHtml(secondaryText) + '</div>' : '');

            option.addEventListener('mousedown', function (event) {
                event.preventDefault();
            });
            option.addEventListener('click', function () {
                selectPrediction(input, item);
            });
            dropdown.appendChild(option);
        });

        container.appendChild(dropdown);
        positionDropdown(input, dropdown);
        bindScrollReposition();
        input.classList.add(ACTIVE_CLASS);
        activeDropdown = dropdown;
        activeInput = input;
    }

    function escapeHtml(value) {
        return String(value || '')
                .replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;')
                .replace(/"/g, '&quot;');
    }

    function isCompanyNameField(target) {
        return target && target.nodeName === 'INPUT' && target.id === 'companyname';
    }

    function fetchAutocomplete(input, query) {
        var inputId = input.id || 'google-address-search';
        var reqId = Date.now();
        pendingRequests[inputId] = reqId;
        var url = AJAX_URL + (AJAX_URL.indexOf('?') >= 0 ? '&' : '?')
                + 'type=autocomplete&input=' + encodeURIComponent(query);

        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.setRequestHeader('Accept', 'application/json');
        xhr.onreadystatechange = function () {
            if (xhr.readyState !== 4) {
                return;
            }
            if (pendingRequests[inputId] !== reqId) {
                return;
            }
            if (xhr.status === 200) {
                try {
                    var response = JSON.parse(xhr.responseText);
                    if (response && response.success) {
                        renderDropdown(input, response.predictions || []);
                    }
                } catch (parseError) {
                    // ignore invalid response
                }
            }
        };
        xhr.send(null);
    }

    function fetchPlaceDetails(input, placeId, description) {
        jQuery.ajax({
            url: AJAX_URL,
            type: 'GET',
            dataType: 'json',
            data: {
                type: 'details',
                placeId: placeId
            },
            success: function (response) {
                if (response && response.success && response.data) {
                    if (!response.data.placeName && description) {
                        response.data.placeName = description.split(',')[0];
                    }
                    fillAddressFields(input, response.data);
                }
            },
            error: function (xhr) {
                if (xhr.status === 403) {
                    window.location.href = 'SessionExpired.jsp';
                }
            }
        });
    }

    function selectPrediction(input, item) {
        var placeId = item.placeId;
        var description = item.description || item.mainText || '';
        input.value = description.split(',')[0];
        closeDropdown();
        if (placeId) {
            fetchPlaceDetails(input, placeId, description);
        }
    }

    function handleInput(input) {
        if (!isEnabled() || input.readOnly || input.disabled) {
            closeDropdown();
            return;
        }

        var query = (input.value || '').trim();
        var timerKey = input.id || 'google-address-search';

        if (debounceTimers[timerKey]) {
            clearTimeout(debounceTimers[timerKey]);
        }

        if (query.length < MIN_CHARS) {
            closeDropdown();
            return;
        }

        debounceTimers[timerKey] = setTimeout(function () {
            if (!input || input.disabled || input.readOnly) {
                return;
            }
            var latestQuery = (input.value || '').trim();
            if (latestQuery.length < MIN_CHARS) {
                return;
            }
            activeInput = input;
            fetchAutocomplete(input, latestQuery);
        }, DEBOUNCE_MS);
    }

    function bindDocumentDelegation() {
        if (document.body.getAttribute(DELEGATION_ATTR) === 'Y') {
            return;
        }
        document.body.setAttribute(DELEGATION_ATTR, 'Y');

        document.addEventListener('input', function (event) {
            if (isCompanyNameField(event.target)) {
                handleInput(event.target);
            }
        }, true);

        document.addEventListener('keyup', function (event) {
            if (isCompanyNameField(event.target)) {
                handleInput(event.target);
            }
        }, true);

        document.addEventListener('paste', function (event) {
            if (isCompanyNameField(event.target)) {
                setTimeout(function () {
                    handleInput(event.target);
                }, 0);
            }
        }, true);

        document.addEventListener('compositionend', function (event) {
            if (isCompanyNameField(event.target)) {
                handleInput(event.target);
            }
        }, true);
    }

    function bindInput(input) {
        if (!input || input.getAttribute(BOUND_ATTR) === 'Y') {
            return;
        }

        input.setAttribute(BOUND_ATTR, 'Y');
        input.setAttribute('autocomplete', 'off');

        input.addEventListener('input', function () {
            handleInput(input);
        });
        input.addEventListener('keyup', function () {
            handleInput(input);
        });
        input.addEventListener('focus', function () {
            handleInput(input);
        });
        input.addEventListener('keydown', function (event) {
            if (event.key === 'Escape') {
                closeDropdown();
            }
        });
        input.addEventListener('blur', function () {
            setTimeout(closeDropdown, 200);
        });
    }

    function bindAll(root) {
        if (!isEnabled()) {
            return 0;
        }
        var scope = root || document;
        var inputs = scope.querySelectorAll('.google-address-search');
        var bound = 0;
        inputs.forEach(function (input) {
            var wasBound = input.getAttribute(BOUND_ATTR) === 'Y';
            bindInput(input);
            if (input.getAttribute(BOUND_ATTR) === 'Y' && !wasBound) {
                bound++;
            }
        });
        var companyInput = getCompanyInput();
        if (companyInput && !companyInput.classList.contains('google-address-search')) {
            companyInput.classList.add('google-address-search');
            bindInput(companyInput);
            bound++;
        }
        return bound;
    }

    function observeDynamicFields() {
        if (!window.MutationObserver) {
            return;
        }
        var observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                mutation.addedNodes.forEach(function (node) {
                    if (node.nodeType === 1) {
                        bindAll(node);
                    }
                });
            });
        });
        observer.observe(document.body, {childList: true, subtree: true});
    }

    function init() {
        bindDocumentDelegation();
        bindAll(document);
        if (isEnabled()) {
            observeDynamicFields();
            document.addEventListener('click', function (event) {
                if (!event.target.closest('.google-address-search')
                        && !event.target.closest('.google-address-search-dropdown')) {
                    closeDropdown();
                }
            });
        }
    }

    return {
        init: init,
        refresh: bindAll,
        onInput: handleInput
    };
})();

(function () {
    function bootGoogleAddressSearch() {
        if (window.ecomGoogleAddressSearchEnabled === 'Y' && typeof EcomGoogleAddressSearch !== 'undefined') {
            EcomGoogleAddressSearch.init();
        }
    }

    if (typeof jQuery !== 'undefined') {
        jQuery(document).ready(bootGoogleAddressSearch);
    } else if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', bootGoogleAddressSearch);
    } else {
        bootGoogleAddressSearch();
    }
})();
