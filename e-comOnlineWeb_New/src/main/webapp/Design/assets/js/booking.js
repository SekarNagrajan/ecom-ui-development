$(function(){
    $("#booking-form").validate({
       
       
    });
    
    $("#form-total").steps({
        headerTag: "h2",
        bodyTag: "section",
        transitionEffect: "fade",
        // enableAllSteps: true,
        autoFocus: true,
        transitionEffectSpeed: 500,
        titleTemplate : '<h2 class="title">#title#</h2>',
        labels: {
            previous :'Prev',
            next : 'Next',
            finish : 'Submit',
            current : ''
        },

        onFinished: function (event, currentIndex)
        {
            alert('Submited');
        }
     
    });
});

