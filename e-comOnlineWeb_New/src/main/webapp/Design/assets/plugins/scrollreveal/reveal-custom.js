
/*----------------------------------------------------
 Scroll reveal animation
 ----------------------------------------------------*/
(function($) {
"use strict";
    if (Modernizr.csstransforms3d) {
        window.sr = ScrollReveal();
        sr.reveal('.page_scrol_data', {
            duration: 500,
            delay: 200,
            reset: true,
            easing: 'linear',
            scale: 1
        });
    }
})(jQuery);