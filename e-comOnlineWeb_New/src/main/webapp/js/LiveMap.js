/**
 * LiveMap.js - Live Container Tracking Map (FMSC-2530)
 * Uses MapBox GL JS to display container positions from SnapGIS API.
 * Uses a custom modal instead of Fancybox to avoid drag/scroll conflicts.
 */
var LiveMap = (function () {

    var map = null;
    var markers = [];
    var modalEl = null;
    var MARKER_COLORS = [
        "#E74C3C", "#2980B9", "#27AE60", "#F39C12", "#8E44AD",
        "#1ABC9C", "#D35400", "#2C3E50", "#C0392B", "#16A085"
    ];

    function getMarkerColor(index) {
        return MARKER_COLORS[index % MARKER_COLORS.length];
    }

    function clearMarkers() {
        for (var i = 0; i < markers.length; i++) {
            markers[i].remove();
        }
        markers = [];
    }

    function formatLastUpdated(dateStr) {
        if (!dateStr || dateStr.length === 0) return "N/A";
        try {
            var d = new Date(dateStr);
            if (isNaN(d.getTime())) return dateStr;
            return d.toLocaleString();
        } catch (e) {
            return dateStr;
        }
    }

    function buildPopupHTML(container) {
        var html = '<div style="font-family:Arial,sans-serif;min-width:200px;">';
        html += '<div style="font-weight:bold;font-size:14px;color:#2C3E50;margin-bottom:6px;border-bottom:2px solid #3498DB;padding-bottom:4px;">';
        html += '<i class="fa fa-cube" style="margin-right:4px;"></i> ';
        html += (container.containerIso || container.name || "Container");
        html += '</div>';
        html += '<div style="font-size:12px;color:#555;margin-bottom:4px;">';
        html += '<strong>Lat:</strong> ' + (container.latitude || 0).toFixed(5);
        html += ' &nbsp;<strong>Lng:</strong> ' + (container.longitude || 0).toFixed(5);
        html += '</div>';
        html += '<div style="font-size:11px;color:#888;margin-top:4px;padding-top:4px;border-top:1px solid #eee;">';
        html += '<strong>Last Updated:</strong> ' + formatLastUpdated(container.lastUpdated);
        html += '</div>';
        html += '</div>';
        return html;
    }

    function closeModal() {
        clearMarkers();
        if (map) {
            map.remove();
            map = null;
        }
        if (modalEl) {
            modalEl.remove();
            modalEl = null;
        }
        $("body").css("overflow", "");
    }

    function openModal(containers, mapboxToken) {
        if (modalEl) closeModal();

        $("body").css("overflow", "hidden");

        modalEl = $('<div id="livemap-modal"></div>');
        modalEl.css({
            position: "fixed", top: 0, left: 0, width: "100%", height: "100%",
            zIndex: 99999, display: "flex", alignItems: "center", justifyContent: "center"
        });

        var overlay = $('<div></div>');
        overlay.css({
            position: "absolute", top: 0, left: 0, width: "100%", height: "100%",
            background: "rgba(0,0,0,0.6)"
        });
        modalEl.append(overlay);

        var dialog = $('<div></div>');
        dialog.css({
            position: "relative", width: "96%", height: "92vh",
            background: "#fff", borderRadius: "10px",
            boxShadow: "0 8px 30px rgba(0,0,0,0.3)", zIndex: 100000,
            padding: "15px", overflow: "hidden",
            display: "flex", flexDirection: "column"
        });

        var closeBtn = $('<button type="button">&times;</button>');
        closeBtn.css({
            position: "absolute", top: "8px", right: "12px", zIndex: 100001,
            background: "#fff", border: "1px solid #ccc", borderRadius: "50%",
            width: "30px", height: "30px", fontSize: "20px", lineHeight: "26px",
            textAlign: "center", cursor: "pointer", color: "#333",
            boxShadow: "0 1px 4px rgba(0,0,0,0.15)"
        });
        closeBtn.hover(
            function () { $(this).css({background: "#e74c3c", color: "#fff", borderColor: "#e74c3c"}); },
            function () { $(this).css({background: "#fff", color: "#333", borderColor: "#ccc"}); }
        );
        closeBtn.on("click", function (e) {
            e.stopPropagation();
            closeModal();
        });
        dialog.append(closeBtn);

        var mapDiv = $('<div id="livemap-container"></div>');
        mapDiv.css({
            width: "100%", flex: "1", borderRadius: "8px",
            border: "1px solid #ddd", marginTop: "10px", minHeight: "0"
        });
        dialog.append(mapDiv);
        dialog.append(buildLegendHTML(containers));

        modalEl.append(dialog);
        $("body").append(modalEl);

        dialog.on("mousedown mouseup mousemove click wheel touchstart touchmove", function (e) {
            e.stopPropagation();
        });

        overlay.on("click", function (e) {
            e.stopPropagation();
        });

        setTimeout(function () {
            initMap("livemap-container", mapboxToken, containers);
        }, 200);
    }

    function initMap(containerId, accessToken, containers) {
        if (!window.mapboxgl) {
            console.error("MapBox GL JS not loaded");
            return;
        }

        mapboxgl.accessToken = accessToken;

        var mapContainer = document.getElementById(containerId);
        if (!mapContainer) {
            console.error("Map container not found: " + containerId);
            return;
        }

        clearMarkers();

        if (map) {
            map.remove();
            map = null;
        }

        var initCenter = [0, 20];
        if (containers && containers.length > 0) {
            initCenter = [parseFloat(containers[0].longitude), parseFloat(containers[0].latitude)];
        }
        map = new mapboxgl.Map({
            container: containerId,
            style: "mapbox://styles/mapbox/outdoors-v12",
            center: initCenter,
            zoom: 2,
            maxZoom: 18
        });

        map.addControl(new mapboxgl.NavigationControl(), "top-left");
        map.addControl(new mapboxgl.FullscreenControl(), "top-left");

        map.on("load", function () {
            plotContainers(containers);
        });
    }

    function plotContainers(containers) {
        if (!map || !containers || containers.length === 0) return;

        clearMarkers();

        var bounds = new mapboxgl.LngLatBounds();

        for (var i = 0; i < containers.length; i++) {
            var c = containers[i];
            var lat = parseFloat(c.latitude);
            var lng = parseFloat(c.longitude);

            if (isNaN(lat) || isNaN(lng)) continue;

            var color = getMarkerColor(i);

            var el = document.createElement("div");
            el.style.width = "32px";
            el.style.height = "32px";
            el.style.borderRadius = "50% 50% 50% 0";
            el.style.background = color;
            el.style.transform = "rotate(-45deg)";
            el.style.border = "2px solid #fff";
            el.style.boxShadow = "0 2px 6px rgba(0,0,0,0.3)";
            el.style.cursor = "pointer";
            el.title = c.containerIso || c.name || ("Container " + (i + 1));

            var inner = document.createElement("div");
            inner.style.width = "100%";
            inner.style.height = "100%";
            inner.style.display = "flex";
            inner.style.alignItems = "center";
            inner.style.justifyContent = "center";
            inner.style.transform = "rotate(45deg)";
            inner.style.color = "#fff";
            inner.style.fontWeight = "bold";
            inner.style.fontSize = "12px";
            inner.textContent = (i + 1);
            el.appendChild(inner);

            var popup = new mapboxgl.Popup({offset: 25, maxWidth: "280px"})
                .setHTML(buildPopupHTML(c));

            var marker = new mapboxgl.Marker({element: el})
                .setLngLat([lng, lat])
                .setPopup(popup)
                .addTo(map);

            markers.push(marker);
            bounds.extend([lng, lat]);
        }

        if (markers.length === 1) {
            map.flyTo({center: [parseFloat(containers[0].longitude), parseFloat(containers[0].latitude)], zoom: 13});
        } else if (markers.length > 1) {
            map.fitBounds(bounds, {padding: 60, maxZoom: 13});
        }
    }

    function buildLegendHTML(containers) {
        if (!containers || containers.length <= 1) return "";
        var html = '<div style="margin-top:8px;padding:8px 12px;background:#f8f9fa;border-radius:6px;border:1px solid #dee2e6;">';
        html += '<div style="font-weight:bold;font-size:12px;margin-bottom:6px;color:#495057;">Container Legend</div>';
        for (var i = 0; i < containers.length; i++) {
            var color = getMarkerColor(i);
            html += '<div style="display:inline-block;margin-right:12px;margin-bottom:4px;">';
            html += '<span style="display:inline-block;width:14px;height:14px;border-radius:50%;background:' + color + ';vertical-align:middle;margin-right:4px;border:1px solid #fff;box-shadow:0 1px 3px rgba(0,0,0,0.2);"></span>';
            html += '<span style="font-size:12px;vertical-align:middle;color:#333;">' + (containers[i].containerIso || containers[i].name || ("Container " + (i + 1))) + '</span>';
            html += '</div>';
        }
        html += '</div>';
        return html;
    }

    return {
        openLiveMap: function (containerIsoList, mapboxToken) {
            if (!containerIsoList || containerIsoList.trim().length === 0) {
                alert("Please enter a Booking or Container number.");
                return;
            }

            openLoader();

            MapBoxLoader.load(function (loadError) {
                if (loadError) {
                    closeLoader();
                    alert(loadError);
                    return;
                }
                $.ajax({
                    type: "GET",
                    url: "LiveMap.do?containerIso=" + encodeURIComponent(containerIsoList),
                    dataType: "json",
                    success: function (data) {
                        closeLoader();
                        if (data.status === "success" && data.containers && data.containers.length > 0) {
                            openModal(data.containers, mapboxToken);
                        } else {
                            var msg = (data.message) ? data.message : "No live location data available.";
                            alert(msg);
                        }
                    },
                    error: function (xhr) {
                        closeLoader();
                        if (xhr.status === 403) {
                            window.location.href = "SessionExpired.jsp";
                        } else {
                            alert("Unable to fetch live container location. Please try again.");
                        }
                    }
                });
            });
        },

        openContainerMap: function (containerNo, mapboxToken) {
            this.openLiveMap(containerNo, mapboxToken);
        }
    };
})();
