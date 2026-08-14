/**
 * MapBox GL JS Loader - Loads MapBox GL resources on demand.
 * Tries CDN first, falls back to local files if CDN fails or times out.
 */
var MapBoxLoader = (function () {
    var loaded = false;
    var callbacks = [];
    var MAPBOX_GL_VERSION = "v3.4.0";
    var CDN_TIMEOUT_MS = 10000;

    var JS_URL_CDN = "https://api.mapbox.com/mapbox-gl-js/" + MAPBOX_GL_VERSION + "/mapbox-gl.js";
    var CSS_URL_CDN = "https://api.mapbox.com/mapbox-gl-js/" + MAPBOX_GL_VERSION + "/mapbox-gl.css";
    var JS_URL_LOCAL = "Design/assets/plugins/mapbox/mapbox-gl.js";
    var CSS_URL_LOCAL = "Design/assets/plugins/mapbox/mapbox-gl.css";

    function loadCSS(url) {
        var link = document.createElement("link");
        link.rel = "stylesheet";
        link.href = url;
        document.head.appendChild(link);
    }

    function loadJS(url, onSuccess, onError) {
        var script = document.createElement("script");
        var timer = null;
        script.src = url;
        script.onload = function () {
            if (timer) clearTimeout(timer);
            if (onSuccess) onSuccess();
        };
        script.onerror = function () {
            if (timer) clearTimeout(timer);
            script.remove();
            if (onError) onError();
        };
        document.head.appendChild(script);
        timer = setTimeout(function () {
            script.onload = null;
            script.onerror = null;
            script.remove();
            if (onError) onError();
        }, CDN_TIMEOUT_MS);
    }

    function fireCallbacks(error) {
        for (var i = 0; i < callbacks.length; i++) {
            callbacks[i](error || null);
        }
        callbacks = [];
    }

    return {
        load: function (callback) {
            if (loaded && window.mapboxgl) {
                if (callback) callback(null);
                return;
            }
            if (callback) callbacks.push(callback);
            if (callbacks.length > 1) return;

            loadCSS(CSS_URL_CDN);
            loadJS(JS_URL_CDN, function () {
                loaded = true;
                fireCallbacks();
            }, function () {
                console.warn("[MapBoxLoader] CDN failed or timed out, loading from local files");
                loadCSS(CSS_URL_LOCAL);
                loadJS(JS_URL_LOCAL, function () {
                    loaded = true;
                    fireCallbacks();
                }, function () {
                    console.error("[MapBoxLoader] Both CDN and local files failed to load");
                    fireCallbacks("Failed to load MapBox. Please check your network connection.");
                });
            });
        },
        isLoaded: function () {
            return loaded && window.mapboxgl;
        }
    };
})();
